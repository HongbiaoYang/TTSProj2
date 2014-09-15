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
    private int count = CONSTANT.START;

    public footer_view(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.foot_banner, this, true);

        String yes = "Yes";
        String no = "No";
        String more = "More";

        if (MyProperties.getInstance().Language == LANG.SPANISH) {
            yes = "sí";
            no = "no";
            more = "más";
        }

        Button Yes, No, More;

        Yes = (Button) this.findViewById(R.id.footer1);
        No = (Button) this.findViewById(R.id.footer2);
        More = (Button) this.findViewById(R.id.footer3);


        //set text
        Yes.setText(yes);
        No.setText(no);
        More.setText(more);

        // set listener
        Yes.setOnClickListener(new doubleTapListener(yes));
        No.setOnClickListener(new doubleTapListener(no));
        More.setOnClickListener(new moreOnclickListener());

    }

    private class moreOnclickListener implements OnClickListener {

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

                Intent intent = new Intent(getContext(), activity_response.class);
                getContext().startActivity(intent);
            }

        }
    }

    // register listeners


}
