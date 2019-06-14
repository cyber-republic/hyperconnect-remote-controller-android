package com.hyper.connect.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

@Entity(tableName="category_record_table")
public class CategoryRecord{
    @PrimaryKey(autoGenerate=true)
    @ColumnInfo(name="id")
    private int id;

    @ColumnInfo(name="categoryId")
    private int categoryId;

    @ColumnInfo(name="attributeId")
    private int attributeId;

    @ColumnInfo(name="deviceId")
    private int deviceId;

    public CategoryRecord(int id, int categoryId, int attributeId, int deviceId){
        this.id=id;
        this.categoryId=categoryId;
        this.attributeId=attributeId;
        this.deviceId=deviceId;
    }

    public int getId(){
        return id;
    }

    public int getCategoryId(){
        return categoryId;
    }

    public int getAttributeId(){
        return attributeId;
    }

    public int getDeviceId(){
        return deviceId;
    }

    public void setId(int id){
        this.id=id;
    }

    public void setCategoryId(int categoryId){
        this.categoryId=categoryId;
    }

    public void setAttributeId(int attributeId){
        this.attributeId=attributeId;
    }

    public void setDeviceId(int deviceId){
        this.deviceId=deviceId;
    }
}
