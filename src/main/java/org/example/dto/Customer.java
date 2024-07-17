package org.example.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString

public class Customer {
    private String name;
    private String email;
    private String mobileNumber;
    private List<Order> orderList = new ArrayList<>();
    public Customer(String name,  String email, String mobileNumber){
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
    }
    public void addOrder(Order order) {
        orderList.add(order);
        order.setCustomer(this);
    }
    public void removeOrder(Order order) {
        orderList.remove(order);
        order.setCustomer(null);
    }
}
