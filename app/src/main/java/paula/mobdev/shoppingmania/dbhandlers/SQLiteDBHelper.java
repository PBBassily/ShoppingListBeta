package paula.mobdev.shoppingmania.dbhandlers;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.util.ArrayList;

import paula.mobdev.shoppingmania.model.Item;


public class SQLiteDBHelper extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "StoryBuilder";
    public static final String ITEM_TABLE = "item";
    public static final String ITEM_ID = "item_id";
    public static final String ITEM_NAME = "name";
    public static final String CATEGORY = "category";
    public static final String ITEM_PRICE = "price";
    public static final String ITEM_CHECKED = "checked";

    //Create story Table Query
    private static final String SQL_CREATE_ITEM =
            "CREATE TABLE ITEM (" + ITEM_ID + "  INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ITEM_NAME + " TEXT NOT NULL," + CATEGORY + "  TEXT NOT NULL, "
                    + ITEM_PRICE + "  REAL NOT NULL, " + ITEM_CHECKED + "  INTEGER NOT NULL);";


    private static final String SQL_DELETE_ITEM =
            "DROP TABLE IF EXISTS " + ITEM_TABLE;

    public SQLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL(FOREIGN);
        db.execSQL(SQL_CREATE_ITEM);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //Drop the table while upgrading the database
        // such as adding new column or changing existing constraint
        db.execSQL(SQL_DELETE_ITEM);

        this.onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.onUpgrade(db, oldVersion, newVersion);
    }

    //Adds a new story
    public boolean addItem(Item item) {

        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        //Create a map having movie details to be inserted
        ContentValues itemDetails = new ContentValues();
        itemDetails.put(ITEM_NAME, item.getName());
        itemDetails.put(CATEGORY, item.getCategory());
        itemDetails.put(ITEM_PRICE, item.getPrice());
        itemDetails.put(ITEM_CHECKED, item.getStatus()==false?0:1);


        long newRowId = db.insert(ITEM_TABLE, null, itemDetails);

        db.close();

        return newRowId != -1;

    }

    //Retrieves details all stories

    public ArrayList<Item> getAllItems() {

        ArrayList <Item> itemList = new ArrayList<Item>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + ITEM_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            do {

                Item item = new Item(
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_ID))),
                        cursor.getString(cursor.getColumnIndex(ITEM_NAME)),
                        cursor.getString(cursor.getColumnIndex(CATEGORY)),
                        cursor.getFloat(cursor.getColumnIndex(ITEM_PRICE)),
                        cursor.getInt(cursor.getColumnIndex(ITEM_CHECKED))==0?false:true);


                // Adding story to list
                itemList.add(item);
            } while (cursor.moveToNext());
        }

        // return story list
        return itemList;

    }
    public void updateItem(Item item) {

        SQLiteDatabase db = this.getWritableDatabase();

        //Create a map having story details to be inserted
        ContentValues itemDetails = new ContentValues();
        itemDetails.put(ITEM_NAME, item.getName());
        itemDetails.put(CATEGORY, item.getCategory());
        itemDetails.put(ITEM_PRICE, item.getPrice());
        itemDetails.put(ITEM_CHECKED, item.getStatus()==false?0:1);
        Log.d("db","item_check_stat:"+itemDetails.getAsString(ITEM_CHECKED));


        db.update(ITEM_TABLE, itemDetails, ITEM_ID + " = ?",
                new String[]{String.valueOf(item.getId())});
    }

    //Deletes the specified story
    public void deleteStory(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " +ITEM_TABLE+ " WHERE "+ITEM_ID+"='"+id+"'");
    }



}

