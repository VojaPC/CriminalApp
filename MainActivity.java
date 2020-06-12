package com.example.criminalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    TextView actionLabel, co1, co2, printNum;
    EditText numInput;
    Button button, loadButton;
    String location1;
    public int cnt1 = 0, cnt2 = 0;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    ///Za cuvanje brojeva
    String nums1, nums2, nums3, nums4, nums5;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT1 = "PhoneNum1";
    public static final String TEXT2 = "PhoneNum2";
    public static final String TEXT3 = "PhoneNum3";
    public static final String TEXT4 = "PhoneNum4";
    public static final String TEXT5 = "PhoneNum5";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TextViews declaration
        actionLabel = (TextView) findViewById(R.id.action3);
        co1 = (TextView) findViewById(R.id.action1);
        co2 = (TextView) findViewById(R.id.action2);
        numInput = (EditText) findViewById(R.id.inputNum);
        button = (Button) findViewById(R.id.SendNum);
        loadButton = (Button) findViewById(R.id.LoadNum);
        printNum = (TextView) findViewById(R.id.PrintSaveNum);
        //Button onClick declaration\

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });

        loadData();
        updateViews();
        //startService(new Intent(this,BackgroundService.class)); //PALIM BG SERVICE
    }

    public void loadData() {

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        /*
        sharedPreferences.edit().remove(TEXT1).commit();
        sharedPreferences.edit().remove(TEXT2).commit();
        sharedPreferences.edit().remove(TEXT3).commit();
        sharedPreferences.edit().remove(TEXT4).commit();
        sharedPreferences.edit().remove(TEXT5).commit();
        */
        nums1 = sharedPreferences.getString(TEXT1, null);
        nums2 = sharedPreferences.getString(TEXT2, null);
        nums3 = sharedPreferences.getString(TEXT3, null);
        nums4 = sharedPreferences.getString(TEXT4, null);
        nums5 = sharedPreferences.getString(TEXT5, null);

        updateViews();
    }

    public void updateViews() {
        printNum.setText(String.format("%s\n%s\n%s\n%s\n%s", nums1, nums2, nums3, nums4, nums5));
    }


    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (numInput.getText().toString().contains("clear")) {
            sharedPreferences.edit().remove(TEXT1).commit();
            sharedPreferences.edit().remove(TEXT2).commit();
            sharedPreferences.edit().remove(TEXT3).commit();
            sharedPreferences.edit().remove(TEXT4).commit();
            sharedPreferences.edit().remove(TEXT5).commit();
        } else if (nums1 == null) {
            editor.putString(TEXT1, numInput.getText().toString());
            //actionLabel.setText(nums1 + "1");
            editor.apply();
        } else if (nums2 == null) {
            editor.putString(TEXT2, numInput.getText().toString());
            //actionLabel.setText(nums2 + "2");
            editor.apply();
        } else if (nums3 == null) {
            editor.putString(TEXT3, numInput.getText().toString());
            //actionLabel.setText(nums3 + "3");
            editor.apply();
        } else if (nums4 == null) {
            editor.putString(TEXT4, numInput.getText().toString());
            //actionLabel.setText(nums4 + "4");
            editor.apply();
        } else if (nums5 == null) {
            editor.putString(TEXT5, numInput.getText().toString());
            //actionLabel.setText(nums5 + "5");
            editor.apply();
        } else actionLabel.setText("Pun je storage");


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //getCurrentLocation();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getCurrentLocation() {
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {

                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            double latitude =
                                    locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            double longitude =
                                    locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            location1 = "UZBUNA: Poslana hitna poruka sa lokacije:\nLati: " + latitude + " Long: " + longitude;
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1);
                            String messageText = location1;
                            SmsManager sm = SmsManager.getDefault();
                            sm.sendTextMessage(nums1, null, messageText, null, null);
                            sm.sendTextMessage(nums2, null, messageText, null, null);
                            sm.sendTextMessage(nums3, null, messageText, null, null);
                            sm.sendTextMessage(nums4, null, messageText, null, null);
                            sm.sendTextMessage(nums5, null, messageText, null, null);
                        }
                    }
                }, Looper.getMainLooper());
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
        {
            if(cnt2==0) cnt1++;
            else
            {
                cnt1=0;
                cnt2=0;
            }
            co1.setText(String.valueOf(cnt1));
            co2.setText(String.valueOf(cnt2));
            if(cnt1==2 && cnt2==2)
            {
                cnt1=0;
                cnt2=0;
                getCurrentLocation();
                return true;
            }
            return true;
        }
        else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            if(cnt1==2) cnt2++;
            else
            {
                cnt1=0;
                cnt2=0;
            }
            co1.setText(String.valueOf(cnt1));
            co2.setText(String.valueOf(cnt2));
            if(cnt1==2 && cnt2==2) {
                cnt1=0;
                cnt2=0;
                getCurrentLocation();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
