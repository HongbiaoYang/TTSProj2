package com.utkise.TTSProj2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hongbiaoyang on 10/10/15.
 */
public class DatabaseHandler extends SQLiteOpenHelper{

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ItemsManager";

    // Contacts table name
    private static final String TABLE_ITEMS = "ItemTable";
    private static final String TABLE_PARA = "ParaTable";
    private static final String TABLE_FIXED = "FixedTable";
    private static final String TABLE_PROPS = "PropTable";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "Title";
    private static final String KEY_TEXT = "Text";
    private static final String KEY_TITULO = "Titulo";
    private static final String KEY_TEXTO = "Texto";
    private static final String KEY_IMAGE = "Image";
    private static final String KEY_IMAGEV = "ImageV";
    private static final String KEY_COLOR = "Color";
    private static final String KEY_FREQ = "Freq";
    private static final String KEY_CUSTOMIZE = "Customize";

    private static final String KEY_FREQ_HEARING = "hearing";
    private static final String KEY_FREQ_COGNITIVE = "cognitive";
    private static final String KEY_FREQ_NONENGLISH = "nonEnglish";
    private static final String KEY_FREQ_VISION = "vision";

    private static final String KEY_MENU = "Menu";

    private static final String KEY_NAME = "Prop_Name";
    private static final String KEY_VALUE = "Prop_Value";


    public boolean firstTime = false;
    private SQLiteDatabase db;
    private String TAG = "DatabaseHandler";


    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHandler(Context context) {
        super(context, CONSTANT.DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // CREATE table properties
        String CREATE_PROPS_TABLE = "CREATE TABLE " + TABLE_PROPS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_VALUE + " TEXT"  + ")";
        db.execSQL(CREATE_PROPS_TABLE);

        // create table items
        String CREATE_PARA_TABLE = "CREATE TABLE " + TABLE_PARA + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
                + KEY_TEXT + " TEXT," + KEY_TITULO + " TEXT," + KEY_TEXTO + " TEXT,"
                + KEY_IMAGE + " TEXT," + KEY_IMAGEV + " TEXT," + KEY_COLOR + " TEXT, " + KEY_CUSTOMIZE + " TEXT,"
                + KEY_FREQ_HEARING + " INTEGER,"+ KEY_FREQ_COGNITIVE + " INTEGER,"+ KEY_FREQ_NONENGLISH + " INTEGER,"+ KEY_FREQ_VISION + " INTEGER,"
                + KEY_MENU + " TEXT" +  ")";
        db.execSQL(CREATE_PARA_TABLE);

        // create table items
        String CREATE_FIXED_TABLE = "CREATE TABLE " + TABLE_FIXED + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
                + KEY_TEXT + " TEXT," + KEY_TITULO + " TEXT," + KEY_TEXTO + " TEXT,"
                + KEY_IMAGE + " TEXT," + KEY_IMAGEV + " TEXT," + KEY_COLOR + " TEXT, " + KEY_CUSTOMIZE + " TEXT,"
                + KEY_FREQ_HEARING + " INTEGER,"+ KEY_FREQ_COGNITIVE + " INTEGER,"+ KEY_FREQ_NONENGLISH + " INTEGER,"+ KEY_FREQ_VISION + " INTEGER,"
                + KEY_MENU +  " TEXT" + ")";
        db.execSQL(CREATE_FIXED_TABLE);

        this.firstTime = true;
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FIXED);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROPS);

