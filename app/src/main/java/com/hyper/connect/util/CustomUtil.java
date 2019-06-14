package com.hyper.connect.util;

import org.apache.commons.lang3.RandomStringUtils;


public class CustomUtil{

    public static String getRandomGlobalEventId(){
        int length=12;
        boolean useLetters=true;
        boolean useNumbers=true;
        String globalEventId=RandomStringUtils.random(length, useLetters, useNumbers);
        return globalEventId;
    }
}
