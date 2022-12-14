package com.example.wan2readdigitallibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;

public class bookmarkList extends AppCompatActivity {
    private ImageView imgBtn,settingsBtn;
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
        //this block of code is copypasted for all activities to make navbar functionable

        android.view.View navbarView = (android.view.View) findViewById(R.id.navbar); //retrieve the id in <include>
        imgBtn = (ImageView) navbarView.findViewById(R.id.nav); //retrieve imgBtn from navbar.xml
        settingsBtn = (ImageView)navbarView.findViewById(R.id.settings);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(bookmarkList.this,Settings.class);
                startActivity(intent);
            }
        });
        //for showing menu items
        PopupMenu popupMenu = new PopupMenu(this, imgBtn);
        popupMenu.getMenuInflater().inflate(R.menu.navitems, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Intent i;
                switch (item.getItemId()) {
                    case R.id.menu_library:
                        i = new Intent(bookmarkList.this,Library.class);
                        startActivity(i);
                        break;
                    case R.id.menu_add:
                        i = new Intent(bookmarkList.this, Add.class);
                        startActivity(i);
                        break;
                    case R.id.menu_credits:
                        i = new Intent(bookmarkList.this,Credits.class);
                        startActivity(i);
                        break;
                    case R.id.menu_bookmark:
                        i = new Intent(bookmarkList.this, bookmarkList.class);
                        startActivity(i);
                        break;
                }
                return true;
            }
        });
        imgBtn.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu.show();
            }
        });

//----------------
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