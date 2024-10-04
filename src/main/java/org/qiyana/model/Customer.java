package org.qiyana.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Customer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
  private Long id;

  private String name;
  private String email;
  private String address;
  private String phone;

  @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Order> orders;

  public Customer(String name, String email, String address, String phone) {
    this.name = name;
    this.email = email;
    this.address = address;
    this.phone = phone;
    this.orders = new ArrayList<>();
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
