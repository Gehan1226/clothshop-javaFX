package org.example.dao.custom;

import org.example.dao.SuperDao;
import org.example.dto.Supplier;
import org.example.entity.SupplierEntity;

import java.util.List;

public interface SupplierDao extends SuperDao {
    List<Supplier> retrieveAll();
    Supplier retrieveLastRow();
    boolean save(SupplierEntity supplierEntity,List<String> itemIDS) ;
    boolean update(SupplierEntity dto,List<String> itemIDS);
    boolean delete(String ID);
    Supplier retrieve(String supplierID);
}
