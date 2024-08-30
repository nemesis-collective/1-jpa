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
import javax.persistence.criteria.CriteriaQuery;

import org.example.imp.OrderDao;
import org.example.model.Customer;
import org.example.model.LineItem;
import org.example.model.Order;
import org.example.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderDAOTest {

  private static final String LOG_PATH = "logs/app.log";
  static Integer orderId = 1;

  EntityManagerFactory etf = Persistence.createEntityManagerFactory("persistence");
  OrderDao orderDao = new OrderDao(etf);

  EntityManagerFactory etfMock = mock(EntityManagerFactory.class);
  EntityManager emMock = mock(EntityManager.class);
  EntityTransaction transaction = mock(EntityTransaction.class);
  OrderDao orderDaoMock = new OrderDao(etfMock);
  CriteriaBuilder cbMock = mock(CriteriaBuilder.class);

  Customer customer =
      new Customer(null, "QiyanaTech", "test@gmail.com", "Rua Tiberius Dourado", "123456789");
  Product product = new Product(null, "Hair Spray", "300 ml", 5, "dollars");
  Order order = new Order(null, customer);
  LineItem lineItem = new LineItem(null, 2, 10, "dollars", product, order);

  @BeforeEach
  public void tearDown() throws IOException {
    Files.write(
            Paths.get(LOG_PATH),
            new byte[0],
            StandardOpenOption.TRUNCATE_EXISTING,
            StandardOpenOption.CREATE);
  }

  @Test
  @org.junit.jupiter.api.Order(1)
  void addOrderTest_whenAddAnOrder_mustNotThrowException() {
    order.addLineItem(lineItem);
    assertDoesNotThrow(() -> orderDao.add(order));
  }

  @Test
  void
  addOrderTest_whenAddAnOrderFails_mustNotThrowException() {

    when(etfMock.createEntityManager()).thenReturn(emMock);
    when(emMock.getTransaction()).thenReturn(transaction);

    doThrow(new IllegalArgumentException()).when(emMock).persist(any());

    assertDoesNotThrow(
        () -> {
          orderDaoMock.add(order);
        });
  }

  @Test
  @org.junit.jupiter.api.Order(5)
  void getAllTest_mustReturnAllOrders() {
    List<Order> list = List.of();
    assertNotEquals(list, orderDao.getAll());
  }

  @Test
  @org.junit.jupiter.api.Order(6)
  void deleteTest_whenOrderIdIsValid_mustNotThrowException() throws IOException {
    orderDao.delete(1L);
    final List<String> logLines = Files.readAllLines(Paths.get(LOG_PATH));
    final String logContent = String.join("\n", logLines);
    assertFalse(logContent.contains("Unable to delete order."));
  }
}
