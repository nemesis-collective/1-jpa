package org.example.imp;

import java.util.List;
import javax.persistence.*;
import javax.persistence.criteria.*;

import lombok.extern.slf4j.Slf4j;
import org.example.DAO.Dao;
import org.example.model.Customer;
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
      log.error("Unable to add order. {}", e.getMessage());
    } finally {
      em.getTransaction().commit();
    }
  }

  @Override
  public Order get(Long id) {
    Order order = null;
    try{
      order = em.find(Order.class, id);
    }catch (IllegalArgumentException e){
      log.error("Unable to find the order.{}", e.getMessage());
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
      log.error("Unable to get orders.{} ", e.getMessage());
    }

    return list;
  }

  @Override
  public Order update(Order order) {
    try{
      CriteriaUpdate<Order> criteriaUpdate = cb.createCriteriaUpdate(Order.class);
      Root<Order> root = criteriaUpdate.from(Order.class);

      criteriaUpdate.set(root.get("customer"), order.getCustomer());

      criteriaUpdate.where(cb.equal(root.get("id"), order.getId()));

      em.createQuery(criteriaUpdate).executeUpdate();
      em.getTransaction().commit();
    } catch (Exception e) {
      log.error("Unable to update order. {}", e.getMessage());
    }
    return order;
  }

  public Order getByLineItem(Integer order_id) {
    Order order = null;
    try {
      CriteriaQuery<Order> cq = cb.createQuery(Order.class);
      Root<LineItem> lineItemRoot = cq.from(LineItem.class);
      Join<LineItem, Order> orderJoin = lineItemRoot.join("order");

      cq.select(orderJoin).where(cb.equal(orderJoin.get("id"), order_id));
      order = em.createQuery(cq).getSingleResult();
    } catch (Exception e) {
      log.error("Unable to find order. {}", e.getMessage());
    } finally {
      em.getTransaction().commit();
    }

    return order;
  }

  public List<Order> getByCustomer(String name, String address) {
    TypedQuery<Order> query = null;
    try {
      CriteriaQuery<Order> criteriaQuery = cb.createQuery(Order.class);
      Root<Order> orderRoot = criteriaQuery.from(Order.class);

      Join<Order, Customer> customerJoin = orderRoot.join("customer");

      Predicate namePredicate = cb.equal(customerJoin.get("name"), name);

      Predicate addressPredicate = cb.like(customerJoin.get("address"), "%" + address + "%");
      criteriaQuery.where(cb.and(namePredicate, addressPredicate));
      query = em.createQuery(criteriaQuery);
    } catch (IllegalArgumentException e) {
      log.error("Unable to find order{}", e.getMessage());
    }
    return query.getResultList();
  }

  @Override
  public void delete(Long orderId) {
    try {
      Order order = em.find(Order.class, orderId);
      em.remove(order);
    } catch (IllegalArgumentException | TransactionRequiredException e) {
      log.error("Unable to delete order. {}", e.getMessage());
    } finally {
      em.getTransaction().commit();
    }
  }
}
