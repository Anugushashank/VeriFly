package com.example.aparna.buddy.app;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
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
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.aparna.buddy.model.VerificationInfo;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import java.io.Serializable;
import java.util.ArrayList;



public class BorrowerDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,CloudinaryAPI.UploadCallBack,Serializable {

    private BorrowerData borrowerData;
    private BorrowerDataUpdater borrowerDataUpdater;
    private BorrowerStateContainer borrowerStateContainer;
    private UploadDocModel uploadDocModel;
    private VerificationInfo verInfo;
    private Toolbar toolBar;
    private TextView phoneNum, name, college, friendPhoneNum, friendName, textView, close, mainText;
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
    private int count, i,numCollegeID, numAddressProof, numGradeSheet, numBankProof;
    private RadioButton radioButton;
    private PopupWindow popupWindow;
    RelativeLayout linearLayout,relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrower_details);

        borrowerDataUpdater = new BorrowerDataUpdater(this, "button");

        Bundle extras = getIntent().getExtras();

        borrowerData = new Gson().fromJson(extras.getString("borrowerData"), BorrowerData.class);
        uploadDocModel = borrowerData.getUploadDocModel();
        verInfo = borrowerData.getVerificationInfo();



        profilePic = (CircularImageView) findViewById(R.id.profile_image);
        name = (TextView) findViewById(R.id.name);
        phoneNum = (TextView) findViewById(R.id.phoneNum);
        phoneIcon = (ImageView) findViewById(R.id.phoneIcon);
        friendName = (TextView) findViewById(R.id.friendName);
        friendPhoneNum = (TextView) findViewById(R.id.friendPhoneNum);
        college = (TextView) findViewById(R.id.college);
        refYear = (EditText) findViewById(R.id.refYear);
        refDept = (EditText) findViewById(R.id.refDept);
        otherNotes = (EditText) findViewById(R.id.otherNotes);
        referenceSelection = (RadioGroup) findViewById(R.id.goodFriendsCheck);
        submitButton = (Button) findViewById(R.id.submitButton);
        toolBar = (Toolbar) findViewById(R.id.borrower_details_tool_bar);
        relativeLayout = (RelativeLayout) findViewById(R.id.external_layout);


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



        String userId = uploadDocModel.getFbUserId();
        Picasso.with(this)
                .load("https://graph.facebook.com/" + userId + "/picture?type=large").placeholder(R.drawable.loading_image)
                .into(profilePic);

        name.setText(uploadDocModel.getName());

        college.setTextSize(18 - (uploadDocModel.getCollege().length()) / 8);
        college.setText(uploadDocModel.getCollege());

        linearLayout = (RelativeLayout) findViewById(R.id.rootLayoutMain);



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



        collegeID = new ArrayList<>();
        count = 0 ; numCollegeID = 0;
        if(uploadDocModel.getCollegeID() != null) {
            if (uploadDocModel.getCollegeID().getFront() != null) {
                ImageBox ib = new ImageBox(imageLayout, BorrowerDetailsActivity.this);
                ib.setImageUrl(uploadDocModel.getCollegeID().getFront().getImgUrl());
                frontImageCollegeId = ib.getImageUrl();
                ib.setPicNum(count++);
                ib.setPicType("College ID");
                ib.setCloudinaryId("collegeId");
                ib.setId(1);
                ib.setMatch("front");
                ib.setIsVerified(uploadDocModel.getCollegeID().getFront().getIsVerified());
                numCollegeID++;
                insertCollegeImageBox(ib);
            }
            if (uploadDocModel.getCollegeID().getBack() != null) {
                ImageBox ib = new ImageBox(imageLayout, BorrowerDetailsActivity.this);
                ib.setImageUrl(uploadDocModel.getCollegeID().getBack().getImgUrl());
                backImageCollegeId = ib.getImageUrl();
                ib.setPicNum(count++);
                ib.setPicType("College ID");
                ib.setCloudinaryId("collegeId");
                ib.setId(1);
                ib.setMatch("back");
                ib.setIsVerified(uploadDocModel.getCollegeID().getBack().getIsVerified());
                numCollegeID++;
                insertCollegeImageBox(ib);
            }
            if (uploadDocModel.getCollegeID().getFront() == null || uploadDocModel.getCollegeID().getBack() == null) {
                if (uploadDocModel.getCollegeID().getImgUrls() != null) {
                    for (i = 0; i < uploadDocModel.getCollegeID().getImgUrls().size(); i++) {
                        ImageBox ib = new ImageBox(imageLayout, BorrowerDetailsActivity.this);
                        ib.setImageUrl(uploadDocModel.getCollegeID().getImgUrls().get(i));
                        ib.setPicNum(i+count);
                        ib.setPicType("College ID");
                        ib.setCloudinaryId("collegeId");
                        ib.setId(1);
                        ib.setMatch("img");
                        ib.setIsVerified(false);
                        numCollegeID++;
                        insertCollegeImageBox(ib);
                    }
                    count+=i;
                }
                ibx = new ImageBox(imageLayout, BorrowerDetailsActivity.this);
                ibx.setImageUrl(null);
                ibx.setPicNum(count++);
                ibx.setPicType("College ID");
                ibx.setCloudinaryId("collegeId");
                ibx.setId(1);
                ibx.setMatch("plus");
                ibx.setIsVerified(false);
                insertCollegeImageBox(ibx);
            }
        }
        else {
            ibx = new ImageBox(imageLayout, BorrowerDetailsActivity.this);
            ibx.setImageUrl(null);
            ibx.setPicNum(count++);
            ibx.setPicType("College ID");
            ibx.setCloudinaryId("collegeId");
            ibx.setId(1);
            ibx.setMatch("plus");
            ibx.setIsVerified(false);
            insertCollegeImageBox(ibx);
        }

        createCollegeIdLayout();




        addressProof = new ArrayList<>();
        count = 0; numAddressProof = 0;
        if(uploadDocModel.getAddressProof() != null) {
            if (uploadDocModel.getAddressProof().getFront() != null) {
                ImageBox ib = new ImageBox(imageLayout, BorrowerDetailsActivity.this);
                ib.setImageUrl(uploadDocModel.getAddressProof().getFront().getImgUrl());
                frontImageAddressProof = ib.getImageUrl();
                ib.setPicNum(count++);
                ib.setPicType("Permanent Address Proof");
                ib.setCloudinaryId("addressProof");
                ib.setId(2);
                ib.setMatch("front");
                ib.setIsVerified(uploadDocModel.getAddressProof().getFront().getIsVerified());
                numAddressProof++;
                insertAddressProofImageBox(ib);
            }
            if (uploadDocModel.getAddressProof().getBack() != null) {
                ImageBox ib = new ImageBox(imageLayout, BorrowerDetailsActivity.this);
                ib.setImageUrl(uploadDocModel.getAddressProof().getBack().getImgUrl());
                backImageAddressProof = ib.getImageUrl();
                ib.setPicNum(count++);
                ib.setPicType("Permanent Address Proof");
                ib.setCloudinaryId("addressProof");
                ib.setId(2);
                ib.setMatch("back");
                ib.setIsVerified(uploadDocModel.getAddressProof().getBack().getIsVerified());
                numAddressProof++;
                insertAddressProofImageBox(ib);
            }
            if (uploadDocModel.getAddressProof().getFront() == null || uploadDocModel.getAddressProof().getBack() == null) {
                if (uploadDocModel.getAddressProof().getImgUrls() != null) {
                    for (i = 0; i < uploadDocModel.getAddressProof().getImgUrls().size() ; i++) {
                        ImageBox ib = new ImageBox(imageLayout, BorrowerDetailsActivity.this);
                        ib.setImageUrl(uploadDocModel.getAddressProof().getImgUrls().get(i));
                        ib.setPicNum(i + count);
                        ib.setPicType("Permanent Address Proof");
                        ib.setCloudinaryId("addressProof");
                        ib.setId(2);
                        ib.setMatch("img");
                        ib.setIsVerified(false);
                        numAddressProof++;
                        insertAddressProofImageBox(ib);
                    }
                    count+=i;
                }
                ibx = new ImageBox(imageLayout,BorrowerDetailsActivity.this);
                ibx.setImageUrl(null);
                ibx.setPicNum(count++);
                ibx.setPicType("Permanent Address Proof");
                ibx.setCloudinaryId("addressProof");
                ibx.setId(2);
                ibx.setMatch("plus");
                ibx.setIsVerified(false);
                insertAddressProofImageBox(ibx);
            }
        }
        else {
            ibx = new ImageBox(imageLayout, BorrowerDetailsActivity.this);
            ibx.setImageUrl(null);
            ibx.setPicNum(count++);
            ibx.setPicType("Permanent Address Proof");
            ibx.setCloudinaryId("addressProof");
            ibx.setId(2);
            ibx.setMatch("plus");
            ibx.setIsVerified(false);
            insertAddressProofImageBox(ibx);
        }

        createAddressProofLayout();




        gradeSheet = new ArrayList<>();
        count = 0;
        if(uploadDocModel.getGradeSheet() != null) {
            if(uploadDocModel.getGradeSheet().getValidImgUrls() != null){
                for (i = 0; i < uploadDocModel.getGradeSheet().getValidImgUrls().size(); i++) {
                    ImageBox ib = new ImageBox(imageLayout, BorrowerDetailsActivity.this);
                    ib.setImageUrl(uploadDocModel.getGradeSheet().getValidImgUrls().get(i));
                    ib.setPicNum(i + count);
                    ib.setPicType("Grade Sheet");
                    ib.setCloudinaryId("gradeSheet");
                    ib.setId(3);
                    ib.setMatch("valid");
                    ib.setIsVerified(true);
                    numGradeSheet++;
                    insertGradeSheetImageBox(ib);
                }
                count+=i;
            }
            if(uploadDocModel.getGradeSheet().getInvalidImgUrls() != null){
                for (i = 0; i < uploadDocModel.getGradeSheet().getInvalidImgUrls().size(); i++) {
                    ImageBox ib = new ImageBox(imageLayout, BorrowerDetailsActivity.this);
                    ib.setImageUrl(uploadDocModel.getGradeSheet().getInvalidImgUrls().get(i));
                    ib.setPicNum(i+count);
                    ib.setPicType("Grade Sheet");
                    ib.setCloudinaryId("gradeSheet");
                    ib.setId(3);
                    ib.setMatch("invalid");
                    ib.setIsVerified(true);
                    numGradeSheet++;
                    insertGradeSheetImageBox(ib);
                }
                count+=i;
            }
            if (uploadDocModel.getGradeSheet().getImgUrls() != null) {
                for (i = 0; i < uploadDocModel.getGradeSheet().getImgUrls().size(); i++) {
                    ImageBox ib = new ImageBox(imageLayout, BorrowerDetailsActivity.this);
                    ib.setImageUrl(uploadDocModel.getGradeSheet().getImgUrls().get(i));
                    ib.setPicNum(i+count);
                    ib.setPicType("Grade Sheet");
                    ib.setCloudinaryId("gradeSheet");
                    ib.setId(3);
                    ib.setMatch("img");
                    ib.setIsVerified(false);
                    numGradeSheet++;
                    insertGradeSheetImageBox(ib);
                }
                count+=i;
            }
        }
        ibx = new ImageBox(imageLayout,BorrowerDetailsActivity.this);
        ibx.setImageUrl(null);
        ibx.setPicNum(count++);
        ibx.setPicType("Grade Sheet");
        ibx.setCloudinaryId("gradeSheet");
        ibx.setId(3);
        ibx.setMatch("plus");
        ibx.setIsVerified(false);
        insertGradeSheetImageBox(ibx);

        createGradeSheetsLayout();




        bankProof = new ArrayList<>();
        count = 0;
        if(uploadDocModel.getBankProof() != null) {
            if (uploadDocModel.getBankProof().getValidImgUrls() != null) {
                for (i = 0; i < uploadDocModel.getBankProof().getValidImgUrls().size(); i++) {
                    ImageBox ib = new ImageBox(imageLayout, BorrowerDetailsActivity.this);
                    ib.setImageUrl(uploadDocModel.getBankProof().getValidImgUrls().get(i));
                    ib.setPicNum(count+i);
                    ib.setPicType("Bank Proof");
                    ib.setCloudinaryId("bankProof");
                    ib.setId(4);
                    ib.setMatch("valid");
                    ib.setIsVerified(true);
                    numBankProof++;
                    insertBankProofImageBox(ib);
                }
                count+=i;
            }
            if (uploadDocModel.getBankProof().getInvalidImgUrls() != null) {
                for (i = 0; i < uploadDocModel.getBankProof().getInvalidImgUrls().size(); i++) {
                    ImageBox ib = new ImageBox(imageLayout, BorrowerDetailsActivity.this);
                    ib.setImageUrl(uploadDocModel.getBankProof().getInvalidImgUrls().get(i));
                    ib.setPicNum(count+i);
                    ib.setPicType("Bank Proof");
                    ib.setCloudinaryId("bankProof");
                    ib.setId(4);
                    ib.setMatch("invalid");
                    ib.setIsVerified(true);
                    numBankProof++;
                    insertBankProofImageBox(ib);
                }
                count+=i;
            }
            if (uploadDocModel.getBankProof().getImgUrls() != null) {
                for (i = 0; i < uploadDocModel.getBankProof().getImgUrls().size(); i++) {
                    ImageBox ib = new ImageBox(imageLayout, BorrowerDetailsActivity.this);
                    ib.setImageUrl(uploadDocModel.getBankProof().getImgUrls().get(i));
                    ib.setPicNum(count+i);
                    ib.setPicType("Bank Proof");
                    ib.setCloudinaryId("bankProof");
                    ib.setId(4);
                    ib.setMatch("img");
                    ib.setIsVerified(false);
                    numBankProof++;
                    insertBankProofImageBox(ib);
                }
                count+=i;
            }
        }
        ibx = new ImageBox(imageLayout,BorrowerDetailsActivity.this);
        ibx.setImageUrl(null);
        ibx.setPicNum(count++);
        ibx.setPicType("Bank Proof");
        ibx.setCloudinaryId("bankProof");
        ibx.setId(4);
        ibx.setMatch("plus");
        ibx.setIsVerified(false);
        insertBankProofImageBox(ibx);

        createBankProofsLayout();




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
                radioButton = (RadioButton) referenceSelection.getChildAt(1);
                radioButton.setChecked(true);
                goodFriendsRadio = "true";
            } else {
                radioButton = (RadioButton) referenceSelection.getChildAt(0);
                radioButton.setChecked(true);
                goodFriendsRadio = "false";
            }
        }
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


        if (verInfo.getReferenceYear() != null) {
            refYear.setText(verInfo.getReferenceYear());

        }

        if (verInfo.getReferenceDepartment() != null) {
            refDept.setText(verInfo.getReferenceDepartment());
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
        if(verInfo.getHaveYearBack() != null) {
            if (verInfo.getHaveYearBack()) {
                radioButton = (RadioButton) yearBackSelection.getChildAt(1);
                radioButton.setChecked(true);
                yearBackRadio = "true";
            } else {
                radioButton = (RadioButton) yearBackSelection.getChildAt(0);
                radioButton.setChecked(true);
                yearBackRadio = "false";
            }
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
        if(verInfo.getBorrowerIsLying() != null) {
            if (verInfo.getBorrowerIsLying()) {
                radioButton = (RadioButton) transparentSelection.getChildAt(1);
                radioButton.setChecked(true);
                transparentRadio = "true";
            } else {
                radioButton = (RadioButton) transparentSelection.getChildAt(0);
                radioButton.setChecked(true);
                transparentRadio = "false";
            }
        }





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
        if(verInfo.getGiveHimLoan() != null) {
            if (verInfo.getGiveHimLoan()) {
                radioButton = (RadioButton) internSelection.getChildAt(1);
                radioButton.setChecked(true);
                giveLoanRadio = "true";
            } else {
                radioButton = (RadioButton) internSelection.getChildAt(0);
                radioButton.setChecked(true);
                giveLoanRadio = "false";
            }
        }



        if (verInfo.getFinalVerificationNotes() != null) {
            otherNotes.setText(verInfo.getFinalVerificationNotes());
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
                        Log.i("dsjfhjdsfds",photoPath);
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

                if(id == 1){

                    if(check.equals("front")){
                        if(frontImageCollegeId != null && backImageCollegeId != null){
                            if(picNum == 1) {
                                backImageCollegeId = null;
                                imageBox1 = collegeID.get(0);
                                imageBox2 = collegeID.get(1);
                                imageBox1.setPicNum(1);
                                imageBox2.setPicNum(0);
                                imageBox2.setMatch("front");
                                imageBox2.setIsVerified(true);
                                imageBox1.setMatch("invalid");
                                imageBox1.setIsVerified(false);
                                collegeID.set(0, imageBox2);
                                collegeID.set(1, imageBox1);
                            }
                            else if(picNum == 0) {
                                imageBox1 = collegeID.get(0);
                                imageBox1.setIsVerified(true);
                            }
                            else{
                                imageBox1 = collegeID.get(0);
                                imageBox2 = collegeID.get(picNum);
                                imageBox1.setPicNum(picNum);
                                imageBox2.setPicNum(0);
                                imageBox2.setMatch("front");
                                imageBox2.setIsVerified(true);
                                imageBox1.setMatch("invalid");
                                imageBox1.setIsVerified(false);
                                collegeID.set(picNum, imageBox1);
                                collegeID.set(0, imageBox2);
                            }
                        }
                        else if(backImageCollegeId != null ) {
                            if (picNum == 0) {
                                imageBox1 = collegeID.get(0);
                                imageBox1.setMatch("front");
                                imageBox1.setIsVerified(true);
                                backImageCollegeId = null;
                            } else if (picNum == 1) {
                                imageBox1 = collegeID.get(0);
                                imageBox2 = collegeID.get(picNum);
                                imageBox1.setPicNum(picNum);
                                imageBox2.setPicNum(0);
                                imageBox2.setMatch("front");
                                imageBox2.setIsVerified(true);
                                imageBox1.setMatch("back");
                                imageBox1.setIsVerified(true);
                                collegeID.set(0, imageBox2);
                                collegeID.set(picNum, imageBox1);
                            } else {
                                imageBox1 = collegeID.get(0);
                                imageBox2 = collegeID.get(picNum);
                                imageBox3 = collegeID.get(1);
                                imageBox1.setPicNum(1);
                                imageBox2.setPicNum(0);
                                imageBox3.setPicNum(picNum);
                                imageBox2.setMatch("front");
                                imageBox2.setIsVerified(true);
                                imageBox1.setMatch("back");
                                imageBox1.setIsVerified(false);
                                imageBox3.setMatch("invalid");
                                imageBox3.setIsVerified(false);
                                collegeID.set(0, imageBox2);
                                collegeID.set(1, imageBox1);
                                collegeID.set(picNum, imageBox3);
                            }
                        }
                        else{
                            imageBox1 = collegeID.get(0);
                            imageBox2 = collegeID.get(picNum);
                            imageBox1.setPicNum(picNum);
                            imageBox2.setPicNum(0);
                            imageBox1.setMatch("invalid");
                            imageBox1.setIsVerified(false);
                            imageBox2.setMatch("front");
                            imageBox2.setIsVerified(true);
                            collegeID.set(picNum,imageBox1);
                            collegeID.set(0,imageBox2);
                        }
                        frontImageCollegeId = imageUrl;
                    }
                    else if(check.equals("back")){

                        if( frontImageCollegeId != null && backImageCollegeId != null) {
                            if(picNum == 0) {
                                imageBox1 = collegeID.get(0);
                                imageBox2 = collegeID.get(1);
                                imageBox1.setMatch("back");
                                imageBox1.setIsVerified(true);
                                imageBox2.setMatch("invalid");
                                imageBox2.setIsVerified(false);
                                frontImageCollegeId = null;
                            }
                            else if(picNum == 1){
                                imageBox1 = collegeID.get(1);
                                imageBox1.setIsVerified(true);
                            }
                            else{
                                imageBox1 = collegeID.get(1);
                                imageBox2 = collegeID.get(picNum);
                                imageBox1.setPicNum(picNum);
                                imageBox2.setPicNum(1);
                                imageBox1.setMatch("invalid");
                                imageBox1.setIsVerified(false);
                                imageBox2.setMatch("back");
                                imageBox2.setIsVerified(true);
                                collegeID.set(picNum, imageBox1);
                                collegeID.set(1,imageBox2);
                            }
                        }
                        else if(frontImageCollegeId != null ){
                            if(picNum == 0){
                                imageBox1 = collegeID.get(0);
                                imageBox1.setMatch("back");
                                imageBox1.setIsVerified(true);
                                frontImageCollegeId = null;
                            }
                            else if(picNum == 1){
                                imageBox1 = collegeID.get(1);
                                imageBox1.setMatch("back");
                                imageBox1.setIsVerified(true);
                            }
                            else{
                                imageBox1 = collegeID.get(1);
                                imageBox2 = collegeID.get(picNum);
                                imageBox1.setPicNum(picNum);
                                imageBox2.setPicNum(1);
                                imageBox2.setMatch("back");
                                imageBox2.setIsVerified(true);
                                imageBox1.setMatch("invalid");
                                imageBox1.setIsVerified(false);
                                collegeID.set(picNum,imageBox1);
                                collegeID.set(1,imageBox2);
                            }
                        }
                        else{
                            imageBox1 = collegeID.get(0);
                            imageBox2 = collegeID.get(picNum);
                            imageBox1.setPicNum(picNum);
                            imageBox2.setPicNum(0);
                            imageBox2.setMatch("back");
                            imageBox2.setIsVerified(true);
                            imageBox1.setMatch("invalid");
                            imageBox1.setIsVerified(false);
                            collegeID.set(picNum,imageBox1);
                            collegeID.set(0,imageBox2);
                        }
                        backImageCollegeId = imageUrl;
                    }
                    else if(check.equals("invalid")){
                        if( frontImageCollegeId != null && backImageCollegeId != null) {
                            if(picNum == 0) {
                                imageBox1 = collegeID.get(0);
                                imageBox2 = collegeID.get(1);
                                imageBox1.setMatch("invalid");
                                imageBox1.setIsVerified(true);
                                collegeID.set(0,imageBox2);
                                collegeID.set(1,imageBox1);
                                frontImageCollegeId = null;
                            }
                            else if(picNum == 1){
                                imageBox1 = collegeID.get(1);
                                imageBox1.setMatch("invalid");
                                imageBox1.setIsVerified(true);
                                backImageCollegeId = null;
                            }
                            else{
                                imageBox1 = collegeID.get(picNum);
                                imageBox1.setMatch("invalid");
                                imageBox1.setIsVerified(true);
                            }
                        }
                        else if(frontImageCollegeId != null ){
                            if(picNum == 0){
                                imageBox1 = collegeID.get(0);
                                imageBox1.setMatch("invalid");
                                imageBox1.setIsVerified(true);
                                frontImageCollegeId = null;
                            }
                            else {
                                imageBox1 = collegeID.get(picNum);
                                imageBox1.setMatch("invalid");
                                imageBox1.setIsVerified(true);
                            }

                        }
                        else if(backImageCollegeId != null){
                            if(picNum == 0){
                                imageBox1 = collegeID.get(0);
                                imageBox1.setMatch("invalid");
                                imageBox1.setIsVerified(true);
                                backImageCollegeId = null;
                            }
                            else {
                                imageBox1 = collegeID.get(picNum);
                                imageBox1.setMatch("invalid");
                                imageBox1.setIsVerified(true);
                            }
                        }
                        else{
                            imageBox2 = collegeID.get(picNum);
                            imageBox2.setMatch("invalid");
                            imageBox2.setIsVerified(true);
                        }
                    }
                    createCollegeIdLayout();
                }
                else if(id == 6){

                    if(check.equals("front")){
                        if(frontImageAddressProof != null && backImageAddressProof != null){
                            if(picNum == 1) {
                                backImageAddressProof = null;
                                imageBox1 = addressProof.get(0);
                                imageBox2 = addressProof.get(1);
                                imageBox1.setPicNum(1);
                                imageBox2.setPicNum(0);
                                imageBox2.setMatch("front");
                                imageBox1.setMatch("invalid");
                                addressProof.set(0, imageBox2);
                                addressProof.set(1, imageBox1);
                            }
                            else if(picNum != 0){
                                imageBox1 = addressProof.get(0);
                                imageBox2 = addressProof.get(picNum);
                                imageBox1.setPicNum(picNum);
                                imageBox2.setPicNum(0);
                                imageBox2.setMatch("front");
                                imageBox1.setMatch("invalid");
                                addressProof.set(picNum, imageBox1);
                                addressProof.set(0, imageBox2);
                            }
                        }
                        else if(backImageAddressProof != null ) {
                            if (picNum == 0) {
                                imageBox1 = addressProof.get(0);
                                imageBox1.setMatch("front");
                                backImageAddressProof = null;
                            } else if (picNum == 1) {
                                imageBox1 = addressProof.get(0);
                                imageBox2 = addressProof.get(picNum);
                                imageBox1.setPicNum(picNum);
                                imageBox2.setPicNum(0);
                                imageBox2.setMatch("front");
                                imageBox1.setMatch("back");
                                addressProof.set(0, imageBox2);
                                addressProof.set(picNum, imageBox1);
                            }
                            else {
                                imageBox1 = addressProof.get(0);
                                imageBox2 = addressProof.get(picNum);
                                imageBox3 = addressProof.get(1);
                                imageBox1.setPicNum(1);
                                imageBox2.setPicNum(0);
                                imageBox3.setPicNum(picNum);
                                imageBox2.setMatch("front");
                                imageBox1.setMatch("back");
                                imageBox3.setMatch("invalid");
                                addressProof.set(0, imageBox2);
                                addressProof.set(1, imageBox1);
                                addressProof.set(picNum, imageBox3);
                            }
                        }
                        else{
                            imageBox1 = addressProof.get(0);
                            imageBox2 = addressProof.get(picNum);
                            imageBox1.setPicNum(picNum);
                            imageBox2.setPicNum(0);
                            imageBox1.setMatch("invalid");
                            imageBox2.setMatch("front");
                            addressProof.set(picNum,imageBox1);
                            addressProof.set(0,imageBox2);
                        }
                        frontImageAddressProof = imageUrl;
                    }
                    else if(check.equals("back")){

                        if( frontImageAddressProof != null && backImageAddressProof != null) {
                            if(picNum == 0) {
                                imageBox1 = addressProof.get(0);
                                imageBox2 = addressProof.get(1);
                                imageBox1.setMatch("back");
                                imageBox2.setMatch("invalid");
                                frontImageCollegeId = null;
                            }
                            else if(picNum != 1){
                                imageBox1 = addressProof.get(1);
                                imageBox2 = addressProof.get(picNum);
                                imageBox1.setPicNum(picNum);
                                imageBox2.setPicNum(1);
                                imageBox1.setMatch("invalid");
                                imageBox2.setMatch("back");
                                addressProof.set(picNum, imageBox1);
                                addressProof.set(1,imageBox2);
                            }
                        }
                        else if(frontImageAddressProof != null ){
                            if(picNum == 0){
                                imageBox1 = addressProof.get(0);
                                imageBox1.setMatch("back");
                                frontImageAddressProof = null;
                            }
                            else if(picNum == 1){
                                imageBox1 = addressProof.get(1);
                                imageBox1.setMatch("back");
                            }
                            else{
                                imageBox1 = addressProof.get(1);
                                imageBox2 = addressProof.get(picNum);
                                imageBox1.setPicNum(picNum);
                                imageBox2.setPicNum(1);
                                imageBox2.setMatch("back");
                                imageBox1.setMatch("invalid");
                                addressProof.set(picNum,imageBox1);
                                addressProof.set(1,imageBox2);
                            }
                        }
                        else{
                            imageBox1 = addressProof.get(0);
                            imageBox2 = addressProof.get(picNum);
                            imageBox1.setPicNum(picNum);
                            imageBox2.setPicNum(0);
                            imageBox2.setMatch("back");
                            imageBox1.setMatch("invalid");
                            addressProof.set(picNum,imageBox1);
                            addressProof.set(0,imageBox2);
                        }
                        backImageAddressProof = imageUrl;
                    }
                    createAddressProofLayout();
                }
                else if(id == 3){
                    if(check.equals("valid")){
                        imageBox1 = gradeSheet.get(picNum);
                        imageBox1.setMatch("valid");
                        imageBox1.setIsVerified(true);
                    }
                    else if(check.equals("invalid")) {
                        imageBox1 = gradeSheet.get(picNum);
                        imageBox1.setMatch("invalid");
                        imageBox1.setIsVerified(true);
                    }
                    createGradeSheetsLayout();
                }
                else if(id == 4){
                    if(check.equals("valid")){
                        imageBox1 = bankProof.get(picNum);
                        imageBox1.setMatch("valid");
                        imageBox1.setIsVerified(true);
                    }
                    else if(check.equals("invalid")) {
                        imageBox1 = bankProof.get(picNum);
                        imageBox1.setMatch("invalid");
                        imageBox1.setIsVerified(true);
                    }
                    createBankProofsLayout();
                }

            }
        }
    }

    public void confirmSubmit(View view) {
        borrowerStateContainer = new BorrowerStateContainer(BorrowerDetailsActivity.this);
        if(borrowerStateContainer.isCompleted()) {
            borrowerDataUpdater = new BorrowerDataUpdater(this, "button");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure the details entered are correct?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            borrowerDataUpdater.updateToApi();
                        }


                    })
                    .setNegativeButton("No", null)                        //Do nothing on no
                    .show();
        }
        else{
            String text = "Fill all the details";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(this, text, duration);
            toast.show();
        }
    }

    private void popupWindow() {
        try {
            LayoutInflater inflater = (LayoutInflater) BorrowerDetailsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.popup_layout, (ViewGroup) findViewById(R.id.popup_element));
            popupWindow = new PopupWindow(layout);
            popupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
            popupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
            popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

            close = (TextView) layout.findViewById(R.id.close);
            mainText = (TextView) layout.findViewById(R.id.mainText);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    relativeLayout.setVisibility(View.INVISIBLE);
                }
            });

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    relativeLayout.setVisibility(View.INVISIBLE);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void popUp(View view){
        popupWindow();
        relativeLayout.setVisibility(View.VISIBLE);
        if(view.getId() == R.id.addressID){
            mainText.setText(getResources().getString(R.string.address_popup));
        }
        else if(view.getId() == R.id.collegeID){
            mainText.setText(getResources().getString(R.string.college_popup));
        }
        else if(view.getId() == R.id.gradeID){
            mainText.setText(getResources().getString(R.string.grade_popup));
        }
        else if(view.getId() == R.id.bankID){
            mainText.setText(getResources().getString(R.string.bank_popup));
        }
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

    public String getScheduleDate() { return borrowerData.getScheduleDate(); }

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

    public TextView getName() {
        return name;
    }

    public int getNumCollegeIds() { return numCollegeID; }

    public int getNumAddressProofs() { return numAddressProof; }

    public int getNumGradeSheets() { return numGradeSheet; }

    public int getNumBankProofs() { return numBankProof; }

    public String getRefYear() {
        return refYear.getText().toString();
    }

    public String getRefDept() {
        return refDept.getText().toString();
    }

    public String getOtherNotes() { return otherNotes.getText().toString();}

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

    public String getSpinnerLoanRepay() { return loanRepay; }



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
            imageBox.setMatch("img");
            imageBox.setIsVerified(false);
            numCollegeID++;
            removeCollegeImageBox();
            imageBox.setPicNum(collegeID.size());
            insertCollegeImageBox(imageBox);
            imageBoxtemp.setPicType("College ID");
            imageBoxtemp.setCloudinaryId("collegeId");
            imageBoxtemp.setImageUrl(null);
            imageBoxtemp.setMatch("plus");
            imageBoxtemp.setIsVerified(false);
            imageBoxtemp.setPicNum(collegeID.size());
            insertCollegeImageBox(imageBoxtemp);
            createCollegeIdLayout();
        }
        else if(id == 2){
            imageBox.setPicType("Permanent Address Proof");
            imageBox.setCloudinaryId("addressProof");
            imageBox.setImageUrl(imageurl);
            imageBox.setMatch("img");
            imageBox.setIsVerified(false);
            numAddressProof++;
            removeAddressProofImageBox();
            imageBox.setPicNum(addressProof.size());
            insertAddressProofImageBox(imageBox);
            imageBoxtemp.setPicType("Permanent Address Proof");
            imageBoxtemp.setCloudinaryId("addressProof");
            imageBoxtemp.setImageUrl(null);
            imageBoxtemp.setMatch("plus");
            imageBoxtemp.setIsVerified(false);
            imageBoxtemp.setPicNum(addressProof.size());
            insertAddressProofImageBox(imageBoxtemp);
            createAddressProofLayout();
        }
        else if(id == 3){
            imageBox.setPicType("Grade Sheet");
            imageBox.setCloudinaryId("gradeSheet");
            imageBox.setImageUrl(imageurl);
            imageBox.setMatch("img");
            imageBox.setIsVerified(false);
            numGradeSheet++;
            removeGradeSheetImageBox();
            imageBox.setPicNum(gradeSheet.size());
            insertGradeSheetImageBox(imageBox);
            imageBoxtemp.setPicType("Grade Sheet");
            imageBoxtemp.setCloudinaryId("gradeSheet");
            imageBoxtemp.setImageUrl(null);
            imageBoxtemp.setMatch("plus");
            imageBoxtemp.setIsVerified(false);
            imageBoxtemp.setPicNum(gradeSheet.size());
            insertGradeSheetImageBox(imageBoxtemp);
            createGradeSheetsLayout();
        }
        else if(id == 4){
            imageBox.setPicType("Bank Proof");
            imageBox.setCloudinaryId("bankProof");
            imageBox.setImageUrl(imageurl);
            imageBox.setMatch("img");
            imageBox.setIsVerified(false);
            numBankProof++;
            removeBankProofImageBox();
            imageBox.setPicNum(bankProof.size());
            insertBankProofImageBox(imageBox);
            imageBoxtemp.setPicType("Bank Proof");
            imageBoxtemp.setCloudinaryId("bankProof");
            imageBoxtemp.setImageUrl(null);
            imageBoxtemp.setMatch("plus");
            imageBoxtemp.setIsVerified(false);
            imageBoxtemp.setPicNum(bankProof.size());
            insertBankProofImageBox(imageBoxtemp);
            createBankProofsLayout();
        }
    }


    @Override
    public void onPause(){
        super.onPause();
        borrowerDataUpdater = new BorrowerDataUpdater(this, "home");
        borrowerDataUpdater.updateToApi();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        if(requestCode == 1
                ){
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
