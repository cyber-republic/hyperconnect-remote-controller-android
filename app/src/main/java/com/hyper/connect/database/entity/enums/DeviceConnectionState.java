package com.hyper.connect.database.entity.enums;

public enum DeviceConnectionState{
    ONLINE,
    OFFLINE;

    public int getValue(){
        switch(this){
            case ONLINE:
                return 0;
            case OFFLINE:
                return 1;
            default:
                throw new IllegalArgumentException("Invalid Device Connection State");
        }
    }

    @Override
    public String toString(){
        switch(this){
            case ONLINE:
                return "Online";
            case OFFLINE:
                return "Offline";
            default:
                throw new IllegalArgumentException("Invalid Device Connection State");
        }
    }
}