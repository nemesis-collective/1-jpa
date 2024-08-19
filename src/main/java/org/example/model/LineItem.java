package org.example.model;

import javax.persistence.*;

@Entity
public class LineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer quantity;
    private double totalPrice;
    private String currency;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_id")
    private Orders orders;

    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;


    public LineItem(){

    }

    public LineItem(Integer id, Integer quantity, double totalPrice, String currency, Product product, Orders order) {
        this.id = id;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.currency = currency;
        this.product = product;
        this.orders = order;

    }

    public Integer getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public Product getProduct() {
        return product;
    }

    @Override
    public String toString() {
        return "LineItem{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                ", currency='" + currency + '\'' +
                ", product=" + product +
                '}';
    }
}
