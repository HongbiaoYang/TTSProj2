package com.utkise.TTSProj2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.*;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Bill on 11/5/2015.
 */
public class activity_home extends Activity implements TextToSpeech.OnInitListener{

    private Button para, fixed, tut;
    private int count = CONSTANT.START;
    private String TAG = "activity_home";
    private int backTimer = 0;
    private boolean accept = false;
    private LinearLayout emergency;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_home);

        MyProperties.getInstance().gtts = new TextToSpeech(getApplicationContext(), this);

        // first animation in main
        ImageView image = (ImageView) findViewById(R.id.frame_home);
        image.setBackgroundResource(R.drawable.frame);
//        MyProperties.getInstance().animStack.push((AnimationDrawable) image.getBackground());
        MyProperties.getInstance().currentAnim = (AnimationDrawable) image.getBackground();

        para = (Button)findViewById(R.id.para);
        fixed = (Button)findViewById(R.id.fixed);
        tut = (Button)findViewById(R.id.tut);

        emergency = (LinearLayout)findViewById(R.id.head_home3);


        para.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                    MyProperties.getInstance().transitType = CONSTANT.PARA;

                    Intent intent = new Intent();
                    intent.setClass(activity_home.this, activity_main.class);
                    startActivity(intent);
                }
            }
        });

        fixed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                    MyProperties.getInstance().transitType = CONSTANT.FIXED;

                    Intent intent = new Intent();
                    intent.setClass(activity_home.this, activity_main.class);
                    startActivity(intent);
                }
            }
        });


        tut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    intent.setClass(activity_home.this, activity_tutorials.class);
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
                    intent.setClass(activity_home.this, activity_emergency.class);
                    startActivity(intent);
                }
            }
        });



        // first time, no db exist, create one and load data from xml
        if (MyProperties.getInstance().database == null) {
            MyProperties.getInstance().database = new DatabaseHandler(this);
        }

        DatabaseHandler database = MyProperties.getInstance().database;

        if (database.firstTime == true) {
            loadDatabaseFromXmls();
            addTimeStamp();
        }

        // *****************************************************************************************
        // *****************************************************************************************
        // *****************************************************************************************
        // Premium - Trial version code. Comment this section out to get premium version

        
        Long start = Long.parseLong(MyProperties.getInstance().database.getProp("startTime"));
        Long now = System.currentTimeMillis();
        int elapsed = (int) (now - start)/1000; // time in seconds
        int remain = CONSTANT.HOURS_24 - elapsed;

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
        // *****************************************************************************************
        // *****************************************************************************************
        // *****************************************************************************************

        if (MyProperties.getInstance().gettingonoff_para == null) {
            MyProperties.getInstance().gettingonoff_para = new DisableType("Getting on and off the bus", R.drawable.gettingonandoffthebus,
                    R.drawable.gettingonandoffthebus_v,
                    R.drawable.gettingonandoffthebus_s);

            List<ItemStruct> completeItems = database.getAllItems(CONSTANT.PARA, "Menu", "gettingonoff");
            fillMissingInformation(completeItems);
            MyProperties.getInstance().gettingonoff_para.setInformation("general", completeItems);
        }

        if (MyProperties.getInstance().ridingbus_para == null) {
            MyProperties.getInstance().ridingbus_para = new DisableType("Riding the bus", R.drawable.ridingthebus,
                    R.drawable.ridingthebus_v,
                    R.drawable.ridingthebus_s);
            // loadXMLResourceParser(MyProperties.getInstance().ridingbus, R.xml.ridingbus);
            List<ItemStruct> completeItems = database.getAllItems( CONSTANT.PARA, "Menu", "ridingbus");
            fillMissingInformation(completeItems);
            MyProperties.getInstance().ridingbus_para.setInformation("general", completeItems);

        }

        if (MyProperties.getInstance().safety_para == null) {
            MyProperties.getInstance().safety_para = new DisableType("Safety", R.drawable.safety,
                    R.drawable.safety_v,
                    R.drawable.safety_s);
            // loadXMLResourceParser(MyProperties.getInstance().safety, R.xml.safety);
            List<ItemStruct> completeItems = database.getAllItems(CONSTANT.PARA,"Menu", "safety");
            fillMissingInformation(completeItems);
            MyProperties.getInstance().safety_para.setInformation("general", completeItems);
        }

        if (MyProperties.getInstance().emergency_para == null) {
            MyProperties.getInstance().emergency_para = new DisableType("Emergency", R.drawable.emergency,
                    R.drawable.emergency_v,
                    R.drawable.emergency_s);
//            loadXMLResourceParser(MyProperties.getInstance().emergency, R.xml.emergency);
            List<ItemStruct> completeItems = database.getAllItems(CONSTANT.PARA, "Menu", "emergency");
            fillMissingInformation(completeItems);
            MyProperties.getInstance().emergency_para.setInformation("emergency", completeItems);
        }


        if (MyProperties.getInstance().response_para == null) {
            MyProperties.getInstance().response_para = new DisableType("Response", 0, 0,0);
            // loadXMLResourceParser(MyProperties.getInstance().response, R.xml.response);

            List<ItemStruct> completeItems = database.getAllItems(CONSTANT.PARA,"Menu", "response");
            fillMissingInformation(completeItems);
            MyProperties.getInstance().response_para.setInformation("response", completeItems);
        }

        if (MyProperties.getInstance().gettingonoff_fixed == null) {
            MyProperties.getInstance().gettingonoff_fixed = new DisableType("Getting on and off the bus", R.drawable.gettingonandoffthebus,
                    R.drawable.gettingonandoffthebus_v,
                    R.drawable.gettingonandoffthebus_s);

            List<ItemStruct> completeItems = database.getAllItems(CONSTANT.FIXED, "Menu", "gettingonoff");
            fillMissingInformation(completeItems);
            MyProperties.getInstance().gettingonoff_fixed.setInformation("general", completeItems);
        }

        if (MyProperties.getInstance().ridingbus_fixed == null) {
            MyProperties.getInstance().ridingbus_fixed = new DisableType("Riding the bus", R.drawable.ridingthebus,
                    R.drawable.ridingthebus_v,
                    R.drawable.ridingthebus_s);
            // loadXMLResourceParser(MyProperties.getInstance().ridingbus, R.xml.ridingbus);
            List<ItemStruct> completeItems = database.getAllItems(CONSTANT.FIXED, "Menu", "ridingbus");
            fillMissingInformation(completeItems);
            MyProperties.getInstance().ridingbus_fixed.setInformation("general", completeItems);

        }

        if (MyProperties.getInstance().safety_fixed == null) {
            MyProperties.getInstance().safety_fixed = new DisableType("Safety", R.drawable.safety,
                    R.drawable.safety_v,
                    R.drawable.safety_s);
            // loadXMLResourceParser(MyProperties.getInstance().safety, R.xml.safety);
            List<ItemStruct> completeItems = database.getAllItems(CONSTANT.FIXED, "Menu", "safety");
            fillMissingInformation(completeItems);
            MyProperties.getInstance().safety_fixed.setInformation("general", completeItems);
        }

        if (MyProperties.getInstance().emergency_fixed == null) {
            MyProperties.getInstance().emergency_fixed = new DisableType("Emergency", R.drawable.emergency,
                    R.drawable.emergency_v,
                    R.drawable.emergency_s);
//            loadXMLResourceParser(MyProperties.getInstance().emergency, R.xml.emergency);
            List<ItemStruct> completeItems = database.getAllItems(CONSTANT.FIXED, "Menu", "emergency");
            fillMissingInformation(completeItems);
            MyProperties.getInstance().emergency_fixed.setInformation("emergency", completeItems);
        }


        if (MyProperties.getInstance().response_fixed == null) {
            MyProperties.getInstance().response_fixed = new DisableType("Response", 0, 0,0);
            // loadXMLResourceParser(MyProperties.getInstance().response, R.xml.response);

            List<ItemStruct> completeItems = database.getAllItems(CONSTANT.FIXED, "Menu", "response");
            fillMissingInformation(completeItems);
            MyProperties.getInstance().response_fixed.setInformation("response", completeItems);
        }

    }


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



    private void loadDatabaseFromXmls() {

        fillDatabaseWithType("gettingonoff", R.xml.para_gettingonoff, CONSTANT.PARA);
        fillDatabaseWithType("ridingbus", R.xml.para_ridingbus, CONSTANT.PARA);
        fillDatabaseWithType("safety", R.xml.para_safety, CONSTANT.PARA);
        fillDatabaseWithType("emergency", R.xml.para_emergency, CONSTANT.PARA);
        fillDatabaseWithType("response", R.xml.para_response, CONSTANT.PARA);

        fillDatabaseWithType("gettingonoff", R.xml.fixed_gettingonoff, CONSTANT.FIXED);
        fillDatabaseWithType("ridingbus", R.xml.fixed_ridingbus, CONSTANT.FIXED);
        fillDatabaseWithType("safety", R.xml.fixed_safety, CONSTANT.FIXED);
        fillDatabaseWithType("emergency", R.xml.fixed_emergency, CONSTANT.FIXED);
        fillDatabaseWithType("response", R.xml.fixed_response, CONSTANT.FIXED);

    }


    private void fillDatabaseWithType(String type, int xmlFile, int transitType) {

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
                                int resID = getResources().getIdentifier(imageString, "drawable", getPackageName());
                                currentItem.setImageID(resID);
                            } else if (name.equalsIgnoreCase("imageV")) {
                                String vImageString = xrp.nextText();
                                currentItem.setvImageString(vImageString);
                                int resID = getResources().getIdentifier(vImageString, "drawable", getPackageName());
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
//                                 MyProperties.getInstance().feedItem(currentItem);
                                if (currentItem.getSpecialTag() == null) {
                                    currentItem.setSpecialTag("normal");
                                }
                                MyProperties.getInstance().database.addItem(currentItem, type, transitType);

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


    private void addTimeStamp() {
        Long timeSeconds = System.currentTimeMillis();
        MyProperties.getInstance().database.addProp("startTime", timeSeconds.toString());

    }


    private void fillMissingInformation(List<ItemStruct> itemsFromDatabase) {
        for (ItemStruct is: itemsFromDatabase) {

            if (is.getImageString() != null) {
                is.setImageID(getResources().getIdentifier(is.getImageString(), "drawable", getPackageName()));
            }

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
}