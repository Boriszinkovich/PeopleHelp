package com.example.boris.androidproject.fragments.menu;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.boris.androidproject.LocationDetector;
import com.example.boris.androidproject.MyHelpDetail;
import com.example.boris.androidproject.ProfileActivity;
import com.example.boris.androidproject.R;
import com.example.boris.androidproject.SosDetailActivity;
import com.example.boris.androidproject.User;
import com.example.boris.androidproject.database.RequestType;
import com.example.boris.androidproject.detail_activity.DetailactivityLayoutActivity;
import com.example.boris.androidproject.fragments.request.MapSetting;
import com.example.boris.androidproject.loaders_services.CreateUserRequest;
import com.example.boris.androidproject.loaders_services.DeleteUserRequest;
import com.example.boris.androidproject.loaders_services.LoadAllMarkers;
import com.example.boris.androidproject.request.FindRequest;
import com.example.boris.androidproject.request.RequestInterface;
import com.example.boris.androidproject.request.SosRequest;
import com.example.boris.androidproject.some_user.DownloadSomeUser;
import com.example.boris.androidproject.some_user.MapUser;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * фрагмент з картою пошуку людей, а також можливістю створення власної заявки та перегляду заявок користувачів на карті у вигляді маркерів
 */
public class MapPageFragment extends Fragment  {
    public final static String BROADCAST_ACTION = "ru.startandroid.develop.p0961servicebackbroadcast";
    boolean bound = false;
    public static final String teg = "MapFragment";
    ServiceConnection sConn;
    Intent intent;
    LoadAllMarkers myService;
    MapUser mapUser = null;
    BroadcastReceiver br;
    private Marker locationMarker = null;
    private HashMap<Marker,RequestInterface> mapRequest;
    private static GoogleMap mMap;
    private int destroyed = 0;
    public static int colour = Color.YELLOW;
    public static MapSetting setting = MapSetting.Non_Created;
    LocationManager locationManager;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState!=null) {
           View v =  super.onCreateView(inflater,container,savedInstanceState);
            return v;
        }
        if (container == null) return null;
        mapRequest = new HashMap<>();
       View v =  inflater.inflate(R.layout.map_fragment,container,false);
        mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
        locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
       initializeButtons(v);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5, 10,locationListener );
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 10, locationListener);
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                RequestInterface requestInterface =  mapRequest.get(marker);
                View v = getLayoutInflater(null).inflate(R.layout.small_request_viev,null);
                if (requestInterface!=null) {
                    DownloadSomeUser downloadSomeUser = new DownloadSomeUser((ImageView)v.findViewById(R.id.smallUserImage),true,false);
                    downloadSomeUser.execute(Integer.parseInt(requestInterface.getUserId()));
                    try {
                         mapUser = downloadSomeUser.get();
                        Log.d("UserMapView",mapUser.getName());
                        ((TextView) v.findViewById(R.id.MapUsername)).setText(mapUser.getName());
                        ((TextView) v.findViewById(R.id.MapUserLastName)).setText(mapUser.getLastName());
                        if (requestInterface instanceof FindRequest)((TextView) v.findViewById(R.id.smallUserImageKategory)).setText("Категория: "+((FindRequest)requestInterface).getFindType());
                        ((TextView) v.findViewById(R.id.smallUserImageThem)).setText(requestInterface.getThem());
                        Bitmap bitmap = mapUser.getSmallBitmap();
                        ((ImageView)v.findViewById(R.id.smallUserImage)).setImageBitmap(mapUser.getSmallBitmap());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                return v;
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.hideInfoWindow();
                if (!marker.equals(locationMarker)) marker.showInfoWindow();
                return true;
            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.d("my_detail","go");
                RequestInterface requestInterface = mapRequest.get(marker);
                int id = Integer.parseInt(requestInterface.getUserId());
                Intent intent1 = new Intent(getActivity(), DetailactivityLayoutActivity.class);
                intent1.putExtra("id",id);
                intent1.putExtra("name",mapUser.getName());
                intent1.putExtra("lastname",mapUser.getLastName());
                startActivity(intent1);
            }
        });
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)&& !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) checkEnabled();
        Log.d("Servis","oncreatestart");
        sConn = new ServiceConnection() {

            public void onServiceConnected(ComponentName name, IBinder binder) {
                Log.d("Servis", "MainActivity onServiceConnected");
                myService = ((LoadAllMarkers.MyBinder) binder).getService();
                bound = true;
            }

            public void onServiceDisconnected(ComponentName name) {
                Log.d("Servis", "MainActivity onServiceDisconnected");
                myService.onUnbind(new Intent());
                bound = false;
            }
        };
        br = new MyBrodcast();
        IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
        // регистрируем (включаем) BroadcastReceiver
        getActivity().registerReceiver(br, intFilt);
        Log.d("Lifecycle","onCreateView");
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d("Lifecycle","onCreate");

    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(br);
        super.onDestroy();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
         destroyed++;
        menu.clear();
        inflater.inflate(R.menu.menu_main,menu);
        Log.d("Lifecycle","onCreateOptionsmenu" + destroyed);
    }
    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
        Log.d("ActionBar","Destroyed");
    }
    @Override
    public void onStart() {
        super.onStart();
        intent = new Intent(getActivity().getApplicationContext(), LoadAllMarkers.class).putExtra("dd","a");
        try {

            getActivity().bindService(intent, sConn, 0);
        }catch (Exception e)
        {
        }
        initializeButtons(getView());
        createUserMarker(0,0,false);
        changeColor();
        getActivity().startService(intent);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().stopService(intent);
        bound = false;
    }
    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
        if (!bound) return;
        myService.onUnbind(new Intent());
        getActivity().stopService(intent);
        bound = false;
        onDestroyOptionsMenu();
    }
    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location)
        {
           if (locationMarker == null )  {
               locationMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Marker")
                       .icon(BitmapDescriptorFactory.fromBitmap(createGreen(colour))));
           }
              else  locationMarker.setPosition(new LatLng(location.getLatitude(),location.getLongitude()));
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d("map","disabled"+provider);
        }
        @Override
        public void onProviderEnabled(String provider) {
            if (provider.equals(LocationManager.NETWORK_PROVIDER)){
                mMap.clear();
                createUserMarker(0,0,false);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (provider.equals(LocationManager.GPS_PROVIDER)) {
            } else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
            }
        }
    };
    public void checkEnabled()
    {
    }
    public void initializeButtons(View v)
    {
        View viewSos= v.findViewById(R.id.button_sos_created_container);
        View viewNon= v.findViewById(R.id.button_non_created_container);
        View viewHelp= v.findViewById(R.id.button_help_created_container);
        final View viewSos2 = viewSos;
        final View viewHelp2 = viewHelp;
        final View viewNon2 = viewNon;
        if (MapPageFragment.setting == MapSetting.Non_Created) {
            viewSos.setVisibility(View.GONE);
            viewHelp.setVisibility(View.GONE);
            viewSos.setVisibility(View.GONE);
            viewHelp.setVisibility(View.GONE);
            viewNon.setVisibility(View.VISIBLE);
            Button buttonHelp = (Button) v.findViewById(R.id.buttonHelp);
            buttonHelp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), MyHelpDetail.class);
                    startActivity(intent);
                }
            });
            Button buttonSos = (Button) v.findViewById(R.id.buttonSos);
            buttonSos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SosRequest request = new SosRequest();
                    User.setRequest(request);
                    ProfileActivity.WasRequest = false;
                    MapPageFragment.colour = Color.RED;
                    MapPageFragment.setting = MapSetting.SosRequest_Created;
                    LocationDetector detector = new LocationDetector(getActivity().getApplicationContext());
                    CreateUserRequest request1 = new CreateUserRequest(""+detector.getLatitude(),""+detector.getLongitude());
                    request1.execute(new SosRequest());
                    initializeButtons(getView());
                    changeColor();
                }
            });

            return;
        }
        if (MapPageFragment.setting == MapSetting.HelpRequest_Created) {
            viewSos.setVisibility(View.GONE);
            viewHelp.setVisibility(View.VISIBLE);
            viewNon.setVisibility(View.GONE);
            Button buttonCancelHelp = (Button) v.findViewById(R.id.buttonCancelHelp);
            buttonCancelHelp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeColor();
                    setting = MapSetting.Non_Created;
                    DeleteUserRequest deleteUserRequest = new DeleteUserRequest();
                    deleteUserRequest.execute();
                    viewSos2.setVisibility(View.GONE);
                    viewHelp2.setVisibility(View.GONE);
                    viewNon2.setVisibility(View.VISIBLE);
                }
            });
            Button buttonRedactHelp = (Button) v.findViewById(R.id.buttonResetHelp);
            buttonRedactHelp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), MyHelpDetail.class);
                    startActivity(intent);
                }
            });
            Button buttonSos2 = (Button) v.findViewById(R.id.buttonSosCreate);
            buttonSos2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeleteUserRequest deleteUserRequest = new DeleteUserRequest();
                    deleteUserRequest.execute();
                    SosRequest request = new SosRequest();
                    User.setRequest(request);
                    LocationDetector detector = new LocationDetector(getActivity().getApplicationContext());
                    CreateUserRequest request1 = new CreateUserRequest(""+detector.getLatitude(),""+detector.getLongitude());
                    request1.execute(request);
                    ProfileActivity.WasRequest = false;
                    MapPageFragment.colour = Color.RED;
                    MapPageFragment.setting = MapSetting.SosRequest_Created;
                    changeColor();
                    initializeButtons(getView());
                }
            });
            return;
        }
        if (MapPageFragment.setting == MapSetting.SosRequest_Created){
            viewHelp.setVisibility(View.GONE);
            viewNon.setVisibility(View.GONE);
            viewHelp.setVisibility(View.GONE);
            viewNon.setVisibility(View.GONE);
            viewSos.setVisibility(View.VISIBLE);
            Button buttonSosCreate2 = (Button)v.findViewById(R.id.buttonCancelSos);
            buttonSosCreate2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewSos2.setVisibility(View.GONE);
                    viewHelp2.setVisibility(View.GONE);
                    viewNon2.setVisibility(View.VISIBLE);
                    setting = MapSetting.Non_Created;
                    DeleteUserRequest deleteUserRequest = new DeleteUserRequest();
                    deleteUserRequest.execute();
             if (locationMarker!=null)  changeColor();
                }
            });
            Button buttonSosRedact = (Button) v.findViewById(R.id.buttonRedactSos);
            buttonSosRedact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), SosDetailActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
    public Bitmap createGreen(int colour)
    {
        int[] colors = new int[30*30];
        Arrays.fill(colors, 0, 30 * 30, colour);
        return Bitmap.createBitmap(colors,30,30, Bitmap.Config.RGB_565);
    }
    public void createUserMarker(double longitude,double latitude, boolean has)
    {
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)||locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (setting == MapSetting.Non_Created) colour = Color.YELLOW;
            if (setting == MapSetting.HelpRequest_Created) colour = Color.GREEN;
            if (setting == MapSetting.SosRequest_Created) colour = Color.RED;
            if (mMap == null) return;
            if (has) {
                locationMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Marker")
                        .icon(BitmapDescriptorFactory.fromBitmap(createGreen(colour))));
                return;
            }
            try {
                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    locationMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude(), locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude())).title("Marker")
                            .icon(BitmapDescriptorFactory.fromBitmap(createGreen(colour))));
                } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null)
                    locationMarker = mMap.addMarker(new MarkerOptions().
                            position(new LatLng(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).
                                    getLatitude(), locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).
                                    getLongitude())).
                            title("Marker")
                            .icon(BitmapDescriptorFactory.fromBitmap(createGreen(colour))));
            }catch (Exception e) {


            }            }
    }
    public void changeColor()
    {
       if (locationMarker != null) locationMarker.remove();
        createUserMarker(0,0,false);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_refresh) {
           myService.update();
        };
        return super.onOptionsItemSelected(item);
    }
    private class MyBrodcast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                List<RequestInterface> data = myService.getList();
                if (data == null) return;
                mMap.clear();
                createUserMarker(0, 0, false);
                Log.d("Servis",""+data.size());
                for (RequestInterface requestInterface : data) {
                   if (requestInterface.getUserId().equals(""+User.id)) return;
                    Marker marker = null;
                    String longitude = requestInterface.getLongitude();
                    String latitude = requestInterface.getLatitude();
                    String them = requestInterface.getThem();
                    String type = requestInterface.getRequestType();
                    if (them.length() > 20) them = them.substring(0, 20);
                    if (type.equals(RequestType.HelpRequest)||type.equals(RequestType.HelpRequestWithDate)||type.equals(RequestType.HelpRequestWithDateAndPlace)||type.equals(RequestType.HelpRequestWithPlace)) {
                        marker = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(longitude), Double.parseDouble(latitude))).title(them)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                       // Log.d("Servis","Marker help created");
                    } else if (type.equals(RequestType.SosRequest)){
                        marker = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(longitude), Double.parseDouble(latitude))).title(them)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    }
                    mapRequest.put(marker, requestInterface);
                }
            }catch (Exception e)
            {
            }
        }
    }
}
