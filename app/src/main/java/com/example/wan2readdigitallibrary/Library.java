package com.example.wan2readdigitallibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Library extends AppCompatActivity {
    private ImageView imgBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        /*This will display the content of the database onto the library
            We also need to show the book ID in order to know which subject + PDF will be specifically deleted
            duplicate subject values may be observed, dislaying ID will remove that issue
        */

        DBHandler db = new DBHandler(this);

        /*
        ArrayList <HashMap<String, String>> bookList = db.GetBooks();
        ListView lv = (ListView) findViewById(R.id.listFiles);
        ListAdapter adapter = new SimpleAdapter(Library.this, bookList, R.layout.list_row,
                new String[]{"subject","name"},
                new int[]{R.id.bookSubject, R.id.bookName});
        lv.setAdapter(adapter);
        */

        //this block of code is copypasted for all activities to make navbar functionable

        View navbarView = (View) findViewById(R.id.navbar); //retrieve the id in <include>
        imgBtn = (ImageView) navbarView.findViewById(R.id.nav); //retrieve imgBtn from navbar.xml

        //for showing menu items
        PopupMenu popupMenu = new PopupMenu(this, imgBtn);
        popupMenu.getMenuInflater().inflate(R.menu.navitems, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Intent i;
                switch (item.getItemId()) {
                    case R.id.menu_library:
                        i = new Intent(Library.this,Library.class);
                        startActivity(i);
                        break;
                    case R.id.menu_view:
                        i = new Intent(Library.this,View.class);
                        startActivity(i);
                        break;
                    case R.id.menu_add:
                        i = new Intent(Library.this, Add.class);
                        startActivity(i);
                        break;
                    case R.id.menu_credits:
                        i = new Intent(Library.this,Credits.class);
                        startActivity(i);
                        break;
                }
                return true;
            }
        });
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu.show();
            }
        });

//----------------


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, getPdfFiles(Environment.getExternalStorageDirectory()));
        ListView listView = (ListView) findViewById(R.id.listFiles);
        listView.setAdapter(adapter);

       //getAllDir(Environment.getExternalStorageDirectory());//temp code to check external storage (see logcat)
    }
    public void getAllDir(File dir) {
        String pdfPattern = ".pdf";

        File listFile[] = dir.listFiles();

        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    getAllDir(listFile[i]);
                } else {
                    if (listFile[i].getName().endsWith(pdfPattern)){
                        Log.d("List Files",listFile[i].getName());

                    }
                }
            }
        }
        else
            Toast.makeText(getBaseContext(), "aaaaa!",
                    Toast.LENGTH_SHORT).show();
    }
    public List<String> getPdfFiles(File dir){
        String pdfPattern = ".pdf";

        List<String> fileList = new ArrayList<String>();
        File listFile[] = dir.listFiles();

        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    getPdfFiles(listFile[i]);
                } else {
                    if (listFile[i].getName().endsWith(pdfPattern)){
                        fileList.add(listFile[i].getName());
//                        if (listFile != null && listFile.length > 0) {
//
//                            for (File aListFile : listFile) {
//
//                                if (aListFile.isDirectory()) {
//                                    fileList.add(aListFile.getName());
//
//                                } else {
//                                    if (aListFile.isFile())
//                                    {
//                                        fileList.add(aListFile.getName());
//                                    }
//                                }
//                            }
//                        }

                    }
                }
            }
        }
        return fileList;
    }
}