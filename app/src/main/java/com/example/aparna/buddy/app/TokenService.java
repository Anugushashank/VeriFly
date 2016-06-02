package com.example.aparna.buddy.app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.aparna.buddy.model.BuddyConstants;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Aparna on 14-Apr-16.
 */
public class TokenService {
    /**
     * Call this only inside an AsyncTask, as it is not handled in a thread here
     * @param context
     * @param act
     * @return token
     */
    public static String getToken(final Context context, final Activity act) {
        final SharedPreferences settings = context.getSharedPreferences(BuddyConstants.PREFS_FILE, 0); // 0 - for private mode
        String token = settings.getString("accessToken","");
        String time_stamp = settings.getString("timeStamp","");
        Long saved_ts = Long.getLong(time_stamp,0);

        Long tsLong = System.currentTimeMillis()/1000;

        if(token.isEmpty() || tsLong > saved_ts) {

            try {
                SharedPreferences.Editor editor = settings.edit();
                OkHttpClient client = new OkHttpClient();

                RequestBody body = RequestBody.create(BuddyConstants.MEDIA_TYPE_JSON, "");

                HttpUrl url = new HttpUrl.Builder()
                                         .scheme(context.getResources().getString(R.string.protocol_scheme))
                                         .host(context.getResources().getString(R.string.api_host))
                                         .addPathSegment(context.getResources().getString(R.string.authenticate_path))
                                         .build();


                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .addHeader("Authorization", context.getResources().getString(R.string.base_auth_token))
                        .addHeader("Content-Type", "application/json")
                        .build();

                Response response = client.newCall(request).execute();

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                JSONObject resp_object = new JSONObject(response.body().string());
                token = resp_object.getString("token");
                String expiresAt = resp_object.getString("expiresAt");
                editor.putString("accessToken", token);
                editor.putString("timeStamp", expiresAt);
                editor.commit();
            } catch (Exception e) {
                Log.d("Token Service Exception", e.toString());
                token = null;
            }
        }
        return token;
    }
}
