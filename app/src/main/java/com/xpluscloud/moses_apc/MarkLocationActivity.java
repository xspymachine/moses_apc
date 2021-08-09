package com.xpluscloud.moses_apc;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xpluscloud.moses_apc.dbase.CustomerDbManager;
import com.xpluscloud.moses_apc.dbase.MarkedLocationDbManager;
import com.xpluscloud.moses_apc.getset.MarkedLocation;
import com.xpluscloud.moses_apc.util.DbUtil;
import com.xpluscloud.moses_apc.util.LayoutUtil;
import com.xpluscloud.moses_apc.util.Master;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MarkLocationActivity extends AppCompatActivity {
	
	public String devId;
	public String customerCode="";
	public String customerName="";
	public String customerAddress="";
	
	static LocationManager mlocGPS;
	static LocationManager mlocNET;
	static LocationListener mlocListener;
	
	
	TextView tvGPSFix;
	TextView tvGPSTime;
	TextView tvTitle;
	TextView tvGPSStatus;
	Button btSubmitIO;
	
	Location gpsFix;
	
	Context context;
	
	public Time LastSent = new Time();
		
	public boolean submitted; 
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);       
       context = MarkLocationActivity.this;
	 
       setContentView(R.layout.activity_inout);
	    
       submitted=false;
       
		Bundle b = getIntent().getExtras();
		devId				=b.getString("devId");
		customerCode		=b.getString("customerCode");
		customerName		=b.getString("customerName");
		customerAddress		=b.getString("customerAddress");
		
		
		setTitle("X+D - Mark Location");
	    
		TextView tvCName = (TextView) findViewById(R.id.tvCName);
		TextView tvAddress = (TextView) findViewById(R.id.tvAddress);
		RelativeLayout rLayout = (RelativeLayout) findViewById(R.id.timeInReminders);
		rLayout.setVisibility(View.GONE);
		ImageView ivIcon = (ImageView) findViewById(R.id.bt_scname);
//		Drawable dr = context.getResources().getDrawable(R.drawable.assets_marklocation);
//		Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
//		// Scale it to 50 x 50
//		Drawable d = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, 60, 60, true));
		Drawable d = new BitmapDrawable(context.getResources(), LayoutUtil.decodeSampledBitmapFromResource(context.getResources(),R.drawable.assets_marklocation, 60, 60));
//		ivIcon.setImageDrawable(d);
		ivIcon.setImageResource(R.drawable.ic_mark);
		
		try {
			tvCName.setText(customerName + "(" + customerCode + ")");			
	   		tvAddress.setText(customerAddress);
		}
		catch (Exception e) {
			Log.e("ERROR"," Error in Customer...");
		}
       
       try {	      
    	   tvGPSStatus = (TextView) findViewById(R.id.tv_status);
	       tvGPSFix = (TextView) findViewById(R.id.tv_gpsfix);
	       tvGPSTime = (TextView) findViewById(R.id.tvDeviceTime);
	       if (Master.getStatGPS(context)==1) {
	   			tvGPSStatus.setText("GPS ON");
	   			//tvGPSStatus.setTextColor(0x00A400);
	   		}
	   		else {
	   			tvGPSStatus.setText("GPS OFF");
	   			//tvGPSStatus.setTextColor(0xD50000);
	   		}
	       
	       btSubmitIO = (Button) findViewById(R.id.bt_submitio);
	       btSubmitIO.setText("Mark Location");
	              	       
	       gpsFix=null;
	       
	       mlocListener =  new mLocationListener();
	       startLocationUpdates(); 
	    
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
    }
	
	
	
	public  void startLocationUpdates() {		
		try{			
			mlocGPS = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        	mlocGPS.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, mlocListener);
		}catch(SecurityException ex){}
		
        try{
        	mlocNET = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        	mlocNET.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0, mlocListener);
        }catch(SecurityException ex){}
        
        Log.e("LocationUpdates: ", "*** Started ***" );
	}
	
	public void stopLocationUpdates() { 
		Log.e("LocationUpdates: ", "*** Disabled ***" );
		try {
			mlocGPS.removeUpdates(mlocListener); 
		 }catch(SecurityException ex){}
		
		
		try {
			mlocNET.removeUpdates(mlocListener); 
		}catch(SecurityException ex){}
		
	} 
	
	private void history(){
		Intent i = new Intent(this, MarkLocationListActivity.class);
		startActivity(i);		
	}
	
	
	public void save() {		
		if (gpsFix==null) {
			btSubmitIO.setEnabled(true);
//			Toast.makeText(getApplicationContext(),"Please wait for device to get GPS Location",Toast.LENGTH_LONG ).show();	
			DbUtil.makeToast(LayoutInflater.from(context), "Please wait for device to get GPS Location", context,
    				(ViewGroup) findViewById(R.id.custom_toast_layout),1);
			return;
		}		
				
		String task = "Marking location...";
//		Toast.makeText(getApplicationContext(), task  ,Toast.LENGTH_LONG ).show();	
		
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
		
		if(loc.getProvider().contains("GPS") ) localTime 	= gpsTime + 1000*60*60*Master.GMT;
		else localTime = gpsTime;		
		
		Long strTime= localTime/1000;
		
		MarkedLocationDbManager db = new MarkedLocationDbManager(context);
		db.open();
		
		MarkedLocation c = new MarkedLocation();
				
		c.setGpstime(String.valueOf(strTime));
		c.setCustomerCode(customerCode);		
		c.setLatitude(lat);
		c.setLongitude(lng);
		c.setAccuracy(accuracy);
		c.setProvider(provider);
		c.setStatus(0);
		
		long id = db.Add(c);		
		db.close();
		
		devId = DbUtil.getSetting(context, Master.DEVID);
		String message = Master.CMD_MTL + " " +
					devId + ";" +
					customerCode + ";" +					
					lat + ";" + 
					lng + ";" + 
					accuracy + ";" + 
					provider + ";" + 
					strTime + ";" + 					
					id;
			
		DbUtil.saveMsg(context,DbUtil.getGateway(context), message);

		CustomerDbManager cdb = new CustomerDbManager(context);
		cdb.open();
		cdb.updateLatLng(customerCode, lat, lng);
		cdb.close();
		stopLocationUpdates();
		submitted=true;
		finish();
	} 
	
	
	/* Class My Location Listener */ 	
	public class mLocationListener implements LocationListener {
		@Override
		public void onLocationChanged(Location location) {
			try {
				if(location==null) return;
				if(location.isFromMockProvider()) return;
				if (location.getAccuracy()> 200) {
					tvGPSFix.setText("-1");
					return;
				}				
				gpsFix = location;
				
				String cAccuracy = String.valueOf((int) gpsFix.getAccuracy());
				tvGPSFix.setText((cAccuracy));				
				Log.i("IO", String.valueOf((int) gpsFix.getAccuracy()));
				SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss a", Locale.US);
			    String gpsTime = df.format(gpsFix.getTime());
				tvGPSTime.setText(gpsTime);	
			}catch(Exception e){
				Log.e("Error", "Location update");
			}
		} 		
		@Override
		public void onProviderDisabled(String provider) {
			//Toast.makeText( getApplicationContext(), "Gps Disabled",Toast.LENGTH_SHORT ).show();			
		} 
		
		@Override
		public void onProviderEnabled(String provider) {
			//Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show(); 
		} 
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras)	{
		}

	
	}/* End of Class MyLocationListener */ 
		
}



