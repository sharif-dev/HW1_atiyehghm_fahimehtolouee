package com.atiyehandfahimeh.hw1.Models;

import java.util.ArrayList;

public class CityInfo {
    private String name;
    private String country;
    private String lastUpdate;
    private ArrayList<DayClimate> weekClimateInfo;

    public CityInfo(String name, String country, String lastUpdate, ArrayList<DayClimate> weekClimateInfo) {
        this.name = name;
        this.country = country;
        this.lastUpdate = lastUpdate;
        this.weekClimateInfo = weekClimateInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public ArrayList<DayClimate> getWeekClimateInfo() {
        return weekClimateInfo;
    }

    public void setWeekClimateInfo(ArrayList<DayClimate> weekClimateInfo) {
        this.weekClimateInfo = weekClimateInfo;
    }
}
