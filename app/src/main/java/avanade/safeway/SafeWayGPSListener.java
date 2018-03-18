package avanade.safeway;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;



////////////GPS LISTENER
public class SafeWayGPSListener implements android.location.LocationListener {
    private static final String TAG = "SafeWayGPSListener";
    private Location current_location;
    private Context context;
    private int profile_code;
    private String profile_id;
    private Calendar calendar;

    SafeWayGPSListener(String provider, Context context, int profile_code) {
        Log.i(TAG, "LocationListener " + provider);
        this.context = context;
        //current_location = new Location(provider);
        this.profile_code = profile_code;
        this.profile_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        //this.profile_id= UUID.randomUUID().toString();
        //this.profile_id = InstanceID.getInstance(context).getId();

        calendar = Calendar.getInstance();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    static void saveData(String file_name, String data) {
        File file = new File(Environment.getExternalStorageDirectory(), file_name);
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

    static void resendData(String file_name) {
        FileInputStream is;
        BufferedReader reader;
        String line;
        File file = new File(Environment.getExternalStorageDirectory(), file_name);
        File temp_file = new File(file.getAbsolutePath() + ".tmp");
        if (file.exists()) {
            try {
                file.renameTo(temp_file);
                is = new FileInputStream(temp_file);
                reader = new BufferedReader(new InputStreamReader(is));

                while ((line = reader.readLine()) != null) {
                    // trim newline when comparing with lineToRemove
                    //String trimmedLine = line.trim();
                    Log.i("sending : ", line);
                    new HTTPClient().execute(file_name, line);
                }

                is.close();
                reader.close();
                temp_file.delete();

            } catch (java.io.IOException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            current_location = location;
            Log.i(TAG, "onLocationChanged: " + location);

            String data = formData();
            broadcastData(data);
            //saveData(context.getString(R.string.logs_file_name),data);
            new HTTPClient().execute(context.getString(R.string.logs_file_name), data);
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

    private String formData() {
        String data = "";

        data += "profile=" + profile_code;
        data += "&id=" + profile_id;

        calendar.setTimeInMillis(current_location.getTime());
        data += "&date=";
        data += calendar.get(Calendar.YEAR) + "-";
        data += calendar.get(Calendar.MONTH) + 1 + "-";
        data += calendar.get(Calendar.DAY_OF_MONTH) + " ";
        data += calendar.get(Calendar.HOUR_OF_DAY) + ":";
        data += calendar.get(Calendar.MINUTE) + ":";
        data += calendar.get(Calendar.SECOND);

        data += "&lat=" + current_location.getLatitude();
        data += "&lon=" + current_location.getLongitude();

        data += "&acc=" + current_location.getAccuracy();
        data += "&cap=" + current_location.getBearing();
        data += "&spe=" + current_location.getSpeed();
        return data;
    }

}
///////////////END GPS LISTENER