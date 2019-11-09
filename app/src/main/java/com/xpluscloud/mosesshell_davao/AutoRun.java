package com.xpluscloud.mosesshell_davao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoRun extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		Intent i= new Intent(context, BootStrapActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i); 
	
	}
}
