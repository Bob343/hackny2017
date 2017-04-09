package com.insite;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

public class LocationList extends AppCompatActivity {

    ListView listview;
    Context context;

    public static final String LOG_TAG = LocationList.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);

        context = this;
        listview = (ListView) findViewById(R.id.LocationListView);

        //get json from file //TODO: Get from url
        try {
            String json = loadJSONFromDemo();
            Log.v(LOG_TAG,json);
            JSONObject jObj = new JSONObject(json);
            JSONArray jArr = jObj.getJSONArray("locations");

            for (int i=0; i<jArr.length(); i++) {
                JSONObject obj = jArr.getJSONObject(i);
                System.out.println(obj.getString("name")); //debug //TODO: Load into listview
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



//        ArrayAdapter adapter = new ArrayAdapter();

        //set image (test)
//        Image image;
//        image = (ImageView) findViewById(R.id.locationImage);

        //change image with image.setImageResource(R.drawable.android);

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
