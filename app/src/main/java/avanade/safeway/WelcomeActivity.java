package avanade.safeway;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {
    private static int MY_PERMISSIONS_LOCATION = 1;
    private String position = "fddhgfsqerg";

    public void onCheckboxClicked(View view) {
        CheckBox checkbox = (CheckBox) view;
        TextView text_logs = (TextView) findViewById(R.id.text_logs);
        Log.e("abc", "abc");
        if (checkbox.isChecked()) {
            text_logs.setText(position);
        } else {
            text_logs.setText("");
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
                    MY_PERMISSIONS_LOCATION);
        }


        ///////SEEKBAR
        SeekBar bar_choice_delay = (SeekBar) findViewById(R.id.seekBar_choice_delay);
        final TextView text_delay = (TextView) findViewById(R.id.textView_choice_delay);
        text_delay.setText(getString(R.string.choice_delay) + Integer.toString(bar_choice_delay.getProgress()) + "sec.");
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
                position = Integer.toString(number);

            }
        };
        buttons_profil[0] = (Button) findViewById(R.id.button_pedestrian);
        buttons_profil[1] = (Button) findViewById(R.id.button_cyclist);
        buttons_profil[2] = (Button) findViewById(R.id.button_driver);
        buttons_profil[0].setOnClickListener(button_listener);
        buttons_profil[1].setOnClickListener(button_listener);
        buttons_profil[2].setOnClickListener(button_listener);
        //////ENDBUTTONS


        ////////////////SERVICE///////////////////////
        stopService(new Intent(this, TCPGPSService.class));
        startService(new Intent(this, TCPGPSService.class));
        Log.e("starting service", "dd");
        /////////ENDSERVICE
    }


    public enum Profile {
        PEDESTRIAN, CYCLIST, DRIVER
    }
}

