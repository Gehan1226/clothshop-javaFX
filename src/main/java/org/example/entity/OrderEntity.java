package org.example.entity;



import jakarta.persistence.*;
import lombok.*;
import org.example.dto.Employee;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString

public class OrderEntity {
    @Id
    private String orderID;
    private LocalDate orderDate;
    private Double fullPrice;
    private String payementType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerID")
    private CustomerEntity customer;
    @ManyToMany(cascade = {CascadeType.ALL })
    @JoinTable(
            name = "order_item",
            joinColumns = { @JoinColumn(name = "orderID") },
            inverseJoinColumns = { @JoinColumn(name = "itemId") }
    )
    private List<ItemEntity> itemList = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empID")
    private EmployeeEntity employee;
    private List<String> data;
    private List<Integer> itemQtyList;
}
