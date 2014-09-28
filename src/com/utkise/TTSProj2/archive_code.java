/*
	
	
package com.utkise.TTSProj2;

import android.app.Activity;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class activity_vision extends Activity {
    private Button left, right, ok;
    private QueryList queryList;
    private int count = 0;
    private ImageView goBack;
    private TextView title;
    private boolean mIsDown;
    private float mPrevX, mPrevY;
    private String direction;

    @Override
    public void onBackPressed() {
        MyProperties.getInstance().titleStack.pop();
        finish();
    }

    /**
     * Called when the activity is first created.

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

        // default
        QueryItem item = queryList.getCurrent();
        ok.setBackground(item.getqImg());

      // ok.setText(item.getqTitle());
        ok.setOnClickListener(new doubleTapListener(queryList.getCurrent().getqText()));

    }

    private void detectSwipe(float dx, float dy) {

        // Log.i("gesture", "dx="+dx + " dy="+dy);
        if (Math.abs(dy) > Math.abs(dx)) {

            if (dy > 0) {
                direction = "down";
            }  else {
                direction = "up";
            }
        }  else {
            if (dx > 0) {
                direction = "right";
            }  else {
                direction = "left";
            }

        }


    }
    


        // left button listener
       /* left.setOnClickListener(new View.OnClickListener() {
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
                        // ok.setText(item.getqTitle());
                        MyProperties.getInstance().speakout(item.getqTitle());
                    }
                }
            }
        });*/

       /* right.setOnClickListener(new View.OnClickListener(){
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
                    // ok.setText(item.getqTitle());
                    speakOut(item.getqTitle());
                }
               }
            }
        });*/

        // right button listener
       /* right.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                float x = event.getX();
                float y = event.getY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mIsDown = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float dx = x - mPrevX;
                        float dy = y - mPrevY;
                        detectSwipe(dx, dy);
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i("gesture", "released");
                        Log.i("gesture", direction);
                        Toast.makeText(getApplicationContext(),direction,1).show();
                        MyProperties.getInstance().speakout(direction);

                        mIsDown = false;
                        break;
                }

                mPrevX = x;
                mPrevY = y;

                return true;
            }
        });




}

	
	*/