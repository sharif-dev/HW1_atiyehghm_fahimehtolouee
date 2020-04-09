package com.atiyehandfahimeh.hw1.Models;

public class DayClimate {
    private String date;
    private String weather;
    private int photoCode;
    private Double maxTemp;
    private Double minTemp;
    private Double avgTemp;

    public DayClimate(String date, String weather, int photoCode, Double maxTemp, Double minTemp, Double avgTemp) {
        this.date = date;
        this.weather = weather;
        this.photoCode = photoCode;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.avgTemp = avgTemp;
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
