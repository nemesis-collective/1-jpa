package org.example.imp;

import java.util.List;
import javax.persistence.*;
import javax.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.example.DAO.Dao;
import org.example.model.LineItem;
import org.example.model.Order;

@Slf4j
public class OrderDao implements Dao<Order> {

  EntityManagerFactory etf;
  EntityManager em;
  CriteriaBuilder cb;

  public OrderDao(EntityManagerFactory etf) {
    this.etf = etf;
    em = etf.createEntityManager();
    cb = em.getCriteriaBuilder();
    em.getTransaction().begin();
  }

  @Override
  public void add(Order order) {
    try {
      em.persist(order.getCustomer());
      em.persist(order);
      for (LineItem item : order.getLineItems()) {
        em.persist(item.getProduct());
        em.persist(item);
      }
    } catch (IllegalArgumentException | TransactionRequiredException e) {
      log.error("Unable to add order.");
    } finally {
      em.getTransaction().commit();
    }
  }

  @Override
  public Order get(Long id) {
    Order order = null;
    try {
      order = em.find(Order.class, id);
      if(order == null){
        throw new IllegalArgumentException();
      }
    } catch (IllegalArgumentException e) {
      log.error("Unable to get order.");
    }
    return order;
  }

  @Override
  public List<Order> getAll() {
    List<Order> list = List.of();
    try {
      CriteriaQuery<Order> cq = cb.createQuery(Order.class);

      Root<Order> root = cq.from(Order.class);

      cq.select(root);

      list = em.createQuery(cq).getResultList();
    } catch (Exception e) {
      log.error("Unable to get all orders.");
    }

    return list;
  }

  @Override
  public void update(Order order) {
    try {
      Order order1 = em.find(Order.class, order.getId());
      if (order1 == null) {
        throw new IllegalArgumentException();
      }
      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaUpdate<Order> update = cb.createCriteriaUpdate(Order.class);
      Root<Order> root = update.from(Order.class);

      update.set(root.get("paymentMethod"), order.getPaymentMethod());
      update.set(root.get("status"), order.getStatus());

      update.where(cb.equal(root.get("id"), order.getId()));

      em.createQuery(update).executeUpdate();

    } catch (IllegalArgumentException e) {
      log.error("Unable to update order.");
    } finally {
      em.getTransaction().commit();
    }
  }

  @Override
  public void delete(Long orderId) {
    try {
      Order order = em.find(Order.class, orderId);
      em.remove(order);
    } catch (IllegalArgumentException | TransactionRequiredException e) {
      log.error("Unable to delete order.");
    } finally {
      em.getTransaction().commit();
    }
  }
}
