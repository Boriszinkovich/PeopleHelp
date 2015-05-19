package com.example.boris.androidproject.fragments.menu;

import android.app.Activity;
import android.content.UriMatcher;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.boris.androidproject.ProfileActivity;
import com.example.boris.androidproject.R;
import com.example.boris.androidproject.User;
import com.example.boris.androidproject.authorization.DownloadProfile;

import java.io.File;

import static com.example.boris.androidproject.R.id.*;

/**
 * Created by Boris on 14.03.2015.
 * фрагмент з інформацією про користувача
 */
public class ProfileFragment extends Fragment {
    public  final String teg = "profile";
    public static TextView id;
    public static TextView name;
    public static boolean initialized = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState!=null) {
            View v =  super.onCreateView(inflater,container,savedInstanceState);
            return v;
        }
        Log.d("Menu","pashet");
        View v = (View)  inflater.inflate(R.layout.profile_fragment,container,false);
        id = (TextView) v.findViewById(R.id.RealId);
        name = (TextView)v.findViewById(R.id.AllName);
        id.setText("id = "+User.id);
        name.setText(User.name + " " + User.lastName);
        ImageView imageView1 = (ImageView)v.findViewById(R.id.userImage);
        File file = new File(DownloadProfile.ProfileImage);
        Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        imageView1.setImageBitmap(myBitmap);
        Log.d("Menu", "" + id.getText());
        initialized = true;
        Log.d("Menu","initialized");
        return v;
       // LinearLayout v= (LinearLayout) inflater.inflate(R.layout.profile_fragment,R.id.container,false);*/
    }

    @Override
    public void onStart() {
        super.onStart();
        RelativeLayout layout = (RelativeLayout)getActivity().findViewById(R.id.my_profile2);
        layout.setVisibility(View.VISIBLE);
        layout.setVisibility(View.VISIBLE);
        layout.setBackgroundColor(Color.parseColor("#0b98fff1"));
        layout.setBackgroundColor(Color.parseColor("#0b98fff1"));
        layout.setBackgroundColor(Color.parseColor("white"));
        //   layout.setBackgroundColor(Color.parseColor("gray"));
     //   layout.setBackgroundColor(Color.parseColor("gray"));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    //    ProfileActivity.initializeProfile();
    }

    @Override
    public void onResume() {
        super.onStart();

    }
}
