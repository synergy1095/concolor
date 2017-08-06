package com.example.jykin.concolor;

import android.graphics.Color;

/**
 * Created by micha on 8/4/2017.
 */

public class GenerateColors {

    public static int checkColor(int color) {
        if (Color.red(color) == 255 && Color.green(color) == 255 && Color.blue(color) == 255) {
            color = Color.BLACK;
        } else if (Color.red(color) == 0 && Color.green(color) == 0 && Color.blue(color) == 0) {
            color = Color.WHITE;
        }

        return color;
    }


    //Changes the hue of the color to get accent.
    public static int colorToAccent(int color) {
        float[] hsv = new float[3];
        color = checkColor(color);

        Color.RGBToHSV(Color.red(color), Color.green(color),
                Color.blue(color), hsv);
        hsv[0] = (hsv[0] + 180) % 360;

        return Color.HSVToColor(hsv);
    }

}
