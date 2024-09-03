package org.example.imp;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import org.example.DAO.Dao;
import org.example.model.Customer;

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
    em.persist(customer);
  }

  @Override
  public Customer get(Long id) {
    return null;
  }

  @Override
  public List<Customer> getAll() {
    return List.of();
  }

  @Override
  public void update(Customer customer) {}

  @Override
  public void delete(Long id) {}
}
