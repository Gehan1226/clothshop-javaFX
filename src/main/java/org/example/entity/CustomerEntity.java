package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerID;
    private String name;
    private String email;
    private String mobileNumber;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderEntity> orderList = new ArrayList<>();

    public void addOrder(OrderEntity order) {
        orderList.add(order);
        order.setCustomer(this);
    }

    public void removeOrder(OrderEntity orderEntity) {
        orderList.remove(orderEntity);
        orderEntity.setCustomer(null);
    }
}
