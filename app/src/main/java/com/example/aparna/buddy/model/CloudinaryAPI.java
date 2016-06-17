package com.example.aparna.buddy.model;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.aparna.buddy.app.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aparna on 24/4/16.
 */
public class CloudinaryAPI {
    private Cloudinary cloudinary;
    private Activity activity;
    private String imageurl;
    private UploadCallBack uploadCallBack;
    public CloudinaryAPI(Activity activity) {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "mesh");
        config.put("api_key", "541578479444757");
        config.put("api_secret", "1ITDPajCmIcYn2UBlE-kbwHkr5A");
        this.cloudinary = new Cloudinary(config);
        this.activity   = activity;
    }

    public void uploadImage(final Bitmap bitmap, final String localPath, final BorrowerData borrowerData,final UploadCallBack uploadCallBack) {
        this.uploadCallBack=uploadCallBack;
        new AsyncTask<String, String, String>() {
            MaterialDialog materialDialog;
            Exception exception;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                materialDialog = new MaterialDialog.Builder(activity)
                        .content(activity.getResources().getString(R.string.uploading_documents))
                        .progress(true, 0)
                        .show();
            }

            @Override
            protected String doInBackground(String... strings) {
                String url = "";
                if (bitmap == null) {
                    return url;
                }

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] imageInByte = byteArrayOutputStream.toByteArray();
                ByteArrayInputStream byteArrayInputStream
                        = new ByteArrayInputStream(imageInByte);

                Long tsLong     = System.currentTimeMillis() / 1000;
                String ts       = tsLong.toString();
                String publicId = borrowerData.getUploadDocModel().getPhone()+"-" + ts + "-" +localPath;

                try {
                    cloudinary.uploader()
                            .upload(byteArrayInputStream,
                                    ObjectUtils.asMap("public_id", publicId));
                    url = cloudinary.url().generate(publicId);

                } catch (Exception e) {
                    Log.e("Cloudinary API", e.getLocalizedMessage());
                    exception = e;
                    return url;
                }

                return url;
            }


            @Override
            protected void onPostExecute(String url) {
                super.onPostExecute(url);
                if (materialDialog.isShowing()) {
                    materialDialog.hide();
                }
                uploadCallBack.methodToCallBack(url);
            }
        }.execute();

    }

  public  interface UploadCallBack {
        void methodToCallBack(String imageUrl);
    }

}
