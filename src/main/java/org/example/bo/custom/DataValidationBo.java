package org.example.bo.custom;

import org.example.bo.SuperBo;

public interface DataValidationBo extends SuperBo {
    boolean isValidEmail(String email);
    boolean isValidPassword(String password);
    boolean isValidMobileNumber(String mobileNo);
    boolean isAllFieldsNotEmpty(String ...values);
    boolean isValidEmpID(String empID);
}
