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
import android.widget.TextView;
import org.w3c.dom.Text;

import java.util.Locale;

/**
 * Created by Bill on 8/28/14.
 */
public class HearingActivity extends Activity implements OnInitListener {
    private TextToSpeech tts;
    private Button boarding, gettingoff, traveling, emergency;
    private ImageView goBack;
    private TextView title;
    private int i;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hearing);
        MyProperties.getInstance().gtts = new TextToSpeech(getApplicationContext(), this);
        tts = MyProperties.getInstance().gtts;

        boarding = (Button)findViewById(R.id.Hboarding);
        gettingoff = (Button)findViewById(R.id.Hseating);
        traveling = (Button)findViewById(R.id.Htraveling);
        emergency = (Button)findViewById(R.id.Hemergency);
        goBack = (ImageView)findViewById(R.id.header1);
        title = (TextView)findViewById(R.id.select);

        title.setText(MyProperties.getInstance().getTitle());

        emergency.setOnClickListener(new View.OnClickListener() {
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
                    speakOut("emergency");
                    Intent intent = new Intent();
                    intent.putExtra("Type", "emergency");
                    intent.setClass(HearingActivity.this, emergencyActivity.class);
                    startActivity(intent);
                    // close current activity to avoid multiple activities existing
                    HearingActivity.this.finish();
                }
            }
        });

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
                    MyProperties.getInstance().titleStack.push("Boarding");

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
                    MyProperties.getInstance().titleStack.push("Travelling");

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
                    MyProperties.getInstance().titleStack.push("Getting Off");

                    Intent intent = new Intent();
                    intent.setClass(HearingActivity.this, boardingActivity.class);
                    MyProperties.getInstance().currentType = MyProperties.getInstance().gettingoff;
                    startActivity(intent);
                    // close current activity to avoid multiple activities existing
                    HearingActivity.this.finish();
                }

            }
        });


        emergency.setOnClickListener(new View.OnClickListener() {
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
                    speakOut("emergency");
                    Intent intent = new Intent();
                    intent.putExtra("Type", "emergency");
                    intent.setClass(HearingActivity.this, emergencyActivity.class);
                    startActivity(intent);
                    // close current activity to avoid multiple activities existing
                    HearingActivity.this.finish();
                }

            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyProperties.getInstance().titleStack.pop();

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