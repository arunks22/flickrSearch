package com.arun.flickrsearch;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<PhotoModel> mPhotos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        findViewById(R.id.searchBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchTag = ((EditText)findViewById(R.id.searchField)).getText().toString();
                new FetchPhotosTask().execute("flickr.photos.search", searchTag);
                EditText searchField = ((EditText)findViewById(R.id.searchField));
                searchField.setText("");
                searchField.setHint(searchTag);
            }
        });

        findViewById(R.id.searchBtn).requestFocus();
        ListView listView = findViewById(R.id.listView);
        mPhotos = new ArrayList<PhotoModel>();
        listView.setAdapter(mRecentsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                PhotoModel model= mPhotos.get(pos);
                Intent intent = new Intent(MainActivity.this, PhotoDetailsActivity.class);
                intent.putExtra("photo_model",model);
                startActivity(intent);
            }
        });
        new FetchPhotosTask().execute("flickr.photos.getRecent");
    }


    private BaseAdapter mRecentsAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return mPhotos.size();
        }

        @Override
        public Object getItem(int i) {
            return mPhotos.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.photo_list_item, null);
                holder.icon = (ImageView)convertView.findViewById(R.id.photo_icon);
                holder.titleText = (TextView)convertView.findViewById(R.id.titleText);
                holder.ownerText = (TextView)convertView.findViewById(R.id.ownerText);
                convertView.setTag(holder);
            }
            PhotoModel model = mPhotos.get(position);
            holder = (ViewHolder) convertView.getTag();
            holder.icon.setImageResource(R.drawable.ic_launcher_background);
            BitmapLoader.load(holder.icon, model.getId());
            holder.titleText.setText(model.getTitle());
            holder.ownerText.setText(model.getOwner());
            return convertView;
        }

        class ViewHolder {

            public ImageView icon;
            public TextView titleText;
            public TextView ownerText;
        }
    };

    private class FetchPhotosTask extends AsyncTask<String, Integer, ArrayList<PhotoModel>>{

        @Override
        protected ArrayList<PhotoModel> doInBackground(String... strings) {
            String params = "";
            String flickrMethod = strings[0];
            if(strings.length>1) {
                params = "tags="+strings[1];
            }
            String response = FlickrApiCaller.makeRequest(flickrMethod, params);
            ArrayList<PhotoModel> photos = FlickrJsonParser.parsePhotos(response);
            return photos;
        }

        @Override
        protected void onPostExecute(ArrayList<PhotoModel> photoModels) {
            super.onPostExecute(photoModels);
            mPhotos = photoModels;
            mRecentsAdapter.notifyDataSetChanged();
        }
    }


}
