package com.insite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
import java.net.URL;

public class LocationList extends AppCompatActivity {
    protected Context mContext;

    public static final String LOG_TAG = LocationList.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);

        setTitle("Nearby Matches");

        
        mContext = this;

        String json = loadJSONFromDemo();

        new FetchImagesAsyncTask().execute(json);
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

    private class FetchImagesAsyncTask extends AsyncTask<String, Void, Void> {

        protected Holder[] mHolders;

        @Override
        protected Void doInBackground(String... strings) {
            try {
                JSONObject jObj = new JSONObject(strings[0]);
                JSONArray jArr = jObj.getJSONArray("sites");

                int size = jArr.length();
                mHolders = new Holder[size];

                for (int i=0; i<size; i++) {
                    JSONObject obj = jArr.getJSONObject(i).getJSONObject("site");
                    mHolders[i] = new Holder();
                    mHolders[i].tvName = obj.getString("name");
                    mHolders[i].tvAddress = obj.getString("address");
                    mHolders[i].img = getImageFromUrl(obj.getString("image"));
                    mHolders[i].prog = obj.getInt("confidence");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        public Bitmap getImageFromUrl(String url) {

            Bitmap map = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.placeholder);

            try {
                URL conn = new URL(url);
                map = BitmapFactory.decodeStream(conn.openConnection().getInputStream());
            }
            catch (IOException e) {
                Log.e(LOG_TAG, e.getLocalizedMessage());
            }

            return map;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ListView listview = (ListView) findViewById(R.id.LocationListView);
            listview.setAdapter(new ListAdapter(mContext, R.layout.location_list, mHolders));
        }
    }
}
