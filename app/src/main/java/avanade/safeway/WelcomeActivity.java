package avanade.safeway;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.CheckBox;

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

        SeekBar bar_choice_delay=(SeekBar) findViewById( R.id.seekBar_choice_delay );
        bar_choice_delay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private TextView text_delay=(TextView)findViewById(R.id.textView_choice_delay);
            private String text;

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                text=getString(R.string.choice_delay);
                if (progress==0) text+=getString(R.string.never);
                else text=text+Integer.toString(progress)+"sec.";
                text_delay.setText(text);
            }
        });



    }
}
