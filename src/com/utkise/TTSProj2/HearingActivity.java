package com.utkise.TTSProj2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Locale;

/**
 * Created by Bill on 8/28/14.
 */
public class HearingActivity extends Activity implements OnInitListener {
    private TextToSpeech tts;
    private Button boarding, gettingoff, traveling;
    private ImageView goBack, emergency;
    private int i;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hearing);
        MyProperties.getInstance().gtts = new TextToSpeech(getApplicationContext(), this);
        tts = MyProperties.getInstance().gtts;

        boarding = (Button)findViewById(R.id.Hboarding);
        gettingoff = (Button)findViewById(R.id.Hseating);
        traveling = (Button)findViewById(R.id.Htraveling);
        emergency = (ImageView)findViewById(R.id.footer2);
        goBack = (ImageView)findViewById(R.id.header1);

        gettingoff.setOnClickListener(new doubleTapListenner("seating"));
        emergency.setOnClickListener(new doubleTapListenner("emergency"));

        boarding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                Handler handler = new Handler();
                Runnable run = new Runnable() {
                    @Override
                    public void run() {
                        i = 0;
                    }
                };

                if (i == 1) {
                    handler.postDelayed(run, 250);
                } else if (i == 2) {
                    i = 0;
                    speakOut("boarding");
                    Intent intent = new Intent();
                    MyProperties.getInstance().currentType = MyProperties.getInstance().boarding;
                    intent.setClass(HearingActivity.this, boardingActivity.class);
                    startActivity(intent);
                    // close current activity to avoid multiple activities existing
                    HearingActivity.this.finish();
                }

            }
        });

        traveling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                Handler handler = new Handler();
                Runnable run = new Runnable() {
                    @Override
                    public void run() {
                        i = 0;
                    }
                };

                if (i == 1) {
                    handler.postDelayed(run, 250);
                } else if (i == 2) {
                    i = 0;
                    speakOut("traveling");
                    Intent intent = new Intent();
                    intent.setClass(HearingActivity.this, boardingActivity.class);
                    MyProperties.getInstance().currentType = MyProperties.getInstance().traveling;
                    startActivity(intent);
                    // close current activity to avoid multiple activities existing
                    HearingActivity.this.finish();
                }

            }
        });

        gettingoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                Handler handler = new Handler();
                Runnable run = new Runnable() {
                    @Override
                    public void run() {
                        i = 0;
                    }
                };

                if (i == 1) {
                    handler.postDelayed(run, 250);
                } else if (i == 2) {
                    i = 0;
                    speakOut("getting off");
                    Intent intent = new Intent();
                    intent.setClass(HearingActivity.this, boardingActivity.class);
                    MyProperties.getInstance().currentType = MyProperties.getInstance().gettingoff;
                    startActivity(intent);
                    // close current activity to avoid multiple activities existing
                    HearingActivity.this.finish();
                }

            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(HearingActivity.this, Main.class);
                startActivity(intent);
                // close current activity to avoid multiple activities existing
                HearingActivity.this.finish();

            }
        });



    }

    private void speakOut(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }


    @Override
    public void onInit(int status) {
    }

}