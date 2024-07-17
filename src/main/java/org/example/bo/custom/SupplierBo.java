package org.example.bo.custom;

import org.example.bo.SuperBo;
import org.example.dto.Supplier;

import java.util.List;

public interface SupplierBo extends SuperBo {
    List<String> getAllIDSAndNames();
    String genarateSupplierID();
    boolean saveSupplier(Supplier supplier, List<String> itemIDS);
    Supplier retrieveById(String id);
    boolean updateSupplier(Supplier supplier,List<String> itemIDS);
    boolean deleteSupplier(String id);
    boolean genarateSupplierReport();
}
