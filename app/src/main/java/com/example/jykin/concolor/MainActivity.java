package com.example.jykin.concolor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rarepebble.colorpicker.ColorPickerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ColorPickerView picker = (ColorPickerView)findViewById(R.id.colorPicker);
        picker.setColor(0xffff0000);
    }


}
