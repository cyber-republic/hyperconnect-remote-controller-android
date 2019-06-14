package com.hyper.connect.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.hyper.connect.database.entity.Attribute;
import com.hyper.connect.database.entity.FavoriteChart;

import java.util.List;


@Dao
public interface FavoriteChartDao{

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    void insert(FavoriteChart favoriteChart);

    @Query("SELECT * FROM favorite_chart_table")
    LiveData<List<FavoriteChart>> getLiveFavoriteChartList();

    @Query("DELETE FROM favorite_chart_table")
    void deleteFavoriteChart();
}
