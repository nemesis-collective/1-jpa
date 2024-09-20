package org.qiyana.DAO.imp;

import java.util.List;
import javax.persistence.*;
import javax.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.qiyana.DAO.Dao;
import org.qiyana.model.Order;

/** Class responsible for database operations at order entity. */
@Slf4j
public class OrderDao implements Dao<Order> {
  EntityManager em;
  CriteriaBuilder cb;

  public OrderDao(EntityManagerFactory etf) {
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
    em.getTransaction().begin();
    em.persist(order);
    em.getTransaction().commit();
  }

  /**
   * Uses an entity manager to insert an order into the database.
   *
   * @param id the order id who will be searched at database.
   * @return an order object.
   */
  @Override
  public Order get(Long id) {
    return em.find(Order.class, id);
  }

  /**
   * Uses an entity manager to search all orders into the database.
   *
   * @return an order list.
   */
  @Override
  public List<Order> getAll() {
    CriteriaQuery<Order> cq = cb.createQuery(Order.class);

    Root<Order> root = cq.from(Order.class);

    cq.select(root);

    return em.createQuery(cq).getResultList();
  }

  /**
   * Uses an entity manager to update an order into the database.
   *
   * @param order the order who will be updated at database.
   */
  @Override
  public void update(Order order) {
    em.getTransaction().begin();
    CriteriaUpdate<Order> update = cb.createCriteriaUpdate(Order.class);
    Root<Order> root = update.from(Order.class);

    update.set(root.get("paymentMethod"), order.getPaymentMethod());
    update.set(root.get("status"), order.getStatus());

    update.where(cb.equal(root.get("id"), order.getId()));

    em.createQuery(update).executeUpdate();
    em.getTransaction().commit();
  }

  /**
   * Uses an entity manager to delete an order into the database.
   *
   * @param id the order id who will be deleted at database.
   */
  @Override
  public void delete(Long id) {
    em.getTransaction().begin();
    Order order = em.find(Order.class, id);
    em.remove(order);
    em.getTransaction().commit();
  }
}
