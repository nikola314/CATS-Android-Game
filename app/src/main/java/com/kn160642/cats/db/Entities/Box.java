package com.kn160642.cats.db.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "box")
public class Box {
    @PrimaryKey(autoGenerate = true)
    private long boxId;

    @ColumnInfo(name="timestamp")
    private long timestamp;

    @ColumnInfo(name="timeToOpen")
    private long timeToOpen;

    @ColumnInfo(name="userId")
    private long userId;

    @ColumnInfo(name="opened")
    private boolean opened;

    public long getBoxId() {
        return boxId;
    }

    public void setBoxId(long boxId) {
        this.boxId = boxId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimeToOpen() {
        return timeToOpen;
    }

    public void setTimeToOpen(long timeToOpen) {
        this.timeToOpen = timeToOpen;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }
}
