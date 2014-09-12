package com.utkise.TTSProj2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Bill on 9/11/14.
 */
public class footer_view extends LinearLayout {

    private LayoutInflater inflater;
    private int count = 0;

    public footer_view(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.foot_banner, this, true);

        this.findViewById(R.id.footer1).setOnClickListener(new doubleTapListener("yes"));
        this.findViewById(R.id.footer2).setOnClickListener(new doubleTapListener("No"));
        this.findViewById(R.id.footer3).setOnClickListener(new moreOnclickListener());
    }

    private class moreOnclickListener implements OnClickListener {

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

                Intent intent = new Intent(getContext(), activity_response.class);
                getContext().startActivity(intent);
            }

        }
    }

    // register listeners


}
