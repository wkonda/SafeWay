package avanade.safeway;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Objects;

public class WelcomeActivity extends AppCompatActivity {
    String position = "";
    String broadcast_action = "";
    boolean show_logs = false;
    int delay = 1;
    int profile_code = -1;
    Intent service;
    private BroadcastReceiver bReceiver;


    private void restartService(Intent service) {
        stopService(service);
        service.putExtra("profile_code", profile_code);
        service.putExtra("delay", delay);
        startService(service);
    }

    public void onCheckboxClicked(View view) {
        CheckBox checkbox = (CheckBox) view;
        TextView text_logs = findViewById(R.id.text_logs);
        Log.e("Activity", "Checkbox clicked");
        if (checkbox.isChecked()) {
            text_logs.setText(position);
            show_logs = true;
        } else {
            text_logs.setText("");
            show_logs = false;
        }
    }

    /////////////////KEY_BUTTONS///////////////
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("Activity", "Back. Stopping service");
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
            stopService(service);
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            restartService(service);
        }
        return true;
    }
    ///////////////END_KEY_BUTTONS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //restore preferences
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        delay = sharedPref.getInt("delay", 1);
        profile_code = sharedPref.getInt("profile_code", -1);

        setContentView(R.layout.activity_welcome);

        if (ContextCompat.checkSelfPermission(this, //checking for permissions
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, //if not allowed => demand
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        else {
            service = new Intent(this, TCPGPSService.class);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);


        /////////SEEK_BAR//////
        final int[] progress2delay = {0, 1, 5, 15};
        SeekBar bar_choice_delay = findViewById(R.id.seekBar_choice_delay);
        bar_choice_delay.setProgress(Arrays.binarySearch(progress2delay, delay));
        final TextView text_delay = findViewById(R.id.textView_choice_delay);
        String seekBar_text = getString(R.string.choice_delay) + Integer.toString(delay) + "sec.";
        text_delay.setText(seekBar_text);
        bar_choice_delay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private String text;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                text = getString(R.string.choice_delay);
                if (progress == 0) {
                    text += getString(R.string.never);
                    delay = 0;
                    stopService(service);
                } else {
                    delay = progress2delay[progress];
                    text = text + Integer.toString(delay) + " sec.";
                    restartService(service);
                }
                text_delay.setText(text);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        ///////END_SEEK_BAR///////////

        ///////BUTTONS//////////////
        final Button[] buttons_profile = new Button[3];

        View.OnClickListener button_listener = new View.OnClickListener() {
            public void onClick(View v) {
                profile_code = Integer.parseInt((String) v.getTag());
                for (int i = 0; i < 3; ++i) {
                    if (i == profile_code)
                        buttons_profile[i].setBackgroundColor(Color.GREEN);
                    else
                        buttons_profile[i].setBackgroundColor(Color.RED);
                }
                Log.i("Activity", "profileChanged");
                restartService(service);
            }
        };
        buttons_profile[0] = findViewById(R.id.button_pedestrian);
        buttons_profile[1] = findViewById(R.id.button_cyclist);
        buttons_profile[2] = findViewById(R.id.button_driver);
        buttons_profile[0].setOnClickListener(button_listener);
        buttons_profile[1].setOnClickListener(button_listener);
        buttons_profile[2].setOnClickListener(button_listener);
        for (int i = 0; i < 3; ++i) {
            if (i == profile_code)
                buttons_profile[i].setBackgroundColor(Color.GREEN);
            else
                buttons_profile[i].setBackgroundColor(Color.RED);
        }
        //////END_BUTTONS/////////


        ////////////////SERVICE///////////////////////
        //stopService(new Intent(this, TCPGPSService.class));
        if (delay != 0) restartService(service);
        //startService(new Intent(this, TCPGPSService.class));
        /////////END_SERVICE//////////////////////////////


        ///////////BROADCAST////////////
        broadcast_action = getString(R.string.broadcast_action_name);
        bReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if ((Objects.equals(intent.getAction(), broadcast_action))) {
                    position = intent.getStringExtra("location");
                    if (position != null) {
                        Log.e("Activity", position);
                        if (show_logs) {
                            TextView text_logs = findViewById(R.id.text_logs);
                            text_logs.setText(position);
                        }
                    } else Log.e("Activity", "Position = NULL");
                }
            }
        };


        LocalBroadcastManager.getInstance(this).registerReceiver(bReceiver, new IntentFilter(broadcast_action));
        //////////END_BROADCAST///////////////




    }


    @Override
    public void onDestroy() {
        SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
        editor.putInt("delay", delay);
        editor.putInt("profile_code", profile_code);
        editor.apply();
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(bReceiver);
        stopService(service);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
        editor.putInt("delay", delay);
        editor.putInt("profile_code", profile_code);
        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        SafeWayGPSListener.resendData(getString(R.string.logs_file_name));

    }
    /*public enum Profile {
        PEDESTRIAN, CYCLIST, DRIVER
    }*/
}

