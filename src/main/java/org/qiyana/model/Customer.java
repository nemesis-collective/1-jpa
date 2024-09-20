package org.qiyana.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Customer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String email;
  private String address;
  private String phone;

  @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Order> orders = new ArrayList<>();

  public Customer(Long id, String name, String email, String address, String phone) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.address = address;
    this.phone = phone;
  }

  public void addOrder(Order order) {
    orders.add(order);
  }

  @Override
  public String toString() {
    return "Customer{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", email='"
        + email
        + '\''
        + ", address='"
        + address
        + '\''
        + ", phone='"
        + phone
        + '\''
        + '}';
  }
}
