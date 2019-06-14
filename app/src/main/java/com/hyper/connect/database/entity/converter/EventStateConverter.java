package com.hyper.connect.database.entity.converter;

import android.arch.persistence.room.TypeConverter;

import com.hyper.connect.database.entity.enums.EventState;

public class EventStateConverter{
    @TypeConverter
    public static EventState toStatus(int state){
        if(state==EventState.ACTIVE.getValue()){
            return EventState.ACTIVE;
        }
        else if(state==EventState.DEACTIVATED.getValue()){
            return EventState.DEACTIVATED;
        }
        else{
            throw new IllegalArgumentException("Invalid Event State");
        }
    }

    @TypeConverter
    public static int toInteger(EventState state){
        return state.getValue();
    }
}
