package com.xpluscloud.moses_apc;

import android.content.ContextWrapper;
import android.support.multidex.MultiDexApplication;

import com.pixplicity.easyprefs.library.Prefs;
import com.zplesac.connectionbuddy.ConnectionBuddy;
import com.zplesac.connectionbuddy.ConnectionBuddyConfiguration;

public class MyApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        ConnectionBuddyConfiguration configuration = new ConnectionBuddyConfiguration.Builder(this)
                .setNotifyImmediately(false)
                .build();
        ConnectionBuddy.getInstance().init(configuration);

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName("MosesSettings")
                .setUseDefaultSharedPreference(true)
                .build();
    }
}
