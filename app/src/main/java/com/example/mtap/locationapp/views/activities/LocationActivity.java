package com.example.mtap.locationapp.views.activities;

import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.mtap.locationapp.MainActivity;
import com.example.mtap.locationapp.R;
import com.example.mtap.locationapp.adapter.LocationAdapter;
import com.example.mtap.locationapp.database.DatabaseHelper;
import com.example.mtap.locationapp.interfaces.IRefreshLocationListListener;
import com.example.mtap.locationapp.provider.LocationsProvider;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends AppCompatActivity implements
        IRefreshLocationListListener,android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private static int REQUEST_CHECK_SETTINGS = 1;
    private LocationCallback mLocationCallback;
    private boolean mRequestingLocationUpdates;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    //Recyclerview items...
    private List<com.example.mtap.locationapp.model.Location> locationList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LocationAdapter mAdapter;

    private int LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Do something for Marshamallow and above versions\
            if (ContextCompat.checkSelfPermission(LocationActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                checkLocationPermission();
            } else {
                initializeLocation();
            }
        } else {
            initializeLocation();
        }


        DatabaseHelper db = new DatabaseHelper(this);
        locationList = db.getAllLocations();


       setAdapter();
    //    getSupportLoaderManager().initLoader(LOADER_ID, null, this);

    }

    private void setAdapter() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        if (locationList != null ) {
            mAdapter = new LocationAdapter(locationList,null);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager
                    (getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
        }
    }

    private void initializeLocation() {
        initializeFusedLocation();
        initializeLocationRequest();
        locationCallBack();
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    private void locationCallBack() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...

                    onLocationChanged(location);
                }
            }
        };
    }

    private void initializeLocationRequest() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());


        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                mRequestingLocationUpdates = true;
                createLocationRequest();
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(LocationActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    private void initializeFusedLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        try {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                onLocationChanged(location);
                            }
                        }
                    });
        } catch (SecurityException sException) {

        }
    }


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(LocationActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location")
                        .setMessage("Please Provide Location Permission")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(LocationActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
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

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        initializeLocation();
                        //Request location updates:
                        // locationManager.requestLocationUpdates(provider, 400, 1, this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }


    protected void createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000 * 3);
        mLocationRequest.setFastestInterval(5000 * 6);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            // do work here
                            onLocationChanged(locationResult.getLastLocation());
                        }
                    },
                    Looper.myLooper());
        } catch (SecurityException exception) {

        }
    }

    public void onLocationChanged(Location location) {
        // New location has now been determined

        String lat = String.valueOf(location.getLatitude());
        String lng = String.valueOf(location.getLongitude());
        com.example.mtap.locationapp.model.Location location1 = new com.example.mtap.locationapp.model.Location();
        String msg = "Updated Location: " +
                lat + "," +
                lng;
        // Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        //  LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        location1.setLat(lat);
        location1.setLng(lng);
        DatabaseHelper db = new DatabaseHelper(LocationActivity.this);
        long check = db.insertLocation(location1);
        if (check >= 1.0) {
           // getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
             Toast.makeText(this, "Location inserted successfully ", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
       /* if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }*/
    }

    private void startLocationUpdates() {
        try {
            if (mFusedLocationClient != null) {
                mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                        mLocationCallback,
                        null /* Looper */);
            }
        } catch (SecurityException excption) {

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    public void onInsertionSuccess(com.example.mtap.locationapp.model.Location location) {
        locationList.add(0, location);

       /* mAdapter.notifyItemInserted(0);
        mAdapter.notifyItemRangeChanged(0, locationList.size());*/
        //mAdapter.notifyItemChanged(0);

      //  mAdapter.notifyDataSetChanged(); actual code
       // Toast.makeText(LocationActivity.this, "refreshed", Toast.LENGTH_SHORT).show();
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new CursorLoader(LocationActivity.this, LocationsProvider.urlForItems(0), null, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
