package com.utkise.TTSProj2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.*;

/**
 * Created by Bill on 10/13/14.
 */
public class activity_cognitive0 extends Activity {

    private ImageView ok, home, left,right;
    private Button  yes, no, more;
    private TextView text;
    private LinearLayout screen;
    private ImageView goBack;


    private List<ItemStruct> root;
    private List<ItemStruct> curLevel;
    private ItemStruct curItem;
    private int curLocation, firstLocation;
    private int count;
    private Stack<List<ItemStruct>> levelStack;

    private SharedPreferences pref;
    private SuperTutorial tutorial;
    private List<ItemStruct> response;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cognitive);

        ok = (ImageView)findViewById(R.id.OK);
        left = (ImageView)findViewById(R.id.left);
        right = (ImageView)findViewById(R.id.right);
        text = (TextView)findViewById(R.id.text);
        screen = (LinearLayout)findViewById(R.id.screen);
        goBack = (ImageView)findViewById(R.id.header1);

        yes = (Button)findViewById(R.id.footer1);
        no =  (Button)findViewById(R.id.footer2);
        more = (Button)findViewById(R.id.footer3);
        home = (ImageView)findViewById(R.id.header3);


        root = loadCognitiveFlatTree();
        mergeList(root, "to the driver");
        mergeList(root, "destination");

        response = loadResponseFlatTree();

        firstLocation = 0;
        curLocation = firstLocation;
        curLevel = root;
        levelStack = new Stack<List<ItemStruct>>();

        // display now
        displayCurrent();

        left.setBackgroundResource(R.drawable.left_anim);
        right.setBackgroundResource(R.drawable.right_anim);

        left.setOnLongClickListener(new NavigateListener(DIRECTION.LEFT));
        right.setOnLongClickListener(new NavigateListener(DIRECTION.RIGHT));
        // screen.setOnClickListener(new EnterListener());
        screen.setOnLongClickListener(new EnterLongListener());

        goBack.setOnLongClickListener(new goBackOrUpListener());
        home.setOnLongClickListener(new goHomeListener());
        yes.setOnLongClickListener(new YesNoListoner("Yes"));
        no.setOnLongClickListener(new YesNoListoner("No"));
        more.setOnLongClickListener(new OnResponseListener());

        pref = this.getSharedPreferences("com.utkise.TTSProj2", Context.MODE_PRIVATE);
        boolean done = pref.getBoolean("tutorial_cognitive", false);

        if (done == false)  {
            tutorial = new CognitiveTutorial(true);
            tutorial.startTutorial();
        } else {
            tutorial = new CognitiveTutorial(false);
        }
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


    private List<ItemStruct> loadResponseFlatTree() {
        List<ItemStruct> level = new ArrayList<ItemStruct>();

        for (ItemStruct is:MyProperties.getInstance().response.getResponseInfo()) {
            level.add(is);
        }

        return level;
    }


    private class NavigateListener implements View.OnLongClickListener {
        private final DIRECTION direction;

        public NavigateListener(DIRECTION direction) {
            this.direction = direction;

        }

  /*      @Override
        public void onClick(View v) {
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
                    goPreviousItem();
                } else if (direction == DIRECTION.RIGHT) {
                    goNextItem();
                } else {
                    Log.e("cognitive", "Something is wrong:"+direction);
                }
            }
        }*/

        @Override
        public boolean onLongClick(View v) {
            if (direction == DIRECTION.LEFT) {
                goPreviousItem();
            } else if (direction == DIRECTION.RIGHT) {
                goNextItem();
            } else {
                Log.e("cognitive", "Something is wrong:"+direction);
            }
            return false;
        }
    }

    private void goPreviousItem() {
      /*  AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to access previous item?");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {*/

                curLocation -= 1;
                if (curLocation <= firstLocation) {
                    curLocation = firstLocation;
                }
                displayCurrent();

                if (tutorial != null) {
                    tutorial.checkNext(CognitiveTutorial.LOCAL_DIRECTION.LEFT.ordinal());
                }

          /*  }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        changeLook(builder);*/
    }

    private void goNextItem() {
      /*  AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to access next item?");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
*/
                curLocation += 1;
                if (curLocation >= curLevel.size() - 1) {
                    curLocation = curLevel.size() - 1;
                }

                displayCurrent();

                if (tutorial != null) {
                    tutorial.checkNext(CognitiveTutorial.LOCAL_DIRECTION.RIGHT.ordinal());
                }

       /*     }
       });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        changeLook(builder);*/
    }

    private void displayCurrent() {
        curItem = curLevel.get(curLocation);

        ok.setImageDrawable(null);
        ok.setImageResource(curItem.getImageID());
        text.setText(curItem.getTitle());
        // MyProperties.getInstance().speakout(curItem.getText());
    }

    private class EnterLongListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {
            if (curItem.getChild() == null) {
                speakItem();
            } else {
                enterNextLevel();
            }

            return false;
        }
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
                curLocation = firstLocation;
                curItem = curLevel.get(curLocation);

                displayCurrent();
                tutorial.checkNext(CognitiveTutorial.LOCAL_DIRECTION.ENTER.ordinal());
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
                tutorial.checkNext(CognitiveTutorial.LOCAL_DIRECTION.SPEAK.ordinal());
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (levelStack.isEmpty()) {

            builder.setTitle("Do you want to go back to cognitive main page?");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (curLevel == response) {
                        curLevel = root;
                        firstLocation = 0;
                        curLocation = firstLocation;

                        displayCurrent();
                        LinearLayout foot = (LinearLayout)findViewById(R.id.responseFoot);
                        foot.setVisibility(View.VISIBLE);

                    } else{
                        MyProperties.getInstance().popStacks();
                        MyProperties.getInstance().shutup();
                        finish();
                    }
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        } else {


            builder.setTitle("Do you want to go back to higher level");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    tutorial.checkNext(CognitiveTutorial.LOCAL_DIRECTION.BACK.ordinal());
                    curLevel = levelStack.pop();
                    curLocation = firstLocation;
                    displayCurrent();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

        }

        changeLook(builder);
    }

    @Override
    public void onBackPressed() {
        goBackOrUp();
    }

    private class goBackOrUpListener implements View.OnLongClickListener {
       /* @Override
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

                goBackOrUp();

            }
        }*/

        @Override
        public boolean onLongClick(View v) {
            goBackOrUp();
            return false;
        }
    }

    private class goHomeListener implements View.OnLongClickListener {
      /*  @Override
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
                goBackHome();
            }
        }*/

        @Override
        public boolean onLongClick(View v) {
            goBackHome();
            return false;
        }
    }

    private void goBackHome() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do you want to go back home?");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                if (curLevel == response) {
                    LinearLayout foot = (LinearLayout)findViewById(R.id.responseFoot);
                    foot.setVisibility(View.VISIBLE);
                }

                tutorial.checkNext(CognitiveTutorial.LOCAL_DIRECTION.HOME.ordinal());
                curLevel = root;
                firstLocation = 0;
                curLocation = firstLocation;

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

    private class YesNoListoner implements View.OnLongClickListener {
        private final String word;

        public YesNoListoner(String word) {
            this.word = word;
        }

      /*  @Override
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
                YesOrNo(word);
            }
        }*/

        @Override
        public boolean onLongClick(View v) {
            YesOrNo(word);
            return false;
        }
    }

    private void YesOrNo(final String word) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do you want say "+word+" ?");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                MyProperties.getInstance().speakout(word);

                if (word.equalsIgnoreCase("yes")) {
                    tutorial.checkNext(CognitiveTutorial.LOCAL_DIRECTION.YES.ordinal());
                } else if (word.equalsIgnoreCase("no")) {
                    tutorial.checkNext(CognitiveTutorial.LOCAL_DIRECTION.NO.ordinal());
                } else {
                    Log.e("activity_cognitive0", "something is wrong:" + word);
                }
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

    private class OnResponseListener implements View.OnLongClickListener {
      /*  @Override
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
                GoToResponse();
            }
        }*/


        @Override
        public boolean onLongClick(View v) {
            GoToResponse();
            return false;
        }
    }

    private void GoToResponse() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do you want to go to response page?");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                curLevel = response;
                firstLocation = MyProperties.getInstance().response.getCustomCount();
                curLocation = firstLocation;
                displayCurrent();
                tutorial.checkNext(CognitiveTutorial.LOCAL_DIRECTION.MORE.ordinal());

                LinearLayout foot = (LinearLayout)findViewById(R.id.responseFoot);
                foot.setVisibility(View.INVISIBLE);

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


}