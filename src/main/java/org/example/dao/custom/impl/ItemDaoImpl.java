package org.example.dao.custom.impl;


import lombok.extern.slf4j.Slf4j;
import org.example.dao.custom.ItemDao;
import org.example.dto.Item;
import org.example.dto.Supplier;
import org.example.entity.ItemEntity;
import org.example.entity.SupplierEntity;
import org.example.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ItemDaoImpl implements ItemDao {
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
    public Item retrieveLastRow() {
        Item item = null;
        try {
            beginSession();

            Query<ItemEntity> query = session.createQuery("from ItemEntity order by id DESC", ItemEntity.class);
            query.setMaxResults(1);
            ItemEntity itemEntity = query.uniqueResult();

            if (itemEntity != null) {
                item = new ModelMapper().map(itemEntity, Item.class);
            }
        } catch (HibernateException e) {
            log.error("Error retrieveByEmail in ItemDaoImpl : HibernateException occurred.", e);
        } catch (Exception e) {
            log.error("Error retrieveByEmail in ItemDaoImpl : An unexpected error occurred.", e);
        } finally {
            closeSession();
        }
        return item;
    }

    @Override
    public boolean save(ItemEntity itemEntity, List<String> supplierIDS) {
        try {
            beginSession();

            if (!itemEntity.getSupplierList().isEmpty()) {
                for (SupplierEntity supplierEntity : itemEntity.getSupplierList()) {
                    session.merge(supplierEntity);
                }
            } else {
                session.persist(itemEntity);
            }
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("Error save in ItemDaoImpl : HibernateException occurred.", e);
            return false;
        }catch (Exception e) {
            log.error("Error retrieveByEmail in ItemDaoImpl : An unexpected error occurred.", e);
        } finally {
            closeSession();
        }
        return true;
    }

    @Override
    public List<Item> retrieveAll() {
        List<Item> itemList = new ArrayList<>();
        try {
            beginSession();

            Query<ItemEntity> query = session.createQuery("from ItemEntity", ItemEntity.class);
            List<ItemEntity> resultList = query.getResultList();
            for (ItemEntity entity : resultList) {
                itemList.add(new ModelMapper().map(entity, Item.class));
            }
        } catch (HibernateException e) {
            log.error("Error retrieveAll in ItemDaoImpl : HibernateException occurred.", e);
        }catch (Exception e) {
            log.error("Error retrieveAll in ItemDaoImpl : An unexpected error occurred.", e);
        }finally {
            closeSession();
        }
        return itemList;
    }

    @Override
    public Item retrieveById(String id) {
        Item item = null;
        try {
            beginSession();

            ItemEntity itemEntity = session.get(ItemEntity.class, id);
            if (itemEntity != null) {
                item = new ModelMapper().map(itemEntity, Item.class);

                String hql = "SELECT s FROM SupplierEntity s JOIN s.itemList i WHERE i.itemId = :itemId";
                Query<SupplierEntity> query = session.createQuery(hql, SupplierEntity.class);
                query.setParameter("itemId", id);
                List<SupplierEntity> supplierEntities = query.getResultList();

                List<Supplier> suppliers = supplierEntities.stream()
                        .map(supplierEntity -> new ModelMapper().map(supplierEntity, Supplier.class))
                        .collect(Collectors.toList());

                item.setSupplierList(suppliers);
            }
        } catch (HibernateException e) {
            log.error("Error retrieve in ItemDaoImpl : HibernateException occurred.", e);
        }catch (Exception e) {
            log.error("Error retrieve in ItemDaoImpl : An unexpected error occurred.", e);
        }finally {
            closeSession();
        }
        return item;
    }

    @Override
    public boolean update(ItemEntity itemEntity,List<SupplierEntity> supplierEntityList) {
        try {
            beginSession();

            ItemEntity oldItem = session.get(ItemEntity.class, itemEntity.getItemId());
            oldItem.setItemName(itemEntity.getItemName());
            oldItem.setSize(itemEntity.getSize());
            oldItem.setPrice(itemEntity.getPrice());
            oldItem.setCategorie(itemEntity.getCategorie());
            oldItem.setQtyOnHand(itemEntity.getQtyOnHand());
            oldItem.setItemImagePath(itemEntity.getItemImagePath());

            for (SupplierEntity supplier : supplierEntityList) {
                if (!oldItem.getSupplierList().contains(supplier)){
                    oldItem.addSupplier(supplier);
                    session.merge(supplier);
                }
            }
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("Error update in ItemDaoImpl : HibernateException occurred.", e);
            return false;
        }catch (Exception e) {
            log.error("Error update in ItemDaoImpl : An unexpected error occurred.", e);
        } finally {
            closeSession();
        }
        return true;
    }

    @Override
    public boolean delete(String id) {
        try {
            beginSession();

            String hql = "DELETE FROM ItemEntity WHERE itemId = :itemId";
            session.createMutationQuery(hql)
                    .setParameter("itemId", id)
                    .executeUpdate();

            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("Error delete in ItemDaoImpl : HibernateException occurred.", e);
            return false;
        }catch (Exception e) {
            log.error("Error delete in ItemDaoImpl : An unexpected error occurred.", e);
        }
        return true;
    }
}
