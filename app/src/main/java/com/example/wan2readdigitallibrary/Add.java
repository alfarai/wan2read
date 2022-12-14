package com.example.wan2readdigitallibrary;


import androidx.activity.result.ActivityResultLauncher;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    private ImageView imgBtn,settingsBtn;
    EditText subj, pageNo;
    Button uploadBtn, deleteBtn, addBtn, viewBookmarkBtn;
    DBHandler dbHandler;


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

        //drawables
        subj = findViewById(R.id.subjectedit);
        pageNo = findViewById(R.id.pageNumEdit);
        uploadBtn = findViewById(R.id.uploadbtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        addBtn = findViewById(R.id.addBtn);
        viewBookmarkBtn = findViewById(R.id.viewBookmarkBtn);

        //database
        dbHandler = new DBHandler(this);

        //For upload PDF Files
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

        //For adding Bookmarks
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
               String subjectTxt = subj.getText().toString();
               int pageNumTxt = Integer.parseInt(String.valueOf(pageNo.getText()));

               Boolean checkinsertBook = dbHandler.insertBookDetails(subjectTxt, pageNumTxt);
               if (checkinsertBook == true){
                   Toast.makeText(getApplicationContext(),
                           "Page successfully marked", Toast.LENGTH_SHORT).show();
               }
               else{
                   Toast.makeText(getApplicationContext(),
                           "Page not marked", Toast.LENGTH_SHORT).show();
               }
            }
        });


        //For removal of bookmarks in db
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String subject = String.valueOf(subj.getText());
                DBHandler deletehandler = new DBHandler(Add.this);
                deletehandler.DeleteBook(subject);
                Toast.makeText(getApplicationContext(),
                        "PDF Deleted", Toast.LENGTH_SHORT).show();
            }

        });

        viewBookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Add.this, bookmarkList.class));
            }
        });

        //this block of code is copypasted for all activities to make navbar functionable

        View navbarView = (View) findViewById(R.id.navbar); //retrieve the id in <include>
        imgBtn = (ImageView) navbarView.findViewById(R.id.nav); //retrieve imgBtn from navbar.xml
        settingsBtn = (ImageView)navbarView.findViewById(R.id.settings);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add.this,Settings.class);
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
                        i = new Intent(Add.this, Library.class);
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
                    case R.id.menu_bookmark:
                        i = new Intent(Add.this, bookmarkList.class);
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
    @SuppressLint("Range")
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
    private boolean copyFile(Uri src)throws IOException{

//        if(src.getAbsolutePath().toString().equals(dst.getAbsolutePath().toString())){
//
//            return true;
//
//        }else{
        if(checkPerms(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            File dir = new File(Environment.getExternalStorageDirectory(), getFileName(src));
            InputStream is = getContentResolver().openInputStream(src);
            FileOutputStream os = new FileOutputStream(dir);
            byte[] buff = new byte[1024];
            int len;

            while ((len = is.read(buff)) > 0) {

                os.write(buff, 0, len);
            }
            is.close();
            os.close();
            Toast.makeText(getBaseContext(), "File saved successfully!",
                    Toast.LENGTH_SHORT).show();
            //}
        }
        return true;
    }
    public boolean checkPerms(String perms){
        int check = ContextCompat.checkSelfPermission(this,perms);
        return (check == PackageManager.PERMISSION_GRANTED);
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


//                File source = new File(resultData.getData().toString());


                try{

                    copyFile(resultData.getData());


                }catch(IOException e){
                    e.printStackTrace();
                }


            }
        }
    }
}