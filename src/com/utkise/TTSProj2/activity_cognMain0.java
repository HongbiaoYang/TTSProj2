package com.utkise.TTSProj2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Bill on 10/17/14.
 */
public class activity_cognMain0 extends Activity {
    private ImageView tutorial;
    private Button skip, start;
    private SharedPreferences pref;
    private boolean loop;
    private String tutorialText;
    private String tutorialWelcome;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cognmain0);

        tutorial = (ImageView)findViewById(R.id.tutorial);
        skip = (Button)findViewById(R.id.skipTutorial);
        start = (Button)findViewById(R.id.startTutorial);

        // first animation in main
        ImageView image = (ImageView) findViewById(R.id.frame_home);
        image.setBackgroundResource(R.drawable.frame);
        MyProperties.getInstance().animStack.push((AnimationDrawable) image.getBackground());

        // pref for cognitive
        pref = this.getSharedPreferences("com.utkise.TTSProj2", Context.MODE_PRIVATE);

        tutorial.setBackgroundResource(R.drawable.tutorialc);

        start.setText(R.string.tapStartTutorial);
        skip.setText(R.string.tapSkipTutorial);

        start.setBackgroundResource(R.drawable.taptutorial);
        skip.setBackgroundResource(R.drawable.taptutorial);

        tutorialWelcome = getResources().getString(R.string.welcomeTutorialCognitive);
        tutorialText  = getResources().getString(R.string.startTutorialCognitive);

        loop = true;
        // for the first time, always speak
        speakRepeated(true);

        skip.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                pref.edit().putBoolean("tutorial_cognitive", true).apply();
                gotoCognitivePage();
                return false;
            }
        });


        start.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                pref.edit().putBoolean("tutorial_cognitive", false).apply();
                gotoCognitivePage();
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loop = true;
        speakRepeated(true);
    }

    private void speakRepeated(boolean delay) {

        // if is speaking, do not disturb and let the current one talk
        if ((MyProperties.getInstance().gtts.isSpeaking() == true || loop == false) && (delay == false)){
            return;
        }

        MyProperties.getInstance().speakAdd(tutorialWelcome);
        MyProperties.getInstance().speakSilent(200);
        MyProperties.getInstance().speakAdd(tutorialText);


        Handler handler = new Handler();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                speakRepeated(false);
            }
        };

        if (loop) {
            handler.postDelayed(run, 15000);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        loop = false;

        // shut up
        MyProperties.getInstance().shutup();
    }

    @Override
    public void onBackPressed() {

        MyProperties.getInstance().popStacks();
        finish();
    }

    private void gotoCognitivePage() {
            MyProperties.getInstance().speakBoth(TITLE.COGNITIVE);
            String cog_str = MyProperties.getInstance().getTitleName(TITLE.COGNITIVE);
            MyProperties.getInstance().titleStack.push(cog_str);

            Intent intent = new Intent();
            intent.setClass(activity_cognMain0.this, activity_cognitive0.class);
            startActivity(intent);
    }
}