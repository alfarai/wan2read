package com.example.wan2readdigitallibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class bookmarkList extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<String> id, subject, page;
    DBHandler db;
    bookmarkAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);
        db = new DBHandler(this);
        id = new ArrayList<>();
        subject = new ArrayList<>();
        page = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview);
        adapter = new bookmarkAdapter(this, id, subject,page);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displaydata();
    }

    private void displaydata() {
        Cursor cursor = db.getBookDetails();
        if (cursor.getCount()==0){
            Toast.makeText(this, "No Bookmarks", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            while (cursor.moveToNext()){
                id.add(cursor.getString(0));
                subject.add(cursor.getString(1));
                page.add(cursor.getString(2));
            }
        }
    }
}