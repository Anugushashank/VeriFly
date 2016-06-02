package com.example.aparna.buddy.model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.aparna.buddy.app.BorrowerDetailsActivity;
import com.example.aparna.buddy.app.HomeActivity;
import com.example.aparna.buddy.app.R;
import com.example.aparna.buddy.app.TokenService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
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

    public BorrowerDataUpdater(BorrowerDetailsActivity activity) {
        this.activity = activity;
    }

    public void updateToApi() {

        String referenceIsGoodFriend =  activity.getGoodFriendsRadio();
        String referenceYear = activity.getRefYear().toString();
        String referenceDepartment = activity.getRefDept().toString();
        String punctualityInClass = activity.getSpinnerPunc();
        String sincerityInStudies = activity.getSpinnerSincere();
        String coCurricularParticipation = activity.getSpinnerCocurricular();
        String financiallyResponsible = activity.getSpinnerFinRes();
        String friendVerificationNotes = activity.getOtherNotes().toString();
        String yearBackTrue = activity.getYearBackRadio();
        String loanRepay = activity.repayBackLoan();
        String transparentTrue = activity.getTransparentRadio();
        String loanRepayTrue = activity.getGiveLoanRadio();


        Boolean collegeIdFrontImageBoxVerified = activity.getCollegeIdFrontImageBox().verified();
        Boolean collegeIdBackImageBoxVerified = activity.getCollegeIdBackImageBox().verified();
        Boolean addressProofBackImageBoxVerified = activity.getAddressProofBackImageBox().verified();
        Boolean addressProofFrontImageBoxVerified = activity.getAddressProofFrontImageBox().verified();

        Boolean collegeIdVerified = false;
        if(collegeIdFrontImageBoxVerified && collegeIdBackImageBoxVerified){
            collegeIdVerified = true;
        }

        Boolean addressProofVerified = false;
        if(addressProofBackImageBoxVerified && addressProofFrontImageBoxVerified){
            addressProofVerified = true;
        }


        

        // update code will go here
        new AsyncTask<Void, String, String>() {
            private Exception asyncException;
            private Context context = activity;
            MaterialDialog materialDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                materialDialog = new MaterialDialog.Builder(activity)
                        .content(context.getResources().getString(R.string.uploading_documents))
                        .progress(true, 0)
                        .show();
            }

            @Override
            protected String doInBackground(Void... v) {
                try {
                    String token = TokenService.getToken(context, activity);

                    if (token == null) {
                        throw new Exception();
                    }

                    OkHttpClient client = new OkHttpClient();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("userid",userInfo.getPhone());


                    HttpUrl url = new HttpUrl.Builder()
                            .scheme("http")
                            .host(context.getResources().getString(R.string.api_host))
                            .addPathSegments(context.getResources().getString(R.string.upload_path))
                            .build();

                    Request request = new Request.Builder()
                            .url(url)
                            .header("x-access-token", token)
                            .addHeader("content-type", "application/json")
                            .post(RequestBody.create(BuddyConstants.MEDIA_TYPE_JSON, jsonObject.toString()))
                            .build();

                    Response response = client.newCall(request).execute();
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    return response.body().string();
                } catch (Exception e) {
                    asyncException = e;
                    return "";
                }

            }

            @Override
            protected void onPostExecute(String apiResponse) {
                if (materialDialog.isShowing()) {
                    materialDialog.hide();
                }

                Type type = new TypeToken<Map<String, String>>(){}.getType();
                Map<String, String> responseMap = new Gson().fromJson(apiResponse, type);

                if (asyncException != null || !responseMap.get("status").equals("success")) {
                    // here tell that login request has thrown exception and ask to try again
                    CharSequence text = context.getResources().getString(R.string.upload_failed);
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else {
                    CharSequence text = context.getResources().getString(R.string.upload_success);
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    activity.finish();
                }
            }
        }.execute();
    }
}
