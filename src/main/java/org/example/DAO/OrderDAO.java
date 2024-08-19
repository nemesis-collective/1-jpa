package org.example.DAO;

import org.example.model.Customer;
import org.example.model.LineItem;
import org.example.model.Orders;

import java.util.List;

public interface OrderDAO {
    void addOrder(Orders order, Customer customer, List<LineItem> lineItems);
    void getOrders();
    void updateOrder(int id);
    void deleteOrder();
}
