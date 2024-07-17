package org.example.bo.custom;

import org.example.bo.SuperBo;
import org.example.dto.Customer;
import org.example.dto.Order;

import java.util.List;
import java.util.Map;

public interface OrderBo extends SuperBo {
    String genarateOrderID();
    boolean saveOrder(Order order, Customer customer, Map<String, List<Object>> itemMap, String employeeId) ;
    Order getOrder(String orderID);
    boolean deleteOrder(String orderID);
}
