package org.example.imp;

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
  public void update(Product product) {
    try {
      Product product1 = em.find(Product.class, product.getId());
      if (product1 == null) {
        throw new IllegalArgumentException();
      }
      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaUpdate<Order> update = cb.createCriteriaUpdate(Order.class);
      Root<Order> root = update.from(Order.class);

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

  @Override
  public void delete(Long id) {
    try {
      Product product = em.find(Product.class, id);
      em.remove(product);
    } catch (IllegalArgumentException | TransactionRequiredException e) {
      log.error("Unable to delete product.");
    } finally {
      em.getTransaction().commit();
    }
  }
}
