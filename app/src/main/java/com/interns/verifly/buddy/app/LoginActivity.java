package com.interns.verifly.buddy.app;


import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.interns.verifly.buddy.model.ApiResponse;
import com.interns.verifly.buddy.model.BuddyConstants;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.intercom.android.sdk.Intercom;
import io.intercom.android.sdk.identity.Registration;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    EditText phoneEmail, password;
    String userid;
    AlertDialog alertDialog;
    RelativeLayout relativeLayout;
    TextView textView, textView1;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        SharedPreferences settings = getSharedPreferences(BuddyConstants.PREFS_FILE, 0);

        //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
        boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);

        relativeLayout = (RelativeLayout)findViewById(R.id.login_layout);
        textView = (TextView) findViewById(R.id.forgotPassword);
        textView1 = (TextView) findViewById(R.id.invalidPhone);

        if (hasLoggedIn) {
            if (!isNetworkConnected()) {
                alertBox();
                return;
            }

            String phoneEmailString = settings.getString("phoneEmail","");
            String passwordString = settings.getString("password","");

            if(!phoneEmailString.isEmpty() && !passwordString.isEmpty()) {
                doLogin(phoneEmailString, passwordString);
            }
            else{

                relativeLayout.setVisibility(View.VISIBLE);
            }
        }
        else {

            relativeLayout.setVisibility(View.VISIBLE);
        }

        phoneEmail    = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText2);

        try {
            Intercom.initialize((Application) getApplicationContext(), "android_sdk-a252775c0f9cdd6cd922b6420a558fd2eb3f89b0", "utga6z2r");
        }
        catch(Exception e){

        }
    }

    public void onClick(View v) {
        try {
            String phoneEmailString = phoneEmail.getText().toString();
            String passString = password.getText().toString();

            if (phoneEmailString.isEmpty()) {
                phoneEmail.setText("");
                phoneEmail.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                textView1.setText(getResources().getString(R.string.enter_phone));
                textView1.setVisibility(View.VISIBLE);
            } else if (phoneEmailString.length() < 10) {
                phoneEmail.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                textView1.setText(getResources().getString(R.string.not_phone));
                textView1.setVisibility(View.VISIBLE);
            } else if (passString.isEmpty()) {
                password.setText("");
                password.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                textView1.setText(getResources().getString(R.string.enter_pass));
                textView1.setVisibility(View.VISIBLE);
            } else {
                textView1.setVisibility(View.INVISIBLE);
                if (!isNetworkConnected()) {
                    Context context = getApplicationContext();
                    CharSequence text = getResources().getString(R.string.no_internet_connection);
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    return;
                }

                doLogin(phoneEmailString, passString);
            }
        }
        catch (Exception e){
            CharSequence text = getResources().getString(R.string.went_wrong);
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
            toast.show();
        }
    }

    public void resetPassword(View view){

        final AlertDialog builder = new AlertDialog.Builder(this,R.style.DialogStyle).create();

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.reset_password,(ViewGroup)findViewById(android.R.id.content), false);
        builder.setView(viewInflated);

        builder.setTitle("Reset Password");
        builder.setMessage("Enter your Phone number. OTP will be sent to this number.");


        final EditText input = (EditText) viewInflated.findViewById(R.id.input);

        builder.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setButton(AlertDialog.BUTTON_NEGATIVE,"Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();

        builder.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                try {
                    if (input.getText() != null && input.getText().length() == 10) {
                        builder.dismiss();
                        sendOTP(input.getText().toString());
                    }
                }
                catch(Exception e){
                    CharSequence text = getResources().getString(R.string.went_wrong);
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();
                }
            }
        });

    }

    private void doLogin(final String phoneEmail, final String password) {
        final SharedPreferences settings = getSharedPreferences(BuddyConstants.PREFS_FILE, 0); // 0 - for private mode

        new AsyncTask<Void, String, String>() {
            private Exception asyncException;
            private Context context = getApplicationContext();
            MaterialDialog materialDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                materialDialog = new MaterialDialog.Builder(LoginActivity.this)
                                     .content(getResources().getString(R.string.login_dialogue))
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
                    String token = TokenService.getToken(getApplicationContext(), LoginActivity.this);

                    if (token == null) {
                        throw new Exception();
                    }

                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(10, TimeUnit.SECONDS)
                            .build();

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("userid", phoneEmail);
                    jsonObject.put("password", password);

                    HttpUrl url = new HttpUrl.Builder()
                                             .scheme("http")
                                             .host(getResources().getString(R.string.api_host))
                                             .addPathSegments(getResources().getString(R.string.login_path))
                                             .build();

                    Request request = new Request.Builder()
                            .url(url)
                            .header("x-access-token", token)
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
            protected void onPostExecute(String apiResponseString) {
                if (materialDialog.isShowing()) {
                    materialDialog.dismiss();
                }
                if (asyncException != null) {
                    connectionTimeOut();
                    return;
                }

                try {
                    ApiResponse apiResponse = new Gson().fromJson(apiResponseString, ApiResponse.class);


                    if (asyncException != null || !apiResponse.getStatus().equals("success")) {

                        CharSequence text = getResources().getString(R.string.invalid_creds);
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        relativeLayout.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.VISIBLE);

                    } else if (!apiResponse.getData().getIsActive()) {

                        successfulLogin(phoneEmail, "inactive");
                        alertDialog = new android.support.v7.app.AlertDialog.Builder(LoginActivity.this, R.style.DialogStyle)
                                .setMessage("You have been deactivated :( . Please contact Buddy.")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        SharedPreferences settings = getSharedPreferences(BuddyConstants.PREFS_FILE, 0); // 0 - for private mode
                                        SharedPreferences.Editor editor = settings.edit();

                                        editor.clear();
                                        editor.apply();

                                        relativeLayout = (RelativeLayout) findViewById(R.id.login_layout);
                                        relativeLayout.setVisibility(View.VISIBLE);
                                    }
                                })
                                .show();
                    } else {
                        SharedPreferences.Editor editor = settings.edit();

                        editor.putBoolean("hasLoggedIn", true);
                        editor.putString("userid", apiResponse.getData().getUserid());
                        editor.putString("password", password);
                        editor.putString("phoneEmail", phoneEmail);
                        successfulLogin(phoneEmail, "active");

                        editor.apply();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        LoginActivity.this.finish();
                    }
                }
                catch(Exception e){
                    CharSequence text = getResources().getString(R.string.went_wrong);
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();
                }
            }
        }.execute();

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
    private void alertBox(){
        alertDialog = new AlertDialog.Builder(this,R.style.DialogStyle)
                .setTitle("Check your Network Connection")
                .setMessage("Try connecting again")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                        if(isNetworkConnected()) {
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
                .setTitle("Something went wrong")
                .setMessage("Try connecting again")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                        if(isNetworkConnected()) {
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

    private void enterOTP(final String phoneNumber){
        final AlertDialog builder = new AlertDialog.Builder(this,R.style.DialogStyle).create();

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.enter_otp,(ViewGroup)findViewById(android.R.id.content), false);

        final EditText input = (EditText) viewInflated.findViewById(R.id.input);

        builder.setView(viewInflated);
        builder.setMessage("Enter the OTP you have received.");
        builder.setButton(AlertDialog.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setButton(AlertDialog.BUTTON_NEUTRAL,"Resend OTP", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                sendOTP(phoneNumber);
            }
        });

        builder.show();
        builder.setCanceledOnTouchOutside(false);

        builder.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                try {
                    if (input.getText() != null && input.getText().length() == 4) {
                        builder.cancel();
                        verifyOTP(input.getText().toString(), phoneNumber);
                    }
                }
                catch(Exception e){
                    CharSequence text = getResources().getString(R.string.went_wrong);
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();
                }
            }
        });
    }

    private void sendOTP(final String phoneNumber){
        new AsyncTask<Void, String, String>() {
            private Exception asyncException;
            private Context context = getApplicationContext();
            MaterialDialog materialDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                materialDialog = new MaterialDialog.Builder(LoginActivity.this)
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
                    String token = TokenService.getToken(getApplicationContext(), LoginActivity.this);

                    if (token == null) {
                        throw new Exception();
                    }

                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(10, TimeUnit.SECONDS)
                            .build();

                    JSONObject jsonObject = new JSONObject();

                    HttpUrl url = new HttpUrl.Builder()
                            .scheme("http")
                            .host(getResources().getString(R.string.api_host))
                            .addPathSegments(getResources().getString(R.string.send_otp))
                            .addQueryParameter("phone",phoneNumber)
                            .build();

                    Request request = new Request.Builder()
                            .url(url)
                            .header("x-access-token", token)
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
            protected void onPostExecute(String apiResponseString) {
                if (materialDialog.isShowing()) {
                    materialDialog.dismiss();
                }

                try {
                    ApiResponse apiResponse = new Gson().fromJson(apiResponseString, ApiResponse.class);


                    if (asyncException != null || !apiResponse.getStatus().equals("success")) {

                        if (!isNetworkConnected()) {
                            CharSequence text = getResources().getString(R.string.no_internet_connection);
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                        else if(asyncException != null){
                            CharSequence text = getResources().getString(R.string.went_wrong);
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                        else {
                            CharSequence text = getResources().getString(R.string.not_registered);
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }

                    } else {
                        enterOTP(phoneNumber);
                    }
                }
                catch(Exception e){
                    CharSequence text = getResources().getString(R.string.went_wrong);
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();
                }
            }
        }.execute();
    }

    private void verifyOTP(final String otp, final String phoneNumber){
        new AsyncTask<Void, String, String>() {
            private Exception asyncException;
            private Context context = getApplicationContext();
            MaterialDialog materialDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                materialDialog = new MaterialDialog.Builder(LoginActivity.this)
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
                    String token = TokenService.getToken(getApplicationContext(), LoginActivity.this);

                    if (token == null) {
                        throw new Exception();
                    }

                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(10, TimeUnit.SECONDS)
                            .build();

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("phone",phoneNumber);
                    jsonObject.put("otp",otp);

                    HttpUrl url = new HttpUrl.Builder()
                            .scheme("http")
                            .host(getResources().getString(R.string.api_host))
                            .addPathSegments(getResources().getString(R.string.verify_otp))
                            .build();

                    Request request = new Request.Builder()
                            .url(url)
                            .header("x-access-token", token)
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
            protected void onPostExecute(String apiResponseString) {
                if (materialDialog.isShowing()) {
                    materialDialog.dismiss();
                }

                try {
                    ApiResponse apiResponse = new Gson().fromJson(apiResponseString, ApiResponse.class);


                    if (asyncException != null || !apiResponse.getStatus().equals("success")) {

                        if (!isNetworkConnected()) {
                            CharSequence text = getResources().getString(R.string.no_internet_connection);
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                        else if(asyncException != null){
                            CharSequence text = getResources().getString(R.string.went_wrong);
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                        else {
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, apiResponse.getMsg(), duration);
                            toast.show();
                        }
                        enterOTP(phoneNumber);

                    } else if (apiResponse.getStatus().equals("success")) {

                        Intent intent = new Intent(LoginActivity.this, ChangePassword.class);
                        intent.putExtra("reset", "yes");
                        intent.putExtra("userid", apiResponse.getData().getUserid());
                        startActivity(intent);
                        LoginActivity.this.finish();
                    }
                }
                catch(Exception e){
                    CharSequence text = getResources().getString(R.string.went_wrong);
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();
                }

            }
        }.execute();
    }

    private void successfulLogin(String username, String state){
        try {
            Intercom.client().registerIdentifiedUser(new Registration().withUserId(username));

            Map userMap = new HashMap();

            Map customAttributes = new HashMap();
            customAttributes.put("verifly_user_status", state);

            userMap.put("custom_attributes", customAttributes);

            Intercom.client().updateUser(userMap);
        }
        catch(Exception e){

        }
    }

}

