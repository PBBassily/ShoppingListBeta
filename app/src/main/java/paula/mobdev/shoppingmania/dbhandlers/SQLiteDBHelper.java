package paula.mobdev.shoppingmania.dbhandlers;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import paula.mobdev.shoppingmania.model.Item;


public class SQLiteDBHelper extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Items";
    public static final String ITEM_TABLE = "item_table";
    public static final String ITEM_ID = "item_id";
    public static final String ITEM_NAME = "item_name";
    public static final String ITEM_CATEGORY = "item_category";
    public static final String ITEM_PRICE = "item_price";
    public static final String ITEM_STATUS = "item_status";

    //Create story Table Query
    private static final String SQL_CREATE_ITEM =
            "CREATE TABLE ITEM (" + ITEM_ID + "  INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ITEM_NAME + " TEXT NOT NULL," + ITEM_CATEGORY + "  TEXT NOT NULL, "
                    + ITEM_PRICE + "  REAL NOT NULL, " + ITEM_STATUS+ "  INTEGER NOT NULL);";


    protected static final String SQL_DELETE_ITEM =
            "DROP TABLE IF EXISTS " + ITEM_TABLE;

    protected SQLiteDBHelper(Context context) {
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

    //Adds a new item
    protected boolean addItem(Item item) {

        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        //Create a map having movie details to be inserted
        ContentValues itemDetails = new ContentValues();
        itemDetails.put(ITEM_NAME, item.getName());
        itemDetails.put(ITEM_CATEGORY, item.getCategory());
        itemDetails.put(ITEM_PRICE, item.getPrice());
        itemDetails.put(ITEM_STATUS, item.getStatus()==false?0:1);


        long newRowId = db.insert(ITEM_TABLE, null, itemDetails);

        db.close();

        return newRowId != -1;

    }

    //Retrieves details all stories

    protected ArrayList<Item> getAllItems() {

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
                        cursor.getString(cursor.getColumnIndex(ITEM_CATEGORY)),
                        cursor.getFloat(cursor.getColumnIndex(ITEM_PRICE)),
                        cursor.getInt(cursor.getColumnIndex(ITEM_STATUS))==0?false:true);


                // Adding story to list
                itemList.add(item);
            } while (cursor.moveToNext());
        }

        // return story list
        return itemList;

    }
    protected void updateItem(Item item) {

        SQLiteDatabase db = this.getWritableDatabase();

        //Create a map having story details to be inserted
        ContentValues itemDetails = new ContentValues();
        itemDetails.put(ITEM_NAME, item.getName());
        itemDetails.put(ITEM_CATEGORY, item.getStatus());
        itemDetails.put(ITEM_PRICE, item.getPrice());
        itemDetails.put(ITEM_STATUS, item.getStatus()==false?0:1);



        db.update(ITEM_TABLE, itemDetails, ITEM_ID + " = ?",
                new String[]{String.valueOf(item.getId())});
    }

    //Deletes the specified story
    protected void deleteStory(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " +ITEM_TABLE+ " WHERE "+ITEM_ID+"='"+id+"'");
    }

}

