package com.example;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.example.imp.OrderDao;
import org.example.model.Customer;
import org.example.model.LineItem;
import org.example.model.Order;
import org.example.model.Product;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderDAOTest {

  EntityManagerFactory etf = Persistence.createEntityManagerFactory("persistence");
  OrderDao orderDao = new OrderDao(etf);
  static Integer orderId = 1;

  Customer customer =
      new Customer(null, "QiyanaTech", "test@gmail.com", "Rua Tiberius Dourado", "123456789");
  Product product = new Product(null, "Hair Spray", "300 ml", 5, "dollars");
  Order order = new Order(null, customer);
  LineItem lineItem = new LineItem(null, 2, 10, "dollars", product, order);

  @Test
  @org.junit.jupiter.api.Order(1)
  void addOrderTest_whenAddAnOrder_mustNotThrowException() {
    order.addLineItem(lineItem);
    assertDoesNotThrow(() -> orderDao.add(order));
  }

  @Test
  void getAllTest_mustReturnAll() {
    List<Order> list = List.of();
    assertNotEquals(list, orderDao.getAll());
  }

  @Test
  void addOrderTest_whenAddAnOrderFails_mustNotThrowException() {
    EntityManagerFactory etfMock = mock(EntityManagerFactory.class);
    EntityManager emMock = mock(EntityManager.class);
    EntityTransaction transaction = mock(EntityTransaction.class);
    when(etfMock.createEntityManager()).thenReturn(emMock);
    when(emMock.getTransaction()).thenReturn(transaction);

    doThrow(new IllegalArgumentException()).when(emMock).persist(any());
    OrderDao orderDAO1 = new OrderDao(etfMock);
    assertDoesNotThrow(
        () -> {
          orderDAO1.add(order);
        });
  }

  @Test
  @org.junit.jupiter.api.Order(3)
  void getOrderByLineItemTest_whenLineItemIsValid_mustReturnTheOrder() {
    assertNotNull(orderDao.getByLineItem(orderId));
  }

  @Test
  @org.junit.jupiter.api.Order(4)
  void getOrdersByCustomerTest_whenCustomerAndAddressIsValid_mustReturnAllOrders() {
    assertNotNull(orderDao.getByCustomer("QiyanaTech", "Tiberius"));
  }

  @Test
  @org.junit.jupiter.api.Order(5)
  void deleteTest_whenOrderIdIsValid_mustNotThrowException() {
     orderDao.delete(1L);
  }
}
