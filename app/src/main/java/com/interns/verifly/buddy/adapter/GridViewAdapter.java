package com.interns.verifly.buddy.adapter;

/**
 * Created by Shashank on 31-05-2016.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.interns.verifly.buddy.app.BorrowerDetailsActivity;
import com.interns.verifly.buddy.app.R;
import com.interns.verifly.buddy.model.ImageBox;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter{

    ImageBox imageBox;
    FrameLayout imageLayout;
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
        return imageBoxArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LinearLayout item = (LinearLayout) (convertView == null ? LayoutInflater.from(activity).inflate(R.layout.imagelayout_item, parent, false) : convertView);
        try {
            imageLayout = (FrameLayout) item.findViewById(R.id.imageLayout);
            imageBox = new ImageBox(imageLayout, activity);
            imageBox.setImageUrl(imageBoxArrayList.get(position).getImageUrl());
            imageBox.setPicType(imageBoxArrayList.get(position).getPicType());
            imageBox.setPicNum(imageBoxArrayList.get(position).getPicNum());
            imageBox.setCloudinaryId(imageBoxArrayList.get(position).getCloudinaryId());
            imageBox.setId(imageBoxArrayList.get(position).getId());
            imageBox.setMatch(imageBoxArrayList.get(position).getMatch());
            imageBox.setPath(imageBoxArrayList.get(position).getPath());
            imageBox.setIsVerified(imageBoxArrayList.get(position).getIsVerified());


            if (imageBoxArrayList.get(position).getIsVerified() && imageBoxArrayList.get(position).getId() != 2) {
                if (imageBoxArrayList.get(position).getMatch().equals("front")) {
                    imageLayout.findViewWithTag(activity.getResources().getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                    TextView textView = (TextView) item.findViewById(R.id.textView);
                    textView.setText(activity.getResources().getString(R.string.front_side));
                    textView.setVisibility(View.VISIBLE);
                }
                if (imageBoxArrayList.get(position).getMatch().equals("back")) {
                    imageLayout.findViewWithTag(activity.getResources().getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                    TextView textView = (TextView) item.findViewById(R.id.textView);
                    textView.setText(activity.getResources().getString(R.string.back_side));
                    textView.setVisibility(View.VISIBLE);
                }
                if (imageBoxArrayList.get(position).getMatch().equals("valid")) {
                    imageLayout.findViewWithTag(activity.getResources().getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                    TextView textView = (TextView) item.findViewById(R.id.textView);
                    textView.setVisibility(View.INVISIBLE);
                }
                if (imageBoxArrayList.get(position).getMatch().equals("invalid")) {
                    imageLayout.findViewWithTag(activity.getResources().getString(R.string.invalid_status_icon)).setVisibility(View.VISIBLE);
                    TextView textView = (TextView) item.findViewById(R.id.textView);
                    textView.setVisibility(View.INVISIBLE);
                }
            } else if (imageBoxArrayList.get(position).getId() != 2) {
                if (imageBoxArrayList.get(position).getMatch().equals("front")) {
                    imageLayout.findViewWithTag(activity.getResources().getString(R.string.unverified_status_icon)).setVisibility(View.VISIBLE);
                    TextView textView = (TextView) item.findViewById(R.id.textView);
                    textView.setText(activity.getResources().getString(R.string.front_side));
                    textView.setVisibility(View.VISIBLE);
                }
                if (imageBoxArrayList.get(position).getMatch().equals("back")) {
                    imageLayout.findViewWithTag(activity.getResources().getString(R.string.unverified_status_icon)).setVisibility(View.VISIBLE);
                    TextView textView = (TextView) item.findViewById(R.id.textView);
                    textView.setText(activity.getResources().getString(R.string.back_side));
                    textView.setVisibility(View.VISIBLE);
                }
                if (imageBoxArrayList.get(position).getMatch().equals("valid")) {
                    imageLayout.findViewWithTag(activity.getResources().getString(R.string.unverified_status_icon)).setVisibility(View.VISIBLE);
                    TextView textView = (TextView) item.findViewById(R.id.textView);
                    textView.setVisibility(View.INVISIBLE);
                }
                if (imageBoxArrayList.get(position).getMatch().equals("invalid")) {
                    imageLayout.findViewWithTag(activity.getResources().getString(R.string.unverified_status_icon)).setVisibility(View.VISIBLE);
                    TextView textView = (TextView) item.findViewById(R.id.textView);
                    textView.setVisibility(View.INVISIBLE);
                }
                if (imageBoxArrayList.get(position).getMatch().equals("img")) {
                    imageLayout.findViewWithTag(activity.getResources().getString(R.string.unverified_status_icon)).setVisibility(View.VISIBLE);
                    TextView textView = (TextView) item.findViewById(R.id.textView);
                    textView.setVisibility(View.INVISIBLE);
                }
            }
        }
        catch(Exception e){

        }
        imageBox.inflate();
        return item;
    }

}

