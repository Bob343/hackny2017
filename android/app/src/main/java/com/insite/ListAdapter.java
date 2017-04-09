package com.insite;


import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

/**
 * Created by Chris on 9/4/17.
 *
 * @author Chris
 * @version 1.0
 */

public class ListAdapter extends ArrayAdapter<Holder> {

    public static final String LOG_TAG = ListAdapter.class.getSimpleName();

    private int layoutResource;
    protected Holder[] mHolders;

    protected int[] mColors = {Color.RED, Color.YELLOW, Color.BLUE, Color.GREEN};

    public ListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull Holder[] objects) {
        super(context, resource, objects);
        this.layoutResource = resource;
        mHolders = objects;
//        Log.v(LOG_TAG, "mHolders length: "+mHolders.length);
    }

    @Nullable
    @Override
    public Holder getItem(int position) {
        return mHolders[position];
    }

    @Override
    public int getCount() {
        return mHolders.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(layoutResource, null);
        }

        final Holder item = getItem(position);

        if(item != null) {
            TextView nameView = (TextView) view.findViewById(R.id.textViewName);
            TextView addressView = (TextView) view.findViewById(R.id.textViewAddress);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageViewRow);
            ProgressBar bar = (ProgressBar) view.findViewById(R.id.row_bar);

            bar.setProgressTintList(getBarColor(item.prog));
            bar.setProgress(item.prog);
            nameView.setText(item.tvName);
            addressView.setText(item.tvAddress);
            imageView.setImageBitmap(item.img);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(),LocationInfo.class);

                    intent.putExtra(LocationInfo.TITLE,item.tvName);
                    String path = createImageFromBitmap(item.img);
                    intent.putExtra(LocationInfo.IMAGE, path);

                    getContext().startActivity(intent);

                }
            });
        }

        return view;
    }

    public ColorStateList getBarColor(int prog) {
        // 0-25 Red
        // 26-50 Yellow
        // 51-75 Blue
        // 76-100 Green

        return ColorStateList.valueOf(mColors[prog/ 25]);

    }

    public String createImageFromBitmap(Bitmap bitmap) {
        String fileName = "myImage.jpg";//no .png or .jpg needed
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            FileOutputStream fo = getContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            fo.write(bytes.toByteArray());
            // remember close file output
            fo.close();
        } catch (Exception e) {
            Log.e(LOG_TAG,e.getLocalizedMessage());
            fileName = null;
        }
        return fileName;
    }
}

class Holder {
    String tvName;
    String tvAddress;
    Bitmap img;
    int prog;

    public String toString() {
        return tvName+": "+tvAddress;
    }
}
