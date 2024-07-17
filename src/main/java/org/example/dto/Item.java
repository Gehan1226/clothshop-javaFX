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

public class Item {
    private String itemId;
    private String itemName;
    private String size;
    private Double price;
    private Integer qtyOnHand;
    private String categorie;
    private String itemImagePath;
    private List<Supplier> supplierList = new ArrayList<>();
    private List<Order> orderList = new ArrayList<>();

    public Item(String itemId, String itemName, String size, Double price, Integer qtyOnHand,String categorie,
                String itemImagePath) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.size = size;
        this.price = price;
        this.qtyOnHand = qtyOnHand;
        this.categorie = categorie;
        this.itemImagePath = itemImagePath;
    }

    public void addOrder(Order order) {
        orderList.add(order);
    }

    public void addSupplier(Supplier supplier){
        supplierList.add(supplier);
        supplier.getItemList().add(this);
    }
}
