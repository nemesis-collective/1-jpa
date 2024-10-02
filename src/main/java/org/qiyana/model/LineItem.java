package org.qiyana.model;

import java.math.BigDecimal;
import java.util.Currency;
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
  private BigDecimal totalItemPrice;
  private Currency currency;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;

  @OneToOne
  @JoinColumn(name = "product_id", referencedColumnName = "id")
  private Product product;

  public LineItem(Integer quantity, String currencyCode, Product product, Order order) {
    this.quantity = quantity;
    this.product = product;
    this.totalItemPrice = calculateTotalItemPrice();
    this.currency = Currency.getInstance(currencyCode);
    this.order = order;
  }

  private BigDecimal calculateTotalItemPrice() {
    return product.getPrice().multiply(BigDecimal.valueOf(quantity));
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
