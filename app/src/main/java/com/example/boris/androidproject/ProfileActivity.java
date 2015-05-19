package com.example.boris.androidproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;


import com.example.boris.androidproject.authorization.ButtonEntryActivity;
import com.example.boris.androidproject.authorization.DownloadProfile;
import com.example.boris.androidproject.fragments.menu.MapPageFragment;
import com.example.boris.androidproject.fragments.menu.ProfileFragment;
import com.example.boris.androidproject.loaders_services.LoadAllMarkers;
import com.example.boris.androidproject.loaders_services.LocationService;
import com.google.android.gms.maps.GoogleMap;

import java.io.File;
import java.util.concurrent.ExecutionException;


/**
 * головне вікно програми з головним меню
 */
public class ProfileActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    public static FragmentManager manager;
    public static boolean WasRequest = false;

    private FragmentTransaction transaction;
    public static GoogleMap mMap;
 //   public static LocationManager locationManager;
    private ProfileFragment profileFragment = new ProfileFragment();
    private static boolean toMap = false;
    /**
     * Used to store the last screen title. For use in {}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        manager = getSupportFragmentManager();
        super.onCreate(savedInstanceState);
        Log.d("Ura","shit");
        setContentView(R.layout.activity_profile);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        getSupportActionBar().setElevation(0f);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0288D1")));
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        File file = new File(Environment.getExternalStorageDirectory()+File.separator+"profile.txt");
        if (file.exists()) file.delete();
        if (file.exists())
        {
            ReadProfileFromFile readProfileFromFile = new ReadProfileFromFile();
            readProfileFromFile.execute();
            try {
                String s = readProfileFromFile.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        else{
            if (!User.authorizationProcess){
                Intent intent = new Intent(getApplicationContext(), ButtonEntryActivity.class);
                startActivity(intent);
                onPause();
            }
                //      onDestroy();
        }
        if (!(new File("profile.txt")).exists()&&User.authorizationProcess)
        {
            DownloadProfile downloadProfile = new DownloadProfile(User.getProfileRequestUrl(),getApplicationContext());
            downloadProfile.execute();
            Log.d("Ura","finally2");
            try {
                String s = downloadProfile.get();
                //   Toast.makeText("")
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        if (WasRequest)
        {
            transaction = manager.beginTransaction();
            transaction.add(R.id.container,new MapPageFragment() , "mapPageFragment");
            transaction.commit();
            WasRequest = false;
        }
        else if (savedInstanceState == null) {
            transaction = manager.beginTransaction();
            transaction.add(R.id.container, profileFragment, profileFragment.teg);
            transaction.commit();
        }
       // Intent serviceintent = new Intent(getApplicationContext(), LocationService.class);
       // startService(serviceintent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("button","enjoy");
    }



    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch(position) {
            case 0:
                if(fragmentManager.findFragmentByTag(profileFragment.teg) != null) {
                    //if the fragment exists, show it.
                    fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(profileFragment.teg)).commit();
                } else {
                    //if the fragment does not exist, add it to fragment manager.
                    fragmentManager.beginTransaction().add(R.id.container, new ProfileFragment(), profileFragment.teg).commit();
                }
                if(fragmentManager.findFragmentByTag(MapPageFragment.teg) != null){
                    //if the other fragment is visible, hide it.
                    fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(MapPageFragment.teg)).commit();
                }
                break;
            case 2:
                if(fragmentManager.findFragmentByTag(MapPageFragment.teg) != null) {
                    //if the fragment exists, show it.
                    fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(MapPageFragment.teg)).commit();
                } else {
                    //if the fragment does not exist, add it to fragment manager.
                    fragmentManager.beginTransaction().add(R.id.container, new MapPageFragment(), MapPageFragment.teg).commit();
                }
                if(fragmentManager.findFragmentByTag(profileFragment.teg) != null){
                    //if the other fragment is visible, hide it.
                    fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(profileFragment.teg)).commit();
                }
                break;
        }
       if(position == 2) {
           Log.d("Menu","mappp "+position);
           manager = getSupportFragmentManager();
       }
      /*  transaction = fragmentManager.beginTransaction();
        transaction
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1));
     // if(position == 0) transaction.replace(R.id.container, PlaceholderFragment.newInstance(position + 1));
       // transaction.addToBackStack(null);
        transaction.commit();*/
  //      transaction.commit();
        Log.d("Menu",""+position);
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Fragment newInstance(int sectionNumber) {
            Fragment fragment = new PlaceholderFragment();
          if (sectionNumber == 1) fragment = new ProfileFragment();
            if (sectionNumber == 3) {
                fragment = new MapPageFragment();
            }
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
            return rootView;
          //  return null;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((ProfileActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    @Override
    public void onBackPressed() {
        if (WasRequest) {
            WasRequest = false;
            transaction = getSupportFragmentManager().beginTransaction();
            transaction .replace(R.id.container, new MapPageFragment());
           // transaction.addToBackStack(null);
            transaction.commit();
            return;
        }
        super.onBackPressed();
    }
}
