package com.utkise.TTSProj2;

import android.os.Vibrator;
import android.speech.tts.TextToSpeech;

import java.util.List;

/**
 * Created by Bill on 8/28/14.
 */
public class MyProperties {
    private static MyProperties ourInstance = new MyProperties();

    public LANG Language;
    public TextToSpeech gtts;
    public Vibrator vb;
    public DisableType boarding, traveling, gettingoff, currentType;

    public void speakout(String text) {
        gtts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    private MyProperties() {
        Language = LANG.ENGLISH;
        boarding = null;
        traveling = null;
        gettingoff = null;
        currentType = null;
    }

    public static synchronized MyProperties getInstance(){
        if(null == ourInstance){
            ourInstance = new MyProperties();
        }
        return ourInstance;
    }


}