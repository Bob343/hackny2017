package com.insite;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

public class LocationList extends AppCompatActivity {

    ListView listview;
    Context context;

    public static final String LOG_TAG = LocationList.class.getSimpleName();
    private ProgressBar mProgress;
    private int mProgressStatus  = 0;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);

        setTitle("Nearby Matches");

        String[] names;
        String[] addresses;
        Bitmap[] images;
        String[] imageUrls;

        //get json from file //TODO: Get from url
        try {
            String json = loadJSONFromDemo();
            Log.v(LOG_TAG,json);
            JSONObject jObj = new JSONObject(json);
            JSONArray jArr = jObj.getJSONArray("locations");

            int size = jArr.length();
            names = new String[size];
            addresses = new String[size];
            imageUrls = new String[size];

            for (int i=0; i<size; i++) {
                JSONObject obj = jArr.getJSONObject(i);
                names[i] = obj.getString("name");
                addresses[i] = obj.getString("address");
                imageUrls[i] = obj.getString("image");
            }

            //convert imageUrls to array of Bitmaps
            images = new Bitmap[imageUrls.length];

            //load into imageView
            context = this;
            listview = (ListView) findViewById(R.id.LocationListView);
            listview.setAdapter(new CustomAdapter(this, names, addresses, images));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String loadJSONFromDemo() {
        String json = null;
        try {
            InputStream is = getResources().openRawResource(R.raw.example);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }



}
