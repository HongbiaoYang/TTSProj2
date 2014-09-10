package com.utkise.TTSProj2;

import android.os.Vibrator;
import android.speech.tts.TextToSpeech;

import java.util.List;
import java.util.Stack;

/**
 * Created by Bill on 8/28/14.
 */
public class MyProperties {
    private static MyProperties ourInstance = new MyProperties();

    public LANG Language;
    public TextToSpeech gtts;
    public Vibrator vb;
    public DisableType boarding, traveling, gettingoff, emergency, currentType;
    public Stack<String> titleStack;

    public void speakout(String text) {
        gtts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    public String getTitle() {
        int i;
        String title = "";
        for (i = 0; i < titleStack.size(); i ++) {
            title += titleStack.get(i);
            if (i < titleStack.size() - 1) {
                title += "->";
            }
        }

        return title;
    }

    private MyProperties() {
        Language = LANG.ENGLISH;
        boarding = null;
        traveling = null;
        gettingoff = null;
        emergency = null;
        currentType = null;
        titleStack = new Stack<String>();
    }

    public static synchronized MyProperties getInstance(){
        if(null == ourInstance){
            ourInstance = new MyProperties();
        }
        return ourInstance;
    }


}