package com.kn160642.cats.db.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "history")
public class History {

    @PrimaryKey(autoGenerate = true)
    private long historyId;

    @ColumnInfo(name="userId")
    private long userId;

    @ColumnInfo(name="result")
    private int result;

    public long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(long historyId) {
        this.historyId = historyId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getStringToShow(){
        String sr = "WON";
        if(result == 1) sr = "LOST";
        if(result == 2) sr = "DRAW";
        return historyId + ". "+ sr;
    }
}
