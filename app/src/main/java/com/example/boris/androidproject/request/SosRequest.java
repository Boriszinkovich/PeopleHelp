package com.example.boris.androidproject.request;

import com.example.boris.androidproject.database.RequestType;

/**
 * Created by Boris on 25.03.2015.
 * клас, що представляє собою модель заявки надзвичайної ситуації користувача
 */
public class SosRequest implements RequestInterface {
    private String userId;
    private String requestThem;
    private String longitude;
    private String latitude;
    public SosRequest() {
        requestThem = "";
    }



    public SosRequest(String requestComment) {
        this.requestThem = requestComment;
    }


    public boolean hasDate() {
        return false;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public String getThem() {
        return requestThem;
    }

    @Override
    public void setUserId(String id) {
        userId = id;
    }

    @Override
    public void setRequestType(String type) {

    }

    @Override
    public String getRequestType() {
        return RequestType.SosRequest;
    }

    @Override
    public void updateLocation(String longitude, String latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public String getLatitude() {
        return latitude;
    }

    @Override
    public String getLongitude() {
        return longitude;
    }

    public void setRequestThem(String requestThem) {
        this.requestThem = requestThem;
    }
}
