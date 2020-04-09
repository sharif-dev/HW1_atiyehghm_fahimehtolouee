package com.atiyehandfahimeh.hw1.DataBase.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.atiyehandfahimeh.hw1.DataBase.Model.City;

import java.util.List;

@Dao
public interface CityDao {
    @Query("SELECT * FROM city")
    List<City> getAll();

    @Query("DELETE FROM city")
    void deleteAll();

    @Insert
    void insert(City city);

}
