package com.arun.flickrsearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by arun on 22/2/18.
 */

class FlickrJsonParser {
    public static ArrayList<PhotoModel> parsePhotos(String response) {
        ArrayList<PhotoModel> photos = new ArrayList<PhotoModel>();
        try {
//            response = response.substring("jsonFlickrApi(".length(), response.length()-2);
            System.out.println(response);
            JSONObject rootObj = new JSONObject(response);
            System.out.println("Step 1");
            JSONObject photosObj = rootObj.getJSONObject("photos");
            System.out.println("Step 2");
            JSONArray photosArray = rootObj.getJSONObject("photos").getJSONArray("photo");
            System.out.println("Step 3");
            for(int i = 0;i<photosArray.length();i++){
                JSONObject jObj = photosArray.getJSONObject(i);
                PhotoModel model = new PhotoModel();
                model.setId(jObj.getString("id"));
                model.setTitle(jObj.getString("title"));
                model.setOwner(jObj.getString("owner"));
                photos.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return photos;
    }
}
