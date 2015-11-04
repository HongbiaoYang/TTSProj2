package com.utkise.TTSProj2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Bill on 8/28/14.
 */
public class activity_main extends Activity implements OnInitListener {
    private TextToSpeech tts;
    private Button vision, hearing, cognitive, nonenglish;
    private LinearLayout emergency;
    private int count = CONSTANT.START;
    private int backTimer = 0;
    private boolean accept = false;
    private String TAG = "activity_main";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        MyProperties.getInstance().gtts = new TextToSpeech(getApplicationContext(), this);

        // first animation in main
        ImageView image = (ImageView) findViewById(R.id.frame_home);
        image.setBackgroundResource(R.drawable.frame);
        MyProperties.getInstance().animStack.push((AnimationDrawable) image.getBackground());

        // first time, no db exist, create one and load data from xml
        if (MyProperties.getInstance().database == null) {
            MyProperties.getInstance().database = new DatabaseHandler(this);
        }

        DatabaseHandler database = MyProperties.getInstance().database;

        if (database.firstTime == true) {
            loadDatabaseFromXmls();
            addTimeStamp();

        }


        Long start = Long.parseLong(MyProperties.getInstance().database.getProp("startTime"));
        Long now = System.currentTimeMillis();
        int elapsed = (int) (now - start)/1000; // time in seconds
        int remain = CONSTANT.HOURS_24 - elapsed;

        Log.d(TAG, "start time = " + start + " now=" +now + "elapse="+elapsed + " remain=" +remain);

        if (remain > 0) {

            if (MyProperties.getInstance().firstTimeOpenApp) {
                int hour = remain / 3600;
                int minute = (remain - hour * 3600) / 60;
                int second = remain % 60;
                String timeLeft = String.format("%d hours, %d minutes, %d seconds", hour, minute, second);

                new AlertDialog.Builder(this)
                        .setTitle("Trial Version")
                        .setCancelable(false)
                        .setMessage("Your trial has " + timeLeft + " remaining")
                        .setIcon(R.drawable.time_left)
                        .setPositiveButton(android.R.string.yes, null).show();
            }

        } else {

            new AlertDialog.Builder(this)
                    .setTitle("Time over!")
                    .setMessage("Your trial has expired! Please purchase a premium version!")
                    .setIcon(R.drawable.app_expire)
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://projectericutk.blogspot.com/p/welcome-to-project-eric.html"));
                            startActivity(browserIntent);
                            finish();
                        }


                    }).show();
        }



