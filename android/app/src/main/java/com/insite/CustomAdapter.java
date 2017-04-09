//package com.insite;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
///**
// * Created by atopka on 4/8/2017.
// */
//
//public class CustomAdapter extends BaseAdapter {
//    String name;
//    String address;
//    String image;
//    Context context;
//
//    private static LayoutInflater inflater = null;
//
//    //constructor
//    public CustomAdapter(MainActivity mainActivity, String locationName, String locationAddress, String locationImage) {
//
//        name = locationName;
//        address = locationAddress;
//        image = locationImage;
//
//        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//    }
//
//    public int getCount() {
//        return name.length();
//    }
//
////    public getView(final int position, View convertView, ViewGroup parent) {
////        Holder holder = new Holder();
////        View rowView;
////            rowView = inflater.inflate(R.layout.location_list, null);
////            holder.tv = (TextView) rowView.findViewById(R.id.textViewName);
////            holder.tv = (TextView) rowView.findViewById(R.id.textViewAddress);
////            holder.img = (ImageView)
////    }
//}
