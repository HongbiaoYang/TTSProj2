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

import java.util.Locale;

/**
 * Created by Bill on 8/28/14.
 */
public class boardingActivity extends Activity implements OnInitListener {
    private TextToSpeech tts;
    private Button generalInfo, tripInfo, safety, comfort;
    private ImageView emergency, goback;
    private TextView title;
    private int i;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boardinglayout);
        MyProperties.getInstance().gtts = new TextToSpeech(getApplicationContext(), this);
        tts = MyProperties.getInstance().gtts;

        generalInfo = (Button)findViewById(R.id.HgeneralInfo);
        tripInfo = (Button)findViewById(R.id.Htripinfo);
        safety = (Button)findViewById(R.id.Hsafety);
        comfort = (Button)findViewById(R.id.Hcomfort);
        emergency = (ImageView)findViewById(R.id.footer2);
        goback = (ImageView)findViewById(R.id.header1);
        title = (TextView) findViewById(R.id.select);

        title.setText(MyProperties.getInstance().getTitle());

        generalInfo.setOnClickListener(new View.OnClickListener() {
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
                    speakOut("general information");
                    MyProperties.getInstance().titleStack.push("General Information");

                    Intent intent = new Intent();
                    intent.putExtra("Type", "general");
                    intent.setClass(boardingActivity.this, gInfoActivity.class);
                    startActivity(intent);
                    // close current activity to avoid multiple activities existing
                    boardingActivity.this.finish();
                }
            }
        });

        tripInfo.setOnClickListener(new View.OnClickListener() {
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
                    speakOut("trip information");
                    MyProperties.getInstance().titleStack.push("Trip Information");

                    Intent intent = new Intent();
                    intent.putExtra("Type", "trip");
                    intent.setClass(boardingActivity.this, gInfoActivity.class);
                    startActivity(intent);
                    // close current activity to avoid multiple activities existing
                    boardingActivity.this.finish();
                }
            }
        });

        safety.setOnClickListener(new View.OnClickListener() {
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
                    speakOut("safety");
                    MyProperties.getInstance().titleStack.push("Safety");

                    Intent intent = new Intent();
                    intent.putExtra("Type", "safety");
                    intent.setClass(boardingActivity.this, gInfoActivity.class);
                    startActivity(intent);
                    // close current activity to avoid multiple activities existing
                    boardingActivity.this.finish();
                }
            }
        });

        comfort.setOnClickListener(new View.OnClickListener() {
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
                    speakOut("comfort");
                    MyProperties.getInstance().titleStack.push("Comfort");

                    Intent intent = new Intent();
                    intent.putExtra("Type", "comfort");
                    intent.setClass(boardingActivity.this, gInfoActivity.class);
                    startActivity(intent);
                    // close current activity to avoid multiple activities existing
                    boardingActivity.this.finish();
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
                    intent.setClass(boardingActivity.this, emergencyActivity.class);
                    startActivity(intent);
                    // close current activity to avoid multiple activities existing
                    boardingActivity.this.finish();
                }
            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyProperties.getInstance().titleStack.pop();

                Intent intent = new Intent();
                intent.setClass(boardingActivity.this, HearingActivity.class);
                startActivity(intent);
                // close current activity to avoid multiple activities existing
                boardingActivity.this.finish();
            }
        });
    }

    private void speakOut(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void onInit(int status) {

    }

}