package com.utkise.TTSProj2;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Created by Bill on 9/11/14.
 */
public class activity_more extends Activity {

    private Button more0, more1, more2, more3, more4;
    private Button more5;
    private ImageView goback;
    //private  ImageButton more5;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_more);

        more0 = (Button)findViewById(R.id.more0);
        more1 = (Button)findViewById(R.id.more1);
        more2 = (Button)findViewById(R.id.more2);
        more3 = (Button)findViewById(R.id.more3);
        more4 = (Button)findViewById(R.id.more4);
        more5 = (Button)findViewById(R.id.more5);
        goback = (ImageView)findViewById(R.id.header1);


        // speak the words
        more1.setOnClickListener(new doubleTapListener(more1.getText().toString()));
        more2.setOnClickListener(new doubleTapListener(more2.getText().toString()));
        more3.setOnClickListener(new doubleTapListener(more3.getText().toString()));
        more4.setOnClickListener(new doubleTapListener(more4.getText().toString()));
        more5.setOnClickListener(new doubleTapListener(more5.getText().toString()));

        more0.setOnClickListener(new gobackOnClickListener());
        goback.setOnClickListener(new gobackOnClickListener());

    }

    private class gobackOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}