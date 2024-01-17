package com.example.myapplication;

public class LocationMessage {
    private String name;
    private double latitude;
    private double longitude;
    private String memo;
    public LocationMessage() {
        this.name = "";
        this.latitude = 0;
        this.longitude = 0;
        this.memo = "";
    }
    public LocationMessage(String name, double latitude, double longitude, String memo) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.memo = memo;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }
    public String getMemo() {
        return memo;
    }
}