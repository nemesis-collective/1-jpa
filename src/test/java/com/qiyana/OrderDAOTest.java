package com.qiyana;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.junit.jupiter.api.*;
import org.qiyana.DAO.imp.CustomerDao;
import org.qiyana.DAO.imp.OrderDao;
import org.qiyana.DAO.imp.ProductDao;
import org.qiyana.model.Customer;
import org.qiyana.model.LineItem;
import org.qiyana.model.Order;
import org.qiyana.model.Product;

public class OrderDAOTest {

  static EntityManagerFactory etf;
  static OrderDao orderDao;
  static CustomerDao customerDao;
  static ProductDao productDao;
  static EntityManagerFactory etfMock;
  static EntityManager emMock;
  static EntityTransaction transaction;
  static Customer customer;
  static Order order;
  static Product product;
  static OrderDao orderDaoMock;
  static LineItem lineItem;

  @BeforeAll
  public static void init() {
    etf = Persistence.createEntityManagerFactory("persistence");

    customerDao = new CustomerDao(etf);
    productDao = new ProductDao(etf);
    orderDao = new OrderDao(etf);

    customer =
        new Customer("QiyanaTech", "test@gmail.com", "Rua Tiberius Dourado", "123456789");
    customerDao.add(customer);

    product = new Product("Hair Spray", "300 ml", "5", "USD");
    productDao.add(product);

    etfMock = mock(EntityManagerFactory.class);
    transaction = mock(EntityTransaction.class);
    emMock = mock(EntityManager.class);
    when(etfMock.createEntityManager()).thenReturn(emMock);
    when(emMock.getTransaction()).thenReturn(transaction);
    orderDaoMock = new OrderDao(etfMock);
  }

  @BeforeEach
  public void setUp() {
    order = new Order(customer, Order.Status.PROCESSING);
    lineItem = new LineItem(2,"USD",product,order);
    order.addLineItem(lineItem);
    orderDao.add(order);
    System.out.println(order.getTotalOrderPrice());
  }

  @Test
  void addTest_whenOrderCorrectlyAdded_mustNotThrowException() {
    assertDoesNotThrow(() -> orderDao.add(order));
  }

  @Test
  void addTest_mustThrowException() {
    doThrow(new IllegalArgumentException()).when(emMock).persist(any());
    assertThrows(IllegalArgumentException.class, () -> orderDaoMock.add(order));
  }

  @Test
  void getTest_mustReturnOrder() {
    assertNotNull(orderDao.get(order.getId()));
  }

  @Test
  void getTest_mustThrowException() {
    doThrow(new IllegalArgumentException()).when(emMock).find(any(), any());
    assertThrows(IllegalArgumentException.class, () -> orderDaoMock.get(order.getId()));
  }

  @Test
  void getAllTest_mustReturnAllOrders() {
    assertDoesNotThrow(() -> orderDao.getAll());
  }

  @Test
  void getAllTest_mustThrowException() {
    when(emMock.getCriteriaBuilder()).thenThrow(new IllegalArgumentException());
    assertThrows(IllegalArgumentException.class, orderDaoMock::getAll);
  }

  @Test
  void updateTest_whenOrderCorrectlyUpdate_mustNotThrowException() {
    Order updatedOrder = new Order(customer, Order.Status.DELIVERED);
    assertDoesNotThrow(() -> orderDao.update(updatedOrder));
  }

  @Test
  void updateTest_mustThrowException() {
    when(emMock.merge(any())).thenThrow(new IllegalArgumentException());
    assertThrows(IllegalArgumentException.class, () -> orderDaoMock.update(order));
  }

  @Test
  void deleteTest_whenOrderCorrectlyDeleted_mustNotThrowException() {
    assertDoesNotThrow(() -> orderDao.delete(order.getId()));
  }

  @Test
  void deleteTest_shouldThrowException() {
    doThrow(new IllegalArgumentException()).when(emMock).remove(any());
    assertThrows(IllegalArgumentException.class, () -> orderDaoMock.delete(order.getId()));
  }
}
