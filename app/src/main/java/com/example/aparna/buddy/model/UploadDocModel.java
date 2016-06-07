package com.example.aparna.buddy.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shashank on 06-06-2016.
 */



/**
 * Created by rock on 6/4/16.
 */
public class UploadDocModel {

    private String userid;
    private NonFrontAndBackDocs bankStatement, bankProof, gradeSheet;
    private FrontAndBackDocs collegeID, addressProof;

    public UploadDocModel(){}

    public void setUserid(String userid){ this.userid = userid; }

    public String getUserid() { return userid; }

    public void setBankStatement(NonFrontAndBackDocs bankStatement){ this.bankStatement = bankStatement; }

    public void setBankProof(NonFrontAndBackDocs bankProof){
        this.bankProof = bankProof;
    }

    public void setGradeSheet(NonFrontAndBackDocs gradeSheet){
        this.gradeSheet = gradeSheet;
    }

    public void setCollegeID(FrontAndBackDocs collegeID){
        this.collegeID = collegeID;
    }

    public void setAddressProof(FrontAndBackDocs addressProof){ this.addressProof = addressProof;}

    public class NonFrontAndBackDocs{
        private List<String> validImgUrls = new ArrayList<>();
        private List<String> invalidImgUrls = new ArrayList<>();
        private String verifiedBy;

        public void setValidImgUrls(ArrayList<ImageBox> arrayList){
            for(int i=0 ; i < arrayList.size()-1 ; i++){
                if(!arrayList.get(i).getVerified().equals("invalid")) {
                    this.validImgUrls.add(arrayList.get(i).getImageUrl());
                }
            }
        }

        public void setInvalidImgUrls(ArrayList<ImageBox> arrayList){
            for(int i=0 ; i < arrayList.size()-1 ; i++){
                if(arrayList.get(i).getVerified().equals("invalid")) {
                    this.invalidImgUrls.add(arrayList.get(i).getImageUrl());
                }
            }
        }

        public void setVerifiedBy(String verifiedBy){
            this.verifiedBy = verifiedBy;
        }

        public String getVerifiedBy() {
            return verifiedBy;
        }

        public List<String> getValidImgUrls(){
            return validImgUrls;
        }

        public List<String> getInvalidImgUrls() {
            return invalidImgUrls;
        }


    }

    public class FrontAndBackDocs{
        private FrontBackImage front;
        private FrontBackImage back;
        private List<String> validImgUrls = new ArrayList<>();
        private List<String> invalidImgUrls = new ArrayList<>();

        public void setFront(FrontBackImage front){
            this.front = front;
        }

        public void setBack(FrontBackImage back){
            this.back = back;
        }

        public void setValidImgUrls(ArrayList<ImageBox> arrayList){
            for(int i=0 ; i < arrayList.size()-1 ; i++){
                if(arrayList.get(i).getVerified().equals("valid")) {
                    Log.i("error","kjdfsgfdgsfhgdsdshfdgfdhfd");
                    this.validImgUrls.add(arrayList.get(i).getImageUrl());
                }
            }
        }

        public void setInvalidImgUrls(ArrayList<ImageBox> arrayList){
            for(int i=0 ; i < arrayList.size()-1 ; i++){
                if(arrayList.get(i).getVerified().equals("invalid")) {
                    Log.i("error","kjdfsgfdgsfhgdsdshfdgfdhfd");
                    this.invalidImgUrls.add(arrayList.get(i).getImageUrl());
                }
            }
        }

        public List<String> getValidImgUrls(){
            return validImgUrls;
        }

        public List<String> getInvalidImgUrls() {
            return invalidImgUrls;
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

