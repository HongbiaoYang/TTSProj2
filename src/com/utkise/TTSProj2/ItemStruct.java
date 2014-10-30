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
    private int VImageID;

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
        if (lan == LANG.SPANISH) {
            String tmp = title.get(LANG.SPANISH) + "/"+title.get(LANG.ENGLISH);
            return tmp;
        } else {
            return title.get(lan);
        }
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

    public void setVImageID(int VImageID) {
        this.VImageID = VImageID;
    }

    public int getVImageID() {
        return VImageID;
    }

    public String getTitle() {
        return getTitle(LANG.ENGLISH);
    }

    public boolean hasChildren() {
        if (child == null) {
            return false;
        } else if (child.size() == 0) {
            return false;
        }

        return true;
    }

    public void setText(String text) {
        setText(LANG.ENGLISH, text);
    }

    public void setTitle(String title) {
        setTitle(LANG.ENGLISH, title);
    }
}
