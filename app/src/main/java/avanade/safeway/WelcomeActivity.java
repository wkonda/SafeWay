package avanade.safeway;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    public void onCheckboxClicked(View view) {
        CheckBox checkbox=(CheckBox)view;
        TextView text_logs= (TextView) findViewById(R.id.text_logs);
        if (checkbox.isChecked()){
            text_logs.setText(getString(R.string.never));
        }else{
            text_logs.setText(getString(R.string.app_name));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ///////SEEKBAR
        SeekBar bar_choice_delay=(SeekBar) findViewById( R.id.seekBar_choice_delay );
        final TextView text_delay = (TextView) findViewById(R.id.textView_choice_delay);
        text_delay.setText(getString(R.string.choice_delay) + Integer.toString(bar_choice_delay.getProgress()) + "sec.");
        bar_choice_delay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private String text;
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                text=getString(R.string.choice_delay);
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
                for (int i = 0; i < 3; ++i)
                    if (i == number)
                        buttons_profil[i].setBackgroundColor(Color.GREEN);
                    else
                        buttons_profil[i].setBackgroundColor(Color.RED);
            }
        };
        buttons_profil[0] = (Button) findViewById(R.id.button_pedestrian);
        buttons_profil[1] = (Button) findViewById(R.id.button_cyclist);
        buttons_profil[2] = (Button) findViewById(R.id.button_driver);
        buttons_profil[0].setOnClickListener(button_listener);
        buttons_profil[1].setOnClickListener(button_listener);
        buttons_profil[2].setOnClickListener(button_listener);
        //////ENDBUTTONS
    }

}
