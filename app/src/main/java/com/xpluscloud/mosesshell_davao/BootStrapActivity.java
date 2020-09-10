package com.xpluscloud.mosesshell_davao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.xpluscloud.mosesshell_davao.util.DateUtil;
import com.xpluscloud.mosesshell_davao.util.DbUtil;
import com.xpluscloud.mosesshell_davao.util.Master;


public class BootStrapActivity extends Activity {
	
	Context context;
	public SharedPreferences settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);		
		context = BootStrapActivity.this;
		
		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//		String devId = telephonyManager.getDeviceId();
		String devId = Master.getDevId2(context);
		String sysDateTime = DateUtil.strDateTime(System.currentTimeMillis());
	   	
	   	Integer battery = Master.getBatteryInfo(context);
	   	String statGPS = Master.getStatGPS(context).equals(1) ? " GPS: ON" : " GPS: OFF";
		
		Log.i("Autorun", sysDateTime + ";" + battery + ";" + statGPS);
		
		String message = Master.CMD_BOOT + " " +
   		   		devId + ";" +
   		   		sysDateTime + ";" + 
   		   		" Battery: " + battery + "%;" + 
   		   		statGPS;
		
		DbUtil.saveMsg(context,DbUtil.getGateway(context), message);
		
		
		Intent i= new Intent(context, StartActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i); 
		
		finish();
		
		
	}

}
