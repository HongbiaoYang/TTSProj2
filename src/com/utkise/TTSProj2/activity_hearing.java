package com.utkise.TTSProj2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Bill on 8/28/14.
 */
public class activity_hearing extends Activity implements OnInitListener {
    private TextToSpeech tts;
    private Button boarding, gettingoff, traveling, emergency;
    private ImageView goBack;
    private TextView title;
    private int count = CONSTANT.START;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_hearing);
        MyProperties.getInstance().gtts = new TextToSpeech(getApplicationContext(), this);
        tts = MyProperties.getInstance().gtts;

        boarding = (Button)findViewById(R.id.Hboarding);
        gettingoff = (Button)findViewById(R.id.Hgettingoff);
        traveling = (Button)findViewById(R.id.Htraveling);
        emergency = (Button)findViewById(R.id.Hemergency);
        goBack = (ImageView)findViewById(R.id.header1);

        if (MyProperties.getInstance().Language == LANG.SPANISH) {
            boarding.setBackgroundResource(R.drawable.boarding_s);
            gettingoff.setBackgroundResource(R.drawable.gettingoff_s);
            traveling.setBackgroundResource(R.drawable.travelling_s);
            emergency.setBackgroundResource(R.drawable.emergency_s);
        } else {
            boarding.setBackgroundResource(R.drawable.boarding);
            gettingoff.setBackgroundResource(R.drawable.gettingoff);
            traveling.setBackgroundResource(R.drawable.travelling);
            emergency.setBackgroundResource(R.drawable.emergency);
        }


        title = (TextView)findViewById(R.id.header2);

        title.setText(MyProperties.getInstance().getTitle());

        emergency.setOnClickListener(new View.OnClickListener() {
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
                    handler.postDelayed(run, 250);
                } else if (count == CONSTANT.END) {
                    count = CONSTANT.START;

                    String emergency_str = MyProperties.getInstance().getTitleName(TITLE.EMERGENCY);
                    speakOut(emergency_str);
                    MyProperties.getInstance().titleStack.push(emergency_str);

                    Intent intent = new Intent();
                    intent.putExtra("Type", "emergency");
                    intent.setClass(activity_hearing.this, activity_emergency.class);
                    startActivity(intent);
                }
            }
        });

        boarding.setOnClickListener(new View.OnClickListener() {
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
                    handler.postDelayed(run, 250);
                } else if (count == CONSTANT.END) {
                    count = CONSTANT.START;


                    String board_str = MyProperties.getInstance().getTitleName(TITLE.BOARDING);
                    speakOut(board_str);
                    MyProperties.getInstance().titleStack.push(board_str);

                    Intent intent = new Intent();
                    MyProperties.getInstance().currentType = MyProperties.getInstance().boarding;
                    intent.setClass(activity_hearing.this, activity_boarding.class);
                    startActivity(intent);
                }

            }
        });

        traveling.setOnClickListener(new View.OnClickListener() {
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
                    handler.postDelayed(run, 250);
                } else if (count == CONSTANT.END) {
                    count = CONSTANT.START;

                    String travel_str = MyProperties.getInstance().getTitleName(TITLE.TRAVELLING);
                    speakOut(travel_str);
                    MyProperties.getInstance().titleStack.push(travel_str);

                    Intent intent = new Intent();
                    intent.setClass(activity_hearing.this, activity_boarding.class);
                    MyProperties.getInstance().currentType = MyProperties.getInstance().traveling;
                    startActivity(intent);
                }

            }
        });

        gettingoff.setOnClickListener(new View.OnClickListener() {
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
                    handler.postDelayed(run, 250);
                } else if (count == CONSTANT.END) {
                    count = CONSTANT.START;


                    String getoff_str = MyProperties.getInstance().getTitleName(TITLE.GETTING_OFF);
                    speakOut(getoff_str);
                    MyProperties.getInstance().titleStack.push(getoff_str);

                    Intent intent = new Intent();
                    intent.setClass(activity_hearing.this, activity_boarding.class);
                    MyProperties.getInstance().currentType = MyProperties.getInstance().gettingoff;
                    startActivity(intent);
                }

            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // change language back to english
               MyProperties.getInstance().doInit(LANG.ENGLISH);
               MyProperties.getInstance().titleStack.pop();
               finish();

            }
        });



    }

    private void speakOut(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }


    @Override
    public void onInit(int status) {
    }

    @Override
    public void onBackPressed() {
        MyProperties.getInstance().doInit(LANG.ENGLISH);
        MyProperties.getInstance().titleStack.pop();
        finish();
    }
}