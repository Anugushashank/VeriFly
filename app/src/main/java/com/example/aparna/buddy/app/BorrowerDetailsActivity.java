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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BorrowerDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,CloudinaryAPI.UploadCallBack {

    private BorrowerData borrowerData;
    private BorrowerStateContainer state;
    private UserInfo userInfo;
    private VerificationInfo verInfo;
    private Toolbar toolBar;
    private TextView phoneNum, name, college;
    private RadioGroup referenceSelection, yearBackSelection, transparentSelection, internSelection;
    private EditText refYear, refDept, otherNotes;
    private ImageView phoneIcon, IdIcon, IdIconPlusFront, IdIconPlusBack;
    private FrameLayout collegeIdFrontLayout, collegeIdBackLayout, addressProofFrontLayout, addressProofBackLayout;
    private ImageBox collegeIdFrontImageBox, collegeIdBackImageBox, addressProofFrontImageBox, addressProofBackImageBox;
    private FrameLayout imageLayout, imageLayout1;
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
    private ArrayList<ImageBox> collegeIds, addressProofs;
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
        borrowerDataUpdater = new BorrowerDataUpdater(this);

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
                insertCollegeImageBox(ib);
            }
            ImageBox ib = new ImageBox(imageLayout,BorrowerDetailsActivity.this);
            ib.setImageUrl(null);
            ib.setPicNum(count);
            ib.setPicType("College ID");
            ib.setCloudinaryId("collegeId");
            ib.setId(1);
            insertCollegeImageBox(ib);
        }
        else{
            ImageBox ib = new ImageBox(imageLayout,BorrowerDetailsActivity.this);
            ib.setImageUrl(null);
            ib.setPicNum(0);
            ib.setPicType("College ID");
            ib.setCloudinaryId("collegeId");
            ib.setId(1);
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
                insertAddressProofImageBox(ib);
            }
            ImageBox ib = new ImageBox(imageLayout, BorrowerDetailsActivity.this);
            ib.setImageUrl(null);
            ib.setPicNum(count);
            ib.setPicType("Permanent Address Proof");
            ib.setCloudinaryId("addressProof");
            ib.setId(2);
            insertAddressProofImageBox(ib);
        }
        else{
            ImageBox ib = new ImageBox(imageLayout, BorrowerDetailsActivity.this);
            ib.setImageUrl(null);
            ib.setPicNum(0);
            ib.setPicType("Permanent Address Proof");
            ib.setCloudinaryId("addressProof");
            ib.setId(2);
            insertAddressProofImageBox(ib);
        }

        createAddressProofLayout();


        profilePic = (CircularImageView) findViewById(R.id.profile_image);
        name = (TextView) findViewById(R.id.name);
        phoneNum = (TextView) findViewById(R.id.phoneNum);
        phoneIcon = (ImageView) findViewById(R.id.phoneIcon);
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

        if (verInfo.isReferenceIsGoodFriend()) {
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

        if(verInfo.isHaveYearBack() != null) {
            if (verInfo.isHaveYearBack()) {
                yearBackSelection.check(R.id.yearBackYes);
            } else {
                yearBackSelection.check(R.id.yearBackNo);
            }
        }

        if(verInfo.isGiveHimLoan() != null) {
            if (verInfo.isGiveHimLoan()) {
                internSelection.check(R.id.internchoiceYes);
            } else {
                internSelection.check(R.id.internchoiceNo);
            }
        }

        if(verInfo.isBorrowerIsLying() != null) {
            if (verInfo.isBorrowerIsLying()) {
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
            Log.d("global","shizzspinnerfdgfgdfgfdgdfdfgfdg");
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
        ImageBox imageBox;
        CloudinaryAPI cloudinary = new CloudinaryAPI(BorrowerDetailsActivity.this);
        Bitmap bp;
        Uri uri;
        if(resultCode == RESULT_OK){
            if(requestCode == 1){
                // If image box is null, then go check for imagebox in extended list
                // This new pic clicked imagebox should go to state container

                bp = (Bitmap) data.getExtras().get("data");
                Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{ MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.ImageColumns.ORIENTATION }, MediaStore.Images.Media.DATE_ADDED, null, "date_added ASC");
                imageBox = new ImageBox(imageLayout,BorrowerDetailsActivity.this);
                if(cursor != null && cursor.moveToFirst()) {
                    do {
                        uri = Uri.parse(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                        photoPath = uri.toString();
                    } while (cursor.moveToNext());
                    cursor.close();
                }
                imageBox.setLocalPhotoPath(photoPath);
                cloudinary.uploadImage(bp, imageBox, borrowerData,this);

            } else if (requestCode == 2) {
                // If image box is null, then go check for imagebox in extended list
                // This new pic clicked imagebox should go to state container
                imageBox = new ImageBox(this.getImageLayout(),BorrowerDetailsActivity.this);

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

                imageBox.getRoundedImageView().setImageBitmap(bp);
                imageBox.setLocalPhotoPath(photoPath);
                imageBox.getPlusIcon().setVisibility(View.INVISIBLE);
                imageBox.setVerified();
                cloudinary.uploadImage(bp, imageBox, borrowerData,this);

            } else if (requestCode == 3) {
                int id = (int) data.getExtras().get("id");
                int picNum = data.getExtras().getInt("picNum");
                String check = data.getExtras().getString("check");
                String imageUrl = data.getExtras().getString("imageUrl");
                ImageBox imageBox1, imageBox2, imageBox3;
                final GridView gridView;

                if(id == 1){
                    gridView = (GridView) findViewById(R.id.collegeIdImages);
                    if(check.equals("front")){
                        frontImageCollegeId = imageUrl;
                        if(backImageCollegeId != null && collegeIds.get(0).getImageUrl() == backImageCollegeId){
                            imageBox1 = collegeIds.get(0);
                            imageBox2 = collegeIds.get(picNum);
                            imageBox3 = collegeIds.get(1);
                            imageBox1.setPicNum(1);
                            imageBox2.setPicNum(0);
                            imageBox3.setPicNum(picNum);

                            collegeIds.set(0,imageBox2);
                            collegeIds.set(1,imageBox1);
                            collegeIds.set(picNum,imageBox3);
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
                                    textView.setText("FrontSide");
                                    textView.setVisibility(View.VISIBLE);

                                    LinearLayout gridChild1 = (LinearLayout) gridView.getChildAt(1);
                                    imageLayout1 = (FrameLayout) gridChild1.findViewById(R.id.imageLayout);
                                    imageLayout1.findViewWithTag(getResources().getString(R.string.unverified_status_icon)).setVisibility(View.INVISIBLE);
                                    imageLayout1.findViewWithTag(getString(R.string.verified_status_icon)).setVisibility(View.VISIBLE);
                                    textView = (TextView)gridChild1.findViewById(R.id.textView);
                                    textView.setText("BackSide");
                                    textView.setVisibility(View.VISIBLE);
                                }
                            });

                        }
                        else{
                            imageBox1 = collegeIds.get(0);
                            imageBox2 = collegeIds.get(picNum);
                            imageBox1.setPicNum(picNum);
                            imageBox2.setPicNum(0);
                            collegeIds.set(picNum,imageBox1);
                            collegeIds.set(0,imageBox2);
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
                    }
                    else if(check.equals("back")){
                        backImageCollegeId = imageUrl;
                        if(frontImageCollegeId != null ){
                            if(picNum == 0){
                                frontImageCollegeId = null;
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
                            else{
                                imageBox1 = collegeIds.get(1);
                                imageBox2 = collegeIds.get(picNum);
                                imageBox1.setPicNum(picNum);
                                imageBox2.setPicNum(1);
                                collegeIds.set(picNum,imageBox1);
                                collegeIds.set(1,imageBox2);
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
                            imageBox1 = collegeIds.get(0);
                            imageBox2 = collegeIds.get(picNum);
                            imageBox1.setPicNum(picNum);
                            imageBox2.setPicNum(0);
                            collegeIds.set(picNum,imageBox1);
                            collegeIds.set(0,imageBox2);
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

    public String getGoodFriendsRadio() {
        return  goodFriendsRadio;
    }

    public String getYearBackRadio() { return yearBackRadio;}

    public String repayBackLoan() { return loanRepay;}

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

    public ImageBox getCollegeIdFrontImageBox() {
        return collegeIdFrontImageBox;
    }

    public ImageBox getAddressProofFrontImageBox() {
        return addressProofFrontImageBox;
    }

    public ImageBox getAddressProofBackImageBox() {
        return addressProofBackImageBox;
    }

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
        Log.d("global","spinner");
        if(parent.getItemAtPosition(position)!=null)
        {
            Log.d("global","spinnernotnull");
            selectItem = parent.getItemAtPosition(position).toString();
        }
        else
        {
            selectItem = "";
        }
        switch (parent.getId())
        {
            case R.id.punctality: punctualityInClass = selectItem; spinnerFlag1 = true; Log.d("global","spinnercheck");break;
            case R.id.finResponsibility: financiallyResponsible = selectItem; spinnerFlag2 = true; Log.d("global","spinnercheck2"); break;
            case R.id.sincerity: sincerityInStudies = selectItem; spinnerFlag3 = true; break;
            case R.id.coCurricular: coCurricularParticipation = selectItem; spinnerFlag4 = true; break;
            case R.id.Loan_repay: loanRepay = selectItem; spinnerFlag5 = true; break;
        }
        if(spinnerFlag1 && spinnerFlag2 && spinnerFlag3 && spinnerFlag4 && spinnerFlag5) {
            spinnerFlag = true;
            Log.d("global","shizzspinnegfgfhgfr");
        }
        else
            Log.d("global","ew");


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
    }
}
