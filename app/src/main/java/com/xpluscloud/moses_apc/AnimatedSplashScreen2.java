package com.xpluscloud.moses_apc;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

import java.util.ArrayList;
import java.util.List;

public class AnimatedSplashScreen2 extends AwesomeSplash {
    @Override
    public void initSplash(ConfigSplash configSplash) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        configSplash.setBackgroundColor(R.color.shellyellow);
        configSplash.setAnimCircularRevealDuration(0);
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM);

        configSplash.setLogoSplash(R.drawable.shell2);
        configSplash.setOriginalHeight(1);
        configSplash.setOriginalWidth(1);
        configSplash.setAnimLogoSplashDuration(1500);
        configSplash.setAnimLogoSplashTechnique(Techniques.BounceIn);

        configSplash.setTitleSplash("");
    }

    @Override
    public void animationsFinished() {

        insertDummyContactWrapper();
    }

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    int ret = 0;

    private void insertDummyContactWrapper(){
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
                                    AnimatedSplashScreen2.this.finish();
                                }
                                else {
                                    ActivityCompat.requestPermissions(AnimatedSplashScreen2.this, permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                }
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AnimatedSplashScreen2.this.finish();
                            }
                        });
                return;
            }
            ActivityCompat.requestPermissions(AnimatedSplashScreen2.this,permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);

            return;
        }

        openSesame();
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(AnimatedSplashScreen2.this,permission)) {
                if(ret == 1) {
                    Toast.makeText(this, "If you check 'Never ask again' go to settings and enable permissions", Toast.LENGTH_SHORT)
                            .show();
                }
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {
            insertDummyContactWrapper();
            ret = 1;
        }
    }

    AlertDialog dialog;
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder ad =  new AlertDialog.Builder(getApplicationContext());
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
                Intent mainIntent = new Intent(AnimatedSplashScreen2.this,
                        StartActivity.class);
                AnimatedSplashScreen2.this.startActivity(mainIntent);

                /* Finish splash activity so user cant go back to it. */
                AnimatedSplashScreen2.this.finish();

	                    /* Apply our splash exit (fade out) and main
	                       entry (fade in) animation transitions. */
                overridePendingTransition(R.anim.mainfadein,
                        R.anim.splashfadeout);
            }
        }, 1000);
    }
}
