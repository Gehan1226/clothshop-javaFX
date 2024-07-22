package org.example.dao.custom.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.dao.custom.EmployeeDao;
import org.example.dto.Employee;
import org.example.entity.EmployeeEntity;
import org.example.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.modelmapper.ModelMapper;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class EmployeeDaoImpl implements EmployeeDao {
    private Session session;
    private Transaction transaction;

    private void beginSession() {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        transaction = session.beginTransaction();
    }

    private void closeSession() {
        if (transaction != null && transaction.isActive()) {
            transaction.commit();
        }
        if (session != null && session.isOpen()) {
            session.close();
        }
    }

    @Override
    public List<Employee> retrieveByEmail(String email) {
        List<Employee> employeeList = new ArrayList<>();
        try{
            beginSession();

            Query<EmployeeEntity> query = session.createQuery(
                    "from EmployeeEntity " +
                    "where email = :email",
                    EmployeeEntity.class
            );
            query.setParameter("email", email);
            List<EmployeeEntity> employeeEntityList = query.getResultList();

            if (!employeeEntityList.isEmpty()){
                employeeList.add(new ModelMapper().map(employeeEntityList.get(0), Employee.class));
            }
        }catch (HibernateException e){
            log.error("Error retrieveByEmail in EmployeeDaoImpl : HibernateException occurred.", e);
        } catch (Exception e) {
            log.error("Error retrieveByEmail in EmployeeDaoImpl : An unexpected error occurred.", e);
        }finally {
            closeSession();
        }
        return employeeList;
    }

    @Override
    public boolean save(EmployeeEntity dto) {
        try {
            beginSession();
            session.persist(dto);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("Error save in EmployeeDaoImpl : HibernateException occurred.", e);
            return false;
        }catch (Exception e) {
            log.error("Error save in EmployeeDaoImpl : An unexpected error occurred.", e);
        }finally {
            closeSession();
        }
        return true;
    }

    @Override
    public Employee retrieveById(String id){
        Employee employee = null;
        try {
            beginSession();

            EmployeeEntity employeeEntity = session.get(EmployeeEntity.class, id);
            if (employeeEntity != null){
                employee = new ModelMapper().map(employeeEntity, Employee.class);
            }
        }catch (HibernateException e) {
            log.error("Error retrieve in EmployeeDaoImpl : HibernateException occurred.", e);
        }catch (Exception e) {
            log.error("Error retrieve in EmployeeDaoImpl : An unexpected error occurred.", e);
        } finally {
            closeSession();
        }
        return employee;
    }

    @Override
    public Employee retrieveLastRow() {
        Employee employee = null;
        try {
            beginSession();

            Query<EmployeeEntity> query = session.createQuery(
                    "from EmployeeEntity " +
                    "order by id DESC",
                    EmployeeEntity.class
            );
            query.setMaxResults(1);
            EmployeeEntity employeeEntity= query.uniqueResult();

            if (employeeEntity!=null){
                employee = new ModelMapper().map(employeeEntity, Employee.class);
            }
        }catch (HibernateException e) {
            log.error("Error retrieveLastRow in EmployeeDaoImpl : HibernateException occurred.", e);
        }catch (Exception e) {
            log.error("Error retrieveLastRow in EmployeeDaoImpl : An unexpected error occurred.", e);
        } finally {
            closeSession();
        }
        return employee;
    }

    @Override
    public boolean update(EmployeeEntity employeeEntity){
        try {
            beginSession();

            EmployeeEntity savedObject = session.get(EmployeeEntity.class, employeeEntity.getEmpID());
            if (!savedObject.getOrderList().isEmpty()){
                savedObject.setFirstName(employeeEntity.getFirstName());
                savedObject.setLastName(employeeEntity.getLastName());
                savedObject.setMobileNumber(employeeEntity.getMobileNumber());
                savedObject.setNic(employeeEntity.getNic());
                savedObject.setDistrict(employeeEntity.getDistrict());
                savedObject.setProvince(employeeEntity.getProvince());
                savedObject.setEmail(employeeEntity.getEmail());
                session.merge(savedObject);
            }else {
                session.merge(employeeEntity);
            }

            transaction.commit();
        }catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("Error update in EmployeeDaoImpl : HibernateException occurred.", e);
            return false;
        }catch (Exception e) {
            log.error("Error update in EmployeeDaoImpl : An unexpected error occurred.", e);
        } finally {
            closeSession();
        }
        return true;
    }

    @Override
    public boolean delete(String empID) {
        try {
            beginSession();

            MutationQuery mutationQuery = session.createMutationQuery(
                    "delete from " +
                    "EmployeeEntity where empID = :primaryKeyValue"
            );
            mutationQuery.setParameter("primaryKeyValue",empID);
            mutationQuery.executeUpdate();

            transaction.commit();
        }catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("Error delete in EmployeeDaoImpl : HibernateException occurred.", e);
            return false;
        }catch (Exception e) {
            log.error("Error update in EmployeeDaoImpl : An unexpected error occurred.", e);
        } finally {
            closeSession();
        }
        return true;
    }

    @Override
    public List<Employee> retrieveAll() {
        List<Employee> employeeList = new ArrayList<>();
        try {
            beginSession();

            Query<EmployeeEntity> query = session.createQuery("from EmployeeEntity", EmployeeEntity.class);
            List<EmployeeEntity> resultList = query.getResultList();

            for (EmployeeEntity entity : resultList) {
                employeeList.add(new ModelMapper().map(entity, Employee.class));
            }
        } catch (HibernateException e) {
            log.error("Error retrieveAll in EmployeeDaoImpl : HibernateException occurred.", e);
        }catch (Exception e) {
            log.error("Error retrieveAll in EmployeeDaoImpl : An unexpected error occurred.", e);
        }  finally {
            closeSession();
        }
        return employeeList;
    }
}
