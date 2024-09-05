package com.example;

import org.example.imp.ProductDao;
import org.example.model.Product;
import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductDaoTest {
    private static final String LOG_PATH = "logs/app.log";

    EntityManagerFactory etf = Persistence.createEntityManagerFactory("persistence");
    ProductDao productDao = new ProductDao(etf);
    Product product =
      new Product(null, "Hair Spray", "300 ml", 5, "dollars");

    @BeforeEach
    public void tearDown() throws IOException {
        Files.write(
                Paths.get(LOG_PATH),
                new byte[0],
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.CREATE);
    }

    @Test
    @Order(1)
    void addTest_whenAddProduct_mustNotThrowException() {
        assertDoesNotThrow(() -> productDao.add(product));
    }

    @Test
    @Order(2)
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
    @Order(3)
    void getTest_whenProductIdIsValid_mustReturnProduct() {
        assertNotNull(productDao.get(1L));
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    void getTest_whenProductIdIsInvalid_mustReceiveErrorMessage() throws IOException {
        productDao.get(2L);
        final List<String> logLines = Files.readAllLines(Paths.get(LOG_PATH));
        final String logContent = String.join("\n", logLines);
        assertTrue(logContent.contains("Unable to get product."));
    }

    @Test
    @Order(5)
    void getAllTest_mustReturnAllCustomer() {
        assertDoesNotThrow(() -> productDao.getAll());
    }

    @Test
    @Order(6)
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
    @Order(7)
    void updateTest_whenProductIsValid_mustNotThrowException() {
    Product product1 =
        new Product(1L, "Vodka", "1000ml", 10, "real");
        assertDoesNotThrow(() -> productDao.update(product1));
    }

    @Test
    @Order(8)
    void updateTest_whenCustomerIsInvalid_mustReceiveErrorMessage() throws IOException {
        Product product1 =
                new Product(2L, "Vodka", "500ml", 10, "real");
        productDao.update(product1);
        final List<String> logLines = Files.readAllLines(Paths.get(LOG_PATH));
        final String logContent = String.join("\n", logLines);
        assertTrue(logContent.contains("Unable to update product."));
    }

    @Test
    @Order(9)
    void deleteTest_whenProductIdIsValid_mustNotThrowException(){
        assertDoesNotThrow(() -> productDao.delete(1L));
    }

    @Test
    @Order(10)
    void deleteTest_whenProductIdIsInvalid_mustReceiveErrorMessage() throws IOException {
        productDao.delete(2L);
        final List<String> logLines = Files.readAllLines(Paths.get(LOG_PATH));
        final String logContent = String.join("\n", logLines);

        assertTrue(logContent.contains("Unable to delete product."));
    }
}
