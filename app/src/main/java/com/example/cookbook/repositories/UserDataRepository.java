package com.example.cookbook.repositories;

import com.example.cookbook.database.dao.UserDao;

public class UserDataRepository {

    private UserDao userDao;

    public UserDataRepository(UserDao userDao) { this.userDao = userDao; }


}
