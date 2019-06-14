package com.hyper.connect.elastos;

import com.google.gson.annotations.SerializedName;

public enum ElastosCommand{
    @SerializedName("0")
    GET_DATA,

    @SerializedName("1")
    ADD_DEVICE,

    @SerializedName("2")
    ADD_SENSOR,

    @SerializedName("3")
    ADD_ATTRIBUTE,

    @SerializedName("4")
    ADD_EVENT,

    @SerializedName("5")
    CHANGE_ATTRIBUTE_STATE,

    @SerializedName("6")
    EXECEUTE_ATTRIBUTE_ACTION,

    @SerializedName("7")
    CHANGE_EVENT_STATE;

    public int getValue(){
        switch(this){
            case GET_DATA:
                return 0;
            case ADD_DEVICE:
                return 1;
            case ADD_SENSOR:
                return 2;
            case ADD_ATTRIBUTE:
                return 3;
            case ADD_EVENT:
                return 4;
            case CHANGE_ATTRIBUTE_STATE:
                return 5;
            case EXECEUTE_ATTRIBUTE_ACTION:
                return 6;
            case CHANGE_EVENT_STATE:
                return 7;
            default:
                throw new IllegalArgumentException("Invalid Elastos Command");
        }
    }

    public static ElastosCommand valueOf(int command){
        switch(command){
            case 0:
                return GET_DATA;
            case 1:
                return ADD_DEVICE;
            case 2:
                return ADD_SENSOR;
            case 3:
                return ADD_ATTRIBUTE;
            case 4:
                return ADD_EVENT;
            case 5:
                return CHANGE_ATTRIBUTE_STATE;
            case 6:
                return EXECEUTE_ATTRIBUTE_ACTION;
            case 7:
                return CHANGE_EVENT_STATE;
            default:
                throw new IllegalArgumentException("Invalid Elastos Command");
        }
    }
}