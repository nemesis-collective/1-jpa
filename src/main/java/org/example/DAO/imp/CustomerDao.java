package org.example.DAO.imp;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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
  EntityManager em;
  CriteriaBuilder cb;

  public CustomerDao(EntityManagerFactory etf) {
    em = etf.createEntityManager();
    cb = em.getCriteriaBuilder();
  }

  /**
   * Uses an entity manager to insert a customer into the database.
   *
   * @param customer the customer object who will be added at database.
   */
  @Override
  public void add(Customer customer) {
    em.getTransaction().begin();
    em.persist(customer);
    em.getTransaction().commit();
  }

  /**
   * Uses an entity manager to insert a customer into the database.
   *
   * @param id the customer id who will be searched at database.
   * @return a customer object.
   */
  @Override
  public Customer get(Long id) {
    return em.find(Customer.class, id);
  }

  /**
   * Uses an entity manager to search all customers into the database.
   *
   * @return a customer list.
   */
  @Override
  public List<Customer> getAll() {
    CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);

    Root<Customer> root = cq.from(Customer.class);

    cq.select(root);

    return em.createQuery(cq).getResultList();
  }

  /**
   * Uses an entity manager to update a customer into the database.
   *
   * @param customer the customer who will be updated at database.
   */
  @Override
  public void update(Customer customer) {
    em.getTransaction().begin();

    CriteriaUpdate<Customer> update = cb.createCriteriaUpdate(Customer.class);
    Root<Customer> root = update.from(Customer.class);

    update.set(root.get("name"), customer.getName());
    update.set(root.get("email"), customer.getEmail());
    update.set(root.get("address"), customer.getAddress());
    update.set(root.get("phone"), customer.getPhone());

    update.where(cb.equal(root.get("id"), customer.getId()));

    em.createQuery(update).executeUpdate();
    em.getTransaction().commit();
  }

  /**
   * Uses an entity manager to delete a customer into the database.
   *
   * @param id the customer id who will be deleted at database.
   */
  @Override
  public void delete(Long id) {
    em.getTransaction().begin();
    Customer customer = em.find(Customer.class, id);
    em.remove(customer);
    em.getTransaction().commit();
  }
}
