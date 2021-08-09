package com.xpluscloud.moses_apc;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.view.Window;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SplashScreen extends Activity {
	
	Context context;
	int optionDownload = 0;	
	
	String devId;
	String APIK;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		context = SplashScreen.this; 
		
		/**
         * Showing splashscreen while making network calls to download necessary
         * data before launching the app Will use AsyncTask to make http call
         */
        setContentView(R.layout.activity_splashscreen);

        insertDummyContactWrapper();

		
//		try{
//			TelephonyManager telephonyManager = (TelephonyManager)getSystemService(android.content.Context.TELEPHONY_SERVICE);
//			devId = telephonyManager.getDeviceId();
//
//			new Handler().postDelayed(new Runnable() {
//	            @Override
//	            public void run() {
//
//	                    /* Create an intent that will start the main activity. */
//	                    Intent mainIntent = new Intent(SplashScreen.this,
//	                            StartActivity.class);
//	                    SplashScreen.this.startActivity(mainIntent);
//
//	                    /* Finish splash activity so user cant go back to it. */
//	                    SplashScreen.this.finish();
//
//	                    /* Apply our splash exit (fade out) and main
//	                       entry (fade in) animation transitions. */
//	                    overridePendingTransition(R.anim.mainfadein,
//	                            R.anim.splashfadeout);
//	            }
//			}, 5000);
//		}
//		catch (Exception e) {
//			AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
//	        .setIcon(android.R.drawable.ic_dialog_alert)
//	        .setTitle("Reminder")
//	        .setMessage("Please allow all permission needed from the application just click 'APP PERMISSION' and turn on all permission.");
//			alertDialog.setCancelable(false);
//			alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener()
//		    {
//		        @Override
//		        public void onClick(DialogInterface dialog, int which) {
//		        	Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
//				    myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
//				    myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				    startActivity(myAppSettings);
//				    finish();
//		        }
//
//		    })
//			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					// TODO Auto-generated method stub
//					dialog.dismiss();
//					finish();
//				}
//			})
//			.show();
//		}
		
	}

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    int ret = 0;

    private void insertDummyContactWrapper() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("Camera");
        if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS))
            permissionsNeeded.add("Contacts");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("Location");
        if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE))
            permissionsNeeded.add("Phone");
        if (!addPermission(permissionsList, Manifest.permission.SEND_SMS))
            permissionsNeeded.add("SMS");
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("Read Storage");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Write Storage");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)

                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(ret==1){
                                    Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                                    myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                                    myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(myAppSettings);
                                    SplashScreen.this.finish();
                                }
                                else {
                                    ActivityCompat.requestPermissions(SplashScreen.this, permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                }
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SplashScreen.this.finish();
                            }
                        });
                return;
            }
            ActivityCompat.requestPermissions(SplashScreen.this,permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);

            return;
        }

        openSesame();
    }
    private final String TAG = "SPLASHSCREEN";

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                insertDummyContactWrapper();
                ret=1;
            }
        }
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ActivityCompat.checkSelfPermission(context,permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(SplashScreen.this,permission)) {
                if(ret == 1) {
                    Toast.makeText(this, "If you check 'Never ask again' go to settings and enable permissions", Toast.LENGTH_SHORT)
                            .show();
                }
                return false;
            }
        }
        return true;
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
       AlertDialog.Builder ad =  new AlertDialog.Builder(context);
       ad   .setCancelable(false)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", cancelListener)
                .create();
       dialog = ad.show();

    }

    private void openSesame(){
        new Handler().postDelayed(new Runnable() {
	            @Override
	            public void run() {

	                    /* Create an intent that will start the main activity. */
	                    Intent mainIntent = new Intent(SplashScreen.this,
	                            StartActivity.class);
	                    SplashScreen.this.startActivity(mainIntent);

	                    /* Finish splash activity so user cant go back to it. */
	                    SplashScreen.this.finish();

	                    /* Apply our splash exit (fade out) and main
	                       entry (fade in) animation transitions. */
	                    overridePendingTransition(R.anim.mainfadein,
	                            R.anim.splashfadeout);
	            }
			}, 2000);
    }

    AlertDialog dialog;

//    @Override
//    protected void onDestroy() {
//        try{
//            dialog.dismiss();
//        }catch (Exception e){e.printStackTrace();}
//        super.onDestroy();
//    }
}
