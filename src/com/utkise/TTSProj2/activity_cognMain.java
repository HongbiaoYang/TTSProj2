package com.utkise.TTSProj2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Bill on 10/17/14.
 */
public class activity_cognMain extends Activity {
    private ImageView tutorial;
    private Button skip, start;
    private SharedPreferences pref;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cognmain);

        tutorial = (ImageView)findViewById(R.id.tutorial);
        skip = (Button)findViewById(R.id.skipTutorial);
        start = (Button)findViewById(R.id.startTutorial);

        // pref for cognitive
        pref = this.getSharedPreferences("com.utkise.TTSProj2", Context.MODE_PRIVATE);

        tutorial.setBackgroundResource(R.drawable.tutorialc);

        start.setText(R.string.tapStartTutorial);
        skip.setText(R.string.tapSkipTutorial);

        start.setBackgroundResource(R.drawable.taptutorial);
        skip.setBackgroundResource(R.drawable.taptutorial);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // skip tutorial
                pref.edit().putBoolean("tutorial_cognitive", true).apply();
                gotoCognitivePage();
            }
        });


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start tutorial
                pref.edit().putBoolean("tutorial_cognitive", false).apply();
                gotoCognitivePage();
            }
        });
    }

    private void gotoCognitivePage() {
            MyProperties.getInstance().speakBoth(TITLE.COGNITIVE);
            String cog_str = MyProperties.getInstance().getTitleName(TITLE.COGNITIVE);
            MyProperties.getInstance().titleStack.push(cog_str);

            Intent intent = new Intent();
            intent.setClass(activity_cognMain.this, activity_cognitive.class);
            startActivity(intent);
    }
}