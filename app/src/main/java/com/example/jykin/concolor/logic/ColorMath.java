package com.example.jykin.concolor.logic;

/**
 * Created by jykin.
 * Using rgb to hsl tutorials along with discovered compliment calculations translated
 */

public class ColorMath {
    public static int[] rgbtohsl(int[] rgb){
        int min = getMin(rgb);
        int max = getMax(rgb);
        int colorMax = getMaxIndex(rgb);
        int dif = max - min;

        //           H S L
        int[] hsl = {0,0,0};

        // hsl[0] == H
        // hsl[1] == S
        // hsl[2] == L

        hsl[2] = (max + min)/2;

        if(dif > 0){
            if(hsl[2] < 128) hsl[1] = dif/(min + max);
            else hsl[1] = dif/(512 - min - max);

            if(colorMax == 0)
                hsl[0] = (((max - rgb[2])/6) + (dif/2))/dif - (((max - rgb[1])/6) + (dif/2))/dif;
            else if(colorMax == 1)
                hsl[0] = 85 + (((max - rgb[0])/6) + (dif/2))/dif - (((max - rgb[2])/6) + (dif/2))/dif;
            else if(colorMax == 2)
                hsl[0] = 170 + (((max - rgb[1])/6) + (dif/2))/dif - (((max - rgb[0])/6) + (dif/2))/dif;
        }

        if(hsl[0] < 0) hsl[0] = hsl[0] + 256;
        if(hsl[0] > 255) hsl[0] = hsl[0] - 256;

        return hsl;
    }
    public static int[] rgbtohsl(String[] hsl){
        return rgbtohsl(Val.hexToInt(hsl));
    }

    public static int[] complimentHSL(int[] hsl){
        // COMPLIMENT WILL ALWAYS USE HSL
        // comp[0-2]
        //            0 1 2
        //            H S L
        int[] comp = {0,0,0};
        comp[0] = hsl[0] + 128;
        if(comp[0] > 255) comp[0] = comp[0] - 256;
        // *** RETURNS IN HSL ***
        return comp;
    }

    public static int[] complimentRGB(int[] rgb){
        // convert to HSL then call complimentHSL
        // convert result HSL to RGB
        int[] comp = complimentHSL(rgbtohsl(rgb));
        //           R G B
        int[] comprgb = {0,0,0};
        if (comp[1] == 0) {
            comprgb[0] = (int)(comp[2]/255.0 * 255.0);
            comprgb[1] = (int)(comp[2]/255.0 * 255.0);
            comprgb[2] = (int)(comp[2]/255.0 * 255.0);
        }else{
            int a = 0;
            int b = (comp[2] + comp[1]) - (comp[2] * comp[1]);
            if(comp[2] < 128) b = comp[1] * (256 + comp[1]);
            a = 2*(comp[2] - b);
            comprgb[0] = huetorgb(a,b,comp[0] + 85);
            comprgb[1] = huetorgb(a,b,comp[0]);
            comprgb[2] = huetorgb(a,b,comp[0] - 85);
        }
        return comprgb;
    }

    private static int huetorgb(int a, int b, int c){
        int c_adj = c;
        if(c_adj < 0) c_adj += 256;
        else if(c_adj > 255) c_adj -= 256;

        if(6 * c_adj < 256)
            return a + (b - a) * 6 * c_adj;
        if(2 * c_adj < 256)
            return b;
        if(3 * c_adj < 512)
            return a + (b - a) * (170 - c) * 6;
        return a;
    }

    public static int getMax(int[] values){
        int max = values[0];
        for(int i = 1; i < values.length; i++) if (max < values[i]) max = values[i];
        return max;
    }
    public static int getMin(int[] values){
        int min = values[0];
        for(int i = 1; i < values.length; i++) if (min > values[i]) min = values[i];
        return min;
    }
    public static int getMaxIndex(int[] values){
        int max = 0;
        for(int i = 1; i < values.length; i++) if (values[max] < values[i]) max = i;
        return max;
    }
    public static int getMinIndex(int[] values){
        int min = 0;
        for(int i = 1; i < values.length; i++) if (values[min] > values[i]) min = i;
        return min;
    }
}
