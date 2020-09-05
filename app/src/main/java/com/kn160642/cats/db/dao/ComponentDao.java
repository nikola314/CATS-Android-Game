package com.kn160642.cats.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.kn160642.cats.db.Entities.Component;
import com.kn160642.cats.db.Entities.UserComponent;

import java.util.List;

@Dao
public abstract class ComponentDao {

    @Insert
    public abstract void insertComponent(Component u);

    @Query("SELECT * FROM component")
    public abstract LiveData<List<Component>> getAllComponents();

    @Query("SELECT componentId, name, type, power, health, energy FROM usercomponent uc JOIN component c ON uc.componentId=c.componentId JOIN user u ON u.userId=uc.userId WHERE username = :username")
    public abstract LiveData<List<UserComponent>> getAllComponentsOfUser(String username);

}
