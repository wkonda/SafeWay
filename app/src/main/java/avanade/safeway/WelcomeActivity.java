package avanade.safeway;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Objects;

public class WelcomeActivity extends AppCompatActivity {
    //////////BROADCAST
    public static final String action = "com.avanade.safeway.safe_way_gps_position";
    String position = "";
    boolean show_logs = false;
    //public static final String action = "safe_way_gps_position";
    //public final String action=" ";//getString(R.string.broadcast_action_name);
    private BroadcastReceiver bReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("Activity", "Enter Broadcast Receiver => " + action);
            if ((Objects.equals(intent.getAction(), action))) {
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

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(bReceiver, new IntentFilter(action));
    }
    //LocalBroadcastManager bManager;
    ////////END BROADCAST

    public void onCheckboxClicked(View view) {
        CheckBox checkbox = (CheckBox) view;
        TextView text_logs = findViewById(R.id.text_logs);
        Log.e("abc", "abc");
        if (checkbox.isChecked()) {
            text_logs.setText(position);
            show_logs = true;
        } else {
            text_logs.setText("");
            show_logs = false;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }


        ///////SEEKBAR
        SeekBar bar_choice_delay = findViewById(R.id.seekBar_choice_delay);
        final TextView text_delay = findViewById(R.id.textView_choice_delay);
        String seekbar_text = getString(R.string.choice_delay) + Integer.toString(bar_choice_delay.getProgress()) + "sec.";
        text_delay.setText(seekbar_text);
        bar_choice_delay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private String text;

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                text = getString(R.string.choice_delay);
                if (progress == 0)
                    text += getString(R.string.never);
                else
                    text = text + Integer.toString(progress) + "sec.";
                text_delay.setText(text);
            }
        });
        ///////ENDSEEKBAR
        ///////BOUTTONS
        final Button[] buttons_profil = new Button[3];
        View.OnClickListener button_listener = new View.OnClickListener() {
            public void onClick(View v) {
                int number = Integer.parseInt((String) v.getTag());
                for (int i = 0; i < 3; ++i) {
                    if (i == number)
                        buttons_profil[i].setBackgroundColor(Color.GREEN);
                    else
                        buttons_profil[i].setBackgroundColor(Color.RED);
                }
                //////Initialisation de ligne de texte position
                //position = Integer.toString(number);

            }
        };
        buttons_profil[0] = findViewById(R.id.button_pedestrian);
        buttons_profil[1] = findViewById(R.id.button_cyclist);
        buttons_profil[2] = findViewById(R.id.button_driver);
        buttons_profil[0].setOnClickListener(button_listener);
        buttons_profil[1].setOnClickListener(button_listener);
        buttons_profil[2].setOnClickListener(button_listener);
        //////ENDBUTTONS


        ////////////////SERVICE///////////////////////
        stopService(new Intent(this, TCPGPSService.class));
        Log.e("stopping service", "ddd");
        startService(new Intent(this, TCPGPSService.class));
        Log.e("starting service", "dd");
        /////////ENDSERVICE


        /*///////////BRODCAST////////////
        bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(action);
        bManager.registerReceiver(bReceiver, intentFilter);
        //////////ENDBRODCAST///////////////*/

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(bReceiver);
    }


    /*public enum Profile {
        PEDESTRIAN, CYCLIST, DRIVER
    }*/
}

