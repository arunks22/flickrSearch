package com.arun.flickrsearch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;


/**
 * Created by arun on 22/2/18.
 */

class BitmapLoader {
    public enum IMAGE_SIZES {
        ICON, NORMAL
    }

    private static HashMap<String, Bitmap> bmpCache = new HashMap<String, Bitmap>();
    private static HashMap<String,ImageView> imageViewMapping = new HashMap<String, ImageView>();
    private static  ArrayBlockingQueue<String> requestQueue = new ArrayBlockingQueue<String>(1000);

    public static void load(ImageView iView, String id) {
        if(bmpCache.containsKey(id) && bmpCache.get(id)!=null) {
            iView.setImageBitmap(bmpCache.get(id));
            return;
        }

        try {
            requestQueue.put(id);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startImageLoaderThread();
    }

    private static Handler mHandler =new Handler();

    private static Thread mImageLoaderThread = null;
    private static void startImageLoaderThread() {
        if(mImageLoaderThread !=null) {
            return;
        }

        mImageLoaderThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String id = null;
                        id = requestQueue.take();
                        String response = FlickrApiCaller.makeRequest("flickr.photos.getSizes", "photo_id=" + id);
                        String path = getImagePath(response);
                        final Bitmap bmp = loadBitmap(path);
                        bmpCache.put(id, bmp);
                        final ImageView img = imageViewMapping.get(id);
                        if(img!=null) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    img.setImageBitmap(bmp);
                                    img.invalidate();
                                }
                            });
                        }
                    } catch(InterruptedException e){
                        e.printStackTrace();
                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        });
        mImageLoaderThread.start();
    }

    private static Bitmap loadBitmap(String path) {
        if(path == null) {
            return null;
        }
        InputStream is = null;
        Bitmap bmp = null;
        try {
            is = new URL(path).openStream();
        bmp = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmp;
    }

    private static String getImagePath(String response) throws JSONException {
        JSONArray jsonArr = new JSONObject(response).getJSONObject("sizes").getJSONArray("size");

        for(int i =0 ;i< jsonArr.length();i++){
            JSONObject jObj = jsonArr.getJSONObject(i);
            String label = jObj.getString("label");
            if(label.equals("Medium")){
                String path = jObj.getString("source");
                System.out.println("Response : "+path);
                return path;
            }
        }
        return null;
    }

}
