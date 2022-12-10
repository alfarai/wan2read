package com.example.wan2readdigitallibrary;


import androidx.activity.result.ActivityResultLauncher;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.OpenableColumns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Add extends AppCompatActivity {
    public static final int PICK_FILE = 99;

    private ImageView imgBtn;


    private void openFile(Uri pickerInitialUri) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");

        // Optionally, specify a URI for the file that should appear in the
        // system file picker when it loads.
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

        startActivityForResult(intent, PICK_FILE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Button uploadBtn = findViewById(R.id.uploadbtn);

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                openFile(null);
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/pdf");
                startActivityForResult(intent, PICK_FILE);
            }
        });
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
                        i = new Intent(Add.this, Library.class);
                        startActivity(i);
                        break;
                    case R.id.menu_view:
                        i = new Intent(Add.this, View.class);
                        startActivity(i);
                        break;
                    case R.id.menu_add:
                        i = new Intent(Add.this, Add.class);
                        startActivity(i);
                        break;
                    case R.id.menu_credits:
                        i = new Intent(Add.this, Credits.class);
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

    }
    private void copyFileUsingStream(File source) throws IOException {
        FileInputStream is = null;
        FileOutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = openFileOutput("test.pdf",MODE_PRIVATE);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }

        } finally {
            if (is != null){
                is.close();
            }
            if (os != null){
                os.close();
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData){
        super.onActivityResult(requestCode,resultCode,resultData);
        if (requestCode == PICK_FILE && resultCode == RESULT_OK) {
            if(resultData != null){
                //Toast.makeText(getApplicationContext(),String.valueOf(resultData.getData()),Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), "PDF Uploaded!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                //intent.putExtra(extra_PDF_FILE_URI,String.valueOf(resultData.getData()));


                File source = new File(String.valueOf(resultData.getData()));


                try{

                    copyFileUsingStream(source);
                    Toast.makeText(getBaseContext(), "File saved successfully!",
                            Toast.LENGTH_SHORT).show();

                }catch(IOException e){
                    e.printStackTrace();
                }


            }
        }
    }
}