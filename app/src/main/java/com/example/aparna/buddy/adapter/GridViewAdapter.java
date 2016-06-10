package com.example.aparna.buddy.adapter;

/**
 * Created by Shashank on 31-05-2016.
 */
import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aparna.buddy.app.BorrowerDetailsActivity;
import com.example.aparna.buddy.app.R;
import com.example.aparna.buddy.model.ImageBox;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter{

    private ImageBox imageBox;
    private FrameLayout imageLayout;
    ArrayList<ImageBox> imageBoxArrayList = new ArrayList<>();
    BorrowerDetailsActivity activity;
    private static LayoutInflater inflater = null;

    public GridViewAdapter(BorrowerDetailsActivity mactivity, ArrayList<ImageBox> imageBoxes) {
        activity = mactivity;
        imageBoxArrayList = imageBoxes;
        inflater = ( LayoutInflater )activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return imageBoxArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LinearLayout item = (LinearLayout) (convertView == null
                ? LayoutInflater.from(activity).inflate(R.layout.imagelayout_item, parent, false)
                : convertView);
        imageLayout = (FrameLayout)item.findViewById(R.id.imageLayout);
        imageBox = new ImageBox(imageLayout,activity);
        imageBox.setImageUrl(imageBoxArrayList.get(position).getImageUrl());
        imageBox.setPicType(imageBoxArrayList.get(position).getPicType());
        imageBox.setPicNum(imageBoxArrayList.get(position).getPicNum());
        imageBox.setCloudinaryId(imageBoxArrayList.get(position).getCloudinaryId());
        imageBox.setId(imageBoxArrayList.get(position).getId());
        imageBox.setVerified(imageBoxArrayList.get(position).getVerified());
        if(imageBoxArrayList.get(position).getVerified().equals("front")){
            imageLayout.findViewWithTag(activity.getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
            imageLayout.findViewWithTag(activity.getResources().getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
            TextView textView = (TextView) item.findViewById(R.id.textView);
            textView.setText("FrontSide");
            textView.setVisibility(View.VISIBLE);
        }
        if(imageBoxArrayList.get(position).getVerified().equals("back")){
            imageLayout.findViewWithTag(activity.getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
            imageLayout.findViewWithTag(activity.getResources().getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
            TextView textView = (TextView) item.findViewById(R.id.textView);
            textView.setText("BackSide");
            textView.setVisibility(View.VISIBLE);
        }
        if(position == imageBoxArrayList.size()-1){
            ImageView imageView = (ImageView) imageLayout.findViewWithTag(activity.getResources().getString(R.string.unverified_status_icon));
            imageView.setVisibility(View.INVISIBLE);
        }
        imageBox.inflate();
        return item;
    }

}

