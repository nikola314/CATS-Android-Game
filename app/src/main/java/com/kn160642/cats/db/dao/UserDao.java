package com.kn160642.cats.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.kn160642.cats.db.Entities.User;

import java.util.List;

@Dao
public abstract class UserDao {

    @Insert
    public abstract void insertUser(User u);

    @Query("SELECT * FROM user")
    public abstract LiveData<List<User>> getAllUsers();

    @Query("UPDATE user SET highScore = :score where userId=:id")
    public abstract void updateScore(long id, int score);
}
