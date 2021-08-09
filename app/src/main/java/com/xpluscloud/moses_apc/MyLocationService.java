package com.xpluscloud.moses_apc;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.xpluscloud.moses_apc.dbase.SettingDbManager;
import com.xpluscloud.moses_apc.util.DateUtil;
import com.xpluscloud.moses_apc.util.DbUtil;
import com.xpluscloud.moses_apc.util.Master;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MyLocationService extends Service {
	public String devId;
	public Long SendInterval;

	public String GpsMinTime;
	public String GpsMinDistance;
	
	public long LastSent = 0;
		
	public boolean gps_enabled;
	public boolean network_enabled;	
	
	public boolean gpsOn = false;
	
	static LocationManager mlocGPS;
	static LocationManager mlocNET;
	static LocationListener mlocListener;
	
	public static Location currentLocation=null;
	public Location bestLocation=null;
	private ArrayList<Location> locationList = new ArrayList<Location>();
	
	public Long bestTimer;
	
	public int cSchedule=1;
	
	Context context;
	
	TelephonyManager telephonyManager;
	
	public Long oldGpsTime=0l;
	
	 /* Called when the activity is first created. */
		@Override
   public IBinder onBind(Intent intent) {
      return null;
   }

   @SuppressLint("Wakelock")
@SuppressWarnings("deprecation")
@Override
   public void onCreate() {
	   //code to execute when the service is first created
	   context = MyLocationService.this;
	   
	   PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
	   PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
	   wl.acquire(); 
	   
	   Settings.System.putInt(getContentResolver(),
	   Settings.System.WIFI_SLEEP_POLICY,
	   Settings.System.WIFI_SLEEP_POLICY_NEVER);
	   
//	   TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//  	   devId = telephonyManager.getDeviceId();
//  	   devId = DbUtil.getSetting(context, Master.DEVID);

       devId = getDeviceImei();
	   
	   SendInterval = Master.INIT_SEND_INTERVAL;
       
       Long value = getSendInterval();
		
       if (value>=1000l) SendInterval = value;
       
 	   bestTimer = System.currentTimeMillis();
	   mlocListener =  new MyLocationListener();
	   GpsOn();
	   
	   if (Master.getStatGPS(context) == 1) {
		   gpsOn=true;
		   sendStat("ON");
		   
	   }
	   else {
		   gpsOn=false;
		   sendStat("OFF");
	   }
	   
	   startLocationUpdates();	     
	   new NewSchedule();	
      
   }

   //End OnCreate
   
   @Override
   public void onDestroy() { }
   
   @Override
   public int onStartCommand(Intent intent, int flags, int startId) {
       return START_STICKY;
   }
     
   @Override
   public void onStart(Intent intent, int startid) { }
   
   private Long getSendInterval() {
	   SettingDbManager db = new SettingDbManager(context);
       db.open();				
       String strValue = db.getSetting(Master.SEND_INTERVAL);
       db.close();
       Long value = 0l;
       if(strValue!=null) value = Long.parseLong(strValue);
       
       return value;
   }
   
   
   
   /* Class My Location Listener */ 	
	public class MyLocationListener implements LocationListener {
		@Override
		public void onLocationChanged(Location location) {
			try {
				if (location == null) return;
				if(location.isFromMockProvider()) return;
				if (location.getAccuracy() > 200f) return; 
		    	    	
			    currentLocation=location;
			    //Long past = System.currentTimeMillis()-cycleTimer;
			    Long pastBest = System.currentTimeMillis()-bestTimer;
			    //Log.i("Current Location ",currentLocation.getProvider() + " : " +currentLocation.getAccuracy()+  " : " + pastBest);
			    
			    if (bestLocation!=null) {
			    	oldGpsTime=bestLocation.getTime();// + 1000*60*60*Master.GMT;
			    	if(location.getAccuracy() < bestLocation.getAccuracy()) bestLocation = location;		    	
			    }
			    else {
			    	bestLocation = location;		    	
			    }			    
			    
			    String strGpsDate = DateUtil.strDate(oldGpsTime);
		   		
		   		DbUtil.saveSetting(context, "gpsdate", strGpsDate);
			   
			    //Log.i("Best Location :",bestLocation.getProvider() + " : " + bestLocation.getAccuracy());
			    if (pastBest  >=  60000l) {			    
			    	locationList.add(bestLocation);			    	
			    	bestTimer= System.currentTimeMillis();
			    	//Log.i("Best location at this time: ",String.valueOf(bestLocation.getAccuracy()) + " : " + bestLocation.getProvider() + " : " + locationList.size());
			    	bestLocation=null;
			    } 
		    }
		    catch(Exception e){}
		} 
	
		@Override
		public void onProviderDisabled(String provider) {
			if(provider.equalsIgnoreCase("gps") && gpsOn==true) {
				sendStat("OFF");
				gpsOn=false;
			}
		} 
		
		@Override
		public void onProviderEnabled(String provider) {
			if(provider.equalsIgnoreCase("gps") && gpsOn==false) {
				sendStat("ON");
				gpsOn=true;
			}
		} 
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras)	{}

	
	}/* End of Class MyLocationListener */ 
	
	private void sendStat(String stat) {
		String strDateTime = DateUtil.strDateTime(System.currentTimeMillis());
		
//		devId = DbUtil.getSetting(context, Master.DEVID);
		String message =Master.CMD_GPS + " " +
   		   		devId + ";" + 
   		   		stat + ";" + 
   		   		strDateTime;
		DbUtil.saveMsg(context,DbUtil.getGateway(context), message);
	}
	
	
		
	private void GpsOn(){
		//Persistently turned on the GPS
		try {
			LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	    	if(!mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			 String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
			    if(!provider.contains("gps")){ //if gps is disabled 
			        final Intent poke = new Intent();
			        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");  
			        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			        poke.setData(Uri.parse("3"));
			        sendBroadcast(poke); 
			    }
	    	}
		 } catch(Exception e){}
	}
	
	public void startLocationUpdates() {		
		try{
			mlocGPS = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			mlocGPS.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0, mlocListener);
		}catch(SecurityException ex){}
		
        try{
        	mlocNET = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        	mlocNET.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,0, mlocListener);
        }catch(SecurityException ex){}
        
	}
	
	///**********************************
	public class NewSchedule {
		final Handler handler = new Handler();
		public Timer t;
		
		public NewSchedule() {
	        t = new Timer();
	        t.schedule(new sendLocationTask(), 1000*60l, SendInterval);
		}
		
		public class sendLocationTask extends TimerTask {
			 @Override
			public void run() {
				 handler.post(new Runnable() {
			        @Override
					public void run() {
			        	GpsOn();
//		            	Time currentTime = new Time();		    					    				
//	    				currentTime.setToNow();
//	    				long elapseTime = Math.abs(currentTime.toMillis(true) - LastSent.toMillis(true));	
//	    				long timeInterval = SendInterval;	    				
//	    				if (elapseTime >= timeInterval ) {	    					
	    					//Send  Updates	
	    					try {
	    						saveUpdates();
	    						locationList.clear();
	    					}
	    				    catch(Exception e){
	    				    	Log.d("MyLoactionService","Exception Erro");
	    				    	e.printStackTrace();
	    				    }	
//	    				}		    					    			
		    			LastSent = SystemClock.elapsedRealtime();
			        }
			    });
			 }
		}
	}
	
	public void saveUpdates() {	
				
	   	Location loc=null;
	   	if (currentLocation != null) loc =currentLocation;
	   	if (!locationList.isEmpty()) loc = locationList.get(locationList.size()-1);	 
	   	
	   	Long sysTime = System.currentTimeMillis()/1000;
	   	
	   	String sysDateTime = DateUtil.strDateTime(System.currentTimeMillis());
	   	
//	   	while(devId == null){
//		   	if(DbUtil.getSetting(context, Master.DEVID).isEmpty() || DbUtil.getSetting(context, Master.DEVID) == null){
//		   		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//		   	    devId = telephonyManager.getDeviceId();
//		   	}
//	   	}
//	   	devId = DbUtil.getSetting(context, Master.DEVID);
	   	
	   	Integer battery = Master.getBatteryInfo(context);
	   	Integer statGPS = Master.getStatGPS(context);
	   	String message = Master.CMD_UPDATE + " " +
   				devId 		+ ";" + 
	   			statGPS 	+ ";" +
   				battery 	+ ";" +
   				sysDateTime + ";" +
   				"******* No GPS location data!";
	   	
	   	
	   	if (loc!=null) { 
	   		if(loc.getTime()!=oldGpsTime) {
		   		Long localTime = loc.getTime()/1000;
		   		if(loc.getProvider().contains("GPS")) {
		   			localTime = (loc.getTime()+ 1000*60*60*Master.GMT)/1000;
		   		}
		   		
		   		oldGpsTime=loc.getTime();
		   		message = Master.CMD_LOCATION + " " +
		   		   		devId + ";" +
		   		   		loc.getLatitude() 	+ ";" +
		   		   		loc.getLongitude() 	+ ";" +
		   		   		loc.getAltitude() 	+ ";" +
			   		   	loc.getAccuracy() 	+ ";" + 
		   		   		loc.getSpeed()  	+ ";" + 
			   		   	loc.getBearing()	+ ";" + 
			   		   	localTime			+ ";" + 
			   		   	loc.getProvider()	+ ";" + 
		   		   		sysTime; 
		   	}
		}	   	
	   	
	   	
	   	DbUtil.saveMsg(context,DbUtil.getGateway(context), message);
		
   }

	private String getDeviceImei(){
		String devId = DbUtil.getSetting(context, Master.DEVID);

		while(devId==null || devId.isEmpty()){

			TelephonyManager telephonyManager = (TelephonyManager)getSystemService(android.content.Context.TELEPHONY_SERVICE);
//			devId = telephonyManager.getDeviceId();
			devId = Master.getDevId2(context);
			DbUtil.saveSetting(context, Master.DEVID, devId);
		}

		return devId;
	}

}
