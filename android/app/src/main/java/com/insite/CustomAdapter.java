package com.insite;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by atopka on 4/8/2017.
 */

public class CustomAdapter extends BaseAdapter {
    String[] names;
    String[] addresses;
    Bitmap[] images;
    Context context;

    private static LayoutInflater inflater = null;

    //constructor4
    public CustomAdapter(LocationList locationList, String[] nameList, String[] addressList, Bitmap[] imageList) {

        names = nameList;
        addresses = addressList;
        images = imageList;
        context = locationList;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        TextView tvName;
        TextView tvAddress;
        ImageView img;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.location_list, null);

        holder.tvName = (TextView) rowView.findViewById(R.id.textViewName);
        holder.tvAddress = (TextView) rowView.findViewById(R.id.textViewAddress);
        holder.img = (ImageView) rowView.findViewById(R.id.imageView);

        holder.tvName.setText(names[position]);
        holder.tvAddress.setText(addresses[position]);
//        holder.img.setImageBitmap(images[position]);


        //TEST
//        Drawable d = context.getResources().getDrawable(android.R.drawable.placeholder);
        holder.img.setImageResource(R.drawable.placeholder);

        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LocationInfo.class);
                context.startActivity(intent);
            }
        });
        rowView.setTag(holder);
        return rowView;
    }
}
