package com.example;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;

import org.example.imp.OrderDao;
import org.example.model.Customer;
import org.example.model.LineItem;
import org.example.model.Order;
import org.example.model.Product;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderDAOTest {

  EntityManagerFactory etf = Persistence.createEntityManagerFactory("persistence");
  OrderDao orderDao = new OrderDao(etf);
  Customer customer =
      new Customer(null, "QiyanaTech", "test@gmail.com", "Rua Tiberius Dourado", "123456789");

  Order order = new Order(null, customer, "pending", "Pix");

  Product product = new Product(null, "Hair Spray", "300 ml", 5, "dollars");

  LineItem lineItem = new LineItem(null, 2, 10, "dollars", product, order);

  @Test
  @org.junit.jupiter.api.Order(1)
  void addOrderTest_whenAddAnOrder_mustNotThrowException() {
    order.addLineItem(lineItem);
    assertDoesNotThrow(() -> orderDao.add(order));
  }

  @Test
  @org.junit.jupiter.api.Order(2)
  void addOrderTest_whenAddAnOrderFails_mustNotThrowException() {
    EntityManagerFactory etfMock = mock(EntityManagerFactory.class);
    EntityManager emMock = mock(EntityManager.class);
    EntityTransaction transaction = mock(EntityTransaction.class);

    when(etfMock.createEntityManager()).thenReturn(emMock);
    when(emMock.getTransaction()).thenReturn(transaction);
    doThrow(new IllegalArgumentException()).when(emMock).persist(any());

    OrderDao orderDaoMock = new OrderDao(etfMock);

    assertDoesNotThrow(() -> orderDaoMock.add(order));
  }

  @Test
  @org.junit.jupiter.api.Order(3)
  void getAllTest_mustReturnAllOrders() {
    List<Order> list = List.of();
    assertNotEquals(list, orderDao.getAll());
  }

  @Test
  @org.junit.jupiter.api.Order(4)
  void getAllTest_whenOccursException_mustNotThrow() {
    EntityManagerFactory etfMock = mock(EntityManagerFactory.class);
    EntityManager emMock = mock(EntityManager.class);
    EntityTransaction transaction = mock(EntityTransaction.class);
    CriteriaBuilder cb = mock(CriteriaBuilder.class);

    when(etfMock.createEntityManager()).thenReturn(emMock);
    when(emMock.getTransaction()).thenReturn(transaction);
    when(emMock.getCriteriaBuilder()).thenReturn(cb);
    when(cb.createQuery()).thenThrow(new IllegalArgumentException());

    OrderDao orderDaoMock = new OrderDao(etfMock);

    assertDoesNotThrow(orderDaoMock::getAll);
  }

  @Test
  @org.junit.jupiter.api.Order(5)
  void updateTest_whenOrderIsValid_mustNotThrowException() {
    Order order1 = new Order(1L, customer, "processing", "Credit Card");
    assertDoesNotThrow(() -> orderDao.update(order1));
  }

  @Test
  @org.junit.jupiter.api.Order(6)
  void updateTest_whenOrderIsInvalid_mustNotThrowException() {
    Order order1 = new Order(3L, customer, "processing", "Ticket");
    assertDoesNotThrow(() -> orderDao.update(order1));
  }

  @Test
  @org.junit.jupiter.api.Order(7)
  void deleteTest_whenOrderIdIsValid_mustNotThrowException(){
    assertDoesNotThrow(() -> orderDao.delete(1L));
  }

  @Test
  @org.junit.jupiter.api.Order(8)
  void deleteTest_whenOrderIdIsInvalid_mustNotThrowException() {
    assertDoesNotThrow(() -> orderDao.delete(2L));
  }
}
