package com.example.boris.androidproject.loaders_services;

import android.os.AsyncTask;

import com.example.boris.androidproject.User;
import com.example.boris.androidproject.database.RequestDao;
import com.example.boris.androidproject.request.RequestInterface;

/**
 * Created by Boris on 18.04.2015.
 * сервіс(процес), що оновлює заявку в віддаленій базі данних
 */
public class UpdateUserRequest extends AsyncTask<RequestInterface,Void,Void> {
    @Override
    protected Void doInBackground(RequestInterface... params) {
        try {
            RequestDao.updateUserRequest(params[0],User.id);
        }catch (Exception e)
        {

        }
        return null;
    }
}
