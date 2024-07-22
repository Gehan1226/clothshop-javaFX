package org.example.bo.custom.impl;

import org.example.bo.custom.SalesReportBo;
import org.example.dao.Daofactory;
import org.example.dao.custom.OrderDao;
import org.example.dto.*;
import org.example.dto.sales.DailySales;
import org.example.dto.sales.MonthlySales;
import org.example.dto.sales.YearlySales;
import org.example.reports.SalesReport;
import org.example.util.DaoType;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalesReportBoImpl implements SalesReportBo {

    private final OrderDao orderDao = Daofactory.getInstance().getDao(DaoType.ORDER);

    @Override
    public boolean genarateDailyReport() {
        List<Order> orders = orderDao.retrieveByDate(LocalDate.now());
        List<DailySales> dailySalesList = new ArrayList<>();
        Map<String, DailySales> dailySalesMap = new HashMap<>();

        for (Order order : orders) {
            List<Item> itemListTemp = order.getItemList();

            for (int x = 0; x < itemListTemp.size(); x++) {
                DailySales dailySales = new DailySales(
                        order.getOrderDate(),
                        itemListTemp.get(x).getItemId(),
                        itemListTemp.get(x).getItemName(),
                        order.getItemQtyList().get(x),
                        itemListTemp.get(x).getPrice(),
                        itemListTemp.get(x).getPrice() * order.getItemQtyList().get(x)
                );
                if (!dailySalesList.contains(dailySales)) {
                    dailySalesList.add(dailySales);
                    dailySalesMap.put(dailySales.getItemID(), dailySales);
                } else {
                    DailySales existingSale = dailySalesMap.get(dailySales.getItemID());
                    existingSale.setQuantity(existingSale.getQuantity() + dailySales.getQuantity());
                    existingSale.setTotal(existingSale.getTotal() + dailySales.getTotal());
                    int index = dailySalesList.indexOf(existingSale);
                    dailySalesList.set(index, existingSale);
                    dailySalesMap.put(dailySales.getItemID(), dailySales);
                }
            }
        }

        DailySales maxQuantitySale = null;
        for (DailySales sale : dailySalesList) {
            if (maxQuantitySale == null || sale.getQuantity() > maxQuantitySale.getQuantity()) {
                maxQuantitySale = sale;
            }
        }
        String mostSoldItemIdOfDay = null;
        if (maxQuantitySale != null){
            mostSoldItemIdOfDay = maxQuantitySale.getItemID();
        }

        return SalesReport.genarateDailySalesReport(dailySalesList, mostSoldItemIdOfDay);
    }

    @Override
    public boolean genarateMonthlyReport() {
        int currentYear = LocalDate.now().getYear();
        LocalDate startDate = LocalDate.of(currentYear, 1, 1);
        LocalDate endDate = YearMonth.of(currentYear, 12).atEndOfMonth();

        List<Order> orders = orderDao.retrieveByYear(startDate, endDate);
        List<MonthlySales> monthlySalesList = new ArrayList<>();

        String[] months = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };

        for (String month : months) {
            monthlySalesList.add(new MonthlySales(month, 0.0));
        }
        for (Order order : orders) {
            MonthlySales monthlySales = monthlySalesList.get(order.getOrderDate().getMonthValue() - 1);
            monthlySales.setTotal(monthlySales.getTotal() + order.getFullPrice());
        }
        return SalesReport.genarateMonthlySalesReport(monthlySalesList);
    }

    @Override
    public boolean genaratYearlyReport() {
        List<Order> orders = orderDao.retrieveAll();
        List<YearlySales> yearlySalesList = new ArrayList<>();

        for (Order order : orders) {
            YearlySales yearlySales = new YearlySales(String.valueOf(order.getOrderDate().getYear()), order.getFullPrice());
            if (!yearlySalesList.contains(yearlySales)){
                yearlySalesList.add(yearlySales);
            }else {
                int i = yearlySalesList.indexOf(yearlySales);
                YearlySales existingObj = yearlySalesList.get(i);
                existingObj.setTotal(existingObj.getTotal() + yearlySales.getTotal());
            }
        }
        return SalesReport.genarateYearlySalesReport(yearlySalesList);
    }
}
