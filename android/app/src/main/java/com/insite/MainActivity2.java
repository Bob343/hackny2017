package com.insite;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.vision.Frame;
//import com.google.android.gms.vision.text.TextBlock;
//import com.google.android.gms.vision.text.TextRecognizer;

import com.insite.util.MultipartUtility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity2 extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    public static final String SERVER = "http://172.27.223.193/phototest/";
    public static final String IMAGE = "IMG_20170203_223045.jpg";


    static Uri mLocationForPhotos;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_EDIT_CODE = 2;
    static final int REQUEST_EDIT_CODE_EXISTING = 3;
    static final int REQUEST_OUTPUT_VIEW = 4;

    public static final String IMAGE_PATH = "/insite/images";
    String mCurrentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocationForPhotos = new Uri.Builder().appendPath(IMAGE_PATH).build();


        defaultView();

    }

    protected void defaultView() {
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

//    protected void startProgressBar() {
//
//        ProgressBar bar = (ProgressBar) findViewById(R.id.progress_bar);
//        TextView preview = (TextView) findViewById(R.id.text_preview);
//
//        bar.setVisibility(View.VISIBLE);
//        preview.setVisibility(View.GONE);
//
//    }

//    protected void endProgressBar() {
//
//        ProgressBar bar = (ProgressBar) findViewById(R.id.progress_bar);
//        TextView preview = (TextView) findViewById(R.id.text_preview);
//
//        preview.setVisibility(View.VISIBLE);
//        bar.setVisibility(View.GONE);
//
//    }

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

    public void TEST_UPLOAD(View view) {
        new ImageUploadAsync().execute(mCurrentPhotoPath);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if(resultCode == RESULT_CANCELED)
//            return;
//
//        Log.v(LOG_TAG, "Result Code: "+resultCode);
//        Log.v(LOG_TAG, "Request Code: "+requestCode);
//        switch (requestCode) {
//            case REQUEST_IMAGE_CAPTURE:
//                recognizeText();
//                break;
//            case REQUEST_EDIT_CODE:
//                mCode += data.getStringExtra(TextValidationActivity.SAVE_TEXT_KEY) + "\n";
//                updatePreview();
//                break;
//            case REQUEST_EDIT_CODE_EXISTING:
//                mCode = data.getStringExtra(TextValidationActivity.SAVE_TEXT_KEY) + "\n";
//                updatePreview();
//                break;
//            case REQUEST_OUTPUT_VIEW:
//                if (resultCode == OutputActivity.RESULT_CLEAR) {
//                    reset();
//                } else if (resultCode == OutputActivity.RESULT_EDIT) {
//                    EDIT(null);
//                }
//            default:
//                break;
//        }
//    }

//    public void reset() {
//        defaultView();
//        mCode = "";
//
//        TextView preview = (TextView) findViewById(R.id.text_preview);
//        preview.setText(getString(R.string.start_text));
//        endProgressBar();
//    }

//    protected void recognizeText() {
//        new TextRecognizerAsyncTask().execute();
//    }

//    protected void updatePreview() {
//        TextView preview = (TextView) findViewById(R.id.text_preview);
//        preview.setText(mCode);
//        endProgressBar();
//    }

    public void CAMERA(View view) {
        Log.v(LOG_TAG, "Taking Picture");

        dispatchTakePictureIntent();
    }

    protected class ImageUploadAsync extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {

            postImage(strings[0]);
            return null;
        }

        protected Bitmap getImage(String filePath) {

            Bitmap map = null;
            try {
                File f = new File(filePath, "profile.jpg");
                map = BitmapFactory.decodeStream(new FileInputStream(f));

            } catch (FileNotFoundException e) {
                Log.e(LOG_TAG, e.getLocalizedMessage());
            }

            return map;
        }

        protected String postImage(String filePath) {
            String charset = "UTF-8";
            try {
                MultipartUtility multipart = new MultipartUtility(SERVER, charset);
                multipart.addFilePart("image", getExternalFilesDir(Environment.DIRECTORY_PICTURES+"/"+filePath));
                Log.v(LOG_TAG, multipart.finish());
            } catch (IOException e) {
                Log.e(LOG_TAG,"postImage:\n\t"+e.getLocalizedMessage());
            }

            return null;
        }
    }

//    public void EDIT(View view) {
//
//        Intent data = new Intent(this, TextValidationActivity.class);
//        data.putExtra(TEXT_KEY,mCode);
//        data.putExtra(getString(R.string.request_code),REQUEST_EDIT_CODE_EXISTING);
//        startActivityForResult(data,REQUEST_EDIT_CODE_EXISTING);
//
//    }

//    public void COMPILE(View view) {
//
//        new TextUploadAsyncTask().execute(mCode);
//    }

    /*public void FORMAT(View view) {

        Jalopy formatter = new Jalopy();

        formatter.setInput(mCode,"");


        StringBuffer output = new StringBuffer();

        formatter.setOutput(output);

        formatter.format();

        mCode = output.toString();
        updatePreview();
    }*/
//
//    protected class TextRecognizerAsyncTask extends AsyncTask<Void,Void,ArrayList<String>> {
//
//
//        protected Context mContext;
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            mContext = getApplicationContext();
//            startProgressBar();
//        }
//
//        @Override
//        protected ArrayList<String> doInBackground(Void... voids) {
//
//            Bitmap img = BitmapFactory.decodeFile(mCurrentPhotoPath);
//            Frame frame = new Frame.Builder().setBitmap(img).build();
//
//            TextRecognizer textRecognizer = new TextRecognizer.Builder(mContext).build();
//
//            if (!textRecognizer.isOperational()) {
//                Log.w(LOG_TAG, "Detector dependencies are not yet available.");
//
//                // Check for low storage.  If there is low storage, the native library will not be
//                // downloaded, so detection will not become operational.
//                IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
//                boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;
//
//                if (hasLowStorage) {
//                    Toast.makeText(mContext, R.string.low_storage_error, Toast.LENGTH_LONG).show();
//                    Log.w(LOG_TAG, getString(R.string.low_storage_error));
//                }
//            }
//
//            SparseArray<TextBlock> text = textRecognizer.detect(frame);
//
//            ArrayList<String> textList = new ArrayList<>();
//
//            if(text != null) {
//                Log.v(LOG_TAG, "Saving text - size: "+text.size());
//                for(int i=0; i<text.size(); i++)
//                    if(text.get(i) != null)
//                        textList.add(text.get(i).getValue());
//            }
//
//            textRecognizer.release();
//            return textList;
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<String> strings) {
//            super.onPostExecute(strings);
//            Intent data = new Intent(mContext, TextValidationActivity.class);
//            data.putStringArrayListExtra(TEXT_KEY,strings);
//            data.putExtra(getString(R.string.request_code),REQUEST_EDIT_CODE);
//            startActivityForResult(data,REQUEST_EDIT_CODE);
//
//            Button edit = (Button) findViewById(R.id.edit_button);
//            if(edit.getVisibility() != View.VISIBLE)
//                enableOptions();
//        }
//    }
//
//    protected class TextUploadAsyncTask extends AsyncTask<String, Void, String> {
//
//        protected Context mContext;
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            mContext = getApplicationContext();
//            startProgressBar();
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//
//            ApiConnection con = new ApiConnection(API_URL)
//                    .buildUpon()
//                    .appendPath(strings[0])
//                    .build();
//
//            return con.connect("GET");
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//            Intent intent = new Intent(mContext, OutputActivity.class);
//            intent.putExtra(getString(R.string.output_key),s);
//
//            startActivityForResult(intent,REQUEST_OUTPUT_VIEW);
//        }
//    }
}
