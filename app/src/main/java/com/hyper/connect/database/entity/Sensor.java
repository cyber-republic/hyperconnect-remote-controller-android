package com.hyper.connect.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.ArrayList;

@Entity(tableName="sensor_table")
public class Sensor{

    @PrimaryKey(autoGenerate=true)
    @ColumnInfo(name="id")
    private int id;

    @ColumnInfo(name="edgeSensorId")
    private int edgeSensorId;

    @NonNull
    @ColumnInfo(name="name")
    private String name;

    @NonNull
    @ColumnInfo(name="type")
    private String type;

    @ColumnInfo(name="deviceId")
    private int deviceId;

    @Ignore
    public Sensor(@NonNull String name){
        this.name=name;
    }

    public Sensor(int id, int edgeSensorId, @NonNull String name, @NonNull String type, int deviceId){
        this.id=id;
        this.edgeSensorId=edgeSensorId;
        this.name=name;
        this.type=type;
        this.deviceId=deviceId;
    }

    public int getId(){
        return id;
    }

    public int getEdgeSensorId(){
        return edgeSensorId;
    }

    @NonNull
    public String getName(){
        return name;
    }

    @NonNull
    public String getType(){
        return type;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setId(int id){
        this.id=id;
    }

    public void setEdgeSensorId(int edgeSensorId){
        this.edgeSensorId=edgeSensorId;
    }

    public void setName(@NonNull String name){
        this.name=name;
    }

    public void setType(@NonNull String type){
        this.type=type;
    }

    public void setDeviceId(int deviceId){
        this.deviceId=deviceId;
    }

    @Override
    public String toString(){
        return "Sensor{"+
                "id="+id+
                ", edgeSensorId="+edgeSensorId+
                ", name='"+name+'\''+
                ", type='"+type+'\''+
                ", deviceId="+deviceId+
                '}';
    }
}
