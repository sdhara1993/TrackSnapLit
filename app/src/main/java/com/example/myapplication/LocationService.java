package com.example.tracksnaplite;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;

import androidx.core.app.ActivityCompat;

public class LocationService extends Service {
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Handler handler = new Handler();
    private Runnable locationTask;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                KMLWriter.appendWaypoint(getApplicationContext(), lat, lon);
            }

            @Override public void onStatusChanged(String provider, int status, android.os.Bundle extras) {}
            @Override public void onProviderEnabled(String provider) {}
            @Override public void onProviderDisabled(String provider) {}
        };

        locationTask = new Runnable() {
            @Override
            public void run() {
                try {
                    if (ActivityCompat.checkSelfPermission(LocationService.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.postDelayed(this, 10000); // every 10 seconds
            }
        };

        handler.post(locationTask);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // Not used
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(locationTask);
        super.onDestroy();
    }
}
