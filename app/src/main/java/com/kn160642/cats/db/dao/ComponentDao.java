package com.kn160642.cats.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.kn160642.cats.db.Entities.Component;
import com.kn160642.cats.db.Entities.User;
import com.kn160642.cats.db.Entities.UserComponent;

import java.util.List;

@Dao
public abstract class ComponentDao {

    @Insert
    public abstract long insertComponent(Component u);

    @Insert
    public abstract long insertUserComponent(UserComponent u);

    @Query("SELECT * FROM component")
    public abstract LiveData<List<Component>> getAllComponents();

    @Query("SELECT * FROM component WHERE componentId IN (SELECT componentId FROM usercomponent WHERE userId = :userId)")
    public abstract LiveData<List<Component>> getUserComponents(long userId);

    @Query("SELECT * FROM component")
    public abstract List<Component> getAllComponentsDead();

    @Query("SELECT * FROM component WHERE componentId IN (SELECT componentId FROM usercomponent WHERE userId = :userId)")
    public abstract List<Component> getUserComponentsDead(long userId);

    @Query("SELECT * FROM component WHERE componentId IN (:id1,:id2,:id3) ORDER BY componentId")
    public abstract List<Component> getThreeComponentsDead(long id1, long id2, long id3);

    @Query("SELECT * FROM component WHERE componentId = :id")
    public abstract Component getComponentById(long id);


//    @Query("SELECT componentId, name, type, power, health, energy FROM usercomponent uc JOIN component c ON uc.componentId=c.componentId JOIN user u ON u.userId=uc.userId WHERE u.username = :username")
//    public abstract LiveData<List<UserComponent>> getAllComponentsOfUser(String username);

}
