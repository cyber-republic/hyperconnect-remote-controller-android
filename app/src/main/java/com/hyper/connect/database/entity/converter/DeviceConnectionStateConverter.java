package com.hyper.connect.database.entity.converter;

import android.arch.persistence.room.TypeConverter;

import com.hyper.connect.database.entity.enums.DeviceConnectionState;


public class DeviceConnectionStateConverter{
    @TypeConverter
    public static DeviceConnectionState toStatus(int connectionState){
        if(connectionState==DeviceConnectionState.ONLINE.getValue()){
            return DeviceConnectionState.ONLINE;
        }
        else if(connectionState==DeviceConnectionState.OFFLINE.getValue()){
            return DeviceConnectionState.OFFLINE;
        }
        else{
            throw new IllegalArgumentException("Invalid Device Connection State");
        }
    }

    @TypeConverter
    public static int toInteger(DeviceConnectionState connectionState){
        return connectionState.getValue();
    }
}