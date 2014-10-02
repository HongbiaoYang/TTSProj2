package com.utkise.TTSProj2;

import android.app.Activity;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.UtteranceProgressListener;
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

        // first animation in main
        ImageView image = (ImageView) findViewById(R.id.frame_home);
        image.setBackgroundResource(R.drawable.frame);
        MyProperties.getInstance().animStack.push((AnimationDrawable) image.getBackground());

        if (MyProperties.getInstance().boarding == null) {
            MyProperties.getInstance().boarding = new DisableType("boarding", R.drawable.boarding);
            loadXMLResourceParser(MyProperties.getInstance().boarding, R.xml.boarding);
        }

        if (MyProperties.getInstance().traveling == null) {
            MyProperties.getInstance().traveling = new DisableType("traveling", R.drawable.travelling);
            loadXMLResourceParser(MyProperties.getInstance().traveling, R.xml.travelling);
        }

        if (MyProperties.getInstance().gettingoff == null) {
            MyProperties.getInstance().gettingoff = new DisableType("getting off", R.drawable.gettingoff);
            loadXMLResourceParser(MyProperties.getInstance().gettingoff, R.xml.gettingoff);
        }

        if (MyProperties.getInstance().emergency == null) {
            MyProperties.getInstance().emergency = new DisableType("emergency", R.drawable.emergency);
            loadXMLResourceParser(MyProperties.getInstance().emergency, R.xml.emergency);
        }


        if (MyProperties.getInstance().response == null) {
            MyProperties.getInstance().response = new DisableType("response", 0);
            loadXMLResourceParser(MyProperties.getInstance().response, R.xml.response);
        }

        vision = (Button)findViewById(R.id.vision);

        hearing = (Button)findViewById(R.id.hearing);
        cognitive = (Button)findViewById(R.id.cognitive);
        nonenglish = (Button)findViewById(R.id.nonenglish);

        emergency = (ImageView)findViewById(R.id.head_home3);

        cognitive.setOnClickListener(new doubleTapListener(MyProperties.getInstance().getTitleName(TITLE.COGNITIVE)));
        // vision.setOnClickListener(new multiTapListener("test"));


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

                    // MyProperties.getInstance().doInit(LANG.SPANISH);
                    // only change the global language setting , not change the tts engine
                    MyProperties.getInstance().Language = LANG.SPANISH;

                 /*   String filename = getApplicationContext().getFilesDir().getPath()+"/test.wav";

                    storeVoiceFile("/storage/sdcard0/video/video.wav");
                    storeVoiceFile(getApplicationContext().getFilesDir().getPath() + "newWavFile.wav");

                    Log.d("activity_main", "path="+"/storage/sdcard0/video/video.wav");
                    Log.d("activity_main", "path="+getApplicationContext().getFilesDir().getPath() + "newWavFile.wav");
*/
                    String non_english_str = MyProperties.getInstance().getTitleEither(TITLE.NON_ENGLISH);

                    MyProperties.getInstance().speakBoth(TITLE.NON_ENGLISH);
                    MyProperties.getInstance().titleStack.push(non_english_str);

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

                    MyProperties.getInstance().speakBoth(TITLE.VISION);
                    String vision_str = MyProperties.getInstance().getTitleName(TITLE.VISION);
                    MyProperties.getInstance().titleStack.push(vision_str);


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


                    MyProperties.getInstance().speakBoth(TITLE.HEARING);

                    String hearing_str = MyProperties.getInstance().getTitleName(TITLE.HEARING);
                    MyProperties.getInstance().titleStack.push(hearing_str);
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
            MyProperties.getInstance().speakout("main Menu");
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
                            } else if (name.equalsIgnoreCase("Customize")) {
                                currentItem.setSpecialTag(xrp.nextText());
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