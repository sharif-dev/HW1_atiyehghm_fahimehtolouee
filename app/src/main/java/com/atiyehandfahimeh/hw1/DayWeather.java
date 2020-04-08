package com.atiyehandfahimeh.hw1;

public class DayWeather {
    private String Date;
    private String weathertext;
    private int photocode;
    private Double maxtempc;
    private Double mintempc;
    private Double avgtempc;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getWeathertext() {
        return weathertext;
    }

    public void setWeathertext(String weathertext) {
        this.weathertext = weathertext;
    }

    public int getPhotocode() {
        return photocode;
    }

    public void setPhotocode(int photocode) {
        this.photocode = photocode;
    }

    public Double getMaxtempc() {
        return maxtempc;
    }

    public void setMaxtempc(Double maxtempc) {
        this.maxtempc = maxtempc;
    }

    public Double getMintempc() {
        return mintempc;
    }

    public void setMintempc(Double mintempc) {
        this.mintempc = mintempc;
    }

    public Double getAvgtempc() {
        return avgtempc;
    }

    public void setAvgtempc(Double avgtempc) {
        this.avgtempc = avgtempc;
    }
}
