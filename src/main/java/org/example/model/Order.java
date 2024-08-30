package org.example.model;

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

  @OneToMany(
      mappedBy = "order",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private List<LineItem> lineItems;

  public Order(Long id, Customer customer) {
    this.customer = customer;
    this.id = id;
    this.lineItems = new ArrayList<>();
  }

  public void addLineItem(LineItem item) {
    lineItems.add(item);
  }

  @Override
  public String toString() {
    return "Order{" + "id=" + id + ", customer=" + customer + ", lineItems=" + lineItems + '}';
  }
}
