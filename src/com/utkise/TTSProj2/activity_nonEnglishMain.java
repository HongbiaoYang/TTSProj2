package com.utkise.TTSProj2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Bill on 10/24/14.
 */
public class activity_nonEnglishMain extends Activity {

    private Button next, skip;
    private ImageView screen, progress;
    private TextView desc;
    private int curPageIndex;
    private List<TutorialItem> itemList;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_nonenglishmain);

        desc = (TextView)findViewById(R.id.description);
        next = (Button)findViewById(R.id.next);
        skip = (Button)findViewById(R.id.skip);
        screen = (ImageView)findViewById(R.id.screenshot);
        progress = (ImageView)findViewById(R.id.progress);

        next.setOnClickListener(new nextTutorialListener());
        skip.setOnClickListener(new skipTutorialListener());

        if (MyProperties.getInstance().tutorialLists == null) {
            MyProperties.getInstance().LoadTutorialXml(this); // load the xml file only when it's null
        }

        itemList = MyProperties.getInstance().getTutorial("NonEnglish");

        curPageIndex = 0;
        setTutorialPage(itemList.get(curPageIndex));

    }


    private void setTutorialPage(TutorialItem tutorialItem) {
        screen.setImageResource(tutorialItem.image);
        progress.setImageResource(tutorialItem.progress);
        desc.setText(tutorialItem.desc);
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
        MyProperties.getInstance().speakBoth(TITLE.NON_ENGLISH);
        MyProperties.getInstance().Language = LANG.SPANISH;
        String non_english_str = MyProperties.getInstance().getTitleEither(TITLE.NON_ENGLISH);
        MyProperties.getInstance().titleStack.push(non_english_str);

        Intent intent = new Intent();
        intent.setClass(activity_nonEnglishMain.this, activity_hearing.class);
        startActivity(intent);

    }


}