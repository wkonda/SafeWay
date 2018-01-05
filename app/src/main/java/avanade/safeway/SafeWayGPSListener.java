package avanade.safeway;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


////////////GPS LISTENER
public class SafeWayGPSListener implements android.location.LocationListener {
    private static final String TAG = "SafeWayGPSListener";
    //private boolean position_available;
    private Location current_location;
    private Context context;

    SafeWayGPSListener(String provider, Context context) {
        Log.i(TAG, "LocationListener " + provider);
        this.context = context;
        current_location = new Location(provider);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            current_location = location;
            Log.i(TAG, "onLocationChanged: " + location);
            sendCurrentLocation();
        }

    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.e(TAG, "onStatusChanged: " + provider);
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.e(TAG, "onProviderEnabled: " + s);
        //position_available = true;
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.e(TAG, "onProviderDisabled: " + s);
        //position_available = false;
    }

    //public boolean isPosition_available() { return position_available;}

    //Location getLocation() {return current_location;}

    private void sendCurrentLocation() {
        Intent intent = new Intent("com.avanade.safeway.safe_way_gps_position");
        intent.putExtra("location", current_location.toString());

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
///////////////END GPS LISTENER