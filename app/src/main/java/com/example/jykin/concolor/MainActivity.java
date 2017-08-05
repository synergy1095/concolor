package com.example.jykin.concolor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.net.URL;


/**
 *
 */

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity
        implements HSV.OnDialogCloseListener, PaletteFragment.OnDialogCloseListener, View.OnLongClickListener, View.OnClickListener {
    private ActionBar actionBar;
    private Button buttonHSV, buttonPalette, buttonPreviewApp, buttonPreviewWeb;
    private ConstraintLayout mainConstraintLayout;
    private ImageButton ibPrimary, ibAccent, ibDark;
    private int sPrimary, sDarkPrimary, sAccent;
    private TextView tvPrimary, tvDark, tvAccent, tvInstruction, tvRGBLabel, tvColor;

    private EditText et_r, et_g, et_b;
    private static final String TAG = "mainactivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set to app defaults initially
        sPrimary = ContextCompat.getColor(this, R.color.colorPrimary);
        sDarkPrimary = ContextCompat.getColor(this, R.color.colorPrimaryDark);
        sAccent = ContextCompat.getColor(this, R.color.colorAccent);

        //get ui elements
        actionBar = getSupportActionBar();
        mainConstraintLayout = (ConstraintLayout) findViewById(R.id.main_constraint);

        //rgb edit text views
        et_r = (EditText) findViewById(R.id.et_r);
        et_g = (EditText) findViewById(R.id.et_g);
        et_b = (EditText) findViewById(R.id.et_b);

        //textviews in main activity
        tvPrimary = (TextView) findViewById(R.id.tv_primary);
        tvDark = (TextView) findViewById(R.id.tv_primary_dark);
        tvAccent = (TextView) findViewById(R.id.tv_accent);

        tvInstruction = (TextView) findViewById(R.id.tv_instructions);
        tvRGBLabel = (TextView) findViewById(R.id.argbLabel);
        tvColor = (TextView) findViewById(R.id.colorSlotsLabel);

        //hsv button
        buttonHSV = (Button) findViewById(R.id.HSV_Button);
        buttonHSV.setOnClickListener(this);

        //image button
        buttonPalette = (Button) findViewById(R.id.palette_button);
        buttonPalette.setOnClickListener(this);

        //preview button
        buttonPreviewApp = (Button) findViewById(R.id.b_preview_app);
        buttonPreviewApp.setOnClickListener(this);

        //Web Preview
        buttonPreviewWeb = (Button) findViewById(R.id.b_preview_web);
        buttonPreviewWeb.setOnClickListener(this);

        //color image buttons
        //ib have onclick set in xml to ibColorsClick
        ibPrimary = (ImageButton) findViewById(R.id.ib_primary);
        ibPrimary.setOnLongClickListener(this);

        ibDark = (ImageButton) findViewById(R.id.ib_primary_dark);
        ibDark.setOnLongClickListener(this);

        ibAccent = (ImageButton) findViewById(R.id.ib_accent);
        ibAccent.setOnLongClickListener(this);

        //check shared preferences for previous installation of app
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //on first run with no saved colors, colors will default to app defaults above
        sPrimary = prefs.getInt("Primary", sPrimary);
        sDarkPrimary = prefs.getInt("Dark", sDarkPrimary);
        sAccent = prefs.getInt("Accent", sAccent);
        setIBColors();
    }

    @SuppressWarnings("PointlessBitwiseExpression")
    @Override
    public void closeDialog(int color) {
        if(color == -1) return;
        String hexValue = String.format("#%08X", (0xFFFFFFFF & color));
        setEditText(color);
//
//        sPrimary = color;
//        sDarkPrimary = rgbToDarkColor();
//        sAccent = colorToAccent(color);
//
//        ibPrimary.setBackgroundColor(sPrimary);
//        ibDark.setBackgroundColor(sDarkPrimary);
//        ibAccent.setBackgroundColor(sAccent);
        Log.d(TAG, hexValue);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //on stop save colors
        saveColors();
    }

    //saves colors to shared preferences
    private void saveColors(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("Primary", sPrimary);
        editor.putInt("Dark", sDarkPrimary);
        editor.putInt("Accent", sAccent);
        editor.commit();
    }

    //helper method to set edit text boxes to arbg numbers
    private void setEditText(int color){
        int r, g, b;
        //a = 255;
        r = Color.red(color);
        g = Color.green(color);
        b = Color.blue(color);

        // et_a.setText(Integer.toString(a));
        et_r.setText(Integer.toString(r));
        et_g.setText(Integer.toString(g));
        et_b.setText(Integer.toString(b));
    }


    public int rgbToColor(){
        //user can only input non decimal numbers
        //try catch still required for empty edittext case
        int r, g, b;
        try {
            // a = Integer.parseInt(et_a.getText().toString());
            r = Integer.parseInt(et_r.getText().toString());
            g = Integer.parseInt(et_g.getText().toString());
            b = Integer.parseInt(et_b.getText().toString());
        } catch (NumberFormatException e) {
            r = g = b = 255;
        }

        r = (r > 255 || r < 0) ? 255 : r;
        g = (g > 255 || g < 0) ? 255 : g;
        b = (b > 255 || b < 0) ? 255 : b;

        return Color.argb(255, r, g, b);
    }

    public int colorToDark(int color){
        int r, g, b;
        //Darkens the color by 50%
        double darkenShade = 0.5;

        //get each color element
        r = Color.red(color);
        g = Color.green(color);
        b = Color.blue(color);

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
            //primary color case also auto generates dark and accent colors
            sPrimary = rgbToColor();
            sDarkPrimary = colorToDark(sPrimary);
            sAccent = colorToAccent(sPrimary);
        } else if (view.getId() == R.id.ib_primary_dark) {
            sDarkPrimary = rgbToColor();
        } else if (view.getId() == R.id.ib_accent){
            sAccent = rgbToColor();
        }
        setIBColors();
    }

    private void setIBColors(){
        ibPrimary.setBackgroundColor(sPrimary);
//        ibPrimary.setBackgroundTintList(new ColorStateList(new int[][]{new int[]{R.attr.state}}));
        ibDark.setBackgroundColor(sDarkPrimary);
        ibAccent.setBackgroundColor(sAccent);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){

            case R.id.HSV_Button:
                //click listener for hsv button
                FragmentManager fmHSV = getSupportFragmentManager();
                HSV hsv = HSV.newInstance(rgbToColor());
                hsv.show(fmHSV, "hsv_fragment");
                break;

            case R.id.palette_button:

                //click listener for palette button
                FragmentManager fmPalette = getSupportFragmentManager();
                PaletteFragment palette = PaletteFragment.newInstance(rgbToColor());
                palette.show(fmPalette, "palette_fragment");
                break;

            case R.id.b_preview_app:
                Log.d(TAG,"App Preview");
                setAppColors();
                break;

            case R.id.b_preview_web:
                Log.d(TAG,"Web Preview");
                String prim = convertIntToHexColor(sPrimary);
                String dark = convertIntToHexColor(sDarkPrimary);
                String acce = convertIntToHexColor(sAccent);

                URL url = WebPreview.makeURL(prim,dark,acce);
                Log.d(TAG,url.toString());

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url.toString()));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                break;

        }
    }

    public String convertIntToHexColor(int color){
        return String.format("%06X", (0xFFFFFF & color));
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

    private void setAppColors(){
        //set button colors to contrasting colors
        setButtonColor(sAccent);
        setButtonTextColor(sPrimary);
        //set actionbar colors to contrasting colors
        setActionBarColor(sPrimary);
        setActionBarTextColor(sAccent);
        //set background and statusbar to same color
        setBackgroundColor(sDarkPrimary);
        setStatusBarColor(sDarkPrimary);
        //set general text color to contrasting color from background
        setTextColor(sAccent);
    }

    private void setButtonColor(int color){
        buttonPreviewWeb.setBackgroundColor(color);
        buttonPreviewApp.setBackgroundColor(color);
        buttonHSV.setBackgroundColor(color);
        buttonPalette.setBackgroundColor(color);
    }

    private void setButtonTextColor(int color){
        buttonPreviewWeb.setTextColor(color);
        buttonPreviewApp.setTextColor(color);
        buttonHSV.setTextColor(color);
        buttonPalette.setTextColor(color);
    }

    private void setActionBarColor(int color){
        actionBar.setBackgroundDrawable(new ColorDrawable(color));
    }

    private void setActionBarTextColor(int color){
        //fromHtml is only deprecated to hint at newer version for higher api versions
        //not actually deprecated
        actionBar.setTitle(Html.fromHtml("<font color='#" + convertIntToHexColor(color) + "'>Color Picker</font>"));
    }

    private void setBackgroundColor(int color){
        mainConstraintLayout.setBackgroundColor(color);
    }

    private void setTextColor(int color){
        //set hint and text colors for edit text
        et_r.setHintTextColor(color);
        et_g.setHintTextColor(color);
        et_b.setHintTextColor(color);

        et_r.setTextColor(color);
        et_g.setTextColor(color);
        et_b.setTextColor(color);

        //set colors for text views
        tvColor.setTextColor(color);
        tvRGBLabel.setTextColor(color);
        tvInstruction.setTextColor(color);
        tvAccent.setTextColor(color);
        tvDark.setTextColor(color);
        tvPrimary.setTextColor(color);
    }

    private void setStatusBarColor(int color){
        Window window = this.getWindow();

        //flags needed to be set before changing status bar color
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }
}