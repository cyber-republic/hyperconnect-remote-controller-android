package com.hyper.connect.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import com.hyper.connect.database.entity.CategoryRecord;

import java.util.List;


@Dao
public interface CategoryRecordDao{

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    void insert(CategoryRecord categoryRecord);

    @Query("SELECT * FROM category_record_table")
    List<CategoryRecord> getCategoryRecordList();

    @Query("SELECT * FROM category_record_table")
    LiveData<List<CategoryRecord>> getLiveCategoryRecordList();

    @Query("SELECT * FROM category_record_table WHERE categoryId = :categoryId AND attributeId = :attributeId")
    CategoryRecord getCategoryRecordByCategoryIdAndAttributeId(int categoryId, int attributeId);

    @Query("SELECT * FROM category_record_table WHERE categoryId = :categoryId AND attributeId = :attributeId")
    LiveData<CategoryRecord> getLiveCategoryRecordByCategoryIdAndAttributeId(int categoryId, int attributeId);

    @Query("SELECT * FROM category_record_table WHERE attributeId = :attributeId")
    LiveData<List<CategoryRecord>> getLiveCategoryRecordListByAttributeId(int attributeId);

    @Query("SELECT * FROM category_record_table WHERE attributeId = :attributeId")
    List<CategoryRecord> getCategoryRecordListByAttributeId(int attributeId);

    @Query("DELETE FROM category_record_table WHERE categoryId = :categoryId AND attributeId = :attributeId")
    void deleteCategoryRecordByCategoryIdAndAttributeId(int categoryId, int attributeId);
}
