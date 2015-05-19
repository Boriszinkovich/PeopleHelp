package com.example.boris.androidproject;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Boris on 14.03.2015.
 * ReadProfileFromFile клас, що читаэ ынформацыю з файлу
 * @author Boris
 */
public class ReadProfileFromFile extends AsyncTask<Void,Void,String> {
    private static String FileUrl = "profile.txt";

    /**
     * main executed class
     * @param params
     * @return
     */
    @Override
    protected String doInBackground(Void... params) {
        File file = new File(Environment.getExternalStorageDirectory()+File.separator+FileUrl);
        /**
         * reading data to file
         */
        try(BufferedReader reader = new BufferedReader(new FileReader(file)))
        {
            String id = reader.readLine();
            String lastName = reader.readLine();
            String name = reader.readLine();
            User.setAllProfile(name,lastName,Integer.parseInt(id)); //initializing user profile
        }catch (IOException e)
        {

        }
        return "dwda";
    }
}
