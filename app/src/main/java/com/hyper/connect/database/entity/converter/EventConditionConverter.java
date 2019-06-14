package com.hyper.connect.database.entity.converter;

import android.arch.persistence.room.TypeConverter;

import com.hyper.connect.database.entity.enums.EventCondition;

public class EventConditionConverter{
    @TypeConverter
    public static EventCondition toStatus(int condition){
        if(condition==EventCondition.EQUAL_TO.getValue()){
            return EventCondition.EQUAL_TO;
        }
        else if(condition==EventCondition.NOT_EQUAL_TO.getValue()){
            return EventCondition.NOT_EQUAL_TO;
        }
        else if(condition==EventCondition.GREATER_THAN.getValue()){
            return EventCondition.GREATER_THAN;
        }
        else if(condition==EventCondition.LESS_THAN.getValue()){
            return EventCondition.LESS_THAN;
        }
        else{
            throw new IllegalArgumentException("Invalid Event Condition");
        }
    }

    @TypeConverter
    public static int toInteger(EventCondition condition){
        return condition.getValue();
    }
}
