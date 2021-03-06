package com.interns.verifly.buddy.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.interns.verifly.buddy.model.ApiResponse;
import com.interns.verifly.buddy.model.ApiResponseTask;
import com.interns.verifly.buddy.model.BuddyConstants;
import com.interns.verifly.buddy.model.IntercomModel;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.intercom.android.sdk.Intercom;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolBar;
    private String userid;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private final Map<Integer, String> allTabsData = new HashMap<>();
    private final List<String> taskTypes = Arrays.asList("new", "ongoing", "completed");
    private Context context;
    Intent intent;
    IntercomModel intercomModel;
    android.support.v7.app.AlertDialog alertDialog;
    SharedPreferences settings;
    String earning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolBar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        context = getApplicationContext();
        intent = getIntent();

        settings = getSharedPreferences(BuddyConstants.PREFS_FILE, 0);
        userid = settings.getString("userid", "");


        final TextView earnings = (TextView) toolBar.findViewById(R.id.earnings);


        if (isNetworkConnected()) {
            new AsyncTask<String, String, String>() {
                MaterialDialog materialDialog;
                Exception apiException;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    materialDialog = new MaterialDialog.Builder(HomeActivity.this)
                            .content(getResources().getString(R.string.loading_tasks))
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
                protected String doInBackground(String... params) {
                    final String token = TokenService.getToken(getApplicationContext(), HomeActivity.this);
                    HttpUrl url;
                    Request request;
                    Response response;
                    String apiResponse;
                    JSONObject responseReader;
                    JSONArray dataList;
                    try {

                        if (token == null) {
                            throw new Exception();
                        }

                        OkHttpClient client = new OkHttpClient.Builder()
                                .connectTimeout(10, TimeUnit.SECONDS)
                                .readTimeout(10, TimeUnit.SECONDS)
                                .build();

                        int position = 0;

                        for (String task : taskTypes) {

                            url = new HttpUrl.Builder()
                                    .scheme("http")
                                    .host(context.getResources().getString(R.string.api_host))
                                    .addPathSegments(context.getResources().getString(R.string.tasks_path))
                                    .addQueryParameter("assignedTo", userid)
                                    .addQueryParameter("taskStatus", task)
                                    .build();

                            request = new Request.Builder()
                                    .url(url)
                                    .get()
                                    .addHeader("x-access-token", token)
                                    .addHeader("content-type", "application/json")
                                    .build();
                            try {
                                response = client.newCall(request).execute();
                                if (!response.isSuccessful()) {
                                    throw new IOException("Unexpected code " + response);
                                }
                                apiResponse = response.body().string();

                                ApiResponseTask apiResponse1 = new Gson().fromJson(apiResponse, ApiResponseTask.class);

                                if (apiResponse1.getStatus().equals("error")) {

                                    allTabsData.put(position, null);
                                    position++;

                                } else {

                                    responseReader = new JSONObject(apiResponse);
                                    dataList = responseReader.getJSONArray("data");

                                    allTabsData.put(position, dataList.toString());
                                    position++;
                                }
                            } catch (Exception e) {
                                apiException = e;
                                break;
                            }
                        }

                        url = new HttpUrl.Builder()
                                .scheme("http")
                                .host(context.getResources().getString(R.string.api_host))
                                .addPathSegments(context.getResources().getString(R.string.earnings_path))
                                .addQueryParameter("assignedTo", userid)
                                .build();

                        request = new Request.Builder()
                                .url(url)
                                .get()
                                .addHeader("x-access-token", token)
                                .addHeader("content-type", "application/json")
                                .build();

                        try {
                            response = client.newCall(request).execute();
                            if (!response.isSuccessful()) {
                                throw new IOException("Unexpected code " + response);
                            }
                            apiResponse = response.body().string();

                            ApiResponse apiResponse1 = new Gson().fromJson(apiResponse, ApiResponse.class);

                            if(apiResponse1.getStatus().equals("success")){

                                earning = "Earnings  ".concat(getString(R.string.Rs)).concat(Integer.toString(apiResponse1.getData().getEarning()));
                            }

                        } catch (Exception e) {
                            apiException = e;
                        }
                    }
                    catch (Exception e) {
                        apiException = e;
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String s) {
                    if (materialDialog.isShowing()) {
                        materialDialog.dismiss();
                    }

                    if (apiException != null) {
                        connectionTimeOut();
                        return;
                    }

                /*
                Creating Adapter and setting that adapter to the viewPager
                setSupportActionBar method takes the toolbar and sets it as
                the default action bar thus making the toolbar work like a normal
                action bar.
                */

                    setSupportActionBar(toolBar);
                    if (earnings != null) {
                        earnings.setText(earning);
                        earnings.setVisibility(View.VISIBLE);
                    }

                    toolBar.setLogo(R.drawable.buddylogosmall);

                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle("");
                    }

                /*
                TabLayout.newTab() method creates a tab view, Now a Tab view is not the view
                which is below the tabs, its the tab itself.
                */
                    final TabLayout.Tab newTask = tabLayout.newTab();
                    final TabLayout.Tab ongoingTask = tabLayout.newTab();
                    final TabLayout.Tab completedTask = tabLayout.newTab();

                    // Setting Title text for our tabs respectively. Later take number of tasks from API and add
                    newTask.setText("New");
                    ongoingTask.setText("Ongoing");
                    completedTask.setText("Completed");

                /*
                Adding the tab view to our tablayout at appropriate positions
                As I want home at first position I am passing home and 0 as argument to
                the tablayout and like wise for other tabs as well
                */
                    tabLayout.addTab(newTask, 0);
                    tabLayout.addTab(ongoingTask, 1);
                    tabLayout.addTab(completedTask, 2);

                /*
                TabTextColor sets the color for the title of the tabs, passing a ColorStateList here makes
                tab change colors in different situations such as selected, active, inactive etc
                TabIndicatorColor sets the color for the indiactor below the tabs
                */

                    tabLayout.setTabTextColors(ContextCompat.getColorStateList(HomeActivity.this, R.color.tab_selector));
                    tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(HomeActivity.this, R.color.indicator));
                    tabLayout.setSelectedTabIndicatorHeight(10);

                /*
                Adding a onPageChangeListener to the viewPager
                1st we add the PageChangeListener and pass a TabLayoutPageChangeListener so that Tabs Selection
                changes when a viewpager page changes.
                */

                    viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                    tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

                    ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), HomeActivity.this, allTabsData);
                    viewPager.setAdapter(viewPagerAdapter);
                    viewPager.setCurrentItem(settings.getInt("tab",0));

                }
            }.execute();

            new AsyncTask<String, String, String>() {
                Exception apiException;
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                }
                @Override
                protected String doInBackground(String... params) {

                    try{
                        intercomModel = new IntercomModel(HomeActivity.this);
                        intercomModel.sendData(allTabsData);
                    }
                    catch (Exception e) {
                        apiException = e;
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(String s) {

                }
            }.execute();
        }
        else{
            alertBox();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        final Intent intent = new Intent(this,LoginActivity.class);

        if (id == R.id.logouticon) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.DialogStyle);
            builder.setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //Yes button clicked, do something
                            try {
                                Intercom.client().reset();
                            }
                            catch(Exception e){

                            }

                            SharedPreferences settings = getSharedPreferences(BuddyConstants.PREFS_FILE, 0); // 0 - for private mode
                            SharedPreferences.Editor editor = settings.edit();

                            //Set "hasLoggedIn" to false
                            editor.putBoolean("hasLoggedIn", false);
                            editor.clear();
                            editor.apply();


                            startActivity(intent);
                            HomeActivity.this.finish();



                        }


                    })
                    .setNegativeButton("No", null)						//Do nothing on no
                    .show();
        }
        if(id == R.id.intercom ){
            try {
                Intercom.client().displayMessageComposer();
            } catch (Exception e) {

            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void showForm(View v) {
        Intent intent = new Intent(this,BorrowerDetailsActivity.class);
        startActivity(intent);
    }


    public class ViewPagerAdapter extends FragmentStatePagerAdapter {
        Context context;
        Map<Integer, String> allTabsData;

        public ViewPagerAdapter(FragmentManager fm, Context context,
                                Map<Integer, String> allTabsData) {
            super(fm);
            this.context     = context;
            this.allTabsData = allTabsData;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new TabFragment();

            Bundle bundle = new Bundle();
            bundle.putString("tasksData", allTabsData.get(position));
            bundle.putInt("taskType", position);

            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
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
                        Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                        if(isNetworkConnected()) {
                            startActivity(intent);
                            HomeActivity.this.finish();
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
                        Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                        if(isNetworkConnected()) {
                            startActivity(intent);
                            HomeActivity.this.finish();
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

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        this.finish();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(intent);
    }


}


