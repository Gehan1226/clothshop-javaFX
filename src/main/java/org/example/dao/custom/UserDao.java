package org.example.dao.custom;

import org.example.dao.SuperDao;
import org.example.dto.User;
import org.example.entity.UserEntity;

import java.util.List;

public interface UserDao extends SuperDao {
    boolean save(UserEntity entity);
    boolean hasAdmin();
    List<User> retrieveUserListByEmail(String email);
    boolean updateUserPassword(String email, String password);
    boolean updateUserEmail(String oldEmail, String newEmail);
    boolean delete(String email);
}
