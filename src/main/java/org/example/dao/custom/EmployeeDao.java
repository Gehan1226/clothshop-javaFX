package org.example.dao.custom;

import org.example.dao.SuperDao;
import org.example.dto.Employee;
import org.example.entity.EmployeeEntity;

import java.util.List;

public interface EmployeeDao extends SuperDao {
    List<Employee> retrieveByEmail(String email);
    Employee retrieveLastRow();
    boolean save(EmployeeEntity dto);
    Employee retrieve(String id);
    boolean update(EmployeeEntity employeeEntity);
    boolean delete(String empID);
    List<Employee> retrieveAll();
}
