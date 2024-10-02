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
import org.qiyana.model.Customer;

/** Class responsible for database operations at customer entity. */
@Slf4j
public class CustomerDao implements Dao<Customer> {
  EntityManager em;

  public CustomerDao(EntityManagerFactory etf) {
    em = etf.createEntityManager();
  }

  /**
   * Uses an entity manager to insert a customer in the database.
   *
   * @param customer the customer object that will be added to the database.
   */
  @Override
  public void add(@NonNull Customer customer) {
    em.getTransaction().begin();
    em.persist(customer);
    em.getTransaction().commit();
  }

  /**
   * Uses an entity manager to insert a customer in the database.
   *
   * @param id the customer id that will be searched in the database.
   * @return a customer object.
   */
  @Override
  public Customer get(@NonNull Long id) {
    return em.find(Customer.class, id);
  }

  /**
   * Uses an entity manager to search all customers in the database.
   *
   * @return a customer list.
   */
  @Override
  public List<Customer> getAll() {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);

    Root<Customer> root = cq.from(Customer.class);

    cq.select(root);

    return em.createQuery(cq).getResultList();
  }

  /**
   * Uses an entity manager to update a customer in the database.
   *
   * @param customer the customer that will be updated in the database.
   */
  @Override
  public void update(@NonNull Customer customer) {
    em.getTransaction().begin();
    em.merge(customer);
    em.getTransaction().commit();
  }

  /**
   * Uses an entity manager to delete a customer in the database.
   *
   * @param id the customer id that will be deleted in the database.
   */
  @Override
  public void delete(@NonNull Long id) {
    em.getTransaction().begin();
    Customer customer = em.find(Customer.class, id);
    em.remove(customer);
    em.getTransaction().commit();
  }
}
