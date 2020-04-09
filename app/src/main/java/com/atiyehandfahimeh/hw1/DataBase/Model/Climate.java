package com.atiyehandfahimeh.hw1.DataBase.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity
public class Climate {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "date")
    private String date;
    @ColumnInfo(name = "weather")
    private String weather;
    @ColumnInfo(name = "photo_code")
    private int photoCode;
    @ColumnInfo(name = "max_temp")
    private Double maxTemp;
    @ColumnInfo(name = "min_temp")
    private Double minTemp;
    @ColumnInfo(name = "avg_temp")
    private Double avgTemp;

    public Climate(String date, String weather, int photoCode, Double maxTemp, Double minTemp, Double avgTemp) {
        this.date = date;
        this.weather = weather;
        this.photoCode = photoCode;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.avgTemp = avgTemp;
//        this.cityId = cityId;
    }

//    public int getCityId() {
//        return cityId;
//    }
//
//    public void setCityId(int cityId) {
//        this.cityId = cityId;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public int getPhotoCode() {
        return photoCode;
    }

    public void setPhotoCode(int photoCode) {
        this.photoCode = photoCode;
    }

    public Double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(Double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public Double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(Double minTemp) {
        this.minTemp = minTemp;
    }

    public Double getAvgTemp() {
        return avgTemp;
    }

    public void setAvgTemp(Double avgTemp) {
        this.avgTemp = avgTemp;
    }
}
