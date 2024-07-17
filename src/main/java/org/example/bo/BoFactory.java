package org.example.bo;

import org.example.bo.custom.impl.*;
import org.example.dao.custom.impl.ItemDaoImpl;
import org.example.util.BoType;

public class BoFactory {
    private static BoFactory instance;
    private BoFactory(){}

    public static BoFactory getInstance(){
        return instance != null ? instance:(instance = new BoFactory());
    }
    public <T extends SuperBo>T getBo(BoType type){
        switch (type){
            case EMPLOYEE:return (T) new EmployeeBoImpl();
            case USER:return (T) new UserBoImpl();
            case VALIDATE:return (T) new DataValidationBoImpl();
            case ITEM:return (T) new ItemBoImpl();
            case SUPPLIER:return (T) new SupplierBoImpl();
            case ORDER:return (T) new OrderBoImpl();
            case SALES:return (T) new SalesReportBoImpl();
        }
        return null;
    }
}

