package com.arun.flickrsearch;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoDetailsActivity extends AppCompatActivity {

    private PhotoModel photoModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_details);

        photoModel = getIntent().getParcelableExtra("photo_model");

        ((TextView)findViewById(R.id.ownerDetails)).setText(photoModel.getOwner());
        ((TextView)findViewById(R.id.titleDetails)).setText(photoModel.getTitle());

        ImageView image = findViewById(R.id.image);
        BitmapLoader.load(image, photoModel.getId());
    }
}
