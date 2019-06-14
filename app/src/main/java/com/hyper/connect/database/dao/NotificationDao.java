package com.hyper.connect.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import com.hyper.connect.database.entity.Notification;

import java.util.List;


@Dao
public interface NotificationDao{

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    void insert(Notification notification);

    @Query("SELECT * FROM notification_table")
    List<Notification> getNotificationList();

    @Query("SELECT * FROM notification_table")
    LiveData<List<Notification>> getLiveNotificationList();

    @Query("SELECT * FROM notification_table WHERE deviceUserId = :deviceUserId")
    LiveData<List<Notification>> getLiveNotificationListByDeviceUserId(String deviceUserId);
}
