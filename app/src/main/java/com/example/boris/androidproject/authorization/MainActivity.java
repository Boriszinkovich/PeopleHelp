package com.example.boris.androidproject.authorization;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.example.boris.androidproject.ProfileActivity;
import com.example.boris.androidproject.R;
import com.example.boris.androidproject.User;
/**
 * клас, з формою авторизації Вконтакте
 */

public class MainActivity extends ActionBarActivity {

    private WebView enterVk;
    private String accessToken;
    private String idVK;
    private static final String PREFS_NAME = "MyPrefs";
    private Boolean imgOn;
    private static final String VKURL = "https://oauth.vk.com/authorize?" + "client_id=4789277&" + "scope=offline&" + "redirect_uri=https://oauth.vk.com/blank.html&" + "display=mobile&" + "v=5.28&" + "response_type=token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        enterVk = (WebView) findViewById(R.id.webView);
        enterVk.clearCache(true);
        WebSettings webSettings = enterVk.getSettings();
        webSettings.setSaveFormData(true);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        imgOn = settings.getBoolean("IMGMODE", false);
        webSettings.setLoadsImagesAutomatically(imgOn);


        enterVk.setWebViewClient(new WebViewClient() {


            public void onPageFinished(WebView view, String url) {

                if (enterVk.getUrl().contains("access_token=")){
                  accessToken = enterVk.getUrl().split("access_token=")[1].split("&expires_in")[0];
                //Toast.makeText(getApplicationContext(),(enterVk.getUrl().toString()).split("user_id=")[1],Toast.LENGTH_LONG).show();
                idVK = enterVk.getUrl().toString().split("user_id=")[1];
                try {
                    inStart2();
                } catch (Exception e) {
                    System.out.println("Exeption one");
                }}
                //enterVk.clearCache(true);
            }

            public void inStart2() throws Exception {
               // Log.d("token", "1step");

             //   String url = "https://api.vkontakte.ru/method/getProfiles?uids=138189309&fields=name";

             // Thread a = new Thread(new GettingProfile("https://api.vkontakte.ru/method/getProfiles?uids=138189309&fields=name",MainActivity.this));
               // a.start();
                    User.setTokenAndId(accessToken, idVK);
                User.startAuthorization();
                Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                startActivity(intent);
                onStop();
            }
        });

        Log.d("token","fuck");
        enterVk.loadUrl(VKURL);
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


}

