package com.qiyana;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.junit.jupiter.api.*;
import org.qiyana.DAO.imp.ProductDao;
import org.qiyana.model.Product;

public class ProductDaoTest {
  static EntityManagerFactory etf;
  static EntityManagerFactory etfMock;
  static EntityManager emMock;
  static EntityTransaction transaction;
  static ProductDao productDao;
  static ProductDao productDaoMock;
  static Product product;

  @BeforeAll
  public static void init() {
    etf = Persistence.createEntityManagerFactory("persistence");
    productDao = new ProductDao(etf);
    etfMock = mock(EntityManagerFactory.class);
    transaction = mock(EntityTransaction.class);
    emMock = mock(EntityManager.class);
    when(etfMock.createEntityManager()).thenReturn(emMock);
    when(emMock.getTransaction()).thenReturn(transaction);
    productDaoMock = new ProductDao(etfMock);
  }

  @BeforeEach
  public void setUp() {
    product = new Product("Hair Spray", "300 ml", "5", "USD");
    productDao.add(product);
  }

  @Test
  void addTest_whenProductAddedCorrectly_mustNotThrowException() {
    assertDoesNotThrow(() -> productDao.add(product));
  }

  @Test
  void addTest_shouldThrowException() {
    doThrow(new IllegalArgumentException()).when(emMock).persist(any());
    assertThrows(IllegalArgumentException.class, () -> productDaoMock.add(product));
  }

  @Test
  void getTest_mustReturnProduct() {
    assertNotNull(productDao.get(product.getId()));
  }

  @Test
  void getTest_mustThrowException() {
    doThrow(new IllegalArgumentException()).when(emMock).find(any(), any());
    assertThrows(IllegalArgumentException.class, () -> productDaoMock.get(product.getId()));
  }

  @Test
  void getAllTest_mustReturnAllProducts() {
    assertNotNull(productDao.getAll());
  }

  @Test
  void getAllTest_mustThrowException() {
    when(emMock.getCriteriaBuilder()).thenThrow(new IllegalArgumentException());
    assertThrows(IllegalArgumentException.class, () -> productDaoMock.getAll());
  }

  @Test
  void updateTest_whenProductCorrectlyUpdated_mustNotThrowException() {
    Product updatedProduct = new Product("Vodka", "1000ml", "10", "BRL");
    assertDoesNotThrow(() -> productDao.update(updatedProduct));
  }

  @Test
  void updateTest_mustThrowException() {
    when(emMock.merge(any())).thenThrow(new IllegalArgumentException());
    assertThrows(IllegalArgumentException.class, () -> productDaoMock.update(product));
  }

  @Test
  void deleteTest_whenProductCorrectlyDeleted_mustNotThrowException() {
    assertDoesNotThrow(() -> productDao.delete(product.getId()));
  }

  @Test
  void deleteTest_mustThrowException() {
    doThrow(new IllegalArgumentException()).when(emMock).remove(any());
    assertThrows(IllegalArgumentException.class, () -> productDaoMock.delete(product.getId()));
  }
}
