package com.utkise.TTSProj2;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Bill on 9/8/14.
 */
public class ItemStruct {
    private HashMap<LANG, String> title;
    private HashMap<LANG, String> text;

    private Integer imageID;
    private List<ItemStruct> child;

    public ItemStruct () {
        title = new HashMap<LANG, String>();
        text = new HashMap<LANG, String>();
        child = null;
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
}
