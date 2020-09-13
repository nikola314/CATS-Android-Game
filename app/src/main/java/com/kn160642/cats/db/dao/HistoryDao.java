package com.kn160642.cats.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kn160642.cats.db.Entities.Box;
import com.kn160642.cats.db.Entities.History;

import java.util.List;
@Dao
public abstract class HistoryDao {

    @Insert
    public abstract long insertStats(History b);

    @Query("SELECT * FROM history WHERE userId = :userId")
    public abstract LiveData<List<History>> getStatsForUser(long userId);

    @Query("SELECT * FROM history WHERE userId = :userId")
    public abstract List<History> getStatsForUserDead(long userId);

    @Query("SELECT COUNT(*) FROM history WHERE userId = :userId AND result = 0")
    public abstract int getCountOfGamesWon(long userId);


    @Update
    public abstract void updateStats(History u);

}