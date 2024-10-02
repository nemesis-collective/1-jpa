package org.qiyana.DAO.imp;

import java.util.List;
import javax.persistence.*;
import javax.persistence.criteria.*;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.qiyana.DAO.Dao;
import org.qiyana.model.Order;

/** Class responsible for database operations at order entity. */
@Slf4j
public class OrderDao implements Dao<Order> {
  EntityManager em;

  public OrderDao(EntityManagerFactory etf) {
    em = etf.createEntityManager();
  }

  /**
   * Uses an entity manager to insert an order in the database.
   *
   * @param order the order object who will be added to the database.
   */
  @Override
  public void add(@NonNull Order order) {
    em.getTransaction().begin();
    em.persist(order);
    em.getTransaction().commit();
  }

  /**
   * Uses an entity manager to insert an order in the database.
   *
   * @param id the order id who will be searched in the database.
   * @return an order object.
   */
  @Override
  public Order get(@NonNull Long id) {
    return em.find(Order.class, id);
  }

  /**
   * Uses an entity manager to search all orders in the database.
   *
   * @return an order list.
   */
  @Override
  public List<Order> getAll() {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Order> cq = cb.createQuery(Order.class);

    Root<Order> root = cq.from(Order.class);

    cq.select(root);

    return em.createQuery(cq).getResultList();
  }

  /**
   * Uses an entity manager to update an order in the database.
   *
   * @param order the order who will be updated in the database.
   */
  @Override
  public void update(@NonNull Order order) {
    em.getTransaction().begin();
    em.merge(order);
    em.getTransaction().commit();
  }

  /**
   * Uses an entity manager to delete an order in the database.
   *
   * @param id the order id who will be deleted in the database.
   */
  @Override
  public void delete(@NonNull Long id) {
    em.getTransaction().begin();
    Order order = em.find(Order.class, id);
    em.remove(order);
    em.getTransaction().commit();
  }
}
