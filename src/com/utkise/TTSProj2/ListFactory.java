package com.utkise.TTSProj2;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bill on 9/8/14.
 */
public class ListFactory {
    private final List<ItemStruct> root;

    public ListFactory(List<ItemStruct> root) {

        this.root = root;
    }

    public String[] produceTitleArray() {
        int i;

        ArrayList<String> titleList = new ArrayList<String>();
        for (i = 0; i < this.root.size(); i++) {
            titleList.add(root.get(i).title);
        }
        return titleList.toArray(new String[0]);
    }

    public String[] produceTextArray() {
        int i;

        ArrayList<String> textList = new ArrayList<String>();

        for (i = 0; i < this.root.size(); i++) {
            textList.add(root.get(i).text);
        }

        return textList.toArray(new String[0]);
    }

    public Integer[] produceImageArray() {
        int i;

        ArrayList<Integer> imageList = new ArrayList<Integer>();
        for (i = 0; i < this.root.size(); i++) {
            imageList.add(root.get(i).imageID);
        }

        return imageList.toArray(new Integer[0]);
    }
}
