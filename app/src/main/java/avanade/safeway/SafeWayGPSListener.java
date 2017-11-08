package avanade.safeway;

import android.location.Location;
import android.os.Bundle;

/**
 * Created by wkond on 08/11/2017.
 */

////////////GPS LISTENER
public class SafeWayGPSListener implements android.location.LocationListener {
    private boolean position_available;
    private Location current_location;

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            current_location = location;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String s) {
        position_available = true;
    }

    @Override
    public void onProviderDisabled(String s) {
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