package com.interns.verifly.buddy.app;


import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.interns.verifly.buddy.model.TouchImageView;
import com.squareup.picasso.Picasso;


public class ImageViewActivity extends AppCompatActivity {
    Toolbar toolBar;
    TextView picNumDisplay;
    String title, imageUrl;
    Bundle extras;
    String match;
    int picNum, id, size;
    FrameLayout frameLayout;
    LinearLayout linearLayout;
    RadioGroup imageSelection, imageVerification;
    private BorrowerDetailsActivity activity;
    Boolean imageCheckValid = false, imageCheckInvalid = false, imageCheckFront = false, imageCheckBack = false, imageValid = false, imageInvalid = false;
    Boolean isVerified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        extras     = getIntent().getExtras();

        try {
            id = extras.getInt("id");
            match = extras.getString("match");
            isVerified = extras.getBoolean("isVerified");
            title = extras.getString("picType");
            picNum = extras.getInt("picNum");
            size = extras.getInt("size");
            imageUrl = extras.getString("imageUrl");
        }
        catch(Exception e){

        }

        toolBar = (Toolbar) findViewById(R.id.borrower_details_tool_bar);
        setSupportActionBar(toolBar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }



        getSupportActionBar().setTitle(title);
        toolBar.setTitleTextColor(Color.WHITE);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        picNumDisplay = (TextView)findViewById(R.id.pic_num);
        picNumDisplay.setText((picNum + 1) + " of " + Integer.toString(size));

        TouchImageView fullImage = (TouchImageView) findViewById(R.id.full_image);
        String imageUrl = extras.getString("imageUrl");
        try {
            Picasso.with(this)
                    .load(imageUrl).placeholder(R.drawable.loading_image)
                    .into(fullImage);
        }
        catch(Exception e){

        }

                frameLayout = (FrameLayout) findViewById(R.id.frameLayout);

                if (id == 1 ) {
                    linearLayout = (LinearLayout) frameLayout.findViewById(R.id.collegeIdLayout);
                    linearLayout.setVisibility(View.VISIBLE);

                    imageSelection = (RadioGroup) linearLayout.findViewById(R.id.imageCheck);
                    if(isVerified) {
                        if (match.equals("front")) {
                            imageSelection.check(R.id.matchFront);
                        } else if (match.equals("back")) {
                            imageSelection.check(R.id.matchBack);
                        } else if (match.equals("invalid")) {
                            imageSelection.check(R.id.matchInvalid);
                        }
                    }

                    imageSelection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            if (checkedId == R.id.matchInvalid) {
                                imageCheckInvalid = true;
                            } else if (checkedId == R.id.matchFront) {
                                imageCheckFront = true;
                            } else if (checkedId == R.id.matchBack) {
                                imageCheckBack = true;
                            }
                        }
                    });

                } else if (id == 4 || id == 3) {
                    linearLayout = (LinearLayout) frameLayout.findViewById(R.id.gradeSheetLayout);
                    linearLayout.setVisibility(View.VISIBLE);

                    imageVerification = (RadioGroup) linearLayout.findViewById(R.id.imageValid);
                    if (isVerified) {
                        if (match.equals("valid")) {
                            imageVerification.check(R.id.valid);
                        } else if (match.equals("invalid")) {
                            imageVerification.check(R.id.invalid);
                        }
                    }


                    imageVerification.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            if (checkedId == R.id.valid) {
                                imageValid = true;
                            } else if (checkedId == R.id.invalid) {
                                imageInvalid = true;
                            }
                        }
                    });
                }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("id", id);
        intent.putExtra("imageUrl",imageUrl);
        intent.putExtra("picNum",picNum);
        if(id == 1 ) {
            if(imageCheckInvalid){
                intent.putExtra("check", "invalid");
            } else if (imageCheckFront) {
                intent.putExtra("check", "front");
            } else if (imageCheckBack) {
                intent.putExtra("check", "back");
            } else {
                intent.putExtra("check", "not");
            }
        }
        else if(id == 3 || id ==4){
            if(imageValid){
                intent.putExtra("check","valid");
            }
            else if(imageInvalid){
                intent.putExtra("check","invalid");
            }
            else{
                intent.putExtra("check","not");
            }
        }
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}
