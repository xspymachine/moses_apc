package com.xpluscloud.mosesshell_davao;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.maps.model.LatLng;
import com.xpluscloud.mosesshell_davao.dbase.SettingDbManager;
import com.xpluscloud.mosesshell_davao.util.DbUtil;
import com.xpluscloud.mosesshell_davao.util.Master;
import com.xpluscloud.mosesshell_davao.util.WakeLocker;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FusedLocationService extends Service implements
				GoogleApiClient.ConnectionCallbacks, 
				GoogleApiClient.OnConnectionFailedListener,
				LocationListener{
	
	Context context;
	String devId;
	Long SendInterval;
	Long bestTimer;
	
	public long LastSent = 0;
	public static Location currentLocation=null;
	public Location bestLocation=null;
	private ArrayList<Location> locationList = new ArrayList<Location>();
	public Long oldGpsTime=0l;
	
	private GoogleApiClient mGoogleApiClient;
	public static final String TAG = "FuseLocationService";
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
	private LocationRequest mLocationRequest;
	

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mGoogleApiClient.connect();
	    return Service.START_STICKY;
	}
	
	protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
//		super.onCreate();
		context = FusedLocationService.this;
		
//		Toast.makeText(getApplicationContext(), "Starting Service",
//     		   Toast.LENGTH_LONG).show();
		
		createLocationRequest();
		mGoogleApiClient = new GoogleApiClient.Builder(this)
	        .addApi(LocationServices.API)
	        .addConnectionCallbacks(this)
	        .addOnConnectionFailedListener(this)
	        .build();


//        if (mGoogleApiClient != null) {
        	mGoogleApiClient.connect();
