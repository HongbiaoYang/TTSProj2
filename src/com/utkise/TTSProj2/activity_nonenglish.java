package com.utkise.TTSProj2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Bill on 9/15/14.
 */
public class activity_nonenglish extends Activity {

    private Button english, spanish, french, chinese;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_nonenglish);

        english = (Button)findViewById(R.id.english);
        spanish = (Button)findViewById(R.id.spanish);
        french = (Button)findViewById(R.id.french);
        chinese = (Button)findViewById(R.id.chinese);


        french.setOnClickListener(new doubleTapListener("french"));
        chinese.setOnClickListener(new doubleTapListener("zhong wen"));

        spanish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
}