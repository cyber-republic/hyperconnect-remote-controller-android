package com.hyper.connect.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

@Entity(tableName="data_record_table")
public class DataRecord{

    @PrimaryKey(autoGenerate=true)
    @ColumnInfo(name="id")
    private int id;

    @NonNull
    @ColumnInfo(name="deviceUserId")
    private String deviceUserId;

    @ColumnInfo(name="edgeAttributeId")
    private int edgeAttributeId;

    @NonNull
    @ColumnInfo(name="dateTime")
    private String dateTime;

    @NonNull
    @ColumnInfo(name="value")
    private String value;

    @Ignore
    public DataRecord(@NonNull String dateTime, @NonNull String value){
        this.dateTime=dateTime;
        this.value=value;
    }

    public DataRecord(int id, @NonNull String deviceUserId, int edgeAttributeId, @NonNull String dateTime, @NonNull String value){
        this.id=id;
        this.deviceUserId=deviceUserId;
        this.edgeAttributeId=edgeAttributeId;
        this.dateTime=dateTime;
        this.value=value;
    }

    public int getId(){
        return id;
    }

    @NonNull
    public String getDeviceUserId(){
        return deviceUserId;
    }

    public int getEdgeAttributeId(){
        return edgeAttributeId;
    }

    @NonNull
    public String getDateTime(){
        return dateTime;
    }

    @NonNull
    public String getValue(){
        return value;
    }

    public void setId(int id){
        this.id=id;
    }

    public void setDeviceUserId(@NonNull String deviceUserId){
        this.deviceUserId=deviceUserId;
    }

    public void setEdgeAttributeId(int edgeAttributeId){
        this.edgeAttributeId=edgeAttributeId;
    }

    public void setDateTime(@NonNull String dateTime){
        this.dateTime=dateTime;
    }

    public void setValue(@NonNull String value){
        this.value=value;
    }

    @Override
    public String toString(){
        return "DataRecord{"+
                "id="+id+
                ", deviceUserId='"+deviceUserId+'\''+
                ", edgeAttributeId="+edgeAttributeId+
                ", dateTime='"+dateTime+'\''+
                ", value='"+value+'\''+
                '}';
    }
}
