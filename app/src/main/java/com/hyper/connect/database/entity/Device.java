package com.hyper.connect.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.hyper.connect.database.entity.converter.DeviceConnectionStateConverter;
import com.hyper.connect.database.entity.converter.DeviceStateConverter;
import com.hyper.connect.database.entity.enums.DeviceConnectionState;
import com.hyper.connect.database.entity.enums.DeviceState;


@Entity(tableName="device_table")
public class Device{
    @PrimaryKey(autoGenerate=true)
    @ColumnInfo(name="id")
    private int id;

    @NonNull
    @ColumnInfo(name="userId")
    private String userId;

    @NonNull
    @ColumnInfo(name="address")
    private String address;

    @NonNull
    @ColumnInfo(name="name")
    private String name;

    @NonNull
    @TypeConverters(DeviceStateConverter.class)
    @ColumnInfo(name="state")
    private DeviceState state; /*** active, pending, deactivated ***/

    @NonNull
    @TypeConverters(DeviceConnectionStateConverter.class)
    @ColumnInfo(name="connectionState")
    private DeviceConnectionState connectionState; /*** online, offline ***/

    @Ignore
    public Device(@NonNull String name){
        this.name=name;
    }

    public Device(int id, @NonNull String userId, @NonNull String address, @NonNull String name, @NonNull DeviceState state, @NonNull DeviceConnectionState connectionState){
        this.id=id;
        this.userId=userId;
        this.address=address;
        this.name=name;
        this.state=state;
        this.connectionState=connectionState;
    }

    public int getId(){
        return id;
    }

    @NonNull
    public String getUserId(){
        return userId;
    }

    @NonNull
    public String getAddress(){
        return address;
    }

    @NonNull
    public String getName(){
        return name;
    }

    @NonNull
    public DeviceState getState(){
        return state;
    }

    @NonNull
    public DeviceConnectionState getConnectionState(){
        return connectionState;
    }

    public void setId(int id){
        this.id=id;
    }

    public void setUserId(@NonNull String userId){
        this.userId=userId;
    }

    public void setAddress(@NonNull String address){
        this.address=address;
    }

    public void setName(@NonNull String name){
        this.name=name;
    }

    public void setState(@NonNull DeviceState state){
        this.state=state;
    }

    public void setConnectionState(@NonNull DeviceConnectionState connectionState){
        this.connectionState=connectionState;
    }
}
