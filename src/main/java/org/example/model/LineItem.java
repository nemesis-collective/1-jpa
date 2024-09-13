package org.example.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class LineItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Integer quantity;
  private double totalPrice;
  private String currency;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;

  @OneToOne
  @JoinColumn(name = "product_id", referencedColumnName = "id")
  private Product product;

  public LineItem(
      Long id, Integer quantity, double totalPrice, String currency, Product product, Order order) {
    this.id = id;
    this.quantity = quantity;
    this.totalPrice = totalPrice;
    this.currency = currency;
    this.product = product;
    this.order = order;
  }

  @Override
  public String toString() {
    return "LineItem{"
        + "id="
        + id
        + ", quantity="
        + quantity
        + ", totalPrice="
        + totalPrice
        + ", currency='"
        + currency
        + '\''
        + ", product="
        + product
        + '}';
  }
}
