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
public class boardingActivity extends Activity implements OnInitListener {
    private TextToSpeech tts;
    private Button generalInfo, tripInfo, safety, comfort;
    private ImageView emergency, goback;
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
                    intent.setClass(boardingActivity.this, gInfoActivity.class);
                    startActivity(intent);
                    // close current activity to avoid multiple activities existing
                    boardingActivity.this.finish();
                }
            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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