package com.example;

import org.example.imp.CustomerDao;
import org.example.imp.LineItemDao;
import org.example.imp.OrderDao;
import org.example.imp.ProductDao;
import org.example.model.Customer;
import org.example.model.LineItem;
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
public class LineItemDaoTest {
    private static final String LOG_PATH = "logs/app.log";

    EntityManagerFactory etf = Persistence.createEntityManagerFactory("persistence");
    CustomerDao customerDao = new CustomerDao(etf);
    OrderDao orderDao = new OrderDao(etf);
    ProductDao productDao = new ProductDao(etf);
    LineItemDao lineItemDao = new LineItemDao(etf);
    Customer customer =
            new Customer(null, "QiyanaTech", "test@gmail.com", "Rua Tiberius Dourado", "123456789");

    org.example.model.Order order = new org.example.model.Order(null, customer, "pending", "Pix");

    Product product = new Product(null, "Hair Spray", "300 ml", 5, "dollars");

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
    @Order(1)
    void addTest_whenAddItem_mustNotThrowException() {
        customerDao.add(customer);
        productDao.add(product);
        orderDao.add(order);
        assertDoesNotThrow(() -> lineItemDao.add(lineItem));
    }

    @Test
    @Order(2)
    void addTest_whenAddItemFails_mustReceiveErrorMessage() {
        EntityManagerFactory etfMock = mock(EntityManagerFactory.class);
        EntityManager emMock = mock(EntityManager.class);
        EntityTransaction transaction = mock(EntityTransaction.class);

        when(etfMock.createEntityManager()).thenReturn(emMock);
        when(emMock.getTransaction()).thenReturn(transaction);
        doThrow(new IllegalArgumentException()).when(emMock).persist(any());

        LineItemDao lineItemDao1 = new LineItemDao(etfMock);

        assertDoesNotThrow(() -> lineItemDao1.add(lineItem));
    }

    @Test
    @Order(3)
    void getTest_whenItemIdIsValid_mustReturnItem() {
        assertNotNull(lineItemDao.get(1L));
    }

    @Test
    @Order(4)
    void getTest_whenItemIdIsInvalid_mustNotThrowException() throws IOException {
        lineItemDao.get(2L);
        final List<String> logLines = Files.readAllLines(Paths.get(LOG_PATH));
        final String logContent = String.join("\n", logLines);
        assertTrue(logContent.contains("Unable to get item."));
    }

    @Test
    @Order(5)
    void getAllTest_mustReturnAllItems() {
        assertDoesNotThrow(() -> lineItemDao.getAll());
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

        LineItemDao lineItemDao1 = new LineItemDao(etfMock);
        lineItemDao1.getAll();
        final List<String> logLines = Files.readAllLines(Paths.get(LOG_PATH));
        final String logContent = String.join("\n", logLines);

        assertTrue(logContent.contains("Unable to get all items."));
    }

    @Test
    @Order(7)
    void updateTest_whenItemIsValid_mustNotThrowException() {
        LineItem lineItem1 = new LineItem(1L, 3, 15, "dollars", product, order);
        assertDoesNotThrow(() -> lineItemDao.update(lineItem1));
    }

    @Test
    @Order(8)
    void updateTest_whenItemIsInvalid_mustReceiveErrorMessage() throws IOException {
        LineItem lineItem1 = new LineItem(2L, 3, 15, "dollars", product, order);
        lineItemDao.update(lineItem1);
        final List<String> logLines = Files.readAllLines(Paths.get(LOG_PATH));
        final String logContent = String.join("\n", logLines);
        assertTrue(logContent.contains("Unable to update item."));
    }

    @Test
    @Order(9)
    void deleteTest_whenItemIdIsValid_mustNotThrowException() throws IOException {
        assertDoesNotThrow(() -> lineItemDao.delete(1L));
    }

    @Test
    @Order(10)
    void deleteTest_whenItemIdIsInvalid_mustReceiveErrorMessage() throws IOException {
        lineItemDao.delete(2L);
        final List<String> logLines = Files.readAllLines(Paths.get(LOG_PATH));
        final String logContent = String.join("\n", logLines);

        assertTrue(logContent.contains("Unable to delete item."));
    }
}