        // Create tables again
        onCreate(db);
    }

    public boolean databaseExist() {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db == null) {
            return false;
        } else {
            return true;
        }
    }

    void addProp(String name, String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_VALUE, value);

        db.insert(TABLE_PROPS, null, values);
        db.close();

    }

    String getProp(String name) {
        String selectQuery = "SELECT  * FROM " + TABLE_PROPS + " where " + KEY_NAME + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{name});

        String value = null;

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
              value = cursor.getString(2);
            } while (cursor.moveToNext());
        }

        return value;
    }

    // Adding new item
    void addItem(ItemStruct itemStruct, String menu, int transitType) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, itemStruct.getTitle(LANG.ENGLISH)); // English title
        values.put(KEY_TEXT, itemStruct.getText(LANG.ENGLISH)); // English Text
        values.put(KEY_TITULO, itemStruct.getTitle(LANG.SPANISH)); // Spanish title
        values.put(KEY_TEXTO, itemStruct.getText(LANG.SPANISH));    // Spanish Text
        values.put(KEY_IMAGE, itemStruct.getImageID());
        values.put(KEY_IMAGEV, itemStruct.getVImageID());
        values.put(KEY_COLOR, itemStruct.getColorCode());
        values.put(KEY_CUSTOMIZE, itemStruct.getSpecialTag());
        values.put(KEY_FREQ_HEARING, itemStruct.getFreq("hearing"));    // 0 frequency at first for hearing
        values.put(KEY_FREQ_COGNITIVE, itemStruct.getFreq("cognitive"));    // 0 frequency at first for cognitive
        values.put(KEY_FREQ_NONENGLISH, itemStruct.getFreq("nonenglish"));    // 0 frequency at first for non English
        values.put(KEY_FREQ_VISION, itemStruct.getFreq("vision"));    // 0 frequency at first for vision

        values.put(KEY_MENU, menu);


        // choose table based on transit type
        String table = transitType == CONSTANT.PARA ? TABLE_PARA : TABLE_FIXED;

        // Inserting Row
        db.insert(table, null, values);

        db.close(); // Closing database connection
    }

    // Getting All Contacts
    public List<ItemStruct> getAllItems(int transitType, String... otherArgs) {
        List<ItemStruct> itemList = new ArrayList<ItemStruct>();

        Cursor cursor;

        // choose table based on transit type
        String table = transitType == CONSTANT.PARA ? TABLE_PARA : TABLE_FIXED;

        // Select All Query

        if (otherArgs.length == 0) {
            String selectQuery = "SELECT  * FROM " + table ;
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);
        } else {
            String selectQuery = "SELECT  * FROM " + table + " where ";
            List<String> valueList = new ArrayList<String>();

            for (int i = 0; i < otherArgs.length; i+= 2) {
                selectQuery += otherArgs[i] + "=? AND ";
                valueList.add(otherArgs[i+1]);
            }
            selectQuery += " 1";
            String[] values = new String[valueList.size()];
            valueList.toArray(values);

            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, values);
        }

     /*   if (menu == "") {
            String selectQuery = "SELECT  * FROM " + table ;
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);
        } else {
            String selectQuery = "SELECT  * FROM " + table + " where " + KEY_MENU + " = ?";
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{menu});
        }*/

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ItemStruct aItem = new ItemStruct();

                aItem.setTitle(LANG.ENGLISH, cursor.getString(1));
                aItem.setText(LANG.ENGLISH, cursor.getString(2));
                aItem.setTitle(LANG.SPANISH, cursor.getString(3));
                aItem.setText(LANG.SPANISH, cursor.getString(4));
//                aItem.setImageString(cursor.getString(5));
//                aItem.setvImageString(cursor.getString(6));
                aItem.setImageID(Integer.parseInt(cursor.getString(5)));
                aItem.setVImageID(Integer.parseInt(cursor.getString(6)));
                aItem.setColor(Integer.parseInt(cursor.getString(7)));
                aItem.setSpecialTag(cursor.getString(8));
                aItem.setFreq("hearing", Integer.parseInt(cursor.getString(9)));
                aItem.setFreq("cognitive", Integer.parseInt(cursor.getString(10)));
                aItem.setFreq("nonenglish", Integer.parseInt(cursor.getString(11)));
                aItem.setFreq("vision", Integer.parseInt(cursor.getString(12)));

                // Adding contact to list
                itemList.add(aItem);
            } while (cursor.moveToNext());
        }

        // return contact list
        return itemList;
    }



    // Updating single item
    public int updateItem(String subMenu, ItemStruct item, int transitType) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(subMenu, item.getFreq(subMenu));

        // choose table based on transit type
        String table = transitType == CONSTANT.PARA ? TABLE_PARA : TABLE_FIXED;

        // updating row
        return db.update(table, values, KEY_TITLE + " = ?",
                new String[] { String.valueOf(item.getTitle())});
    }

    // Deleting single item
    public void deleteContact(ItemStruct item, int transitType) {
        SQLiteDatabase db = this.getWritableDatabase();

        // choose table based on transit type
        String table = transitType == CONSTANT.PARA ? TABLE_PARA : TABLE_FIXED;

        db.delete(table, KEY_TITLE + " = ?",
                new String[] { String.valueOf(item.getTitle()) });
        db.close();
    }

    public int getMaxFreq(String menu, int transitType) {

        Cursor cursor;
        int count = 0;

        // choose table based on transit type
        String table = transitType == CONSTANT.PARA ? TABLE_PARA : TABLE_FIXED;

        String selectQuery = "SELECT MAX("+KEY_FREQ_HEARING+") FROM " + table + " where " + KEY_MENU + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.rawQuery(selectQuery, new String[]{menu});
        if (cursor.moveToFirst()) {
            count = Integer.parseInt(cursor.getString(0));
        }

        return count;
    }

/*
    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ITEMS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }*/

}
