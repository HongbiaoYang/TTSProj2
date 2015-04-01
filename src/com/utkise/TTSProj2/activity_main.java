package com.utkise.TTSProj2;

import android.app.Activity;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
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

        if (MyProperties.getInstance().gettingonoff == null) {
            MyProperties.getInstance().gettingonoff = new DisableType("boarding", R.drawable.gettingon, R.drawable.boarding_v);

            loadXMLResourceParser(MyProperties.getInstance().gettingonoff, R.xml.gettingonoff);
        }

        if (MyProperties.getInstance().ridingbus == null) {
            MyProperties.getInstance().ridingbus = new DisableType("traveling", R.drawable.travelling, R.drawable.travelling_v);
            loadXMLResourceParser(MyProperties.getInstance().ridingbus, R.xml.ridingbus);
        }

        if (MyProperties.getInstance().safety == null) {
            MyProperties.getInstance().safety = new DisableType("Safety", R.drawable.safety, R.drawable.safety_v);
            loadXMLResourceParser(MyProperties.getInstance().safety, R.xml.safety);
        }

        if (MyProperties.getInstance().emergency == null) {
            MyProperties.getInstance().emergency = new DisableType("emergency", R.drawable.emergency, R.drawable.emergency_v);
            loadXMLResourceParser(MyProperties.getInstance().emergency, R.xml.emergency);
        }


        if (MyProperties.getInstance().response == null) {
            MyProperties.getInstance().response = new DisableType("response", 0, 0);
            loadXMLResourceParser(MyProperties.getInstance().response, R.xml.response);
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


    @Override
    public void onBackPressed() {
        int curTime = Calendar.getInstance().get(Calendar.SECOND);


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
            MyProperties.getInstance().speakout("Welcome to project Eric, double tap to make a selection");
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
        MyProperties.getInstance().speakout("Welcome to project Eric, double tap to make a selection");
        super.onResume();
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
}