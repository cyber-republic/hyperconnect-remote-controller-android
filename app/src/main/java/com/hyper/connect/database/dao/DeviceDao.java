package com.hyper.connect.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import com.hyper.connect.database.entity.Device;
import com.hyper.connect.database.entity.enums.DeviceConnectionState;
import com.hyper.connect.database.entity.converter.DeviceConnectionStateConverter;

import java.util.List;


@Dao
public interface DeviceDao{

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    void insert(Device device);

    @Update(onConflict=OnConflictStrategy.REPLACE)
    void update(Device device);

    @TypeConverters(DeviceConnectionStateConverter.class)
    @Query("UPDATE device_table SET connectionState = :connectionState;")
    void setDeviceListConnectionState(DeviceConnectionState connectionState);

    @Query("SELECT * FROM device_table")
    List<Device> getDeviceList();

    @Query("SELECT * FROM device_table WHERE deletedState = 0")
    LiveData<List<Device>> getLiveDeviceList();

    @Query("SELECT * FROM device_table WHERE id = :id")
    Device getDeviceById(int id);

    @Query("SELECT * FROM device_table WHERE userId = :userId")
    Device getDeviceByUserId(String userId);

    @Query("SELECT * FROM device_table WHERE address = :address")
    Device getDeviceByAddress(String address);

    @Query("SELECT * FROM device_table WHERE id = :id")
    LiveData<Device> getLiveDeviceById(int id);

    @Query("SELECT * FROM device_table WHERE userId = :userId")
    LiveData<Device> getLiveDeviceByUserId(String userId);

    @Query("DELETE FROM device_table WHERE id = :id")
    void deleteDeviceById(int id);

    @Query("SELECT COUNT(id) FROM device_table")
    LiveData<Integer> getLiveDeviceCount();

    @Query("SELECT COUNT(id) FROM device_table WHERE connectionState = 0")
    LiveData<Integer> getLiveOnlineDeviceCount();
}
