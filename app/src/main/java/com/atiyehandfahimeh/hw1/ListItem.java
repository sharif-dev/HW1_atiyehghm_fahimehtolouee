package com.atiyehandfahimeh.hw1;

public class ListItem {
    private String id ;
    private  String name ;
    private double centerX;
    private double centerY;

    public ListItem(String id, String name, double centerX, double centerY) {
        this.id = id;
        this.name = name;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public String getId() {
        return id;
    }
    public double getCenterX() {
        return centerX;
    }
    public double getCenterY() {
        return centerY;
    }
    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setCenterX(double centerX) { this.centerX = centerX; }
    public void setCenterY(double centerY) {
        this.centerY = centerY;
    }
    public void setName(String name) {
        this.name = name;
    }
}
