package com.example.lab2app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class GeoActivity extends AppCompatActivity {

    private TextView geo_message;
    private TextView geo_content;
    private FusedLocationProviderClient fusedLocationClient;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    long EndTime, UpdateTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo);

        geo_message = findViewById(R.id.geo_message);
        geo_content = findViewById(R.id.geo_content);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        handler = new Handler();
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("I need your location")
                        .setMessage("All your base will belong to us.")
                        .setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(GeoActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    set_location();
                } else {
                    show_unknown();
                    EndTime = SystemClock.uptimeMillis() + 10_000;
                    handler.postDelayed(runnable, 0);
                }
                return;
            }

        }
    }

    private void show_unknown() {
        geo_message.setText(R.string.permission_denied);
        geo_content.setText("");
    }

    private void set_location() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                geo_message.setText(R.string.location_received);
                                geo_content.setText(String.format("Longitude: %s\nLatitude: %s\nTime: %s",
                                        location.getLongitude(), location.getLatitude(), location.getTime()));
                            }
                        }
                    });
        }
    }

    public void updateLocation(View view) {
        if (checkLocationPermission()) {
            set_location();
        }
    }

    Handler handler;
    protected Runnable runnable = new Runnable() {
        public void run() {
            long milliseconds = EndTime - SystemClock.uptimeMillis();
            int seconds = (int) milliseconds / 1000;
            milliseconds = (int) (milliseconds % 1000);

            geo_content.setText(String.format("%d.%d", seconds, milliseconds));
            if (seconds <= 0) {
                Intent k = new Intent(GeoActivity.this, BoomActivity.class);
                startActivity(k);
            } else {
                handler.postDelayed(this, 0);
            }
        }
    };
}