//        List<ItemStruct> allItems = MyProperties.getInstance().database.getAllItems("gettingonoff");
//
//        for (ItemStruct is : allItems) {
//            String log = "Title: " + is.getTitle() + " ,text: " + is.getText() + " ,color:: " + is.getColorCode();
//            // Writing items to log
//            Log.d("Item", log);
//        }

        if (MyProperties.getInstance().gettingonoff == null) {
            MyProperties.getInstance().gettingonoff = new DisableType("Getting on and off the bus", R.drawable.gettingonandoffthebus,
                                                                                     R.drawable.gettingonandoffthebus_v,
                                                                                     R.drawable.gettingonandoffthebus_s);

            // loadXMLResourceParser(MyProperties.getInstance().gettingonoff, R.xml.gettingonoff);


            List<ItemStruct> completeItems = database.getAllItems("gettingonoff");
            fillMissingInformation(completeItems);
            MyProperties.getInstance().gettingonoff.setInformation("general", completeItems);
        }

        if (MyProperties.getInstance().ridingbus == null) {
            MyProperties.getInstance().ridingbus = new DisableType("Riding the bus", R.drawable.ridingthebus,
                                                                                   R.drawable.ridingthebus_v,
                                                                                   R.drawable.ridingthebus_s);
            // loadXMLResourceParser(MyProperties.getInstance().ridingbus, R.xml.ridingbus);
            List<ItemStruct> completeItems = database.getAllItems("ridingbus");
            fillMissingInformation(completeItems);
            MyProperties.getInstance().ridingbus.setInformation("general", completeItems);

        }

        if (MyProperties.getInstance().safety == null) {
            MyProperties.getInstance().safety = new DisableType("Safety", R.drawable.safety,
                                                                             R.drawable.safety_v,
                                                                             R.drawable.safety_s);
            // loadXMLResourceParser(MyProperties.getInstance().safety, R.xml.safety);
            List<ItemStruct> completeItems = database.getAllItems("safety");
            fillMissingInformation(completeItems);
            MyProperties.getInstance().safety.setInformation("general", completeItems);
        }

        if (MyProperties.getInstance().emergency == null) {
            MyProperties.getInstance().emergency = new DisableType("Emergency", R.drawable.emergency,
                                                                                    R.drawable.emergency_v,
                                                                                    R.drawable.emergency_s);
//            loadXMLResourceParser(MyProperties.getInstance().emergency, R.xml.emergency);
            List<ItemStruct> completeItems = database.getAllItems("emergency");
            fillMissingInformation(completeItems);
            MyProperties.getInstance().emergency.setInformation("emergency", completeItems);
        }


        if (MyProperties.getInstance().response == null) {
            MyProperties.getInstance().response = new DisableType("Response", 0, 0,0);
            // loadXMLResourceParser(MyProperties.getInstance().response, R.xml.response);

            List<ItemStruct> completeItems = database.getAllItems("response");
            fillMissingInformation(completeItems);
            MyProperties.getInstance().response.setInformation("response", completeItems);
        }



        vision = (Button)findViewById(R.id.vision);

        hearing = (Button)findViewById(R.id.hearing);
        cognitive = (Button)findViewById(R.id.cognitive);
        nonenglish = (Button)findViewById(R.id.nonenglish);

        emergency = (LinearLayout)findViewById(R.id.head_home3);

        cognitive.setOnClickListener(new View.OnClickListener() {
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

                    /*MyProperties.getInstance().speakBoth(TITLE.COGNITIVE);

                    Intent intent = new Intent();
                    intent.setClass(activity_main.this, activity_cognitiveMain.class);
                    startActivity(intent);*/

                    // MyProperties.getInstance().speakBoth(TITLE.COGNITIVE);
                    MyProperties.getInstance().speakout(getResources().getString(R.string.cogHint));

                    String cognitive_str = MyProperties.getInstance().getTitleName(TITLE.COGNITIVE);
                    MyProperties.getInstance().titleStack.push(cognitive_str);
                    Intent intent = new Intent();
                    intent.setClass(activity_main.this, activity_cognitive.class);
                    startActivity(intent);
                }
            }
        });

        nonenglish.setOnClickListener(new View.OnClickListener() {
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

                    Intent intent = new Intent();
                    intent.setClass(activity_main.this, activity_nonEnglishMain.class);

                    startActivity(intent);
                }
            }
        });
        // click vision button
        vision.setOnClickListener(new View.OnClickListener() {
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

                    // MyProperties.getInstance().speakBoth(TITLE.VISION);

                    String vision_str = MyProperties.getInstance().getTitleName(TITLE.VISION);
                    MyProperties.getInstance().titleStack.push(vision_str);

                    Intent intent = new Intent();
                    intent.setClass(activity_main.this, activity_visionMain.class);
                    startActivity(intent);
                }
            }
        });

        // click layout_hearing button
        hearing.setOnClickListener(new View.OnClickListener() {
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


                    MyProperties.getInstance().speakBoth(TITLE.HEARING);

                    /*String hearing_str = MyProperties.getInstance().getTitleName(TITLE.HEARING);
                    MyProperties.getInstance().titleStack.push(hearing_str);*/
                    Intent intent = new Intent();
                    intent.setClass(activity_main.this, activity_hearingMain.class);
                    startActivity(intent);
                }
            }
        });

        emergency.setOnClickListener(new View.OnClickListener() {
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

                    MyProperties.getInstance().speakBoth(TITLE.EMERGENCY);

                    String emergency_str = MyProperties.getInstance().getTitleName(TITLE.EMERGENCY);
                    MyProperties.getInstance().titleStack.push(emergency_str);

                    Intent intent = new Intent();
                    intent.putExtra("Type", "emergency");
                    intent.setClass(activity_main.this, activity_emergency.class);
                    startActivity(intent);
                }
            }
        });

    }

    private void addTimeStamp() {
        Long timeSeconds = System.currentTimeMillis();
        MyProperties.getInstance().database.addProp("startTime", timeSeconds.toString());

    }

    private void fillMissingInformation(List<ItemStruct> itemsFromDatabase) {
        for (ItemStruct is: itemsFromDatabase) {
            is.setImageID(getResources().getIdentifier(is.getImageString(), "drawable", getPackageName()));

        }
    }

    private void loadDatabaseFromXmls() {

        fillDatabaseWithType("gettingonoff", R.xml.gettingonoff);
        fillDatabaseWithType("ridingbus", R.xml.ridingbus);
        fillDatabaseWithType("safety", R.xml.safety);
        fillDatabaseWithType("emergency", R.xml.emergency);
        fillDatabaseWithType("response", R.xml.response);

    }

    private void fillDatabaseWithType(String type, int xmlFile) {

        try {

            XmlResourceParser xrp = this.getResources().getXml(xmlFile);
            xrp.next();
            int eventType = xrp.getEventType();
            boolean done = false;

            ItemStruct currentItem = null;

            while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                String name = null;
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        name = xrp.getName();
                        if (name.equalsIgnoreCase("item")) {
                            currentItem = new ItemStruct();
                        } else if (name.equalsIgnoreCase("general") || name.equalsIgnoreCase("emergency") || name.equalsIgnoreCase("response")){
                            accept = true;
                            // accept those items in cognitive only for these above categories
                        } else if (currentItem != null) {
                            if (name.equalsIgnoreCase("title")) {
                                currentItem.setTitle(LANG.ENGLISH, xrp.nextText());
                            } else if (name.equalsIgnoreCase("Text")) {
                                currentItem.setText(LANG.ENGLISH, xrp.nextText());
                            } else if (name.equalsIgnoreCase("Texto")) {
                                currentItem.setText(LANG.SPANISH, xrp.nextText());
                            } else if (name.equalsIgnoreCase("Titulo")) {
                                currentItem.setTitle(LANG.SPANISH, xrp.nextText());
                            } else if (name.equalsIgnoreCase("image")) {
                                String imageString = xrp.nextText();
                                currentItem.setImageString(imageString);
//                                int resID = getResources().getIdentifier(imageString, "drawable", getPackageName());
//                                currentItem.setImageID(resID);
                            } else if (name.equalsIgnoreCase("imageV")) {
                                String vImageString = xrp.nextText();
                                currentItem.setvImageString(vImageString);
//                                int resID = getResources().getIdentifier(vImageString, "drawable", getPackageName());
//                                currentItem.setVImageID(resID);
                            } else if (name.equalsIgnoreCase("Color")) {
                                try{
                                    //Log.e(TAG, "c="+xrp.toString());
                                    currentItem.setColor(Color.parseColor(xrp.nextText().trim()));
                                } catch (Exception ex) {
                                    Log.e(TAG, ex.toString()+" item="+currentItem.getTitle());
                                }
                            }else if (name.equalsIgnoreCase("Customize")) {
                                currentItem.setSpecialTag(xrp.nextText());
                            }

                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = xrp.getName();
                        if (name.equalsIgnoreCase("item")) {
                            if (accept == true) {
//                                 MyProperties.getInstance().feedItem(currentItem);
                                MyProperties.getInstance().database.addItem(currentItem, type);

                            }

                        } else if (name.equalsIgnoreCase("general") ||
                                name.equalsIgnoreCase("emergency") || name.equalsIgnoreCase("response")) {

                            accept = false;
                            // close the accept on whatever
                        }

                        break;
                    case XmlPullParser.END_DOCUMENT:
                        done = true;
                        break;
                }
                eventType = xrp.next();
            }

        } catch (XmlPullParserException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


    /*
    private void storeVoiceFile(String destFileName) {
        HashMap<String, String> myHashRender = new HashMap();
        String wakeUpText = "Are you up yet?";
        // String destFileName = "/sdcard/myAppCache/wakeUp.wav";
        // String destFileName = "/storage/extSdCard/myapp.wav";
        myHashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, wakeUpText);
        int res = MyProperties.getInstance().gtts.synthesizeToFile(wakeUpText, myHashRender, destFileName);

        if (res == TextToSpeech.ERROR) {
            Log.d("activity_main","save file failed");
        } else {
            Log.d("activity_main", "save success" + res);
        }

    }
    */


    @Override
    public void onBackPressed() {
        int curTime = Calendar.getInstance().get(Calendar.SECOND);

        Log.d(TAG,"back is pressed in main");

        if (curTime - backTimer < 3) {
            TextToSpeech mTts = MyProperties.getInstance().gtts;
            mTts.stop();
            mTts.shutdown();
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Click again to quit this app", 3).show();
            backTimer = curTime;
        }

    }


    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            LANG lan = MyProperties.getInstance().Language;
            MyProperties.getInstance().doInit(lan);


            Log.i(TAG, "onInit TTS, firstOpen="+MyProperties.getInstance().firstTimeOpenApp);
            if (MyProperties.getInstance().firstTimeOpenApp) {
                MyProperties.getInstance().speakout("Welcome to project Eric, double tap to make a selection");
                Log.i(TAG, "onInit inside long, firstOpen="+MyProperties.getInstance().firstTimeOpenApp);
                MyProperties.getInstance().firstTimeOpenApp = false;

            } else {
                MyProperties.getInstance().speakout("Project Eric");
            }

            Log.i("activity_main", "onInit of TTS");
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }


    @Override
    protected void onDestroy() {


        TextToSpeech mTts = MyProperties.getInstance().gtts;
        //Close the Text to Speech Library
        if( mTts!= null) {

            mTts.stop();
            mTts.shutdown();
            Log.d("activity_main", "TTS Destroyed");
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {

            MyProperties.getInstance().speakout("Project Eric");

        super.onResume();
    }

    /*
    private void loadXMLResourceParser(DisableType boarding, int xmlFile) {
        List<ItemStruct> root = null;

        try {
            Stack<ItemStruct> itemStack = new Stack<ItemStruct>();

            XmlResourceParser xrp = this.getResources().getXml(xmlFile);
            xrp.next();
            int eventType = xrp.getEventType();
            boolean done = false;

            ItemStruct currentItem = null;
            List<ItemStruct> currentLevel = null;

            root = new ArrayList<ItemStruct>();
            currentLevel = root;


            while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                String name = null;
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        name = xrp.getName();
                       if (name.equalsIgnoreCase("item")) {
                            if (currentItem != null) {
                                itemStack.push(currentItem);

                                if (currentItem.getChild() == null) {
                                    currentItem.setChild(new ArrayList<ItemStruct>());
                                }
                                currentLevel = currentItem.getChild();
                            }
                            currentItem = new ItemStruct();
                            currentLevel.add(currentItem);
                       } else if (name.equalsIgnoreCase("general") ){
                           accept = true;
                           // accept those items in cognitive only for these above categories
                        } else if (currentItem != null) {
                            if (name.equalsIgnoreCase("title")) {
                                currentItem.setTitle(LANG.ENGLISH, xrp.nextText());
                            } else if (name.equalsIgnoreCase("Text")) {
                                currentItem.setText(LANG.ENGLISH, xrp.nextText());
                            } else if (name.equalsIgnoreCase("Texto")) {
                                currentItem.setText(LANG.SPANISH, xrp.nextText());
                            } else if (name.equalsIgnoreCase("Titulo")) {
                                currentItem.setTitle(LANG.SPANISH, xrp.nextText());
                            } else if (name.equalsIgnoreCase("image")) {
                                int resID = getResources().getIdentifier(xrp.nextText(), "drawable", getPackageName());
                                currentItem.setImageID(resID);
                            } else if (name.equalsIgnoreCase("imageV")) {
                                int resID = getResources().getIdentifier(xrp.nextText(), "drawable", getPackageName());
                                currentItem.setVImageID(resID);
                            } else if (name.equalsIgnoreCase("Color")) {
                                try{
                                    //Log.e(TAG, "c="+xrp.toString());
                                    currentItem.setColor(Color.parseColor(xrp.nextText().trim()));
                                } catch (Exception ex) {
                                    Log.e(TAG, ex.toString()+" item="+currentItem.getTitle());
                                }
                            }else if (name.equalsIgnoreCase("Customize")) {
                                currentItem.setSpecialTag(xrp.nextText());
                            }

                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = xrp.getName();
                        if (name.equalsIgnoreCase("item")) {
                            if (accept == true) {
                                MyProperties.getInstance().feedItem(currentItem);
                            }
                            //currentLevel.add(currentItem);
                            if (itemStack.isEmpty()) {
                                currentItem = null;
                                currentLevel = root;
                            } else {
                                currentItem = itemStack.pop();
                            }
                        } else if (name.equalsIgnoreCase("general") ||
                                    name.equalsIgnoreCase("emergency") || name.equalsIgnoreCase("response")) {
                            boarding.setInformation(name, root);
                            root = new ArrayList<ItemStruct>();
                            currentLevel = root;

                            accept = false;
                            // close the accept on whatever
                        }

                        break;
                    case XmlPullParser.END_DOCUMENT:
                        done = true;
                        break;
                }
                eventType = xrp.next();
            }

        } catch (XmlPullParserException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    */
}