package com.atiyehandfahimeh.hw1.DataBase.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.atiyehandfahimeh.hw1.DataBase.Weathers;

import java.util.List;

@Dao
public interface WeatherDao {
    @Query("SELECT * FROM Weathers")
    List<Weathers> getAll();

    @Insert
    void insert(Weathers weatherEntity);

}
