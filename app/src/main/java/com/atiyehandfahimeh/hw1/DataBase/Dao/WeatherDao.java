package com.atiyehandfahimeh.hw1.DataBase.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.atiyehandfahimeh.hw1.DataBase.Model.Weather;

import java.util.List;

@Dao
public interface WeatherDao {
    @Query("SELECT * FROM task")
    List<Weather> getAll();

    @Insert
    void insert(Weather weather);
}
