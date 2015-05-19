package com.example.boris.androidproject.loaders_services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.boris.androidproject.ConnectionDetector;
import com.example.boris.androidproject.database.RequestDao;
import com.example.boris.androidproject.fragments.menu.MapPageFragment;
import com.example.boris.androidproject.request.FindRequest;
import com.example.boris.androidproject.request.RequestInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * сервіс, що завантажує кожні 60 секунд всі заявки з віддаленої бази данних
 */

public class LoadAllMarkers extends Service {
    ExecutorService es;
    GetTimeTask mr = null;
    MyBinder binder = new MyBinder();
    Intent intent;
    List<RequestInterface> list = new ArrayList<>();
    public LoadAllMarkers() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        es = Executors.newFixedThreadPool(1);
    }
    public List<RequestInterface> getList()
    {
        return list;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mr == null)
        {
            mr = new GetTimeTask();
            mr.start();
        }
        mr.cango= true;
        String z = "";
       // RequestDao.createUserRequest(new SosRequest("sos1"),""+50,""+60,77312533);
       FindRequest bb = new FindRequest(false,false,"sdfs","dfsdf","wefwe");
     //   RequestDao.createUserRequest(bb,""+(-22),""+(-49),115457842);
        try {
            z = intent.getStringExtra("dd");
        }catch (NullPointerException e)
        {
            Log.d("Service","Exception in extra");
        }
        Log.d("Servis","FirstGone");
        Log.d("Servis",z);
    //    mr = new GetTimeTask();
        Log.d("Servis","mr start");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
       if (mr!=null) {
           mr.interrupt();
       }
        return super.onUnbind(intent);
    }
    public void update()
    {
        //mr.interrupt();
        mr.cango = true;
        try {
           mr.join(100);
 //           mr = new GetTimeTask();
            mr.start();
        } catch (Exception e) {
            Log.d("Servis","cant join" + e.toString());
        }
    }


    class GetTimeTask extends Thread {
        public GetTimeTask()
        {
            Log.d("Servis","instart constructor");
            Log.d("Servis","in constructor");
        }
        private boolean cango = false;
        @Override
        public void run() {
            Intent intent = new Intent(MapPageFragment.BROADCAST_ACTION);
            ConnectionDetector detector = new ConnectionDetector(getApplicationContext());
            while (true) {
                try{
                cango = false;
                while (!detector.isConnectingToInternet()) {
                    Thread.sleep(1000);
                    Log.d("Servis", "is sleeping because the internet");
                }
                    Log.d("Servis", "Here");
                    if (detector.isConnectingToInternet()) list = RequestDao.getAllRequests();
                    sendBroadcast(intent);
                    Log.d("Servis", "BeforeWork");
                    int counter = 0;
                    while(!cango&&counter<300) {
                        Thread.sleep(200);
                        counter++;
                    }
                    Log.d("Loader", "Worked");
            }
                catch (Exception e)
                {
                    Log.d("Service","Interrupted");
                }

            }
        }
    }
    public class MyBinder extends Binder{
        public LoadAllMarkers getService()
        {
            return LoadAllMarkers.this;
        }
    }
}
