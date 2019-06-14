package com.hyper.connect.database.entity.converter;

import android.arch.persistence.room.TypeConverter;

import com.hyper.connect.database.entity.enums.NotificationType;

public class NotificationTypeConverter{
    @TypeConverter
    public static NotificationType toStatus(int type){
        if(type==NotificationType.SUCCESS.getValue()){
            return NotificationType.SUCCESS;
        }
        else if(type==NotificationType.WARNING.getValue()){
            return NotificationType.WARNING;
        }
        else if(type==NotificationType.ERROR.getValue()){
            return NotificationType.ERROR;
        }
        else{
            throw new IllegalArgumentException("Invalid Notification Type");
        }
    }

    @TypeConverter
    public static int toInteger(NotificationType type){
        return type.getValue();
    }
}