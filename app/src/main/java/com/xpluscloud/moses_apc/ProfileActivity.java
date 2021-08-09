package com.xpluscloud.moses_apc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xpluscloud.moses_apc.util.DateUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProfileActivity extends Activity {
	Context context;
	
	TextView tvName;
	TextView tvAddress;
	ImageView ivOwner;
	TextView tvCStatus;
	TextView tvBdate;
	TextView tvInt;
	TextView tvPhone;
	TextView tvEmail;
	
	TextView tvRoute;
	TextView tvLoan;
	TextView tvAverage;
	TextView tvFreq;
	
	private String devPath;
	private String imagePathFromSDCard="";
	private String filename;
	private String folder;
	
	private String customerName="";
	private String customerCode="";
	private String customerAddress="";
	private String birthdate="";
	private String hobbies="";
	private String number="";
	private String email="";
	private String civilStatus="";
	
	private String route = "";
	private String loan  = "";
	private String freq 	 = "";
	private String average	 = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customer_profile);
		context = ProfileActivity.this;
		
		Bundle b = getIntent().getExtras();
		customerName	= b.getString("cname");
		customerCode	= b.getString("ccode");
		customerAddress	= b.getString("address");
		birthdate		= b.getString("bday");
		hobbies			= b.getString("hobbies");
		number			= b.getString("num");
		email			= b.getString("email");
		civilStatus		= b.getString("cstatus");
		
		average		= b.getString("average");
		loan		= b.getString("loan");
		route		= b.getString("route");
		freq		= b.getString("freq");
		
		filename = customerName + "_" + customerCode + ".png";
		folder = getResources().getString(R.string.app_name)+getResources().getString(R.string.version)+"_Profile";
		devPath = Environment.getExternalStorageDirectory() + "/"
	            + getResources().getString(R.string.pictures_dir) + "/"
	            + folder +"/"; 
		
		setUp();		
		
	}
	
	private void setUp() {
		// TODO Auto-generated method stub
		tvName 		= (TextView) findViewById(R.id.tvName);
		tvAddress 	= (TextView) findViewById(R.id.tvAddress);
		ivOwner 	= (ImageView) findViewById(R.id.ivOwner);
		
		tvCStatus 	= (TextView) findViewById(R.id.tvCStatus);
		tvBdate 	= (TextView) findViewById(R.id.tvBdate);
		tvInt 		= (TextView) findViewById(R.id.tvInt);
		tvPhone 	= (TextView) findViewById(R.id.tvPhone);
		tvEmail 	= (TextView) findViewById(R.id.tvEmail);
		
		tvLoan		= (TextView) findViewById(R.id.tvTrucks);
		tvRoute		= (TextView) findViewById(R.id.tvWareCap);
		tvFreq		= (TextView) findViewById(R.id.tvDelivery);
		tvAverage	= (TextView) findViewById(R.id.tvSupplier);
		
		tvName.setText(customerName);
		tvAddress.setText(customerAddress);
		tvCStatus.setText(civilStatus);
		tvBdate.setText(birthdate.isEmpty() ? "" : DateUtil.reverseDate(birthdate));
		tvInt.setText(hobbies);
		tvPhone.setText(number);
		tvEmail.setText(email);
		
		tvLoan.setText(loan);
		tvRoute.setText(route);
		tvFreq.setText(freq);
		tvAverage.setText(average);
		
		ivOwner.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("OWNER","take picture");
				takePicture();
			}
		});
		
		getNewImage();
	}
	
	private void getNewImage(){
		prepareDirectory();   
		imagePathFromSDCard = devPath + filename;
		
		File directory = new File(devPath);
		Boolean available = false;
		// Folder is empty
		for(File f : directory.listFiles()){
			Log.e("contents", f.getName());
			if (f.isFile() && f.getName().contains(filename)){
				available = true;
				break;
			}
		}
				
		Log.e(available.toString(),filename);
		if(available && !filename.equals("")){
			Bitmap bitmapImage = BitmapFactory.decodeFile(imagePathFromSDCard);
	        Drawable drawableImage = new BitmapDrawable(context.getResources(), bitmapImage);
	
//	        ivOwner.setBackgroundDrawable(drawableImage);
	        int sdk = android.os.Build.VERSION.SDK_INT;
	        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
	        	ivOwner.setBackgroundDrawable(drawableImage);
	        } else {
	        	ivOwner.setBackground(drawableImage);
	        }
		}
	}
	
	private void takePicture(){		
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        startActivityForResult(takePictureIntent, 1);
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
	        Toast.makeText(
	                this, 
	                "Could not initiate File System.. Is Sdcard mounted properly?", Toast.LENGTH_LONG).show();
	        return false; 
	    } 
	} 
	
	private boolean makedirs() { 
	    File tempdir = new File(devPath);
	    if (!tempdir.exists()) 
	        tempdir.mkdirs(); 
	    /*
	    if (tempdir.isDirectory()) { 
	        File[] files = tempdir.listFiles(); 
	        for (File file : files) { 
	            if (!file.delete()) { 
	                System.out.println("Failed to delete " + file); 
	            } 
	        } 
	    } 
	    */
	    return (tempdir.isDirectory()); 
	} 
	
	private void savePicture(Bitmap Picture) {
		
        prepareDirectory();          
        Bitmap picture = getRoundedRectBitmap(Picture, 20);

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
            Toast.makeText( getApplicationContext(), "Error saving picture!"  , Toast.LENGTH_LONG ).show();
        }
        
	}
	
	public static Bitmap getRoundedRectBitmap(Bitmap bitmap, int pixels) {
	    Bitmap result = null;
	    try {
	        result = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);
	        Canvas canvas = new Canvas(result);

	        int color = 0xffffffff;
	        Paint paint = new Paint();
	        Rect rect = new Rect(0, 0, 150, 150);
	        RectF rectF = new RectF(rect);
	        int roundPx = pixels;

	        paint.setAntiAlias(true);
	        canvas.drawARGB(0, 0, 0, 0);
	        paint.setColor(color);
	        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
	        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	        canvas.drawBitmap(bitmap, rect, rect, paint);

	    } catch (NullPointerException e) {
	    } catch (OutOfMemoryError o) {
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
			        picture = Bitmap.createScaledBitmap(picture, 150, 150, true);
			        savePicture(picture);
			        getNewImage();
			    }
			    break;     
			     
		}		
		  
	}//onActivityResult

}
