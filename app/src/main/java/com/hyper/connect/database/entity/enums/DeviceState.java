package com.hyper.connect.database.entity.enums;

public enum DeviceState{
    ACTIVE,
    PENDING,
    DEACTIVATED;

    public int getValue(){
        switch(this){
            case ACTIVE:
                return 0;
            case PENDING:
                return 1;
            case DEACTIVATED:
                return 2;
            default:
                throw new IllegalArgumentException("Invalid Device State");
        }
    }

    @Override
    public String toString(){
        switch(this){
            case ACTIVE:
                return "Active";
            case PENDING:
                return "Pending";
            case DEACTIVATED:
                return "Deactivated";
            default:
                throw new IllegalArgumentException("Invalid Device State");
        }
    }
}