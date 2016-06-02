package com.example.aparna.buddy.model;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.example.aparna.buddy.app.BorrowerDetailsActivity;
import com.example.aparna.buddy.app.ImageViewActivity;
import com.example.aparna.buddy.app.R;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

/**
 * Created by Aparna on 26/4/16.
 */
public class ImageBox {
    private FrameLayout imageBoxLayout;
    private int picNum,id;
    private String localPhotoPath, picType, cloudinaryId;
    private BorrowerDetailsActivity activity;
    private RoundedImageView roundedImageView;
    private ImageView plusIcon, unverifiedIcon, verifiedIcon;
    private String imageUrl, roundedImageViewTag, plusIconTag, unverifiedIconTag, verifiedIconTag;
    private Boolean verified = false;

    public ImageBox(FrameLayout view, BorrowerDetailsActivity activity) {
        imageBoxLayout = view;
        this.activity  = activity;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void inflate() {
        roundedImageViewTag = activity.getResources().getString(R.string.rounded_image_view);
        plusIconTag         = activity.getResources().getString(R.string.plus_icon);
        unverifiedIconTag   = activity.getResources().getString(R.string.unverified_status_icon);
        verifiedIconTag     = activity.getResources().getString(R.string.verified_status_icon);

        roundedImageView       = (RoundedImageView) imageBoxLayout.findViewWithTag(roundedImageViewTag);
        plusIcon               = (ImageView) imageBoxLayout.findViewWithTag(plusIconTag);
        unverifiedIcon = (ImageView) imageBoxLayout.findViewWithTag(unverifiedIconTag);
        verifiedIcon   = (ImageView) imageBoxLayout.findViewWithTag(verifiedIconTag);

        if (imageUrl != null) {
            loadImage(roundedImageView, imageUrl);

            roundedImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    verifyPic(view, imageUrl);
                }
            });

            plusIcon.setVisibility(View.INVISIBLE);
            if(this.verified){
                setVerified();
            }
        } else {
            imageBoxLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadImagePrompt();
                }
            });
        }
    }

    public int getId() {return id;}

    public void setId(int id) {this.id = id; }

    public RoundedImageView getRoundedImageView() {
        return roundedImageView;
    }

    public ImageView getPlusIcon() {
        return plusIcon;
    }

    public ImageView getUnverifiedIcon() {
        return unverifiedIcon;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setLocalPhotoPath(String localPhotoPath) {
        this.localPhotoPath = localPhotoPath;
    }

    public int getPicNum() {
        return picNum;
    }

    public void setPicNum(int picNum) {
        this.picNum = picNum;
    }

    public String getPicType() {
        return picType;
    }

    public void setPicType(String picType) {
        this.picType = picType;
    }

    public String getCloudinaryId() {
        return cloudinaryId;
    }

    public void setCloudinaryId(String cloudinaryId) {
        this.cloudinaryId = cloudinaryId;
    }

    public void verifyPic(View view, String imageUrl) {
        if(!view.isClickable()) return;

        Intent intent = new Intent(activity, ImageViewActivity.class);
        intent.putExtra("imageUrl", imageUrl);
        intent.putExtra("picType", picType);
        intent.putExtra("id", getId());
        intent.putExtra("picNum", picNum);
        intent.putExtra("verified",verified);
        if(getId() == 1) {
            intent.putExtra("size", activity.getNumCollegeIds());
        }
        else if(getId() == 2){
            intent.putExtra("size",activity.getNumAddressProofs());
        }
        activity.startActivityForResult(intent, 3);
    }

    public void loadImage(RoundedImageView view, String imageUrl) {
        Picasso.with(activity)
               .load(imageUrl)
               .fit()
               .into(view);
    }

    public void loadImagePrompt() {
        final String take_photo   = activity.getResources().getString(R.string.take_photo);
        final String choose_photo = activity.getResources().getString(R.string.choose_photo);

        final MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter(activity);
        adapter.add(new MaterialSimpleListItem.Builder(activity)
                .iconPadding(10)
                .content(take_photo)
                .icon(R.drawable.cameraicon)
                .backgroundColor(Color.WHITE)
                .build());

        adapter.add(new MaterialSimpleListItem.Builder(activity)
                .content(choose_photo)
                .iconPadding(10)
                .icon(R.drawable.photoicon)
                .backgroundColor(Color.WHITE)
                .build());



        new MaterialDialog.Builder(activity)
                .adapter(adapter, new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        MaterialSimpleListItem item = adapter.getItem(which);
                        SharedPreferences settings = activity.getSharedPreferences(BuddyConstants.PREFS_FILE, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        if (item.getContent().equals(take_photo)) {
                            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            editor.putInt("conform",getId());
                            editor.commit();
                            activity.startActivityForResult(intent, 1);
                            dialog.dismiss();
                        } else if (item.getContent().equals(choose_photo)) {
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            editor.putInt("conform",getId());
                            editor.commit();
                            intent.setType("image/*");
                            activity.startActivityForResult(intent, 2);
                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
                        }
                    }
                })
                .show();
    }

    public void setVerified() {
        this.verified = true;
        this.verifiedIcon.setVisibility(View.VISIBLE);
    }

    public void setVerifiedImage() {
        this.verified = true;
    }

    public void setUnverified() {
        this.verified = false;
        this.unverifiedIcon.setVisibility(View.VISIBLE);
    }


    public Boolean verified(){
        return this.verified;
    }
}
