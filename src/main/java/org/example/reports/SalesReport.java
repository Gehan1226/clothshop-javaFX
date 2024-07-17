package org.example.reports;


import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.example.dto.sales.DailySales;
import org.example.dto.sales.MonthlySales;
import org.example.dto.sales.YearlySales;
import org.example.util.PdfUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class SalesReport {
    private SalesReport(){}

    public static boolean genarateDailySalesReport(List<DailySales> dailySalesList) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    "src/main/resources/jrxmlFiles/dailySalesReport.jrxml");
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dailySalesList);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
            JasperExportManager.exportReportToPdfFile(jasperPrint, "pdf/DailySalesReport.pdf");
            PdfUtil.openPdf("pdf/DailySalesReport.pdf");
            return true;
        } catch (JRException e) {
            log.error("Error generating daily sales report: JRException occurred.", e);
        } catch (Exception e) {
            log.error("Error generating daily sales report: An unexpected error occurred.", e);
        }
        return false;
    }

    public static boolean genarateMonthlySalesReport(List<MonthlySales> monthlySalesList) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    "src/main/resources/jrxmlFiles/monthlySalesReport.jrxml");
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(monthlySalesList);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("currentYear", String.valueOf(LocalDate.now().getYear()));

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            JasperExportManager.exportReportToPdfFile(jasperPrint, "pdf/MonthlySalesReport.pdf");
            PdfUtil.openPdf("pdf/MonthlySalesReport.pdf");
            return true;
        } catch (JRException e) {
            log.error("Error generating monthly sales report: JRException occurred.", e);
        } catch (Exception e) {
            log.error("Error generating monthly sales report: An unexpected error occurred.", e);
        }
        return false;
    }

    public static boolean genarateYearlySalesReport(List<YearlySales> yearlySalesList) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    "src/main/resources/jrxmlFiles/yearlySalesReport.jrxml");
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(yearlySalesList);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
            JasperExportManager.exportReportToPdfFile(jasperPrint, "pdf/YearlySalesReport.pdf");
            PdfUtil.openPdf("pdf/YearlySalesReport.pdf");
            return true;
        } catch (JRException e) {
            log.error("Error generating yearly sales report: JRException occurred.", e);
        } catch (Exception e) {
            log.error("Error generating yearly sales report: An unexpected error occurred.", e);
        }
        return false;
    }
}
