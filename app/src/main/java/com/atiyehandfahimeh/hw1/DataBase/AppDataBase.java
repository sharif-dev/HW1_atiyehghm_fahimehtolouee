package com.atiyehandfahimeh.hw1.DataBase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.atiyehandfahimeh.hw1.DataBase.Dao.CityDao;
import com.atiyehandfahimeh.hw1.DataBase.Dao.ClimateDao;
import com.atiyehandfahimeh.hw1.DataBase.Model.City;
import com.atiyehandfahimeh.hw1.DataBase.Model.Climate;

@Database(entities = {City.class, Climate.class} ,  version = 1)
public abstract class AppDataBase extends RoomDatabase {
    public abstract ClimateDao climateDao();
    public abstract CityDao cityDao();
}
