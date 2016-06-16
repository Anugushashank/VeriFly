package com.example.aparna.buddy.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.intercom.android.sdk.Intercom;
import io.intercom.android.sdk.identity.Registration;


/**
 * Created by Shashank on 10-06-2016.
 */
public class IntercomModel {
    BorrowerData borrowerData;
    List<BorrowerData> allUsersData = new ArrayList<>();

    public IntercomModel(){}

    public void sendData(Map<Integer, String> allTabsData){

        for(int i = 0 ; i < 3 ; i++){

            Type type = new TypeToken<List<BorrowerData>>(){}.getType();
            List<BorrowerData> cardDataList = new Gson().fromJson(allTabsData.get(i), type);

            if(cardDataList != null) {
                for (int j = 0; j < cardDataList.size(); j++) {

                    allUsersData.add(cardDataList.get(j));
                }
            }
        }
        try {
            Intercom.client().reset();

            for (int i = 0; i < allUsersData.size(); i++) {

                borrowerData = allUsersData.get(i);

                Intercom.client().registerIdentifiedUser(new Registration().withUserId(borrowerData.getUserId()));

                Map userMap = new HashMap();

                Map customAttributes = new HashMap();

                customAttributes.put("_id", borrowerData.get_id());
                customAttributes.put("name", borrowerData.getUploadDocModel().getName());
                customAttributes.put("phone", borrowerData.getUploadDocModel().getPhone());
                customAttributes.put("college", borrowerData.getUploadDocModel().getCollege());
                customAttributes.put("friendName", borrowerData.getUploadDocModel().getFriendName());
                customAttributes.put("friendNumber", borrowerData.getUploadDocModel().getFriendNumber());
                customAttributes.put("distanceBwColleges", borrowerData.getDistanceBwColleges());
                customAttributes.put("differentColleges", borrowerData.getDifferentColleges());
                customAttributes.put("updatedAt", borrowerData.getUpdatedAt());
                customAttributes.put("completionDate", borrowerData.getCompletionDate());
                customAttributes.put("fbUserId", borrowerData.getUploadDocModel().getFbUserId());
                customAttributes.put("taskStatus", borrowerData.getTaskStatus());
                customAttributes.put("assignedTo", borrowerData.getAssignedTo());
                customAttributes.put("assignedToName", borrowerData.getAssignedToName());
                customAttributes.put("assignedToCollege", borrowerData.getAssignedToCollege());
                customAttributes.put("taskType", borrowerData.getTaskType());
                customAttributes.put("scheduleDate", borrowerData.getScheduleDate());
                customAttributes.put("assignDate", borrowerData.getAssignDate());
                customAttributes.put("referenceYear", borrowerData.getVerificationInfo().getReferenceYear());
                customAttributes.put("referenceDepartment", borrowerData.getVerificationInfo().getReferenceDepartment());

                userMap.put("custom_attributes", customAttributes);

                Intercom.client().updateUser(userMap);

                Intercom.client().reset();
            }
            Intercom.client().registerIdentifiedUser(new Registration().withUserId(borrowerData.getAssignedTo()));
        }
        catch(Exception e){

        }
    }
}
