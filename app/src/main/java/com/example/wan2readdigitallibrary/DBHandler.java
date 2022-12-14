package com.example.wan2readdigitallibrary;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.*;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 2;
    private static final String DB_Name = "librarydb";
    private static final String TABLE_books = "librarydetails";
    private static final String KEY_ID = "id";
    private static final String KEY_SUBJ = "subject";
    private static final String KEY_PAGE = "page";

    /*
    * Perhaps we save the file destination of the pdf file in the string Name?
    * */


    public DBHandler (Context context) {
        super(context,DB_Name,null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_books + "(" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_SUBJ + " TEXT, " +
                KEY_PAGE + " INTEGER);";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_books);
        onCreate(db);
    }

    /* CRUD Operations */

    // Add Book
    public Boolean insertBookDetails(String subject, int page){
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_PAGE, page);
        cValues.put(KEY_SUBJ, subject);

        long newRowId = db.insert(TABLE_books, null, cValues);
        if (newRowId == -1){
            return false;
        }
        else{
            return true;
        }
    }


    public Cursor getBookDetails(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from " +  TABLE_books, null);
        return cursor;
    }

    //Delete Book Details
    public void DeleteBook(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_books, "id = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

}
