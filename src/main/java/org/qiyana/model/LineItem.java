package org.qiyana.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Currency;

@Entity
@NoArgsConstructor
@Getter
public class LineItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Integer quantity;
  private double totalItemPrice;
  private Currency currency;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;

  @OneToOne
  @JoinColumn(name = "product_id", referencedColumnName = "id")
  private Product product;

  public LineItem(
      Long id, Integer quantity,String currencyCode, Product product, Order order) {
    this.id = id;
    this.quantity = quantity;
    this.product = product;
    this.totalItemPrice = calculateTotalItemPrice();
    this.currency = Currency.getInstance(currencyCode);
    this.order = order;
  }

  private double calculateTotalItemPrice() {
    return product.getPrice() * quantity;
  }

  @Override
  public String toString() {
    return "LineItem{"
        + "id="
        + id
        + ", quantity="
        + quantity
        + ", totalPrice="
        + totalItemPrice
        + ", currency='"
        + currency
        + '\''
        + ", product="
        + product
        + '}';
  }
}
