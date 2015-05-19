package com.example.boris.androidproject.request;

import com.example.boris.androidproject.database.RequestDao;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by Boris on 25.03.2015.
 * клас, що представляє собою модель заявки пошуку користувача
 */
public class FindRequest implements RequestInterface {
    private String requestType;
    private String userId;
    private boolean hasDate = false;
    private boolean isOffLineWatched = false;
    private String requestName;
    private String requestComment;
    private String findType;
    private Date lastData;
    private boolean hasConcretePlace;
    private String longitude;
    private String latitude;
    private boolean isOnline = true;

    public FindRequest(boolean hasData, boolean isOffLineWatched, String requestName, String requestComment, String requestPayment, Date lastData) {
        this.hasDate = hasData;
        this.isOffLineWatched = isOffLineWatched;
        this.requestName = requestName;
        this.requestComment = requestComment;
        this.findType = requestPayment;
        this.lastData = lastData;
        hasConcretePlace = false;
    }
    public FindRequest(ParseObject object)
    {
        setUserId(""+object.getInt(RequestDao.UserId));
        setRequestType(object.getString(RequestDao.Type));
        setRequestName(object.getString(RequestDao.RequestThem));
        setRequestComment(object.getString(RequestDao.RequestComment));
        setFindType(object.getString(RequestDao.FindType));
        setLastData(object.getDate(RequestDao.Data));
        setHasDate(object.getBoolean(RequestDao.HasData));
        setOffLineWatched(object.getBoolean(RequestDao.OfflineVisibility));
        setHasConcretePlace(object.getBoolean(RequestDao.HasConcretePlace));
        setOnline(object.getBoolean(RequestDao.IsOnlineNow));
        updateLocation(object.getString(RequestDao.Longitude), object.getString(RequestDao.Latitude));
    }






    public void setHasDate(boolean hasDate) {
        this.hasDate = hasDate;
    }

    public void setOffLineWatched(boolean isOffLineWatched) {
        this.isOffLineWatched = isOffLineWatched;
    }

    public void setHasConcretePlace(boolean hasConcretePlace) {
        this.hasConcretePlace = hasConcretePlace;
    }

    public boolean isHasConcretePlace() {
        return hasConcretePlace;
    }

    public FindRequest() {
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public void setRequestComment(String requestComment) {
        this.requestComment = requestComment;
    }

    public void setFindType(String requestPayment) {
        this.findType = requestPayment;
    }

    public void setLastData(Date lastData) {
        this.lastData = lastData;
    }

    public FindRequest(boolean hasData, boolean isOffLineWatched, String requestName, String requestComment, String requestPayment) {
        this.hasDate = hasData;
        this.isOffLineWatched = isOffLineWatched;
        this.requestName = requestName;
        this.requestComment = requestComment;
        this.findType = requestPayment;
        lastData = new Date();
    }


    public boolean hasTimeGone() {
        if ((new Date()).getTime() - lastData.getTime() < 0) return false;
        return true;
    }



    public boolean isOffLineWatched() {
        return isOffLineWatched;
    }

    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public String getRequestComment() {
        return requestComment;
    }

    public String getFindType() {
        return findType;
    }

    public Date getLastData() {
        return lastData;
    }

    @Override
    public boolean hasDate() {
        return hasDate;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public String getThem() {
        return requestName;
    }

    @Override
    public void setUserId(String id) {
        this.userId = id;
    }

    @Override
    public void setRequestType(String type) {
        this.requestType = type;
    }

    @Override
    public String getRequestType() {
        return requestType;
    }

    @Override
    public void updateLocation(String longitude, String latitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String getLatitude() {
        return latitude;
    }

    @Override
    public String getLongitude() {
        return longitude;
    }
}
