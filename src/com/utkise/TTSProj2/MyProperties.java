package com.utkise.TTSProj2;

import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.ArrayList;
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
    public List<String[]> TITLES;

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

    public String getTitleName(TITLE index) {
        return TITLES.get(index.ordinal())[Language.ordinal()];
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
        initTITLES();
    }

    public static synchronized MyProperties getInstance(){
        if(null == ourInstance){
            ourInstance = new MyProperties();
        }
        return ourInstance;
    }

    private void initTITLES() {
        TITLES = new ArrayList<String[]>();

        // vision, hearing, cognitive, non english
        TITLES.add(new String[]{"vision","visión"});
        TITLES.add(new String[]{"hearing","escuchar"});
        TITLES.add(new String[]{"cognitive","cognitiva"});
        TITLES.add(new String[]{"non english","no Inglés"});

        // boarding, getting off, travelling, emergency
        TITLES.add(new String[]{"boarding","embarque"});
        TITLES.add(new String[]{"getting off","bajar"});
        TITLES.add(new String[]{"travelling","viajar"});
        TITLES.add(new String[]{"emergency","emergencia"});

        // general information, trip information, safety, comfort
        // información general , información de viaje , la seguridad , la comodidad
        TITLES.add(new String[]{"general information","información general"});
        TITLES.add(new String[]{"trip information","información de viaje"});
        TITLES.add(new String[]{"safety","la seguridad"});
        TITLES.add(new String[]{"comfort","la comodidad"});
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