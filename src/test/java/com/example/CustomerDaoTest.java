package com.example;

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
import org.example.DAO.imp.CustomerDao;
import org.example.model.Customer;
import org.junit.jupiter.api.*;

public class CustomerDaoTest {
  private static final String LOG_PATH = "logs/app.log";

  EntityManagerFactory etf;
  CustomerDao customerDao;
  Customer customer;

  @BeforeEach
  public void tearDown() throws IOException {
    Files.write(
        Paths.get(LOG_PATH),
        new byte[0],
        StandardOpenOption.TRUNCATE_EXISTING,
        StandardOpenOption.CREATE);
    etf = Persistence.createEntityManagerFactory("persistence");
    customerDao = new CustomerDao(etf);
    customer =
        new Customer(null, "QiyanaTech", "test@gmail.com", "Rua Tiberius Dourado", "123456789");
  }

  @Test
  void addCustomerTest_whenAddCustomer_mustNotThrowException() {
    assertDoesNotThrow(() -> customerDao.add(customer));
  }

  @Test
  void addCustomerTest_whenAddCustomerFails_mustNotThrowException() {
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
  void getTest_whenCustomerIdIsValid_mustReturnCustomer() {
    customerDao.add(customer);
    assertNotNull(customerDao.get(4L));
  }

  @Test
  void getTest_whenCustomerIdIsInvalid_mustNotThrowException() throws IOException {
    customerDao.get(5L);
    final List<String> logLines = Files.readAllLines(Paths.get(LOG_PATH));
    final String logContent = String.join("\n", logLines);
    assertTrue(logContent.contains("Unable to get customer."));
  }

  @Test
  void getAllTest_mustReturnAllCustomer() throws IOException {
    customerDao.getAll();
    final List<String> logLines = Files.readAllLines(Paths.get(LOG_PATH));
    final String logContent = String.join("\n", logLines);

    assertFalse(logContent.contains("Unable to get all customers."));
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

    CustomerDao customerDaoMock = new CustomerDao(etfMock);
    customerDaoMock.getAll();
    final List<String> logLines = Files.readAllLines(Paths.get(LOG_PATH));
    final String logContent = String.join("\n", logLines);

    assertTrue(logContent.contains("Unable to get all customers."));
  }

  @Test
  void updateTest_whenCustomerIsValid_mustNotThrowException() throws IOException {
    customerDao.add(customer);
    Customer customer1 =
        new Customer(1L, "QiyanaTech1", "teste456@gmail.com", "Rua Ixtal", "1254785336");
    customerDao.update(customer1);
    final List<String> logLines = Files.readAllLines(Paths.get(LOG_PATH));
    final String logContent = String.join("\n", logLines);

    assertFalse(logContent.contains("Unable to update customer."));
  }

  @Test
  void updateTest_whenCustomerIsInvalid_mustReceiveErrorMessage() throws IOException {
    Customer customer1 =
        new Customer(5L, "QiyanaTech", "teste456@gmail.com", "Rua Ixtal", "1254785336");
    customerDao.update(customer1);
    final List<String> logLines = Files.readAllLines(Paths.get(LOG_PATH));
    final String logContent = String.join("\n", logLines);
    assertTrue(logContent.contains("Unable to update customer."));
  }

  @Test
  void deleteTest_whenCustomerIdIsValid_mustNotThrowException() throws IOException {
    customerDao.add(customer);
    customerDao.delete(2L);
    final List<String> logLines = Files.readAllLines(Paths.get(LOG_PATH));
    final String logContent = String.join("\n", logLines);

    assertFalse(logContent.contains("Unable to delete customer."));
  }

  @Test
  void deleteTest_whenCustomerIdIsInvalid_mustReceiveErrorMessage() throws IOException {
    customerDao.delete(5L);
    final List<String> logLines = Files.readAllLines(Paths.get(LOG_PATH));
    final String logContent = String.join("\n", logLines);

    assertTrue(logContent.contains("Unable to delete customer."));
  }
}
