package com.example.aparna.buddy.model;

import java.util.List;

/**
 * Created by Aparna on 24/4/16.
 */
public class UserInfo {
    String name, phone, college, fbUserId, date, friendName, friendNumber;
    List<String> collegeIDs, bankStatements, addressProofs, gradeSheets, bankProofs;

    public UserInfo() {}

    public String getName() {
        return name;
    }

    public String getFriendName() { return friendName; }

    public String getFriendNumber() { return friendNumber; }

    public String getPhone() {
        return phone;
    }

    public String getCollege() {
        return college;
    }

    public String getFbUserId() {
        return fbUserId;
    }

    public List<String> getCollegeIDs() {
        return collegeIDs;
    }

    public List<String> getBankStatements() {
        return bankStatements;
    }

    public List<String> getAddressProofs() {
        return addressProofs;
    }

    public List<String> getGradeSheets() { return gradeSheets; }

    public List<String> getBankProofs() { return bankProofs; }

    public String getDate() {
        return "May 17";
    }
}
