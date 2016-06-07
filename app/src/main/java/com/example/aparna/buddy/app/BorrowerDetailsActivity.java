package com.example.aparna.buddy.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
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
    private BorrowerStateContainer state;
    private UserInfo userInfo;
    private VerificationInfo verInfo;
    private Toolbar toolBar;
    private TextView phoneNum, name, college;
    private TextView friendPhoneNum, friendName;
    private RadioGroup referenceSelection, yearBackSelection, transparentSelection, internSelection;
    private EditText refYear, refDept, otherNotes;
    private ImageView phoneIcon, IdIcon, IdIconPlusFront, IdIconPlusBack;
    private FrameLayout collegeIdFrontLayout, collegeIdBackLayout, addressProofFrontLayout, addressProofBackLayout;
    private ImageBox collegeIdFrontImageBox, collegeIdBackImageBox, addressProofFrontImageBox, addressProofBackImageBox;
    private FrameLayout imageLayout, imageLayout1, imageLayout2;
    private ImageBox imageBox, imageBoxtemp;
    private Map<Integer, ImageBox> allImageBoxes = new HashMap<>();
    private AppCompatSpinner spinnerPunc,spinnerSincere,spinnerCocurricular,spinnerFinRes,spinnerLoan;
    private Spinner spinner;
    private CircularImageView profilePic;
    private String newPicClickedImageBoxId;
    private LinearLayout rootLayoutMain;
    private boolean spinnerFlag1, spinnerFlag2, spinnerFlag3, spinnerFlag4,spinnerFlag5 = true, spinnerFlag;
    private String goodFriendsRadio, yearBackRadio, transparentRadio, giveLoanRadio;
    private Button submitButton;
    private ArrayList<ImageBox> collegeIds, addressProofs, gradeSheets, bankProofs;
    private String items[];
    private TextView textView;
    private String frontImageCollegeId, backImageCollegeId, frontImageAddressProof, backImageAddressProof;
    private String punctualityInClass, sincerityInStudies, coCurricularParticipation, financiallyResponsible, loanRepay, friendVerificationNotes;
    //String idFront, idBack, addFront, addBack;
    ImageView Im;
    private String imageurl;
    BorrowerDataUpdater borrowerDataUpdater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrower_details);

        final BorrowerStateContainer borrowerStateContainer = new BorrowerStateContainer(this);
        borrowerDataUpdater = new BorrowerDataUpdater(this, "button");

        Bundle extras = getIntent().getExtras();

        spinnerFlag1 = false;
        spinnerFlag2 = false;
        spinnerFlag3 = false;
        spinnerFlag4 = false;
        spinnerFlag5 = false;
        spinnerFlag = false;


        borrowerData = new Gson().fromJson(extras.getString("borrowerData"), BorrowerData.class);
        state = new BorrowerStateContainer(BorrowerDetailsActivity.this);
        userInfo = borrowerData.getUserInfo();
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

        rootLayoutMain = (LinearLayout) findViewById(R.id.rootLayoutMain);
        rootLayoutMain.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.d("global", "shizz");
                borrowerStateContainer.update();
            }
        });

        submitButton = (Button) findViewById(R.id.submitButton);


        collegeIds = new ArrayList<>();

        if (userInfo.getCollegeIDs() != null) {
            int count;
            for (count = 0 ; count < userInfo.getCollegeIDs().size(); count++) {
                ImageBox ib = new ImageBox(imageLayout,BorrowerDetailsActivity.this);
                ib.setImageUrl(userInfo.getCollegeIDs().get(count));
                ib.setPicNum(count);
                ib.setPicType("College ID");
                ib.setCloudinaryId("collegeId");
                ib.setId(1);
                ib.setVerified("invalid");
                insertCollegeImageBox(ib);
            }
            ImageBox ib = new ImageBox(imageLayout,BorrowerDetailsActivity.this);
            ib.setImageUrl(null);
            ib.setPicNum(count);
            ib.setPicType("College ID");
            ib.setCloudinaryId("collegeId");
            ib.setId(1);
            ib.setVerified("invalid");
            insertCollegeImageBox(ib);
        }
        else{
            ImageBox ib = new ImageBox(imageLayout,BorrowerDetailsActivity.this);
            ib.setImageUrl(null);
            ib.setPicNum(0);
            ib.setPicType("College ID");
            ib.setCloudinaryId("collegeId");
            ib.setId(1);
            ib.setVerified("invalid");
            insertCollegeImageBox(ib);
        }

        createCollegeIdLayout();

        addressProofs = new ArrayList<>();

        if(userInfo.getAddressProofs() != null) {
            int count;
            for (count = 0; count < userInfo.getAddressProofs().size(); count++) {
                ImageBox ib = new ImageBox(imageLayout, BorrowerDetailsActivity.this);
                ib.setImageUrl(userInfo.getAddressProofs().get(count));
                ib.setPicNum(count);
                ib.setPicType("Permanent Address Proof");
                ib.setCloudinaryId("addressProof");
                ib.setId(2);
                ib.setVerified("invalid");
                insertAddressProofImageBox(ib);
            }
            ImageBox ib = new ImageBox(imageLayout, BorrowerDetailsActivity.this);
            ib.setImageUrl(null);
            ib.setPicNum(count);
            ib.setPicType("Permanent Address Proof");
            ib.setCloudinaryId("addressProof");
            ib.setId(2);
            ib.setVerified("invalid");
            insertAddressProofImageBox(ib);
        }
        else{
            ImageBox ib = new ImageBox(imageLayout, BorrowerDetailsActivity.this);
            ib.setImageUrl(null);
            ib.setPicNum(0);
            ib.setPicType("Permanent Address Proof");
            ib.setCloudinaryId("addressProof");
            ib.setId(2);
            ib.setVerified("invalid");
            insertAddressProofImageBox(ib);
        }

        createAddressProofLayout();

        gradeSheets = new ArrayList<>();

        if (userInfo.getGradeSheets() != null) {
            int count;
            for (count = 0 ; count < userInfo.getGradeSheets().size(); count++) {
                ImageBox ib = new ImageBox(imageLayout,BorrowerDetailsActivity.this);
                ib.setImageUrl(userInfo.getGradeSheets().get(count));
                ib.setPicNum(count);
                ib.setPicType("Grade Sheet");
                ib.setCloudinaryId("gradeSheet");
                ib.setId(3);
                ib.setVerified("invalid");
                insertCollegeImageBox(ib);
            }
            ImageBox ib = new ImageBox(imageLayout,BorrowerDetailsActivity.this);
            ib.setImageUrl(null);
            ib.setPicNum(count);
            ib.setPicType("Grade Sheet");
            ib.setCloudinaryId("gradeSheet");
            ib.setId(3);
            ib.setVerified("invalid");
            insertCollegeImageBox(ib);
        }
        else{
            ImageBox ib = new ImageBox(imageLayout,BorrowerDetailsActivity.this);
            ib.setImageUrl(null);
            ib.setPicNum(0);
            ib.setPicType("Grade Sheet");
            ib.setCloudinaryId("gradeSheet");
            ib.setVerified("invalid");
            ib.setId(3);
            insertGradeSheetImageBox(ib);
        }

        createGradeSheetsLayout();

        bankProofs = new ArrayList<>();

        if (userInfo.getBankProofs() != null) {
            int count;
            for (count = 0 ; count < userInfo.getBankProofs().size(); count++) {
                ImageBox ib = new ImageBox(imageLayout,BorrowerDetailsActivity.this);
                ib.setImageUrl(userInfo.getBankProofs().get(count));
                ib.setPicNum(count);
                ib.setPicType("Bank Proof");
                ib.setCloudinaryId("bankProof");
                ib.setId(4);
                ib.setVerified("invalid");
                insertBankProofImageBox(ib);
            }
            ImageBox ib = new ImageBox(imageLayout,BorrowerDetailsActivity.this);
            ib.setImageUrl(null);
            ib.setPicNum(count);
            ib.setPicType("Bank Proof");
            ib.setCloudinaryId("bankProof");
            ib.setId(4);
            ib.setVerified("invalid");
            insertBankProofImageBox(ib);
        }
        else{
            ImageBox ib = new ImageBox(imageLayout,BorrowerDetailsActivity.this);
            ib.setImageUrl(null);
            ib.setPicNum(0);
            ib.setPicType("Bank Proof");
            ib.setCloudinaryId("bankProof");
            ib.setId(4);
            ib.setVerified("invalid");
            insertBankProofImageBox(ib);
        }

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

        String userId = userInfo.getFbUserId();
        Picasso.with(this)
                .load("https://graph.facebook.com/" + userId + "/picture?type=large")
                .into(profilePic);

        name.setText(userInfo.getName());

        phoneNum.setText(userInfo.getPhone());
        phoneNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone_num = (phoneNum.getText()).toString();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phone_num));
                {
                    startActivity(callIntent);
                }
            }
        });
        college.setTextSize(18 - (userInfo.getCollege().length()) / 8);
        college.setText(userInfo.getCollege());

        friendName.setText(userInfo.getFriendName());

        friendPhoneNum.setText(userInfo.getFriendNumber());
        friendPhoneNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone_num = (friendPhoneNum.getText()).toString();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phone_num));
                {
                    startActivity(callIntent);
                }
            }
        });

        if (verInfo.getReferenceIsGoodFriend()) {
            referenceSelection.check(R.id.goodFriendsYes);
        } else {
            referenceSelection.check(R.id.goodFriendsNo);
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
            } else {
                yearBackSelection.check(R.id.yearBackNo);
            }
        }

        if(verInfo.getGiveHimLoan() != null) {
            if (verInfo.getGiveHimLoan()) {
                internSelection.check(R.id.internchoiceYes);
            } else {
                internSelection.check(R.id.internchoiceNo);
            }
        }

        if(verInfo.getBorrowerIsLying() != null) {
            if (verInfo.getBorrowerIsLying()) {
                transparentSelection.check(R.id.transparentYes);
            } else {
                transparentSelection.check(R.id.transparentNo);
            }
        }

        spinnerPunc = (AppCompatSpinner) findViewById(R.id.punctality);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.opinions, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_list);
        //spinner.setAdapter(adapter);
        spinnerPunc.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.nothing_selected_spinner,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));
        spinnerPunc.setOnItemSelectedListener(this);

        if(verInfo.getPunctualityInClass() != null){
            spinnerPunc.setSelection(adapter.getPosition(verInfo.getPunctualityInClass()) + 1);
            spinnerFlag1 = true;
        }

        spinnerFinRes = (AppCompatSpinner) findViewById(R.id.finResponsibility);
        // Create an ArrayAdapter using the string array and a default spinner layout
        spinnerFinRes.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.nothing_selected_spinner,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));
        spinnerFinRes.setOnItemSelectedListener(this);

        if(verInfo.getFinanciallyResponsible() != null){
            spinnerFinRes.setSelection(adapter.getPosition(verInfo.getFinanciallyResponsible()) + 1);
            spinnerFlag2 = true;
        }

        spinnerSincere = (AppCompatSpinner) findViewById(R.id.sincerity);
        // Create an ArrayAdapter using the string array and a default spinner layout
        spinnerSincere.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.nothing_selected_spinner,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));

        spinnerSincere.setOnItemSelectedListener(this);

        if(verInfo.getSincerityInStudies() != null){
            spinnerSincere.setSelection(adapter.getPosition(verInfo.getSincerityInStudies()) + 1);
            spinnerFlag3 = true;
        }

        spinnerCocurricular = (AppCompatSpinner) findViewById(R.id.coCurricular);
        // Create an ArrayAdapter using the string array and a default spinner layout
        spinnerCocurricular.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.nothing_selected_spinner,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));
        spinnerCocurricular.setOnItemSelectedListener(this);

        if(verInfo.getCoCurricularParticipation() != null){
            spinnerCocurricular.setSelection(adapter.getPosition(verInfo.getCoCurricularParticipation()) + 1);
            spinnerFlag4 = true;
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
            spinnerFlag5 = true;
        }

        if(spinnerFlag1 && spinnerFlag2 && spinnerFlag3 && spinnerFlag4 && spinnerFlag5) {
            spinnerFlag = true;
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
                // If image box is null, then go check for imagebox in extended list
                // This new pic clicked imagebox should go to state container

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
                // If image box is null, then go check for imagebox in extended list
                // This new pic clicked imagebox should go to state container

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
                                imageBox1 = collegeIds.get(0);
                                imageBox2 = collegeIds.get(1);

                                imageBox1.setPicNum(1);
                                imageBox2.setPicNum(0);

                                imageBox2.setVerified("front");
                                imageBox1.setVerified("invalid");

                                collegeIds.set(0, imageBox2);
                                collegeIds.set(1, imageBox1);

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
                                imageBox1 = collegeIds.get(0);
                                imageBox2 = collegeIds.get(picNum);

                                imageBox1.setPicNum(picNum);
                                imageBox2.setPicNum(0);

                                imageBox2.setVerified("front");
                                imageBox1.setVerified("invalid");

                                collegeIds.set(picNum, imageBox1);
                                collegeIds.set(0, imageBox2);

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
                                imageBox1 = collegeIds.get(0);
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
                                imageBox1 = collegeIds.get(0);
                                imageBox2 = collegeIds.get(picNum);

                                imageBox1.setPicNum(picNum);
                                imageBox2.setPicNum(0);

                                imageBox2.setVerified("front");
                                imageBox1.setVerified("back");

                                collegeIds.set(0, imageBox2);
                                collegeIds.set(picNum, imageBox1);

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
                                imageBox1 = collegeIds.get(0);
                                imageBox2 = collegeIds.get(picNum);
                                imageBox3 = collegeIds.get(1);

                                imageBox1.setPicNum(1);
                                imageBox2.setPicNum(0);
                                imageBox3.setPicNum(picNum);

                                imageBox2.setVerified("front");
                                imageBox1.setVerified("back");
                                if(collegeIds.get(1).getVerified().equals("valid")) {
                                    imageBox3.setVerified(collegeIds.get(1).getVerified());
                                }
                                else{
                                    imageBox3.setVerified("invalid");
                                }

                                collegeIds.set(0, imageBox2);
                                collegeIds.set(1, imageBox1);
                                collegeIds.set(picNum, imageBox3);
                                createCollegeIdLayout();
                                ViewTreeObserver viewTreeObserver = gridView.getViewTreeObserver();
                                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                    @Override
                                    public void onGlobalLayout() {
                                        if(collegeIds.get(picNum).getVerified().equals("valid")) {
                                            LinearLayout gridChild2 = (LinearLayout) gridView.getChildAt(picNum);
                                            imageLayout2 = (FrameLayout) gridChild2.findViewById(R.id.imageLayout);
                                            imageLayout2.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                            imageLayout2.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                            textView = (TextView) gridChild2.findViewById(R.id.textView);
                                            textView.setVisibility(View.INVISIBLE);
                                        }
                                        else{
                                            LinearLayout gridChild2 = (LinearLayout) gridView.getChildAt(picNum);
                                            imageLayout2 = (FrameLayout) gridChild2.findViewById(R.id.imageLayout);
                                            imageLayout2.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.VISIBLE);
                                            imageLayout2.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.INVISIBLE);
                                            textView = (TextView) gridChild2.findViewById(R.id.textView);
                                            textView.setVisibility(View.INVISIBLE);
                                        }

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
                            imageBox1 = collegeIds.get(0);
                            imageBox2 = collegeIds.get(picNum);

                            imageBox1.setPicNum(picNum);
                            imageBox2.setPicNum(0);
                            if(collegeIds.get(1).getVerified().equals("valid")) {
                                imageBox1.setVerified(collegeIds.get(0).getVerified());
                            }
                            else{
                                imageBox1.setVerified("invalid");
                            }
                            imageBox2.setVerified("front");

                            collegeIds.set(picNum,imageBox1);
                            collegeIds.set(0,imageBox2);
                            createCollegeIdLayout();
                            ViewTreeObserver viewTreeObserver = gridView.getViewTreeObserver();
                            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                @Override
                                public void onGlobalLayout() {
                                    if(collegeIds.get(picNum).getVerified().equals("valid")) {
                                        LinearLayout gridChild1 = (LinearLayout) gridView.getChildAt(picNum);
                                        imageLayout1 = (FrameLayout) gridChild1.findViewById(R.id.imageLayout);
                                        imageLayout1.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                        imageLayout1.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                        textView = (TextView) gridChild1.findViewById(R.id.textView);
                                        textView.setVisibility(View.INVISIBLE);
                                    }
                                    else{
                                        LinearLayout gridChild1 = (LinearLayout) gridView.getChildAt(picNum);
                                        imageLayout1 = (FrameLayout) gridChild1.findViewById(R.id.imageLayout);
                                        imageLayout1.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.VISIBLE);
                                        imageLayout1.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.INVISIBLE);
                                        textView = (TextView) gridChild1.findViewById(R.id.textView);
                                        textView.setVisibility(View.INVISIBLE);
                                    }

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
                                imageBox1 = collegeIds.get(0);
                                imageBox2 = collegeIds.get(1);

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
                                imageBox1 = collegeIds.get(1);
                                imageBox2 = collegeIds.get(picNum);

                                imageBox1.setPicNum(picNum);
                                imageBox2.setPicNum(1);

                                imageBox1.setVerified("invalid");
                                imageBox2.setVerified("back");

                                collegeIds.set(picNum, imageBox1);
                                collegeIds.set(1,imageBox2);

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
                                imageBox1 = collegeIds.get(0);

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
                                imageBox1 = collegeIds.get(1);
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
                                imageBox1 = collegeIds.get(1);
                                imageBox2 = collegeIds.get(picNum);

                                imageBox1.setPicNum(picNum);
                                imageBox2.setPicNum(1);

                                imageBox2.setVerified("back");
                                if(collegeIds.get(1).getVerified().equals("valid")) {
                                    imageBox1.setVerified(collegeIds.get(1).getVerified());
                                }
                                else{
                                    imageBox1.setVerified("invalid");
                                }

                                collegeIds.set(picNum,imageBox1);
                                collegeIds.set(1,imageBox2);

                                createCollegeIdLayout();
                                ViewTreeObserver viewTreeObserver = gridView.getViewTreeObserver();
                                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                    @Override
                                    public void onGlobalLayout() {
                                        if(collegeIds.get(picNum).getVerified().equals("valid")) {
                                            LinearLayout gridChild1 = (LinearLayout) gridView.getChildAt(picNum);
                                            imageLayout1 = (FrameLayout) gridChild1.findViewById(R.id.imageLayout);
                                            imageLayout1.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                            imageLayout1.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                            textView = (TextView) gridChild1.findViewById(R.id.textView);
                                            textView.setVisibility(View.INVISIBLE);
                                        }
                                        else{
                                            LinearLayout gridChild1 = (LinearLayout) gridView.getChildAt(picNum);
                                            imageLayout1 = (FrameLayout) gridChild1.findViewById(R.id.imageLayout);
                                            imageLayout1.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.VISIBLE);
                                            imageLayout1.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.INVISIBLE);
                                            textView = (TextView) gridChild1.findViewById(R.id.textView);
                                            textView.setVisibility(View.INVISIBLE);
                                        }

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
                            imageBox1 = collegeIds.get(0);
                            imageBox2 = collegeIds.get(picNum);

                            imageBox1.setPicNum(picNum);
                            imageBox2.setPicNum(0);

                            imageBox2.setVerified("back");
                            if(collegeIds.get(1).getVerified().equals("valid")) {
                                imageBox1.setVerified(collegeIds.get(0).getVerified());
                            }
                            else{
                                imageBox1.setVerified("invalid");
                            }

                            collegeIds.set(picNum,imageBox1);
                            collegeIds.set(0,imageBox2);

                            createCollegeIdLayout();
                            ViewTreeObserver viewTreeObserver = gridView.getViewTreeObserver();
                            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                @Override
                                public void onGlobalLayout() {
                                    if(collegeIds.get(picNum).getVerified().equals("valid")) {
                                        LinearLayout gridChild1 = (LinearLayout) gridView.getChildAt(picNum);
                                        imageLayout1 = (FrameLayout) gridChild1.findViewById(R.id.imageLayout);
                                        imageLayout1.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                        imageLayout1.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                        textView = (TextView) gridChild1.findViewById(R.id.textView);
                                        textView.setVisibility(View.INVISIBLE);
                                    }
                                    else{
                                        LinearLayout gridChild1 = (LinearLayout) gridView.getChildAt(picNum);
                                        imageLayout1 = (FrameLayout) gridChild1.findViewById(R.id.imageLayout);
                                        imageLayout1.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.VISIBLE);
                                        imageLayout1.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.INVISIBLE);
                                        textView = (TextView) gridChild1.findViewById(R.id.textView);
                                        textView.setVisibility(View.INVISIBLE);
                                    }

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
                    else if(check.equals("valid")){
                            imageBox1 = collegeIds.get(picNum);
                            imageBox1.setVerified("valid");
                            if(imageBox1.getImageUrl().equals(frontImageCollegeId)){
                                frontImageCollegeId = null;
                            }
                            if(imageBox1.getImageUrl().equals(backImageCollegeId)){
                                backImageCollegeId = null;
                            }
                            createCollegeIdLayout();
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
                        imageBox1 = collegeIds.get(picNum);
                        imageBox1.setVerified("invalid");
                        createCollegeIdLayout();
                        if(imageBox1.getImageUrl().equals(frontImageCollegeId)){
                            frontImageCollegeId = null;
                        }
                        if(imageBox1.getImageUrl().equals(backImageCollegeId)){
                            backImageCollegeId = null;
                        }
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
                else if(id == 2){
                    gridView = (GridView) findViewById(R.id.permanentAddressProofImages);
                    if(check.equals("front")){
                        if(frontImageAddressProof != null && backImageAddressProof != null){
                            if(picNum == 1) {
                                backImageAddressProof = null;
                                imageBox1 = addressProofs.get(0);
                                imageBox2 = addressProofs.get(1);

                                imageBox1.setPicNum(1);
                                imageBox2.setPicNum(0);

                                imageBox2.setVerified("front");
                                imageBox1.setVerified("invalid");

                                addressProofs.set(0, imageBox2);
                                addressProofs.set(1, imageBox1);

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
                                imageBox1 = addressProofs.get(0);
                                imageBox2 = addressProofs.get(picNum);

                                imageBox1.setPicNum(picNum);
                                imageBox2.setPicNum(0);

                                imageBox2.setVerified("front");
                                imageBox1.setVerified("invalid");

                                addressProofs.set(picNum, imageBox1);
                                addressProofs.set(0, imageBox2);

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
                                imageBox1 = addressProofs.get(0);
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
                                imageBox1 = addressProofs.get(0);
                                imageBox2 = addressProofs.get(picNum);

                                imageBox1.setPicNum(picNum);
                                imageBox2.setPicNum(0);

                                imageBox2.setVerified("front");
                                imageBox1.setVerified("back");

                                addressProofs.set(0, imageBox2);
                                addressProofs.set(picNum, imageBox1);

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
                                imageBox1 = addressProofs.get(0);
                                imageBox2 = addressProofs.get(picNum);
                                imageBox3 = addressProofs.get(1);

                                imageBox1.setPicNum(1);
                                imageBox2.setPicNum(0);
                                imageBox3.setPicNum(picNum);

                                imageBox2.setVerified("front");
                                imageBox1.setVerified("back");
                                if(addressProofs.get(1).getVerified().equals("valid")) {
                                    imageBox3.setVerified(addressProofs.get(1).getVerified());
                                }
                                else{
                                    imageBox3.setVerified("invalid");
                                }

                                addressProofs.set(0, imageBox2);
                                addressProofs.set(1, imageBox1);
                                addressProofs.set(picNum, imageBox3);
                                createAddressProofLayout();
                                ViewTreeObserver viewTreeObserver = gridView.getViewTreeObserver();
                                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                    @Override
                                    public void onGlobalLayout() {
                                        if(addressProofs.get(picNum).getVerified().equals("valid")) {
                                            LinearLayout gridChild2 = (LinearLayout) gridView.getChildAt(picNum);
                                            imageLayout2 = (FrameLayout) gridChild2.findViewById(R.id.imageLayout);
                                            imageLayout2.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                            imageLayout2.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                            textView = (TextView) gridChild2.findViewById(R.id.textView);
                                            textView.setVisibility(View.INVISIBLE);
                                        }
                                        else{
                                            LinearLayout gridChild2 = (LinearLayout) gridView.getChildAt(picNum);
                                            imageLayout2 = (FrameLayout) gridChild2.findViewById(R.id.imageLayout);
                                            imageLayout2.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.VISIBLE);
                                            imageLayout2.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.INVISIBLE);
                                            textView = (TextView) gridChild2.findViewById(R.id.textView);
                                            textView.setVisibility(View.INVISIBLE);
                                        }

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
                            imageBox1 = addressProofs.get(0);
                            imageBox2 = addressProofs.get(picNum);

                            imageBox1.setPicNum(picNum);
                            imageBox2.setPicNum(0);
                            if(addressProofs.get(1).getVerified().equals("valid")) {
                                imageBox1.setVerified(addressProofs.get(0).getVerified());
                            }
                            else{
                                imageBox1.setVerified("invalid");
                            }
                            imageBox2.setVerified("front");

                            addressProofs.set(picNum,imageBox1);
                            addressProofs.set(0,imageBox2);
                            createCollegeIdLayout();
                            ViewTreeObserver viewTreeObserver = gridView.getViewTreeObserver();
                            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                @Override
                                public void onGlobalLayout() {
                                    if(addressProofs.get(picNum).getVerified().equals("valid")) {
                                        LinearLayout gridChild1 = (LinearLayout) gridView.getChildAt(picNum);
                                        imageLayout1 = (FrameLayout) gridChild1.findViewById(R.id.imageLayout);
                                        imageLayout1.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                        imageLayout1.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                        textView = (TextView) gridChild1.findViewById(R.id.textView);
                                        textView.setVisibility(View.INVISIBLE);
                                    }
                                    else{
                                        LinearLayout gridChild1 = (LinearLayout) gridView.getChildAt(picNum);
                                        imageLayout1 = (FrameLayout) gridChild1.findViewById(R.id.imageLayout);
                                        imageLayout1.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.VISIBLE);
                                        imageLayout1.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.INVISIBLE);
                                        textView = (TextView) gridChild1.findViewById(R.id.textView);
                                        textView.setVisibility(View.INVISIBLE);
                                    }

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
                                imageBox1 = addressProofs.get(0);
                                imageBox2 = addressProofs.get(1);

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
                                imageBox1 = addressProofs.get(1);
                                imageBox2 = addressProofs.get(picNum);

                                imageBox1.setPicNum(picNum);
                                imageBox2.setPicNum(1);

                                imageBox1.setVerified("invalid");
                                imageBox2.setVerified("back");

                                addressProofs.set(picNum, imageBox1);
                                addressProofs.set(1,imageBox2);

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
                                imageBox1 = addressProofs.get(0);

                                imageBox1.setVerified("back");
                                frontImageAddressProof = null;
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
                                imageBox1 = addressProofs.get(1);
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
                                imageBox1 = addressProofs.get(1);
                                imageBox2 = addressProofs.get(picNum);

                                imageBox1.setPicNum(picNum);
                                imageBox2.setPicNum(1);

                                imageBox2.setVerified("back");
                                if(addressProofs.get(1).getVerified().equals("valid")) {
                                    imageBox1.setVerified(addressProofs.get(1).getVerified());
                                }
                                else{
                                    imageBox1.setVerified("invalid");
                                }

                                addressProofs.set(picNum,imageBox1);
                                addressProofs.set(1,imageBox2);

                                createAddressProofLayout();
                                ViewTreeObserver viewTreeObserver = gridView.getViewTreeObserver();
                                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                    @Override
                                    public void onGlobalLayout() {
                                        if(addressProofs.get(picNum).getVerified().equals("valid")) {
                                            LinearLayout gridChild1 = (LinearLayout) gridView.getChildAt(picNum);
                                            imageLayout1 = (FrameLayout) gridChild1.findViewById(R.id.imageLayout);
                                            imageLayout1.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                            imageLayout1.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                            textView = (TextView) gridChild1.findViewById(R.id.textView);
                                            textView.setVisibility(View.INVISIBLE);
                                        }
                                        else{
                                            LinearLayout gridChild1 = (LinearLayout) gridView.getChildAt(picNum);
                                            imageLayout1 = (FrameLayout) gridChild1.findViewById(R.id.imageLayout);
                                            imageLayout1.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.VISIBLE);
                                            imageLayout1.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.INVISIBLE);
                                            textView = (TextView) gridChild1.findViewById(R.id.textView);
                                            textView.setVisibility(View.INVISIBLE);
                                        }

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
                            imageBox1 = addressProofs.get(0);
                            imageBox2 = addressProofs.get(picNum);

                            imageBox1.setPicNum(picNum);
                            imageBox2.setPicNum(0);

                            imageBox2.setVerified("back");
                            if(addressProofs.get(1).getVerified().equals("valid")) {
                                imageBox1.setVerified(addressProofs.get(0).getVerified());
                            }
                            else{
                                imageBox1.setVerified("invalid");
                            }

                            addressProofs.set(picNum,imageBox1);
                            addressProofs.set(0,imageBox2);

                            createAddressProofLayout();
                            ViewTreeObserver viewTreeObserver = gridView.getViewTreeObserver();
                            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                @Override
                                public void onGlobalLayout() {
                                    if(addressProofs.get(picNum).getVerified().equals("valid")) {
                                        LinearLayout gridChild1 = (LinearLayout) gridView.getChildAt(picNum);
                                        imageLayout1 = (FrameLayout) gridChild1.findViewById(R.id.imageLayout);
                                        imageLayout1.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                        imageLayout1.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                        textView = (TextView) gridChild1.findViewById(R.id.textView);
                                        textView.setVisibility(View.INVISIBLE);
                                    }
                                    else{
                                        LinearLayout gridChild1 = (LinearLayout) gridView.getChildAt(picNum);
                                        imageLayout1 = (FrameLayout) gridChild1.findViewById(R.id.imageLayout);
                                        imageLayout1.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.VISIBLE);
                                        imageLayout1.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.INVISIBLE);
                                        textView = (TextView) gridChild1.findViewById(R.id.textView);
                                        textView.setVisibility(View.INVISIBLE);
                                    }

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
                    else if(check.equals("valid")){
                        imageBox1 = addressProofs.get(picNum);
                        imageBox1.setVerified("valid");
                        createAddressProofLayout();
                        if(imageBox1.getImageUrl().equals(frontImageAddressProof)){
                            frontImageAddressProof = null;
                        }
                        if(imageBox1.getImageUrl().equals(backImageAddressProof)){
                            backImageAddressProof = null;
                        }
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
                        imageBox1 = addressProofs.get(picNum);
                        imageBox1.setVerified("invalid");
                        createAddressProofLayout();
                        if(imageBox1.getImageUrl().equals(frontImageAddressProof)){
                            frontImageAddressProof = null;
                        }
                        if(imageBox1.getImageUrl().equals(backImageAddressProof)){
                            backImageAddressProof = null;
                        }
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
                else if(id == 3){
                    gridView = (GridView) findViewById(R.id.gradeSheetImages);
                    if(check.equals("valid")){
                        imageBox1 = gradeSheets.get(picNum);
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
                        imageBox1 = gradeSheets.get(picNum);
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
                        imageBox1 = bankProofs.get(picNum);
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
                        imageBox1 = bankProofs.get(picNum);
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
        collegeIds.add(imageBox);
    }

    public void insertAddressProofImageBox(ImageBox imageBox){
        addressProofs.add(imageBox);
    }

    public void insertGradeSheetImageBox(ImageBox imageBox){ gradeSheets.add(imageBox); }

    public void insertBankProofImageBox(ImageBox imageBox){ bankProofs.add(imageBox); }

    public void removeGradeSheetImageBox(){ gradeSheets.remove(gradeSheets.size()-1); }

    public void removeBankProofImageBox(){ bankProofs.remove(bankProofs.size()-1); }

    public void removeCollegeImageBox(){
        collegeIds.remove(collegeIds.size()-1);
    }

    public void removeAddressProofImageBox(){
        addressProofs.remove(addressProofs.size()-1);
    }

    public void createCollegeIdLayout(){
        ExpandableHeightGridView gridView = (ExpandableHeightGridView) findViewById(R.id.collegeIdImages);
        gridView.setAdapter(new GridViewAdapter(this,collegeIds));
    }

    public void createAddressProofLayout(){
        ExpandableHeightGridView gridView = (ExpandableHeightGridView) findViewById(R.id.permanentAddressProofImages);
        gridView.setAdapter(new GridViewAdapter(this,addressProofs));
    }

    public void createGradeSheetsLayout(){
        ExpandableHeightGridView gridView = (ExpandableHeightGridView) findViewById(R.id.gradeSheetImages);
        gridView.setAdapter(new GridViewAdapter(this,gradeSheets));
    }

    public void createBankProofsLayout(){
        ExpandableHeightGridView gridView = (ExpandableHeightGridView) findViewById(R.id.bankProofImages);
        gridView.setAdapter(new GridViewAdapter(this,bankProofs));
    }

    public String getFrontImageCollegeId() { return frontImageCollegeId; }

    public String getBackImageCollegeId() { return backImageCollegeId; }

    public String getFrontImageAddressProof() { return frontImageAddressProof; }

    public String getBackImageAddressProof() { return backImageAddressProof; }

    public String getGoodFriendsRadio() {
        return  goodFriendsRadio;
    }

    public String getYearBackRadio() { return yearBackRadio;}

    public String repayBackLoan() { return loanRepay;}

    public ArrayList<ImageBox> getCollegeIDs() {
        return collegeIds;
    }

    public ArrayList<ImageBox> getAddressProofs() { return addressProofs; }

    public ArrayList<ImageBox> getGradeSheets() { return gradeSheets; }

    public ArrayList<ImageBox> getBankProofs() { return bankProofs; }

    public String getTransparentRadio() { return transparentRadio;}

    public String getGiveLoanRadio() { return giveLoanRadio;}

    public ImageBox getCollegeIdBackImageBox() {
        return collegeIdBackImageBox;
    }

    public BorrowerData getBorrowerData() {
        return borrowerData;
    }

    public UserInfo getUserInfo() {
        return userInfo;
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

    public int getNumCollegeIds() { return collegeIds.size()-1;}

    public int getNumAddressProofs() { return addressProofs.size()-1;}

    public int getNumGradeSheets() { return gradeSheets.size()-1; }

    public int getNumBankProofs() { return bankProofs.size()-1; }

    public Button getSubmitButton() { return submitButton; }

    public RadioGroup getReferenceSelection() {
        return referenceSelection;
    }

    public RadioGroup getYearBackSelection() {return yearBackSelection; }

    public RadioGroup getTransparentSelection() {return transparentSelection; }

    public RadioGroup getInternSelection() {return internSelection; }

    public EditText getRefYear() {
        return refYear;
    }

    public EditText getRefDept() {
        return refDept;
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

    public Map<Integer, ImageBox> getAllImageBoxes() {
        return allImageBoxes;
    }

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

    public boolean getSpinnerFlag() {
        return spinnerFlag;
    }


    /**
     * <p>Callback method to be invoked when an item in this view has been
     * selected. This callback is invoked only when the newly selected
     * position is different from the previously selected position or if
     * there was no selected item.</p>
     * <p/>
     * Impelmenters can call getItemAtPosition(position) if they need to access the
     * data associated with the selected item.
     *
     * @param parent   The AdapterView where the selection happened
     * @param view     The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id       The row id of the item that is selected
     */
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
            case R.id.punctality: punctualityInClass = selectItem; spinnerFlag1 = true; break;
            case R.id.finResponsibility: financiallyResponsible = selectItem; spinnerFlag2 = true;  break;
            case R.id.sincerity: sincerityInStudies = selectItem; spinnerFlag3 = true; break;
            case R.id.coCurricular: coCurricularParticipation = selectItem; spinnerFlag4 = true; break;
            case R.id.Loan_repay: loanRepay = selectItem; spinnerFlag5 = true; break;
        }
        if(spinnerFlag1 && spinnerFlag2 && spinnerFlag3 && spinnerFlag4 && spinnerFlag5) {
            spinnerFlag = true;
        }
        else {

        }


    }

    /**
     * Callback method to be invoked when the selection disappears from this
     * view. The selection can disappear for instance when touch is activated
     * or when the adapter becomes empty.
     *
     * @param parent The AdapterView that now contains no selected item.
     */
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
            imageBox.setPicNum(collegeIds.size());
            insertCollegeImageBox(imageBox);
            imageBoxtemp.setPicType("College ID");
            imageBoxtemp.setCloudinaryId("collegeId");
            imageBoxtemp.setImageUrl(null);
            imageBoxtemp.setPicNum(collegeIds.size());
            insertCollegeImageBox(imageBoxtemp);
            createCollegeIdLayout();
        }
        else if(id == 2){
            imageBox.setPicType("Permanent Address Proof");
            imageBox.setCloudinaryId("addressProof");
            imageBox.setImageUrl(imageurl);
            imageBox.setVerified("invalid");
            removeAddressProofImageBox();
            imageBox.setPicNum(addressProofs.size());
            insertAddressProofImageBox(imageBox);
            imageBoxtemp.setPicType("Permanent Address Proof");
            imageBoxtemp.setCloudinaryId("addressProof");
            imageBoxtemp.setImageUrl(null);
            imageBoxtemp.setPicNum(addressProofs.size());
            insertAddressProofImageBox(imageBoxtemp);
            createAddressProofLayout();
        }
        else if(id == 3){
            imageBox.setPicType("Grade Sheet");
            imageBox.setCloudinaryId("gradeSheet");
            imageBox.setImageUrl(imageurl);
            imageBox.setVerified("invalid");
            removeGradeSheetImageBox();
            imageBox.setPicNum(gradeSheets.size());
            insertGradeSheetImageBox(imageBox);
            imageBoxtemp.setPicType("Grade Sheet");
            imageBoxtemp.setCloudinaryId("gradeSheet");
            imageBoxtemp.setImageUrl(null);
            imageBoxtemp.setPicNum(gradeSheets.size());
            insertGradeSheetImageBox(imageBoxtemp);
            createGradeSheetsLayout();
        }
        else if(id == 4){
            imageBox.setPicType("Bank Proof");
            imageBox.setCloudinaryId("bankProof");
            imageBox.setImageUrl(imageurl);
            imageBox.setVerified("invalid");
            removeBankProofImageBox();
            imageBox.setPicNum(bankProofs.size());
            insertBankProofImageBox(imageBox);
            imageBoxtemp.setPicType("Bank Proof");
            imageBoxtemp.setCloudinaryId("bankProof");
            imageBoxtemp.setImageUrl(null);
            imageBoxtemp.setPicNum(bankProofs.size());
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
}
