package org.example.model;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "customer_id",nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    private List<LineItem> lineItems = new ArrayList<>();

    public Orders(){

    }

    public Orders(Integer id,Customer customer) {
        this.customer = customer;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", customer=" + customer +
                ", lineItems=" + lineItems +
                '}';
    }
}
