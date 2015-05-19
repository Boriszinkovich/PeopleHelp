package com.example.boris.androidproject.request;

/**
 * Created by Boris on 25.03.2015.
 * Інтерфейс заявки користувача
 */
public interface RequestInterface {
    public boolean hasDate();
    public String getUserId();
    public String getThem();
    public void setUserId(String id);
    public void setRequestType(String type);
    public String getRequestType();
    public void updateLocation(String longitude,String latitude);
    public String getLatitude();
    public String getLongitude();
}
