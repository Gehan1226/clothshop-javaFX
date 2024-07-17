package org.example.bo.custom.impl;

import net.sf.jasperreports.engine.JRException;
import org.example.bo.custom.OrderBo;
import org.example.dao.Daofactory;
import org.example.dao.custom.EmployeeDao;
import org.example.dao.custom.ItemDao;
import org.example.dao.custom.OrderDao;
import org.example.dto.Customer;
import org.example.dto.Employee;
import org.example.dto.Item;
import org.example.dto.Order;
import org.example.entity.CustomerEntity;
import org.example.entity.OrderEntity;
import org.example.util.DaoType;
import org.example.util.EmailUtil;
import org.example.reports.OrderReceipt;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Map;


public class OrderBoImpl implements OrderBo {
    private final OrderDao orderDao = Daofactory.getInstance().getDao(DaoType.ORDER);
    private final ItemDao itemDao = Daofactory.getInstance().getDao(DaoType.ITEM);
    private final EmployeeDao employeeDao = Daofactory.getInstance().getDao(DaoType.EMPLOYEE);

    @Override
    public String genarateOrderID() {
        Order order = orderDao.retrieveLastRow();
        if (order != null) {
            return "O" + (Integer.parseInt(order.getOrderID().substring(1)) + 1);
        }
        return "O1";
    }

    @Override
    public boolean saveOrder(Order order, Customer customer, Map<String, List<Object>> itemMap, String employeeId) {
        List<Object> itemIDs = itemMap.get("ItemIDs");
        List<Object> itemQtys = itemMap.get("ItemQtys");

        for (int i = 0; i < itemIDs.size(); i++) {
            Item item = itemDao.retrieve(String.valueOf(itemIDs.get(i)));
            order.addItem(item, (Integer) itemQtys.get(i));
        }
        Employee employee = employeeDao.retrieve("E1");
        order.setEmployee(employee);
        CustomerEntity customerEntity = new ModelMapper().map(customer, CustomerEntity.class);
        OrderEntity orderEntity = new ModelMapper().map(order, OrderEntity.class);

        if (orderDao.save(customerEntity)) {
            sendRecieptToEmail(order);
            return true;
        }
        return false;
    }

    @Override
    public Order getOrder(String orderID) {
        return orderDao.retrieve(orderID);
    }

    @Override
    public boolean deleteOrder(String orderID) {
        return orderDao.delete(orderID);
    }

    private void sendRecieptToEmail(Order order) {
        OrderReceipt.generateReceipt(order);
        EmailUtil.sendEmail(
                order.getCustomer().getEmail(),
                "Cloth Shop Order Receipt",
                "order receipt pdf",
                "orderReceipt.pdf"
        );
    }
}
