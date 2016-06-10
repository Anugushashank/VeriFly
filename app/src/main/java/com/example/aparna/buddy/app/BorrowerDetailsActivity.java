package com.example.aparna.buddy.app;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.aparna.buddy.adapter.GridViewAdapter;
import com.example.aparna.buddy.adapter.NothingSelectedSpinnerAdapter;
import com.example.aparna.buddy.model.BorrowerData;
import com.example.aparna.buddy.model.BorrowerDataUpdater;
import com.example.aparna.buddy.model.BorrowerStateContainer;
import com.example.aparna.buddy.model.BuddyConstants;
import com.example.aparna.buddy.model.CloudinaryAPI;
import com.example.aparna.buddy.model.ExpandableHeightGridView;
import com.example.aparna.buddy.model.ImageBox;
import com.example.aparna.buddy.model.UploadDocModel;
import com.example.aparna.buddy.model.UserInfo;
import com.example.aparna.buddy.model.VerificationInfo;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BorrowerDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,CloudinaryAPI.UploadCallBack,Serializable {

    private BorrowerData borrowerData;
    private BorrowerDataUpdater borrowerDataUpdater;
    private UploadDocModel uploadDocModel;
    private VerificationInfo verInfo;
    private Toolbar toolBar;
    private TextView phoneNum, name, college, friendPhoneNum, friendName, textView;
    private RadioGroup referenceSelection, yearBackSelection, transparentSelection, internSelection;
    private EditText refYear, refDept, otherNotes;
    private ImageView phoneIcon, IdIcon, IdIconPlusFront, IdIconPlusBack, Im;
    private FrameLayout imageLayout, imageLayout1, imageLayout2;
    private ImageBox imageBox, imageBoxtemp, ibx;
    private AppCompatSpinner spinnerPunc,spinnerSincere,spinnerCocurricular,spinnerFinRes,spinnerLoan;
    private CircularImageView profilePic;
    private String goodFriendsRadio, yearBackRadio, transparentRadio, giveLoanRadio;
    private Button submitButton;
    private ArrayList<ImageBox> collegeID, addressProof, gradeSheet, bankProof, bankStatement;
    private String frontImageCollegeId, backImageCollegeId, frontImageAddressProof, backImageAddressProof;
    private String punctualityInClass, sincerityInStudies, coCurricularParticipation, financiallyResponsible, loanRepay, friendVerificationNotes;
    private String imageurl;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrower_details);

        borrowerDataUpdater = new BorrowerDataUpdater(this, "button");

        Bundle extras = getIntent().getExtras();


        borrowerData = new Gson().fromJson(extras.getString("borrowerData"), BorrowerData.class);
        uploadDocModel = borrowerData.getUploadDocModel();
        verInfo = borrowerData.getVerificationInfo();

        toolBar = (Toolbar) findViewById(R.id.borrower_details_tool_bar);
        setSupportActionBar(toolBar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.borrower_activity_title));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setTitleTextColor(Color.WHITE);
        toolBar.setNavigationIcon(R.drawable.back);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        submitButton = (Button) findViewById(R.id.submitButton);

        collegeID = new ArrayList<>();
        count = -1;

        if (uploadDocModel.getCollegeID().getFront()!= null) {
            ImageBox ib = new ImageBox(imageLayout, BorrowerDetailsActivity.this);
            ib.setImageUrl(uploadDocModel.getCollegeID().getFront().getImgUrl());
            frontImageCollegeId = ib.getImageUrl();
            ib.setPicNum(++count);
            ib.setPicType("College ID");
            ib.setCloudinaryId("collegeId");
            ib.setId(1);
            ib.setVerified("front");
            insertCollegeImageBox(ib);
        }

        if (uploadDocModel.getCollegeID().getBack().getImgUrl() != null) {
            ImageBox ib = new ImageBox(imageLayout, BorrowerDetailsActivity.this);
            ib.setImageUrl(uploadDocModel.getCollegeID().getBack().getImgUrl());
            backImageCollegeId = ib.getImageUrl();
            ib.setPicNum(++count);
            ib.setPicType("College ID");
            ib.setCloudinaryId("collegeId");
            ib.setId(1);
            ib.setVerified("back");
            insertCollegeImageBox(ib);
        }
        if(uploadDocModel.getCollegeID().getFront()== null ||  uploadDocModel.getCollegeID().getBack() != null) {
            if (uploadDocModel.getCollegeID().getImgUrls() != null) {
                for (int i = count + 1; i < uploadDocModel.getCollegeID().getImgUrls().size(); i++) {
                    ImageBox ib = new ImageBox(imageLayout, BorrowerDetailsActivity.this);
                    ib.setImageUrl(uploadDocModel.getCollegeID().getImgUrls().get(count));
                    ib.setPicNum(count);
                    count = i;
                    ib.setPicType("College ID");
                    ib.setCloudinaryId("collegeId");
                    ib.setId(1);
                    ib.setVerified("invalid");
                    insertCollegeImageBox(ib);
                }
            }
        }
        ibx = new ImageBox(imageLayout,BorrowerDetailsActivity.this);
        ibx.setImageUrl(null);
        ibx.setPicNum(++count);
        ibx.setPicType("College ID");
        ibx.setCloudinaryId("collegeId");
        ibx.setId(1);
        ibx.setVerified("invalid");
        insertCollegeImageBox(ibx);

        createCollegeIdLayout();




        addressProof = new ArrayList<>();
        count = -1;

        if (uploadDocModel.getAddressProof().getFront()!= null) {
            ImageBox ib = new ImageBox(imageLayout, BorrowerDetailsActivity.this);
            ib.setImageUrl(uploadDocModel.getAddressProof().getFront().getImgUrl());
            frontImageAddressProof = ib.getImageUrl();
            ib.setPicNum(++count);
            ib.setPicType("Permanent Address Proof");
            ib.setCloudinaryId("addressProof");
            ib.setId(2);
            ib.setVerified("front");
            insertAddressProofImageBox(ib);
        }
        if (uploadDocModel.getAddressProof().getBack() != null) {
            ImageBox ib = new ImageBox(imageLayout, BorrowerDetailsActivity.this);
            ib.setImageUrl(uploadDocModel.getAddressProof().getBack().getImgUrl());
            backImageAddressProof = ib.getImageUrl();
            ib.setPicNum(++count);
            ib.setPicType("Permanent Address Proof");
            ib.setCloudinaryId("addressProof");
            ib.setId(2);
            ib.setVerified("back");
            insertAddressProofImageBox(ib);
        }
        if(uploadDocModel.getAddressProof().getFront()== null ||  uploadDocModel.getAddressProof().getBack() != null) {
            if (uploadDocModel.getAddressProof().getImgUrls() != null) {
                for (int i = count + 1; i < uploadDocModel.getAddressProof().getImgUrls().size(); i++) {
                    ImageBox ib = new ImageBox(imageLayout, BorrowerDetailsActivity.this);
                    ib.setImageUrl(uploadDocModel.getAddressProof().getImgUrls().get(count));
                    ib.setPicNum(count);count = i;
                    ib.setPicType("Permanent Address Proof");
                    ib.setCloudinaryId("addressProof");
                    ib.setId(2);
                    ib.setVerified("invalid");
                    insertAddressProofImageBox(ib);
                }
            }
        }
        ibx = new ImageBox(imageLayout,BorrowerDetailsActivity.this);
        ibx.setImageUrl(null);
        ibx.setPicNum(++count);
        ibx.setPicType("Permanent Address Proof");
        ibx.setCloudinaryId("addressProof");
        ibx.setId(2);
        ibx.setVerified("invalid");
        insertAddressProofImageBox(ibx);

        createAddressProofLayout();




        gradeSheet = new ArrayList<>();
        count = -1;

        if (uploadDocModel.getGradeSheet().getImgUrls() != null) {
            for (int i = count + 1; i < uploadDocModel.getGradeSheet().getImgUrls().size(); i++) {
                ImageBox ib = new ImageBox(imageLayout, BorrowerDetailsActivity.this);
                ib.setImageUrl(uploadDocModel.getGradeSheet().getImgUrls().get(i));
                ib.setPicNum(i);
                count = i;
                ib.setPicType("Grade Sheet");
                ib.setCloudinaryId("gradeSheet");
                ib.setId(3);
                ib.setVerified("invalid");
                insertGradeSheetImageBox(ib);
            }
        }
        ibx = new ImageBox(imageLayout,BorrowerDetailsActivity.this);
        ibx.setImageUrl(null);
        ibx.setPicNum(++count);
        ibx.setPicType("Grade Sheet");
        ibx.setCloudinaryId("gradeSheet");
        ibx.setId(3);
        ibx.setVerified("invalid");
        insertGradeSheetImageBox(ibx);

        createGradeSheetsLayout();




        bankProof = new ArrayList<>();
        count = -1;

        if (uploadDocModel.getBankProof().getImgUrls() != null) {
            for (int i = count + 1; i < uploadDocModel.getBankProof().getImgUrls().size(); i++) {
                ImageBox ib = new ImageBox(imageLayout, BorrowerDetailsActivity.this);
                ib.setImageUrl(uploadDocModel.getBankProof().getImgUrls().get(i));
                ib.setPicNum(i);
                count = i;
                ib.setPicType("Bank Proof");
                ib.setCloudinaryId("bankProof");
                ib.setId(4);
                ib.setVerified("invalid");
                insertBankProofImageBox(ib);
            }
        }
        ibx = new ImageBox(imageLayout,BorrowerDetailsActivity.this);
        ibx.setImageUrl(null);
        ibx.setPicNum(++count);
        ibx.setPicType("Bank Proof");
        ibx.setCloudinaryId("bankProof");
        ibx.setId(4);
        ibx.setVerified("invalid");
        insertBankProofImageBox(ibx);

        createBankProofsLayout();





        profilePic = (CircularImageView) findViewById(R.id.profile_image);
        name = (TextView) findViewById(R.id.name);
        phoneNum = (TextView) findViewById(R.id.phoneNum);
        phoneIcon = (ImageView) findViewById(R.id.phoneIcon);
        friendName = (TextView) findViewById(R.id.friendName);
        friendPhoneNum = (TextView) findViewById(R.id.friendPhoneNum);
        college = (TextView) findViewById(R.id.college);
        IdIcon = (ImageView) findViewById(R.id.IdIcon);
        refYear = (EditText) findViewById(R.id.refYear);
        refDept = (EditText) findViewById(R.id.refDept);
        otherNotes = (EditText) findViewById(R.id.otherNotes);
        referenceSelection = (RadioGroup) findViewById(R.id.goodFriendsCheck);
        referenceSelection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.goodFriendsYes) {
                    goodFriendsRadio = "true";
                } else {
                    goodFriendsRadio = "false";
                }
            }
        });
        yearBackSelection = (RadioGroup) findViewById(R.id.yearBackCheck);
        yearBackSelection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.yearBackYes) {
                    yearBackRadio = "true";
                } else {
                    yearBackRadio = "false";
                }
            }
        });
        transparentSelection = (RadioGroup) findViewById(R.id.transparentCheck);
        transparentSelection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.transparentYes) {
                    transparentRadio = "true";
                } else {
                    transparentRadio = "false";
                }
            }
        });
        internSelection = (RadioGroup) findViewById(R.id.internchoicecheck);
        internSelection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.internchoiceYes) {
                    giveLoanRadio = "true";
                } else {
                    giveLoanRadio = "false";
                }
            }
        });

        String userId = uploadDocModel.getFbUserId();
        Picasso.with(this)
                .load("https://graph.facebook.com/" + userId + "/picture?type=large")
                .into(profilePic);

        name.setText(uploadDocModel.getName());

        phoneNum.setText(uploadDocModel.getPhone());
        phoneNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone_num = (phoneNum.getText()).toString();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phone_num));
                if (ContextCompat.checkSelfPermission(BorrowerDetailsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(BorrowerDetailsActivity.this, new String[]{Manifest.permission.CALL_PHONE},3);
                }
                if(ContextCompat.checkSelfPermission(BorrowerDetailsActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(callIntent);
                }
            }
        });

        college.setTextSize(18 - (uploadDocModel.getCollege().length()) / 8);
        college.setText(uploadDocModel.getCollege());

        friendName.setText(uploadDocModel.getFriendName());
        friendPhoneNum.setText(uploadDocModel.getFriendNumber());
        friendPhoneNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone_num = (friendPhoneNum.getText()).toString();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phone_num));
                if (ContextCompat.checkSelfPermission(BorrowerDetailsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(BorrowerDetailsActivity.this, new String[]{Manifest.permission.CALL_PHONE},4);
                }
                if(ContextCompat.checkSelfPermission(BorrowerDetailsActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(callIntent);
                }
            }
        });
        if(verInfo.getReferenceIsGoodFriend() != null) {
            if (verInfo.getReferenceIsGoodFriend()) {
                referenceSelection.check(R.id.goodFriendsYes);
                goodFriendsRadio = "true";
            } else {
                referenceSelection.check(R.id.goodFriendsNo);
                goodFriendsRadio = "false";
            }
        }

        if (verInfo.getReferenceYear() != null) {
            refYear.setText(verInfo.getReferenceYear());

        }

        if (verInfo.getReferenceDepartment() != null) {
            refDept.setText(verInfo.getReferenceDepartment());
        }

        if (verInfo.getFinalVerificationNotes() != null) {
            otherNotes.setText(verInfo.getFinalVerificationNotes());
        }

        if(verInfo.getHaveYearBack() != null) {
            if (verInfo.getHaveYearBack()) {
                yearBackSelection.check(R.id.yearBackYes);
                yearBackRadio = "true";
            } else {
                yearBackSelection.check(R.id.yearBackNo);
                yearBackRadio = "false";
            }
        }

        if(verInfo.getGiveHimLoan() != null) {
            if (verInfo.getGiveHimLoan()) {
                internSelection.check(R.id.internchoiceYes);
                giveLoanRadio = "true";
            } else {
                internSelection.check(R.id.internchoiceNo);
                giveLoanRadio = "false";
            }
        }

        if(verInfo.getBorrowerIsLying() != null) {
            if (verInfo.getBorrowerIsLying()) {
                transparentSelection.check(R.id.transparentYes);
                transparentRadio = "true";
            } else {
                transparentSelection.check(R.id.transparentNo);
                transparentRadio = "false";
            }
        }

        spinnerPunc = (AppCompatSpinner) findViewById(R.id.punctality);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.opinions, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_list);
        spinnerPunc.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.nothing_selected_spinner,
                        this));
        spinnerPunc.setOnItemSelectedListener(this);

        if(verInfo.getPunctualityInClass() != null){
            spinnerPunc.setSelection(adapter.getPosition(verInfo.getPunctualityInClass()) + 1);
            punctualityInClass = verInfo.getPunctualityInClass();
        }

        spinnerFinRes = (AppCompatSpinner) findViewById(R.id.finResponsibility);
        spinnerFinRes.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.nothing_selected_spinner,
                        this));
        spinnerFinRes.setOnItemSelectedListener(this);

        if(verInfo.getFinanciallyResponsible() != null){
            spinnerFinRes.setSelection(adapter.getPosition(verInfo.getFinanciallyResponsible()) + 1);
            financiallyResponsible = verInfo.getFinanciallyResponsible();
        }

        spinnerSincere = (AppCompatSpinner) findViewById(R.id.sincerity);
        spinnerSincere.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.nothing_selected_spinner,
                        this));

        spinnerSincere.setOnItemSelectedListener(this);

        if(verInfo.getSincerityInStudies() != null){
            spinnerSincere.setSelection(adapter.getPosition(verInfo.getSincerityInStudies()) + 1);
            sincerityInStudies = verInfo.getSincerityInStudies();
        }

        spinnerCocurricular = (AppCompatSpinner) findViewById(R.id.coCurricular);
        spinnerCocurricular.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.nothing_selected_spinner,
                        this));
        spinnerCocurricular.setOnItemSelectedListener(this);

        if(verInfo.getCoCurricularParticipation() != null){
            spinnerCocurricular.setSelection(adapter.getPosition(verInfo.getCoCurricularParticipation()) + 1);
            coCurricularParticipation = verInfo.getCoCurricularParticipation();
        }

        ArrayAdapter<CharSequence> sadapter = ArrayAdapter.createFromResource(this,
                R.array.loan_items, R.layout.spinner_item);
        sadapter.setDropDownViewResource(R.layout.spinner_dropdown_list);

        spinnerLoan = (AppCompatSpinner) findViewById(R.id.Loan_repay);
        // Create an ArrayAdapter using the string array and a default spinner layout
        spinnerLoan.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        sadapter,
                        R.layout.nothing_selected_spinner,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));
        spinnerLoan.setOnItemSelectedListener(this);

        if(verInfo.getRepayCapacity() != null){
            spinnerLoan.setSelection(sadapter.getPosition(verInfo.getRepayCapacity()) + 1);
            loanRepay = verInfo.getRepayCapacity();
        }

    }


    public void showLocation(View view) {
        Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String photoPath = "";
        CloudinaryAPI cloudinary = new CloudinaryAPI(BorrowerDetailsActivity.this);
        Bitmap bp;
        Uri uri;
        SharedPreferences settings = getSharedPreferences(BuddyConstants.PREFS_FILE, 0);
        String localPath = settings.getString("localPath","");
        if(resultCode == RESULT_OK){
            if(requestCode == 1){

                bp = (Bitmap) data.getExtras().get("data");
                Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{ MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.ImageColumns.ORIENTATION }, MediaStore.Images.Media.DATE_ADDED, null, "date_added ASC");
                if(cursor != null && cursor.moveToFirst()) {
                    do {
                        uri = Uri.parse(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                        photoPath = uri.toString();
                    } while (cursor.moveToNext());
                    cursor.close();
                }
                cloudinary.uploadImage(bp, localPath, borrowerData,this);

            } else if (requestCode == 2) {

                Uri selectedImageUri = data.getData();
                Cursor cursor = getContentResolver().query(selectedImageUri, null, null, null, null);
                cursor.moveToFirst();
                photoPath = cursor.getString(0);
                photoPath = photoPath.substring(photoPath.lastIndexOf(":") + 1);
                cursor.close();

                cursor = getContentResolver().query(
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        null, MediaStore.Images.Media._ID + " = ? ", new String[]{photoPath}, null);
                cursor.moveToFirst();

                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                cursor.close();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(path, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bp = BitmapFactory.decodeFile(path, options);

                cloudinary.uploadImage(bp, localPath, borrowerData,this);

            } else if (requestCode == 3) {
                int id = (int) data.getExtras().get("id");
                final int picNum = data.getExtras().getInt("picNum");
                String check = data.getExtras().getString("check");
                String imageUrl = data.getExtras().getString("imageUrl");
                ImageBox imageBox1, imageBox2, imageBox3;
                final GridView gridView;

                if(id == 1){
                    gridView = (GridView) findViewById(R.id.collegeIdImages);
                    if(check.equals("front")){
                        if(frontImageCollegeId != null && backImageCollegeId != null){
                            if(picNum == 1) {
                                backImageCollegeId = null;
                                imageBox1 = collegeID.get(0);
                                imageBox2 = collegeID.get(1);

                                imageBox1.setPicNum(1);
                                imageBox2.setPicNum(0);

                                imageBox2.setVerified("front");
                                imageBox1.setVerified("invalid");

                                collegeID.set(0, imageBox2);
                                collegeID.set(1, imageBox1);

                                createCollegeIdLayout();
                                ViewTreeObserver viewTreeObserver = gridView.getViewTreeObserver();
                                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                    @Override
                                    public void onGlobalLayout() {

                                        LinearLayout gridChild = (LinearLayout) gridView.getChildAt(0);
                                        imageLayout = (FrameLayout) gridChild.findViewById(R.id.imageLayout);
                                        imageLayout.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                        imageLayout.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                        textView = (TextView) gridChild.findViewById(R.id.textView);
                                        textView.setText("FrontSide");
                                        textView.setVisibility(View.VISIBLE);

                                        LinearLayout gridChild1 = (LinearLayout) gridView.getChildAt(1);
                                        imageLayout1 = (FrameLayout) gridChild1.findViewById(R.id.imageLayout);
                                        imageLayout1.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.VISIBLE);
                                        imageLayout1.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.INVISIBLE);
                                        textView = (TextView) gridChild1.findViewById(R.id.textView);
                                        textView.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }
                            else if(picNum == 0) {
                            }
                            else{
                                imageBox1 = collegeID.get(0);
                                imageBox2 = collegeID.get(picNum);

                                imageBox1.setPicNum(picNum);
                                imageBox2.setPicNum(0);

                                imageBox2.setVerified("front");
                                imageBox1.setVerified("invalid");

                                collegeID.set(picNum, imageBox1);
                                collegeID.set(0, imageBox2);

                                createCollegeIdLayout();
                                ViewTreeObserver viewTreeObserver = gridView.getViewTreeObserver();
                                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                    @Override
                                    public void onGlobalLayout() {

                                        LinearLayout gridChild1 = (LinearLayout) gridView.getChildAt(picNum);
                                        imageLayout1 = (FrameLayout) gridChild1.findViewById(R.id.imageLayout);
                                        imageLayout1.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.VISIBLE);
                                        imageLayout1.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.INVISIBLE);
                                        textView = (TextView) gridChild1.findViewById(R.id.textView);
                                        textView.setVisibility(View.INVISIBLE);

                                        LinearLayout gridChild = (LinearLayout) gridView.getChildAt(0);
                                        imageLayout = (FrameLayout) gridChild.findViewById(R.id.imageLayout);
                                        imageLayout.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                        imageLayout.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                        textView = (TextView) gridChild.findViewById(R.id.textView);
                                        textView.setText("FrontSide");
                                        textView.setVisibility(View.VISIBLE);

                                    }
                                });
                            }
                        }
                        else if(backImageCollegeId != null ) {
                            if (picNum == 0) {
                                imageBox1 = collegeID.get(0);
                                imageBox1.setVerified("front");

                                backImageCollegeId = null;

                                createCollegeIdLayout();
                                ViewTreeObserver viewTreeObserver = gridView.getViewTreeObserver();
                                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                    @Override
                                    public void onGlobalLayout() {

                                        LinearLayout gridChild = (LinearLayout) gridView.getChildAt(0);
                                        imageLayout = (FrameLayout) gridChild.findViewById(R.id.imageLayout);
                                        imageLayout.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                        imageLayout.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                        textView = (TextView) gridChild.findViewById(R.id.textView);
                                        textView.setText("FrontSide");
                                        textView.setVisibility(View.VISIBLE);

                                    }
                                });
                            } else if (picNum == 1) {
                                imageBox1 = collegeID.get(0);
                                imageBox2 = collegeID.get(picNum);

                                imageBox1.setPicNum(picNum);
                                imageBox2.setPicNum(0);

                                imageBox2.setVerified("front");
                                imageBox1.setVerified("back");

                                collegeID.set(0, imageBox2);
                                collegeID.set(picNum, imageBox1);

                                createCollegeIdLayout();
                                ViewTreeObserver viewTreeObserver = gridView.getViewTreeObserver();
                                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                    @Override
                                    public void onGlobalLayout() {

                                        LinearLayout gridChild = (LinearLayout) gridView.getChildAt(0);
                                        imageLayout = (FrameLayout) gridChild.findViewById(R.id.imageLayout);
                                        imageLayout.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                        imageLayout.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                        textView = (TextView) gridChild.findViewById(R.id.textView);
                                        textView.setText("FrontSide");
                                        textView.setVisibility(View.VISIBLE);

                                        LinearLayout gridChild1 = (LinearLayout) gridView.getChildAt(1);
                                        imageLayout1 = (FrameLayout) gridChild1.findViewById(R.id.imageLayout);
                                        imageLayout1.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                        imageLayout1.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                        textView = (TextView) gridChild1.findViewById(R.id.textView);
                                        textView.setText("BackSide");
                                        textView.setVisibility(View.VISIBLE);
                                    }
                                });
                            } else {
                                imageBox1 = collegeID.get(0);
                                imageBox2 = collegeID.get(picNum);
                                imageBox3 = collegeID.get(1);

                                imageBox1.setPicNum(1);
                                imageBox2.setPicNum(0);
                                imageBox3.setPicNum(picNum);

                                imageBox2.setVerified("front");
                                imageBox1.setVerified("back");
                                imageBox3.setVerified("invalid");


                                collegeID.set(0, imageBox2);
                                collegeID.set(1, imageBox1);
                                collegeID.set(picNum, imageBox3);
                                createCollegeIdLayout();
                                ViewTreeObserver viewTreeObserver = gridView.getViewTreeObserver();
                                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                    @Override
                                    public void onGlobalLayout() {
                                        LinearLayout gridChild = (LinearLayout) gridView.getChildAt(0);
                                        imageLayout = (FrameLayout) gridChild.findViewById(R.id.imageLayout);
                                        imageLayout.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                        imageLayout.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                        textView = (TextView) gridChild.findViewById(R.id.textView);
                                        textView.setText("FrontSide");
                                        textView.setVisibility(View.VISIBLE);

                                        LinearLayout gridChild1 = (LinearLayout) gridView.getChildAt(1);
                                        imageLayout1 = (FrameLayout) gridChild1.findViewById(R.id.imageLayout);
                                        imageLayout1.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                        imageLayout1.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                        textView = (TextView) gridChild1.findViewById(R.id.textView);
                                        textView.setText("BackSide");
                                        textView.setVisibility(View.VISIBLE);
                                    }
                                });

                            }
                        }
                        else{
                            imageBox1 = collegeID.get(0);
                            imageBox2 = collegeID.get(picNum);

                            imageBox1.setPicNum(picNum);
                            imageBox2.setPicNum(0);

                            imageBox1.setVerified("invalid");
                            imageBox2.setVerified("front");

                            collegeID.set(picNum,imageBox1);
                            collegeID.set(0,imageBox2);
                            createCollegeIdLayout();
                            ViewTreeObserver viewTreeObserver = gridView.getViewTreeObserver();
                            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                @Override
                                public void onGlobalLayout() {

                                    LinearLayout gridChild = (LinearLayout) gridView.getChildAt(0);
                                    imageLayout = (FrameLayout) gridChild.findViewById(R.id.imageLayout);
                                    imageLayout.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                    imageLayout.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                    TextView textView = (TextView)gridChild.findViewById(R.id.textView);
                                    textView.setText("FrontSide");
                                    textView.setVisibility(View.VISIBLE);
                                }
                            });

                        }
                        frontImageCollegeId = imageUrl;
                    }
                    else if(check.equals("back")){

                        if( frontImageCollegeId != null && backImageCollegeId != null) {
                            if(picNum == 0) {
                                imageBox1 = collegeID.get(0);
                                imageBox2 = collegeID.get(1);

                                imageBox1.setVerified("back");
                                imageBox2.setVerified("invalid");

                                createCollegeIdLayout();

                                frontImageCollegeId = null;
                                ViewTreeObserver viewTreeObserver = gridView.getViewTreeObserver();
                                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                    @Override
                                    public void onGlobalLayout() {

                                        LinearLayout gridChild = (LinearLayout) gridView.getChildAt(0);
                                        imageLayout = (FrameLayout) gridChild.findViewById(R.id.imageLayout);
                                        imageLayout.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                        imageLayout.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                        textView = (TextView) gridChild.findViewById(R.id.textView);
                                        textView.setText("BackSide");
                                        textView.setVisibility(View.VISIBLE);

                                        LinearLayout gridChild1 = (LinearLayout) gridView.getChildAt(1);
                                        imageLayout1 = (FrameLayout) gridChild1.findViewById(R.id.imageLayout);
                                        imageLayout1.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.VISIBLE);
                                        imageLayout1.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.INVISIBLE);
                                        textView = (TextView) gridChild1.findViewById(R.id.textView);
                                        textView.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }
                            else if(picNum == 1){}
                            else{
                                imageBox1 = collegeID.get(1);
                                imageBox2 = collegeID.get(picNum);

                                imageBox1.setPicNum(picNum);
                                imageBox2.setPicNum(1);

                                imageBox1.setVerified("invalid");
                                imageBox2.setVerified("back");

                                collegeID.set(picNum, imageBox1);
                                collegeID.set(1,imageBox2);

                                createCollegeIdLayout();

                                ViewTreeObserver viewTreeObserver = gridView.getViewTreeObserver();
                                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                    @Override
                                    public void onGlobalLayout() {

                                        LinearLayout gridChild = (LinearLayout) gridView.getChildAt(1);
                                        imageLayout = (FrameLayout) gridChild.findViewById(R.id.imageLayout);
                                        imageLayout.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                        imageLayout.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                        textView = (TextView) gridChild.findViewById(R.id.textView);
                                        textView.setText("BackSide");
                                        textView.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        }
                        else if(frontImageCollegeId != null ){
                            if(picNum == 0){
                                imageBox1 = collegeID.get(0);

                                imageBox1.setVerified("back");
                                frontImageCollegeId = null;
                                createCollegeIdLayout();
                                ViewTreeObserver viewTreeObserver = gridView.getViewTreeObserver();
                                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                    @Override
                                    public void onGlobalLayout() {

                                        LinearLayout gridChild = (LinearLayout) gridView.getChildAt(0);
                                        imageLayout = (FrameLayout) gridChild.findViewById(R.id.imageLayout);
                                        imageLayout.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                        imageLayout.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                        textView = (TextView)gridChild.findViewById(R.id.textView);
                                        textView.setText("BackSide");
                                        textView.setVisibility(View.VISIBLE);

                                    }
                                });
                            }
                            else if(picNum == 1){
                                imageBox1 = collegeID.get(1);
                                imageBox1.setVerified("back");

                                createCollegeIdLayout();
                                ViewTreeObserver viewTreeObserver = gridView.getViewTreeObserver();
                                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                    @Override
                                    public void onGlobalLayout() {
                                        LinearLayout gridChild = (LinearLayout) gridView.getChildAt(1);
                                        imageLayout = (FrameLayout) gridChild.findViewById(R.id.imageLayout);
                                        imageLayout.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                        imageLayout.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                        TextView textView = (TextView)gridChild.findViewById(R.id.textView);
                                        textView.setText("BackSide");
                                        textView.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                            else{
                                imageBox1 = collegeID.get(1);
                                imageBox2 = collegeID.get(picNum);

                                imageBox1.setPicNum(picNum);
                                imageBox2.setPicNum(1);

                                imageBox2.setVerified("back");
                                imageBox1.setVerified("invalid");

                                collegeID.set(picNum,imageBox1);
                                collegeID.set(1,imageBox2);

                                createCollegeIdLayout();
                                ViewTreeObserver viewTreeObserver = gridView.getViewTreeObserver();
                                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                    @Override
                                    public void onGlobalLayout() {

                                        LinearLayout gridChild = (LinearLayout) gridView.getChildAt(1);
                                        imageLayout = (FrameLayout) gridChild.findViewById(R.id.imageLayout);
                                        imageLayout.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                        imageLayout.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                        TextView textView = (TextView)gridChild.findViewById(R.id.textView);
                                        textView.setText("BackSide");
                                        textView.setVisibility(View.VISIBLE);
                                    }
                                });
                            }

                        }
                        else{
                            imageBox1 = collegeID.get(0);
                            imageBox2 = collegeID.get(picNum);

                            imageBox1.setPicNum(picNum);
                            imageBox2.setPicNum(0);

                            imageBox2.setVerified("back");
                            imageBox1.setVerified("invalid");

                            collegeID.set(picNum,imageBox1);
                            collegeID.set(0,imageBox2);

                            createCollegeIdLayout();
                            ViewTreeObserver viewTreeObserver = gridView.getViewTreeObserver();
                            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                @Override
                                public void onGlobalLayout() {

                                    LinearLayout gridChild = (LinearLayout) gridView.getChildAt(0);
                                    imageLayout = (FrameLayout) gridChild.findViewById(R.id.imageLayout);
                                    imageLayout.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                    imageLayout.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                    TextView textView = (TextView)gridChild.findViewById(R.id.textView);
                                    textView.setText("BackSide");
                                    textView.setVisibility(View.VISIBLE);
                                }
                            });

                        }
                        backImageCollegeId = imageUrl;
                    }
                }
                else if(id == 2){
                    gridView = (GridView) findViewById(R.id.permanentAddressProofImages);
                    if(check.equals("front")){
                        if(frontImageAddressProof != null && backImageAddressProof != null){
                            if(picNum == 1) {
                                backImageAddressProof = null;
                                imageBox1 = addressProof.get(0);
                                imageBox2 = addressProof.get(1);

                                imageBox1.setPicNum(1);
                                imageBox2.setPicNum(0);

                                imageBox2.setVerified("front");
                                imageBox1.setVerified("invalid");

                                addressProof.set(0, imageBox2);
                                addressProof.set(1, imageBox1);

                                createAddressProofLayout();
                                ViewTreeObserver viewTreeObserver = gridView.getViewTreeObserver();
                                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                    @Override
                                    public void onGlobalLayout() {

                                        LinearLayout gridChild = (LinearLayout) gridView.getChildAt(0);
                                        imageLayout = (FrameLayout) gridChild.findViewById(R.id.imageLayout);
                                        imageLayout.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                        imageLayout.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                        textView = (TextView) gridChild.findViewById(R.id.textView);
                                        textView.setText("FrontSide");
                                        textView.setVisibility(View.VISIBLE);

                                        LinearLayout gridChild1 = (LinearLayout) gridView.getChildAt(1);
                                        imageLayout1 = (FrameLayout) gridChild1.findViewById(R.id.imageLayout);
                                        imageLayout1.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.VISIBLE);
                                        imageLayout1.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.INVISIBLE);
                                        textView = (TextView) gridChild1.findViewById(R.id.textView);
                                        textView.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }
                            else if(picNum == 0) {
                            }
                            else{
                                imageBox1 = addressProof.get(0);
                                imageBox2 = addressProof.get(picNum);

                                imageBox1.setPicNum(picNum);
                                imageBox2.setPicNum(0);

                                imageBox2.setVerified("front");
                                imageBox1.setVerified("invalid");

                                addressProof.set(picNum, imageBox1);
                                addressProof.set(0, imageBox2);

                                createAddressProofLayout();
                                ViewTreeObserver viewTreeObserver = gridView.getViewTreeObserver();
                                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                    @Override
                                    public void onGlobalLayout() {

                                        LinearLayout gridChild = (LinearLayout) gridView.getChildAt(0);
                                        imageLayout = (FrameLayout) gridChild.findViewById(R.id.imageLayout);
                                        imageLayout.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                        imageLayout.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                        textView = (TextView) gridChild.findViewById(R.id.textView);
                                        textView.setText("FrontSide");
                                        textView.setVisibility(View.VISIBLE);

                                    }
                                });
                            }
                        }
                        else if(backImageAddressProof != null ) {
                            if (picNum == 0) {
                                imageBox1 = addressProof.get(0);
                                imageBox1.setVerified("front");

                                backImageAddressProof = null;

                                createAddressProofLayout();
                                ViewTreeObserver viewTreeObserver = gridView.getViewTreeObserver();
                                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                    @Override
                                    public void onGlobalLayout() {

                                        LinearLayout gridChild = (LinearLayout) gridView.getChildAt(0);
                                        imageLayout = (FrameLayout) gridChild.findViewById(R.id.imageLayout);
                                        imageLayout.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                        imageLayout.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                        textView = (TextView) gridChild.findViewById(R.id.textView);
                                        textView.setText("FrontSide");
                                        textView.setVisibility(View.VISIBLE);

                                    }
                                });
                            } else if (picNum == 1) {
                                imageBox1 = addressProof.get(0);
                                imageBox2 = addressProof.get(picNum);

                                imageBox1.setPicNum(picNum);
                                imageBox2.setPicNum(0);

                                imageBox2.setVerified("front");
                                imageBox1.setVerified("back");

                                addressProof.set(0, imageBox2);
                                addressProof.set(picNum, imageBox1);

                                createAddressProofLayout();
                                ViewTreeObserver viewTreeObserver = gridView.getViewTreeObserver();
                                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                    @Override
                                    public void onGlobalLayout() {

                                        LinearLayout gridChild = (LinearLayout) gridView.getChildAt(0);
                                        imageLayout = (FrameLayout) gridChild.findViewById(R.id.imageLayout);
                                        imageLayout.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                        imageLayout.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                        textView = (TextView) gridChild.findViewById(R.id.textView);
                                        textView.setText("FrontSide");
                                        textView.setVisibility(View.VISIBLE);

                                        LinearLayout gridChild1 = (LinearLayout) gridView.getChildAt(1);
                                        imageLayout1 = (FrameLayout) gridChild1.findViewById(R.id.imageLayout);
                                        imageLayout1.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                        imageLayout1.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                        textView = (TextView) gridChild1.findViewById(R.id.textView);
                                        textView.setText("BackSide");
                                        textView.setVisibility(View.VISIBLE);
                                    }
                                });
                            } else {
                                imageBox1 = addressProof.get(0);
                                imageBox2 = addressProof.get(picNum);
                                imageBox3 = addressProof.get(1);

                                imageBox1.setPicNum(1);
                                imageBox2.setPicNum(0);
                                imageBox3.setPicNum(picNum);

                                imageBox2.setVerified("front");
                                imageBox1.setVerified("back");
                                imageBox3.setVerified("invalid");


                                addressProof.set(0, imageBox2);
                                addressProof.set(1, imageBox1);
                                addressProof.set(picNum, imageBox3);
                                createAddressProofLayout();
                                ViewTreeObserver viewTreeObserver = gridView.getViewTreeObserver();
                                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                    @Override
                                    public void onGlobalLayout() {

                                        LinearLayout gridChild = (LinearLayout) gridView.getChildAt(0);
                                        imageLayout = (FrameLayout) gridChild.findViewById(R.id.imageLayout);
                                        imageLayout.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                        imageLayout.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                        textView = (TextView) gridChild.findViewById(R.id.textView);
                                        textView.setText("FrontSide");
                                        textView.setVisibility(View.VISIBLE);

                                        LinearLayout gridChild1 = (LinearLayout) gridView.getChildAt(1);
                                        imageLayout1 = (FrameLayout) gridChild1.findViewById(R.id.imageLayout);
                                        imageLayout1.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                        imageLayout1.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                        textView = (TextView) gridChild1.findViewById(R.id.textView);
                                        textView.setText("BackSide");
                                        textView.setVisibility(View.VISIBLE);
                                    }
                                });

                            }
                        }
                        else{
                            imageBox1 = addressProof.get(0);
                            imageBox2 = addressProof.get(picNum);

                            imageBox1.setPicNum(picNum);
                            imageBox2.setPicNum(0);
                            imageBox1.setVerified("invalid");
                            imageBox2.setVerified("front");

                            addressProof.set(picNum,imageBox1);
                            addressProof.set(0,imageBox2);
                            createAddressProofLayout();
                            ViewTreeObserver viewTreeObserver = gridView.getViewTreeObserver();
                            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                @Override
                                public void onGlobalLayout() {

                                    LinearLayout gridChild = (LinearLayout) gridView.getChildAt(0);
                                    imageLayout = (FrameLayout) gridChild.findViewById(R.id.imageLayout);
                                    imageLayout.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                    imageLayout.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                    TextView textView = (TextView)gridChild.findViewById(R.id.textView);
                                    textView.setText("FrontSide");
                                    textView.setVisibility(View.VISIBLE);
                                }
                            });

                        }
                        frontImageAddressProof = imageUrl;
                    }
                    else if(check.equals("back")){

                        if( frontImageAddressProof != null && backImageAddressProof != null) {
                            if(picNum == 0) {
                                imageBox1 = addressProof.get(0);
                                imageBox2 = addressProof.get(1);

                                imageBox1.setVerified("back");
                                imageBox2.setVerified("invalid");

                                createAddressProofLayout();

                                frontImageCollegeId = null;
                                ViewTreeObserver viewTreeObserver = gridView.getViewTreeObserver();
                                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                    @Override
                                    public void onGlobalLayout() {

                                        LinearLayout gridChild = (LinearLayout) gridView.getChildAt(0);
                                        imageLayout = (FrameLayout) gridChild.findViewById(R.id.imageLayout);
                                        imageLayout.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                        imageLayout.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                        textView = (TextView) gridChild.findViewById(R.id.textView);
                                        textView.setText("BackSide");
                                        textView.setVisibility(View.VISIBLE);

                                        LinearLayout gridChild1 = (LinearLayout) gridView.getChildAt(1);
                                        imageLayout1 = (FrameLayout) gridChild1.findViewById(R.id.imageLayout);
                                        imageLayout1.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.VISIBLE);
                                        imageLayout1.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.INVISIBLE);
                                        textView = (TextView) gridChild1.findViewById(R.id.textView);
                                        textView.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }
                            else if(picNum == 1){}
                            else{
                                imageBox1 = addressProof.get(1);
                                imageBox2 = addressProof.get(picNum);

                                imageBox1.setPicNum(picNum);
                                imageBox2.setPicNum(1);

                                imageBox1.setVerified("invalid");
                                imageBox2.setVerified("back");

                                addressProof.set(picNum, imageBox1);
                                addressProof.set(1,imageBox2);

                                createAddressProofLayout();

                                ViewTreeObserver viewTreeObserver = gridView.getViewTreeObserver();
                                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                    @Override
                                    public void onGlobalLayout() {
                                        LinearLayout gridChild1 = (LinearLayout) gridView.getChildAt(picNum);
                                        imageLayout1 = (FrameLayout) gridChild1.findViewById(R.id.imageLayout);
                                        imageLayout1.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.VISIBLE);
                                        imageLayout1.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.INVISIBLE);
                                        textView = (TextView) gridChild1.findViewById(R.id.textView);
                                        textView.setVisibility(View.INVISIBLE);

                                        LinearLayout gridChild = (LinearLayout) gridView.getChildAt(1);
                                        imageLayout = (FrameLayout) gridChild.findViewById(R.id.imageLayout);
                                        imageLayout.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                        imageLayout.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                        textView = (TextView) gridChild.findViewById(R.id.textView);
                                        textView.setText("BackSide");
                                        textView.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        }
                        else if(frontImageAddressProof != null ){
                            if(picNum == 0){
                                imageBox1 = addressProof.get(0);

                                imageBox1.setVerified("back");
                                frontImageAddressProof = null;
                                createAddressProofLayout();
                                ViewTreeObserver viewTreeObserver = gridView.getViewTreeObserver();
                                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                    @Override
                                    public void onGlobalLayout() {

                                        LinearLayout gridChild = (LinearLayout) gridView.getChildAt(0);
                                        imageLayout = (FrameLayout) gridChild.findViewById(R.id.imageLayout);
                                        imageLayout.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                        imageLayout.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                        textView = (TextView)gridChild.findViewById(R.id.textView);
                                        textView.setText("BackSide");
                                        textView.setVisibility(View.VISIBLE);

                                    }
                                });
                            }
                            else if(picNum == 1){
                                imageBox1 = addressProof.get(1);
                                imageBox1.setVerified("back");

                                createAddressProofLayout();
                                ViewTreeObserver viewTreeObserver = gridView.getViewTreeObserver();
                                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                    @Override
                                    public void onGlobalLayout() {
                                        LinearLayout gridChild = (LinearLayout) gridView.getChildAt(1);
                                        imageLayout = (FrameLayout) gridChild.findViewById(R.id.imageLayout);
                                        imageLayout.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                        imageLayout.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                        TextView textView = (TextView)gridChild.findViewById(R.id.textView);
                                        textView.setText("BackSide");
                                        textView.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                            else{
                                imageBox1 = addressProof.get(1);
                                imageBox2 = addressProof.get(picNum);

                                imageBox1.setPicNum(picNum);
                                imageBox2.setPicNum(1);

                                imageBox2.setVerified("back");
                                imageBox1.setVerified("invalid");


                                addressProof.set(picNum,imageBox1);
                                addressProof.set(1,imageBox2);

                                createAddressProofLayout();
                                ViewTreeObserver viewTreeObserver = gridView.getViewTreeObserver();
                                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                    @Override
                                    public void onGlobalLayout() {

                                        LinearLayout gridChild = (LinearLayout) gridView.getChildAt(1);
                                        imageLayout = (FrameLayout) gridChild.findViewById(R.id.imageLayout);
                                        imageLayout.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                        imageLayout.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                        TextView textView = (TextView)gridChild.findViewById(R.id.textView);
                                        textView.setText("BackSide");
                                        textView.setVisibility(View.VISIBLE);
                                    }
                                });
                            }

                        }
                        else{
                            imageBox1 = addressProof.get(0);
                            imageBox2 = addressProof.get(picNum);

                            imageBox1.setPicNum(picNum);
                            imageBox2.setPicNum(0);

                            imageBox2.setVerified("back");
                            imageBox1.setVerified("invalid");


                            addressProof.set(picNum,imageBox1);
                            addressProof.set(0,imageBox2);

                            createAddressProofLayout();
                            ViewTreeObserver viewTreeObserver = gridView.getViewTreeObserver();
                            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                @Override
                                public void onGlobalLayout() {

                                    LinearLayout gridChild = (LinearLayout) gridView.getChildAt(0);
                                    imageLayout = (FrameLayout) gridChild.findViewById(R.id.imageLayout);
                                    imageLayout.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                    imageLayout.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                    TextView textView = (TextView)gridChild.findViewById(R.id.textView);
                                    textView.setText("BackSide");
                                    textView.setVisibility(View.VISIBLE);
                                }
                            });

                        }
                        backImageAddressProof = imageUrl;
                    }
                }
                else if(id == 3){
                    gridView = (GridView) findViewById(R.id.gradeSheetImages);
                    if(check.equals("valid")){
                        imageBox1 = gradeSheet.get(picNum);
                        imageBox1.setVerified("valid");
                        createGradeSheetsLayout();
                        ViewTreeObserver viewTreeObserver = gridView.getViewTreeObserver();
                        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                            @Override
                            public void onGlobalLayout() {
                                    LinearLayout gridChild = (LinearLayout) gridView.getChildAt(picNum);
                                    imageLayout = (FrameLayout) gridChild.findViewById(R.id.imageLayout);
                                    imageLayout.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                    imageLayout.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                    textView = (TextView) gridChild.findViewById(R.id.textView);
                                    textView.setVisibility(View.INVISIBLE);

                            }
                        });
                    }
                    else if(check.equals("invalid")){
                        imageBox1 = gradeSheet.get(picNum);
                        imageBox1.setVerified("invalid");
                        createGradeSheetsLayout();
                        ViewTreeObserver viewTreeObserver = gridView.getViewTreeObserver();
                        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                            @Override
                            public void onGlobalLayout() {
                                LinearLayout gridChild = (LinearLayout) gridView.getChildAt(picNum);
                                imageLayout = (FrameLayout) gridChild.findViewById(R.id.imageLayout);
                                imageLayout.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.VISIBLE);
                                imageLayout.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.INVISIBLE);
                                textView = (TextView) gridChild.findViewById(R.id.textView);
                                textView.setVisibility(View.INVISIBLE);

                            }
                        });
                    }
                }
                else if(id == 4){
                    gridView = (GridView) findViewById(R.id.bankProofImages);
                    if(check.equals("valid")){
                        imageBox1 = bankProof.get(picNum);
                        imageBox1.setVerified("valid");
                        createBankProofsLayout();
                        ViewTreeObserver viewTreeObserver = gridView.getViewTreeObserver();
                        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                            @Override
                            public void onGlobalLayout() {
                                LinearLayout gridChild = (LinearLayout) gridView.getChildAt(picNum);
                                imageLayout1 = (FrameLayout) gridChild.findViewById(R.id.imageLayout);
                                imageLayout1.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                imageLayout1.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                textView = (TextView) gridChild.findViewById(R.id.textView);
                                textView.setVisibility(View.INVISIBLE);

                            }
                        });
                    }
                    else if(check.equals("invalid")){
                        imageBox1 = bankProof.get(picNum);
                        imageBox1.setVerified("invalid");
                        createBankProofsLayout();
                        ViewTreeObserver viewTreeObserver = gridView.getViewTreeObserver();
                        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                            @Override
                            public void onGlobalLayout() {
                                LinearLayout gridChild = (LinearLayout) gridView.getChildAt(picNum);
                                imageLayout1 = (FrameLayout) gridChild.findViewById(R.id.imageLayout);
                                imageLayout1.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.VISIBLE);
                                imageLayout1.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.INVISIBLE);
                                textView = (TextView) gridChild.findViewById(R.id.textView);
                                textView.setVisibility(View.INVISIBLE);

                            }
                        });
                    }
                }

            }
        }
    }

    public void confirmSubmit(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure the details entered are correct?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        borrowerDataUpdater.updateToApi();
                    }


                })
                .setNegativeButton("No", null)						//Do nothing on no
                .show();
    }

    public void insertCollegeImageBox(ImageBox imageBox){
        collegeID.add(imageBox);
    }

    public void insertAddressProofImageBox(ImageBox imageBox){
        addressProof.add(imageBox);
    }

    public void insertGradeSheetImageBox(ImageBox imageBox){ gradeSheet.add(imageBox); }

    public void insertBankProofImageBox(ImageBox imageBox){ bankProof.add(imageBox); }

    public void removeGradeSheetImageBox(){ gradeSheet.remove(gradeSheet.size()-1); }

    public void removeBankProofImageBox(){ bankProof.remove(bankProof.size()-1); }

    public void removeCollegeImageBox(){
        collegeID.remove(collegeID.size()-1);
    }

    public void removeAddressProofImageBox(){
        addressProof.remove(addressProof.size()-1);
    }

    public void createCollegeIdLayout(){
        ExpandableHeightGridView gridView = (ExpandableHeightGridView) findViewById(R.id.collegeIdImages);
        gridView.setAdapter(new GridViewAdapter(this,collegeID));
    }

    public void createAddressProofLayout(){
        ExpandableHeightGridView gridView = (ExpandableHeightGridView) findViewById(R.id.permanentAddressProofImages);
        gridView.setAdapter(new GridViewAdapter(this,addressProof));
    }

    public void createGradeSheetsLayout(){
        ExpandableHeightGridView gridView = (ExpandableHeightGridView) findViewById(R.id.gradeSheetImages);
        gridView.setAdapter(new GridViewAdapter(this,gradeSheet));
    }

    public void createBankProofsLayout(){
        ExpandableHeightGridView gridView = (ExpandableHeightGridView) findViewById(R.id.bankProofImages);
        gridView.setAdapter(new GridViewAdapter(this,bankProof));
    }

    public String getFrontImageCollegeId() { return frontImageCollegeId; }

    public String getBackImageCollegeId() { return backImageCollegeId; }

    public String getFrontImageAddressProof() { return frontImageAddressProof; }

    public String getBackImageAddressProof() { return backImageAddressProof; }

    public String getGoodFriendsRadio() {
        return  goodFriendsRadio;
    }

    public String getYearBackRadio() { return yearBackRadio;}

    public String getRepayBackLoan() { return loanRepay;}

    public String getDate() { return uploadDocModel.getDate(); }

    public ArrayList<ImageBox> getCollegeIDs() {
        return collegeID;
    }

    public ArrayList<ImageBox> getAddressProofs() { return addressProof; }

    public ArrayList<ImageBox> getGradeSheets() { return gradeSheet; }

    public ArrayList<ImageBox> getBankProofs() { return bankProof; }

    public String getTransparentRadio() { return transparentRadio;}

    public String getGiveLoanRadio() { return giveLoanRadio;}

    public UploadDocModel getUploadDocModel() { return uploadDocModel;}

    public BorrowerData getBorrowerData() {
        return borrowerData;
    }

    public VerificationInfo getVerInfo() {
        return verInfo;
    }

    public Toolbar getToolBar() {
        return toolBar;
    }

    public TextView getPhoneNum() {
        return phoneNum;
    }

    public TextView getName() {
        return name;
    }

    public TextView getCollege() {
        return college;
    }

    public int getNumCollegeIds() { return collegeID.size()-1;}

    public int getNumAddressProofs() { return addressProof.size()-1;}

    public int getNumGradeSheets() { return gradeSheet.size()-1; }

    public int getNumBankProofs() { return bankProof.size()-1; }

    public Button getSubmitButton() { return submitButton; }

    public RadioGroup getReferenceSelection() {
        return referenceSelection;
    }

    public RadioGroup getYearBackSelection() {return yearBackSelection; }

    public RadioGroup getTransparentSelection() {return transparentSelection; }

    public RadioGroup getInternSelection() {return internSelection; }

    public String getRefYear() {
        return refYear.getText().toString();
    }

    public String getRefDept() {
        return refDept.getText().toString();
    }

    public EditText getOtherNotes() {
        return otherNotes;
    }

    public ImageView getPhoneIcon() {
        return phoneIcon;
    }

    public ImageView getIdIcon() {
        return IdIcon;
    }

    public ImageView getIdIconPlusFront() {
        return IdIconPlusFront;
    }

    public ImageView getIdIconPlusBack() {
        return IdIconPlusBack;
    }

    public FrameLayout getImageLayout() {
        return imageLayout;
    }

    public void setImageLayout(FrameLayout imageLayout) {this.imageLayout = imageLayout; }

    public String getSpinnerPunc() {
        return punctualityInClass;
    }

    public String getSpinnerSincere() {
        return sincerityInStudies;
    }

    public String getSpinnerCocurricular() {
        return coCurricularParticipation;
    }

    public String getSpinnerFinRes() {
        return financiallyResponsible;
    }

    public CircularImageView getProfilePic() {
        return profilePic;
    }

    public ImageView getIm() {
        return Im;
    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectItem;
        if(parent.getItemAtPosition(position)!=null)
        {
            selectItem = parent.getItemAtPosition(position).toString();
        }
        else
        {
            selectItem = "";
        }
        switch (parent.getId())
        {
            case R.id.punctality: punctualityInClass = selectItem;  break;
            case R.id.finResponsibility: financiallyResponsible = selectItem;  break;
            case R.id.sincerity: sincerityInStudies = selectItem;  break;
            case R.id.coCurricular: coCurricularParticipation = selectItem;  break;
            case R.id.Loan_repay: loanRepay = selectItem;  break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void methodToCallBack(String imageUrl) {
        imageurl=imageUrl;
        int id;
        SharedPreferences settings = getSharedPreferences(BuddyConstants.PREFS_FILE, 0);
        imageBox = new ImageBox(imageLayout,BorrowerDetailsActivity.this);
        imageBoxtemp = new ImageBox(imageLayout,BorrowerDetailsActivity.this);
        id = settings.getInt("conform",-1);
        imageBox.setId(id);
        imageBoxtemp.setId(id);
        if(id == 1){
            imageBox.setPicType("College ID");
            imageBox.setCloudinaryId("collegeId");
            imageBox.setImageUrl(imageurl);
            imageBox.setVerified("invalid");
            removeCollegeImageBox();
            imageBox.setPicNum(collegeID.size());
            insertCollegeImageBox(imageBox);
            imageBoxtemp.setPicType("College ID");
            imageBoxtemp.setCloudinaryId("collegeId");
            imageBoxtemp.setImageUrl(null);
            imageBoxtemp.setPicNum(collegeID.size());
            insertCollegeImageBox(imageBoxtemp);
            createCollegeIdLayout();
        }
        else if(id == 2){
            imageBox.setPicType("Permanent Address Proof");
            imageBox.setCloudinaryId("addressProof");
            imageBox.setImageUrl(imageurl);
            imageBox.setVerified("invalid");
            removeAddressProofImageBox();
            imageBox.setPicNum(addressProof.size());
            insertAddressProofImageBox(imageBox);
            imageBoxtemp.setPicType("Permanent Address Proof");
            imageBoxtemp.setCloudinaryId("addressProof");
            imageBoxtemp.setImageUrl(null);
            imageBoxtemp.setPicNum(addressProof.size());
            insertAddressProofImageBox(imageBoxtemp);
            createAddressProofLayout();
        }
        else if(id == 3){
            imageBox.setPicType("Grade Sheet");
            imageBox.setCloudinaryId("gradeSheet");
            imageBox.setImageUrl(imageurl);
            imageBox.setVerified("invalid");
            removeGradeSheetImageBox();
            imageBox.setPicNum(gradeSheet.size());
            insertGradeSheetImageBox(imageBox);
            imageBoxtemp.setPicType("Grade Sheet");
            imageBoxtemp.setCloudinaryId("gradeSheet");
            imageBoxtemp.setImageUrl(null);
            imageBoxtemp.setPicNum(gradeSheet.size());
            insertGradeSheetImageBox(imageBoxtemp);
            createGradeSheetsLayout();
        }
        else if(id == 4){
            imageBox.setPicType("Bank Proof");
            imageBox.setCloudinaryId("bankProof");
            imageBox.setImageUrl(imageurl);
            imageBox.setVerified("invalid");
            removeBankProofImageBox();
            imageBox.setPicNum(bankProof.size());
            insertBankProofImageBox(imageBox);
            imageBoxtemp.setPicType("Bank Proof");
            imageBoxtemp.setCloudinaryId("bankProof");
            imageBoxtemp.setImageUrl(null);
            imageBoxtemp.setPicNum(bankProof.size());
            insertBankProofImageBox(imageBoxtemp);
            createBankProofsLayout();
        }
    }
    @Override
    public void onBackPressed() {
        borrowerDataUpdater = new BorrowerDataUpdater(this, "back");
        borrowerDataUpdater.updateToApi();
        super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        if(requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                this.startActivityForResult(intent, 1);
            }
        }
        else if(requestCode == 2){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                this.startActivityForResult(intent, 2);
            }
        }
        else if(requestCode == 3){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                String phone_num = (phoneNum.getText()).toString();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phone_num));
                {startActivity(callIntent);}
            }
        }
        else if(requestCode == 3){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                String phone_num = (friendPhoneNum.getText()).toString();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phone_num));
                {startActivity(callIntent);}
            }
        }


    }


}
