package com.hyper.connect.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.hyper.connect.database.entity.Sensor;

import java.util.List;


@Dao
public interface SensorDao{

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    void insert(Sensor sensor);

    @Update(onConflict=OnConflictStrategy.REPLACE)
    void update(Sensor sensor);

    @Query("SELECT * FROM sensor_table")
    LiveData<List<Sensor>> getLiveSensorList();

    @Query("SELECT * FROM sensor_table WHERE deviceId = :deviceId")
    LiveData<List<Sensor>> getLiveSensorListByDeviceId(int deviceId);

    @Query("SELECT * FROM sensor_table WHERE id = :id")
    Sensor getSensorById(int id);

    @Query("SELECT * FROM sensor_table WHERE edgeSensorId = :edgeSensorId AND deviceId = :deviceId")
    Sensor getSensorByEdgeSensorIdAndDeviceId(int edgeSensorId, int deviceId);

    @Query("DELETE FROM sensor_table WHERE id = :id")
    void deleteSensorById(int id);
}
