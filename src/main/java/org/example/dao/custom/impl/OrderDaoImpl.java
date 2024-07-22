package org.example.dao.custom.impl;


import lombok.extern.slf4j.Slf4j;
import org.example.dao.custom.OrderDao;
import org.example.dto.Order;
import org.example.entity.CustomerEntity;
import org.example.entity.OrderEntity;
import org.example.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.modelmapper.ModelMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OrderDaoImpl implements OrderDao {
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
    public Order retrieveLastRow() {
        Order order = null;
        try {
            beginSession();

            Query<OrderEntity> query = session.createQuery("from OrderEntity order by id DESC", OrderEntity.class);
            query.setMaxResults(1);
            OrderEntity orderEntity = query.uniqueResult();

            if (orderEntity != null) {
                order = new ModelMapper().map(orderEntity, Order.class);
            }
        } catch (HibernateException e) {
            log.error("Error retrieveLastRow in OrderDaoImpl : HibernateException occurred.", e);
        } catch (Exception e) {
            log.error("Error retrieveLastRow in OrderDaoImpl : An unexpected error occurred.", e);
        }  finally {
            closeSession();
        }
        return order;
    }

    @Override
    public Order retrieveById(String orderID) {
        Order order = null;
        try {
            beginSession();

            OrderEntity orderEntity = session.get(OrderEntity.class, orderID);
            if (orderEntity != null) {
                order = new ModelMapper().map(orderEntity, Order.class);
            }
        } catch (HibernateException e) {
            log.error("Error retrieve in OrderDaoImpl : HibernateException occurred.", e);
        } catch (Exception e) {
            log.error("Error retrieve in OrderDaoImpl : An unexpected error occurred.", e);
        } finally {
            closeSession();
        }
        return order;
    }

    @Override
    public boolean save(CustomerEntity customerEntity) {
        try {
            beginSession();
            session.merge(customerEntity);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("Error save in OrderDaoImpl : HibernateException occurred.", e);
            return false;
        }catch (Exception e) {
            log.error("Error save in OrderDaoImpl : An unexpected error occurred.", e);
        } finally {
            closeSession();
        }
        return true;
    }

    @Override
    public boolean delete(String orderId) {
        try {
            beginSession();

            String hql = "DELETE FROM OrderEntity WHERE orderID = :orderID";
            session.createMutationQuery(hql)
                    .setParameter("orderID", orderId)
                    .executeUpdate();

            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("Error delete in OrderDaoImpl : HibernateException occurred.", e);
            return false;
        } catch (Exception e) {
            log.error("Error delete in OrderDaoImpl : An unexpected error occurred.", e);
        }
        return true;
    }

    @Override
    public List<Order> retrieveAll() {
        List<Order> orderList = new ArrayList<>();
        try {
            beginSession();

            Query<OrderEntity> query = session.createQuery("from OrderEntity", OrderEntity.class);
            List<OrderEntity> resultList = query.getResultList();
            for (OrderEntity order : resultList) {
                orderList.add(new ModelMapper().map(order, Order.class));
            }
        } catch (HibernateException e) {
            log.error("Error retrieveAll in OrderDaoImpl : HibernateException occurred.", e);
        } catch (Exception e) {
            log.error("Error retrieveAll in OrderDaoImpl : An unexpected error occurred.", e);
        }finally {
            closeSession();
        }
        return orderList;
    }

    @Override
    public List<Order> retrieveByDate(LocalDate date) {
        List<Order> orderList = new ArrayList<>();
        try {
            beginSession();

            Query<OrderEntity> query = session.createQuery("from OrderEntity where orderDate = :date", OrderEntity.class);
            query.setParameter("date", date);
            List<OrderEntity> resultList = query.getResultList();
            for (OrderEntity order : resultList) {
                orderList.add(new ModelMapper().map(order, Order.class));
            }
        } catch (HibernateException e) {
            log.error("Error retrieveByDate in OrderDaoImpl : HibernateException occurred.", e);
        } catch (Exception e) {
            log.error("Error retrieveByDate in OrderDaoImpl : An unexpected error occurred.", e);
        } finally {
            closeSession();
        }
        return orderList;
    }

    @Override
    public List<Order> retrieveByYear(LocalDate startDate, LocalDate endDate) {
        List<Order> orderList = new ArrayList<>();
        try {
            beginSession();

            String hql = "FROM OrderEntity e WHERE e.orderDate BETWEEN :startDate AND :endDate";
            List<OrderEntity> resultList = session.createQuery(hql, OrderEntity.class)
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .getResultList();

            for (OrderEntity order : resultList) {
                orderList.add(new ModelMapper().map(order, Order.class));
            }
        } catch (HibernateException e) {
            log.error("Error retrieveByYear in OrderDaoImpl : HibernateException occurred.", e);
        } catch (Exception e) {
            log.error("Error retrieveByYear in OrderDaoImpl : An unexpected error occurred.", e);
        } finally {
            closeSession();
        }
        return orderList;
    }
}
