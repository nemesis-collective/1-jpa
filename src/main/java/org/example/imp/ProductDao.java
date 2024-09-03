package org.example.imp;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TransactionRequiredException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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
      log.error("Unable to add product.");
    } finally {
      em.getTransaction().commit();
    }
  }

  @Override
  public Product get(Long id) {
    Product product = null;
    try {
      product = em.find(Product.class, id);
      if(product == null){
        throw new IllegalArgumentException();
      }
    } catch (IllegalArgumentException e) {
      log.error("Unable to get product.");
    }
    return product;
  }

  @Override
  public List<Product> getAll() {
    List<Product> list = List.of();
    try {
      CriteriaQuery<Product> cq = cb.createQuery(Product.class);

      Root<Product> root = cq.from(Product.class);

      cq.select(root);

      list = em.createQuery(cq).getResultList();
    } catch (Exception e) {
      log.error("Unable to get all products.");
    }

    return list;
  }

  @Override
  public void update(Product product) {}

  @Override
  public void delete(Long id) {}
}
