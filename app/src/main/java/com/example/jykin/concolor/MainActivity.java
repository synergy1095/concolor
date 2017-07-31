package com.example.jykin.concolor;

import android.graphics.Color;
import android.nfc.Tag;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rarepebble.colorpicker.ColorPickerView;

import org.w3c.dom.Text;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity
    implements HSV.OnDialogCloseListener {
    private ActionBar actionBar;
    private int primary, darkPrimary, accent;
    private Button buttonHSV;

    private TextView et_a, et_r, et_g, et_b;
    private static final String TAG = "mainactivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ColorPickerView picker = (ColorPickerView)findViewById(R.id.colorPicker);
//        picker.setColor(0xffff0000);

        actionBar = getSupportActionBar();
        primary = ContextCompat.getColor(this, R.color.colorPrimary);
        darkPrimary = ContextCompat.getColor(this, R.color.colorPrimaryDark);
        accent = ContextCompat.getColor(this, R.color.colorAccent);

        et_a = (TextView) findViewById(R.id.et_a);
        et_r = (TextView) findViewById(R.id.et_r);
        et_g = (TextView) findViewById(R.id.et_g);
        et_b = (TextView) findViewById(R.id.et_b);

        buttonHSV = (Button) findViewById(R.id.HSV_Button);
        buttonHSV.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //user can only input non decimal numbers
                int a, r, g, b;
                try {
                    a = Integer.parseInt(et_a.getText().toString());
                    r = Integer.parseInt(et_r.getText().toString());
                    g = Integer.parseInt(et_g.getText().toString());
                    b = Integer.parseInt(et_b.getText().toString());
                } catch (NumberFormatException e) {
                    a = 255;
                    r = g = b = 0;
                }

                a = (a > 255 || a < 0) ? 255 : a;
                r = (r > 255 || r < 0) ? 255 : r;
                g = (g > 255 || g < 0) ? 255 : g;
                b = (b > 255 || b < 0) ? 255 : b;

                int color = Color.argb(a, r, g, b);
                FragmentManager fm = getSupportFragmentManager();
                HSV hsv = HSV.newInstance(color);
                hsv.show(fm, "hsv_fragment");
            }
        });
    }


    @Override
    public void closeDialog(int color) {
        int a, r, g, b;
        a = Color.alpha(color);
        r = Color.red(color);
        g = Color.green(color);
        b = Color.blue(color);

        et_a.setText(Integer.toString(a));
        et_r.setText(Integer.toString(r));
        et_g.setText(Integer.toString(g));
        et_b.setText(Integer.toString(b));

        String hexValue = String.format("#%08X", (0xFFFFFFFF & color));
        Log.d(TAG, hexValue);
    }
}
