package com.insite;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.insite.util.ApiConnection;
import com.insite.util.MultipartUtility;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.okhttp.OkHttpStack;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ImageUploadTest extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

//    public static final String SERVER = "https://serene-refuge-17558.herokuapp.com/upload";
    public static final String SERVER = "http://172.27.223.193/phototest";
    public static final String IMAGE = "IMG_20170203_223045.jpg";

    public static final String LOG_TAG = ImageUploadTest.class.getSimpleName();

    public static final int REQUEST_IMAGE_CAPTURE = 1;

    protected GoogleApiClient mGoogleApiClient = null;
    protected Location mLastLocation;

    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload_test);
        setUpGoogleApi();

        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;

        dispatchTakePictureIntent();
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onConnected(Bundle connectionHint) {

        Log.v(LOG_TAG,"In onConnected");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.e(LOG_TAG,"Permission Check failed");
            //requestPermissions();
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Log.v(LOG_TAG,mLastLocation.toString());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(LOG_TAG,"Connection Suspended: "+i);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(LOG_TAG,"Connection Failed: "+connectionResult.getErrorMessage());
    }


    public void setUpGoogleApi() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            Log.v(LOG_TAG,"Setting up GoogleApi");
        }
    }

    public void TEST_UPLOAD(View view) {

//        new ImageUploadAsync().execute(IMAGE);

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                //...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.v(LOG_TAG,"PhotoFile:"+photoFile.toString());
                Uri photoURI = Uri.parse(photoFile.toURI().toString());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                new ImageUploadAsync().execute(mCurrentPhotoPath);
        }
    }

    public void CAMERA(View view) {
        Log.v(LOG_TAG, "Taking Picture");
        Intent intent = new Intent(this, LocationList.class);
        intent.putExtra("resource",R.raw.example);

        startActivity(intent);
    }

    protected class ImageUploadAsync extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {

//            String resp = uploadImage(strings[0]);
//            Log.v(LOG_TAG,resp);
            return null;
        }

        protected String uploadImage(String filePath) {

            UploadService.HTTP_STACK = new OkHttpStack();
            uploadMultipart(getApplicationContext(), filePath);

            return "";
        }

        public void uploadMultipart(final Context context, String filePath) {
            try {
                String uploadId =
                        new MultipartUploadRequest(context, SERVER)
                                // starting from 3.1+, you can also use content:// URI string instead of absolute file
                                .addFileToUpload(filePath, "image")
                                .setNotificationConfig(new UploadNotificationConfig())
                                .setMaxRetries(2)
                                .startUpload();
                Log.v(LOG_TAG,uploadId);
            } catch (Exception exc) {
                Log.e("AndroidUploadService", exc.getMessage(), exc);
            }
        }

        protected String postImage(String filePath) {
            String charset = "UTF-8";
            try {
                MultipartUtility multipart = new MultipartUtility(SERVER, charset);
                multipart.addFilePart("image", new File(filePath));
                multipart.addFormField("title",filePath);

//                multipart.addFormField("lat",String.valueOf(mLastLocation.getLatitude()));
//                multipart.addFormField("lon",String.valueOf(mLastLocation.getLongitude()));

                multipart.addFormField("lat",String.valueOf(40.749627));
                multipart.addFormField("lon",String.valueOf(-73.985246));

                multipart.addHeaderField("title",filePath);
                Log.v(LOG_TAG, multipart.finish());
            } catch (IOException e) {
                Log.e(LOG_TAG,"postImage:\n\t"+e.getLocalizedMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void s) {

//            Log.v(LOG_TAG,s);
            Intent intent = new Intent(getApplicationContext(),LocationList.class);
            intent.putExtra("resource",R.raw.chelsea);
            startActivity(intent);
        }
    }
}
