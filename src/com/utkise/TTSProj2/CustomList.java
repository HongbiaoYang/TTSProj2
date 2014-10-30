package com.utkise.TTSProj2;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Bill on 9/08/14.
 */
public class CustomList extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] web;
    private final Integer[] imageId;

    private int textStyle, backgroundColor;
    private int xmlayout;
    private boolean colorful = false;
    private ArrayList<Integer> colorArray;

    public CustomList(Activity context, String[] web, Integer[] imageId) {
        super(context, R.layout.list_single, web);

        this.context = context;
        this.web = web;
        this.imageId = imageId;
        xmlayout = R.layout.list_single;
    }

    public CustomList(Activity context, String[] web, Integer[] imageId, int xml) {
        super(context, R.layout.list_single, web);

        this.context = context;
        this.web = web;
        this.imageId = imageId;
        xmlayout = xml;

    }

    public void setColors(boolean colorful) {
        this.colorful = colorful;
        colorArray = new ArrayList<Integer>();
        colorArray.add(Color.BLUE);
        colorArray.add(Color.RED);
        colorArray.add(Color.YELLOW);
        colorArray.add(Color.GREEN);
        colorArray.add(Color.CYAN);
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(xmlayout, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        txtTitle.setText(web[position]);
        imageView.setImageResource(imageId[position]);




        if (colorful == true) {

            final Random rand = new Random();
            int dice = rand.nextInt(colorArray.size());
            dice = position % colorArray.size();

            rowView.setBackgroundColor(colorArray.get(dice));
          /*  txtTitle.setTextAppearance(context, textStyle);
            txtTitle.setBackgroundResource(backgroundColor);
            imageView.setBackgroundResource(backgroundColor);*/
        }

        return rowView;
    }
}