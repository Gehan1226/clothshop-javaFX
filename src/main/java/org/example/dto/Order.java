package org.example.dto;

import lombok.*;
import org.example.entity.EmployeeEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Getter
@Setter
@ToString
public class Order {
    private String orderID;
    private LocalDate orderDate;
    private Double fullPrice;
    private String payementType;
    private Customer customer;
    private List<Item> itemList = new ArrayList<>();
    private Employee employee;
    private List<Integer> itemQtyList  = new ArrayList<>();

    public Order(String orderID, LocalDate orderDate, Double fullPrice, String payementType){
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.fullPrice = fullPrice;
        this.payementType = payementType;
    }
    public void addItem(Item item,Integer qty){
        itemList.add(item);
        itemQtyList.add(qty);
        item.addOrder(this);
    }
    public void setEmployee(Employee employee){
        this.employee = employee;
        employee.addOrder(this);
    }
}


