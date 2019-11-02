package com.example.cookbook.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.cookbook.models.User;

@Dao
public interface UserDao {

    /*@Query("SELECT * FROM User WHERE id = :userId")
    LiveData<User> getUser(long userId);*/
}
