package com.xpluscloud.mosesshell_davao;

/**
 * Created by Shirwen on 11/14/2016.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import static com.xpluscloud.mosesshell_davao.MainActivity.devId;

public class AnimatedSplashScreen extends Activity {
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    /** Called when the activity is first created. */
    Thread splashTread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splashscreen2);
//        StartAnimations();

        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            }
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(android.content.Context.TELEPHONY_SERVICE);
            devId = telephonyManager.getDeviceId();

            StartAnimations();
        }
        catch (Exception e) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(AnimatedSplashScreen.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Reminder")
                    .setMessage("Please allow all permission needed from the application just click 'APP PERMISSION' and turn on all permission.");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                    myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                    myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(myAppSettings);
                    finish();
                }

            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .show();
        }
    }
    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        RelativeLayout l=(RelativeLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.splash);
        ImageView iv2 = (ImageView) findViewById(R.id.imageView12);
        iv.clearAnimation();
        iv.startAnimation(anim);
        iv2.clearAnimation();
        iv2.startAnimation(anim);

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 2000) {
                        sleep(100);
                        waited += 100;
                    }
                    AnimatedSplashScreen.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                                    /* Create an intent that will start the main activity. */
                            Intent mainIntent = new Intent(AnimatedSplashScreen.this,
                                    StartActivity.class);
                            AnimatedSplashScreen.this.startActivity(mainIntent);

                                    /* Finish splash activity so user cant go back to it. */
                            AnimatedSplashScreen.this.finish();

                                    /* Apply our splash exit (fade out) and main
                                       entry (fade in) animation transitions. */
                            overridePendingTransition(R.anim.mainfadein,
                                    R.anim.splashfadeout);
                        }
                    });
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    AnimatedSplashScreen.this.finish();

                    overridePendingTransition(R.anim.mainfadein,
                            R.anim.splashfadeout);
                }

            }
        };
        splashTread.start();

    }

}