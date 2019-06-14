package com.hyper.connect.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

@Entity(tableName="favorite_chart_table")
public class FavoriteChart{
    @PrimaryKey(autoGenerate=true)
    @ColumnInfo(name="id")
    private int id;

    @ColumnInfo(name="attributeId")
    private int attributeId;

    public FavoriteChart(int id, int attributeId){
        this.id=id;
        this.attributeId=attributeId;
    }

    public int getId(){
        return id;
    }

    public int getAttributeId(){
        return attributeId;
    }

    public void setId(int id){
        this.id=id;
    }

    public void setAttributeId(int attributeId){
        this.attributeId=attributeId;
    }
}
