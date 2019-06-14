package com.hyper.connect.database.entity.converter;

import android.arch.persistence.room.TypeConverter;

import com.hyper.connect.database.entity.enums.AttributeScriptState;


public class AttributeScriptStateConverter{
    @TypeConverter
    public static AttributeScriptState toStatus(int scriptState){
        if(scriptState==AttributeScriptState.VALID.getValue()){
            return AttributeScriptState.VALID;
        }
        else if(scriptState==AttributeScriptState.INVALID.getValue()){
            return AttributeScriptState.INVALID;
        }
        else{
            throw new IllegalArgumentException("Invalid Attribute Script State");
        }
    }

    @TypeConverter
    public static int toInteger(AttributeScriptState scriptState){
        return scriptState.getValue();
    }
}
