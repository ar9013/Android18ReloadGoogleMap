package com.javaclass.anima.android18googlemapreload;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private WebView webview;
    private LocationManager lmgr;
     private MyLocationListener listener;
    private TextView tv;
      private UIHandler handler;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new UIHandler();
        tv = (TextView) findViewById(R.id.tv);

        lmgr = (LocationManager) getSystemService(LOCATION_SERVICE);

        webview = (WebView) findViewById(R.id.webview);
        listener = new MyLocationListener();
        initWebView();
    }

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            //Log.i("brad", "javascript:moveTo(" + lat + ", " + lng + ")");
            webview.loadUrl("javascript:moveTo(" + lat + ", " + lng + ")");

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }

    private class TaskTest extends TimerTask {
        @Override
        public void run() {
            handler.sendEmptyMessage(2);
        }
    }

    @Override
    public void finish() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lmgr.removeUpdates(listener);
        super.finish();
    }

    private void initWebView() {
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);

        webview.addJavascriptInterface(new BradJS(), "brad");
        webview.loadUrl("file:///android_asset/brad.html");
    }

    public class BradJS {
        @JavascriptInterface
        public void showNewPlace() {
            handler.sendEmptyMessage(2);
        }

        @JavascriptInterface
        public void showName(String data) {
            Log.i("brad", data);

            Message msg = new Message();
            Bundle b = new Bundle();
            b.putString("urname", data);
            msg.setData(b);
            handler.sendMessage(msg);
        }
    }

    private class UIHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 2) {
                Log.d("rural", "Load 123");
                webview.loadUrl("javascript:moveTo(21.945902, 120.790243)");
            } else {
                String urname = msg.getData().getString("urname");
                tv.setText(urname);
            }
        }
    }
    public void b1(View v) {
        webview.loadUrl("javascript:moveTo(21.945902, 120.790243)");
    }

}