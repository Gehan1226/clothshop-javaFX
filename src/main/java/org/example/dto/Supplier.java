package org.example.dto;

import lombok.*;

import javax.management.ConstructorParameters;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Supplier {
    private String supID;
    private String firstName;
    private String lastName;
    private String company;
    private String email;
    private String mobileNumber;
    private List<Item> itemList = new ArrayList<>();

    public Supplier(String supID, String firstName, String lastName, String company,String email,String mobileNumber){
        this.supID = supID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.company = company;
        this.email = email;
        this.mobileNumber = mobileNumber;
    }


    public void addItem(Item item){
        itemList.add(item);
        item.addSupplier(this);
    }

}
