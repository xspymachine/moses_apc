package com.xpluscloud.moses_apc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
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
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.xpluscloud.moses_apc.dbase.InOutDbManager;
import com.xpluscloud.moses_apc.getset.TimeInOut;
import com.xpluscloud.moses_apc.util.DateUtil;
import com.xpluscloud.moses_apc.util.DbUtil;
import com.xpluscloud.moses_apc.util.DialogManager;
import com.xpluscloud.moses_apc.util.Master;

import java.util.Timer;
import java.util.TimerTask;

public class InOutFusedActivity extends Activity implements
			GoogleApiClient.ConnectionCallbacks, 
			GoogleApiClient.OnConnectionFailedListener,
			LocationListener{		
	
	public String devId;
	public String customerCode="";
	public String customerName="";
	public String customerAddress="";
	
	public String inout;

	static LocationManager mlocGPS;
	static LocationManager mlocNET;
	
	TextView tvGPSStatus;
	TextView tvGPSFix;
	TextView tvGPSTime;
	TextView tvDevTime;
	
	TextView tvTitle;
	Button btSubmitIO;
	Button btMerchCheck;
	
	Location gpsFix;
	
	Context context;
	
	public Time LastSent = new Time();
		
	public boolean submitted; 
	
	LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
	
	protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);       
       context = InOutFusedActivity.this;
	 
       setContentView(R.layout.activity_inout);
       
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
		inout				=b.getString("inout");
		
		setTitle("In/Out - Time" + inout);
	    
		TextView tvCName = (TextView) findViewById(R.id.tvCName);
		TextView tvAddress = (TextView) findViewById(R.id.tvAddress);
		
		tvGPSStatus = (TextView) findViewById(R.id.tv_status);
		tvDevTime = (TextView) findViewById(R.id.tvDeviceTime);
		try {
			tvCName.setText(customerName);			
	   		tvAddress.setText(customerAddress);
	   		
	   		if (Master.getStatGPS(context)==1) {
	   			tvGPSStatus.setText("GPS ON");
	   			//tvGPSStatus.setTextColor(0x00A400);
	   		}
	   		else {
	   			tvGPSStatus.setText("GPS OFF");
	   			//tvGPSStatus.setTextColor(0xD50000);
	   		}
	   		
	   		long iDate = System.currentTimeMillis();
	   		tvDevTime.setText(DateUtil.phShortDateTime(iDate));
	   		
	   		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
       
       try {	      
	       tvGPSFix = (TextView) findViewById(R.id.tv_gpsfix);
	       
	       tvGPSTime = (TextView) findViewById(R.id.tv_gpstime);
	       
	       btSubmitIO = (Button) findViewById(R.id.bt_submitio);
	       
	       btMerchCheck = (Button) findViewById(R.id.btMerch);
	       
	       if(inout.equalsIgnoreCase("Out")) {
	    	   RelativeLayout timeinReminder = (RelativeLayout) findViewById(R.id.timeInReminders);
	    	   timeinReminder.setVisibility(View.GONE);
	    	   
	    	   RelativeLayout timeoutReminder = (RelativeLayout) findViewById(R.id.timeOutReminders);
	    	   timeoutReminder.setVisibility(View.VISIBLE);
	    	   
	    	   btMerchCheck.setVisibility(View.VISIBLE);
	    	   btMerchCheck.setOnClickListener(new OnClickListener() {
			        @Override
					public void onClick(View v) {
//			        	btMerchCheck.setClickable(false);
			        	Intent intent = new Intent(context, MerchandisingActivity.class);
			    		Bundle b = new Bundle();
			    		b.putString("merchandising", "merchandising");
			    		b.putString("devid", devId);
			    		b.putString("ccode", customerCode);
			    		
			    		intent.putExtras(b); 
			    		startActivityForResult(intent,1);
			        } 
			    });
	       }
	       
	                             
	       
	       btSubmitIO.setText("Time "+inout);
	              	       
	       gpsFix=null;
	       	    
	       btSubmitIO.setOnClickListener(new OnClickListener() {
		        @Override
				public void onClick(View v) {
		            saveIO();
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
//	    	stopLocationUpdates();
	    	finish();
	        return true; 
	    } 
	    return super.onKeyDown(keyCode, event); 
	} 	
		
	private void history(){
		Intent i = new Intent(this, IOListActivity.class);
		startActivity(i);		
	}
	
	
	public void saveIO() {
		
		RelativeLayout cbGroup;
		if(inout.equalsIgnoreCase("In")) {
			cbGroup = (RelativeLayout) findViewById(R.id.timeInReminders);
			if (!allChecked(cbGroup)) {
				return;
			}			
		}
		else {
			cbGroup = (RelativeLayout) findViewById(R.id.timeOutReminders);
			if (!allChecked(cbGroup)) {
				return;
			}
			if(btMerchCheck.isClickable()){
				DialogManager.showAlertDialog(context,
    	                "Merchandising Checklists", 
    	                "This button must be clicked.", false); 
				return;
			}
		}
		
		
		btSubmitIO.setEnabled(false);
		Long sysTime = System.currentTimeMillis();
		//DialogManager.showAlertDialog(context, "Time " + inout  , "In/Out transaction has been processed.", false);
		
		Toast.makeText(getApplicationContext(), "Processing Time "+inout+"..."  , Toast.LENGTH_LONG ).show();
		
		Double lat=0.00;
		Double lng=0.00;
		long gpsTime;
		
		Long ioTime;
		
		if (gpsFix!=null) {
			Location loc = gpsFix;
			lat 	= loc.getLatitude();
			lng 	= loc.getLongitude();
			gpsTime = loc.getTime();
			if(loc.getProvider().contains("GPS") ) ioTime 	= gpsTime + 1000*60*60*Master.GMT;
			else ioTime = gpsTime;
		}
		else {
			ioTime = sysTime;
		}
		
		Long strTime= ioTime/1000;
		
		//Save to "InOut" table
		
		InOutDbManager db = new InOutDbManager(context);
		db.open();
		
		TimeInOut c = new TimeInOut();

		sysTime = System.currentTimeMillis()/1000;
		strTime = System.currentTimeMillis()/1000;
		
		Log.i("In/Out",""+inout);
		int io=0;
		if (inout.equalsIgnoreCase("IN")) io = 1;
		
		c.setDateTime(String.valueOf(strTime));
		c.setCustomerCode(customerCode);
		c.setInout(io);
		c.setLatitude(lat);
		c.setLongitude(lng);
		c.setStatus(0);
		
		long id = db.Add(c);		
		db.close();
		
		
		devId = DbUtil.getSetting(context, Master.DEVID);
				
		String message = Master.CMD_INOUT + " " +
					devId + ";" +
					customerCode + ";" +
					inout + ";" +
					lat + ";" + 
					lng + ";" + 
					strTime + ";" + 
					sysTime + ";" + 
					id;
			
		DbUtil.saveMsg(context,DbUtil.getGateway(context), message);
			
//		stopLocationUpdates();
		submitted=true;
		
		finish();
	} 
	
	private boolean allChecked(RelativeLayout cbGroup) {
		for(int i=0;i<cbGroup.getChildCount();i++){						
		       View child=cbGroup.getChildAt(i);
		       
		       if (child instanceof CheckBox){
		    	   if (!((CheckBox) child).isChecked()) {
		    		   DialogManager.showAlertDialog(context, 
		    	                "All Items Must Be Checked!", 
		    	                "Please do the steps and check when done.", false); 
		    		   return false;
		    		   
		    	   }
		       }
		}
		return true;
	}
				
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode != RESULT_OK) return;
		
		switch(requestCode) {
			case 1:
					btMerchCheck.setClickable(false);
//					Log.e("check","done");
				break;	
		}
		
		 
	}//onActivityResult

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		handleLocation(location);
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		startLocationUpdates(); 
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		try{
        mGoogleApiClient.connect();
		}catch(Exception e){}
	}
	
	@Override
    protected void onStop(){
       super.onStop();
       mGoogleApiClient.disconnect();
    }
	
	protected void startLocationUpdates() {
		try{
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
		}catch(SecurityException e){}
                
    }
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		stopLocationUpdates();
	}
	
	protected void stopLocationUpdates() {
		try{
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
		}catch(Exception e){}
    }
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try{
			if (mGoogleApiClient.isConnected()) {
	            startLocationUpdates();
	        }
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
			
		    String gpsTime = DateUtil.phShortDateTime(gpsFix.getTime());
			
		    tvGPSTime.setText(gpsTime);	
		}catch(Exception e){
			Log.e("Error", "Location update");
		}
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
			        			LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, InOutFusedActivity.this);
			        		}catch(Exception e){
			        			
			        		}
			    		}
		    						    			
			        }
			    });
			 }
		}
	}
}



