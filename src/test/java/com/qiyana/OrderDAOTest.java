package com.qiyana;

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
import org.junit.jupiter.api.*;
import org.qiyana.DAO.imp.CustomerDao;
import org.qiyana.DAO.imp.OrderDao;
import org.qiyana.DAO.imp.ProductDao;
import org.qiyana.model.Customer;
import org.qiyana.model.LineItem;
import org.qiyana.model.Order;
import org.qiyana.model.Product;

public class OrderDAOTest {

  private static final String LOG_PATH = "logs/app.log";

  EntityManagerFactory etf;
  OrderDao orderDao;
  CustomerDao customerDao;
  ProductDao productDao;

  Customer customer;
  Order order;
  Product product;
  LineItem lineItem;

  @BeforeEach
  public void tearDown() throws IOException {
    Files.write(
        Paths.get(LOG_PATH),
        new byte[0],
        StandardOpenOption.TRUNCATE_EXISTING,
        StandardOpenOption.CREATE);
    etf = Persistence.createEntityManagerFactory("persistence");
    orderDao = new OrderDao(etf);
    customerDao = new CustomerDao(etf);
    productDao = new ProductDao(etf);
    customer =
        new Customer(null, "QiyanaTech", "test@gmail.com", "Rua Tiberius Dourado", "123456789");
    order = new Order(null, customer, Order.Status.PROCESSING, "Pix");
    product = new Product(null, "Hair Spray", "300 ml", 5, "dollars");
    lineItem = new LineItem(null, 2, 10, "dollars", product, order);
  }

  @Test
  void addTest_whenAddAnOrder_mustNotThrowException() {
    order.addLineItem(lineItem);
    customerDao.add(customer);
    productDao.add(product);
    assertDoesNotThrow(() -> orderDao.add(order));
  }

  @Test
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
  void getTest_whenOrderIdIsValid_mustReturnOrder() {
    assertNotNull(orderDao.get(1L));
  }

  @Test
  void getTest_whenOrderIdIsInvalid_mustNotThrowException() throws IOException {
    orderDao.get(10L);
    final List<String> logLines = Files.readAllLines(Paths.get(LOG_PATH));
    final String logContent = String.join("\n", logLines);
    assertTrue(logContent.contains("Unable to get order."));
  }

  @Test
  void getAllTest_mustReturnAllOrders() {
    assertDoesNotThrow(() -> orderDao.getAll());
  }

  @Test
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
  void updateTest_whenOrderIsValid_mustNotThrowException() {
    Order order1 = new Order(1L, customer, Order.Status.DELIVERED, "Credit Card");
    assertDoesNotThrow(() -> orderDao.update(order1));
  }

  @Test
  void updateTest_whenOrderIsInvalid_mustReceiveErrorMessage() throws IOException {
    Order order1 = new Order(10L, customer, Order.Status.SHIPPED, "Ticket");
    orderDao.update(order1);
    final List<String> logLines = Files.readAllLines(Paths.get(LOG_PATH));
    final String logContent = String.join("\n", logLines);
    assertTrue(logContent.contains("Unable to update order."));
  }

  @Test
  void deleteTest_whenOrderIdIsValid_mustNotThrowException() {
    assertDoesNotThrow(() -> orderDao.delete(1L));
  }

  @Test
  void deleteTest_whenOrderIdIsInvalid_mustReceiveErrorMessage() throws IOException {
    orderDao.delete(10L);
    final List<String> logLines = Files.readAllLines(Paths.get(LOG_PATH));
    final String logContent = String.join("\n", logLines);

    assertTrue(logContent.contains("Unable to delete order."));
  }
}
