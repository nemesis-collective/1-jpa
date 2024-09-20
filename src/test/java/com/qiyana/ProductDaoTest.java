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
import org.qiyana.DAO.imp.ProductDao;
import org.qiyana.model.Product;

public class ProductDaoTest {
  private static final String LOG_PATH = "logs/app.log";

  EntityManagerFactory etf = Persistence.createEntityManagerFactory("persistence");
  ProductDao productDao = new ProductDao(etf);
  Product product = new Product(null, "Hair Spray", "300 ml", 5, "dollars");

  @BeforeEach
  public void tearDown() throws IOException {
    Files.write(
        Paths.get(LOG_PATH),
        new byte[0],
        StandardOpenOption.TRUNCATE_EXISTING,
        StandardOpenOption.CREATE);
  }

  @Test
  void addTest_whenAddProduct_mustNotThrowException() {
    assertDoesNotThrow(() -> productDao.add(product));
  }

  @Test
  void addOrderTest_whenAddCustomerFails_mustReceiveErrorMessage() {
    EntityManagerFactory etfMock = mock(EntityManagerFactory.class);
    EntityManager emMock = mock(EntityManager.class);
    EntityTransaction transaction = mock(EntityTransaction.class);

    when(etfMock.createEntityManager()).thenReturn(emMock);
    when(emMock.getTransaction()).thenReturn(transaction);
    doThrow(new IllegalArgumentException()).when(emMock).persist(any());

    ProductDao productDaoMock1 = new ProductDao(etfMock);

    assertDoesNotThrow(() -> productDaoMock1.add(product));
  }

  @Test
  void getTest_whenProductIdIsValid_mustReturnProduct() {
    productDao.add(product);
    assertNotNull(productDao.get(1L));
  }

  @Test
  void getTest_whenProductIdIsInvalid_mustReceiveErrorMessage() throws IOException {
    productDao.get(10L);
    final List<String> logLines = Files.readAllLines(Paths.get(LOG_PATH));
    final String logContent = String.join("\n", logLines);
    assertTrue(logContent.contains("Unable to get product."));
  }

  @Test
  void getAllTest_mustReturnAllCustomer() {
    assertDoesNotThrow(() -> productDao.getAll());
  }

  @Test
  void getAllTest_whenOccursException_mustReceiveErrorMessage() throws IOException {
    EntityManagerFactory etfMock = mock(EntityManagerFactory.class);
    EntityManager emMock = mock(EntityManager.class);
    EntityTransaction transaction = mock(EntityTransaction.class);
    CriteriaBuilder cb = mock(CriteriaBuilder.class);

    when(etfMock.createEntityManager()).thenReturn(emMock);
    when(emMock.getTransaction()).thenReturn(transaction);
    when(emMock.getCriteriaBuilder()).thenReturn(cb);
    when(cb.createQuery()).thenThrow(new IllegalArgumentException());

    ProductDao productDao1 = new ProductDao(etfMock);
    productDao1.getAll();
    final List<String> logLines = Files.readAllLines(Paths.get(LOG_PATH));
    final String logContent = String.join("\n", logLines);

    assertTrue(logContent.contains("Unable to get all products."));
  }

  @Test
  void updateTest_whenProductIsValid_mustNotThrowException() {
    productDao.add(product);
    Product product1 = new Product(3L, "Vodka", "1000ml", 10, "real");
    assertDoesNotThrow(() -> productDao.update(product1));
  }

  @Test
  void updateTest_whenProductIsInvalid_mustReceiveErrorMessage() throws IOException {
    Product product1 = new Product(10L, "Vodka", "500ml", 10, "real");
    productDao.update(product1);
    final List<String> logLines = Files.readAllLines(Paths.get(LOG_PATH));
    final String logContent = String.join("\n", logLines);
    assertTrue(logContent.contains("Unable to update product."));
  }

  @Test
  void deleteTest_whenProductIdIsValid_mustNotThrowException() {
    assertDoesNotThrow(() -> productDao.delete(1L));
  }

  @Test
  void deleteTest_whenProductIdIsInvalid_mustReceiveErrorMessage() throws IOException {
    productDao.delete(10L);
    final List<String> logLines = Files.readAllLines(Paths.get(LOG_PATH));
    final String logContent = String.join("\n", logLines);

    assertTrue(logContent.contains("Unable to delete product."));
  }
}
