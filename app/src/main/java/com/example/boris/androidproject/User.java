package com.example.boris.androidproject;

import com.example.boris.androidproject.request.RequestInterface;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * Created by Boris on 02.03.2015.
 * клас з інформацією авторизованого користувача
 */
public class User {
    public static String name = null;
    public static String lastName = null;
    public static boolean authorizationProcess = false;
    public static int id = 0;
    private static String token;
    private static RequestInterface request;
    public static void setAllProfile(String name,String surname,int id)
   {
       User.name = name;
       User.lastName = surname;
       User.id = id;
   }

    public static void setRequest(RequestInterface request) {
        User.request = request;
    }

    public static RequestInterface getRequest() {
        return request;
    }

    public static boolean isInitialized()
    {
        if (id == 0) return false;
        return true;
    }
    public static void startAuthorization()
    {
        authorizationProcess = true;
    }
    public static void setTokenAndId(String token,String id)
    {
        User.id = Integer.parseInt(id);
        User.token = token;
    }
    public static String getProfileRequestUrl()
    {
      return "https://api.vkontakte.ru/method/getProfiles?uid=" + id +"&fields=photo_200 "+"&access_token=" + token;
    }
}
