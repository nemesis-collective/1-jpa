package com.example;

import org.example.imp.CustomerDao;
import org.example.model.Customer;
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
public class CustomerDaoTest {
    private static final String LOG_PATH = "logs/app.log";

    EntityManagerFactory etf = Persistence.createEntityManagerFactory("persistence");
    CustomerDao customerDao = new CustomerDao(etf);
    Customer customer =
            new Customer(null, "QiyanaTech", "test@gmail.com", "Rua Tiberius Dourado", "123456789");


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
    void addOrderTest_whenAddCustomer_mustNotThrowException() {
        assertDoesNotThrow(() -> customerDao.add(customer));
    }

    @Test
    @Order(2)
    void addOrderTest_whenAddCustomerFails_mustNotThrowException() {
        EntityManagerFactory etfMock = mock(EntityManagerFactory.class);
        EntityManager emMock = mock(EntityManager.class);
        EntityTransaction transaction = mock(EntityTransaction.class);

        when(etfMock.createEntityManager()).thenReturn(emMock);
        when(emMock.getTransaction()).thenReturn(transaction);
        doThrow(new IllegalArgumentException()).when(emMock).persist(any());

        CustomerDao customerDaoMock = new CustomerDao(etfMock);

        assertDoesNotThrow(() -> customerDaoMock.add(customer));
    }

    @Test
    @Order(3)
    void getTest_whenCustomerIdIsValid_mustReturnCustomer() {
        assertNotNull(customerDao.get(1L));
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    void getTest_whenCustomerIdIsInvalid_mustNotThrowException() throws IOException {
        customerDao.get(2L);
        final List<String> logLines = Files.readAllLines(Paths.get(LOG_PATH));
        final String logContent = String.join("\n", logLines);
        assertTrue(logContent.contains("Unable to get customer."));
    }

    @Test
    @Order(5)
    void getAllTest_mustReturnAllCustomer() {
        assertDoesNotThrow(() -> customerDao.getAll());
    }

    @Test
    @Order(6)
    void getAllTest_whenOccursException_mustNotThrow() throws IOException {
        EntityManagerFactory etfMock = mock(EntityManagerFactory.class);
        EntityManager emMock = mock(EntityManager.class);
        EntityTransaction transaction = mock(EntityTransaction.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        when(etfMock.createEntityManager()).thenReturn(emMock);
        when(emMock.getTransaction()).thenReturn(transaction);
        when(emMock.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery()).thenThrow(new IllegalArgumentException());

        CustomerDao customerDaoMock = new CustomerDao(etfMock);
        customerDaoMock.getAll();
        final List<String> logLines = Files.readAllLines(Paths.get(LOG_PATH));
        final String logContent = String.join("\n", logLines);

        assertTrue(logContent.contains("Unable to get all customers."));
    }

    @Test
    @Order(7)
    void updateTest_whenCustomerIsValid_mustNotThrowException() {
        Customer customer1 = new Customer(1L,"QiyanaTech1","teste456@gmail.com","Rua Ixtal","1254785336");
        assertDoesNotThrow(() -> customerDao.update(customer1));
    }

    @Test
    @Order(8)
    void updateTest_whenCustomerIsInvalid_mustReceiveErrorMessage() throws IOException {
        Customer customer1 = new Customer(2L,"QiyanaTech","teste456@gmail.com","Rua Ixtal","1254785336");
        customerDao.update(customer1);
        final List<String> logLines = Files.readAllLines(Paths.get(LOG_PATH));
        final String logContent = String.join("\n", logLines);
        assertTrue(logContent.contains("Unable to update customer."));
    }

    @Test
    @Order(9)
    void deleteTest_whenCustomerIdIsValid_mustNotThrowException(){
        assertDoesNotThrow(() -> customerDao.delete(1L));
    }

    @Test
    @Order(10)
    void deleteTest_whenOrderIdIsInvalid_mustReceiveErrorMessage() throws IOException {
        customerDao.delete(2L);
        final List<String> logLines = Files.readAllLines(Paths.get(LOG_PATH));
        final String logContent = String.join("\n", logLines);

        assertTrue(logContent.contains("Unable to delete customer."));
    }
}
