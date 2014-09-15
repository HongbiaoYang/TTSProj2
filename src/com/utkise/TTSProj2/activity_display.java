package com.utkise.TTSProj2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.widget.*;

import java.util.List;
import java.util.Stack;

/**
 * Created by Bill on 8/28/14.
 */
public class activity_display extends Activity implements OnInitListener {
    private TextToSpeech tts;
    private int i;
    private ImageView lastLevelBtn;
    private TextView title;


    ListView list;
    String[] web;
    Integer[] imageId;
    LANG lan;


    private List<ItemStruct> thisLevel;
    private Stack<List<ItemStruct>> levelStack;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_display);
        MyProperties.getInstance().gtts = new TextToSpeech(getApplicationContext(), this);
        tts = MyProperties.getInstance().gtts;
        levelStack = new Stack<List<ItemStruct>>();

        // the setting language
        lan = MyProperties.getInstance().Language;

        lastLevelBtn = (ImageView) findViewById(R.id.header1);
        title = (TextView) findViewById(R.id.header2);

        title.setText(MyProperties.getInstance().getTitle());

        lastLevelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (levelStack.isEmpty()) {
                    MyProperties.getInstance().titleStack.pop();
                    finish();
                } else {
                    thisLevel = levelStack.pop();
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
                CustomList(activity_display.this, web, imageId);

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
                    if (item.getChild() == null) {
                        speakOut(item.getText(lan));
                    } else {
                        speakOut(item.getText(lan));
                        levelStack.push(thisLevel);
                        thisLevel = item.getChild();
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
        MyProperties.getInstance().titleStack.pop();
        finish();
    }


    @Override
    public void onInit(int status) {
    }

}