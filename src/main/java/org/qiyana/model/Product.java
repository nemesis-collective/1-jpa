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
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
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
