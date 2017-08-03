package com.example.jykin.concolor;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.net.URL;

/**
 *
 */

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity
    implements HSV.OnDialogCloseListener, PaletteFragment.OnDialogCloseListener {
    private ActionBar actionBar;
    private int primary, darkPrimary, accent;
    private Button buttonHSV, buttonPalette, buttonPreviewApp, buttonPreviewWeb;
    private ImageButton ibPrimary, ibAccent, ibDark;

    private TextView et_a, et_r, et_g, et_b;
    private static final String TAG = "mainactivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get ui elements
        actionBar = getSupportActionBar();
        primary = ContextCompat.getColor(this, R.color.colorPrimary);
        darkPrimary = ContextCompat.getColor(this, R.color.colorPrimaryDark);
        accent = ContextCompat.getColor(this, R.color.colorAccent);
        //argb edit text views
        et_a = (TextView) findViewById(R.id.et_a);
        et_r = (TextView) findViewById(R.id.et_r);
        et_g = (TextView) findViewById(R.id.et_g);
        et_b = (TextView) findViewById(R.id.et_b);
        //hsv button
        buttonHSV = (Button) findViewById(R.id.HSV_Button);
        buttonHSV.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //click listener for hsv button
                FragmentManager fm = getSupportFragmentManager();
                HSV hsv = HSV.newInstance(argbToColor());
                hsv.show(fm, "hsv_fragment");
            }
        });
        //image button
        buttonPalette = (Button) findViewById(R.id.palette_button);
        buttonPalette.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //click listener for palette button
                FragmentManager fm = getSupportFragmentManager();
                PaletteFragment palette = PaletteFragment.newInstance(argbToColor());
                palette.show(fm, "palette_fragment");
            }
        });
        //preview buttons
        buttonPreviewApp = (Button) findViewById(R.id.b_preview_app);
        //Web Preview
        buttonPreviewWeb = (Button) findViewById(R.id.b_preview_web);
        buttonPreviewWeb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                int r, g, b;
                r = Integer.parseInt(et_r.getText().toString());
                g = Integer.parseInt(et_g.getText().toString());
                b = Integer.parseInt(et_b.getText().toString());
                int color = Color.rgb(r, g, b);

                String hexValue = String.format("%06X", (0xFFFFFF & color));
                URL url = WebPreview.makeURL(hexValue);

                Uri newsPage = Uri.parse(url.toString());
                Intent intent = new Intent(Intent.ACTION_VIEW, newsPage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        //color image buttons
        ibPrimary = (ImageButton) findViewById(R.id.ib_primary);
        ibDark = (ImageButton) findViewById(R.id.ib_primary_dark);
        ibAccent = (ImageButton) findViewById(R.id.ib_accent);
    }


    @SuppressWarnings("PointlessBitwiseExpression")
    @Override
    public void closeDialog(int color) {
        String hexValue = String.format("#%08X", (0xFFFFFFFF & color));
        setEditText(color);
        Log.d(TAG, hexValue);
    }

    //helper method to set edit text boxes to arbg numbers
    private void setEditText(int color){
        int a, r, g, b;
        a = Color.alpha(color);
        r = Color.red(color);
        g = Color.green(color);
        b = Color.blue(color);

        et_a.setText(Integer.toString(a));
        et_r.setText(Integer.toString(r));
        et_g.setText(Integer.toString(g));
        et_b.setText(Integer.toString(b));
    }
    public int argbToColor(){
        //user can only input non decimal numbers
        //try catch still required for empty edittext case
        int a, r, g, b;
        try {
            a = Integer.parseInt(et_a.getText().toString());
            r = Integer.parseInt(et_r.getText().toString());
            g = Integer.parseInt(et_g.getText().toString());
            b = Integer.parseInt(et_b.getText().toString());
        } catch (NumberFormatException e) {
            a = 255;
            r = g = b = 255;
        }

        a = (a > 255 || a < 0) ? 255 : a;
        r = (r > 255 || r < 0) ? 255 : r;
        g = (g > 255 || g < 0) ? 255 : g;
        b = (b > 255 || b < 0) ? 255 : b;

        return Color.argb(a, r, g, b);
    }
}
