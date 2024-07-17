package org.example.dao.custom.impl;


import lombok.extern.slf4j.Slf4j;
import org.example.dao.custom.SupplierDao;
import org.example.dto.Supplier;
import org.example.entity.ItemEntity;
import org.example.entity.SupplierEntity;
import org.example.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.modelmapper.ModelMapper;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SupplierDaoImpl implements SupplierDao {
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
    public List<Supplier> retrieveAll() {
        List<Supplier> supplierList = new ArrayList<>();
        try {
            beginSession();

            Query<SupplierEntity> query = session.createQuery("from SupplierEntity", SupplierEntity.class);
            List<SupplierEntity> resultList = query.getResultList();
            for (SupplierEntity entity : resultList) {
                supplierList.add(new ModelMapper().map(entity, Supplier.class));
            }
        } catch (HibernateException e) {
            log.error("Error retrieveAll in SupplierDaoImpl : HibernateException occurred.", e);
        } catch (Exception e) {
            log.error("Error retrieveAll in SupplierDaoImpl : An unexpected error occurred.", e);
        }  finally {
            closeSession();
        }
        return supplierList;
    }

    @Override
    public Supplier retrieve(String supplierID) {
        Supplier supplier = null;
        try {
            beginSession();

            SupplierEntity supplierEntity = session.get(SupplierEntity.class, supplierID);
            if (supplierEntity!=null){
                supplier = new ModelMapper().map(supplierEntity, Supplier.class);
            }
        } catch (HibernateException e) {
            log.error("Error retrieve in SupplierDaoImpl : HibernateException occurred.", e);
        } catch (Exception e) {
            log.error("Error retrieve in SupplierDaoImpl : An unexpected error occurred.", e);
        }  finally {
            closeSession();
        }
        return supplier;
    }

    @Override
    public Supplier retrieveLastRow() {
        Supplier supplier = null;
        try {
            beginSession();

            Query<SupplierEntity> query = session.createQuery(
                    "from SupplierEntity order by id DESC", SupplierEntity.class
            );
            query.setMaxResults(1);
            SupplierEntity supplierEntity = query.uniqueResult();
            if (supplierEntity != null) {
                supplier = new ModelMapper().map(supplierEntity, Supplier.class);
            }
        } catch (HibernateException e) {
            log.error("Error retrieveLastRow in SupplierDaoImpl : HibernateException occurred.", e);
        } catch (Exception e) {
            log.error("Error retrieveLastRow in SupplierDaoImpl : An unexpected error occurred.", e);
        } finally {
            closeSession();
        }
        return supplier;
    }

    @Override
    public boolean save(SupplierEntity supplierEntity,List<String> itemIDS) {
        try {
            beginSession();

            if (!itemIDS.isEmpty()) {
                for (String id : itemIDS) {
                    ItemEntity itemEntity = session.get(ItemEntity.class, id);
                    if (itemEntity != null){
                        itemEntity.addSupplier(supplierEntity);
                        session.merge(supplierEntity);
                    }
                }
            }else {
                session.persist(supplierEntity);
            }
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("Error save in SupplierDaoImpl : HibernateException occurred.", e);
            return false;
        } catch (Exception e) {
            log.error("Error save in SupplierDaoImpl : An unexpected error occurred.", e);
            return false;
        }finally {
            closeSession();
        }
        return true;
    }

    @Override
    public boolean update(SupplierEntity dto,List<String> itemIDS) {
        try {
            beginSession();

            SupplierEntity supplierEntity = session.get(SupplierEntity.class, dto.getSupID());
            if (supplierEntity != null){
                new ModelMapper().map(dto,supplierEntity);
                for (String id : itemIDS) {
                    ItemEntity itemEntity = session.get(ItemEntity.class, id);
                    if (itemEntity != null && (!itemEntity.getSupplierList().contains(supplierEntity))) {
                            itemEntity.getSupplierList().add(supplierEntity);
                            supplierEntity.getItemList().add(itemEntity);

                    }
                }
            }
            session.merge(supplierEntity);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("Error update in SupplierDaoImpl : HibernateException occurred.", e);
            return false;
        }catch (Exception e) {
            log.error("Error update in SupplierDaoImpl : An unexpected error occurred.", e);
            return false;
        } finally {
            closeSession();
        }
        return true;
    }

    @Override
    public boolean delete(String id) {
        try  {
            beginSession();

            String hql = "DELETE FROM SupplierEntity WHERE supID = :supId";
            session.createMutationQuery(hql)
                    .setParameter("supId", id)
                    .executeUpdate();

            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("Error delete in SupplierDaoImpl : HibernateException occurred.", e);
            return false;
        }catch (Exception e) {
            log.error("Error update in SupplierDaoImpl : An unexpected error occurred.", e);
            return false;
        }
        return true;
    }
}
