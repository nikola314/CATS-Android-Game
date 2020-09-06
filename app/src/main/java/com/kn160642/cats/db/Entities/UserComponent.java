package com.kn160642.cats.db.Entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "usercomponent")
public class UserComponent {

    @PrimaryKey(autoGenerate = true)
    private long userComponentId;

    @ColumnInfo(name="userId")
    private long userId;

    @ColumnInfo(name="componentId")
    private long componentId;

    public UserComponent(long userId, long componentId){
        this.componentId = componentId;
        this.userId = userId;
    }

    public long getUserComponentId() {
        return userComponentId;
    }

    public void setUserComponentId(long userComponentId) {
        this.userComponentId = userComponentId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getComponentId() {
        return componentId;
    }

    public void setComponentId(long componentId) {
        this.componentId = componentId;
    }
}
