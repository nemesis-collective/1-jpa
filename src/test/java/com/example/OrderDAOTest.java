package com.example;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;

import org.example.imp.CustomerDao;
import org.example.imp.OrderDao;
import org.example.imp.ProductDao;
import org.example.model.Customer;
import org.example.model.LineItem;
import org.example.model.Order;
import org.example.model.Product;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderDAOTest {

  private static final String LOG_PATH = "logs/app.log";

  EntityManagerFactory etf = Persistence.createEntityManagerFactory("persistence");
  OrderDao orderDao = new OrderDao(etf);
  CustomerDao customerDao = new CustomerDao(etf);
  ProductDao productDao = new ProductDao(etf);
  Customer customer =
      new Customer(null, "QiyanaTech", "test@gmail.com", "Rua Tiberius Dourado", "123456789");

  Order order = new Order(null, customer, "pending", "Pix");

  Product product = new Product(null, "Hair Spray", "300 ml", 5, "dollars");

  LineItem lineItem = new LineItem(null, 2, 10, "dollars", product, order);

  @AfterEach
  public void tearDown() throws IOException {
    Files.write(
        Paths.get(LOG_PATH),
        new byte[0],
        StandardOpenOption.TRUNCATE_EXISTING,
        StandardOpenOption.CREATE);
  }

  @Test
  @org.junit.jupiter.api.Order(1)
  void addTest_whenAddAnOrder_mustNotThrowException() {
    order.addLineItem(lineItem);
    customerDao.add(customer);
    productDao.add(product);
    assertDoesNotThrow(() -> orderDao.add(order));
  }

  @Test
  @org.junit.jupiter.api.Order(2)
  void addTest_whenAddAnOrderFails_mustNotThrowException() {
    EntityManagerFactory etfMock = mock(EntityManagerFactory.class);
    EntityManager emMock = mock(EntityManager.class);
    EntityTransaction transaction = mock(EntityTransaction.class);

    when(etfMock.createEntityManager()).thenReturn(emMock);
    when(emMock.getTransaction()).thenReturn(transaction);
    doThrow(new IllegalArgumentException()).when(emMock).persist(any());

    OrderDao orderDaoMock = new OrderDao(etfMock);

    assertDoesNotThrow(() -> orderDaoMock.add(order));
  }

  @Test
  @org.junit.jupiter.api.Order(3)
  void getTest_whenOrderIdIsValid_mustReturnOrder() {
    assertNotNull(orderDao.get(1L));
  }

  @Test
  @org.junit.jupiter.api.Order(4)
  void getTest_whenOrderIdIsInvalid_mustNotThrowException() throws IOException {
     orderDao.get(5L);
    final List<String> logLines = Files.readAllLines(Paths.get(LOG_PATH));
    final String logContent = String.join("\n", logLines);
    assertTrue(logContent.contains("Unable to get order."));
  }

  @Test
  @org.junit.jupiter.api.Order(5)
  void getAllTest_mustReturnAllOrders() {
    assertDoesNotThrow(() -> orderDao.getAll());
  }

  @Test
  @org.junit.jupiter.api.Order(6)
  void getAllTest_whenOccursException_mustNotThrow() throws IOException {
    EntityManagerFactory etfMock = mock(EntityManagerFactory.class);
    EntityManager emMock = mock(EntityManager.class);
    EntityTransaction transaction = mock(EntityTransaction.class);
    CriteriaBuilder cb = mock(CriteriaBuilder.class);

    when(etfMock.createEntityManager()).thenReturn(emMock);
    when(emMock.getTransaction()).thenReturn(transaction);
    when(emMock.getCriteriaBuilder()).thenReturn(cb);
    when(cb.createQuery()).thenThrow(new IllegalArgumentException());

    OrderDao orderDaoMock = new OrderDao(etfMock);
    orderDaoMock.getAll();
    final List<String> logLines = Files.readAllLines(Paths.get(LOG_PATH));
    final String logContent = String.join("\n", logLines);

    assertTrue(logContent.contains("Unable to get all orders."));
  }

  @Test
  @org.junit.jupiter.api.Order(7)
  void updateTest_whenOrderIsValid_mustNotThrowException() {
    Order order1 = new Order(1L, customer, "processing", "Credit Card");
    assertDoesNotThrow(() -> orderDao.update(order1));
  }

  @Test
  @org.junit.jupiter.api.Order(8)
  void updateTest_whenOrderIsInvalid_mustReceiveErrorMessage() throws IOException {
    Order order1 = new Order(3L, customer, "processing", "Ticket");
    orderDao.update(order1);
    final List<String> logLines = Files.readAllLines(Paths.get(LOG_PATH));
    final String logContent = String.join("\n", logLines);
    assertTrue(logContent.contains("Unable to update order."));
  }

  @Test
  @org.junit.jupiter.api.Order(9)
  void deleteTest_whenOrderIdIsValid_mustNotThrowException() {
    assertDoesNotThrow(() -> orderDao.delete(1L));
  }

  @Test
  @org.junit.jupiter.api.Order(10)
  void deleteTest_whenOrderIdIsInvalid_mustReceiveErrorMessage() throws IOException {
    orderDao.delete(5L);
    final List<String> logLines = Files.readAllLines(Paths.get(LOG_PATH));
    final String logContent = String.join("\n", logLines);

    assertTrue(logContent.contains("Unable to delete order."));
  }
}
