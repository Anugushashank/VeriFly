package com.example.aparna.buddy.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.aparna.buddy.model.ApiResponse;
import com.example.aparna.buddy.model.BuddyConstants;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ChangePassword extends AppCompatActivity {
    EditText newPassword, confirmPassword;
    String stringPassword, stringConfirmPassword;
    TextView textView;
    Intent intent;
    String urlToken, userid;
    android.support.v7.app.AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        newPassword = (EditText)findViewById(R.id.password);
        confirmPassword = (EditText)findViewById(R.id.confirmPassword);
        textView = (TextView)findViewById(R.id.doNotMatch);

        intent = getIntent();
        urlToken = intent.getStringExtra("token");

        if(urlToken == null){
            urlToken = intent.getData().getQueryParameter("token");
        }

        sendToken(urlToken);

        setContentView(R.layout.activity_change_password);
    }

    public void onClick(View view){
        stringConfirmPassword = confirmPassword.getText().toString();
        stringPassword = newPassword.getText().toString();
        if(stringPassword.isEmpty() ){
            newPassword.setText("");
            newPassword.setHintTextColor(Color.RED);
            textView.setText(getResources().getString(R.string.enter_password));
            textView.setVisibility(View.VISIBLE);
        }
        else if(stringPassword.length()<4){
            newPassword.setText("");
            newPassword.setHintTextColor(Color.RED);
            textView.setText(getResources().getString(R.string.password_length));
            textView.setVisibility(View.VISIBLE);
        }
        else if(stringConfirmPassword.isEmpty()){
            confirmPassword.setText("");
            confirmPassword.setHintTextColor(Color.RED);
            textView.setText(getResources().getString(R.string.please_confirm_password));
            textView.setVisibility(View.VISIBLE);
        }
        else if(!stringPassword.equals(stringConfirmPassword)){
            confirmPassword.setText("");
            confirmPassword.setHintTextColor(Color.RED);
            textView.setText(getResources().getString(R.string.do_not_match));
            textView.setVisibility(View.VISIBLE);
        }
        else{
            textView.setVisibility(View.INVISIBLE);
            AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.DialogStyle);
            builder.setMessage("Are you sure you want to keep this password?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            changePassword(userid, stringPassword);
                        }
                    })
                    .setNegativeButton("No", null)						//Do nothing on no
                    .show();
        }
    }

    public void sendToken(final String urlToken){
        if(isNetworkConnected()) {
            new AsyncTask<Void, String, String>() {
                private Exception asyncException;
                private Context context = getApplicationContext();
                MaterialDialog materialDialog;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    materialDialog = new MaterialDialog.Builder(ChangePassword.this)
                            .content(getResources().getString(R.string.please_wait))
                            .progress(true, 0)
                            .canceledOnTouchOutside(false)
                            .show();

                    materialDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            finish();
                        }

                    });
                }

                @Override
                protected String doInBackground(Void... v) {
                    try {
                        String token = TokenService.getToken(getApplicationContext(), ChangePassword.this);

                        if (token == null) {
                            throw new Exception();
                        }

                        OkHttpClient client = new OkHttpClient.Builder()
                                .connectTimeout(10, TimeUnit.SECONDS)
                                .readTimeout(10, TimeUnit.SECONDS)
                                .build();

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("token", urlToken);

                        HttpUrl url = new HttpUrl.Builder()
                                .scheme("http")
                                .host(getResources().getString(R.string.api_host))
                                .addPathSegments(getResources().getString(R.string.password_token))
                                .build();

                        Request request = new Request.Builder()
                                .url(url)
                                .header("x-access-token", token)
                                .post(RequestBody.create(BuddyConstants.MEDIA_TYPE_JSON, jsonObject.toString()))
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
                protected void onPostExecute(String apiResponseString) {
                    if (materialDialog.isShowing()) {
                        materialDialog.dismiss();
                    }
                    if (asyncException != null) {
                        connectionTimeOut();
                        return;
                    }

                    ApiResponse apiResponse = new Gson().fromJson(apiResponseString, ApiResponse.class);


                    if (asyncException != null || !apiResponse.getStatus().equals("success")) {

                        CharSequence text = getResources().getString(R.string.invalid_token);
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        finish();

                    } else {

                        userid = apiResponse.getData().getUserid();
                    }
                }
            }.execute();
        }
        else{

            alertBox();
        }
    }

    public void changePassword(final String userid, final String password){
        if(isNetworkConnected()) {
            new AsyncTask<Void, String, String>() {
                private Exception asyncException;
                private Context context = getApplicationContext();
                MaterialDialog materialDialog;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    materialDialog = new MaterialDialog.Builder(ChangePassword.this)
                            .content(getResources().getString(R.string.please_wait))
                            .progress(true, 0)
                            .canceledOnTouchOutside(false)
                            .show();

                    materialDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            finish();
                        }

                    });
                }

                @Override
                protected String doInBackground(Void... v) {
                    try {
                        String token = TokenService.getToken(getApplicationContext(), ChangePassword.this);

                        if (token == null) {
                            throw new Exception();
                        }

                        OkHttpClient client = new OkHttpClient.Builder()
                                .connectTimeout(10, TimeUnit.SECONDS)
                                .readTimeout(10, TimeUnit.SECONDS)
                                .build();

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("userid", userid);
                        jsonObject.put("password",password);

                        HttpUrl url = new HttpUrl.Builder()
                                .scheme("http")
                                .host(getResources().getString(R.string.api_host))
                                .addPathSegments(getResources().getString(R.string.password_set))
                                .build();

                        Request request = new Request.Builder()
                                .url(url)
                                .header("x-access-token", token)
                                .post(RequestBody.create(BuddyConstants.MEDIA_TYPE_JSON, jsonObject.toString()))
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
                protected void onPostExecute(String apiResponseString) {
                    if (materialDialog.isShowing()) {
                        materialDialog.dismiss();
                    }
                    if (asyncException != null) {
                        connectionTimeOut();
                        return;
                    }

                    ApiResponse apiResponse = new Gson().fromJson(apiResponseString, ApiResponse.class);



                    if (asyncException != null || !apiResponse.getStatus().equals("success")) {

                        CharSequence text = getResources().getString(R.string.invalid_creds);
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    } else {

                        alertDialog = new android.support.v7.app.AlertDialog.Builder(ChangePassword.this,R.style.DialogStyle)
                                .setMessage("Password Changed Successfully.")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(isNetworkConnected()) {
                                            Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                                            startActivity(intent);

                                            ChangePassword.this.finish();
                                        }
                                        else{
                                            alertBox();
                                        }
                                    }
                                })
                                .show();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                finish();
                            }

                        });

                    }
                }
            }.execute();
        }
        else{

            alertBox();
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private void alertBox(){
        alertDialog = new android.support.v7.app.AlertDialog.Builder(this,R.style.DialogStyle)
                .setTitle("Check your Network Connection")
                .setMessage("Try connecting again")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(getApplicationContext(),ChangePassword.class);
                        if(isNetworkConnected()) {
                            intent.putExtra("token",urlToken);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            alertBox();
                        }
                    }
                })
                .setIcon(R.drawable.dialog_alert)
                .show();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }

        });
    }
    private void connectionTimeOut(){
        alertDialog = new android.support.v7.app.AlertDialog.Builder(this,R.style.DialogStyle)
                .setTitle("Unable to Connect")
                .setMessage("Try connecting again")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(getApplicationContext(),ChangePassword.class);
                        if(isNetworkConnected()) {
                            intent.putExtra("token",urlToken);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            alertBox();
                        }
                    }
                })
                .setIcon(R.drawable.dialog_alert)
                .show();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }

        });
    }
}
