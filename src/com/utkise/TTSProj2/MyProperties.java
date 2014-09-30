package com.utkise.TTSProj2;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.ImageView;
import org.w3c.dom.Text;

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
    public Stack<AnimationDrawable> animStack;

    public void speakout(String text) {
        gtts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        playAnimation();
    }

    public void playAnimation() {
        AnimationDrawable anim = animStack.peek();

        Log.i("footer_view","anim="+anim);
        if (anim.isRunning()) {
            anim.stop();
        }
        anim.start();
    }

    public void speakAdd(String text) {
        gtts.speak(text, TextToSpeech.QUEUE_ADD, null);
    }

    // speakBoth language if not english
    public void speakBoth(TITLE title) {

        speakout(getTitleName(title));

       /* if (Language != LANG.ENGLISH) {
            speakSilent(700);
            speakAdd(getTitleName(title, LANG.ENGLISH));
        }*/
    }

    // speakBoth language if not english
    public void speakBoth(ItemStruct item) {

        speakout(item.getText(Language));
       /* if (Language != LANG.ENGLISH) {
            speakSilent(700);
            speakAdd(item.getText(LANG.ENGLISH));
        }*/

    }

    public void speakBoth(List<String> words) {

        speakout(words.get(0));

        speakout(words.get(1));
    }

    public void speakSilent(int ms) {
        gtts.playSilence(ms, TextToSpeech.QUEUE_ADD, null);
    }

    public String getTitleStack() {
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

    public String getTitleName(TITLE index, LANG lan) {
        return TITLES.get(index.ordinal())[lan.ordinal()];
    }

    public String getTitleName(TITLE index) {
        return getTitleName(index, Language);
    }

    public String getTitleEither(TITLE index) {
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
        animStack = new Stack<AnimationDrawable>();

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

        /* The sequence of adding the string matters */

        // vision, hearing, cognitive, non english
        TITLES.add(new String[]{"vision","visión"});
        TITLES.add(new String[]{"hearing","escuchar"});
        TITLES.add(new String[]{"cognitive","cognitiva"});
        TITLES.add(new String[]{"english","español"});

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

    public void popStacks() {
        titleStack.pop();
        animStack.pop();
    }
}