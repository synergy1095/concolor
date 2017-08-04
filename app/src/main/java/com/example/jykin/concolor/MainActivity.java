package com.example.jykin.concolor;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
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

import java.io.File;
import java.net.URL;


/**
 *
 */

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity
        implements HSV.OnDialogCloseListener, PaletteFragment.OnDialogCloseListener, View.OnLongClickListener, View.OnClickListener {
    private ActionBar actionBar;
    private int primary, darkPrimary, accent;
    private Button buttonHSV, buttonPalette, buttonPreviewApp, buttonPreviewWeb;
    private ImageButton ibPrimary, ibAccent, ibDark;
    private int sPrimary, sDarkPrimary, sAccent;


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

        //image button
        buttonPalette = (Button) findViewById(R.id.palette_button);

        //preview button
        buttonPreviewApp = (Button) findViewById(R.id.b_preview_app);

        //Web Preview
        buttonPreviewWeb = (Button) findViewById(R.id.b_preview_web);


        //color image buttons
        ibPrimary = (ImageButton) findViewById(R.id.ib_primary);
        ibPrimary.setOnLongClickListener(this);

        ibDark = (ImageButton) findViewById(R.id.ib_primary_dark);
        ibDark.setOnLongClickListener(this);

        ibAccent = (ImageButton) findViewById(R.id.ib_accent);
        ibAccent.setOnLongClickListener(this);
    }

    @SuppressWarnings("PointlessBitwiseExpression")
    @Override
    public void closeDialog(int color) {
        String hexValue = String.format("#%08X", (0xFFFFFFFF & color));
        setEditText(color);


        sPrimary = color;
        sDarkPrimary = rgbToDarkColor();
        sAccent = colorToAccent(color);

        ibPrimary.setBackgroundColor(sPrimary);
        ibDark.setBackgroundColor(sDarkPrimary);
        ibAccent.setBackgroundColor(sAccent);
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

    public int rgbToDarkColor(){
        int r, g, b;
        //Darkens the color by 50%
        double darkenShade = 0.5;
        try {
            r = Integer.parseInt(et_r.getText().toString());
            g = Integer.parseInt(et_g.getText().toString());
            b = Integer.parseInt(et_b.getText().toString());
        } catch (NumberFormatException e) {
            r = g = b = 255;
        }

        r = (int) (((r > 255 || r < 0) ? 255 : r) * darkenShade);
        g = (int) (((g > 255 || g < 0) ? 255 : g) * darkenShade);
        b = (int) (((b > 255 || b < 0) ? 255 : b) * darkenShade);

        return Color.rgb(r, g, b);
    }

    //Changes the hue of the color to get accent. Currently does not work if primary is white or black
    public int colorToAccent(int color) {
        float[] hsv = new float[3];
        Color.RGBToHSV(Color.red(color), Color.green(color),
                Color.blue(color), hsv);
        hsv[0] = (hsv[0] + 180) % 360;

        return Color.HSVToColor(hsv);
    }

    //Saves color and displays on imagebutton when clicked
    public void ibColorsClick(View view){
        if (view.getId() == R.id.ib_primary) {
            sPrimary = argbToColor();
            ibPrimary.setBackgroundColor(sPrimary);
        } else if (view.getId() == R.id.ib_primary_dark) {
            sDarkPrimary = argbToColor();
            ibDark.setBackgroundColor(sDarkPrimary);
        } else if (view.getId() == R.id.ib_accent){
            sAccent = argbToColor();
            ibAccent.setBackgroundColor(sAccent);
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){

            case R.id.HSV_Button:
                //click listener for hsv button
                FragmentManager fmHSV = getSupportFragmentManager();
                HSV hsv = HSV.newInstance(argbToColor());
                hsv.show(fmHSV, "hsv_fragment");
                break;

            case  R.id.palette_button:

                //click listener for palette button
                FragmentManager fmPalette = getSupportFragmentManager();
                PaletteFragment palette = PaletteFragment.newInstance(argbToColor());
                palette.show(fmPalette, "palette_fragment");
                break;

            case R.id.b_preview_app:
                break;

            case R.id.b_preview_web:

                String hexValue = String.format("%06X", (0xFFFFFF & argbToColor()));
                URL url = WebPreview.makeURL(hexValue);

                Uri newsPage = Uri.parse(url.toString());
                Intent intent = new Intent(Intent.ACTION_VIEW, newsPage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                break;

        }
    }

    //Long Click on imagebutton displays color rgb currently in it
    @Override
    public boolean onLongClick(View view) {
        if (view.getId() == R.id.ib_primary) {
            setEditText(sPrimary);
        } else if (view.getId() == R.id.ib_primary_dark) {
            setEditText(sDarkPrimary);
        } else if (view.getId() == R.id.ib_accent){
            setEditText(sAccent);
        }
        return true;
    }
}