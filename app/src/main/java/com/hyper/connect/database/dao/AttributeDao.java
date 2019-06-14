package com.hyper.connect.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.hyper.connect.database.entity.Attribute;

import java.util.List;


@Dao
public interface AttributeDao{

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    void insert(Attribute attribute);

    @Update(onConflict=OnConflictStrategy.REPLACE)
    void update(Attribute attribute);

    @Query("SELECT * FROM attribute_table")
    LiveData<List<Attribute>> getLiveAttributeList();

    @Query("SELECT * FROM attribute_table WHERE edgeSensorId = :edgeSensorId AND deviceId = :deviceId")
    LiveData<List<Attribute>> getLiveAttributeListByEdgeSensorIdAndDeviceId(int edgeSensorId, int deviceId);

    @Query("SELECT * FROM attribute_table WHERE edgeSensorId = :edgeSensorId AND deviceId = :deviceId AND direction = :direction")
    LiveData<List<Attribute>> getLiveAttributeListByEdgeSensorIdAndDeviceIdAndDirection(int edgeSensorId, int deviceId, int direction);

    @Query("SELECT * FROM attribute_table WHERE id = :id")
    Attribute getAttributeById(int id);

    @Query("SELECT * FROM attribute_table WHERE edgeAttributeId = :edgeAttributeId AND deviceId = :deviceId")
    Attribute getAttributeByEdgeAttributeIdAndDeviceId(int edgeAttributeId, int deviceId);

    @Query("SELECT * FROM attribute_table WHERE edgeAttributeId = :edgeAttributeId AND deviceId = :deviceId")
    LiveData<Attribute> getLiveAttributeByEdgeAttributeIdAndDeviceId(int edgeAttributeId, int deviceId);

    @Query("DELETE FROM attribute_table WHERE id = :id")
    void deleteAttributeById(int id);

    @Query("SELECT COUNT(id) FROM attribute_table")
    LiveData<Integer> getLiveAttributeCount();

    @Query("SELECT COUNT(id) FROM attribute_table WHERE state = 0")
    LiveData<Integer> getLiveActiveAttributeCount();
}
