package com.utkise.TTSProj2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Stack;

/**
 * Created by Bill on 8/28/14.
 */
public class activity_cognitive extends Activity implements OnInitListener {
    private TextToSpeech tts;
    private int count = CONSTANT.START;
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

        lastLevelBtn = (ImageView) findViewById(R.id.header1);
        title = (TextView) findViewById(R.id.header2);

        title.setText(MyProperties.getInstance().getTitleStack());
        MyProperties.getInstance().playAnimation();

        lastLevelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               goUpOrBack();
            }
        });

        String type = getIntent().getStringExtra("Type");

        thisLevel = MyProperties.getInstance().getCognitiveList();

        updateList(thisLevel);
    }

    private void goUpOrBack() {
        if (levelStack.isEmpty()) {
            MyProperties.getInstance().popStacks();
            goBackToMain();
        } else {
            thisLevel = levelStack.pop();
            updateList(thisLevel);
        }
    }

    private void updateList(List<ItemStruct> level) {
        ListFactory lf = new ListFactory(level);
        CustomList adapter;
        web =  lf.produceTitleArray();
        imageId = lf.produceImageArray();

        adapter = new
                CustomList(activity_cognitive.this, web, imageId, R.layout.fatlist);
        adapter.setColors(true);

        list = (ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ItemStruct item = thisLevel.get(position);
                if (item.getChild() == null) {
                    MyProperties.getInstance().speakBoth(item);
                } else {
                    MyProperties.getInstance().speakBoth(item);
                    levelStack.push(thisLevel);
                    thisLevel = item.getChild();
                    updateList(thisLevel);
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        goUpOrBack();

    }

    private void goBackToMain() {
        MyProperties.getInstance().doInit(LANG.ENGLISH);
        MyProperties.getInstance().popStacks();

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(activity_cognitive.this, activity_main.class);
        startActivity(intent);
    }


    @Override
    public void onInit(int status) {
    }

}