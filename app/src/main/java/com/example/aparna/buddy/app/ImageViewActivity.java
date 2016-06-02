package com.example.aparna.buddy.app;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ImageViewActivity extends AppCompatActivity {
    Toolbar toolBar;
    TextView picNumDisplay;
    Switch verifySwitch;
    String title, imageUrl;
    Bundle extras;
    Boolean verified;
    int picNum, id, size;
    private RadioGroup imageSelection;
    private Boolean imageCheckNo = false, imageCheckFront = false, imageCheckBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        extras     = getIntent().getExtras();
        id = extras.getInt("id");
        verified = extras.getBoolean("verified");
        title = extras.getString("picType");
        picNum = extras.getInt("picNum");
        size = extras.getInt("size");
        imageUrl = extras.getString("imageUrl");

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


        ImageView fullImage = (ImageView) findViewById(R.id.full_image);
        String imageUrl = extras.getString("imageUrl");
        Picasso.with(this)
               .load(imageUrl)
               .into(fullImage);

        imageSelection = (RadioGroup) findViewById(R.id.imageCheck);
        imageSelection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.No) {
                    imageCheckNo = true;
                }
                else if(checkedId == R.id.matchFront){
                    imageCheckFront = true;
                }
                else{
                    imageCheckBack = true;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("id", id);
        intent.putExtra("imageUrl",imageUrl);
        intent.putExtra("picNum",picNum);
        if(imageCheckNo ){
            intent.putExtra("check","no");
        }
        else if(imageCheckFront){
            intent.putExtra("check","front");
        }
        else if(imageCheckBack){
            intent.putExtra("check","back");
        }
        else{
            intent.putExtra("check","not");
        }
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}
