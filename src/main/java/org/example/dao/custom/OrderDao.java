package org.example.dao.custom;

import org.example.dao.SuperDao;
import org.example.dto.Order;
import org.example.entity.CustomerEntity;
import org.example.entity.OrderEntity;

import java.time.LocalDate;
import java.util.List;

public interface OrderDao extends SuperDao {
    Order retrieveLastRow();
    Order retrieve(String orderID);
    boolean save(CustomerEntity customerEntity);
    boolean delete(String orderId);
    List<Order> retrieveAll();
    List<Order> retrieveByDate(LocalDate date);
    List<Order> retrieveByYear(LocalDate startDate, LocalDate endDate);
}

