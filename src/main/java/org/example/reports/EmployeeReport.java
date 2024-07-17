package org.example.reports;


import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.example.dto.Employee;
import org.example.util.PdfUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class EmployeeReport {
    private EmployeeReport(){}

    public static boolean genarateEmployeeReport(List<Employee> employeeList) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    "src/main/resources/jrxmlFiles/employeeReport.jrxml");
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employeeList);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
            JasperExportManager.exportReportToPdfFile(jasperPrint, "pdf/employeeReport.pdf");
            PdfUtil.openPdf("pdf/employeeReport.pdf");
            return true;
        } catch (JRException e) {
            log.error("Error generating employee report: JRException occurred.", e);
        }catch (Exception e) {
            log.error("Error generating employee report : An unexpected error occurred.", e);
        }
        return false;
    }
}

