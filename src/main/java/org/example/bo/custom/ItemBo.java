package org.example.bo.custom;

import org.example.bo.SuperBo;
import org.example.dto.Item;

import java.util.List;

public interface ItemBo extends SuperBo {
    String genarateItemID();
    boolean saveItem(Item item, List<String> supIDS);
    List<String> getAllIDSAndNames();
    Item retrieveById(String id);
    boolean updateItem(Item item,List<String> supllierIDS);
    boolean deleteItem(String itemID);
    List<Item> getAllItems();
    boolean genarateInventoryReport();
}
