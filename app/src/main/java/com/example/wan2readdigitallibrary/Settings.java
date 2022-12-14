package com.example.wan2readdigitallibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Switch;

public class Settings extends AppCompatActivity{

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private Switch switchBtnDark,switchBtnOther,switchBtnOther2;
    private LinearLayout layout;

    private ImageView imgBtn,settingsBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        /*Intializes the drop down menu as the font size button
        *
        * Reference for drop down menu: https://developer.android.com/develop/ui/views/components/spinner
        *
        *
        * */


        switchBtnDark = findViewById(R.id.btnDarkMode);
        layout = findViewById(R.id.layout);

        pref = getSharedPreferences("com.example.wan2readdigitallibrary", Context.MODE_PRIVATE);
        editor = pref.edit();

        checkSharedPreferences(); //call this method to set preferences when opening activity

        switchBtnDark.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) { //if button is clicked, save its status in preferences

                if(switchBtnDark.isChecked()){
                    editor.putBoolean(getString(R.string.switchTheme),true);
                    editor.commit();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                else{
                    editor.putBoolean(getString(R.string.switchTheme),false);
                    editor.commit();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });


//this block of code is copypasted for all activities to make navbar functionable

        View navbarView = (View) findViewById(R.id.navbar); //retrieve the id in <include>
        imgBtn = (ImageView) navbarView.findViewById(R.id.nav); //retrieve imgBtn from navbar.xml
        settingsBtn = (ImageView)navbarView.findViewById(R.id.settings);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this,Settings.class);
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
                        i = new Intent(Settings.this,Library.class);
                        startActivity(i);
                        break;
                    case R.id.menu_add:
                        i = new Intent(Settings.this, Add.class);
                        startActivity(i);
                        break;
                    case R.id.menu_credits:
                        i = new Intent(Settings.this,Credits.class);
                        startActivity(i);
                        break;
                    case R.id.menu_bookmark:
                        i = new Intent(Settings.this, bookmarkList.class);
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
    /*
    Check preferences and set them (called in onCreate())
     */
    private void checkSharedPreferences(){
        Boolean switchState = pref.getBoolean(getString(R.string.switchTheme),false); //default state is dark mode is off
        switchBtnDark.setChecked(switchState);
        if(switchState)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2)
        {
            checkSharedPreferences();
            Log.d("aaa","aaaaaa");
        }
    }

}



