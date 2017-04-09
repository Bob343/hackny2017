package com.insite;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.insite.util.ApiConnection;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Iterator;

public class LocationInfo extends AppCompatActivity {

    public static final String LOG_TAG = LocationInfo.class.getSimpleName();
//    public static final String EXTRA = "extra";
    public static final String TITLE = "title";
    public static final String IMAGE = "image";

    protected String mTitle;
//    protected Bitmap mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_info);

        setTitle("");

        Bundle extras = getIntent().getExtras();

        mTitle = extras.getString(TITLE);
//        mImage = extras.getParcelable(IMAGE);

        setupDefaults();
    }

    public void setupDefaults() {
        showProgressBar();

        TextView titleView = (TextView) findViewById(R.id.info_title);
        titleView.setText(mTitle);
    }

    public void showProgressBar() {
        TextView bodyText = (TextView) findViewById(R.id.info_body);
        bodyText.setVisibility(View.GONE);

        ProgressBar bar = (ProgressBar) findViewById(R.id.info_prog);
        bar.setVisibility(View.VISIBLE);

    }

    public void hideProgressBar() {
        ProgressBar bar = (ProgressBar) findViewById(R.id.info_prog);
        bar.setVisibility(View.GONE);

        TextView bodyText = (TextView) findViewById(R.id.info_body);
        bodyText.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new WikipediaAsyncTask().execute(mTitle);
    }

    public void GET_WIKI(View view) {
        new WikipediaAsyncTask().execute(mTitle);
    }

    private class WikipediaAsyncTask extends AsyncTask<String,Void,String> {

        public static final String WIKIPEDIA_URL = "https://en.wikipedia.org/w/api.php";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressBar();

        }

        @Override
        protected String doInBackground(String... strings) {

            ApiConnection conn = new ApiConnection(WIKIPEDIA_URL).buildUpon()
                    .appendQuery("format","json")
                    .appendQuery("action","query")
                    .appendQuery("prop","extracts")
                    .appendQuery("titles",strings[0])
                    .appendQuery("exintro","true")
                    .appendQuery("explaintext","true")
                    .build();
            return conn.connect("GET");
        }

        @Override
        protected void onPostExecute(String s) {

            TextView bodyView = (TextView) findViewById(R.id.info_body);
            TextView titleView = (TextView) findViewById(R.id.info_title);

            String bodyText = "";
            String titleText = "";
            try {
                JSONObject root = new JSONObject(s).getJSONObject("query").getJSONObject("pages");

                String key = root.keys().next();
                if(key.equals("-1")) {
                    bodyText = getString(R.string.not_found);
                    titleText = root.getJSONObject(key).getString("title");
                }
                else {
                    JSONObject page = root.getJSONObject(key);
                    bodyText = page.getString("extract").replace("\n","\n\n");
                    titleText = page.getString("title");
                }
            }
            catch (JSONException e) {
                Log.e(LOG_TAG,e.getLocalizedMessage());
            }

            bodyView.setText(bodyText);
            titleView.setText(titleText);

            hideProgressBar();

        }
    }
}
