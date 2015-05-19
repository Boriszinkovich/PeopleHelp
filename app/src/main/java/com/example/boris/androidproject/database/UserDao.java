package com.example.boris.androidproject.database;

import android.util.Log;

import com.example.boris.androidproject.User;
import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseSession;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Boris on 11.04.2015.
 * клас, що має методи роботи з віддаленю базою данних, зокрема по зчитуванні та видаленні і створення данних користувачів
 */
public class UserDao {
    public static final String CLASSNAME = "userclass";
    public static final String USERID = "userid";
    public static final String USERNAME = "username";
    public static final String USERLASTNAME = "lastname";
    public static final String USERONLINE = "isonline";
    public static void setUser()
    {
        ParseObject object = new ParseObject(CLASSNAME);
        object.put(USERID, User.id);
        object.put(USERNAME,User.name);
        object.put(USERLASTNAME,User.lastName);
        object.put(USERLASTNAME,1);
        object.saveInBackground();
        Log.d("ran", "good1");
    }
    public static void setOnline(String userId, boolean isOnline)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(UserDao.CLASSNAME);
        query.whereEqualTo(USERID, userId);
        List<ParseObject> list = new ArrayList<ParseObject>();
        try {
            list = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(list.size() == 1)
        {
            ParseObject object = list.get(0);
            if (isOnline) object.put(USERONLINE,1);
            else object.put(USERONLINE,0);
            object.saveInBackground();
        }
    }
}
