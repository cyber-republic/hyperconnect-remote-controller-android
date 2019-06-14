package com.hyper.connect.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;


@Entity(tableName="category_table")
public class Category{
    @PrimaryKey(autoGenerate=true)
    @ColumnInfo(name="id")
    private int id;

    @NonNull
    @ColumnInfo(name="name")
    private String name;

    public Category(int id, @NonNull String name){
        this.id=id;
        this.name=name;
    }

    public int getId(){
        return id;
    }

    @NonNull
    public String getName(){
        return name;
    }

    public void setId(int id){
        this.id=id;
    }

    public void setName(@NonNull String name){
        this.name=name;
    }

    @Override
    public String toString(){
        return "Category{"+
                "id="+id+
                ", name='"+name+'\''+
                '}';
    }
}
