package com.insite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, ImageUploadTest.class);
        startActivity(intent);

    }

    public void UPLOAD_ACTIVITY(View view) {


//        intent.putExtra(LocationInfo.TITLE,"Courant Institute of Mathematical Sciences");


    }

    public void LIST_ACTIVITY(View view) {
        Intent intent = new Intent(this, LocationList.class);
        startActivity(intent);

    }
}
