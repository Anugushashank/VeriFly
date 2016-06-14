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
    BorrowerData borrowerData;
    public IntercomModel(BorrowerData borrowerData){
        this.borrowerData = borrowerData;
    }
    public void sendData(){

        Map userMap = new HashMap ();
        userMap.put("assignedTo",borrowerData.getAssignedTo());
        userMap.put("assignedToName",borrowerData.getAssignedToName());
        userMap.put("assignedToCollege",borrowerData.getAssignedToCollege());
        userMap.put("taskType",borrowerData.getTaskType());
        userMap.put("scheduleDate", borrowerData.getScheduleDate());
        userMap.put("assignDate", borrowerData.getAssignDate());
        Map customAttributes = new HashMap ();
        customAttributes.put("name",borrowerData.getUploadDocModel().getName());
        customAttributes.put("phone",borrowerData.getUploadDocModel().getPhone());
        customAttributes.put("userCollege", borrowerData.getUploadDocModel().getCollege());
        customAttributes.put("friendName",borrowerData.getUploadDocModel().getFriendName());
        customAttributes.put("friendNumber",borrowerData.getUploadDocModel().getFriendNumber());
        customAttributes.put("referenceYear",borrowerData.getVerificationInfo().getReferenceYear());
        customAttributes.put("referenceDepartment",borrowerData.getVerificationInfo().getReferenceDepartment());
        userMap.put("custom_attributes", customAttributes);
        Intercom.client().updateUser(userMap);
    }
}
