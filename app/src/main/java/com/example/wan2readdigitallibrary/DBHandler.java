package com.example.wan2readdigitallibrary;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.*;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_Name = "librarydb";
    private static final String TABLE_books = "librarydetails";
    private static final String KEY_ID = "id";
    private static final String KEY_SECT = "section";
    private static final String KEY_TOP = "topic";
    private static final String KEY_NAME = "name";

    /*
    * Perhaps we save the file destination of the pdf file in the string Name?
    * */


    public DBHandler (Context context) {
        super(context,DB_Name,null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_books + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_SECT + " TEXT,"
                + KEY_TOP + " TEXT,"
                + KEY_NAME + " TEXT"+ ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_books);
        onCreate(db);
    }

    /* CRUD Operations */

    // Add Book
    void insertBookDetails(String section, String topic, String name){
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_SECT, section);
        cValues.put(KEY_TOP, topic);
        cValues.put(KEY_NAME, name);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_books,null, cValues);
        db.close();
    }

    // Get Book Details
    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> GetBooks(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> bookList = new ArrayList<>();
        String query = "SELECT section, topic, name FROM "+
                TABLE_books;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            HashMap<String,String> book = new HashMap<>();
            book.put("section",cursor.getString(cursor.getColumnIndex(KEY_SECT)));
            book.put("topic",cursor.getString(cursor.getColumnIndex(KEY_TOP)));
            book.put("name",cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            bookList.add(book);
        }
        return bookList;
    }

    // Get Book Details based on ID
    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> GetBookbyBookId(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> bookList = new ArrayList<>();
        String query = "SELECT section, topic, name FROM "+
                TABLE_books;
        Cursor cursor = db.query(TABLE_books, new String[]{KEY_SECT, KEY_TOP, KEY_NAME},
                KEY_ID+ "=?",new String[]{String.valueOf(id)},
                null, null, null, null);
        if (cursor.moveToNext()){
            HashMap<String,String> book = new HashMap<>();
            book.put("section",cursor.getString(cursor.getColumnIndex(KEY_SECT)));
            book.put("topic",cursor.getString(cursor.getColumnIndex(KEY_TOP)));
            book.put("name",cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            bookList.add(book); }
        return bookList;
    }

    //Delete Book Details
    public void DeleteBook(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_books, KEY_ID+" = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    // Update Book Details - Implement if necessary
    public int UpdateBookDetails(String section, String topic, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cVals = new ContentValues();
        cVals.put(KEY_SECT, section); cVals.put(KEY_TOP, topic);
        int count = db.update(TABLE_books, cVals, KEY_ID+" = ?",
                new String[]{String.valueOf(id)});
        return count;
    }
}
