package org.example.DAO.imp;

import java.util.List;
import javax.persistence.*;
import javax.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.example.DAO.Dao;
import org.example.model.Order;

/** Class responsible for database operations at order entity. */
@Slf4j
public class OrderDao implements Dao<Order> {

  EntityManagerFactory etf;
  EntityManager em;
  CriteriaBuilder cb;

  public OrderDao(EntityManagerFactory etf) {
    this.etf = etf;
    em = etf.createEntityManager();
    cb = em.getCriteriaBuilder();
  }

  /**
   * Uses an entity manager to insert an order into the database.
   *
   * @param order the order object who will be added at database.
   */
  @Override
  public void add(Order order) {
    try {
      em.getTransaction().begin();
      em.persist(order);
    } catch (IllegalArgumentException | TransactionRequiredException e) {
      log.error("Unable to add order.");
    } finally {
      em.getTransaction().commit();
    }
  }

  /**
   * Uses an entity manager to insert an order into the database.
   *
   * @param id the order id who will be searched at database.
   * @return a order object.
   */
  @Override
  public Order get(Long id) {
    Order order = null;
    try {
      em.getTransaction().begin();
      order = em.find(Order.class, id);
      if (order == null) {
        throw new IllegalArgumentException();
      }
    } catch (IllegalArgumentException e) {
      log.error("Unable to get order.");
    }
    return order;
  }

  /**
   * Uses an entity manager to search all orders into the database.
   *
   * @return an order list.
   */
  @Override
  public List<Order> getAll() {
    List<Order> list = List.of();
    try {
      em.getTransaction().begin();
      CriteriaQuery<Order> cq = cb.createQuery(Order.class);

      Root<Order> root = cq.from(Order.class);

      cq.select(root);

      list = em.createQuery(cq).getResultList();
    } catch (Exception e) {
      log.error("Unable to get all orders.");
    }

    return list;
  }

  /**
   * Uses an entity manager to update an order into the database.
   *
   * @param order the order who will be updated at database.
   */
  @Override
  public void update(Order order) {
    try {
      em.getTransaction().begin();
      Order order1 = em.find(Order.class, order.getId());
      if (order1 == null) {
        throw new IllegalArgumentException();
      }
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

  /**
   * Uses an entity manager to delete an order into the database.
   *
   * @param id the order id who will be deleted at database.
   */
  @Override
  public void delete(Long id) {
    try {
      em.getTransaction().begin();
      Order order = em.find(Order.class, id);
      em.remove(order);
    } catch (IllegalArgumentException | TransactionRequiredException e) {
      log.error("Unable to delete order.");
    } finally {
      em.getTransaction().commit();
    }
  }
}
