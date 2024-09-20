package org.qiyana.DAO.imp;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.qiyana.DAO.Dao;
import org.qiyana.model.LineItem;

/** Class responsible for database operations at lineItem entity. */
@Slf4j
public class LineItemDao implements Dao<LineItem> {
  EntityManager em;
  CriteriaBuilder cb;

  public LineItemDao(EntityManagerFactory etf) {
    em = etf.createEntityManager();
    cb = em.getCriteriaBuilder();
  }

  /**
   * Uses an entity manager to insert a lineItem into the database.
   *
   * @param lineItem the lineItem object who will be added at database.
   */
  @Override
  public void add(LineItem lineItem) {
    em.getTransaction().begin();
    em.persist(lineItem);
    em.getTransaction().commit();
  }

  /**
   * Uses an entity manager to insert a lineItem into the database.
   *
   * @param id the lineItem id who will be searched at database.
   * @return a lineItem object.
   */
  @Override
  public LineItem get(Long id) {
    return em.find(LineItem.class, id);
  }

  /**
   * Uses an entity manager to search all lineItems into the database.
   *
   * @return a lineItem list.
   */
  @Override
  public List<LineItem> getAll() {
    CriteriaQuery<LineItem> cq = cb.createQuery(LineItem.class);

    Root<LineItem> root = cq.from(LineItem.class);

    cq.select(root);

    return em.createQuery(cq).getResultList();
  }

  /**
   * Uses an entity manager to update a lineItem into the database.
   *
   * @param lineItem the lineItem who will be updated at database.
   */
  @Override
  public void update(LineItem lineItem) {
    em.getTransaction().begin();
    em.merge(lineItem);
    em.getTransaction().commit();
  }

  /**
   * Uses an entity manager to delete a lineItem into the database.
   *
   * @param id the lineItem id who will be deleted at database.
   */
  @Override
  public void delete(Long id) {
    em.getTransaction().begin();
    LineItem lineItem = em.find(LineItem.class, id);
    em.remove(lineItem);
    em.getTransaction().commit();
  }
}
