package com.utkise.TTSProj2;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Bill on 9/8/14.
 */
public class ItemStruct {
    private HashMap<LANG, String> title;
    private HashMap<LANG, String> text;
    private String SpecialTag;

    private Integer imageID;
    private List<ItemStruct> child;

    public String getSpecialTag() {
        return SpecialTag;
    }

    public void setSpecialTag(String specialTag) {
        SpecialTag = specialTag;
    }

    public ItemStruct () {
        title = new HashMap<LANG, String>();
        text = new HashMap<LANG, String>();
        SpecialTag = null;

        child = null;
    }

    public ItemStruct(Integer image, String content) {
        this.imageID = image;
        this.title = new HashMap<LANG, String>();
        this.setTitle(LANG.ENGLISH, content);
        this.text = new HashMap<LANG, String>();
        this.setText(LANG.ENGLISH, content);
    }

    public String getText(LANG lan) {
        return text.get(lan);
    }

    public String getTitle(LANG lan) {
        return title.get(lan);
    }

    public void setText(LANG lan, String content) {
        text.put(lan, content);
    }

    public void setTitle(LANG lan, String content) {
        title.put(lan, content);
    }

    public void setImageID (Integer img) {
        imageID = img;
    }

    public Integer getImageID() {
        return imageID;
    }

    public void setChild(List<ItemStruct> child) {
        this.child = child;
    }

    public List<ItemStruct> getChild() {
        return child;
    }

    // default text
    public String getText() {
        return getText(LANG.ENGLISH);
    }
}
