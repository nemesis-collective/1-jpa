package org.example.imp;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TransactionRequiredException;
import javax.persistence.criteria.CriteriaBuilder;
import lombok.extern.slf4j.Slf4j;
import org.example.DAO.Dao;
import org.example.model.LineItem;

@Slf4j
public class LineItemDao implements Dao<LineItem> {
  EntityManagerFactory etf;
  EntityManager em;
  CriteriaBuilder cb;

  public LineItemDao(EntityManagerFactory etf) {
    this.etf = etf;
    em = etf.createEntityManager();
    cb = em.getCriteriaBuilder();
    em.getTransaction().begin();
  }

  @Override
  public void add(LineItem lineItem) {
    try {
      em.persist(lineItem);
    } catch (IllegalArgumentException | TransactionRequiredException e) {
      log.error("Unable to add item to order.");
    }finally{
      em.getTransaction().commit();
    }
  }

  @Override
  public LineItem get(Long id) {
    return null;
  }

  @Override
  public List<LineItem> getAll() {
    return List.of();
  }

  @Override
  public void update(LineItem lineItem) {}

  @Override
  public void delete(Long id) {}
}
