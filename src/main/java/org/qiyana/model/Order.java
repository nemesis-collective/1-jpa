package org.qiyana.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@Getter
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "customer_id", nullable = false)
  private Customer customer;

  @Enumerated(EnumType.STRING)
  private Status status;

  private String paymentMethod;

  @OneToMany(
      mappedBy = "order",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private List<LineItem> lineItems;

  public Order(Long id, Customer customer, Status status, String paymentMethod) {
    this.customer = customer;
    this.id = id;
    this.lineItems = new ArrayList<>();
    this.status = status;
    this.paymentMethod = paymentMethod;
  }

  public void addLineItem(LineItem item) {
    lineItems.add(item);
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
