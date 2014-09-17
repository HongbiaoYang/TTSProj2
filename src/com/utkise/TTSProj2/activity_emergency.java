package com.utkise.TTSProj2;

import android.app.Activity;
import android.content.Intent;
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
public class activity_emergency extends Activity implements OnInitListener {
    private TextToSpeech tts;
    private int count = CONSTANT.START;
    private ImageView lastLevelBtn;
    private ImageView emergency;
    private TextView title;


    ListView list;
    String[] web;
    Integer[] imageId;


    private List<ItemStruct> thisLevel;
    private Stack<List<ItemStruct>> levelStack;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_emergency);

        levelStack = new Stack<List<ItemStruct>>();

        lastLevelBtn = (ImageView) findViewById(R.id.header1);
        title = (TextView) findViewById(R.id.header2);

        title.setText("Emergency");

        lastLevelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (levelStack.isEmpty()) {
                    MyProperties.getInstance().titleStack.clear();
                    finish();
                } else {
                    thisLevel = levelStack.pop();
                    updateList(thisLevel);
                }
            }
        });


        String type = getIntent().getStringExtra("Type");

        thisLevel = MyProperties.getInstance().emergency.getInformation(type);

        updateList(thisLevel);
    }

    private void updateList(List<ItemStruct> level) {
        ListFactory lf = new ListFactory(level);
        CustomList adapter;
        web =  lf.produceTitleArray();
        imageId = lf.produceImageArray();

        adapter = new
                CustomList(activity_emergency.this, web, imageId);

        list = (ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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

                    ItemStruct item = thisLevel.get(position);
                    LANG lan = MyProperties.getInstance().Language;

                    if (item.getChild() == null) {

                        MyProperties.getInstance().speakBoth(item);
                    } else {
                        MyProperties.getInstance().speakBoth(item);
                        levelStack.push(thisLevel);
                        thisLevel = item.getChild();
                        updateList(thisLevel);
                    }
                }

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