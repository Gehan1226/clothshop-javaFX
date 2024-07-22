package org.example.dao;

import org.example.dao.custom.impl.*;
import org.example.util.DaoType;

public class Daofactory {

    private static Daofactory instance;

    private Daofactory() {
    }

    public static Daofactory getInstance() {
        if (instance != null) return instance;
        instance = new Daofactory();
        return instance;
    }

    public <T extends SuperDao> T getDao(DaoType type) {
        switch (type) {
            case USER:
                return (T) new UserDaoImpl();
            case EMPLOYEE:
                return (T) new EmployeeDaoImpl();
            case ITEM:
                return (T) new ItemDaoImpl();
            case SUPPLIER:
                return (T) new SupplierDaoImpl();
            case ORDER:
                return (T) new OrderDaoImpl();
            default:
                throw new IllegalArgumentException("Invalid DoType: " + type);
        }
    }
}
