package avanade.safeway;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Calendar;



////////////GPS LISTENER
public class SafeWayGPSListener implements android.location.LocationListener {
    private static final String TAG = "SafeWayGPSListener";
    private Location current_location;
    private Context context;
    private int profile_code;
    private String profile_id;

    SafeWayGPSListener(String provider, Context context, int profile_code) {
        Log.i(TAG, "LocationListener " + provider);
        this.context = context;
        current_location = new Location(provider);
        this.profile_code = profile_code;
        this.profile_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            current_location = location;
            Log.i(TAG, "onLocationChanged: " + location);

            String data = formData();
            broadcastData(data);
            saveData(data);
            //sendHttpData(data);
            new HTTPClient().execute(data);
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


    private void broadcastData(String data) {
        Intent intent = new Intent(context.getString(R.string.broadcast_action_name));
        intent.putExtra("location", data);

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }


    private void saveData(String data) {
        File file = new File(Environment.getExternalStorageDirectory(), context.getString(R.string.logs_file_name));
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (Exception e) {
                Log.e("Listener", "File not created.");
            }
        try {
            FileOutputStream fs = new FileOutputStream(file, true);
            OutputStreamWriter sw = new OutputStreamWriter(fs);
            sw.append(data);
            sw.close();
            fs.flush();
            fs.close();
        } catch (Exception e) {
            Log.e("Listener", "File not saved: " + e.toString());
        }
    }


    private String formData() {
        String data = "";

        data += "profile=" + profile_code;
        data += "&id=" + profile_id;

        //data+=new Date();
        data += "&date=";
        Calendar calendar = Calendar.getInstance();
        data += calendar.get(Calendar.YEAR) + ",";
        data += calendar.get(Calendar.MONTH) + 1 + ",";
        data += calendar.get(Calendar.DAY_OF_MONTH) + ",";
        data += calendar.get(Calendar.HOUR) + ",";
        data += calendar.get(Calendar.MINUTE) + ",";
        data += calendar.get(Calendar.SECOND) + ",";


        data += "&lat=" + current_location.getLatitude();
        data += "&lon=" + current_location.getLongitude();
        return data;
    }

}
///////////////END GPS LISTENER