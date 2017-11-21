package avanade.safeway;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by wkond on 08/11/2017.
 */

////////////GPS LISTENER
public class SafeWayGPSListener implements android.location.LocationListener {
    private static final String TAG = "SafeWayGPSListener";
    //Location mLastLocation;
    private boolean position_available;
    private Location current_location;

    public SafeWayGPSListener(String provider) {
        Log.e(TAG, "LocationListener " + provider);
        current_location = new Location(provider);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            current_location = location;
            Log.e(TAG, "onLocationChanged: " + location);
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.e(TAG, "onStatusChanged: " + provider);
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.e(TAG, "onProviderEnabled: " + s);
        position_available = true;
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.e(TAG, "onProviderDisabled: " + s);
        position_available = false;
    }

    public boolean isPosition_available() {
        return position_available;
    }

    public Location getLocation() {
        return current_location;
    }
}
///////////////END GPS LISTENER