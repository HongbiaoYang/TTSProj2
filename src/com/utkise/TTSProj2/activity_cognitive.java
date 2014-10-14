package com.utkise.TTSProj2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.*;

/**
 * Created by Bill on 10/13/14.
 */
public class activity_cognitive extends Activity {

    private ImageView ok;
    private Button left, right;
    private TextView text;
    private LinearLayout screen;
    private ImageView goBack;


    private List<ItemStruct> root;
    private List<ItemStruct> curLevel;
    private ItemStruct curItem;
    private int curLocation;
    private int count;
    private Stack<List<ItemStruct>> levelStack;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cognitive);

        ok = (ImageView)findViewById(R.id.OK);
        left = (Button)findViewById(R.id.left);
        right = (Button)findViewById(R.id.right);
        text = (TextView)findViewById(R.id.text);
        screen = (LinearLayout)findViewById(R.id.screen);
        goBack = (ImageView)findViewById(R.id.header1);

        root = loadCognitiveFlatTree();
        mergeList(root, "to the driver");
        mergeList(root, "destination");

        curLocation = 0;
        curLevel = root;
        levelStack = new Stack<List<ItemStruct>>();

        // display now
        displayCurrent();

        left.setBackgroundResource(R.drawable.left);
        right.setBackgroundResource(R.drawable.right);

        left.setOnClickListener(new NavigateListener(DIRECTION.LEFT));
        right.setOnClickListener(new NavigateListener(DIRECTION.RIGHT));
        screen.setOnClickListener(new EnterListener());

        goBack.setOnClickListener(new goBackOrUpListener());
    }

    private void mergeList(List<ItemStruct> root, String tag) {
        int order = 0;
        List<ItemStruct> main = null;

        for (Iterator<ItemStruct> iter = root.listIterator();iter.hasNext();) {
            ItemStruct is = iter.next();

            if (is.getTitle().equalsIgnoreCase(tag)) {
                if (order++ == 0) {
                   main  = is.getChild();
                } else {
                    main.addAll(is.getChild());
                    iter.remove();
                }
            }
        }

    }

    private List<ItemStruct> loadCognitiveFlatTree() {
        List<ItemStruct> level = new ArrayList<ItemStruct>();

        for (DisableType dis : MyProperties.getInstance().DisableList())       {
            for (ItemStruct is :dis.getAllInfo())   {
                level.add(is);
            }
        }
        return level;
    }


    private class NavigateListener implements View.OnClickListener {
        private final DIRECTION direction;

        public NavigateListener(DIRECTION direction) {
            this.direction = direction;

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

                if (direction == DIRECTION.LEFT) {
                    curLocation -= 1;
                    if (curLocation <= 0) {
                        curLocation = 0;
                    }
                } else if (direction == DIRECTION.RIGHT) {
                    curLocation += 1;
                    if (curLocation >= curLevel.size() - 1) {
                        curLocation = curLevel.size() - 1;
                    }
                } else {

                    Log.e("cognitive", "Something is wrong:"+direction);
                }

                displayCurrent();
            }

        }
    }

    private void displayCurrent() {
        curItem = curLevel.get(curLocation);

        ok.setImageDrawable(null);         ok.setBackgroundResource(0);
        ok.setImageResource(curItem.getImageID());
        text.setText(curItem.getTitle());
        // MyProperties.getInstance().speakout(curItem.getText());
    }


    private class EnterListener implements View.OnClickListener {
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

                if (curItem.getChild() == null) {
                    speakItem();
                } else {
                    enterNextLevel();
                }

            }

        }
    }

    // enter next level
    private void enterNextLevel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to enter?");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                levelStack.push(curLevel);
                curLevel = curItem.getChild();
                curLocation = 0;
                curItem = curLevel.get(curLocation);

                displayCurrent();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        changeLook(builder);
    }

    // speak current item content
    private void speakItem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do you want to speak this?");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                MyProperties.getInstance().speakout(curItem.getText());
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

       changeLook(builder);
    }

    private void changeLook(AlertDialog.Builder builder) {
        AlertDialog dialog = builder.create();
        dialog.show();

        Button speak = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button cancel = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        // set appearance and background
        speak.setBackgroundResource(R.drawable.btn_yellow);
        speak.setTextAppearance(this, R.style.ButtonText_Black_20);

        cancel.setBackgroundResource(R.drawable.btn_yellow);
        cancel.setTextAppearance(this, R.style.ButtonText_Black_20);

        int textViewId = dialog.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
        TextView tv = (TextView) dialog.findViewById(textViewId);
        tv.setTextAppearance(this, R.style.ButtonText_yellow);
    }

    private void goBackOrUp(){
        if (levelStack.isEmpty()) {
            finish();
        } else {
            curLevel = levelStack.pop();
            curLocation = 0;
            displayCurrent();
        }
    }

    @Override
    public void onBackPressed() {
        goBackOrUp();
    }

    private class goBackOrUpListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            goBackOrUp();
        }
    }
}