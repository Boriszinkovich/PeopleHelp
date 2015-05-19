package com.example.boris.androidproject.loaders_services;

import android.os.AsyncTask;
import android.util.Log;

import com.example.boris.androidproject.User;
import com.example.boris.androidproject.database.RequestDao;
import com.example.boris.androidproject.request.RequestInterface;

/**
 * Created by Boris on 17.04.2015.
 * сервіс(процес), що створює заявку в віддаленій базі данних
 */
public class CreateUserRequest extends AsyncTask<RequestInterface,Void,Void> {
    private String longitude;
    private String latitude;
    public CreateUserRequest(String longitude,String latitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    protected Void doInBackground(RequestInterface... params) {
        try {
            Log.d("Servis","create long ="+longitude+" lat = "+latitude+" params[0] "+params[0].getThem());
            RequestDao.createUserRequest(params[0],longitude,latitude);
        }catch (Exception e)
        {

        }
        return null;
    }
}
