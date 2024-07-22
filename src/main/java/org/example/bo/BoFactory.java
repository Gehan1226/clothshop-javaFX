package org.example.bo;

import org.example.bo.custom.impl.*;
import org.example.util.BoType;

public class BoFactory {
    private static BoFactory instance;

    private BoFactory() {
    }

    public static BoFactory getInstance() {
        if (instance != null) return instance;
        instance = new BoFactory();
        return instance;
    }

    public <T extends SuperBo> T getBo(BoType type) {
        switch (type) {
            case EMPLOYEE:
                return (T) new EmployeeBoImpl();
            case USER:
                return (T) new UserBoImpl();
            case VALIDATE:
                return (T) new DataValidationBoImpl();
            case ITEM:
                return (T) new ItemBoImpl();
            case SUPPLIER:
                return (T) new SupplierBoImpl();
            case ORDER:
                return (T) new OrderBoImpl();
            case SALES:
                return (T) new SalesReportBoImpl();
            default:
                throw new IllegalArgumentException("Invalid BoType: " + type);
        }
    }
}

