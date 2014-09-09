package com.utkise.TTSProj2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

/**
 * Created by Bill on 8/28/14.
 */
public class gInfoActivity extends Activity implements OnInitListener {
    private TextToSpeech tts;
    private int i;
    private ImageView lastLevelBtn;
    private ImageView emergency;


    ListView list;
    String[] web;
    Integer[] imageId;


    private List<ItemStruct> thisLevel;
    private Stack<List<ItemStruct>> levelStack;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ginfolayout);
        MyProperties.getInstance().gtts = new TextToSpeech(getApplicationContext(), this);
        tts = MyProperties.getInstance().gtts;
        levelStack = new Stack<List<ItemStruct>>();

        lastLevelBtn = (ImageView) findViewById(R.id.header1);
        emergency = (ImageView) findViewById(R.id.footer2);

        lastLevelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (levelStack.isEmpty()) {
                    Intent intent = new Intent();
                    intent.setClass(gInfoActivity.this, boardingActivity.class);
                    startActivity(intent);
                    // close current activity to avoid multiple activities existing
                    gInfoActivity.this.finish();
                }  else {
                    thisLevel = levelStack.pop();
                    updateList(thisLevel);
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

                    levelStack.clear();
                    thisLevel = MyProperties.getInstance().currentType.getEmergency();
                    updateList(thisLevel);
                }
            }
        });

        String type = getIntent().getStringExtra("Type");

        thisLevel = MyProperties.getInstance().currentType.getInformation(type);

        updateList(thisLevel);
    }

    private void updateList(List<ItemStruct> level) {
        ListFactory lf = new ListFactory(level);
        CustomList adapter;
        web =  lf.produceTitleArray();
        imageId = lf.produceImageArray();

        adapter = new
                CustomList(gInfoActivity.this, web, imageId);

        list = (ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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

                    ItemStruct item = thisLevel.get(position);
                    if (item.child == null) {
                        speakOut(item.text);
                    } else {
                        speakOut(item.text);
                        levelStack.push(thisLevel);
                        thisLevel = item.child;
                        updateList(thisLevel);
                    }
                }

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