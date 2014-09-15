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

public class activity_vision extends Activity implements OnInitListener{
    private Button left, right, ok;
    private QueryList queryList;
    private int count = 0;
    private ImageView goBack;
    private TextView title;


    @Override
    public void onBackPressed() {
        MyProperties.getInstance().titleStack.pop();
        finish();
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_vision);

        left  = (Button)findViewById(R.id.Left);
        right = (Button)findViewById(R.id.Right);
        ok    = (Button)findViewById(R.id.Ok);
        goBack = (ImageView)findViewById(R.id.header1);
        title  = (TextView)findViewById(R.id.header2);

        queryList = new QueryList();
        queryList.AddItem(new QueryItem(1, "Boarding", "How can I board this bus", getResources().getDrawable(R.drawable.boarding)));
        queryList.AddItem(new QueryItem(2, "Travelling" , "I want Traveling", getResources().getDrawable(R.drawable.travelling)));
        queryList.AddItem(new QueryItem(3, "Getting off", "I want to get off", getResources().getDrawable(R.drawable.gettingoff)));


        title.setText(MyProperties.getInstance().getTitle());
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyProperties.getInstance().titleStack.pop();

                Intent intent = new Intent();
                intent.setClass(activity_vision.this, activity_main.class);
                startActivity(intent);
            }
        });


        // default
        QueryItem item = queryList.getCurrent();
        ok.setBackground(item.getqImg());
        ok.setText(item.getqTitle());

        // left button listener
        left.setOnClickListener(new View.OnClickListener() {
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
                    if (queryList.currentPos() == 0) {
                     MyProperties.getInstance().speakout("The Beginning");
                    } else {
                        QueryItem item = queryList.getPrevious();
                        ok.setBackground(item.getqImg());
                        ok.setText(item.getqTitle());
                        speakOut(item.getqTitle());
                    }
                }
            }
        });

        // right button listener
        right.setOnClickListener(new View.OnClickListener() {
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

                    if (queryList.currentPos() >= queryList.getSize() - 1) {
                        MyProperties.getInstance().speakout("The end");
                    } else {
                        QueryItem item = queryList.getNext();
                        ok.setBackground(item.getqImg());
                        ok.setText(item.getqTitle());
                        speakOut(item.getqTitle());
                    }
                }
            }
        });

        ok.setOnClickListener(new doubleTapListener(queryList.getCurrent().getqText()));

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void speakOut(String text) {
        MyProperties.getInstance().gtts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onInit(int status) {

    }
}
