package org.example.reports;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.example.dto.Item;
import org.example.util.PdfUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class InventoryReport {
    private InventoryReport(){}

    public static boolean genarateInventoryReport(List<Item> itemList) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    "src/main/resources/jrxmlFiles/inventoryReport.jrxml");
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(itemList);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
            JasperExportManager.exportReportToPdfFile(jasperPrint, "pdf/inventoryReport.pdf");
            PdfUtil.openPdf("pdf/inventoryReport.pdf");
            return true;
        }catch (JRException e) {
            log.error("Error generating inventory report: JRException occurred.", e);
        }catch (Exception e) {
            log.error("Error generating inventory report : An unexpected error occurred.", e);
        }
        return false;
    }
}
