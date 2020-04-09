package com.atiyehandfahimeh.hw1.DataBase.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import com.atiyehandfahimeh.hw1.Models.DayClimate;

import java.util.ArrayList;

@Entity
public class City {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "place_name")
    private String city;

    @ColumnInfo(name = "county")
    private String country;

    @ColumnInfo(name = "last_update")
    private String lastUpDate;


    public City(String city, String country, String lastUpDate) {
        this.city = city;
        this.country = country;
        this.lastUpDate = lastUpDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLastUpDate() {
        return lastUpDate;
    }

    public void setLastUpDate(String lastUpDate) {
        this.lastUpDate = lastUpDate;
    }

}
