package org.qiyana.DAO.imp;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.qiyana.DAO.Dao;
import org.qiyana.model.LineItem;

/** Class responsible for database operations at lineItem entity. */
@Slf4j
public class LineItemDao implements Dao<LineItem> {
  EntityManager em;

  public LineItemDao(EntityManagerFactory etf) {
    em = etf.createEntityManager();
  }

  /**
   * Uses an entity manager to insert a lineItem in the database.
   *
   * @param lineItem the lineItem object that will be added to the database.
   */
  @Override
  public void add(@NonNull LineItem lineItem) {
    em.getTransaction().begin();
    em.persist(lineItem);
    em.getTransaction().commit();
  }

  /**
   * Uses an entity manager to insert a lineItem in the database.
   *
   * @param id the lineItem id that will be searched in the database.
   * @return a lineItem object.
   */
  @Override
  public LineItem get(@NonNull Long id) {
    return em.find(LineItem.class, id);
  }

  /**
   * Uses an entity manager to search all lineItems in the database.
   *
   * @return a lineItem list.
   */
  @Override
  public List<LineItem> getAll() {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<LineItem> cq = cb.createQuery(LineItem.class);

    Root<LineItem> root = cq.from(LineItem.class);

    cq.select(root);

    return em.createQuery(cq).getResultList();
  }

  /**
   * Uses an entity manager to update a lineItem in the database.
   *
   * @param lineItem the lineItem that will be updated in the database.
   */
  @Override
  public void update(@NonNull LineItem lineItem) {
    em.getTransaction().begin();
    em.merge(lineItem);
    em.getTransaction().commit();
  }

  /**
   * Uses an entity manager to delete a lineItem in the database.
   *
   * @param id the lineItem id that will be deleted in the database.
   */
  @Override
  public void delete(@NonNull Long id) {
    em.getTransaction().begin();
    LineItem lineItem = em.find(LineItem.class, id);
    em.remove(lineItem);
    em.getTransaction().commit();
  }
}