//        }
		
	   //PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE); 
	   //PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG"); 
	   //wl.acquire(); 
	   
	   Settings.System.putInt(getContentResolver(),
	   Settings.System.WIFI_SLEEP_POLICY,
	   Settings.System.WIFI_SLEEP_POLICY_NEVER);
	   
	   TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//  	   devId = telephonyManager.getDeviceId();
		devId = Master.getDevId2(context);
	   SendInterval = Master.INIT_SEND_INTERVAL;
       
       Long value = getSendInterval();
		
       if (value>=1000l) SendInterval = value;
       
 	   bestTimer = SystemClock.elapsedRealtime();
	   GpsOn();
	   new NewSchedule();	
		
	}
	
	private void startLocationRequest(){
		
//		mGoogleApiClient = new GoogleApiClient.Builder(this)
//        .addConnectionCallbacks(this)
//        .addOnConnectionFailedListener(this)
//        .addApi(LocationServices.API)
//        .build();
//		
//		// Create the LocationRequest object
//				mLocationRequest = LocationRequest.create()
//			        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//			        .setInterval(1 * 1000)        // 1 second, in milliseconds
//			        .setFastestInterval(500); // .5 second, in milliseconds
				
				mLocationRequest = LocationRequest.create();
				mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
				mLocationRequest.setInterval(1 * 1000);
				mLocationRequest.setFastestInterval(500);

		        mGoogleApiClient = new GoogleApiClient.Builder(context)
		                .addApi(LocationServices.API)
		                .addConnectionCallbacks(this)
		                .addOnConnectionFailedListener(this)
		                .build();

		        if (mGoogleApiClient != null) {
		        	mGoogleApiClient.connect();
		        }
	}
	
	private Long getSendInterval() {
		   SettingDbManager db = new SettingDbManager(context);
	       db.open();				
	       String strValue = db.getSetting(Master.SEND_INTERVAL);
	       db.close();
	       Long value = 0l;
	       if(strValue!=null) value = Long.parseLong(strValue);
	       
	       return value;
	       
	      
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

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
		handleNewLocation(location);
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		// TODO Auto-generated method stub
	        Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		Log.i(TAG, "Location services connected.");
		PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
		
		Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
		
		if (location == null) {  
		    // Blank for a moment...
			LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
			Executors.newScheduledThreadPool(1).schedule(new Runnable() {
                @Override
                public void run() {
                	LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
                            FusedLocationService.this);
                }
            }, 1000*60, TimeUnit.MILLISECONDS);
		}
		else {
		    handleNewLocation(location);
		};
	}

	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub
		Log.i(TAG, "Location services suspended.");
	}
	
	private void handleNewLocation(Location location) {
	    Log.w(TAG, location.getAccuracy()+" changeMoSeS1 "+location.toString());
	    
	    double currentLatitude = location.getLatitude();
	    double currentLongitude = location.getLongitude();
	    
//	    LatLng latLng = new LatLng(currentLatitude, currentLongitude);

	    try {	
			if (location == null) return;
//			if (location.getAccuracy() > 200f) return; 
			
			WakeLocker.acquire(context);
	    	    	
		    currentLocation=location;
		    //Long past = System.currentTimeMillis()-cycleTimer;
		    Long pastBest = SystemClock.elapsedRealtime()-bestTimer;
		    //Log.i("Current Location ",currentLocation.getProvider() + " : " +currentLocation.getAccuracy()+  " : " + pastBest);
		    
		    if (bestLocation!=null) {
		    	oldGpsTime=bestLocation.getTime();// + 1000*60*60*Master.GMT;
		    	if(location.getAccuracy() < bestLocation.getAccuracy()) bestLocation = location;		    	
		    }
		    else {
		    	bestLocation = location;		    	
		    }			    
		    
		    String strGpsDate = DbUtil.strDate(oldGpsTime);
	   		
	   		DbUtil.saveSetting(context, "gpsdate", strGpsDate);
		   
		    //Log.i("Best Location :",bestLocation.getProvider() + " : " + bestLocation.getAccuracy());
		    if (pastBest  >=  60000l) {			    
		    	locationList.add(bestLocation);			    	
		    	bestTimer= SystemClock.elapsedRealtime();
		    	//Log.i("Best location at this time: ",String.valueOf(bestLocation.getAccuracy()) + " : " + bestLocation.getProvider() + " : " + locationList.size());
		    	bestLocation=null;
		    } 
		    
//		    saveUpdates();
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    }		 	    
	    
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
				 public void run() {
					 handler.post(new Runnable() {
				        public void run() {
				        	GpsOn();
			            	//Time currentTime = new Time();		    					    				
		    				//currentTime.setToNow();
		    				//long elapseTime = SystemClock.elapsedRealtime() - LastSent;	
		    				//long timeInterval = SendInterval;	 
		    				Log.e("SendInterval",""+SendInterval);
		    				//if (elapseTime >= timeInterval ) {	    					
		    					//Send  Updates	
		    					try {
		    						saveUpdates();
		    						locationList.clear();
		    					}
		    				    catch(Exception e){
		    				    	Log.d("MyLoactionService:sendLoactionTask","Exception Error");
		    				    	e.printStackTrace();
		    				    }	
		    				//}		    					    			
			    			LastSent = SystemClock.elapsedRealtime();
				        }
				    });
				 }
			}
		}
		
		public void saveUpdates() {	   
			
		   	Location loc=null;
		   	if (currentLocation != null) loc =currentLocation;
		   	else{
		   		// Blank for a moment...
				LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);				
		   	}
		   	if (!locationList.isEmpty()) loc = locationList.get(locationList.size()-1);	 
		   	
		   	Long sysTime = System.currentTimeMillis()/1000;
		   	
		   	while(devId == null){
			   	if(DbUtil.getSetting(context, Master.DEVID).isEmpty() || DbUtil.getSetting(context, Master.DEVID) == null){
			   		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//			   	    devId = telephonyManager.getDeviceId();
					devId = Master.getDevId2(context);
			   	}
		   	}
		   	
		   	Integer battery = Master.getBatteryInfo(context);
		   	Integer statGPS = 1;
		   	String message = Master.CMD_UPDATE + " " +
	   				devId + ";" 
	   				+ statGPS + ";" +
	   				+ battery + ";" +
	   				+ sysTime + ";" +
	   				"******* No GPS location data!";
		   	
		   	
		   	if (loc!=null) { 
		   		//if(loc.getTime()!=oldGpsTime) {
			   		Long localTime = System.currentTimeMillis()/1000;//loc.getTime()/1000;
			   		//if(loc.getProvider().contains("GPS")) {
			   		//	localTime = (loc.getTime()+ 1000*60*60*Master.GMT)/1000;
			   		//}
			   		
			   		float accuracy = (float) 10.0;
			   		
			   		oldGpsTime=loc.getTime();
			   		message = Master.CMD_LOCATION + " " +
			   		   		devId + ";" +
			   		   		loc.getLatitude() 	+ ";" +
			   		   		loc.getLongitude() 	+ ";" +
			   		   		loc.getAltitude() 	+ ";" +
			   		   		accuracy			+ ";" + 
				   		   	//loc.getAccuracy() 	+ ";" + 
			   		   		loc.getSpeed()  	+ ";" + 
				   		   	loc.getBearing()	+ ";" + 
				   		   	localTime			+ ";" + 
				   		   	loc.getProvider()	+ ";" + 
			   		   		sysTime; 
			   	//}
			}	   	
		   	
		   	
		   	DbUtil.saveMsg(context,DbUtil.getGateway(context), message);
			
	   }	
		
		@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			Intent intent = new Intent("restartService");
			String serviceClass = "com.xplus.xpdes.FusedLocationService";
			intent.putExtra("serviceClass", serviceClass); 			
			sendBroadcast(intent);
			super.onDestroy();
		}
		
		@Override
		public void onTaskRemoved(Intent rootIntent) {
			// TODO Auto-generated method stub
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
	        	this.onDestroy();
	        	super.onTaskRemoved(rootIntent);
	        }      
		}
		
}
