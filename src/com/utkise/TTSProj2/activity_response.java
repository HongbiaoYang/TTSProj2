package com.utkise.TTSProj2;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Stack;

/**
 * Created by Bill on 9/11/14.
 */
public class activity_response extends Activity {
    private String[] web;
    private TextView title;
    private ImageView goback;

    @Override
    public void onBackPressed() {
        finish();
    }

    private Integer[] imageId;
    private ListView list;
    private int count = CONSTANT.START;
    private List<ItemStruct> thisLevel;
    private Stack<List<ItemStruct>> levelStack;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_response);

        goback = (ImageView)findViewById(R.id.header1);
        title = (TextView)findViewById(R.id.header2);

        title.setText("Responses");
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        thisLevel = MyProperties.getInstance().response.getInformation("response");
        updateList(thisLevel);
    }



    private void updateList(List<ItemStruct> level) {
        ListFactory lf = new ListFactory(level);
        ResponseList adapter;
        web =  lf.produceTitleArray();
        imageId = lf.produceImageArray();

        adapter = new
                ResponseList(activity_response.this, web, imageId);
        // adapter.setColors(R.style.ButtonText_Black_18, R.color.Yellow);

        list = (ListView)findViewById(R.id.rlist);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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

                    ItemStruct item = thisLevel.get(position);
                    MyProperties.getInstance().speakBoth(item);
                }

            }
        });


    }

}