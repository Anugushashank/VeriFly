package com.example.aparna.buddy.model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.aparna.buddy.app.BorrowerDetailsActivity;
import com.example.aparna.buddy.app.HomeActivity;
import com.example.aparna.buddy.app.R;
import com.example.aparna.buddy.app.TokenService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Aparna on 1/5/16.
 */
public class BorrowerDataUpdater {
    BorrowerDetailsActivity activity;
    UserInfo userInfo;
    UploadDocModel uploadDocModel;
    VerificationInfo verificationInfo;
    BorrowerStateContainer borrowerStateContainer;
    String submit, username;
    int conformDocs = 0, conformVerify = 0;

    public BorrowerDataUpdater(BorrowerDetailsActivity activity, String submit) {
        this.activity = activity;
        this.submit = submit;
    }

    public void updateToApi() {

        SharedPreferences settings = activity.getSharedPreferences(BuddyConstants.PREFS_FILE, 0);
        username = settings.getString("username","");

        String referenceIsGoodFriend =  activity.getGoodFriendsRadio();
        String phone = activity.getUploadDocModel().getPhone();
        String referenceYear = activity.getRefYear();
        String referenceDepartment = activity.getRefDept();
        String punctualityInClass = activity.getSpinnerPunc();
        String sincerityInStudies = activity.getSpinnerSincere();
        String coCurricularParticipation = activity.getSpinnerCocurricular();
        String financiallyResponsible = activity.getSpinnerFinRes();
        String friendVerificationNotes = activity.getOtherNotes().toString();
        String yearBackTrue = activity.getYearBackRadio();
        String loanRepay = activity.getRepayBackLoan();
        String transparentTrue = activity.getTransparentRadio();
        String loanRepayTrue = activity.getGiveLoanRadio();
        ArrayList<ImageBox> addressProofs = activity.getAddressProofs();
        ArrayList<ImageBox> collegeIds = activity.getCollegeIDs();
        ArrayList<ImageBox> bankProofs = activity.getBankProofs();
        ArrayList<ImageBox> gradeSheets = activity.getGradeSheets();






        verificationInfo = new VerificationInfo();

        if(referenceIsGoodFriend != null){
            if(referenceIsGoodFriend.equals("true")){
                verificationInfo.setReferenceIsGoodFriend(true);
            }
            else{
                verificationInfo.setReferenceIsGoodFriend(false);
            }
            conformVerify++;
        }

        if(referenceYear != null){
            verificationInfo.setReferenceYear(referenceYear);
            conformVerify++;
        }

        if(referenceDepartment != null){
            verificationInfo.setReferenceDepartment(referenceDepartment);
            conformVerify++;
        }

        if(punctualityInClass != null){
            verificationInfo.setPunctualityInClass(punctualityInClass);
            conformVerify++;
        }

        if(sincerityInStudies != null){
            verificationInfo.setSincerityInStudies(sincerityInStudies);
            conformVerify++;
        }

        if(coCurricularParticipation != null){
            verificationInfo.setCoCurricularParticipation(coCurricularParticipation);
            conformVerify++;
        }

        if(financiallyResponsible != null){
            verificationInfo.setFinanciallyResponsible(financiallyResponsible);
            conformVerify++;
        }

        if(friendVerificationNotes != null){
            verificationInfo.setFriendVerificationNotes(friendVerificationNotes);
            conformVerify++;
        }

        if(yearBackTrue != null){
            if(yearBackTrue.equals("true")){
                verificationInfo.setHaveYearBack(true);
            }
            else{
                verificationInfo.setHaveYearBack(false);
            }
            conformVerify++;
        }

        if(loanRepay != null){
            verificationInfo.setRepayCapacity(loanRepay);
            conformVerify++;
        }

        if(transparentTrue != null){
            if(transparentTrue.equals("true")){
                verificationInfo.setBorrowerIsLying(true);
            }
            else{
                verificationInfo.setBorrowerIsLying(false);
            }
            conformVerify++;
        }

        if(loanRepayTrue != null){
            if(loanRepayTrue.equals("true")){
                verificationInfo.setGiveHimLoan(true);
            }
            else{
                verificationInfo.setGiveHimLoan(false);
            }
            conformVerify++;
        }

        borrowerStateContainer = new BorrowerStateContainer(activity);

        if(borrowerStateContainer.isCompleted()){
            verificationInfo.setTaskStatus("completed");
        }
        else if(borrowerStateContainer.isOngoing()){
            verificationInfo.setTaskStatus("ongoing");
        }
        else{
            verificationInfo.setTaskStatus("new");
        }









        uploadDocModel = new UploadDocModel();

        uploadDocModel.setUserid(phone);

        if(bankProofs.size() > 1) {

            UploadDocModel.NonFrontAndBackDocs nonFrontAndBackDocsBankProof = uploadDocModel.new NonFrontAndBackDocs();

            nonFrontAndBackDocsBankProof.setInvalidImgUrls(bankProofs);
            nonFrontAndBackDocsBankProof.setValidImgUrls(bankProofs);
            nonFrontAndBackDocsBankProof.setVerifiedBy(username);
            nonFrontAndBackDocsBankProof.setIsVerified(true);
            uploadDocModel.setBankProof(nonFrontAndBackDocsBankProof);
            conformDocs++;
        }

        if(gradeSheets.size() > 1) {

            UploadDocModel.NonFrontAndBackDocs nonFrontAndBackDocsGradeSheet = uploadDocModel.new NonFrontAndBackDocs();

            nonFrontAndBackDocsGradeSheet.setInvalidImgUrls(gradeSheets);
            nonFrontAndBackDocsGradeSheet.setValidImgUrls(gradeSheets);
            nonFrontAndBackDocsGradeSheet.setVerifiedBy(username);
            nonFrontAndBackDocsGradeSheet.setIsVerified(true);
            uploadDocModel.setGradeSheet(nonFrontAndBackDocsGradeSheet);
            conformDocs++;
        }

        if(collegeIds.size() > 1) {

            UploadDocModel.FrontAndBackDocs frontAndBackDocsCollegeId = uploadDocModel.new FrontAndBackDocs();

            UploadDocModel.FrontBackImage frontBackImageFront = uploadDocModel.new FrontBackImage();

            UploadDocModel.FrontBackImage frontBackImageBack = uploadDocModel.new FrontBackImage();

            if(activity.getBackImageCollegeId() != null) {
                frontBackImageBack.setImgUrl(activity.getBackImageCollegeId());
                frontBackImageFront.setVerifiedBy(username);
                frontBackImageFront.setIsVerified(true);
                frontAndBackDocsCollegeId.setFront(frontBackImageFront);
            }
            if(activity.getFrontImageCollegeId() != null) {
                frontBackImageFront.setImgUrl(activity.getFrontImageCollegeId());
                frontBackImageFront.setVerifiedBy(username);
                frontBackImageFront.setIsVerified(true);
                frontAndBackDocsCollegeId.setBack(frontBackImageBack);
            }
            uploadDocModel.setCollegeID(frontAndBackDocsCollegeId);
            conformDocs++;
        }

        if(addressProofs.size() > 1) {

            UploadDocModel.FrontAndBackDocs frontAndBackDocsAddressProof = uploadDocModel.new FrontAndBackDocs();

            UploadDocModel.FrontBackImage frontBackImageFront = uploadDocModel.new FrontBackImage();

            UploadDocModel.FrontBackImage frontBackImageBack = uploadDocModel.new FrontBackImage();

            if(activity.getFrontImageAddressProof() != null) {
                frontBackImageFront.setImgUrl(activity.getFrontImageAddressProof());
                frontBackImageFront.setVerifiedBy(username);
                frontBackImageFront.setIsVerified(true);
                frontAndBackDocsAddressProof.setFront(frontBackImageFront);
            }
            if(activity.getBackImageAddressProof() != null) {
                frontBackImageBack.setImgUrl(activity.getBackImageAddressProof());
                frontBackImageFront.setVerifiedBy(username);
                frontBackImageFront.setIsVerified(true);
                frontAndBackDocsAddressProof.setBack(frontBackImageBack);
            }
            uploadDocModel.setAddressProof(frontAndBackDocsAddressProof);
            conformDocs++;
        }

        if(activity.getBackImageAddressProof() != null && activity.getFrontImageAddressProof() != null &&
                activity.getFrontImageCollegeId() != null && activity.getBackImageCollegeId() != null){
            conformDocs++;
        }








        // Documents Upload
        if(conformDocs > 0) {
            new AsyncTask<Void, String, String>() {
                private Exception asyncException;
                private Context context = activity;
                MaterialDialog materialDialog;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    if (submit.equals("button")) {
                        materialDialog = new MaterialDialog.Builder(activity)
                                .content(context.getResources().getString(R.string.uploading_documents))
                                .progress(true, 0)
                                .show();
                    }
                }

                @Override
                protected String doInBackground(Void... v) {
                    try {
                        String token = TokenService.getToken(context, activity);

                        if (token == null) {
                            throw new Exception();
                        }


                        OkHttpClient client = new OkHttpClient();
                        String jsonString = new Gson().toJson(uploadDocModel);

                        HttpUrl url = new HttpUrl.Builder()
                                .scheme("http")
                                .host(context.getResources().getString(R.string.api_host))
                                .addPathSegments(context.getResources().getString(R.string.uploadDoc_path))
                                .build();

                        Request request = new Request.Builder()
                                .url(url)
                                .header("x-access-token", token)
                                .addHeader("content-type", "application/json")
                                .post(RequestBody.create(BuddyConstants.MEDIA_TYPE_JSON, jsonString))
                                .build();

                        Response response = client.newCall(request).execute();
                        if (!response.isSuccessful())
                            throw new IOException("Unexpected code " + response);

                        return response.body().string();
                    } catch (Exception e) {
                        asyncException = e;
                        return "";
                    }

                }

                @Override
                protected void onPostExecute(String apiResponse) {
                    if (submit.equals("button")) {
                        if (materialDialog.isShowing()) {
                            materialDialog.hide();
                        }
                    }
                    ApiResponse apiResponse1 = new Gson().fromJson(apiResponse, ApiResponse.class);

                    if (asyncException != null) {
                        // here tell that login request has thrown exception and ask to try again
                        CharSequence text = context.getResources().getString(R.string.upload_failed);
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    } else {
                        activity.finish();
                    }
                }
            }.execute();
        }


