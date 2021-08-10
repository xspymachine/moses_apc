package com.xpluscloud.moses_apc;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.xpluscloud.moses_apc.dbase.CustomerDbManager;
import com.xpluscloud.moses_apc.dbase.PictureDbManager;
import com.xpluscloud.moses_apc.getset.Picture;
import com.xpluscloud.moses_apc.util.DateUtil;
import com.xpluscloud.moses_apc.util.DbUtil;
import com.xpluscloud.moses_apc.util.Master;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.xpluscloud.moses_apc.util.DbUtil.getSetting;

public class TakePhotoActivity extends Activity {

    ImageView iv_store;
    RadioGroup rg_reason;
    RadioButton rbMerch,rbCusdel,rbPO;
    Button btn_camera;
    Button btn_gallery;
    Button btn_submit;
    EditText etPhotoRemark;

    String devId;
    String customerCode;
    String customerName;
    String customerAddress;
    int customerId;
    String del_remarks;

    String devPath = "";
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takephoto);
        this.setFinishOnTouchOutside(false);
        context = TakePhotoActivity.this;

        Bundle extras = getIntent().getExtras();
        devId 			= extras.getString("devId");
        customerCode	= extras.getString("customerCode");
        customerName	= extras.getString("customerName");
        customerAddress	= extras.getString("customerAddress");
//        customerId		= extras.getInt("customerId");
//        del_remarks     = extras.getString("del_remarks");

        iv_store = findViewById(R.id.image_store);
        iv_store.setTag("0");
        rg_reason = findViewById(R.id.rg_reason);
        rbMerch = findViewById(R.id.pic_merch);
        rbCusdel = findViewById(R.id.pic_cusdel);
        rbPO = findViewById(R.id.pic_po);
        btn_camera = findViewById(R.id.open_camera);
        btn_gallery = findViewById(R.id.open_gallery);
        btn_submit = findViewById(R.id.submit_deletion_image);
        etPhotoRemark = findViewById(R.id.photo_remark);

        CustomerDbManager db = new CustomerDbManager(context);
        db.open();
        customerId = db.getCustomer(customerCode).getId();
        db.close();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 1);
                }
            }
        });

        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap picture = (Bitmap) extras.get("data");
                    picture = Bitmap.createScaledBitmap(picture, 320, 240, true);

                    iv_store.setImageBitmap(picture);
                    iv_store.setTag("updated");
//                    savePicture(picture);

                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        iv_store.setImageBitmap(selectedImage);
                        iv_store.setTag("updated");
