package com.atiyehandfahimeh.hw1.DataBase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.atiyehandfahimeh.hw1.DataBase.Dao.WeatherDao;
import com.atiyehandfahimeh.hw1.Models.DayWeather;

@Database(entities = {DayWeather.class} ,  version = 1)
public abstract class AppDataBase extends RoomDatabase {
    public abstract WeatherDao weatherDao();
}