package org.example.model;

import javax.persistence.*;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private double price;
    private String currency;

    public Product(){

    }

    public Product(Integer id, String name, String description, double price, String currency) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.currency = currency;

    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }
}
