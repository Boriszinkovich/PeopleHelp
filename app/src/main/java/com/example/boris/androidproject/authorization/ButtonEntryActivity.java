package com.example.boris.androidproject.authorization;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.boris.androidproject.R;
import com.example.boris.androidproject.authorization.MainActivity;

/**
 * Created by Boris on 14.03.2015.
 * класс з вікном з кнопкою "війти" для авторизації ВК
 */
public class ButtonEntryActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout2);
    }
    public void onButtonClicked(View view)
    {
        Log.d("Ura","gone");
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
 //      onStop();
        onStop();
    }
}
