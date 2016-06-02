package com.example.aparna.buddy.app;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.squareup.picasso.Picasso;


public class DetailsActivity extends AppCompatActivity {
    ImageView plus,verify;
    RoundedImageView roundView;
    String pathIdFront="", pathIdBack;
    private Uri mImageCaptureUri;
    CircularImageView profile_pic;
    private boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        plus = (ImageView)findViewById(R.id.plusAddProofFront);
        roundView = (RoundedImageView) findViewById(R.id.AddProofFront);
        verify = (ImageView)findViewById(R.id.AddProofVerifyFront);
        profile_pic = (CircularImageView) findViewById(R.id.profile_image);
        String userid = "687182721423324";
        Picasso.with(this)
                .load("https://graph.facebook.com/" + userid+ "/picture?type=large")
                .into(profile_pic);
        //  "https://graph.facebook.com/" + userid + "/picture?type=large"
        roundView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag){
                    Log.d("true","click");
                    verifyPic(pathIdFront,"Address Proof",1);
                }

            }
        });



    }

    public void getPic(View v){

        /*final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_view);
        dialog.show();*/
        final MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter(this);
        adapter.add(new MaterialSimpleListItem.Builder(this)
                .iconPadding(10)
                .content("Take a photo")
                .icon(R.drawable.cameraicon)
                .backgroundColor(Color.WHITE)
                .build());

        //MaterialSimpleListIteme item = new MaterialSimpleListItem();
        adapter.add(new MaterialSimpleListItem.Builder(this)
                .content("Choose a photo")
                .iconPadding(10)
                .icon(R.drawable.photoicon)
                .backgroundColor(Color.WHITE)
                .build());



        new MaterialDialog.Builder(this)
                .adapter(adapter, new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        MaterialSimpleListItem item = adapter.getItem(which);
                        if(item.getContent().equals("Take a photo"))
                        {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, 1);
                            dialog.dismiss();
                        }
                        else if(item.getContent().equals("Choose a photo"))
                        {
                            Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(intent, 2);
                            dialog.dismiss();

                        }
                        else {
                            dialog.dismiss();
                        }
                    }
                })
                .show();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==1){

                Bitmap bp = (Bitmap) data.getExtras().get("data");
                Uri uri;
                String path="";
                Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.ImageColumns.ORIENTATION}, MediaStore.Images.Media.DATE_ADDED, null, "date_added ASC");
                if(cursor != null && cursor.moveToFirst())
                {
                    do {
                        uri = Uri.parse(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                        path = uri.toString();
                    }while(cursor.moveToNext());
                    cursor.close();
                }

                // ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                /*File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                pathIdFront=destination.getAbsolutePath();
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

                roundView.setImageBitmap(bp);
                pathIdFront = path;
                plus.setVisibility(View.INVISIBLE);
                verify.setVisibility(View.VISIBLE);
                roundView.setBorderWidth(0);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //roundView.setElevation(50);
                }
                roundView.setClickable(true);
                flag = true;
                putnewbox(roundView.getParent(),roundView.getId());

                // plus.setClickable(false);


            }
            else if(requestCode==2)
            {
                Uri selectedImageUri = data.getData();
                Cursor cursor = getContentResolver().query(selectedImageUri, null, null, null, null);
                cursor.moveToFirst();
                String document_id = cursor.getString(0);
                document_id = document_id.substring(document_id.lastIndexOf(":")+1);
                cursor.close();

                cursor = getContentResolver().query(
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
                cursor.moveToFirst();
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                pathIdFront=path;
                cursor.close();
                Bitmap bm;
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
                bm = BitmapFactory.decodeFile(path, options);
                roundView.setImageBitmap(bm);
                plus.setVisibility(View.INVISIBLE);
                roundView.setBorderWidth(0);
                roundView.setClickable(true);
                verify.setVisibility(View.VISIBLE);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //roundView.setElevation(50);
                }
                flag = true;
                // plus.setClickable(false);


            }
            else if(requestCode==3)
            {
                if(!pathIdFront.isEmpty())
                {
                    Log.d("result",pathIdFront);
                    Bitmap bm;
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(pathIdFront, options);
                    final int REQUIRED_SIZE = 200;
                    int scale = 1;
                    while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                            && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                        scale *= 2;
                    options.inSampleSize = scale;
                    options.inJustDecodeBounds = false;
                    bm = BitmapFactory.decodeFile(pathIdFront, options);
                    roundView.setImageBitmap(bm);
                }

            }
        }
    }
    public void verifyPic(String image_path,String pic_type,int num)
    {
        if(!roundView.isClickable())
            return;

        Intent intent = new Intent(getApplicationContext(),ImageViewActivity.class);
        intent.putExtra("image_path",image_path);
        intent.putExtra("pic_type",pic_type);
        intent.putExtra("picNum",num);
        startActivityForResult(intent,3);
    }


    public void putnewbox(ViewParent viewParent, int id)
    {
        Log.d("reach","reached");
        LinearLayout layout = (LinearLayout)viewParent.getParent();
        LinearLayout layout_vertical = new LinearLayout(this);
        layout_vertical.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        int leftmargin = getPx(30);
        layoutParams.setMargins(leftmargin, 0, 0, 0);

        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        RoundedImageView baseImageView = new RoundedImageView(this);
//        baseImageView.setId(Integer.parseInt("@+id/apa"));
        int pixels = getPx(80);
        baseImageView.setLayoutParams(new LinearLayout.LayoutParams(pixels,pixels));
        pixels=getPx(6);
        baseImageView.setRadius(pixels);
        pixels=getPx(1);
        baseImageView.setBorderWidth(pixels);
        baseImageView.setBorderColor(Color.DKGRAY);
        baseImageView.setImageResource(R.drawable.blank);
        frameLayout.addView(baseImageView);
        layout_vertical.addView(frameLayout,layoutParams);
        layout.addView(layout_vertical);

    }

    public int getPx(int dps)
    {
        final float scale = this.getResources().getDisplayMetrics().density;
        int pixels = (int) ( dps* scale + 0.5f);
        return pixels;
    }
}
