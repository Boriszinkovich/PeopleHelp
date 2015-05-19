package com.example.boris.androidproject.some_user;

import android.graphics.Bitmap;

import com.example.boris.androidproject.request.RequestInterface;

/**
 * Created by Boris on 21.04.2015.
 * модель для представлення користувача на карті
 */
public class MapUser {
    private String name;
    private String lastName;
    private int id;
    private String photoUrl;
    private Bitmap smallBitmap;
    private Bitmap bigBitmap;
    private RequestInterface requestInterface;
    public MapUser() {
    }

    public MapUser(String name, String lastName, int id, String photoUrl) {
        this.name = name;
        this.lastName = lastName;
        this.id = id;
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Bitmap getSmallBitmap() {
        return smallBitmap;
    }

    public void setSmallBitmap(Bitmap smallBitmap) {
        this.smallBitmap = smallBitmap;
    }

    public Bitmap getBigBitmap() {
        return bigBitmap;
    }
    public void setBigBitmap(Bitmap bigBitmap) {
        this.bigBitmap = bigBitmap;
    }

    public RequestInterface getRequestInterface() {
        return requestInterface;
    }

    public void setRequestInterface(RequestInterface requestInterface) {
        this.requestInterface = requestInterface;
    }
}
