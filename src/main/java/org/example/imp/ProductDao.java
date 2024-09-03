package org.example.imp;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TransactionRequiredException;
import javax.persistence.criteria.CriteriaBuilder;
import lombok.extern.slf4j.Slf4j;
import org.example.DAO.Dao;
import org.example.model.Product;

@Slf4j
public class ProductDao implements Dao<Product> {

  EntityManagerFactory etf;
  EntityManager em;
  CriteriaBuilder cb;

  public ProductDao(EntityManagerFactory etf) {
    this.etf = etf;
    em = etf.createEntityManager();
    cb = em.getCriteriaBuilder();
    em.getTransaction().begin();
  }

  @Override
  public void add(Product product) {
    try {
      em.persist(product);
    } catch (IllegalArgumentException | TransactionRequiredException e) {
      log.error("Unable to add order. {}", e.getMessage());
    } finally {
      em.getTransaction().commit();
    }
  }

  @Override
  public Product get(Long id) {
    return null;
  }

  @Override
  public List<Product> getAll() {
    return List.of();
  }

  @Override
  public void update(Product product) {}

  @Override
  public void delete(Long id) {}
}
