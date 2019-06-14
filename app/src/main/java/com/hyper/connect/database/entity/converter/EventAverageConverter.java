package com.hyper.connect.database.entity.converter;

import android.arch.persistence.room.TypeConverter;

import com.hyper.connect.database.entity.enums.EventAverage;

public class EventAverageConverter{
    @TypeConverter
    public static EventAverage toStatus(int average){
        if(average==EventAverage.REAL_TIME.getValue()){
            return EventAverage.REAL_TIME;
        }
        else if(average==EventAverage.ONE_MINUTE.getValue()){
            return EventAverage.ONE_MINUTE;
        }
        else if(average==EventAverage.FIVE_MINUTES.getValue()){
            return EventAverage.FIVE_MINUTES;
        }
        else if(average==EventAverage.FIFTEEN_MINUTES.getValue()){
            return EventAverage.FIFTEEN_MINUTES;
        }
        else if(average==EventAverage.ONE_HOUR.getValue()){
            return EventAverage.ONE_HOUR;
        }
        else if(average==EventAverage.THREE_HOURS.getValue()){
            return EventAverage.THREE_HOURS;
        }
        else if(average==EventAverage.SIX_HOURS.getValue()){
            return EventAverage.SIX_HOURS;
        }
        else if(average==EventAverage.ONE_DAY.getValue()){
            return EventAverage.ONE_DAY;
        }
        else{
            throw new IllegalArgumentException("Invalid Event Average");
        }
    }

    @TypeConverter
    public static int toInteger(EventAverage average){
        return average.getValue();
    }
}
