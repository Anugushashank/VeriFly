package com.example.aparna.buddy.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shashank on 06-06-2016.
 */

public class UploadDocModel {

    private String userid;
    private String name, phone, college, fbUserId, date, friendName, friendNumber;
    private NonFrontAndBackDocs bankStatement, bankProof, gradeSheet;
    private FrontAndBackDocs collegeID, addressProof;
    private FrontBackImage panProof;
    private List<String> selfie, signature;
    private String taskStatus;

    public UploadDocModel(){}

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

    public String getDate() {
        return "May 17";
    }

    public void setUserid(String userid){ this.userid = userid; }

    public String getUserid() { return userid; }

    public List<String> getSelfie() { return selfie; }

    public List<String> getSignature() { return signature; }

    public String getTaskStatus() { return taskStatus; }

    public void setTaskStatus(String taskStatus) { this.taskStatus = taskStatus; }

    public void setBankStatement(NonFrontAndBackDocs bankStatement){ this.bankStatement = bankStatement; }

    public void setBankProof(NonFrontAndBackDocs bankProof){
        this.bankProof = bankProof;
    }

    public void setGradeSheet(NonFrontAndBackDocs gradeSheet){
        this.gradeSheet = gradeSheet;
    }

    public NonFrontAndBackDocs getBankProof() { return bankProof; }

    public NonFrontAndBackDocs getGradeSheet() { return gradeSheet; }

    public void setCollegeID(FrontAndBackDocs collegeID){
        this.collegeID = collegeID;
    }

    public FrontAndBackDocs getCollegeID() { return collegeID; }

    public void setAddressProof(FrontAndBackDocs addressProof){ this.addressProof = addressProof;}

    public FrontAndBackDocs getAddressProof() { return addressProof; }

    public class NonFrontAndBackDocs{
        private List<String> validImgUrls = new ArrayList<>();
        private List<String> invalidImgUrls = new ArrayList<>();
        private List<String> imgUrls = new ArrayList<>();
        private String type;
        private Boolean isVerified;
        private String verifiedBy;

        public void setValidImgUrls(ArrayList<ImageBox> arrayList){
            for(int i=0 ; i < arrayList.size(); i++){
                if(arrayList.get(i).getMatch().equals("valid")) {
                    this.validImgUrls.add(arrayList.get(i).getImageUrl());
                }
            }
        }

        public void setInvalidImgUrls(ArrayList<ImageBox> arrayList){
            for(int i=0 ; i < arrayList.size(); i++){
                if(arrayList.get(i).getMatch().equals("invalid")) {
                    this.invalidImgUrls.add(arrayList.get(i).getImageUrl());
                }
            }
        }

        public void setImgUrls(ArrayList<ImageBox> arrayList){
            for(int i=0 ; i<arrayList.size(); i++) {
                if (arrayList.get(i).getMatch().equals("img")) {
                    this.imgUrls.add(arrayList.get(i).getImageUrl());
                }
            }
        }

        public void setVerifiedBy(String verifiedBy){
            this.verifiedBy = verifiedBy;
        }

        public void setIsVerified(Boolean isVerified) { this.isVerified = isVerified; }

        public Boolean getIsVerified() { return isVerified; }

        public String getVerifiedBy() {
            return verifiedBy;
        }

        public List<String> getValidImgUrls(){
            return validImgUrls;
        }

        public List<String> getInvalidImgUrls() {
            return invalidImgUrls;
        }

        public List<String> getImgUrls() { return imgUrls; }


    }

    public class FrontAndBackDocs{
        private FrontBackImage front;
        private FrontBackImage back;
        private List<String> imgUrls = new ArrayList<>();

        public void setFront(FrontBackImage front){
            this.front = front;
        }

        public FrontBackImage getFront() { return front; }

        public void setBack(FrontBackImage back){
            this.back = back;
        }

        public FrontBackImage getBack() { return back; }

        public List<String> getImgUrls(){ return imgUrls; }

        public void setImgUrls(ArrayList<ImageBox> arrayList){
            for(int i=0 ; i<arrayList.size(); i++) {
                if (arrayList.get(i).getMatch().equals("img")) {
                    this.imgUrls.add(arrayList.get(i).getImageUrl());
                }
            }
        }

    }

    public class FrontBackImage {

        private String imgUrl;
        private Boolean isVerified;
        private String verifiedBy;

        public String getImgUrl() {
            return imgUrl;
        }

        public String getVerifiedBy() {
            return verifiedBy;
        }

        public void setVerifiedBy(String verifiedBy){
            this.verifiedBy = verifiedBy;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public Boolean getIsVerified() {
            return isVerified;
        }

        public void setIsVerified(Boolean verified) {
            this.isVerified = verified;
        }
    }

}

