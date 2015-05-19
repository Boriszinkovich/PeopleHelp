package com.example.boris.androidproject.fragments.request;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.boris.androidproject.LocationDetector;
import com.example.boris.androidproject.ProfileActivity;
import com.example.boris.androidproject.R;
import com.example.boris.androidproject.User;
import com.example.boris.androidproject.fragments.menu.MapPageFragment;
import com.example.boris.androidproject.loaders_services.CreateUserRequest;
import com.example.boris.androidproject.loaders_services.UpdateUserRequest;
import com.example.boris.androidproject.request.SosRequest;

/**
 * Created by Boris on 21.03.2015.
 * фрагмент, що представляє собою заявку надзвичайної допомоги користувача
 */
public class SosCreateRequest extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.my_sos_request,container,false);
        ProfileActivity.WasRequest = true;
           initializeButtons(v);
        if (MapPageFragment.setting == MapSetting.SosRequest_Created) initializeFields(v);
        return v;
    }
    public void initializeButtons(View v)
    {
        Button button = (Button) v.findViewById(R.id.saveButtonForSos);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String comment = ((EditText)(getView().findViewById(R.id.commentSos))).getText().toString();
                SosRequest request = new SosRequest(comment);
                User.setRequest(request);
                FragmentManager manager = getFragmentManager();
                //    Log.d("button","do"+manager.getBackStackEntryCount());
                ProfileActivity.WasRequest = false;
                MapPageFragment.colour = Color.RED;
                    UpdateUserRequest request1 = new UpdateUserRequest();
                    request1.execute(User.getRequest());

                MapPageFragment.setting = MapSetting.SosRequest_Created;

                goBack();
            }
        });
        Button button2 = (Button) v.findViewById(R.id.cancelButtonForSos);

        button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ProfileActivity.WasRequest = false;
                Log.d("button", "backgo");
                //  FragmentManager manager = getFragmentManager();
                //  manager.popBackStackImmediate();
                goBack();
            }
        });
    }

    public void goBack()
    {
        Fragment newFragment = new MapPageFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, newFragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }
    public void initializeFields(View v)
    {
        if (User.getRequest().getThem()!="") {
            EditText text = (EditText)v.findViewById(R.id.commentSos);
            text.setText(User.getRequest().getThem());
        }
    }
}
