package com.utkise.TTSProj2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    private static final String KEY_FREQ_HEARING = "hearing";
    private static final String KEY_FREQ_COGNITIVE = "cognitive";
    private static final String KEY_FREQ_NONENGLISH = "nonEnglish";
    private static final String KEY_FREQ_VISION = "vision";

    private static final String KEY_MENU = "Menu";

    private static final String KEY_NAME = "Prop_Name";
    private static final String KEY_VALUE = "Prop_Value";


    public boolean firstTime = false;
    private SQLiteDatabase db;


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
        String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
                + KEY_TEXT + " TEXT," + KEY_TITULO + " TEXT," + KEY_TEXTO + " TEXT,"
                + KEY_IMAGE + " TEXT," + KEY_IMAGEV + " TEXT," + KEY_COLOR + " TEXT,"
                + KEY_FREQ_HEARING + " INTEGER,"+ KEY_FREQ_COGNITIVE + " INTEGER,"+ KEY_FREQ_NONENGLISH + " INTEGER,"+ KEY_FREQ_VISION + " INTEGER,"
                + KEY_MENU + " TEXT " + ")";
        db.execSQL(CREATE_ITEMS_TABLE);

        this.firstTime = true;
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);

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
    void addItem(ItemStruct itemStruct, String menu) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, itemStruct.getTitle(LANG.ENGLISH)); // English title
        values.put(KEY_TEXT, itemStruct.getText(LANG.ENGLISH)); // English Text
        values.put(KEY_TITULO, itemStruct.getTitle(LANG.SPANISH)); // Spanish title
        values.put(KEY_TEXTO, itemStruct.getText(LANG.SPANISH));    // Spanish Text
        values.put(KEY_IMAGE, itemStruct.getImageString());
        values.put(KEY_IMAGEV, itemStruct.getvImageString());
        values.put(KEY_COLOR, itemStruct.getColorCode());
        values.put(KEY_FREQ_HEARING, 0);    // 0 frequency at first for hearing
        values.put(KEY_FREQ_COGNITIVE, 0);    // 0 frequency at first for cognitive
        values.put(KEY_FREQ_NONENGLISH, 0);    // 0 frequency at first for non English
        values.put(KEY_FREQ_VISION, 0);    // 0 frequency at first for vision

        values.put(KEY_MENU, menu);

        // Inserting Row
        db.insert(TABLE_ITEMS, null, values);
        db.close(); // Closing database connection
    }

    // Getting All Contacts
    public List<ItemStruct> getAllItems(String menu) {
        List<ItemStruct> itemList = new ArrayList<ItemStruct>();

        Cursor cursor;

        // Select All Query
        if (menu == "") {
            String selectQuery = "SELECT  * FROM " + TABLE_ITEMS ;
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);
        } else {
            String selectQuery = "SELECT  * FROM " + TABLE_ITEMS + " where " + KEY_MENU + " = ?";
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{menu});
        }

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ItemStruct aItem = new ItemStruct();

                aItem.setTitle(LANG.ENGLISH, cursor.getString(1));
                aItem.setText(LANG.ENGLISH, cursor.getString(2));
                aItem.setTitle(LANG.SPANISH, cursor.getString(3));
                aItem.setText(LANG.SPANISH, cursor.getString(4));
                aItem.setImageString(cursor.getString(5));
                aItem.setvImageString(cursor.getString(6));
                aItem.setColor(Integer.parseInt(cursor.getString(7)));
                aItem.setFreq("hearing", Integer.parseInt(cursor.getString(8)));
                aItem.setFreq("cognitive", Integer.parseInt(cursor.getString(9)));
                aItem.setFreq("nonenglish", Integer.parseInt(cursor.getString(10)));
                aItem.setFreq("vision", Integer.parseInt(cursor.getString(11)));

                // Adding contact to list
                itemList.add(aItem);
            } while (cursor.moveToNext());
        }

        // return contact list
        return itemList;
    }



    // Updating single item
    public int updateItem(String subMenu, ItemStruct item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(subMenu, item.getFreq(subMenu));

        // updating row
        return db.update(TABLE_ITEMS, values, KEY_TITLE + " = ?",
                new String[] { String.valueOf(item.getTitle())});
    }

    // Deleting single item
    public void deleteContact(ItemStruct item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS, KEY_TITLE + " = ?",
                new String[] { String.valueOf(item.getTitle()) });
        db.close();
    }


    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ITEMS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}
