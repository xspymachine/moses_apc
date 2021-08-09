package com.xpluscloud.moses_apc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class RestartService extends BroadcastReceiver {
	private final String TAG = "RestartService";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.e(TAG, "onReceive");
		
		String serviceClass =   intent.getStringExtra("serviceClass");
		Log.e(TAG, "onReceive: " + serviceClass);
		
		try {
			context.startService(new Intent(context, Class.forName(serviceClass)));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

}
