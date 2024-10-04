package com.qiyana;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.junit.jupiter.api.*;
import org.qiyana.DAO.imp.CustomerDao;
import org.qiyana.DAO.imp.LineItemDao;
import org.qiyana.DAO.imp.OrderDao;
import org.qiyana.DAO.imp.ProductDao;
import org.qiyana.model.Customer;
import org.qiyana.model.LineItem;
import org.qiyana.model.Order;
import org.qiyana.model.Product;

public class LineItemDaoTest {
  static EntityManagerFactory etf;
  static EntityManagerFactory etfMock;
  static EntityManager emMock;
  static EntityTransaction transaction;

  static ProductDao productDao;
  static CustomerDao customerDao;
  static LineItemDao lineItemDao;
  static LineItemDao lineItemDaoMock;
  static OrderDao orderDao;

  static Product product;
  static Customer customer;
  static Order order;
  static LineItem lineItem;

  @BeforeAll
  public static void init() {
    etf = Persistence.createEntityManagerFactory("persistence");
    productDao = new ProductDao(etf);
    etfMock = mock(EntityManagerFactory.class);
    transaction = mock(EntityTransaction.class);
    emMock = mock(EntityManager.class);

    customer = new Customer("QiyanaTech", "test@gmail.com", "Rua Tiberius Dourado", "123456789");
    order = new Order(customer, Order.Status.PROCESSING);
    product = new Product("Hair Spray", "300 ml", "5", "USD");

    customerDao = new CustomerDao(etf);
    orderDao = new OrderDao(etf);
    productDao = new ProductDao(etf);
    lineItemDao = new LineItemDao(etf);

    customerDao.add(customer);
    productDao.add(product);
    orderDao.add(order);

    when(etfMock.createEntityManager()).thenReturn(emMock);
    when(emMock.getTransaction()).thenReturn(transaction);

    lineItemDaoMock = new LineItemDao(etfMock);
  }

  @BeforeEach
  public void setUp() {
    lineItem = new LineItem(2, "USD", product, order);
    lineItemDao.add(lineItem);
  }

  @Test
  void addTest_whenItemCorrectlyAdded_mustNotThrowException() {
    assertDoesNotThrow(() -> lineItemDao.add(lineItem));
  }

  @Test
  void addTest_mustThrowException() {
    doThrow(new IllegalArgumentException()).when(emMock).persist(any());
    assertThrows(IllegalArgumentException.class, () -> lineItemDaoMock.add(lineItem));
  }

  @Test
  void getTest_mustReturnItem() {
    assertNotNull(lineItemDao.get(lineItem.getId()));
  }

  @Test
  void getTest_mustThrowException() {
    doThrow(new IllegalArgumentException()).when(emMock).find(any(), any());
    assertThrows(IllegalArgumentException.class, () -> lineItemDaoMock.get(lineItem.getId()));
  }

  @Test
  void getAllTest_mustReturnAllItems() {
    assertNotNull(lineItemDao.getAll());
  }

  @Test
  void getAllTest_mustThrowException() {
    when(emMock.getCriteriaBuilder()).thenThrow(new IllegalArgumentException());
    assertThrows(IllegalArgumentException.class, () -> lineItemDaoMock.getAll());
  }

  @Test
  void updateTest_whenItemCorrectlyUpdated_mustNotThrowException() {
    LineItem lineItemToUpdate = lineItemDao.get(lineItem.getId());
    lineItemToUpdate.setQuantity(4);
    lineItemDao.update(lineItemToUpdate);
    assertEquals(4, lineItemDao.get(lineItem.getId()).getQuantity());
  }

  @Test
  void updateTest_mustThrowException() {
    when(emMock.merge(any())).thenThrow(new IllegalArgumentException());
    assertThrows(IllegalArgumentException.class, () -> lineItemDaoMock.update(lineItem));
  }

  @Test
  void deleteTest_whenItemCorrectlyDeleted_mustNotThrowException() {
    assertDoesNotThrow(() -> lineItemDao.delete(lineItem.getId()));
  }

  @Test
  void deleteTest_whenItemIdIsInvalid_mustReceiveErrorMessage() {
    doThrow(new IllegalArgumentException()).when(emMock).remove(any());
    assertThrows(IllegalArgumentException.class, () -> lineItemDaoMock.delete(lineItem.getId()));
  }
}
