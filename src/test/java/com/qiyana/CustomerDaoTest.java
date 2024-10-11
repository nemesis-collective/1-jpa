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
import org.qiyana.DAO.imp.CustomerDao;
import org.qiyana.model.Customer;

public class CustomerDaoTest {
  static EntityManagerFactory etf;
  static EntityManagerFactory etfMock;
  static EntityManager emMock;
  static EntityTransaction transaction;
  static CustomerDao customerDao;
  static Customer customer;
  static CustomerDao customerDaoMock;

  @BeforeAll
  public static void init() {
    etf = Persistence.createEntityManagerFactory("persistence");
    customerDao = new CustomerDao(etf);
    etfMock = mock(EntityManagerFactory.class);
    transaction = mock(EntityTransaction.class);
    emMock = mock(EntityManager.class);
    when(etfMock.createEntityManager()).thenReturn(emMock);
    when(emMock.getTransaction()).thenReturn(transaction);
    customerDaoMock = new CustomerDao(etfMock);
  }

  @BeforeEach
  public void setUp() {
    customer = new Customer("QiyanaTech", "test@gmail.com", "Rua Tiberius Dourado", "123456789");
    customerDao.add(customer);
  }

  @Test
  void addTest_whenCustomerCorrectlyAdded_mustNotThrowException() {
    assertNotNull(customer.getId());
  }

  @Test
  void addTest_shouldThrowException() {
    doThrow(new IllegalArgumentException()).when(emMock).persist(any());
    assertThrows(IllegalArgumentException.class, () -> customerDaoMock.add(customer));
  }

  @Test
  void getTest_shouldReturnCustomer() {
    assertNotNull(customerDao.get(customer.getId()));
  }

  @Test
  void getTest_shouldThrowException() {
    doThrow(new IllegalArgumentException()).when(emMock).find(any(), any());
    assertThrows(IllegalArgumentException.class, () -> customerDaoMock.get(customer.getId()));
  }

  @Test
  void getAllTest_shouldReturnAllCustomer() {
    assertNotNull(customerDao.getAll());
  }

  @Test
  void getAllTest_shouldThrowException() {
    when(emMock.getCriteriaBuilder()).thenThrow(new IllegalArgumentException());
    assertThrows(IllegalArgumentException.class, customerDaoMock::getAll);
  }

  @Test
  void updateTest_whenCustomerCorrectlyUpdated_mustNotThrowException() {
    Customer customerToUpdate = customerDao.get(customer.getId());
    customerToUpdate.setEmail("teste789@gmail.com");
    customerDao.update(customerToUpdate);
    assertEquals("teste789@gmail.com", customerDao.get(customer.getId()).getEmail());
  }

  @Test
  void updateTest_shouldThrowException() {
    when(emMock.merge(any())).thenThrow(new IllegalArgumentException());
    assertThrows(IllegalArgumentException.class, () -> customerDaoMock.update(customer));
  }

  @Test
  void deleteTest_whenCustomerCorrectlyDeleted_mustNotThrowException() {
    assertDoesNotThrow(() -> customerDao.delete(customer.getId()));
  }

  @Test
  void deleteTest_shouldThrowException() {
    doThrow(new IllegalArgumentException()).when(emMock).remove(any());
    assertThrows(IllegalArgumentException.class, () -> customerDaoMock.delete(customer.getId()));
  }
}
