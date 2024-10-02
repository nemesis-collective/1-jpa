package org.qiyana.model;

import java.math.BigDecimal;
import java.util.Currency;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String description;
  private BigDecimal price;
  private Currency currency;

  public Product(String name, String description, String price, String currencyCode) {
    this.name = name;
    this.description = description;
    this.price = new BigDecimal(price);
    this.currency = Currency.getInstance(currencyCode);
  }

  @Override
  public String toString() {
    return "Product{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", description='"
        + description
        + '\''
        + ", price="
        + price
        + ", currency='"
        + currency
        + '\''
        + '}';
  }
}
