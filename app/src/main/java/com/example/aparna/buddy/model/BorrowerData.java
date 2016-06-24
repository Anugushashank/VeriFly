package com.example.aparna.buddy.model;


/**
 * Created by Shashank on 5-June-16.
 */
public class BorrowerData {

    String _id,  assignDate, scheduleDate, assignedTo, assignedToName, assignedToCollege, userId, userName, userCollege, distanceBwColleges, taskStatus,
            taskType, updatedAt, completionDate, taskStartDate, taskInProgressDate, taskCompletionDate;
    Boolean differentColleges;
    UploadDocModel userInfo;
    VerificationInfo verificationInfo;


    public BorrowerData() { }

    public UploadDocModel getUploadDocModel() { return userInfo; }

    public VerificationInfo getVerificationInfo() {
        return verificationInfo;
    }

    public String getUserId() {
        return userId;
    }

    public String get_id() { return _id; }

    public String getAssignDate() { return assignDate; }

    public String getScheduleDate() { return scheduleDate; }

    public String getAssignedTo() { return assignedTo; }

    public String getAssignedToName() { return assignedToName; }

    public String getAssignedToCollege() { return assignedToCollege; }

    public String getDistanceBwColleges() { return distanceBwColleges; }

    public String getTaskStatus() { return taskStatus; }

    public String getTaskType() { return taskType; }

    public Boolean getDifferentColleges() { return differentColleges; }

    public String getUpdatedAt() { return updatedAt; }

    public String getCompletionDate() { return completionDate; }
}
