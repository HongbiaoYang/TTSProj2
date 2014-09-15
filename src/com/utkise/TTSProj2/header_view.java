package com.utkise.TTSProj2;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by Bill on 9/11/14.
 */
public class header_view extends LinearLayout {

    private LayoutInflater inflater;
    private int count = 0;

    public header_view(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.head_banner, this, true);

        this.findViewById(R.id.header3).setOnClickListener(new homeOnClickListener());

    }

    private class homeOnClickListener implements OnClickListener {

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

                MyProperties.getInstance().doInit(LANG.ENGLISH);
                MyProperties.getInstance().titleStack.clear();
                Intent intent = new Intent(getContext(), activity_main.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getContext().startActivity(intent);
            }

        }
    }

    // register listeners


}
