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
public class activity_boarding extends Activity implements OnInitListener {
    private TextToSpeech tts;
    private Button generalInfo, tripInfo, safety, comfort;
    private ImageView  goback;
    private TextView title;
    private int count;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_boarding);

        generalInfo = (Button)findViewById(R.id.HgeneralInfo);
        tripInfo = (Button)findViewById(R.id.Htripinfo);
        safety = (Button)findViewById(R.id.Hsafety);
        comfort = (Button)findViewById(R.id.Hcomfort);
        goback = (ImageView)findViewById(R.id.header1);
        title = (TextView) findViewById(R.id.header2);

        if (MyProperties.getInstance().Language == LANG.SPANISH) {
            generalInfo.setBackgroundResource(R.drawable.generalinfo_s);
            tripInfo.setBackgroundResource(R.drawable.tripinfo_s);
            safety.setBackgroundResource(R.drawable.safety_s);
            comfort.setBackgroundResource(R.drawable.comfort_s);
        } else {
            generalInfo.setBackgroundResource(R.drawable.generalinfo);
            tripInfo.setBackgroundResource(R.drawable.tripinfo);
            safety.setBackgroundResource(R.drawable.safety);
            comfort.setBackgroundResource(R.drawable.comfort);
        }


        title.setText(MyProperties.getInstance().getTitleStack());

        generalInfo.setOnClickListener(new View.OnClickListener() {
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

                    MyProperties.getInstance().speakBoth(TITLE.GENERAL_INFORMATION);

                    String g_info = MyProperties.getInstance().getTitleName(TITLE.GENERAL_INFORMATION);
                    MyProperties.getInstance().titleStack.push(g_info);

                    Intent intent = new Intent();
                    intent.putExtra("Type", "general");
                    intent.setClass(activity_boarding.this, activity_display.class);
                    startActivity(intent);
                }
            }
        });

        tripInfo.setOnClickListener(new View.OnClickListener() {
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

                    MyProperties.getInstance().speakBoth(TITLE.TRIP_INFORMATION);

                    String t_info = MyProperties.getInstance().getTitleName(TITLE.TRIP_INFORMATION);
                    MyProperties.getInstance().titleStack.push(t_info);

                    Intent intent = new Intent();
                    intent.putExtra("Type", "trip");
                    intent.setClass(activity_boarding.this, activity_display.class);
                    startActivity(intent);
                }
            }
        });

        safety.setOnClickListener(new View.OnClickListener() {
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


                    MyProperties.getInstance().speakBoth(TITLE.SAFETY);
                    String safe_str = MyProperties.getInstance().getTitleName(TITLE.SAFETY);
                    MyProperties.getInstance().titleStack.push(safe_str);

                    Intent intent = new Intent();
                    intent.putExtra("Type", "safety");
                    intent.setClass(activity_boarding.this, activity_display.class);
                    startActivity(intent);
                }
            }
        });

        comfort.setOnClickListener(new View.OnClickListener() {
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

                    MyProperties.getInstance().speakBoth(TITLE.COMFORT);

                    String comfort_str = MyProperties.getInstance().getTitleName(TITLE.COMFORT);
                    MyProperties.getInstance().titleStack.push(comfort_str);

                    Intent intent = new Intent();
                    intent.putExtra("Type", "comfort");
                    intent.setClass(activity_boarding.this, activity_display.class);
                    startActivity(intent);
                }
            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyProperties.getInstance().titleStack.pop();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        MyProperties.getInstance().titleStack.pop();
        finish();
    }

    @Override
    public void onInit(int status) {

    }

}