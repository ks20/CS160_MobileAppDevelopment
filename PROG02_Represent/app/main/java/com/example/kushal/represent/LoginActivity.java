package com.example.kushal.represent;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Random;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_FINE_LOCATION = 36;
    private static final double MIN_LAT = 25.00000;
    private static final double MAX_LAT = 45.99999;
    private static final double MIN_LONG = 71.00000;
    private static final double MAX_LONG = 115.99999;

    private AutoCompleteTextView zipCode;
    private View mProgressView;
    private View mLoginFormView;
    private Button currLocation, randomLocation;
    private ImageButton zipLocation;
    private int zipCodeVal;
    private FusedLocationProviderClient mFusedLocationClient;

    private LocationRequest mLocationRequest;

    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    private TextView mLatitudeText;
    private TextView mLongitudeText;
    private double latitude, longitude, randLatitude, randLongitude;

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        zipCode = (AutoCompleteTextView) findViewById(R.id.zipcode);

        mLatitudeText = (TextView) findViewById((R.id.latitude_text));
        mLongitudeText = (TextView) findViewById((R.id.longitude_text));

        startLocationUpdates();
        Random rand = new Random();
        randLatitude = MIN_LAT + (MAX_LAT - MIN_LAT) * rand.nextDouble();
        randLongitude = (MIN_LONG + (MAX_LONG - MIN_LONG) * rand.nextDouble()) * -1.0;

        //mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //checkPermission();
        //mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, (android.location.LocationListener) mLocationListener);

        currLocation = (Button) findViewById(R.id.currentLocation);
        currLocation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent currLocationIntent = new Intent(getApplicationContext(), MainActivity.class);
                currLocationIntent.putExtra("currLocationFlag", true);
                currLocationIntent.putExtra("zipLocationFlag", false);
                currLocationIntent.putExtra("randLocationFlag", false);
                currLocationIntent.putExtra("Latitude", latitude);
                currLocationIntent.putExtra("Longitude", longitude);
                startActivity(currLocationIntent);
            }
        });

        randomLocation = (Button) findViewById(R.id.randomLocation);
        randomLocation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent randLocationIntent = new Intent(getApplicationContext(), MainActivity.class);
                randLocationIntent.putExtra("currLocationFlag", false);
                randLocationIntent.putExtra("zipLocationFlag", false);
                randLocationIntent.putExtra("randLocationFlag", true);
                randLocationIntent.putExtra("Latitude", randLatitude);
                randLocationIntent.putExtra("Longitude", randLongitude);
                startActivity(randLocationIntent);
            }
        });

        zipLocation = (ImageButton) findViewById(R.id.sendButton);
        zipLocation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final String zipCodeString = zipCode.getText().toString();
                Intent zipIntent = new Intent(getApplicationContext(), MainActivity.class);
                zipIntent.putExtra("currLocationFlag", false);
                zipIntent.putExtra("zipLocationFlag", true);
                zipIntent.putExtra("randLocationFlag", false);
                zipIntent.putExtra("ZipCode", zipCodeString);
                startActivity(zipIntent);
            }
        });
    }

    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        checkPermission();
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }

    public void onLocationChanged(Location location) {
        //String msg = "Updated Location: " + Double.toString(location.getLatitude()) + "," + Double.toString(location.getLongitude());
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        //System.out.print("CAPTAIN AMERICAAAAAA");

        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    public void getLastLocation() {
        // Get last known recent location using new Google Play Services SDK (v11+)
        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(this);

        checkPermission();
        locationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // GPS location can be null if GPS is switched off
                        if (location != null) {
                            onLocationChanged(location);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {
                //Log.d("MapDemoActivity", "Error trying to get last GPS location");
                e.printStackTrace();
            }
        });
    }

    private boolean checkPermission() {
        /*if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions();
            return false;
        }*/
        return true;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
    }
}

