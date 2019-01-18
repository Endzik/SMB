package pl.edu.pja.s13227.smb.maps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MapUtils implements LocationListener {

    private Activity activity;

    public MapUtils(Activity activity) {
        this.activity = activity;
    }

    public Task<Location> currentPositionTask() {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        checkPermissions();
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1, 1, this);
            return LocationServices.getFusedLocationProviderClient(activity)
                    .getLastLocation();
        } catch (SecurityException e) {
            Log.e("PERMISSIONS", "Missing permissions to get location!", e);
        }
        return null;
    }

    public void zoomToCurrentPosition(final GoogleMap googleMap) {
        try {
            currentPositionTask().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(location.getLatitude(), location.getLongitude()), 16));
                }
            });

        } catch (SecurityException e) {
            Log.e("PERMISSIONS", "Missing permissions to get location!", e);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }
}
