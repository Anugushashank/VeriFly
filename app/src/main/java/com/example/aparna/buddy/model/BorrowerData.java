package com.example.aparna.buddy.model;


/**
 * Created by Aparna on 17-Apr-16.
 */
public class BorrowerData {

    VerificationInfo verificationInfo;
    UserInfo userInfo;
    String _id, userId;

    public BorrowerData() { }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public VerificationInfo getVerificationInfo() {
        return verificationInfo;
    }

    public String getUserId() {
        return userId;
    }
}
