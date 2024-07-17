package org.example.bo.custom.impl;

import org.example.bo.custom.DataValidationBo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataValidationBoImpl implements DataValidationBo {
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean isValidEmpID(String empID) {
        return empID.matches("^E\\d+$");
    }

    @Override
    public boolean isAllFieldsNotEmpty(String... values) {
        try {
            for (String value : values) {
                if (value.isEmpty()) return false;
            }
            return true;
        }catch (NullPointerException e){
            return false;
        }
    }

    @Override
    public boolean isValidEmail(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.matches();
    }

    @Override
    public boolean isValidPassword(String password) {
        String regex = "^(?=.*\\d)(?=.*[a-zA-Z])(?=.*[@#$%^&+=]).{8,}$";
        return password.matches(regex);
    }

    @Override
    public boolean isValidMobileNumber(String mobileNo) {
        return mobileNo.length() == 10 && mobileNo.startsWith("07");
    }
}
