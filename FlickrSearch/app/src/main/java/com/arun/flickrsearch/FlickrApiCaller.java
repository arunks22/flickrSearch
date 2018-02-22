package com.arun.flickrsearch;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by arun on 22/2/18.
 */

class FlickrApiCaller {
    private static final String FLICKR_API = "https://api.flickr.com/services/rest/?format=json&method=%s&api_key=%s";
    private static  final String API_KEY = "6bf318919bbbc455f3573d18798a58e3";

    public static String makeRequest(String flickrMethod, String params) {
        String response = null;

        String url = String.format(FLICKR_API, flickrMethod, API_KEY);
        if(!TextUtils.isEmpty(params)){
            url+="&"+params;
        }
        System.out.println("URL : "+url);
        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-length", "0");
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null)
            {
                stringBuilder.append(line + "\n");
            }
            response = stringBuilder.toString();


        } catch (IOException e) {
            e.printStackTrace();
        }
        response = response.substring("jsonFlickrApi(".length(), response.length()-2);
        return response;
    }
}
