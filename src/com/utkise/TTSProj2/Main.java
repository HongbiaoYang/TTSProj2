package com.utkise.TTSProj2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

/**
 * Created by Bill on 8/28/14.
 */
public class Main extends Activity implements OnInitListener {
    private TextToSpeech tts;
    private Button aging, hearing, speech, vision;
    private ImageView emergency;
    private int i;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        MyProperties.getInstance().gtts = new TextToSpeech(getApplicationContext(), this);
        tts = MyProperties.getInstance().gtts;

        if (MyProperties.getInstance().boarding == null) {
            MyProperties.getInstance().boarding = new DisableType();
            loadXMLResourceParser(MyProperties.getInstance().boarding, R.xml.boarding);
        }

        if (MyProperties.getInstance().traveling == null) {
            MyProperties.getInstance().traveling = new DisableType();
            loadXMLResourceParser(MyProperties.getInstance().traveling, R.xml.travelling);
        }

        if (MyProperties.getInstance().gettingoff == null) {
            MyProperties.getInstance().gettingoff = new DisableType();
            loadXMLResourceParser(MyProperties.getInstance().gettingoff, R.xml.gettingoff);
        }

        if (MyProperties.getInstance().emergency == null) {
            MyProperties.getInstance().emergency = new DisableType();
            loadXMLResourceParser(MyProperties.getInstance().emergency, R.xml.emergency);
        }



        aging = (Button)findViewById(R.id.aging);
        hearing = (Button)findViewById(R.id.hearing);
        speech = (Button)findViewById(R.id.speech);
        vision = (Button)findViewById(R.id.vision);
        emergency = (ImageView)findViewById(R.id.footer2);

        aging.setOnClickListener(new doubleTapListenner("aging"));
        speech.setOnClickListener(new doubleTapListenner("speech"));

        // click vision button
        vision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                Handler handler = new Handler();
                Runnable run = new Runnable() {
                    @Override
                    public void run() {
                        i = 0;
                    }
                };

                if (i == 1) {
                    handler.postDelayed(run, 250);
                } else if (i == 2) {
                    i = 0;
                    speakOut("Vision");
                    MyProperties.getInstance().titleStack.push("Vision");
                    Intent intent = new Intent();
                    intent.setClass(Main.this, Vision.class);
                    startActivity(intent);
                    // close current activity to avoid multiple activities existing
                    Main.this.finish();
                }
            }
        });

        // click hearing button
        hearing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                Handler handler = new Handler();
                Runnable run = new Runnable() {
                    @Override
                    public void run() {
                        i = 0;
                    }
                };

                if (i == 1) {
                    handler.postDelayed(run, 250);
                } else if (i == 2) {
                    i = 0;
                    speakOut("hearing");
                    MyProperties.getInstance().titleStack.push("Hearing");
                    Intent intent = new Intent();
                    intent.setClass(Main.this, HearingActivity.class);
                    startActivity(intent);
                    // close current activity to avoid multiple activities existing
                    Main.this.finish();
                }
            }
        });

        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                Handler handler = new Handler();
                Runnable run = new Runnable() {
                    @Override
                    public void run() {
                        i = 0;
                    }
                };

                if (i == 1) {
                    handler.postDelayed(run, 250);
                } else if (i == 2) {
                    i = 0;
                    speakOut("emergency");
                    Intent intent = new Intent();
                    intent.putExtra("Type", "emergency");
                    intent.setClass(Main.this, emergencyActivity.class);
                    startActivity(intent);
                    // close current activity to avoid multiple activities existing
                    Main.this.finish();
                }
            }
        });


    }

    private void speakOut(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }


    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            LANG lan = MyProperties.getInstance().Language;
            doInit(lan);
            speakOut("Main Menu");
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    private void doInit(LANG lan) {
        int result = TextToSpeech.ERROR;

        if (lan == LANG.ENGLISH) {
            result = tts.setLanguage(Locale.US);
        } else if (lan==LANG.SPANISH) {
            Locale locSpanish = new Locale("spa", "ESP");
            result = tts.setLanguage(locSpanish);
        }

        if (result == TextToSpeech.LANG_MISSING_DATA
                || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            Log.e("TTS", "This Language is not supported");
        }

    }


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

                                if (currentItem.child == null) {
                                    currentItem.child = new ArrayList<ItemStruct>();
                                }
                                currentLevel = currentItem.child;
                            }
                            currentItem = new ItemStruct();
                            currentLevel.add(currentItem);

                        } else if (currentItem != null) {
                            if (name.equalsIgnoreCase("title")) {
                                currentItem.title = xrp.nextText();
                            } else if (name.equalsIgnoreCase("Text")) {
                                currentItem.text = xrp.nextText();
                            } else if (name.equalsIgnoreCase("image")) {
                                int resID = getResources().getIdentifier(xrp.nextText(), "drawable", getPackageName());
                                currentItem.imageID = resID;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = xrp.getName();
                        if (name.equalsIgnoreCase("item")) {
                            //currentLevel.add(currentItem);
                            if (itemStack.isEmpty()) {
                                currentItem = null;
                                currentLevel = root;
                            } else {
                                currentItem = itemStack.pop();
                            }
                        } else if (name.equalsIgnoreCase("general") || name.equalsIgnoreCase("trip") ||
                                    name.equalsIgnoreCase("safety") || name.equalsIgnoreCase("comfort") ||
                                    name.equalsIgnoreCase("emergency")) {
                            boarding.setInformation(name, root);
                            root = new ArrayList<ItemStruct>();
                            currentLevel = root;
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
        Log.i("gInfoActivity","localroot="+root.toString());

    }
}