package org.example.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class LineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer quantity;
    private double totalPrice;
    private String currency;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders orders;

    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    public LineItem(Integer id, Integer quantity, double totalPrice, String currency, Product product, Orders order) {
        this.id = id;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.currency = currency;
        this.product = product;
        this.orders = order;

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
