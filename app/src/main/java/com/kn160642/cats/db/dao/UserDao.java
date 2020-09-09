package com.kn160642.cats.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.kn160642.cats.db.Entities.Box;
import com.kn160642.cats.db.Entities.User;

import java.util.List;

@Dao
public abstract class UserDao {

    @Insert
    public abstract long insertUser(User u);

    @Query("SELECT * FROM user")
    public abstract LiveData<List<User>> getAllUsers();

    @Query("SELECT userId FROM user WHERE username = :username")
    public abstract long getUserId(String username);

    @Query("SELECT * FROM user WHERE userId = :id")
    public abstract User getUserById(long id);

    @Update
    public abstract void updateUser(User u);

}
