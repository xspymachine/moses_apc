package com.xpluscloud.mosesshell_davao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.xpluscloud.mosesshell_davao.dbase.MarkedLocationDbManager;
import com.xpluscloud.mosesshell_davao.getset.MarkedLocation;
import com.xpluscloud.mosesshell_davao.util.DbUtil;
import com.xpluscloud.mosesshell_davao.util.Master;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MarkFusedLocationActivity extends Activity implements
			GoogleApiClient.ConnectionCallbacks, 
			GoogleApiClient.OnConnectionFailedListener,
			LocationListener{		
	
	public String devId;
	public String customerCode="";
	public String customerName="";
	public String customerAddress="";
	
	private GoogleApiClient mGoogleApiClient;
	public static final String TAG = "MarkFusedLocationActivity";
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
	private LocationRequest mLocationRequest;
	
	TextView tvGPSFix;
	TextView tvGPSTime;
	TextView tvTitle;
	Button btSubmitIO;
	
	Location gpsFix;
	
	Context context;
	
	public Time LastSent = new Time();
		
	public boolean submitted; 
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);       
       context = MarkFusedLocationActivity.this;
	 
       setContentView(R.layout.mark_location);
       
       createLocationRequest();
       mGoogleApiClient = new GoogleApiClient.Builder(this)
               .addApi(LocationServices.API)
               .addConnectionCallbacks(this)
               .addOnConnectionFailedListener(this)
               .build();
	    
       submitted=false;
       
		Bundle b = getIntent().getExtras();
		devId				=b.getString("devId");
		customerCode		=b.getString("customerCode");
		customerName		=b.getString("customerName");
		customerAddress		=b.getString("customerAddress");
		
		
		setTitle("X+D - Mark Location");
	    
		TextView tvCName = (TextView) findViewById(R.id.tvCName);
		TextView tvAddress = (TextView) findViewById(R.id.tvAddress);
		
		try {
			tvCName.setText(customerName + "(" + customerCode + ")");			
	   		tvAddress.setText(customerAddress);
		}
		catch (Exception e) {
			Log.e("ERROR"," Error in Customer...");
		}
       
       try {	      
	       tvGPSFix = (TextView) findViewById(R.id.tv_gpsfix);
	       tvGPSTime = (TextView) findViewById(R.id.tv_gpstime);
	       
	       btSubmitIO = (Button) findViewById(R.id.bt_submitio);
	              	       
	       gpsFix=null;     
	       
	    
	       btSubmitIO.setOnClickListener(new OnClickListener() {
		        @Override
				public void onClick(View v) {
		            Log.v("Button Clicked!", "btSubmitIO");
		            btSubmitIO.setEnabled(false);
		            save();
		        } 
		    });
	       
       }catch (Exception e) {
           Log.e("ERROR"," Error in transporting contact");}
	       
	   new NewSchedule();   
       
	 }
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.action_menu, menu);
       return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {		    
	    	case R.id.history:
	    		history();
	    		break;	    
	    }
	    return true;
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
	    	stopLocationUpdates();
	    	finish();
	        return true; 
	    } 
	    return super.onKeyDown(keyCode, event); 
	} 
	
	@Override
    protected void onStop(){
       super.onStop();
       Log.d(TAG, "onStop fired ..............");
       mGoogleApiClient.disconnect();
       Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
    }
	
	private void history(){
		Intent i = new Intent(this, MarkLocationListActivity.class);
		startActivity(i);		
	}
	
	
	public void save() {		
		if (gpsFix==null) {
			btSubmitIO.setEnabled(true);
			return;
		}		
				
		String task = "Marking location...";
		Toast.makeText(getApplicationContext(), task  , Toast.LENGTH_LONG ).show();
		
		Double lat=0.00;
		Double lng=0.00;
		Float accuracy=0f;
		String provider="";
		long gpsTime;
		Long localTime=0l;
		
		Location loc = gpsFix;
		lat 	= loc.getLatitude();
		lng 	= loc.getLongitude();
		accuracy= loc.getAccuracy();
		provider= loc.getProvider();
		gpsTime = loc.getTime();		
		
		if(loc.getProvider().contains("GPS") ) localTime 	= gpsTime + 1000*60*60* Master.GMT;
		else localTime = gpsTime;		
		
		Long strTime= localTime/1000;
		
		MarkedLocationDbManager db = new MarkedLocationDbManager(context);
		db.open();
		
		MarkedLocation c = new MarkedLocation();
		
		strTime = System.currentTimeMillis()/1000;
				
		c.setGpstime(String.valueOf(strTime));
		c.setCustomerCode(customerCode);		
		c.setLatitude(lat);
		c.setLongitude(lng);
		c.setAccuracy(accuracy);
		c.setProvider(provider);
		c.setStatus(0);
		
		long id = db.Add(c);		
		db.close();
		
		float accuracy2 = (float) 10.0;
		devId = DbUtil.getSetting(context, Master.DEVID);
		
		
		String message = Master.CMD_MTL + " " +
					devId + ";" +
					customerCode + ";" +					
					lat + ";" + 
					lng + ";" + 
					accuracy2 + ";" + 
					provider + ";" + 
					strTime + ";" + 					
					id;
			
		DbUtil.saveMsg(context,DbUtil.getGateway(context), message);
		
		stopLocationUpdates();
		submitted=true;
		finish();
		
	} //	Unimplemented methods of FusedLocation
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		Log.i(TAG, "Mark Location change.");
		handleLocation(location);
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		Log.i(TAG, "Mark Location services connected.");
		startLocationUpdates(); 
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		
	}
	
	protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
		
	protected void startLocationUpdates() {
		try{
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "Location update started ..............: ");
		}catch(Exception e){}
                
    }
			
	
	protected void stopLocationUpdates() {
		try{
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d(TAG, "Location update stopped .......................");
		}catch(Exception e){}
    }
	
	private void handleLocation(Location location){
		try {

			if (location==null) {
				tvGPSFix.setText("-1");
				return;
			}				
			gpsFix = location;
			
			String cAccuracy = String.valueOf((int) gpsFix.getAccuracy());
			tvGPSFix.setText((cAccuracy));			
			SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss a", Locale.US);
		    String gpsTime = df.format(gpsFix.getTime());
			tvGPSTime.setText(gpsTime);	
		}catch(Exception e){
			Log.e("Error", "Location update");
		}
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d(TAG, "onStart fired ..............");
        mGoogleApiClient.connect();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try{
			if (mGoogleApiClient.isConnected()) {
	            startLocationUpdates();
	            Log.d(TAG, "Location update resumed .....................");
	        }
		}catch(Exception e){}
	}
	
	public class NewSchedule {
		final Handler handler = new Handler();
		public Timer t;
		
		public NewSchedule() {
	        t = new Timer();
	        t.schedule(new sendLocationTask(), 1000*60l, 1000*60l);
		}
		
		public class sendLocationTask extends TimerTask {
			 public void run() {
				 handler.post(new Runnable() {
			        public void run() {
			        	
			        	if (gpsFix == null) {  
			    		    // Blank for a moment...
			        		try{
			        			LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, MarkFusedLocationActivity.this);
			        		}catch(Exception e){}
			    		}
		    						    			
			        }
			    });
			 }
		}
	}
	
}



