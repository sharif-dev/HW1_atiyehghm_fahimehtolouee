package com.atiyehandfahimeh.hw1.DataBase.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.atiyehandfahimeh.hw1.Models.DayWeather;

import java.util.List;

@Dao
public interface WeatherDao {
    @Query("SELECT * FROM dayweather")
    List<DayWeather> getAll();

    @Insert
    void insert(DayWeather dayWeather);

}
