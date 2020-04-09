package com.atiyehandfahimeh.hw1.DataBase.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.atiyehandfahimeh.hw1.DataBase.Model.Climate;

import java.util.List;

@Dao
public interface ClimateDao {
    @Query("SELECT * FROM climate")
    List<Climate> getAll();

    @Query("DELETE FROM climate")
    void deleteAll();

    @Insert
    void insert(Climate climate);

}
