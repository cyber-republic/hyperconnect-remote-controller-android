package com.hyper.connect.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.hyper.connect.database.entity.Event;

import java.util.List;


@Dao
public interface EventDao{

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    void insert(Event event);

    @Update(onConflict=OnConflictStrategy.REPLACE)
    void update(Event event);

    @Query("SELECT * FROM event_table")
    LiveData<List<Event>> getLiveEventList();

    @Query("SELECT * FROM event_table WHERE id = :id")
    Event getEventById(int id);

    @Query("SELECT * FROM event_table WHERE globalEventId = :globalEventId")
    Event getEventByGlobalEventId(String globalEventId);

    @Query("SELECT * FROM event_table WHERE globalEventId = :globalEventId AND sourceDeviceId = :sourceDeviceId")
    Event getEventByGlobalEventIdAndSourceDeviceId(String globalEventId, int sourceDeviceId);

    @Query("DELETE FROM event_table WHERE id = :id")
    void deleteEventById(int id);

    @Query("SELECT COUNT(id) FROM event_table")
    LiveData<Integer> getLiveEventCount();

    @Query("SELECT COUNT(id) FROM event_table WHERE state = 0")
    LiveData<Integer> getLiveActiveEventCount();
}
