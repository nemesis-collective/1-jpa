package org.qiyana.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@Getter
@Setter
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "customer_id", nullable = false)
  @Setter(AccessLevel.NONE)
  private Customer customer;

  @Enumerated(EnumType.STRING)
  private Status status;

  private BigDecimal totalOrderPrice;

  @OneToMany(
      mappedBy = "order",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  @Setter(AccessLevel.NONE)
  private List<LineItem> lineItems;

  public Order(Customer customer, Status status) {
    this.customer = customer;
    this.lineItems = new ArrayList<>();
    this.status = status;
    this.totalOrderPrice = calculateTotalPrice();
  }

  private BigDecimal calculateTotalPrice() {
    return lineItems.stream()
        .map(LineItem::getTotalItemPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public void addLineItem(LineItem item) {
    lineItems.add(item);
    totalOrderPrice = calculateTotalPrice();
  }

  public enum Status {
    PROCESSING,
    SHIPPED,
    DELIVERED
  }

  @Override
  public String toString() {
    return "Order{" + "id=" + id + ", customer=" + customer + ", lineItems=" + lineItems + '}';
  }
}
