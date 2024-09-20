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
import org.qiyana.model.Product;

/** Class responsible for database operations at product entity. */
@Slf4j
public class ProductDao implements Dao<Product> {
  EntityManager em;
  CriteriaBuilder cb;

  public ProductDao(EntityManagerFactory etf) {
    em = etf.createEntityManager();
    cb = em.getCriteriaBuilder();
  }

  /**
   * Uses an entity manager to insert a product into the database.
   *
   * @param product the product object who will be added at database.
   */
  @Override
  public void add(Product product) {
    em.getTransaction().begin();
    em.persist(product);
    em.getTransaction().commit();
  }

  /**
   * Uses an entity manager to insert a product into the database.
   *
   * @param id the product id who will be searched at database.
   * @return a product object.
   */
  @Override
  public Product get(Long id) {
    return em.find(Product.class, id);
  }

  /**
   * Uses an entity manager to search all products into the database.
   *
   * @return a product list.
   */
  @Override
  public List<Product> getAll() {
    CriteriaQuery<Product> cq = cb.createQuery(Product.class);

    Root<Product> root = cq.from(Product.class);

    cq.select(root);

    return em.createQuery(cq).getResultList();
  }

  /**
   * Uses an entity manager to update a product into the database.
   *
   * @param product the product who will be updated at database.
   */
  @Override
  public void update(Product product) {
    em.getTransaction().begin();
    em.merge(product);
    em.getTransaction().commit();
  }

  /**
   * Uses an entity manager to delete a product into the database.
   *
   * @param id the product id who will be deleted at database.
   */
  @Override
  public void delete(Long id) {
    em.getTransaction().begin();
    Product product = em.find(Product.class, id);
    em.remove(product);
    em.getTransaction().commit();
  }
}
