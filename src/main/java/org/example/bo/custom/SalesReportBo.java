package org.example.bo.custom;

import org.example.bo.SuperBo;

public interface SalesReportBo extends SuperBo {
    boolean genarateDailyReport();
    boolean genarateMonthlyReport();
    boolean genaratYearlyReport();
}
