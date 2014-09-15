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
        this.count = CONSTANT.START;
    }

    @Override
    public void onClick(View v) {
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
            MyProperties.getInstance().speakout(word);
        }

    }
}
