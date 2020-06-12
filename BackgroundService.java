package com.example.criminalapp;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class BackgroundService extends Service {
    private static final String TAG = "BackgroundService";
    MainActivity ma = new MainActivity();
    String location1;
    int cnt1=0, cnt2=0;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    ///Za cuvanje brojeva
    String nums1,nums2,nums3,nums4,nums5;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT1 = "PhoneNum1";
    public static final String TEXT2 = "PhoneNum2";
    public static final String TEXT3 = "PhoneNum3";
    public static final String TEXT4 = "PhoneNum4";
    public static final String TEXT5 = "PhoneNum5";


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        registerReceiver(broadcastReceiver, filter);

        return START_STICKY;
    }

    int volumePrev = 0;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if ("android.media.VOLUME_CHANGED_ACTION".equals(intent.getAction())) {

                int volume = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_VALUE",0);

                Log.i(TAG, "volume = " + volume);

                if (volumePrev  < volume) {
                    //Log.i(TAG, "You have pressed volume up button");
                    volumePrev = volume;
                    if(cnt1==2) cnt2++;
                    else
                    {
                        cnt1=0;
                        cnt2=0;
                    }
                    //co1.setText(String.valueOf(cnt1));
                    //co2.setText(String.valueOf(cnt2));
                    if(cnt1==2 && cnt2==2) {
                        cnt1=0;
                        cnt2=0;
                        ma.getCurrentLocation();
                        //return;
                    }
                } else if(volumePrev > volume){
                    volumePrev = volume;
                    Log.i(TAG, "You have pressed volume down button");
                    if(cnt2==0) cnt1++;
                    else
                    {
                        cnt1=0;
                        cnt2=0;
                    }
                    //co1.setText(String.valueOf(cnt1));
                    //co2.setText(String.valueOf(cnt2));
                    if(cnt1==2 && cnt2==2)
                    {
                        cnt1=0;
                        cnt2=0;
                        ma.getCurrentLocation();
                        //return;
                    }
                    //return;
                }

            }
        }
    };

}
