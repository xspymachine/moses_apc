package com.xpluscloud.mosesshell_davao;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class StoretypeActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        int opt	=b.getInt("type");
        if(opt==1) setContentView(R.layout.activity_storetype);
        else setContentView(R.layout.activity_nonbuytype);
    }
}
