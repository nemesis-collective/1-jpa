package com.qiyana;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

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
import org.qiyana.DAO.imp.LineItemDao;
import org.qiyana.DAO.imp.OrderDao;
import org.qiyana.DAO.imp.ProductDao;
import org.qiyana.model.Customer;
import org.qiyana.model.LineItem;
import org.qiyana.model.Order;
import org.qiyana.model.Product;

public class LineItemDaoTest {
  private static final String LOG_PATH = "logs/app.log";

  EntityManagerFactory etf = Persistence.createEntityManagerFactory("persistence");
  CustomerDao customerDao;
  OrderDao orderDao;
  ProductDao productDao;
  LineItemDao lineItemDao;
  Customer customer =
      new Customer(null, "QiyanaTech", "test@gmail.com", "Rua Tiberius Dourado", "123456789");

  Order order;

  Product product;

  LineItem lineItem;

  @BeforeEach
  public void tearDown(){
    customerDao = new CustomerDao(etf);
    orderDao = new OrderDao(etf);
    productDao = new ProductDao(etf);
    lineItemDao = new LineItemDao(etf);
    order = new Order(null, customer, Order.Status.PROCESSING, "Pix");
    product = new Product(null, "Hair Spray", "300 ml", 5, "dollars");
    lineItem = new LineItem(null, 2, 10, "dollars", product, order);
  }

  @Test
  void addTest_whenAddItem_mustNotThrowException() {
    customerDao.add(customer);
    productDao.add(product);
    orderDao.add(order);
    assertDoesNotThrow(() -> lineItemDao.add(lineItem));
  }

//  @Test
//  void addTest_whenAddItemFails_mustReceiveErrorMessage() {
//    EntityManagerFactory etfMock = mock(EntityManagerFactory.class);
//    EntityManager emMock = mock(EntityManager.class);
//    EntityTransaction transaction = mock(EntityTransaction.class);
//
//    when(etfMock.createEntityManager()).thenReturn(emMock);
//    when(emMock.getTransaction()).thenReturn(transaction);
//    doThrow(new IllegalArgumentException()).when(emMock).persist(any());
//
//    LineItemDao lineItemDao1 = new LineItemDao(etfMock);
//
//    assertDoesNotThrow(() -> lineItemDao1.add(lineItem));
//  }

  @Test
  void getTest_whenItemIdIsValid_mustReturnItem() {
    customerDao.add(customer);
    productDao.add(product);
    orderDao.add(order);
    lineItemDao.add(lineItem);
    assertNotNull(lineItemDao.get(1L));
  }

//  @Test
//  void getTest_whenItemIdIsInvalid_mustNotThrowException() throws IOException {
//    lineItemDao.get(5L);
//    final List<String> logLines = Files.readAllLines(Paths.get(LOG_PATH));
//    final String logContent = String.join("\n", logLines);
//    assertTrue(logContent.contains("Unable to get item."));
//  }

  @Test
  void getAllTest_mustReturnAllItems() {
    assertDoesNotThrow(() -> lineItemDao.getAll());
  }

//  @Test
//  void getAllTest_whenOccursException_mustReceiveErrorMessage() throws IOException {
//    EntityManagerFactory etfMock = mock(EntityManagerFactory.class);
//    EntityManager emMock = mock(EntityManager.class);
//    EntityTransaction transaction = mock(EntityTransaction.class);
//    CriteriaBuilder cb = mock(CriteriaBuilder.class);
//
//    when(etfMock.createEntityManager()).thenReturn(emMock);
//    when(emMock.getTransaction()).thenReturn(transaction);
//    when(emMock.getCriteriaBuilder()).thenReturn(cb);
//    when(cb.createQuery()).thenThrow(new IllegalArgumentException());
//
//    LineItemDao lineItemDao1 = new LineItemDao(etfMock);
//    lineItemDao1.getAll();
//    final List<String> logLines = Files.readAllLines(Paths.get(LOG_PATH));
//    final String logContent = String.join("\n", logLines);
//
//    assertTrue(logContent.contains("Unable to get all items."));
//  }

  @Test
  void updateTest_whenItemIsValid_mustNotThrowException() {
    lineItemDao.add(lineItem);
    LineItem lineItem3 = new LineItem(3L, 3, 15, "dollars", product, order);
    assertDoesNotThrow(() -> lineItemDao.update(lineItem3));
  }

//  @Test
//  void updateTest_whenItemIsInvalid_mustReceiveErrorMessage() throws IOException {
//    customerDao.add(customer);
//    productDao.add(product);
//    orderDao.add(order);
//    LineItem lineItem1 = new LineItem(5L, 3, 15, "dollars", product, order);
//    lineItemDao.update(lineItem1);
//    final List<String> logLines = Files.readAllLines(Paths.get(LOG_PATH));
//    final String logContent = String.join("\n", logLines);
//    assertTrue(logContent.contains("Unable to update item."));
//  }

  @Test
  void deleteTest_whenItemIdIsValid_mustNotThrowException() {
    lineItemDao.add(lineItem);
    assertDoesNotThrow(() -> lineItemDao.delete(1L));
  }

//  @Test
//  void deleteTest_whenItemIdIsInvalid_mustReceiveErrorMessage() throws IOException {
//    lineItemDao.delete(5L);
//    final List<String> logLines = Files.readAllLines(Paths.get(LOG_PATH));
//    final String logContent = String.join("\n", logLines);
//
//    assertTrue(logContent.contains("Unable to delete item."));
//  }
}
