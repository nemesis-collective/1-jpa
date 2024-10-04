package org.qiyana.model;

import java.math.BigDecimal;
import java.util.Currency;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class LineItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
  private Long id;

  private Integer quantity;
  private BigDecimal totalItemPrice;
  private Currency currency;

  @ManyToOne
  @JoinColumn(name = "order_id")
  @Setter(AccessLevel.NONE)
  private Order order;

  @OneToOne
  @JoinColumn(name = "product_id", referencedColumnName = "id")
  @Setter(AccessLevel.NONE)
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