        if(conformVerify > 0) {
            new AsyncTask<Void, String, String>() {
                private Exception asyncException;
                private Context context = activity;
                MaterialDialog materialDialog;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    if (submit.equals("button")) {
                        materialDialog = new MaterialDialog.Builder(activity)
                                .content(context.getResources().getString(R.string.uploading_documents))
                                .progress(true, 0)
                                .show();
                    }
                }

                @Override
                protected String doInBackground(Void... v) {
                    try {
                        String token = TokenService.getToken(context, activity);

                        if (token == null) {
                            throw new Exception();
                        }


                        OkHttpClient client = new OkHttpClient();
                        String jsonString = new Gson().toJson(verificationInfo);

                        HttpUrl url = new HttpUrl.Builder()
                                .scheme("http")
                                .host(context.getResources().getString(R.string.api_host))
                                .addPathSegments(context.getResources().getString(R.string.uploadVerify_path))
                                .build();

                        Request request = new Request.Builder()
                                .url(url)
                                .header("x-access-token", token)
                                .addHeader("content-type", "application/json")
                                .put(RequestBody.create(BuddyConstants.MEDIA_TYPE_JSON, jsonString))
                                .build();

                        Response response = client.newCall(request).execute();
                        if (!response.isSuccessful())
                            throw new IOException("Unexpected code " + response);

                        return response.body().string();
                    } catch (Exception e) {
                        asyncException = e;
                        return "";
                    }

                }

                @Override
                protected void onPostExecute(String apiResponse) {
                    if (submit.equals("button")) {
                        if (materialDialog.isShowing()) {
                            materialDialog.hide();
                        }
                    }
                    ApiResponse apiResponse1 = new Gson().fromJson(apiResponse, ApiResponse.class);

                    if (asyncException != null) {
                        // here tell that login request has thrown exception and ask to try again
                        CharSequence text = context.getResources().getString(R.string.upload_failed);
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    } else {
                        activity.finish();
                    }
                }
            }.execute();
        }
    }
}
