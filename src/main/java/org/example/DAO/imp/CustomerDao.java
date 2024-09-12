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
import org.example.model.Customer;

/** Class responsible for database operations at customer entity. */
@Slf4j
public class CustomerDao implements Dao<Customer> {
  EntityManagerFactory etf;
  EntityManager em;
  CriteriaBuilder cb;

  public CustomerDao(EntityManagerFactory etf) {
    this.etf = etf;
    em = etf.createEntityManager();
    cb = em.getCriteriaBuilder();
    em.getTransaction().begin();
  }

  @Override
  public void add(Customer customer) {
    try {
      em.persist(customer);
    } catch (IllegalArgumentException | TransactionRequiredException e) {
      log.error("Unable to add customer.{}",e.getMessage());
    }finally{
      em.getTransaction().commit();
    }
  }

  @Override
  public Customer get(Long id) {
    Customer customer = null;
    try {
      customer = em.find(Customer.class, id);
      if(customer == null){
        throw new IllegalArgumentException();
      }
    } catch (IllegalArgumentException e) {
      log.error("Unable to get customer.");
    }
    return customer;
  }

  @Override
  public List<Customer> getAll() {
    List<Customer> list = List.of();
    try {
      CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);

      Root<Customer> root = cq.from(Customer.class);

      cq.select(root);

      list = em.createQuery(cq).getResultList();
    } catch (Exception e) {
      log.error("Unable to get all customers.");
    }

    return list;
  }

  @Override
  public void update(Customer customer) {
    try {
     Customer customer1 = em.find(Customer.class, customer.getId());
      if (customer1 == null) {
        throw new IllegalArgumentException();
      }
      CriteriaUpdate<Customer> update = cb.createCriteriaUpdate(Customer.class);
      Root<Customer> root = update.from(Customer.class);

      update.set(root.get("name"),customer.getName());
      update.set(root.get("email"), customer.getEmail());
      update.set(root.get("address"), customer.getAddress());
      update.set(root.get("phone"), customer.getPhone());

      update.where(cb.equal(root.get("id"), customer.getId()));

      em.createQuery(update).executeUpdate();
    } catch (Exception e) {
      log.error("Unable to update customer.");
    } finally {
      em.getTransaction().commit();
    }
  }

  @Override
  public void delete(Long id) {
    try {
      Customer customer = em.find(Customer.class, id);
      em.remove(customer);
    } catch (IllegalArgumentException | TransactionRequiredException e) {
      log.error("Unable to delete customer.");
    } finally {
      em.getTransaction().commit();
    }
  }
}
