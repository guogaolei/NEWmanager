package com.ghl.manage.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ghl.manage.entity.table.UserEntity;

@Repository
public interface UserDao {
    UserEntity getUser(String userName);
    List<UserEntity> getAllUser();
}
