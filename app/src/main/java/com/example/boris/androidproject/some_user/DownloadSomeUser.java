package com.example.boris.androidproject.some_user;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.boris.androidproject.User;
import com.example.boris.androidproject.database.RequestDao;
import com.example.boris.androidproject.request.RequestInterface;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Boris on 21.04.2015.
 * процес для зчитування інформації про користувача на карті та інформації про його заявку
 */
public class DownloadSomeUser extends AsyncTask<Integer,Void,MapUser> {
    private ImageView img;
    private boolean small;
    private boolean downloadRequest;
    private final int smallSize = 50;
    private  int bigSize = 200;
    public DownloadSomeUser(ImageView a,boolean small,boolean downloadRequest)
    {
        img = a;
        this.small = small;
        this.downloadRequest = downloadRequest;
    }

    @Override
    protected MapUser doInBackground(Integer... params) {
        InputStream inputStream = null;
        MapUser user = null;
        try {
            String size;
            if (small) size = ""+smallSize;
            else size = ""+bigSize;
            String url = "https://api.vkontakte.ru/method/getProfiles?uid=" + params[0] +"&fields=photo_"+size;
            Bitmap mIcon11 = null;
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            System.out.println("err");
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
            Log.d("token","it works1");            // receive response as inputStream

            inputStream = httpResponse.getEntity().getContent();
            String result = "";
            Log.d("token","it really works");
            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
            if (!result.contains("photo"))
            {
                bigSize = 100;
                if (small) size = ""+smallSize;
                else size = ""+bigSize;
                url = "https://api.vkontakte.ru/method/getProfiles?uid=" + params[0] +"&fields=photo_"+size;
                httpclient = new DefaultHttpClient();

                // make GET request to the given URL
                System.out.println("err");
                httpResponse = httpclient.execute(new HttpGet(url));
                Log.d("token","it works1");            // receive response as inputStream

                inputStream = httpResponse.getEntity().getContent();
                result = "";
                Log.d("token","it really works");
                // convert inputstream to string
                if(inputStream != null)
                    result = convertInputStreamToString(inputStream);
            }
            Log.d("token", result);
            user = initializeUser(result);
            InputStream in = null;
            try {
                in = new java.net.URL(user.getPhotoUrl()).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                in.close();
                if (small) user.setSmallBitmap(mIcon11);
                else user.setBigBitmap(mIcon11);
                Log.d("DownloadUser","downloaded2 "+user.getId());
            } catch (Exception e) {
                Log.e("Error33", e.getMessage());
                in.close();
                e.printStackTrace();
            }
            if (downloadRequest) {
                Log.d("DownloadUser","downloaded1"+user.getId());
                RequestInterface requestInterface = RequestDao.getUserRequest(params[0]);
                user.setRequestInterface(requestInterface);
                Log.d("DownloadUser","downloaded "+user.getId());
            }
        }
        catch (Exception e)
        {
            Log.e("DownloadUser!!!!!!","Exception"+e.toString());
        }
        return user;
    }
    public MapUser initializeUser(String parsing)
    {
        String photoName = "";
        boolean hasphoto = parsing.contains("photo");
        int id = Integer.parseInt(parsing.split("\"uid\":")[1].split(",")[0]);
        String firstName = parsing.split("\"first_name\":\"")[1].split("\",")[0];
        String lastName = parsing.split("\"last_name\":\"")[1].split("\"")[0];
        String size;
        if (small) size = ""+smallSize;
        else size = bigSize+"";
        if (hasphoto) photoName = parsing.split("photo_"+size+"\":\"")[1].split("\"")[0];
        Log.d("token",""+id);
        Log.d("token",""+firstName);
        Log.d("token",""+lastName);
        if (hasphoto) Log.d("vkphoto",photoName);
        if(hasphoto) {
            StringBuilder builder = new StringBuilder(photoName);
            while (builder.indexOf("\\") >= 0) {
                builder.deleteCharAt(builder.indexOf("\\"));
                Log.d("imageprof", "" + builder.toString());
            }
            photoName = builder.toString();
        }
        return (new MapUser(firstName,lastName,id,photoName));
    }
    public String convertInputStreamToString(InputStream inputStream) throws IOException {
        String result = "";
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))){
            String line = "";
            while ((line = bufferedReader.readLine()) != null)
                result += line;
            Log.d("Ura",result);
        }catch (IOException e) {

        }
        finally {
            inputStream.close();
        }
        return result;
    }

    @Override
    protected void onPostExecute(MapUser mapUser) {
    //    Log.d("UserMapView",""+mapUser.getPhotoUrl());
      //  Log.d("UserMapView",""+mapUser.getSmallBitmap().getHeight());
     //   if (small) img.setImageBitmap(mapUser.getSmallBitmap());
     //   else img.setImageBitmap(mapUser.getBigBitmap());
        super.onPostExecute(mapUser);
    }
}
