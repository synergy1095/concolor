package com.example.jykin.concolor;

import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Syntax Mike.
 */


public class WebPreview {

    private static  final String BASIC_HTML_PAGE = "http://138.68.240.171/";

    public static URL makeURL(String hexCode){
        Uri uri = Uri.parse(BASIC_HTML_PAGE).buildUpon()
                .appendQueryParameter("hex", hexCode).build();

        URL url = null;

        try {
            url = new URL(uri.toString());
        }catch(MalformedURLException e){
            e.printStackTrace();
        }

        return url;
    }

    // newer version to account for color scheme full.
    public static URL makeURL(String primCol, String darkCol, String acceCol){
        Uri uri = Uri.parse(BASIC_HTML_PAGE).buildUpon()
                .appendQueryParameter("prim",primCol)
                .appendQueryParameter("dark",darkCol)
                .appendQueryParameter("acce",acceCol).build();

        URL url = null;

        try{
            url = new URL(uri.toString());
        }catch(MalformedURLException e){
            e.printStackTrace();
        }

        return url;
    }
}
