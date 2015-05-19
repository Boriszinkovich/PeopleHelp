package com.example.boris.androidproject.authorization;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.boris.androidproject.User;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Boris on 04.03.2015.
 *
 * DownloadProfile - клас, що зчитує інформацію користувача з серверу ВК і записує в файл
 * @author Boris
 */
public class DownloadProfile extends AsyncTask<Void,Void,String> {
    /**
     *the url of request to VK server
     */
    private String url;
    /**
     * the name of file
     */
    private  String FileUrl = "profile.txt";
    private String photoName;
    public static String ProfileImage = Environment.getExternalStorageDirectory() +"/androidproject_files/"+"profile3.jpg";
       private Context context;
    /**
     *
     * @param url - the url of request to VK server
     */
    public DownloadProfile(String url, Context context)
    {
        this.context = context;
        this.url = url;
    }

    /**
     *
     * @param inputStream the stream with the data which must be converted to String
     * @return String with the profile data
     * @throws IOException
     */
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

    /**
     *
     * @param parsing String with the profile data
     */
    public void initializeUser(String parsing)
    {
        int id = Integer.parseInt(parsing.split("\"uid\":")[1].split(",")[0]);
        String firstName = parsing.split("\"first_name\":\"")[1].split("\",")[0];
        String lastName = parsing.split("\"last_name\":\"")[1].split("\"")[0];
        photoName = parsing.split("photo_200\":\"")[1].split("\"")[0];
        Log.d("token",""+id);
        Log.d("token",""+firstName);
        Log.d("token",""+lastName);
        Log.d("vkphoto",photoName);
        User.setAllProfile(firstName, lastName, id);
    }
    /**
     * main method of AsyncTask
     */
    @Override
    protected String doInBackground(Void... params) {
        InputStream inputStream = null;
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            Log.d("token", url);
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
            Log.d("token", result);
            initializeUser(result);
            writeToFile();
            Log.d("imageprof","before");
            StringBuilder builder = new StringBuilder(photoName);
            while(builder.indexOf("\\")>=0) {
                builder.deleteCharAt(builder.indexOf("\\"));
                Log.d("imageprof",""+builder.toString());
            }
            photoName = builder.toString();
            Log.d("imageprof","after");
            file_download(photoName);
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return "made";
    }

    /**
     * the method which write data to file
     */
    private void writeToFile()
    {
        File file = new File(Environment.getExternalStorageDirectory()+File.separator+FileUrl);
        boolean flag = false;
        try {
            flag =file.createNewFile(); // creating a file
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("Ura","notcreated3"+flag);
        /**
         * writing data to file
         */
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            Log.d("Ura", "created file " + flag);
            //  FileWriter writer = new FileWriter(FileUrl);
              writer.write(""+User.id);
            writer.newLine();
            writer.write(User.lastName);
            writer.newLine();
            writer.write(User.name);
        }catch(IOException e)
        {
            Log.d("Ura","fuck");
        }
    }
    public void file_download(String uRl) {

        File direct = new File(Environment.getExternalStorageDirectory()
                + "/androidproject_files");

        if (!direct.exists()) {
            direct.mkdirs();
        }
        Log.d("imageprof",uRl);
        DownloadManager mgr = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(uRl);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);

        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false).setTitle("Demo")
                .setDescription("Something useful. No, really.")
                .setDestinationInExternalPublicDir("/androidproject_files", "profile3.jpg");

        mgr.enqueue(request);

    }


}
