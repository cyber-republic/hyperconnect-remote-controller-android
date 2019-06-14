package com.hyper.connect.database.entity.converter;

import android.arch.persistence.room.TypeConverter;

import com.hyper.connect.database.entity.enums.EventType;

public class EventTypeConverter{
    @TypeConverter
    public static EventType toStatus(int type){
        if(type==EventType.LOCAL.getValue()){
            return EventType.LOCAL;
        }
        else if(type==EventType.GLOBAL.getValue()){
            return EventType.GLOBAL;
        }
        else{
            throw new IllegalArgumentException("Invalid Event Type");
        }
    }

    @TypeConverter
    public static int toInteger(EventType type){
        return type.getValue();
    }
}
