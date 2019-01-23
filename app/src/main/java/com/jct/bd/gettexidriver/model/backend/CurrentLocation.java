package com.jct.bd.gettexidriver.model.backend;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.jct.bd.gettexidriver.controller.fragments.availableRiedsFragment;
import com.jct.bd.gettexidriver.model.entities.MyLocation;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.jct.bd.gettexidriver.controller.fragments.availableRiedsFragment.listAdapter;
/**
 * <h1>funcs on the location funcs</h1></h1>
 * The CurrentLocation is class that is responsibility to all the funcs and listeners
 of that works with location.
 * @author  David Elkayam and Nath Ascoli
 * @version 1.0
 * @since   2019-01-23
 */
public class CurrentLocation {
    public static Context context;
    boolean isGPSEnabled;
    boolean isNetworkEnabled;
    LocationManager locationManager;
    LocationListener locationListener;
    public static MyLocation locationA = new MyLocation();
    /**
     * This is the constructor of CurrentLocation
     * @param context This is the context to run funcs that you must you on context
     */
    public CurrentLocation(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                if (isGPSEnabled) {
                    listAdapter.notifyDataSetChanged();
                    locationA.set(location);
                }
                if (isNetworkEnabled) {
                    locationA.set(location);
                    listAdapter.notifyDataSetChanged();
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
    }
    /**
     * This method is used to get location and convert it to name of location
     on this method you get only the name of the city;
     *@return the string that he the name of the city of the location
     */
    public static String getPlace(Location location) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (addresses.size() > 0) {
                String cityName = addresses.get(0).getAddressLine(0);
                return cityName;
            }

            return "no place: \n (" + location.getLongitude() + " , " + location.getLatitude() + ")";
        } catch (
                IOException e)

        {
            e.printStackTrace();
        }
        return "IOException ...";
    }
    /**
     * This method is used to find the current location of the phone right now
     when he find the location he put it on LocationA that he static field .
     */
    public void getLocation() {
        int locationOff = 0;
        try {
            //gets the status mode of the location's settings in the device
            locationOff = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        //if the GPS in the the device is off
        if (locationOff == 0) {
            //Moves to the device's location settings and asks to enable the GPS
            Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            Toast.makeText(context, "please turn on your gps location", Toast.LENGTH_SHORT).show();
            context.startActivity(onGPS);
        } else {
            try {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
            }else{
                isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                if (!isGPSEnabled && !isNetworkEnabled) {
                    Toast.makeText(context, "Please Set Network/GPS ON", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isNetworkEnabled)
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                if (isGPSEnabled)
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
            } catch(Exception e){
                Toast.makeText(context, "Error: " + e, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
