package com.xpluscloud.moses_apc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xpluscloud.moses_apc.dbase.CustomerDbManager;
import com.xpluscloud.moses_apc.getset.Customer;
import com.xpluscloud.moses_apc.util.DbUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Shirwen on 3/25/2017.
 */

public class CustomerProfileActivity extends Activity {
    Context context;

    ArrayList<String> values1 = new ArrayList<>();
    ArrayList<String> values2 = new ArrayList<>();
    String[] keys = {
            "Name",
            "Position",
            "Birthdate",
            "Hobbies and Interest",
            "Contact Number",
            "E-mail Address",
            "Total AR"
    };

    Customer customer_info;
    String folder;
    String devPath;
    String filename;
    String imagePathFromSDCard="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);
        context = CustomerProfileActivity.this;

        Bundle b = getIntent().getExtras();
        String devId = b.getString("devId");
        String ccode = b.getString("ccode");

        folder = getResources().getString(R.string.app_name)+getResources().getString(R.string.version)+"_Profile";
        devPath = Environment.getExternalStorageDirectory() + "/"
                + getResources().getString(R.string.pictures_dir) + "/"
                + folder +"/";

        getInfo(ccode);
        createView();

    }

    private void getInfo(String ccode) {
        // TODO Auto-generated method stub
        CustomerDbManager db = new CustomerDbManager(context);
        db.open();
        values1 = db.getCustomerDataInfo3(ccode,10);
        values1.add(DbUtil.getCustomerCLBAL(context,ccode));
        values2 = db.getCustomerDataInfo3(ccode,20);
        customer_info = db.getCustomer(ccode);
        filename = customer_info.getName() + "_" + customer_info.getCustomerCode() + ".png";
        db.close();
    }

    private void createView(){
        TextView profile_name =  findViewById(R.id.user_profile_name);
        profile_name.setText(customer_info.getName());
        TextView profile_address =  findViewById(R.id.user_profile_short_bio);
        profile_address.setText(customer_info.getAddress()+" "+customer_info.getBrgy()+"\n"+customer_info.getCity()+" "+customer_info.getState());

        LinearLayout info_layout =  findViewById(R.id.profile_info);
        info_layout.removeAllViews();
        int temp=0;
        for(int i=0;i<keys.length;i++){
            View inflateView = View.inflate(context, R.layout.profile_single_row,null);
            TextView key =  inflateView.findViewById(R.id.key);
            TextView value =  inflateView.findViewById(R.id.value);

            if(keys[i].equalsIgnoreCase("SECONDARY CONTACT")){
                key.setText("");
                value.setText(keys[i]);
                value.setTypeface(null, Typeface.BOLD);
                value.setGravity(Gravity.CENTER);
                key.setVisibility(View.GONE);
                info_layout.addView(inflateView);
                temp=1;
            }
            else if(temp==1){
                if(!values2.get(i-7).isEmpty()){
                    key.setText(keys[i]);
                    value.setText(values2.get(i-7));
                    info_layout.addView(inflateView);
                }
            }
            else if(!values1.get(i).isEmpty()){
                key.setText(keys[i]);
                value.setText(values1.get(i));
                info_layout.addView(inflateView);
            }

        }

        setupImageProfile();
    }

    private void setupImageProfile(){
        MLRoundedImageView profile_pic = (MLRoundedImageView) findViewById(R.id.user_profile_photo);
        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
        imagePathFromSDCard = devPath + filename;
        Log.e("devpath",imagePathFromSDCard);
        File directory = new File(imagePathFromSDCard);
        if(directory.exists()){
//            Bitmap bitmapImage = BitmapFactory.decodeFile(imagePathFromSDCard);
//            Drawable drawableImage = new BitmapDrawable(context.getResources(), bitmapImage);
            try {
                Bitmap bit = BitmapFactory.decodeFile (imagePathFromSDCard); // Customize / / path
                profile_pic.setImageBitmap(bit);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    private void takePicture(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 1);
        }

    }

    private void savePicture(Bitmap Picture) {

        prepareDirectory();
        Bitmap picture = getRoundedRectBitmap(Picture,20);

        String file = devPath + filename;

        File newfile = new File(file);
        try {
            newfile.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            FileOutputStream fo = new FileOutputStream(newfile);
            picture.compress(Bitmap.CompressFormat.PNG,100, fo);
            fo.flush();
            fo.close();

        } catch (Exception e) {
            Log.v("log_tag", e.toString());
            DbUtil.makeToast(LayoutInflater.from(context), "Error saving picture!" , context,null,0);
        }

    }

    private boolean prepareDirectory() {
        try {
            if (makedirs()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            DbUtil.makeToast(LayoutInflater.from(context), "Could not initiate File System.. Is Sdcard mounted properly?" , context,null,0);
            return false;
        }
    }

    private boolean makedirs() {
        File tempdir = new File(devPath);
        if (!tempdir.exists())
            tempdir.mkdirs();
        return (tempdir.isDirectory());
    }

    public static Bitmap getRoundedRectBitmap(Bitmap bitmap, int pixels) {
        Bitmap result = null;
        try {
            result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);;
            pixels = bitmap.getWidth();
            Canvas canvas = new Canvas(result);

            int color = 0xffffffff;
            Paint paint = new Paint();
            Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            RectF rectF = new RectF(rect);
            int roundPx = pixels;

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

        } catch (NullPointerException | OutOfMemoryError e) {
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap picture = (Bitmap) extras.get("data");
//                    picture = Bitmap.createScaledBitmap(picture, picture.getWidth(), picture.getHeight(), true);
                    savePicture(picture);
                    setupImageProfile();
                }
                break;

        }

    }//onActivityResult
}
