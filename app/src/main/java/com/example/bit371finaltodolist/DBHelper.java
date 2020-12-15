package com.example.bit371finaltodolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "ToDoDB";
    public static final int VERSION = 1;
    public static final String TABLE_NAME = "Items";
    public static final String ID_COL = "_id";
    public static final String TITLE_COL = "title";
    public static final String DATE_COL = "date";
    public static final String DONE_COL = "done";

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TITLE_COL + " TEXT," +
                DATE_COL + " TEXT," +
                DONE_COL + " INTEGER)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public ArrayList<Item> getItems() {
        // Fills cursor with all items in TABLE, and their info from an array of Columns
        Cursor cursor = getReadableDatabase().query(TABLE_NAME, new String[]{ID_COL, TITLE_COL, DATE_COL, DONE_COL},
                null, null, null, null, null);
        ArrayList<Item> items = new ArrayList<>();

        //moveToFirst returns FALSE if the cursor is empty
        if(cursor.moveToFirst()) {
            do {
                Item item = new Item(
                        cursor.getInt(cursor.getColumnIndex(ID_COL)),
                        cursor.getString(cursor.getColumnIndex(TITLE_COL)),
                        cursor.getString(cursor.getColumnIndex(DATE_COL)),
                        cursor.getInt(cursor.getColumnIndex(DONE_COL)) == 1 // Checks value of Done column
                );
                items.add(item); // Adds the new Item to the Array
            } while (cursor.moveToNext()); // Returns FALSE if the cursor has passed the last entry
        }
        cursor.close();
        return items;
    }

    public void setDone(int id, boolean done) {
        ContentValues cv = new ContentValues();

        // Shows where to put it, and what to put there based on passed values
        cv.put(DONE_COL, done);

        // Updates the table/columns in TABLE/CV where the ID matches the ID that was passed
        getWritableDatabase().update(TABLE_NAME, cv, "_id=?", new String[]{Integer.toString(id)});
    }

    public int insertItem(String title, String date) {
        ContentValues cv = new ContentValues();

        // Stores passed title&date in the CV item.
        cv.put(TITLE_COL, title);
        cv.put(DATE_COL, date);

        // Default state is Not Done
        cv.put(DONE_COL, false);

        // INSERTS stored CV data to the specified TABLE,
        // returning the row ID of the newly inserted row,
        // or -1 if an error occurred
        return (int) getWritableDatabase().insert(TABLE_NAME, null, cv);
    }

    public boolean deleteItem(int id) {
        // Delete returns the number of rows deleted
        // This checks that value against 0
        // If any rows are deleted, this will return TRUE
        // Otherwise, this will return FALSE
        return getWritableDatabase().delete(TABLE_NAME, "_id=?", new String[]{Integer.toString(id)}) > 0;
    }
}
