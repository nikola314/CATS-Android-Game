package com.kn160642.cats.db.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {

    @PrimaryKey(autoGenerate = true)
    private long userId;

    @ColumnInfo(name="username")
    private String username;

    @ColumnInfo(name = "chassisId")
    private long chassisId;

    @ColumnInfo(name = "wheelsId")
    private long wheelsId;

    @ColumnInfo(name = "weaponId")
    private long weaponId;

    public User(String username) {
        this.username = username;
    }

    public long getChassisId() {
        return chassisId;
    }

    public void setChassisId(long chassisId) {
        this.chassisId = chassisId;
    }

    public long getWheelsId() {
        return wheelsId;
    }

    public void setWheelsId(long wheelsId) {
        this.wheelsId = wheelsId;
    }

    public long getWeaponId() {
        return weaponId;
    }

    public void setWeaponId(long weaponId) {
        this.weaponId = weaponId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
