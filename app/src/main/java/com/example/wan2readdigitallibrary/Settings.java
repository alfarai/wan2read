package com.example.wan2readdigitallibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.fonts.Font;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Settings extends AppCompatActivity{
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
        Spinner FontSize = (Spinner) findViewById(R.id.btnFontSize);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.arrayFontSizes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        FontSize.setAdapter(adapter);
    }

}

class FontSize extends Activity implements AdapterView.OnItemSelectedListener{
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
        //An item was selected . YOu can retrieve the selected item using
        //parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent){
        //Another interface callback
    }
}
