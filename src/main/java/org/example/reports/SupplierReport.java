package org.example.reports;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.example.dto.Supplier;
import org.example.util.PdfUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class SupplierReport {
    private SupplierReport(){}

    public static boolean genarateSupplierReport(List<Supplier> supplierList) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    "src/main/resources/jrxmlFiles/supplierReport.jrxml");
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(supplierList);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
            JasperExportManager.exportReportToPdfFile(jasperPrint, "pdf/supplierReport.pdf");
            PdfUtil.openPdf("pdf/supplierReport.pdf");
            return true;
        }catch (JRException e) {
            log.error("Error generating supplier report: JRException occurred.", e);
        } catch (Exception e) {
            log.error("Error generating supplier report : An unexpected error occurred.", e);
        }
        return false;
    }
}
