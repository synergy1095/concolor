package com.example.jykin.concolor;

import android.graphics.Color;

/**
 * Created by micha on 8/4/2017.
 */

public class AccentBlackWhite {

    public static int checkColor(int color) {
        if (Color.red(color) == 255 && Color.green(color) == 255 && Color.blue(color) == 255) {
            color = Color.BLACK;
        } else if (Color.red(color) == 0 && Color.green(color) == 0 && Color.blue(color) == 0) {
            color = Color.WHITE;
        }

        return color;
    }
}
