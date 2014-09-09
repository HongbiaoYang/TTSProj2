package com.utkise.TTSProj2;

import java.util.List;

/**
 * Created by Bill on 9/8/14.
 */
public class ItemStruct {
    String title;
    String text;
    Integer imageID;
    List<ItemStruct> child;

    public ItemStruct () {
        child = null;
    }

    public ItemStruct(String title, String text, Integer imageID) {
        this.title = title;
        this.text = text;
        this.imageID = imageID;
        this.child = null;
    }
}
