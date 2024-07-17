package org.example.bo.custom.impl;

import org.example.bo.BoFactory;
import org.example.bo.custom.EmployeeBo;
import org.example.bo.custom.UserBo;
import org.example.dao.Daofactory;
import org.example.dao.custom.EmployeeDao;
import org.example.dao.custom.UserDao;
import org.example.dto.Employee;
import org.example.dto.User;
import org.example.entity.UserEntity;
import org.example.reports.EmployeeReport;
import org.example.util.BoType;
import org.example.util.DaoType;
import org.example.util.EmailUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class UserBoImpl implements UserBo {
    private Integer lastOTP;
    private final UserDao userDao = Daofactory.getInstance().getDao(DaoType.USER);
    private  final EmployeeBo employeeBo = BoFactory.getInstance().getBo(BoType.EMPLOYEE);

    @Override
    public boolean confirmPassword(String email,String password ){
        List<User> users = userDao.retrieveUserListByEmail(email);
        return  users.get(0).getPassword().equals(passwordEncryption(password));
    }
    @Override
    public String updateEmail(String oldEmail, String newEmail) {
        if (userDao.updateUserEmail(oldEmail, newEmail)) {
            return "✅ Email Change Successfully !";
        }
        return "❌ Email Change Failed !";
    }

    @Override
    public String updatePassword(String email, String password) {
        if (userDao.updateUserPassword(email, passwordEncryption(password))) {
            return "✅ Password Change Successfully !";
        }
        return "❌ Password Change Failed !";
    }

    @Override
    public boolean isEqualsOTP(Integer otpByUser) {
        return Objects.equals(lastOTP, otpByUser);
    }

    @Override
    public boolean sendOTPTo(String email) {
        String body = "Your OTP Code - " + genarateOTP();
        return EmailUtil.sendEmail(email,"Clothify Shop OTP Verification !", body);
    }

    @Override
    public boolean loginRequest(User dto) {
        dto.setPassword(passwordEncryption(dto.getPassword()));
        List<User> users = userDao.retrieveUserListByEmail(dto.getEmail());
        if (!users.isEmpty()) {
            return users.get(0).equals(dto);
        }
        return false;
    }

    @Override
    public boolean hasAdmin() {
        return userDao.hasAdmin();
    }

    @Override
    public String saveUser(User dto) {
        dto.setPassword(passwordEncryption(dto.getPassword()));
        if (Boolean.TRUE.equals(dto.getIsAdmin())) {
            if (!hasAdmin()) {
                boolean value = userDao.save(new ModelMapper().map(dto, UserEntity.class));
                return value ? "Admin Account Created Successfully!" : "Admin Account Create Failed !";
            }
            return "Admin User already exist!";
        } else {
            if (!employeeBo.isEmployee(dto.getEmail())) {
                return dto.getEmail() + " is not a register email";
            }
            if (userDao.retrieveUserListByEmail(dto.getEmail()).isEmpty()) {
                userDao.save(new ModelMapper().map(dto, UserEntity.class));
                return "User Account Created Successfully!";
            }
            return "User Account Alrady Exist !";
        }
    }
    @Override
    public boolean isUser(String email) {
        return !userDao.retrieveUserListByEmail(email).isEmpty();
    }

    private String passwordEncryption(String password) {
        Logger logger = LoggerFactory.getLogger(UserBoImpl.class);

        String encryptedpassword = null;
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(password.getBytes());
            byte[] bytes = m.digest();
            StringBuilder s = new StringBuilder();
            for (byte aByte : bytes) {
                s.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            encryptedpassword = s.toString();

        } catch (NoSuchAlgorithmException e) {
            logger.error("Error passwordEncryption method : NoSuchAlgorithmException occurred.", e);
        }
        return encryptedpassword;
    }

    private Integer genarateOTP() {
        Random random = new Random(System.currentTimeMillis());
        lastOTP = 10000 + random.nextInt(50000);
        return lastOTP;
    }

    @Override
    public boolean deleteUserAccount(Employee employee){
        return userDao.delete(employee.getEmail());
    }

}
