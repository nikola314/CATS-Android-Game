package com.kn160642.cats.db.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "component")
public class Component {
    @PrimaryKey(autoGenerate = true)
    private long componentId;

    @ColumnInfo(name="name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ColumnInfo(name="power")
    private int power;

    @ColumnInfo(name="health")
    private int health;

    @ColumnInfo(name="energy")
    private int energy;

    @ColumnInfo(name="type")
    private int type;

    public Component(int power, int health, int energy, int type) {
        this.power = power;
        this.health = health;
        this.energy = energy;
        this.type = type;
    }

    public long getComponentId() {
        return componentId;
    }

    public void setComponentId(long componentId) {
        this.componentId = componentId;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

