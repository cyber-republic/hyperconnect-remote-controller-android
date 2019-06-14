package com.hyper.connect.database.entity.converter;

import android.arch.persistence.room.TypeConverter;

import com.hyper.connect.database.entity.enums.AttributeDirection;


public class AttributeDirectionConverter{
    @TypeConverter
    public static AttributeDirection toStatus(int direction){
        if(direction==AttributeDirection.INPUT.getValue()){
            return AttributeDirection.INPUT;
        }
        else if(direction==AttributeDirection.OUTPUT.getValue()){
            return AttributeDirection.OUTPUT;
        }
        else{
            throw new IllegalArgumentException("Invalid Attribute Direction");
        }
    }

    @TypeConverter
    public static int toInteger(AttributeDirection direction){
        return direction.getValue();
    }
}
