package org.example.DAO.imp;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TransactionRequiredException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

import lombok.extern.slf4j.Slf4j;
import org.example.DAO.Dao;
import org.example.model.LineItem;
import org.example.model.Order;
import org.example.model.Product;

/** Class responsible for database operations at lineItem entity. */
@Slf4j
public class LineItemDao implements Dao<LineItem> {
  EntityManagerFactory etf;
  EntityManager em;
  CriteriaBuilder cb;

  public LineItemDao(EntityManagerFactory etf) {
    this.etf = etf;
    em = etf.createEntityManager();
    cb = em.getCriteriaBuilder();
  }

  /**
   * Uses an entity manager to insert a lineItem into the database.
   *
   * @param  lineItem the lineItem object who will be added at database.
   */
  @Override
  public void add(LineItem lineItem) {
    try {
      em.getTransaction().begin();
      em.persist(lineItem);
    } catch (IllegalArgumentException | TransactionRequiredException e) {
      log.error("Unable to add item to order.");
    }finally{
      em.getTransaction().commit();
    }
  }

  /**
   * Uses an entity manager to insert a lineItem into the database.
   *
   * @param  id the lineItem id who will be searched at database.
   * @return a lineItem object.
   */
  @Override
  public LineItem get(Long id) {
    LineItem lineItem = null;
    try {
      em.getTransaction().begin();
      lineItem = em.find(LineItem.class, id);
      if(lineItem == null){
        throw new IllegalArgumentException();
      }
    } catch (IllegalArgumentException e) {
      log.error("Unable to get item.");
    }
    return lineItem;
  }

  /**
   * Uses an entity manager to search all lineItems into the database.
   *
   * @return a lineItem list.
   */
  @Override
  public List<LineItem> getAll() {
    List<LineItem> list = List.of();
    try {
      em.getTransaction().begin();
      CriteriaQuery<LineItem> cq = cb.createQuery(LineItem.class);

      Root<LineItem> root = cq.from(LineItem.class);

      cq.select(root);

      list = em.createQuery(cq).getResultList();
    } catch (Exception e) {
      log.error("Unable to get all items.");
    }

    return list;
  }

  /**
   * Uses an entity manager to update a lineItem into the database.
   *
   * @param  lineItem the lineItem who will be updated at database.
   */
  @Override
  public void update(LineItem lineItem) {
    try {
      em.getTransaction().begin();
      LineItem lineItem1 = em.find(LineItem.class, lineItem.getId());
      if (lineItem1 == null) {
        throw new IllegalArgumentException();
      }
      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaUpdate<LineItem> update = cb.createCriteriaUpdate(LineItem.class);
      Root<LineItem> root = update.from(LineItem.class);

      update.set(root.get("currency"), lineItem1.getCurrency());
      update.set(root.get("totalPrice"), lineItem1.getTotalPrice());
      update.set(root.get("quantity"), lineItem1.getQuantity());

      update.where(cb.equal(root.get("id"), lineItem1.getId()));

      em.createQuery(update).executeUpdate();
  } catch (Exception e) {
    log.error("Unable to update item.");
  } finally {
    em.getTransaction().commit();
  }
  }

  /**
   * Uses an entity manager to delete a lineItem into the database.
   *
   * @param  id the lineItem id who will be deleted at database.
   */
  @Override
  public void delete(Long id) {
    try {
      em.getTransaction().begin();
      LineItem lineItem = em.find(LineItem.class, id);
      em.remove(lineItem);
    } catch (IllegalArgumentException | TransactionRequiredException e) {
      log.error("Unable to delete item.");
    } finally {
      em.getTransaction().commit();
    }
  }
}
