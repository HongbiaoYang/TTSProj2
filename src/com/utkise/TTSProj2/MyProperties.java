package com.utkise.TTSProj2;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.ImageView;
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.*;

/**
 * Created by Bill on 8/28/14.
 */
public class MyProperties {
    private static MyProperties ourInstance = new MyProperties();

    public LANG Language;
    public TextToSpeech gtts;
    public Vibrator vb;
    public DisableType gettingonoff, ridingbus, safety, emergency, response, currentType;
    public Stack<String> titleStack;
    public List<String[]> TITLES;
    public Stack<AnimationDrawable> animStack;
    public List<TutorialItem> tutorialListHearing, tutorialListCognitive, tutorialListNonEnglish;
    public HashMap<String, List<TutorialItem>> tutorialLists;
    private List<ItemStruct> flatList;
    private boolean E_merged = false;

    // shut up
    public void shutup() {
        if (gtts.isSpeaking()) {
            gtts.stop();
        }
    }

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
        playAnimation();
    }

    // speakBoth language if not english
    public void speakBoth(TITLE title) {

        // speak only english. Change to Language if need to speak certain chosen language
        speakout(getTitleName(title, LANG.ENGLISH));

       /* if (Language != LANG.ENGLISH) {
            speakSilent(700);
            speakAdd(getTitleName(title, LANG.ENGLISH));
        }*/
    }

    // speakBoth language if not english
    public void speakBoth(ItemStruct item) {

        // speak only english. Change to Language if need to speak certain chosen language
        speakout(item.getText(LANG.ENGLISH));
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
        gettingonoff = null;
        ridingbus = null;
        safety = null;
        emergency = null;
        response = null;
        currentType = null;
        tutorialListHearing = null;
        tutorialListCognitive = null;
        tutorialListNonEnglish = null;
        flatList = new ArrayList<ItemStruct>();
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
        TITLES.add(new String[]{"Vision","Visión"});
        TITLES.add(new String[]{"Hearing","Escuchar"});
        TITLES.add(new String[]{"Cognitive","Cognitiva"});
        TITLES.add(new String[]{"Spanish","Español"});

        // boarding, getting off, travelling, emergency
        TITLES.add(new String[]{"Getting ON","Embarque"});
        TITLES.add(new String[]{"Getting OFF","Bajar"});
        TITLES.add(new String[]{"Travelling","Viajar"});
        TITLES.add(new String[]{"Emergency","Emergencia"});

        // general information, trip information, safety, comfort
        // información general , información de viaje , la seguridad , la comodidad
        TITLES.add(new String[]{"General information","Información general"});
        TITLES.add(new String[]{"Trip information","Información de viaje"});
        TITLES.add(new String[]{"Safety","La seguridad"});
        TITLES.add(new String[]{"Comfort","La comodidad"});
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

        gtts.setSpeechRate(0.85f);

        if (result == TextToSpeech.LANG_MISSING_DATA
                || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            Log.e("TTS", "This Language is not supported");
        }


    }

    public void popStacks() {
            titleStack.pop();
            animStack.pop();
    }

    // the disable type list
    public DisableType[] DisableList() {
        DisableType[] temp = {MyProperties.getInstance().gettingonoff,
                              MyProperties.getInstance().ridingbus,
                              MyProperties.getInstance().safety};

        return temp;
    }

    public void LoadTutorialXml(Context context) {
        tutorialLists = new HashMap<String, List<TutorialItem>>();

        try {
            XmlResourceParser xrp = context.getResources().getXml(R.xml.tutorials);
            xrp.next();
            int eventType = xrp.getEventType();
            boolean done = false;

            List<TutorialItem> currentLevel = new ArrayList<TutorialItem>();
            TutorialItem currentItem = null;


            while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                String name = null;
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        name = xrp.getName();

                        if (name.equalsIgnoreCase("item")) {
                            currentItem = new TutorialItem();
                            currentLevel.add(currentItem);

                        } else if (currentItem != null) {
                            if (name.equalsIgnoreCase("Desc")) {
                                currentItem.desc = xrp.nextText();
                            } else if (name.equalsIgnoreCase("Voice")) {
                                currentItem.voice = xrp.nextText();
                            } else if (name.equalsIgnoreCase("image")) {
                                int resID = context.getResources().getIdentifier(xrp.nextText(), "drawable", context.getPackageName());
                                currentItem.image = resID;
                            } else if (name.equalsIgnoreCase("progress")) {
                                int resID = context.getResources().getIdentifier(xrp.nextText(), "drawable", context.getPackageName());
                                currentItem.progress = resID;
                            }else if (name.equalsIgnoreCase("Customize")) {
                                currentItem.custom = xrp.nextText();
                            }

                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = xrp.getName();
                        if (name.equalsIgnoreCase("item")) {
                                currentItem = null;
                        } else if (name.equalsIgnoreCase("Hearing") || name.equalsIgnoreCase("Cognitive") ||
                                name.equalsIgnoreCase("NonEnglish")) {

                            tutorialLists.put(name, currentLevel);
                            currentLevel = new ArrayList<TutorialItem>();
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

    public List<TutorialItem> getTutorial(String type) {
        if (tutorialLists == null) {
            return null;
        }

        return tutorialLists.get(type);
    }

/*    public List<ItemStruct> getCognitiveList() {
        List<ItemStruct> flat = new ArrayList<ItemStruct>();
        flat.addAll(flattenTreeToList(boarding));
        flat.addAll(flattenTreeToList(traveling));
        flat.addAll(flattenTreeToList(gettingoff));
        flat.addAll(flattenTreeToList(emergency));

        return flat;
    }

    private List<ItemStruct> flattenTreeToList(DisableType type) {

        return null;
    }*/

    public void feedItem(ItemStruct currentItem) {
        if (currentItem.getChild() != null) {
            return;
        }

        ItemStruct tmp = new ItemStruct();
        tmp.setText(currentItem.getText());
        tmp.setTitle(currentItem.getTitle());
        tmp.setImageID(currentItem.getImageID());
        tmp.setColor(currentItem.getColorCode());

        flatList.add(tmp);
    }

    public List<ItemStruct> getCognitiveList() {

        if (E_merged == false) {
            flatList.addAll(0, emergency.getEmergency());
            E_merged = true;
        }
        return flatList;
    }

    public void clearStacks() {
        animStack.clear();
        titleStack.clear();
    }

}