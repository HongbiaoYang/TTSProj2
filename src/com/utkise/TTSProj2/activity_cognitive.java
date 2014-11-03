package com.utkise.TTSProj2;

import android.app.Activity;
import android.content.Intent;
import android.opengl.Visibility;
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
public class activity_cognitive extends Activity implements OnInitListener {
    private TextToSpeech tts;
    private int count = CONSTANT.START;
    private ImageView lastLevelBtn, emergency;
    private TextView title;
    private Button yes, no, response;



    ListView list;
    String[] web;
    Integer[] imageId;
    LANG lan;


    private List<ItemStruct> thisLevel;
    private Stack<List<ItemStruct>> levelStack;
    private LinearLayout head_banner;
    private boolean EmergencyState;
    private Integer[] colors;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_display);
        MyProperties.getInstance().gtts = new TextToSpeech(getApplicationContext(), this);
        tts = MyProperties.getInstance().gtts;
        levelStack = new Stack<List<ItemStruct>>();

        lastLevelBtn = (ImageView) findViewById(R.id.header1);
        title = (TextView) findViewById(R.id.header2);
        emergency = (ImageView)findViewById(R.id.header3);
        head_banner = (LinearLayout)findViewById(R.id.head_banner);
        yes = (Button)findViewById(R.id.footer1);
        no  = (Button)findViewById(R.id.footer2);
        response = (Button)findViewById(R.id.footer3);


        title.setText(MyProperties.getInstance().getTitleStack());
        MyProperties.getInstance().playAnimation();

        lastLevelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               goUpOrBack();
            }
        });
        yes.setOnClickListener(new onHintListener("to say yes"));
        no.setOnClickListener(new onHintListener("to say no"));
        response.setOnClickListener(new onHintListener("to enter response page"));
        emergency.setOnClickListener(new onHintListener("to enter emergency page"));

        yes.setOnLongClickListener(new onHoldSpeakListener("yes"));
        no.setOnLongClickListener(new onHoldSpeakListener("no"));

        emergency.setBackgroundResource(0);
        emergency.setImageResource(R.drawable.emergency_icon);
        emergency.setOnLongClickListener(new OnEmergencyListener());
        response.setOnLongClickListener(new OnResponseListener());

        String type = getIntent().getStringExtra("Type");

        thisLevel = MyProperties.getInstance().getCognitiveList();

        updateList(thisLevel);
    }

    private void goUpOrBack() {

        if (EmergencyState == true) {
            EmergencyState = false;
            emergency.setVisibility(View.VISIBLE);
            response.setVisibility(View.VISIBLE);
            thisLevel = MyProperties.getInstance().getCognitiveList();
            updateList(thisLevel);
            title.setText(MyProperties.getInstance().getTitleStack());
            return;
        }

        if (levelStack.isEmpty()) {
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
        colors = lf.produceColorArray();

        adapter = new
                CustomList(activity_cognitive.this, web, imageId, R.layout.fatlist, colors);
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


    private class OnEmergencyListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {
            thisLevel = MyProperties.getInstance().emergency.getEmergency();
            updateList(thisLevel);

            title.setText("Emergency!!!");
            MyProperties.getInstance().speakout("Emergency");
            emergency.setVisibility(View.INVISIBLE);
            response.setVisibility(View.INVISIBLE);

            EmergencyState = true;

            return true;
        }

    }

    private class onHintListener implements View.OnClickListener {
        private final String hint;

        public onHintListener(String s) {
            this.hint = s;
        }

        @Override
        public void onClick(View v) {
            MyProperties.getInstance().speakout("Please hold this button " + hint);
        }
    }

    private class onHoldSpeakListener implements View.OnLongClickListener {
        private final String text;

        public onHoldSpeakListener(String content) {
            this.text = content;
        }


        @Override
        public boolean onLongClick(View v) {
            MyProperties.getInstance().speakout(text);
            return true;
        }
    }

    private class OnResponseListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {

            thisLevel = MyProperties.getInstance().response.getSystemResponse();
            updateList(thisLevel);

            title.setText("Response!!!");
            MyProperties.getInstance().speakout("Response");
            emergency.setVisibility(View.INVISIBLE);
            response.setVisibility(View.INVISIBLE);

            EmergencyState = true;

            return true;
        }
    }
}