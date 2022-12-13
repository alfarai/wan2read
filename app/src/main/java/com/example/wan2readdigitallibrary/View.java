package com.example.wan2readdigitallibrary;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.FileNotFoundException;
import java.io.IOException;

public class View extends AppCompatActivity {
    public static final int PICK_FILE = 99;
    /*
    *   https://stackoverflow.com/questions/48947034/i-want-to-open-pdf-file-in-my-application
    *   Will this do for this type of application?
    * */
    ImageView img;
    PdfRenderer renderer;
    int display_page = 0;
    int total_pages = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        Button prev = findViewById(R.id.prev);
        Button next = findViewById(R.id.next);
        Bundle mainData = getIntent().getExtras();
        if(mainData == null)
            return;
        Uri uri = Uri.parse(mainData.getString("fileUri"));

        try {
            ParcelFileDescriptor parcelFileDescriptor = getContentResolver()
                    .openFileDescriptor(uri, "r");
            renderer = new PdfRenderer(parcelFileDescriptor);
            total_pages = renderer.getPageCount();

            display_page = 0;
            _display(display_page);
        } catch (FileNotFoundException fnfe) {

        } catch (IOException e) {

        }
        prev.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                if (display_page > 0) {
                    display_page--;
                    _display(display_page);
                }
            }
        });
        next.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                if (display_page < (total_pages - 1)) {
                    display_page++;
                    _display(display_page);
                }
            }
        });
    }
    private void _display(int _n) {
        img = findViewById(R.id.imageView3);
        if (renderer != null) {
            PdfRenderer.Page page = renderer.openPage(_n);
            Bitmap mBitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
            page.render(mBitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            img.setImageBitmap(mBitmap);
            page.close();

        }
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.d("test","aaaaa");
//        if (requestCode == PICK_FILE && resultCode == RESULT_OK) {
//            if (data != null) {
//                Bundle mainData = getIntent().getExtras();
//                if(mainData == null)
//                    return;
//                Uri uri = Uri.parse(mainData.getString("fileUri"));
//
//                try {
//                    ParcelFileDescriptor parcelFileDescriptor = getContentResolver()
//                            .openFileDescriptor(uri, "r");
//                    renderer = new PdfRenderer(parcelFileDescriptor);
//                    total_pages = renderer.getPageCount();
//
//                    display_page = 0;
//                    _display(display_page);
//                } catch (FileNotFoundException fnfe) {
//
//                } catch (IOException e) {
//
//                }
//            }
//        }
//
//    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (renderer != null){
            renderer.close();
        }
    }
}