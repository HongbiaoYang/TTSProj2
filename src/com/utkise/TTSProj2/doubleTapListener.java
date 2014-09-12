package com.utkise.TTSProj2;

import android.content.Intent;
import android.os.Handler;
import android.view.View;

/**
 * Created by Bill on 9/3/14.
 */
public class doubleTapListener implements View.OnClickListener {

    private String word;
    private int count;


    public doubleTapListener(String word) {
        this.word = word;
        this.count = 0;
    }

    @Override
    public void onClick(View v) {
        count++;
        Handler handler = new Handler();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                count = 0;
            }
        };

        if (count == 1) {
            handler.postDelayed(run, 250);
        } else if (count == 2) {
            count = 0;
            MyProperties.getInstance().speakout(word);
        }

    }
}
