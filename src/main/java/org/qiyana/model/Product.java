package org.qiyana.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Currency;

@Entity
@NoArgsConstructor
@Getter
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String description;
  private double price;
  private Currency currency;

  public Product(Long id, String name, String description, double price, String currencyCode) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
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
