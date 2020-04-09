package com.atiyehandfahimeh.hw1.DataBase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.atiyehandfahimeh.hw1.DataBase.Dao.WeatherDao;

@Database(entities = {Weathers.class} ,  version = 1)
public abstract class AppDataBase extends RoomDatabase {
    public abstract WeatherDao weatherDao();
}
