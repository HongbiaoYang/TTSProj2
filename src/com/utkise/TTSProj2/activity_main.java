package com.utkise.TTSProj2;

import android.app.Activity;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.*;

/**
 * Created by Bill on 8/28/14.
 */
public class activity_main extends Activity implements OnInitListener {
    private TextToSpeech tts;
    private Button vision, hearing, cognitive, nonenglish;
    private ImageView emergency;
    private int count = CONSTANT.START;
    private int backTimer = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
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


        if (MyProperties.getInstance().response == null) {
            MyProperties.getInstance().response = new DisableType();
            loadXMLResourceParser(MyProperties.getInstance().response, R.xml.response);
        }




        vision = (Button)findViewById(R.id.vision);

        hearing = (Button)findViewById(R.id.hearing);
        cognitive = (Button)findViewById(R.id.cognitive);
        nonenglish = (Button)findViewById(R.id.nonenglish);

        emergency = (ImageView)findViewById(R.id.head_home3);

        cognitive.setOnClickListener(new doubleTapListener("Cognitive"));


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
                    speakOut("non english speaking");

                    MyProperties.getInstance().titleStack.push("espanol");
                    MyProperties.getInstance().doInit(LANG.SPANISH);

                    Intent intent = new Intent();
                    intent.setClass(activity_main.this, activity_hearing.class);
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
                    speakOut("vision");
                    MyProperties.getInstance().titleStack.push("Vision");
                    Intent intent = new Intent();
                    intent.setClass(activity_main.this, activity_vision.class);
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
                    speakOut("hearing");
                    MyProperties.getInstance().titleStack.push("Hearing");
                    Intent intent = new Intent();
                    intent.setClass(activity_main.this, activity_hearing.class);
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
                    speakOut("emergency");
                    Intent intent = new Intent();
                    intent.putExtra("Type", "emergency");
                    intent.setClass(activity_main.this, activity_emergency.class);
                    startActivity(intent);
                }
            }
        });


    }

    private void speakOut(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onBackPressed() {
        int curTime = Calendar.getInstance().get(Calendar.SECOND);

        if (curTime - backTimer < 3) {
            super.onBackPressed();
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
            speakOut("main Menu");
        } else {
            Log.e("TTS", "Initilization Failed!");
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

                                if (currentItem.getChild() == null) {
                                    currentItem.setChild(new ArrayList<ItemStruct>());
                                }
                                currentLevel = currentItem.getChild();
                            }
                            currentItem = new ItemStruct();
                            currentLevel.add(currentItem);

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
                                    name.equalsIgnoreCase("emergency") || name.equalsIgnoreCase("response")) {
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
    }
}