package com.utkise.TTSProj2;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;

/**
 * Created by Bill on 10/20/14.
 */
public class debug_activity_click extends Activity {

    private Button click, ok;
    private ImageView image;
    private TextView text;
    private EditText edit;
    private int timeInteval = 250;
    private int count;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.debug_layout_click);

        click = (Button)findViewById(R.id.debug_click);
        ok = (Button)findViewById(R.id.debug_ok);
        image = (ImageView)findViewById(R.id.debug_image);
        text = (TextView)findViewById(R.id.debug_text);
        edit  = (EditText)findViewById(R.id.debug_edit);



        text.setText("Current Time value: "+timeInteval+"ms");
        image.setImageResource(R.drawable.picture45);
        image.setOnClickListener(new debugOnClickListener());

        ok.setText("Confirm");
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeInteval = Integer.parseInt(edit.getText().toString());
                text.setText("Current Time value: "+timeInteval+"ms");
            }
        });

        click.setText("Double Click Me!");
        click.setOnClickListener(new debugOnClickListener());
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private class debugOnClickListener implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                count++;
                Handler handler = new Handler();
                Runnable run = new Runnable() {
                    @Override
                    public void run() {
                        count = CONSTANT.START;
                    }
                };

                if (count == CONSTANT.MIDDLE) {
                    handler.postDelayed(run, timeInteval);
                } else if (count == CONSTANT.END) {
                    count = CONSTANT.START;

                    Toast.makeText(getApplicationContext(), "Click Succeed!", Toast.LENGTH_SHORT). show();
                    MyProperties.getInstance().speakout("Good Job!");
                }
            }


    }
}