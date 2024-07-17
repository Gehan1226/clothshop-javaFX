package org.example.bo.custom;

import org.example.bo.SuperBo;
import org.example.dto.Employee;

public interface EmployeeBo extends SuperBo {
     boolean isEmployee(String email);

     boolean save(Employee employee);

     String genarateEmployeeID();

     Employee retrieveByEmail(String email);

     Employee retrieveById(String empID);

     boolean replace(Employee employee);

     boolean deleteEmployee(Employee employee);

     boolean genarateEmployeeReport();
}

