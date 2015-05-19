package com.example.boris.androidproject;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Boris on 11.04.2015.
 * головний клас проекту, необхідний для синхронізації з віддаленою базою данних (на Parse.com) при першому використанні додатку
 */
public class AndroidProject extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //  super.onCreate();

        // Initialize Crash Reporting.

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        //Parse.initialize(this, "tJnMKt7X0o9GqFxhsJJuiX2kJgWgZWysDCa8HkeS", "v0FkSCZuezC5ZX2kjtcAzfX12plMrS8AP5EGBUql");
        Log.d("initialized", "jjj");
        //  Parse.initialize(this, "GjPjY5gSyazmxQQ5Z0zqymhE9zPfPyUhIZj6UWQy", "8DLjAS0SbDNyPnL1stvBjr98sFnOXcwpx2XSahjP");
        Parse.initialize(this);
        ParseUser.enableAutomaticUser();
       // ParseObject object = new ParseObject("zz");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
