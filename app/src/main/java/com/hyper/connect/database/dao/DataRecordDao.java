package com.hyper.connect.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import com.hyper.connect.database.entity.DataRecord;
import com.hyper.connect.database.entity.Device;
import com.hyper.connect.database.entity.enums.DeviceConnectionState;
import com.hyper.connect.database.entity.converter.DeviceConnectionStateConverter;

import java.util.List;


@Dao
public interface DataRecordDao{

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    void insert(DataRecord dataRecord);

    @Update(onConflict=OnConflictStrategy.REPLACE)
    void update(DataRecord dataRecord);

    @Query("SELECT * FROM data_record_table")
    List<DataRecord> getDataRecordList();

    @Query("SELECT * FROM data_record_table")
    LiveData<List<DataRecord>> getLiveDataRecordList();

    @Query("SELECT * FROM data_record_table WHERE id = :id")
    DataRecord getDataRecordById(int id);

    @Query("SELECT * FROM data_record_table WHERE deviceUserId = :deviceUserId AND edgeAttributeId = :edgeAttributeId")
    DataRecord getDataRecordByDeviceUserIdAndEdgeAttributeId(String deviceUserId, int edgeAttributeId);

    @Query("SELECT * FROM data_record_table WHERE deviceUserId = :deviceUserId AND edgeAttributeId = :edgeAttributeId")
    LiveData<DataRecord> getLiveDataRecordByDeviceUserIdAndEdgeAttributeId(String deviceUserId, int edgeAttributeId);

    @Query("DELETE FROM data_record_table WHERE id = :id")
    void deleteDataRecordById(int id);
}
