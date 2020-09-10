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
public abstract class BoxDao {

    @Insert
    public abstract long insertBox(Box b);

    @Query("SELECT * FROM box WHERE userId = :userId AND NOT opened")
    public abstract LiveData<List<Box>> getUnopenedBoxesForUser(long userId);

    @Query("SELECT * FROM box WHERE userId = :userId AND NOT opened")
    public abstract List<Box> getUnopenedBoxesForUserDeadData(long userId);

    @Update
    public abstract void updateBox(Box u);

}