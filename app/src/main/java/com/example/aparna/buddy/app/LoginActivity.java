package com.example.aparna.buddy.app;


import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.aparna.buddy.model.BuddyConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
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
    EditText phone, password;
    String username;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Intercom.initialize((Application) getApplicationContext(), "android_sdk-a252775c0f9cdd6cd922b6420a558fd2eb3f89b0", "utga6z2r");
        }
        catch(Exception e){

        }
        SharedPreferences settings = getSharedPreferences(BuddyConstants.PREFS_FILE, 0);

        //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
        boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);

        if (hasLoggedIn) {
            if (!isNetworkConnected()) {
                alertBox();
                return;
            }
            try {
                Intercom.client().registerIdentifiedUser(new Registration().withUserId(settings.getString("username", "")));
            }
            catch (Exception e){

            }
            Intent intent = new Intent(this, com.example.aparna.buddy.app.HomeActivity.class);
            startActivity(intent);
            this.finish();
        }
        setContentView(R.layout.activity_main);
        phone    = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText2);
    }

    public void onClick(View v) {
        String phoneString = phone.getText().toString();

        username          = phoneString;
        String passString = password.getText().toString();

        if (phoneString.isEmpty() || phoneString.length() != 10) {
            phone.setText("");
            phone.setHintTextColor(Color.RED);
        } else if (passString.isEmpty()) {
            password.setHintTextColor(Color.RED);
        } else {
            if (!isNetworkConnected()) {
                Context context   = getApplicationContext();
                CharSequence text = getResources().getString(R.string.no_internet_connection);
                int duration      = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                return;
            }

            doLogin(phoneString, passString);
        }
    }

    private void doLogin(final String phone, final String password) {
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
                    jsonObject.put("userid", phone);
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
            protected void onPostExecute(String apiResponse) {
                if (materialDialog.isShowing()) {
                    materialDialog.dismiss();
                }
                if(asyncException != null){
                    connectionTimeOut();
                    return;
                }

                Type type = new TypeToken<Map<String, String>>(){}.getType();
                Map<String, String> responseMap = new Gson().fromJson(apiResponse, type);

                if (asyncException != null || !responseMap.get("status").equals("success")) {
                    // here tell that login request has thrown exception and ask to try again
                    CharSequence text = getResources().getString(R.string.invalid_creds);
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else {
                    SharedPreferences.Editor editor = settings.edit();

                    //Set "hasLoggedIn" to true and set username
                    editor.putBoolean("hasLoggedIn", true);
                    editor.putString("username", username);
                    successfulLogin(username);
                    // Commit the edits!
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this , HomeActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();
                }
            }
        }.execute();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
    private void alertBox(){
        alertDialog = new AlertDialog.Builder(this)
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
                .setIcon(android.R.drawable.ic_dialog_alert)
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
        alertDialog = new android.support.v7.app.AlertDialog.Builder(this)
                .setTitle("Unable to Connect")
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
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }

        });
    }

    private void successfulLogin(String username){
        try {
            Intercom.client().registerIdentifiedUser(new Registration().withUserId(username));
        }
        catch(Exception e){

        }
    }

}

