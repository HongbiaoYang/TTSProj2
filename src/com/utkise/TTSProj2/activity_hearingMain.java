package com.utkise.TTSProj2;

import android.app.Activity;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Bill on 10/24/14.
 */
public class activity_hearingMain extends Activity {

    private Button next, skip, prev;
    private ImageView screen, progress;
    private TextView desc;
    private int curPageIndex;
    private List<TutorialItem> itemList;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_hearingmain);

        desc = (TextView)findViewById(R.id.description);
        prev = (Button)findViewById(R.id.prev);
        next = (Button)findViewById(R.id.next);
        skip = (Button)findViewById(R.id.skip);
        screen = (ImageView)findViewById(R.id.screenshot);
        progress = (ImageView)findViewById(R.id.progress);

        prev.setOnClickListener(new prevTutorialListener());
        next.setOnClickListener(new nextTutorialListener());
        skip.setOnClickListener(new skipTutorialListener());

        if (MyProperties.getInstance().tutorialLists == null) {
            MyProperties.getInstance().LoadTutorialXml(this); // load the xml file only when it's null
        }

        itemList = MyProperties.getInstance().getTutorial("Hearing");

        curPageIndex = 0;
        setTutorialPage(itemList.get(curPageIndex));

    }


    private void setTutorialPage(TutorialItem tutorialItem) {
        screen.setImageResource(tutorialItem.image);
        progress.setImageResource(tutorialItem.progress);
        desc.setText(tutorialItem.desc.replace("\\n", "\n"));
        MyProperties.getInstance().speakout(tutorialItem.voice);
    }


    private class nextTutorialListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            curPageIndex++;
            if (curPageIndex < itemList.size()) {
                setTutorialPage(itemList.get(curPageIndex));
                if (curPageIndex == itemList.size() - 1) {
                    next.setText("Done");
                }

            } else {

                goToNewPage();

            }


        }
    }

    private class skipTutorialListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            goToNewPage();
        }
    }

    private void goToNewPage() {
        MyProperties.getInstance().speakBoth(TITLE.HEARING);

        String hearing_str = MyProperties.getInstance().getTitleName(TITLE.HEARING);
        MyProperties.getInstance().titleStack.push(hearing_str);
        Intent intent = new Intent();
        intent.setClass(activity_hearingMain.this, activity_hearing.class);
        startActivity(intent);

    }



    private class prevTutorialListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            curPageIndex--;
            if (curPageIndex >= 0) {
                setTutorialPage(itemList.get(curPageIndex));
                next.setText("NEXT");
            } else {
                curPageIndex = 0;

            }


        }
    }
}