//                    savePicture(picture);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                    }

                }
                break;

        }
    }

    private void submit(){
        int selectedRadioButtonId = rg_reason.getCheckedRadioButtonId();
        RadioButton selectedRadioButton;
//        Log.e("rg_selected",selectedRadioButtonId+"");

        if (selectedRadioButtonId != -1) {
            selectedRadioButton = findViewById(selectedRadioButtonId);
            String selectedRbText = selectedRadioButton.getText().toString();
//            Log.e("rg_selected",selectedRbText);
            if(iv_store.getTag().equals("updated")){
                savePicture(selectedRbText,selectedRadioButtonId);
            }
            else Toast.makeText(context,"Please take or upload a photo of the store.",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context,"Please select a reason to submit.",Toast.LENGTH_LONG).show();
        }
    }

    private void savePicture(String rb_reason, int rg_selected) {
        Calendar c = Calendar.getInstance();

        iv_store.invalidate();
        BitmapDrawable drawable = (BitmapDrawable) iv_store.getDrawable();
        Bitmap Picture = drawable.getBitmap();

        SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd", Locale.US);
        String folder = df2.format(c.getTime());

        Boolean mSDcheck = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

        File[] extMounts = getApplicationContext().getExternalFilesDirs(null);
        File sdRoot = null;
        if(extMounts.length > 1) sdRoot = extMounts[1];
        if(sdRoot == null){
            Log.e("STORAGE","NOT AVAILABLE");
            devPath = Environment.getExternalStorageDirectory() + "/"
                    + getResources().getString(R.string.pictures_dir) + "/"
                    + folder +"/";
            prepareDirectory();
        }else{
            String domainedFolder = sdRoot.getAbsolutePath();
            Log.e("STORAGE","AVAILABLE="+domainedFolder);
            devPath = sdRoot.getAbsolutePath()+"/"+getResources().getString(R.string.pictures_dir)+"/"+folder+"/";
            prepareDirectory();

        }

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss",Locale.US);
        String dateStamp =  df.format(c.getTime());
        String filename = dateStamp + "_" + customerCode + ".jpg";


        Bitmap picture = embedText(Picture, c.getTimeInMillis(), customerName,customerAddress);


        String file = devPath + filename;


        File newfile = new File(file);
        try {
            newfile.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            FileOutputStream fo = new FileOutputStream(newfile);
            picture.compress(Bitmap.CompressFormat.JPEG,100, fo);
            fo.flush();
            fo.close();

        } catch (Exception e) {
            Log.v("log_tag", e.toString());
            DbUtil.makeToast(LayoutInflater.from(context),  "Error saving picture!" , context,null,0);
        }
        deleteLastPhotoTaken();
        String sysTime = String.valueOf(System.currentTimeMillis()/1000);

        int ba = 0; //BeforeAFter

        PictureDbManager db = new PictureDbManager(context);
        db.open();

        Picture p = new Picture();
        p.setDatetime(sysTime);
        p.setCustomerCode(customerCode);
        p.setBa(ba);
        p.setFilename(filename);
        p.setStatus(0);

        Long id = db.Add(p);
        db.close();

        devId = getSetting(context, Master.DEVID);

        if(rg_selected == rbMerch.getId()){
            String message = Master.CMD_MERCH_PICTURE + " " +
                    devId + ";" +
                    customerCode + ";" +
                    filename + ";" +
                    ba + ";" +
                    dateStamp + ";" +
                    sysTime + ";" +
                    id + ";" +
                    rb_reason;

            DbUtil.saveMsg(context, DbUtil.getGateway(context), message);
        }
        else if(rg_selected == rbCusdel.getId()) {
            String message = Master.CMD_DEL_PICTURE + " " +
                    devId + ";" +
                    customerCode + ";" +
                    filename + ";" +
                    ba + ";" +
                    dateStamp + ";" +
                    sysTime + ";" +
                    id + ";" +
                    rb_reason;

            DbUtil.saveMsg(context, DbUtil.getGateway(context), message);

            String message2 = Master.CMD_DCUS + " " +
                    devId + ";" +
                    customerCode + ";" +
                    sysTime + ";";

            DbUtil.saveMsg(context, DbUtil.getGateway(context), message2);
            CustomerDbManager db2 = new CustomerDbManager(this);
            db2.open();
            db2.updateDeletionPic(customerId);
            db2.close();
        }
        else if(rg_selected == rbPO.getId()){
            String message = Master.CMD_PO_PICTURE + " " +
                    devId + ";" +
                    customerCode + ";" +
                    filename + ";" +
                    ba + ";" +
                    dateStamp + ";" +
                    sysTime + ";" +
                    id + ";" +
                    rb_reason;

            DbUtil.saveMsg(context, DbUtil.getGateway(context), message);
        }

        finish();
    }

    private boolean prepareDirectory() {
        try {
            return makedirs();
        } catch (Exception e) {
            e.printStackTrace();
            DbUtil.makeToast(LayoutInflater.from(context), "Could not initiate File System.. Is Sdcard mounted properly?" , context,null,1);
            return false;
        }
    }

    private boolean makedirs() {
        File tempdir = new File(devPath);
        if (!tempdir.exists())
            tempdir.mkdirs();
        return (tempdir.isDirectory());
    }

    private void deleteLastPhotoTaken() {

        String[] projection = new String[] {
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.MIME_TYPE };

        final Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                null,null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

        if (cursor != null) {
            cursor.moveToFirst();

            int column_index_data =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            String image_path = cursor.getString(column_index_data);
            Log.e("sdpath",image_path);
            File file = new File(image_path);
            if (file.exists()) {
                file.delete();
                System.out.println("file Deleted :" + image_path);
                deleteFileFromMediaStore(context.getContentResolver(),file);
            }else System.out.println("file not Deleted :" + image_path);
        }
    }
    public static void deleteFileFromMediaStore(final ContentResolver contentResolver, final File file) {
        String canonicalPath;
        try {
            canonicalPath = file.getCanonicalPath();
        } catch (IOException e) {
            canonicalPath = file.getAbsolutePath();
        }
        final Uri uri = MediaStore.Files.getContentUri("external");
        final int result = contentResolver.delete(uri,
                MediaStore.Files.FileColumns.DATA + "=?", new String[] {canonicalPath});
        if (result == 0) {
            final String absolutePath = file.getAbsolutePath();
            if (!absolutePath.equals(canonicalPath)) {
                contentResolver.delete(uri,
                        MediaStore.Files.FileColumns.DATA + "=?", new String[]{absolutePath});
            }
        }
    }

    private Bitmap embedText(Bitmap picture, long timeStamp, String customerName, String customerAddress) {

        String strDate = DateUtil.phLongDateTime(timeStamp);
        del_remarks = etPhotoRemark.getText().toString();

        Bitmap mutableBitmap = picture.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);

//        Canvas canvas = new Canvas(picture);
        Paint paint = new Paint();

        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setShadowLayer(1.0f, 1.0f, 1.0f, Color.BLACK);
        paint.setTextSize(10);
//	    Log.e("height",""+picture.getHeight());
        int height = picture.getHeight();
        canvas.drawText(customerName, 10,(height-55),paint);
        canvas.drawText(customerAddress, 10,(height-45),paint);
        canvas.drawText(strDate, 10,(height-35),paint);
        canvas.drawText("Remarks: "+del_remarks, 10,(height-20),paint);
        return picture;
    }

}
