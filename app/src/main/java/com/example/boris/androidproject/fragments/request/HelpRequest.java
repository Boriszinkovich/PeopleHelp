package com.example.boris.androidproject.fragments.request;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.boris.androidproject.LocationDetector;
import com.example.boris.androidproject.ProfileActivity;
import com.example.boris.androidproject.R;
import com.example.boris.androidproject.User;
import com.example.boris.androidproject.fragments.menu.MapPageFragment;
import com.example.boris.androidproject.loaders_services.CreateUserRequest;
import com.example.boris.androidproject.loaders_services.UpdateUserRequest;
import com.example.boris.androidproject.request.FindRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Created by Boris on 21.03.2015.
 * фрагмент, що представляє собою заявку для пошуку людей за тематикою
 */
public class HelpRequest extends Fragment {
    private boolean parseException ;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parseException = false;
        View v =  inflater.inflate(R.layout.my_help_request,container,false);
         ProfileActivity.WasRequest = true;
        if (MapPageFragment.setting == MapSetting.HelpRequest_Created) initializeFields(v);
        initializeButtons(v);
        return v;
    }

    public void initializeButtons(View v)
    {
        Button button = (Button) v.findViewById(R.id.saveHelpButton);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                parseException = false;
                UserInformationReader reader = new UserInformationReader();
                reader.execute();
                try {
                    reader.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    reader.cancel(true);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if (parseException) return;
                ProfileActivity.WasRequest = false;
               MapPageFragment.colour = Color.GREEN; // changeColour
                if (MapPageFragment.setting == MapSetting.Non_Created && User.getRequest()!=null) {
                    LocationDetector detector = new LocationDetector(getActivity().getApplicationContext());
                    CreateUserRequest request1 = new CreateUserRequest(""+detector.getLatitude(),""+detector.getLongitude());
                    request1.execute(User.getRequest());
                }
                else  if (MapPageFragment.setting == MapSetting.HelpRequest_Created && User.getRequest()!=null) {
                    UpdateUserRequest request1 = new UpdateUserRequest();
                    request1.execute(User.getRequest());
                }
                MapPageFragment.setting = MapSetting.HelpRequest_Created;
                getActivity().finish();
                if (!parseException) {
                   getActivity().finish();

               }
            }
        });
        Button button2 = (Button) v.findViewById(R.id.cancelHelpButton);

        button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ProfileActivity.WasRequest = false;
                Log.d("button","backgo");
               // goBack();
               // getFragmentManager().popBackStackImmediate();
                getActivity().onBackPressed();
               // onDestroyView();
            }
        });
        ((CheckBox)(v.findViewById(R.id.checkUserDate))).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
           ((EditText)(getView().findViewById(R.id.endDate))).setEnabled(!((CheckBox)(v.findViewById(R.id.checkUserDate))).isChecked());
            }
        });
    }


    private class UserInformationReader extends AsyncTask<Void,Void,String>
    {

        @Override
        protected String doInBackground(Void... params) {
            FindRequest userRequest;
            String them,comment,findType,dateToParse;
            try {
                them = ((EditText) (getView().findViewById(R.id.themHelp))).getText().toString();
            }catch (Exception e)
            {
                them = " ";
            }
            try {
                comment = ((EditText) (getView().findViewById(R.id.commentHelp))).getText().toString();
            }catch (Exception e)
            {
             comment = " ";
            }
            try {
      //          findType = ((EditText) (getView().findViewById(R.id.moneyHelp))).getText().toString();
            }catch (Exception e)
            {
                findType = " ";
            }
            findType = "";
            boolean hasDate;
            boolean setOffline;
            setOffline = ((CheckBox)(getView().findViewById(R.id.checkUserOffline))).isChecked();
            if (((CheckBox)(getView().findViewById(R.id.checkUserDate))).isChecked())
            {
                userRequest = new FindRequest(false,setOffline,them,comment,findType);
                userRequest.setHasDate(true);
                User.setRequest(userRequest);
            }
            else {
                try {
                    dateToParse = ((EditText) (getView().findViewById(R.id.endDate))).getText().toString();
                    Date date = parseDate(dateToParse);
                    userRequest = new FindRequest(true, setOffline, them, comment, findType, date);
                    User.setRequest(userRequest);
                } catch (Exception e) {
                    dateToParse = "";
                    parseException = true;
                }
            }

            return "ee";
        }
    }
    private Date parseDate(String a)
    {
        SimpleDateFormat format =
                new SimpleDateFormat("dd.MM.yyyy KK:mm");
        Date date = null;
        try {
            date = format.parse(a);
        } catch (ParseException e) {
        parseException = true;            // ParseException
            Toast.makeText(getParentFragment().getActivity().getApplicationContext(),"Error date. Try again please)",Toast.LENGTH_LONG).show();
        }
        finally {
            if (date == null) date = new Date();
        }
            return date;
    }
    public void initializeFields(View v)
    {
        FindRequest request = (FindRequest)User.getRequest();
        Log.d("Help.request",request.getThem());
        ((EditText)(v.findViewById(R.id.themHelp))).setText(request.getThem());
        Log.d("Help.request",request.getRequestComment());
        ((EditText)(v.findViewById(R.id.commentHelp))).setText(request.getRequestComment());
     //   ((EditText)(v.findViewById(R.id.moneyHelp))).setText(request.getFindType());
        Log.d("Help.request",""+request.hasDate());
        ((CheckBox)(v.findViewById(R.id.checkUserDate))).setChecked(request.hasDate());
        Log.d("Help.request",""+request.isOffLineWatched());
        ((CheckBox)(v.findViewById(R.id.checkUserOffline))).setChecked(request.isOffLineWatched());
        if (request.hasDate()) {
            String ss;
            Log.d("Help.request",""+request.getLastData().getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy KK:mm");
            Log.d("Help.request","gfsgsd");
            ss = dateFormat.format(request.getLastData());
            ((EditText)(v.findViewById(R.id.endDate))).setText(ss);
        }

    }
}
