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
import org.qiyana.model.Customer;

public class CustomerDaoTest {
  EntityManagerFactory etf;
  CustomerDao customerDao;
  Customer customer;

  @BeforeEach
  public void tearDown(){
    etf = Persistence.createEntityManagerFactory("persistence");
    customerDao = new CustomerDao(etf);
    customer =
        new Customer(null, "QiyanaTech", "test@gmail.com", "Rua Tiberius Dourado", "123456789");
  }

  @Test
  void addCustomerTest_whenAddCustomer_mustNotThrowException() {
    customerDao.add(customer);
    assertNotNull(customer.getId());
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
    assertThrows(IllegalArgumentException.class,() -> customerDaoMock.add(customer));
  }

  @Test
  void getTest_whenCustomerIdIsValid_mustReturnCustomer() {
    customerDao.add(customer);
    assertNotNull(customerDao.get(customer.getId()));
  }

  @Test
  void getTest_whenCustomerIdIsInvalid_mustThrowException(){
    EntityManagerFactory etfMock = mock(EntityManagerFactory.class);
    EntityManager emMock = mock(EntityManager.class);
    EntityTransaction transaction = mock(EntityTransaction.class);

    when(etfMock.createEntityManager()).thenReturn(emMock);
    when(emMock.getTransaction()).thenReturn(transaction);
    doThrow(new IllegalArgumentException()).when(emMock).find(any(),any());

    CustomerDao customerDaoMock = new CustomerDao(etfMock);
    assertThrows(IllegalArgumentException.class,() -> customerDaoMock.get(customer.getId()));
  }

  @Test
  void getAllTest_shouldReturnAllCustomer(){
    customerDao.add(customer);
    assertNotNull(customerDao.getAll());
  }

  @Test
  void getAllTest_shouldThrowException(){
    EntityManagerFactory etfMock = mock(EntityManagerFactory.class);
    EntityManager emMock = mock(EntityManager.class);
    EntityTransaction transaction = mock(EntityTransaction.class);

    when(etfMock.createEntityManager()).thenReturn(emMock);
    when(emMock.getTransaction()).thenReturn(transaction);
    when(emMock.getCriteriaBuilder()).thenThrow(new IllegalArgumentException());

    CustomerDao customerDaoMock = new CustomerDao(etfMock);
    assertThrows(IllegalArgumentException.class, customerDaoMock::getAll);
  }

  @Test
  void updateTest_whenCustomerCorrectlyUpdated_mustNotThrowException(){
    customerDao.add(customer);
    Customer customerUpdated =
        new Customer(customer.getId(), "QiyanaTech1", "teste456@gmail.com", "Rua Ixtal", "1254785336");
    assertDoesNotThrow(() -> customerDao.update(customerUpdated));
  }

  @Test
  void updateTest_shouldThrowException(){
    EntityManagerFactory etfMock = mock(EntityManagerFactory.class);
    EntityManager emMock = mock(EntityManager.class);
    EntityTransaction transaction = mock(EntityTransaction.class);

    when(etfMock.createEntityManager()).thenReturn(emMock);
    when(emMock.getTransaction()).thenReturn(transaction);
    when(emMock.merge(any())).thenThrow(new IllegalArgumentException());

    CustomerDao customerDaoMock = new CustomerDao(etfMock);
    customerDao.add(customer);
    assertThrows(IllegalArgumentException.class,() -> customerDaoMock.update(customer));
  }

  @Test
  void deleteTest_whenCustomerCorrectlyDeleted_mustNotThrowException(){
    customerDao.add(customer);
    assertDoesNotThrow(() -> customerDao.delete(customer.getId()));
  }

  @Test
  void deleteTest_shouldThrowException(){
    EntityManagerFactory etfMock = mock(EntityManagerFactory.class);
    EntityManager emMock = mock(EntityManager.class);
    EntityTransaction transaction = mock(EntityTransaction.class);

    when(etfMock.createEntityManager()).thenReturn(emMock);
    when(emMock.getTransaction()).thenReturn(transaction);
    doThrow(new IllegalArgumentException()).when(emMock).remove(any());

    customerDao.add(customer);
    CustomerDao customerDaoMock = new CustomerDao(etfMock);
    assertThrows(IllegalArgumentException.class, () -> customerDaoMock.delete(customer.getId()));
  }
}
