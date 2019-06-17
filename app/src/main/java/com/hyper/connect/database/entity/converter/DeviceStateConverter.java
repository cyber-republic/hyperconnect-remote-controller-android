package com.hyper.connect.database.entity.converter;

import android.arch.persistence.room.TypeConverter;

import com.hyper.connect.database.entity.enums.DeviceState;


public class DeviceStateConverter {
    @TypeConverter
    public static DeviceState toStatus(int state){
        if(state==DeviceState.ACTIVE.getValue()){
            return DeviceState.ACTIVE;
        }
        else if(state==DeviceState.PENDING.getValue()){
            return DeviceState.PENDING;
        }
        else if(state==DeviceState.DEACTIVATED.getValue()){
            return DeviceState.DEACTIVATED;
        }
        else{
            throw new IllegalArgumentException("Invalid Device State");
        }
    }

    @TypeConverter
    public static int toInteger(DeviceState state){
        return state.getValue();
    }
}