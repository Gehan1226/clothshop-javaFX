package org.example.reports;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.example.dto.Item;
import org.example.dto.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

@Slf4j
public class OrderReceipt {
    private OrderReceipt(){}

    public static void generateReceipt(Order order){
        try{
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    "src/main/resources/jrxmlFiles/orderReceipt.jrxml");
            JRBeanCollectionDataSource dataSource = getJrBeanCollectionDataSource(order);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("orderID", order.getOrderID());
            parameters.put("orderDate", order.getOrderDate().toString());
            parameters.put("fullPrice", order.getFullPrice());

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            JasperExportManager.exportReportToPdfFile(jasperPrint, "orderReceipt.pdf");
        }catch (JRException e) {
            log.error("Error generating order receipt : JRException occurred.", e);
        }catch (Exception e) {
            log.error("Error generating order receipt : An unexpected error occurred.", e);
        }
    }

    private static JRBeanCollectionDataSource getJrBeanCollectionDataSource(Order order) {
        List<Map<String, Object>> itemListData = new ArrayList<>();

        for (int i = 0; i < order.getItemList().size(); i++) {
            Item item = order.getItemList().get(i);
            Map<String, Object> itemData = new HashMap<>();
            itemData.put("itemName", item.getItemName());
            itemData.put("itemSize", item.getSize());
            itemData.put("itemPrice", item.getPrice());
            itemData.put("itemQuantity", order.getItemQtyList().get(i));
            itemListData.add(itemData);
        }
        return new JRBeanCollectionDataSource(itemListData);
    }
}
