package com.insite.util;

import android.net.Uri;
import android.net.Uri.Builder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

// TODO: 3/8/16 Document this class

/**
 * Created by Chris on 10/6/16.
 *
 * @author Chris
 * @version 1.0
 */
public class ApiConnection {

    protected final String BASE_URL;
    protected static final String LOG_TAG = ApiConnection.class.getSimpleName();
    protected Builder builder = null;
    protected Uri uri = null;
    protected static final int TIMEOUT = 5000;

    public ApiConnection(String base) {
        BASE_URL = base;
    }

    public ApiConnection buildUpon() {
        builder = Uri.parse(BASE_URL).buildUpon();
        return this;
    }


    public ApiConnection appendPath(String path) {
        builder = builder.appendPath(path);
        return this;
    }

    public ApiConnection appendQuery(String key, String value) {
        builder = builder.appendQueryParameter(key, value);
        return this;
    }

    public ApiConnection build() {
        uri = builder.build();

//        Log.v(LOG_TAG, "Built URI " + builder.toString());
        return this;
    }

    public String connect(String method) {

        if (builder == null)
            return null;

        BufferedReader reader = null;
        HttpURLConnection urlConnection = null;

        String resp = null;

        try {
            URL url = new URL(builder.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(method);
            urlConnection.setConnectTimeout(TIMEOUT);
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();

            if (inputStream == null)
                return null;

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while ((line = reader.readLine()) != null)
                buffer.append(line + "\n");

            if (buffer.length() == 0)
                return null;

            resp = buffer.toString();
        } catch(SocketTimeoutException e) {
            Log.e(LOG_TAG, "Connection Timeout", e);
        } catch (ConnectException e) {
            Log.e(LOG_TAG, "Connection Failed", e);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error", e);
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();

            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
        }

        return resp;
    }
}
