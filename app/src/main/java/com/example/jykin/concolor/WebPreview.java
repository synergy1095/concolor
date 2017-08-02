package com.example.jykin.concolor;

import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Syntax Mike on 8/1/2017.
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
}
