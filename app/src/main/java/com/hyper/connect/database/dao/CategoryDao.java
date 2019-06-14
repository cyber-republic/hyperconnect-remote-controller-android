package com.hyper.connect.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import com.hyper.connect.database.entity.Category;
import com.hyper.connect.database.entity.CategoryRecord;

import java.util.List;


@Dao
public interface CategoryDao{

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    void insert(Category category);

    @Query("SELECT * FROM category_table")
    List<Category> getCategoryList();

    @Query("SELECT * FROM category_table")
    LiveData<List<Category>> getLiveCategoryList();

    @Query("SELECT * FROM category_table WHERE id = :id")
    LiveData<Category> getLiveCategoryById(int id);

    @Query("SELECT * from category_table c WHERE EXISTS (SELECT 1 FROM category_record_table cr WHERE cr.categoryId=c.id AND cr.deviceId = :deviceId)")
    LiveData<List<Category>> getLiveCategoryListByDeviceId(int deviceId);

    @Query("SELECT * FROM category_table WHERE id = :id")
    Category getCategoryById(int id);
}
