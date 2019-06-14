package com.hyper.connect.database.entity.converter;

import android.arch.persistence.room.TypeConverter;

import com.hyper.connect.database.entity.enums.AttributeState;


public class AttributeStateConverter{
    @TypeConverter
    public static AttributeState toStatus(int state){
        if(state==AttributeState.ACTIVE.getValue()){
            return AttributeState.ACTIVE;
        }
        else if(state==AttributeState.DEACTIVATED.getValue()){
            return AttributeState.DEACTIVATED;
        }
        else{
            throw new IllegalArgumentException("Invalid Attribute State");
        }
    }

    @TypeConverter
    public static int toInteger(AttributeState state){
        return state.getValue();
    }
}
