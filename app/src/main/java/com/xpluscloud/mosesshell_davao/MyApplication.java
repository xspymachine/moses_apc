package com.xpluscloud.mosesshell_davao;

import android.support.multidex.MultiDexApplication;

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
    }
}
