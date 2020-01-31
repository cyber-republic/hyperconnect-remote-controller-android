package com.hyper.connect.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class CustomUtil{

    public static String getRandomGlobalEventId(){
        int length=12;
        boolean useLetters=true;
        boolean useNumbers=true;
        String globalEventId=RandomStringUtils.random(length, useLetters, useNumbers);
        return globalEventId;
    }

    public static void byte2File(byte[] data, String path){
        if(data.length<3 || path.equals("")) return;
        try{
            FileOutputStream output=new FileOutputStream(new File(path), true);
            output.write(data, 0, data.length);
            output.flush();
            output.close();
        }
        catch(Exception ex){}
    }

    public static String getDateTimeByPattern(String dateTime, String pattern){
        DateFormat dateFormat=new SimpleDateFormat(pattern);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        long utcLong=0;
        try{
            Date utcDate=dateFormat.parse(dateTime);
            utcLong=utcDate.getTime();
        }
        catch(ParseException pe){}
        dateFormat.setTimeZone(TimeZone.getDefault());
        String newDateTime=dateFormat.format(new Date(utcLong));
        return newDateTime;
    }
}
