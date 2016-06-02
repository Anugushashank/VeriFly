package com.example.aparna.buddy.model;

import android.util.Log;
import android.view.View;

import com.example.aparna.buddy.app.BorrowerDetailsActivity;

/**
 * Created by Aparna on 1/5/16.
 */
public class BorrowerStateContainer {
    private BorrowerDetailsActivity activity;

    public BorrowerStateContainer(BorrowerDetailsActivity activity) {
        this.activity = activity;
    }

    public void update() {
        Log.d("global","shashank");


    }
}
