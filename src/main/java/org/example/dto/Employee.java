package org.example.dto;

import lombok.*;
import org.example.entity.OrderEntity;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Employee {
    private String empID;
    private String firstName;
    private String lastName;
    private String nic;
    private String mobileNumber;
    private String province;
    private String district;
    private String email;
    private List<Order> orderList = new ArrayList<>();

    public Employee(String empID, String firstName, String lastName,
                    String nic, String mobileNumber, String province, String district, String email) {
        this.empID = empID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nic = nic;
        this.mobileNumber = mobileNumber;
        this.province = province;
        this.district = district;
        this.email = email;
    }

    public void addOrder(Order order) {
        orderList.add(order);
    }
}
