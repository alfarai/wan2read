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
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Library extends AppCompatActivity {
    private ImageView imgBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
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
        List<String> fileList = new ArrayList<String>();

        File mydir = this.getDir("Myfolder", Context.MODE_PRIVATE);
        File listFile[] = mydir.listFiles();

        if (listFile != null && listFile.length > 0) {

            for (File aListFile : listFile) {

                if (aListFile.isDirectory()) {
                    fileList.add(aListFile.getName());

                } else {
                    if (aListFile.isFile()) ;
                    {
                        fileList.add(aListFile.getName());
                    }
                }
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, fileList);
        ListView listView = (ListView) findViewById(R.id.listFiles);
        listView.setAdapter(adapter);

       getAllDir(Environment.getExternalStorageDirectory());//temp code to check external storage (see logcat)
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
}