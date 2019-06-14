package com.hyper.connect.database.entity.converter;

import android.arch.persistence.room.TypeConverter;

import com.hyper.connect.database.entity.enums.NotificationCategory;

public class NotificationCategoryConverter{
    @TypeConverter
    public static NotificationCategory toStatus(int category){
        if(category==NotificationCategory.DEVICE.getValue()){
            return NotificationCategory.DEVICE;
        }
        else if(category==NotificationCategory.SENSOR.getValue()){
            return NotificationCategory.SENSOR;
        }
        else if(category==NotificationCategory.ATTRIBUTE.getValue()){
            return NotificationCategory.ATTRIBUTE;
        }
        else if(category==NotificationCategory.EVENT.getValue()){
            return NotificationCategory.EVENT;
        }
        else if(category==NotificationCategory.SYSTEM.getValue()){
            return NotificationCategory.SYSTEM;
        }
        else{
            throw new IllegalArgumentException("Invalid Notification Category");
        }
    }

    @TypeConverter
    public static int toInteger(NotificationCategory category){
        return category.getValue();
    }
}
