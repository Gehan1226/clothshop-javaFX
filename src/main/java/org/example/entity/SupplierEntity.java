package org.example.entity;


import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Entity
@ToString
public class SupplierEntity {
    @Id
    private String supID;
    private String firstName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SupplierEntity that = (SupplierEntity) o;
        return Objects.equals(supID, that.supID) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(company, that.company) && Objects.equals(email, that.email) && Objects.equals(mobileNumber, that.mobileNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(supID, firstName, lastName, company, email, mobileNumber);
    }

    private String lastName;
    private String company;
    private String email;
    private String mobileNumber;
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "supplier_item",
            joinColumns = { @JoinColumn(name = "supID") },
            inverseJoinColumns = { @JoinColumn(name = "itemId") }
    )
    private List<ItemEntity> itemList= new ArrayList<>();

    public void addItem(ItemEntity itemEntity){
        itemList.add(itemEntity);
        itemEntity.addSupplier(this);
    }
    public void removeItem(ItemEntity itemEntity){
        itemList.remove(itemEntity);
        itemEntity.getSupplierList().remove(this);
    }
}
