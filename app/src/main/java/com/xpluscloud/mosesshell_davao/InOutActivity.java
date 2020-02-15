package com.xpluscloud.mosesshell_davao;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xpluscloud.mosesshell_davao.dbase.CustomerDbManager;
import com.xpluscloud.mosesshell_davao.dbase.InOutDbManager;
import com.xpluscloud.mosesshell_davao.getset.Customer;
import com.xpluscloud.mosesshell_davao.getset.TimeInOut;
import com.xpluscloud.mosesshell_davao.util.DateUtil;
import com.xpluscloud.mosesshell_davao.util.DbUtil;
import com.xpluscloud.mosesshell_davao.util.DialogManager;
import com.xpluscloud.mosesshell_davao.util.Master;


public class InOutActivity extends AppCompatActivity {

	public String devId;
	public String customerCode="";
	public String customerName="";
	public String customerAddress="";

	public String inout;

	static LocationManager mlocGPS;
	static LocationManager mlocNET;
	static LocationListener mlocListener;

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


	@Override
	public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       context = InOutActivity.this;

       setContentView(R.layout.activity_inout);


       submitted=false;

		Bundle b = getIntent().getExtras();
		devId				=b.getString("devId");
		customerCode		=b.getString("customerCode");
		customerName		=b.getString("customerName");
		customerAddress		=b.getString("customerAddress");
		inout				=b.getString("inout");

		setCustomer(customerCode);

		setTitle("In/Out - Time" + inout);

		TextView tvCName = (TextView) findViewById(R.id.tvCName);
		TextView tvAddress = (TextView) findViewById(R.id.tvAddress);

		tvGPSStatus = (TextView) findViewById(R.id.tv_status);
		tvDevTime = (TextView) findViewById(R.id.tvDeviceTime);
		ImageView ivIcon = (ImageView) findViewById(R.id.bt_scname);
//		Drawable dr = context.getResources().getDrawable(R.drawable.assets_timein);
//		Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
		// Scale it to 50 x 50
//		Drawable d = new BitmapDrawable(context.getResources(), LayoutUtil.decodeSampledBitmapFromResource(context.getResources(),R.drawable.ic_timein, 60,60));
//		ivIcon.setImageDrawable(d);
		ivIcon.setImageResource(R.drawable.ic_timein);
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
//	       DbUtil.changeDrawableColor("#8DC33D", btSubmitIO);

	       btMerchCheck = (Button) findViewById(R.id.btMerch);

	       if(inout.equalsIgnoreCase("Out")) {
//	    	    dr = context.getResources().getDrawable(R.drawable.assets_timeout);
//		   		bitmap = ((BitmapDrawable) dr).getBitmap();
//		   		// Scale it to 50 x 50
//		   		d = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, 60, 60, true));
//               d = new BitmapDrawable(context.getResources(), LayoutUtil.decodeSampledBitmapFromResource(context.getResources(),R.drawable.ic_out, 60,60));
//		   		ivIcon.setImageDrawable(d);
			   ivIcon.setImageResource(R.drawable.ic_out);

		   		btSubmitIO.setTextColor(Color.parseColor("#E21717"));
//		   	   DbUtil.changeDrawableColor("#E21717", btSubmitIO);

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

	       mlocListener =  new mLocationListener();
	       startLocationUpdates();

	       btSubmitIO.setOnClickListener(new OnClickListener() {
		        @Override
				public void onClick(View v) {
		        	saveIO();
//					if(inout.contains("OUT")) {
//						saveIO();
//					}else {
//						if (gpsFix == null) {
//							DbUtil.makeToast(LayoutInflater.from(context), "Searching location please wait...", context,
//									(ViewGroup) findViewById(R.id.custom_toast_layout), 0);
//							return;
//						}
//						CustomerDbManager db = new CustomerDbManager(context);
//						db.open();
//						Customer c = db.getCustomerByCode(customerCode);
//						db.close();
//
//						Location loc1 = new Location("");
//						loc1.setLatitude(c.getLatitude());
//						loc1.setLongitude(c.getLatitude());
//
//						Location loc2 = new Location("");
//						loc2.setLatitude(gpsFix.getLatitude());
//						loc2.setLongitude(gpsFix.getLatitude());
//
//						float distanceInMeters = loc1.distanceTo(loc2);
//						Log.e("in", inout + distanceInMeters);
//						if (distanceInMeters > 20) {
//							DialogManager.showAlertDialog(InOutActivity.this,
//									"Time IN unacceptable!",
//									"You have to time in while you are near the customer’s marked location GPS coordinates.", false);
//						} else saveIO();
//					}
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
		long gpsTime = 0;
		
		Long ioTime;
		
		if (gpsFix!=null) {
			Location loc = gpsFix;
			lat 	= loc.getLatitude();
			lng 	= loc.getLongitude();
			gpsTime = loc.getTime();
			Log.e("gpsTime",""+gpsTime+"--"+loc.getProvider());
			if(loc.getProvider().contains("GPS") ) ioTime 	= gpsTime + 1000*60*60*Master.GMT;
			else ioTime = gpsTime;	
			ioTime = gpsTime;		
		}
		else {
			ioTime = sysTime;
		}
		
		if(ioTime<100000000l) ioTime = sysTime;
		Log.e("ioTime",""+ioTime);
		Log.e("sysTime",""+sysTime);
		Long strTime= ioTime/1000;
		
		//Save to "InOut" table
		
		InOutDbManager db = new InOutDbManager(context);
		db.open();
		
		TimeInOut c = new TimeInOut();
		
		
		Log.i("In/Out",""+inout);
		int io=0;
		if (inout.equalsIgnoreCase("IN")) io = 1;
		
		c.setDateTime(String.valueOf(strTime));
		c.setCustomerCode(customerCode);
		c.setInout(io);
		c.setLatitude(lat);
		c.setLongitude(lng);
		c.setStatus(0);
		
//		Log.e("inout","tracert");
		long id = db.Add(c);		
		db.close();
		
		sysTime = sysTime/1000;

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
			
		stopLocationUpdates();
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
				
			    String gpsTime = DateUtil.phShortDateTime(gpsFix.getTime());
				
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
		
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode != RESULT_OK) return;
		
		switch(requestCode) {
			case 1:
					btMerchCheck.setClickable(false);
//					Log.e("check","done");
				break;	
		}
		
		 
	}//onActivityResult

	private void setCustomer(String ccode){
		CustomerDbManager db = new CustomerDbManager(context);
		db.open();
		Customer info = db.getCustomer(ccode);
		db.close();

		customerName		=info.getName();
		customerAddress		=info.getAddress();
	}
}



