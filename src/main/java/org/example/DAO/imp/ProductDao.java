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
import org.example.model.Order;
import org.example.model.Product;

/** Class responsible for database operations at product entity. */
@Slf4j
public class ProductDao implements Dao<Product> {

  EntityManagerFactory etf;
  EntityManager em;
  CriteriaBuilder cb;

  public ProductDao(EntityManagerFactory etf) {
    this.etf = etf;
    em = etf.createEntityManager();
    cb = em.getCriteriaBuilder();
  }

  /**
   * Uses an entity manager to insert a product into the database.
   *
   * @param  product the product object who will be added at database.
   */
  @Override
  public void add(Product product) {
    try {
      em.getTransaction().begin();
      em.persist(product);
    } catch (IllegalArgumentException | TransactionRequiredException e) {
      log.error("Unable to add product.");
    } finally {
      em.getTransaction().commit();
    }
  }

  /**
   * Uses an entity manager to insert a product into the database.
   *
   * @param  id the product id who will be searched at database.
   * @return a product object.
   */
  @Override
  public Product get(Long id) {
    Product product = null;
    try {
      em.getTransaction().begin();
      product = em.find(Product.class, id);
      if(product == null){
        throw new IllegalArgumentException();
      }
    } catch (IllegalArgumentException e) {
      log.error("Unable to get product.");
    }
    return product;
  }

  /**
   * Uses an entity manager to search all products into the database.
   *
   * @return a product list.
   */
  @Override
  public List<Product> getAll() {
    List<Product> list = List.of();
    try {
      em.getTransaction().begin();
      CriteriaQuery<Product> cq = cb.createQuery(Product.class);

      Root<Product> root = cq.from(Product.class);

      cq.select(root);

      list = em.createQuery(cq).getResultList();
    } catch (Exception e) {
      log.error("Unable to get all products.");
    }

    return list;
  }

  /**
   * Uses an entity manager to update a product into the database.
   *
   * @param  product the product  who will be updated at database.
   */
  @Override
  public void update(Product product) {
    try {
      em.getTransaction().begin();
      Product product1 = em.find(Product.class, product.getId());
      if (product1 == null) {
        throw new IllegalArgumentException();
      }
      CriteriaUpdate<Product> update = cb.createCriteriaUpdate(Product.class);
      Root<Product> root = update.from(Product.class);

      update.set(root.get("name"), product.getName());
      update.set(root.get("description"), product.getDescription());
      update.set(root.get("price"), product.getPrice());
      update.set(root.get("currency"), product.getCurrency());

      update.where(cb.equal(root.get("id"), product.getId()));

      em.createQuery(update).executeUpdate();
    } catch (Exception e) {
      log.error("Unable to update product.");
    } finally {
      em.getTransaction().commit();
    }
  }

  /**
   * Uses an entity manager to delete a product into the database.
   *
   * @param  id the product id who will be deleted at database.
   */
  @Override
  public void delete(Long id) {
    try {
      em.getTransaction().begin();
      Product product = em.find(Product.class, id);
      em.remove(product);
    } catch (IllegalArgumentException | TransactionRequiredException e) {
      log.error("Unable to delete product.");
    } finally {
      em.getTransaction().commit();
    }
  }
}
