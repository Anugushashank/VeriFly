package com.example.aparna.buddy.model;

import android.content.SharedPreferences;

import com.example.aparna.buddy.app.BorrowerDetailsActivity;

import java.util.HashMap;
import java.util.Map;

import io.intercom.android.sdk.Intercom;

/**
 * Created by Shashank on 10-06-2016.
 */
public class IntercomModel {
    String username;
    BorrowerDetailsActivity activity;
    public IntercomModel(BorrowerDetailsActivity activity){
        this.activity = activity;
    }
    public void sendData(){
        SharedPreferences settings = activity.getSharedPreferences(BuddyConstants.PREFS_FILE, 0);
        username = settings.getString("username","");

        Map userMap = new HashMap ();
        userMap.put("userid",username);
        Map customAttributes = new HashMap ();
        customAttributes.put("phone",activity.getPhoneNum());
        customAttributes.put("scheduledOn", activity.getDate());
        customAttributes.put("monthly_spend", 155.5);
        customAttributes.put("team_mates", 3);
        userMap.put("custom_attributes", customAttributes);
        Intercom.client().updateUser(userMap);
    }
}
