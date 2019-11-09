package com.xpluscloud.mosesshell_davao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class BarcodeScanner extends Activity implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;

    private final String TAG = "BarcodeScanner";
    Context context;
    private boolean mFlash;
    private boolean mAutoFocus;
    private boolean hasFlash;
    private boolean supportsAutofocus;
    private static final String FLASH_STATE = "FLASH_STATE";
    private static final String AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE";
    private static final String SELECTED_FORMATS = "SELECTED_FORMATS";
    private static final String CAMERA_ID = "CAMERA_ID";
    private ArrayList<Integer> mSelectedIndices;
    private int mCameraId = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = BarcodeScanner.this;
        mScannerView = new ZBarScannerView(this);    // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view

        hasFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        supportsAutofocus = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS);
        if(savedInstanceState != null)
        {
            mFlash = savedInstanceState.getBoolean(FLASH_STATE,false);
            mAutoFocus = savedInstanceState.getBoolean(AUTO_FOCUS_STATE,false);
            mSelectedIndices = savedInstanceState.getIntegerArrayList(SELECTED_FORMATS);
            mCameraId = savedInstanceState.getInt(CAMERA_ID,-1);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                Window window = getWindow();
//                window.setStatusBarColor(ContextCompat.getColor(this,R.color.actionbar_opacity));
//            }
        }
        else
        {
            mFlash = false;
            mAutoFocus = true;
            mSelectedIndices = null;
            mCameraId = -1;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v(TAG, rawResult.getContents()); // Prints scan results
        Log.v(TAG, rawResult.getBarcodeFormat().getName()); // Prints the scan format (qrcode, pdf417 etc.)

        Intent intent = new Intent(context, InOutActivity.class);
        Bundle b = new Bundle();

        b.putString("ccode", rawResult.getContents());
        intent.putExtras(b);
        startActivity(intent);

        mScannerView.stopCamera();           // Stop camera on pause
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem;


        if(hasFlash)
        {
            if(mFlash) {
                menuItem = menu.add(Menu.NONE, R.id.menu_flash, 0, R.string.flash_on);
            } else {
                menuItem = menu.add(Menu.NONE, R.id.menu_flash, 0, R.string.flash_off);
            }
            MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_NEVER);
        }

        if(supportsAutofocus)
        {
            if(mAutoFocus) {
                menuItem = menu.add(Menu.NONE, R.id.menu_auto_focus, 0, R.string.auto_focus_on);
            } else {
                menuItem = menu.add(Menu.NONE, R.id.menu_auto_focus, 0, R.string.auto_focus_off);
            }
            MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_NEVER);
        }



//        menuItem = menu.add(Menu.NONE, R.id.menu_formats, 0, R.string.formats);
//        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_NEVER);
//
//
//        menuItem = menu.add(Menu.NONE, R.id.menu_camera_selector, 0, R.string.select_camera);
//        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_NEVER);
//

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            finish();
            return true;
        } else if (i == R.id.menu_flash) {
            mFlash = !mFlash;
            if (mFlash) {
                item.setTitle(R.string.flash_on);
            } else {
                item.setTitle(R.string.flash_off);
            }
            mScannerView.setFlash(mFlash);
            return true;
        } else if (i == R.id.menu_auto_focus) {
            mAutoFocus = !mAutoFocus;
            if (mAutoFocus) {
                item.setTitle(R.string.auto_focus_on);
            } else {
                item.setTitle(R.string.auto_focus_off);
            }
            mScannerView.setAutoFocus(mAutoFocus);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }
}
