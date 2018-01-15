package avanade.safeway;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;



public class TCPGPSService extends Service {
    private static final String TAG = "TCPGPSService";
    int location_delay_ms;
    int profile_code;
    SafeWayGPSListener locationListener;
    private LocationManager mLocationManager = null;
    @Override
    public IBinder onBind(Intent arg) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "Starting service");
        super.onStartCommand(intent, flags, startId);
        location_delay_ms = 1000 * intent.getExtras().getInt("delay");
        profile_code = intent.getExtras().getInt("profile_code");
        Log.e(TAG, "delay" + location_delay_ms + profile_code);

        initializeLocationManager();
        locationListener = new SafeWayGPSListener(LocationManager.GPS_PROVIDER, this, profile_code);

        try {
            mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            if (mLocationManager != null)
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, location_delay_ms, 0, locationListener);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");


    }


    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mLocationManager.removeUpdates(locationListener);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listener, ignore", ex);
                }

        }
    }

    private void initializeLocationManager() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }


}