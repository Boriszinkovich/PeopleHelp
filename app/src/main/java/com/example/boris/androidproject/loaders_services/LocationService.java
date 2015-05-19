package com.example.boris.androidproject.loaders_services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.example.boris.androidproject.ConnectionDetector;
import com.example.boris.androidproject.User;
import com.example.boris.androidproject.database.RequestDao;


/**
 * Created by Boris on 17.04.2015.
 */
public class LocationService extends Service {
    private Thread t;
    private UpdateThread updateThread;
    private LocationManager locationManager;
    private LocationListener listener;
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        updateThread = new UpdateThread();
        t = new Thread(updateThread);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);
        try {
            ConnectionDetector detector = new ConnectionDetector(getApplicationContext());
          if (detector.isConnectingToInternet() && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))  {
              updateThread.updateCoords(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude(), locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude());
              Log.d("Servisl","before");
               t.start();
          }
            else   if (detector.isConnectingToInternet() && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))  {
              updateThread.updateCoords(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude(), locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude());
              t.start();
          }

        }
        catch (Exception e)
        {

        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public class UpdateThread implements Runnable{
        private double longitude;
        private double latitude;
        public UpdateThread()
        {
            this.latitude = latitude;
            this.longitude = longitude;
        }
       public void  updateCoords(double latitude,double longitude){
           this.latitude = latitude;
           this.longitude = longitude;
       }
        @Override
        public void run() {
            try{
            RequestDao.updateUserLocation(""+User.id,""+longitude,""+latitude);      }
            catch (Exception e)
            {
                Log.e("Servis","Location servis update error");
            }
        }
    }
    public class MyLocationListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            ConnectionDetector detector = new ConnectionDetector(getApplicationContext());
            if (detector.isConnectingToInternet()) {
                updateThread.updateCoords(location.getLatitude(), location.getLongitude());
                t.start();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            ConnectionDetector detector = new ConnectionDetector(getApplicationContext());
            if (detector.isConnectingToInternet()&&provider.equals(LocationManager.NETWORK_PROVIDER) )  {
                updateThread.updateCoords(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude(), locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude());
                t.start();
            }
            else if (detector.isConnectingToInternet()&&provider.equals(LocationManager.GPS_PROVIDER) )  {
                updateThread.updateCoords(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude(), locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude());
                t.start();
            }

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
