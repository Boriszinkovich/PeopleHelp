package com.example.boris.androidproject.database;

import android.content.Context;
import android.util.Log;
import com.example.boris.androidproject.ConnectionDetector;
import com.example.boris.androidproject.User;
import com.example.boris.androidproject.request.FindRequest;
import com.example.boris.androidproject.request.RequestInterface;
import com.example.boris.androidproject.request.SosRequest;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * клас, що має методи роботи з віддаленою базою данних, зокрема методи по зчитуванню, видаленню та створенню запитів
 */
public class RequestDao {
    private static Context context = null;
    public static final String ClassName ="RequestTable10";
    public static final String UserId = "UserId";
    public static final String Type = "RequestType";
    public static final String IsOnlineNow = "Online";
    public static final String RequestThem = "RequestThem";
    public static final String RequestComment = "RequestComment";
    public static final String FindType = "FindType";
    public static final String OfflineVisibility = "RequestOffline";
    public static final String HasData = "requesthasdata";
    public static final String Data = "Data";
    public static final String HasConcretePlace = "ConcretePlace";
    public static final String Longitude = "Longitude";
    public static final String Latitude = "Latitude";
    public static void setContext(Context context)
    {
        RequestDao.context = context;
    }
    public static void updateUserLocation(String id,String latitude,String longitude)
    {

        ParseQuery<ParseObject> query = ParseQuery.getQuery(ClassName);
        query.whereEqualTo(UserId, id);
        List<ParseObject> list = new ArrayList<>();
        try {
            list = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (list.size() ==  1)
        {
            ParseObject object =  list.get(0);
            object.put(Latitude,latitude);
            object.put(Longitude,longitude);
            object.saveInBackground();
        }
        else Log.d("DaoUpdate", "fuck") ; }
    public static void createUserRequest(RequestInterface request,String longitude,String latitude) {
        ConnectionDetector detector = new ConnectionDetector(context);
        ParseObject object = new ParseObject(ClassName);
        object.put(UserId, User.id);
        object.put(RequestThem, request.getThem());
        object.put(Longitude, longitude);
        object.put(Latitude, latitude);
        if (request instanceof SosRequest) {
            object.put(Type, RequestType.SosRequest);
            object.put(RequestComment, "");
            object.put(FindType, "");
            object.put(OfflineVisibility, true);
            object.put(HasData, false);
            object.put(Data, new Date());
            object.put(HasConcretePlace, false);
            object.put(IsOnlineNow,true);
            object.saveInBackground();
        } else {
            if (request instanceof FindRequest) {
                FindRequest helprequest = (FindRequest) request;
                object.put(RequestComment, helprequest.getRequestComment());
                object.put(FindType, helprequest.getFindType());
                object.put(IsOnlineNow,helprequest.isOnline());
                object.put(OfflineVisibility, ((FindRequest) request).isOffLineWatched());
                object.put(HasData, helprequest.hasDate());
                object.put(Data, helprequest.getLastData());
                object.put(HasConcretePlace, helprequest.isHasConcretePlace());
                if (helprequest.hasDate()) {
                    if (helprequest.isHasConcretePlace()) {
                        object.put(Type, RequestType.HelpRequestWithDateAndPlace);
                    } else object.put(Type, RequestType.HelpRequestWithDate);
                } else object.put(Type, RequestType.HelpRequestWithPlace);
                object.saveInBackground();
            }
        }

    }
    public static void createUserRequest(RequestInterface request,String longitude,String latitude,int id) {
        ConnectionDetector detector = new ConnectionDetector(context);
        ParseObject object = new ParseObject(ClassName);
        object.put(UserId, id);
        object.put(RequestThem, request.getThem());
        object.put(Longitude, longitude);
        object.put(Latitude, latitude);
        if (request instanceof SosRequest) {
            object.put(Type, RequestType.SosRequest);
            object.put(RequestComment, "");
            object.put(FindType, "");
            object.put(OfflineVisibility, true);
            object.put(HasData, false);
            object.put(Data, new Date());
            object.put(HasConcretePlace, false);
            object.put(IsOnlineNow,true);
            object.saveInBackground();
        } else {
            if (request instanceof FindRequest) {
                FindRequest helprequest = (FindRequest) request;
                object.put(RequestComment, helprequest.getRequestComment());
                object.put(FindType, helprequest.getFindType());
                object.put(IsOnlineNow,helprequest.isOnline());
                object.put(OfflineVisibility, ((FindRequest) request).isOffLineWatched());
                object.put(HasData, helprequest.hasDate());
                object.put(Data, helprequest.getLastData());
                object.put(HasConcretePlace, helprequest.isHasConcretePlace());
                if (helprequest.hasDate()) {
                    if (helprequest.isHasConcretePlace()) {
                        object.put(Type, RequestType.HelpRequestWithDateAndPlace);
                    } else object.put(Type, RequestType.HelpRequestWithDate);
                } else object.put(Type, RequestType.HelpRequestWithPlace);
                object.saveInBackground();
            }
        }
    }
        public static void updateUserRequest(RequestInterface n,int idUser)
        {
            ConnectionDetector detector = new ConnectionDetector(context);
            if (!detector.isConnectingToInternet()) return;
            ParseQuery<ParseObject> query = ParseQuery.getQuery(ClassName);
            query.whereEqualTo(UserId, idUser);
            List<ParseObject> list = new ArrayList<>();
            try {
                list = query.find();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (list.size() == 1)
            {
                ParseObject object = list.get(0);
                String h = object.getString(Type);
                object.put(RequestThem,n.getThem());
                if (h.equals(RequestType.HelpRequest))
                {
                    FindRequest helprequest = (FindRequest) n;
                    object.put(RequestComment, helprequest.getRequestComment());
                    object.put(FindType, helprequest.getFindType());
                    object.put(OfflineVisibility, helprequest.isOffLineWatched());
                    object.put(HasData, helprequest.hasDate());
                    object.put(Data, helprequest.getLastData());
                    object.put(HasConcretePlace, helprequest.isHasConcretePlace());
                }
                object.saveInBackground();
            }
        }
    public static boolean deleteRequest(int idUser)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ClassName);
        query.whereEqualTo(UserId, idUser);
        List<ParseObject> list = new ArrayList<>();
        try {
            list = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (list.size() == 1)
        {
            ParseObject parseObject = list.get(0);
            parseObject.deleteInBackground();
            return true;
        }
        return  false;
    }
    public static List<RequestInterface> getAllRequests()
    {
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery(ClassName);
            List<ParseObject> list = new ArrayList<>();
            try {
                list = query.find();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (list.size() != 0) {
                ArrayList<RequestInterface> aa = new ArrayList<>();
                Log.d("database", "before " + list.size());
                for (ParseObject object : list) {
                    String type = object.getString(Type);
                    RequestInterface requestInterface;
                    Log.d("database","id" + object.getInt(UserId));
                    Log.d("database","type" + type);
                    if (type.equals(RequestType.SosRequest)) {
                        requestInterface = new SosRequest(object.getString(RequestThem));
                        requestInterface.setUserId(""+object.getInt(UserId));
                        requestInterface.setRequestType(RequestType.SosRequest);
                        requestInterface.updateLocation(object.getString(Longitude), object.getString(Latitude));
                    } else {
                        Log.d("database","in");
                        requestInterface = new FindRequest(object);
                        Log.d("database",""+requestInterface.getClass());
                    }
                    aa.add(requestInterface);
                }
                Log.d("database", "after " + list.size());
                return aa;
            }
        }catch (Exception e)
        {

        }
        return new ArrayList<>();
    }
    public static void updateUserOnline(int id,boolean isOnline)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ClassName);
        query.whereEqualTo(UserId, id);
        List<ParseObject> list = new ArrayList<>();
        try {
            list = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (list.size() == 1) {
            ParseObject object = list.get(0);
            object.put(IsOnlineNow,isOnline);
            object.saveInBackground();
        }
    }
    public static RequestInterface getUserRequest(int id)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ClassName);
        query.whereEqualTo(UserId, id);
        List<ParseObject> list = new ArrayList<>();
        try {
            list = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (list.size() >= 1)
        {
            ParseObject object = list.get(0);
            String type = object.getString(Type);
            RequestInterface requestInterface;
            Log.d("database","id" + object.getInt(UserId));
            Log.d("database","type" + type);
            if (type.equals(RequestType.SosRequest)) {
                requestInterface = new SosRequest(object.getString(RequestThem));
                requestInterface.setUserId(""+object.getInt(UserId));
                requestInterface.setRequestType(RequestType.SosRequest);
                requestInterface.updateLocation(object.getString(Longitude), object.getString(Latitude));
            } else {
                Log.d("database","in");
                requestInterface = new FindRequest(object);
                Log.d("database",""+requestInterface.getClass());
            }
            return requestInterface;
        }
        return null;
    }

}
