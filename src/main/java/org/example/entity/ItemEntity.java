package org.example.entity;


import lombok.*;
import jakarta.persistence.*;
import org.example.dto.Order;
import org.example.dto.Supplier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class ItemEntity {
    @Id
    private String itemId;
    private String itemName;
    private String size;
    private Double price;
    private Integer qtyOnHand;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemEntity that = (ItemEntity) o;
        return Objects.equals(itemId, that.itemId) && Objects.equals(itemName, that.itemName) && Objects.equals(size, that.size) && Objects.equals(price, that.price) && Objects.equals(qtyOnHand, that.qtyOnHand) && Objects.equals(categorie, that.categorie) && Objects.equals(itemImagePath, that.itemImagePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, itemName, size, price, qtyOnHand, categorie, itemImagePath);
    }

    private String categorie;
    private String itemImagePath;

    @ManyToMany(mappedBy = "itemList")
    private List<SupplierEntity> supplierList = new ArrayList<>();

    @ManyToMany(mappedBy = "itemList")
    private List<OrderEntity> orderList = new ArrayList<>();

    public void clearSupplierList(){
        for (SupplierEntity supplierEntity : supplierList){
            supplierEntity.removeItem(this);
        }
        supplierList.clear();
    }

    public void addSupplier(SupplierEntity supplierEntity){
        supplierList.add(supplierEntity);
        supplierEntity.getItemList().add(this);
    }


}
