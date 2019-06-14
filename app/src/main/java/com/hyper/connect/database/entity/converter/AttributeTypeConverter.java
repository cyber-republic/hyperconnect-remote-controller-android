package com.hyper.connect.database.entity.converter;

import android.arch.persistence.room.TypeConverter;

import com.hyper.connect.database.entity.enums.AttributeType;


public class AttributeTypeConverter{
    @TypeConverter
    public static AttributeType toStatus(int type){
        if(type==AttributeType.STRING.getValue()){
            return AttributeType.STRING;
        }
        else if(type==AttributeType.BOOLEAN.getValue()){
            return AttributeType.BOOLEAN;
        }
        else if(type==AttributeType.INTEGER.getValue()){
            return AttributeType.INTEGER;
        }
        else if(type==AttributeType.DOUBLE.getValue()){
            return AttributeType.DOUBLE;
        }
        else{
            throw new IllegalArgumentException("Invalid Attribute Type");
        }
    }

    @TypeConverter
    public static int toInteger(AttributeType type){
        return type.getValue();
    }
}
