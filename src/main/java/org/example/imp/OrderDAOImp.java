package org.example.imp;

import org.example.DAO.OrderDAO;
import org.example.model.Customer;
import org.example.model.LineItem;
import org.example.model.Orders;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class OrderDAOImp implements OrderDAO {

    EntityManagerFactory etf = Persistence.createEntityManagerFactory("persistence");
    EntityManager em = etf.createEntityManager();

    public OrderDAOImp(){
        em.getTransaction().begin();
    }

    @Override
    public void addOrder(Orders order, Customer customer, List<LineItem> lineItems) {
        em.persist(customer);
        em.persist(order);

        for (LineItem item : lineItems){
            em.persist(item.getProduct());
            em.persist(item);
        }

        em.getTransaction().commit();
    }

    @Override
    public void getOrders() {

    }

    @Override
    public void updateOrder(int id) {

    }

    @Override
    public void deleteOrder() {

    }
}
