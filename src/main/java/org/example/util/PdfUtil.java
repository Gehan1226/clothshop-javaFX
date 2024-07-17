package org.example.util;

import lombok.extern.slf4j.Slf4j;
import org.example.reports.SalesReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;

@Slf4j
public class PdfUtil {
    private PdfUtil(){}

    public static void openPdf(String pdfFilePath) {
        if (Desktop.isDesktopSupported()) {
            try {
                File pdfFile = new File(pdfFilePath);
                if (pdfFile.exists()) {
                    Desktop.getDesktop().open(pdfFile);
                } else {
                    log.error("The file {} does not exist.",pdfFilePath);
                }
            } catch (IOException e) {
                log.error("Error open pdf : IOException occurred.", e);
            }
        } else {
            log.error("Desktop is not supported on this platform.");
        }
    }
}
