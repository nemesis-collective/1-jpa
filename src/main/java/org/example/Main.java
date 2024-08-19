package org.example;

import org.example.imp.OrderDAOImp;
import org.example.model.Customer;

import org.example.model.LineItem;
import org.example.model.Orders;
import org.example.model.Product;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Customer customer = new Customer(null,"QiyanaTech","test@gmail.com","Rua Francisco Duarte","123456789","test123");

        Product product = new Product(null,"Hair Spray","300 ml",5,"dollars");
        Product product1 = new Product(null,"Water Bottle","500 ml",10,"dollars");

        Orders order = new Orders(null,customer);

        LineItem lineItem = new LineItem(null,2,10,"dollars",product, order);
        LineItem lineItem1 = new LineItem(null,3,30,"dollars",product1, order);

        List<LineItem> list = new ArrayList<>();

        list.add(lineItem);
        list.add(lineItem1);

        OrderDAOImp orderDAOImp = new OrderDAOImp();

        orderDAOImp.addOrder(order,customer,list);

        }
    }
