package com.example.boris.androidproject.detail_activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.boris.androidproject.LocationDetector;
import com.example.boris.androidproject.ProfileActivity;
import com.example.boris.androidproject.R;
import com.example.boris.androidproject.request.FindRequest;
import com.example.boris.androidproject.request.RequestInterface;
import com.example.boris.androidproject.request.SosRequest;
import com.example.boris.androidproject.some_user.DownloadSomeUser;
import com.example.boris.androidproject.some_user.MapUser;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
/**
 * клас, що відображає детальну інформацію про заявку користувача на карті
 */
public class DetailactivityLayoutActivity extends ActionBarActivity implements ActionBar.TabListener {

    SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link android.support.v4.view.ViewPager} that will host the section contents.
     */
    private static MapUser user;
    ViewPager mViewPager;
   static int id;
    static DownloadSomeUser downloadSomeUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       if (savedInstanceState!=null) return;
        setContentView(R.layout.detailactivity_layout);
        ProfileActivity.WasRequest = true;
        final ActionBar actionBar = getSupportActionBar();
        Log.d("DownloadUser","BeforeExtra");
        id = getIntent().getIntExtra("id",1);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        getSupportActionBar().setElevation(0f);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0288D1")));
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff64c2f4")));
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });
        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this).setText(mSectionsPagerAdapter.getPageTitle(i)));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
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
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position+1);
        }
        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "Пользователь";
                case 1:
                    return "Карта";
            }
            return null;
        }
    }
    public static class PlaceholderFragment extends Fragment {
        GoogleMap map;
        Marker marker = null;
        Marker userMarker = null;
        private static final String ARG_SECTION_NUMBER = "section_number";
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = null;
            downloadSomeUser = new DownloadSomeUser(new ImageView(getActivity()),false,true);
            Log.d("DownloadUser","Before");
            downloadSomeUser.execute(id);
            try {
                user = downloadSomeUser.get();
                Log.d("DownloadUser","After" + user.getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            SupportMapFragment mapFragment;
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                {
                    rootView = inflater.inflate(R.layout.fragment_mapuser_detail, container, false);
                    ImageView view = (ImageView)rootView.findViewById(R.id.mapuserbigimage);
                    Picasso.with(getActivity()).load(user.getPhotoUrl()).resize(360,200).centerCrop().into(view);
                    initMapUserFragment(rootView);
                    break;
                }
                case 2:
                {
                    rootView = inflater.inflate(R.layout.map_detail_fragment,container,false);
                    mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment_id);
                    map = mapFragment.getMap();
                    break;
                }
                default:
                    rootView = inflater.inflate(R.layout.fragment_mapuser_detail, container, false);
                    break;
            }
            return rootView;
        }
        public void initMapUserFragment(View v)
        {
            TextView personName = (TextView)v.findViewById(R.id.myImageViewText);
            if ( personName!=null) personName.setText(user.getName()+" "+user.getLastName());
            RequestInterface requestInterface = user.getRequestInterface();
            if(requestInterface == null) return;
            TextView them = (TextView)v.findViewById(R.id.themFind);
            LinearLayout linearLayout = (LinearLayout)v.findViewById(R.id.happened_container);
            LinearLayout linearLayout2 = (LinearLayout)v.findViewById(R.id.additional_info_container);
            LinearLayout linearLayout3 = (LinearLayout)v.findViewById(R.id.end_date_container);
            LinearLayout viewGroup = (LinearLayout)v.findViewById(R.id.container_main);
            if (requestInterface.getThem()!=null&&!requestInterface.getThem().equals(""))
            {
                if (them!=null) them.setText(requestInterface.getThem());
            }
            else {
                viewGroup.removeView(linearLayout);
                viewGroup.removeView(linearLayout2);
                viewGroup.removeView(linearLayout3);
                return;
            }
            if (requestInterface instanceof SosRequest)
            {
                TextView text = (TextView)v.findViewById(R.id.search_mapuser_text);
                text.setText(user.getName()+" нуждается в срочной помощи");
                viewGroup.removeView(linearLayout2);
                viewGroup.removeView(linearLayout3);
            }
            else{
                FindRequest request = (FindRequest) requestInterface;
                TextView text = (TextView)v.findViewById(R.id.search_mapuser_text);
                text.setText(user.getName()+" ищет "+request.getFindType());
                TextView textView = (TextView)v.findViewById(R.id.what_happened);
                textView.setText("Тема ");
                if (request.getRequestComment()!=null) if (!request.getRequestComment().equals(""))
                {
                    TextView additional = (TextView)v.findViewById(R.id.commentFind);
                    additional.setText(request.getRequestComment());
                }
                else{
                    viewGroup.removeView(linearLayout2);
                }
                if (request.getLastData()!=null)
                {
                    TextView date = (TextView)v.findViewById(R.id.mapuser_data);
                    date.setText("02 10 2015 17:33");
                }
                else{
                    viewGroup.removeView(linearLayout3);
                }
            }
        }

        @Override
        public void onStart() {
            super.onStart();
           if (getArguments().getInt(ARG_SECTION_NUMBER)==2) {
               LocationDetector detector = new LocationDetector(getActivity());
               double latitude = detector.getLatitude();
               double longitude = detector.getLongitude();
               if (marker != null) {
                   marker.remove();
               }
               if (userMarker!=null)
               {
                   userMarker.remove();
               }
               LatLng fromPosition = new LatLng(latitude, longitude);
               LatLng toPosition = new LatLng(Double.parseDouble(user.getRequestInterface().getLongitude()), Double.parseDouble(user.getRequestInterface().getLatitude()));
               userMarker = map.addMarker(new MarkerOptions()
                       .position(toPosition)
                       .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                       .title(user.getName() + " " + user.getLastName()));
               marker = map.addMarker(new MarkerOptions()
                       .position(fromPosition)
                       .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                       .title("You"));
               CameraPosition cameraPosition = new CameraPosition.Builder()
                       .target(new LatLng(latitude, longitude))
                       .zoom(13)
                       .tilt(20)
                       .build();
               CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
               map.animateCamera(cameraUpdate);
           }
        }
    }
 }
