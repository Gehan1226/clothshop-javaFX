package org.example.bo.custom;

import org.example.bo.SuperBo;
import org.example.dto.Employee;
import org.example.dto.User;

public interface UserBo extends SuperBo {
    String saveUser(User dto) ;
    boolean hasAdmin();
    boolean loginRequest(User dto);
    boolean sendOTPTo(String email);
    boolean isUser(String email);
    boolean isEqualsOTP(Integer otpByUser);
    String updatePassword(String email, String password);
    boolean confirmPassword(String email,String password);
    public String updateEmail(String oldEmail, String newEmail);
    boolean deleteUserAccount(Employee employee);
}
