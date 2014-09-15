package com.utkise.TTSProj2;

import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.List;
import java.util.Locale;
import java.util.Stack;

/**
 * Created by Bill on 8/28/14.
 */
public class MyProperties {
    private static MyProperties ourInstance = new MyProperties();

    public LANG Language;
    public TextToSpeech gtts;
    public Vibrator vb;
    public DisableType boarding, traveling, gettingoff, emergency, response, currentType;
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
                title += " > ";
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
        response = null;
        currentType = null;
        titleStack = new Stack<String>();
    }

    public static synchronized MyProperties getInstance(){
        if(null == ourInstance){
            ourInstance = new MyProperties();
        }
        return ourInstance;
    }



    public void doInit(LANG lan) {
        int result = TextToSpeech.ERROR;

        Language = lan;
        if (lan == LANG.ENGLISH) {
            result = gtts.setLanguage(Locale.US);
        } else if (lan==LANG.SPANISH) {
            Locale locSpanish = new Locale("spa", "ESP");
            result = gtts.setLanguage(locSpanish);
        }

        if (result == TextToSpeech.LANG_MISSING_DATA
                || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            Log.e("TTS", "This Language is not supported");
        }

    }

}