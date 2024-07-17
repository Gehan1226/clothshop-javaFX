package org.example.dao.custom.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.dao.custom.UserDao;
import org.example.dto.User;
import org.example.entity.UserEntity;
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
public class UserDaoImpl implements UserDao {
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
    public boolean hasAdmin() {
        List<User> userList = new ArrayList<>();
        try {
            beginSession();

            Query<UserEntity> query = session.createQuery(
                    "from UserEntity where isAdmin = :boolean", UserEntity.class);
            query.setParameter("boolean", true);
            List<UserEntity> userEntities = query.getResultList();

            for (UserEntity userEntity : userEntities) {
                userList.add(new ModelMapper().map(userEntity, User.class));
            }
        } catch (HibernateException e) {
            log.error("Error hasAdmin in UserDaoImpl : HibernateException occurred.", e);
        } catch (Exception e) {
            log.error("Error hasAdmin in UserDaoImpl : An unexpected error occurred.", e);
        } finally {
            closeSession();
        }
        return !userList.isEmpty();
    }
    @Override
    public boolean save(UserEntity entity) {
        try {
            beginSession();
            session.persist(entity);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("Error save in UserDaoImpl : HibernateException occurred.", e);
            return false;
        } catch (Exception e) {
            log.error("Error save in UserDaoImpl : An unexpected error occurred.", e);
        }finally {
            closeSession();
        }
        return true;
    }
    @Override
    public List<User> retrieveUserListByEmail(String email) {
        List<User> userList = new ArrayList<>();
        try {
            beginSession();

            Query<UserEntity> query = session.createQuery(
                    "from UserEntity " +
                    "where email = :userEmail",
                    UserEntity.class
            );
            query.setParameter("userEmail", email);
            List<UserEntity> userEntityList = query.getResultList();

            for (UserEntity entity : userEntityList) {
                userList.add(new ModelMapper().map(entity, User.class));
            }
        } catch (HibernateException e) {
            log.error("Error retrieveUser in UserDaoImpl : HibernateException occurred.", e);
        } catch (Exception e) {
            log.error("Error retrieveUser in UserDaoImpl : An unexpected error occurred.", e);
        }finally {
            closeSession();
        }
        return userList;
    }
    @Override
    public boolean updateUserPassword(String email, String password) {
        try {
            beginSession();

            MutationQuery mutationQuery = session.createMutationQuery(
                    "update UserEntity " +
                    "set password=:password" +
                    " where email=:email"
            );
            mutationQuery.setParameter("password", password);
            mutationQuery.setParameter("email", email);
            mutationQuery.executeUpdate();

            transaction.commit();
        }catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("Error updateUserPassword in UserDaoImpl : HibernateException occurred.", e);
            return false;
        } catch (Exception e) {
            log.error("Error updateUserPassword in UserDaoImpl : An unexpected error occurred.", e);
        }finally {
            closeSession();
        }
        return true;
    }
    @Override
    public boolean updateUserEmail(String oldEmail, String newEmail) {
        try {
            beginSession();

            MutationQuery mutationQuery = session.createMutationQuery(
                    "update UserEntity " +
                     "set email=:newEmail " +
                     "where email=:oldEmail"
            );
            mutationQuery.setParameter("newEmail", newEmail);
            mutationQuery.setParameter("oldEmail", oldEmail);
            mutationQuery.executeUpdate();

            transaction.commit();
        }catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("Error updateuserEmail in UserDaoImpl : HibernateException occurred.", e);
            return false;
        }catch (Exception e) {
            log.error("Error updateuserEmail in UserDaoImpl : An unexpected error occurred.", e);
        }finally {
            closeSession();
        }
        return true;
    }
    @Override
    public boolean delete(String email) {
        try {
            beginSession();

            MutationQuery mutationQuery = session.createMutationQuery("delete from UserEntity where email = :email");
            mutationQuery.setParameter("email",email);
            mutationQuery.executeUpdate();

            transaction.commit();
        }catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("Error delete in UserDaoImpl : HibernateException occurred.", e);
            return false;
        }catch (Exception e) {
            log.error("Error delete in UserDaoImpl : An unexpected error occurred.", e);
        } finally {
            closeSession();
        }
        return true;
    }
}
