package com.xpluscloud.mosesshell_davao;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xpluscloud.mosesshell_davao.dbase.CustomerDbManager;
import com.xpluscloud.mosesshell_davao.getset.CustomerData;

import java.util.ArrayList;

public class ProfileListActivity extends ListActivity {
	
	static final String[] PROF_LIST =
            new String[] {
				"Owner Profile"
			};
	
	Context context=ProfileListActivity.this;
	
	public final int OWNER		= 0;
	public final int PURCHASER	= 1;
	
	String devId           ="";
	String customerCode	   ="";
	String customerAddress ="";
	String customerName    = "";
	
	String oName	 ="";
	String oCstatus  ="";
	String oBday 	 = "";
	String oInterest = "";
	String oPhone 	 = "";
	String oEmail 	 = "";

	String average = "";
	String loan    = "";
	String freq	   = "";
	String route   = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		Bundle b 	 	= getIntent().getExtras();
	    devId 		 	= b.getString("devId");	    
	    customerCode 	= b.getString("ccode");
		customerAddress = b.getString("customerAddress");
		customerName    = b.getString("customerName");
	    
	    getInfo(customerCode);
	    
	    ListView lv = getListView();
        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.profile_header, (ViewGroup) findViewById(R.id.proheader_layout_root));
        lv.addHeaderView(header, null, false);
        
        setListAdapter(new ProfileArrayAdapter(this, PROF_LIST));
	}
	
	private void getInfo(String ccode) {
		// TODO Auto-generated method stub
		CustomerData cd = new CustomerData();
		CustomerDbManager db = new CustomerDbManager(context);
		db.open();
		cd = db.getCustomerDataInfo2(ccode,10);
		db.close();
		
		try{
			oName	  = cd.getOname();
			oCstatus  = cd.getOstat();
			oBday 	  = cd.getObday();
			oInterest = cd.getOhobInt();
			oPhone 	  = cd.getOphone();
			oEmail 	  = cd.getOemail();
			
			average   = cd.getAverage();
			loan	  = cd.getLoan();
			freq	  = cd.getFreq();
			route	  = cd.getRoute();			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		switch(position-1) {		
			case OWNER:
				OwnerProfile();
				break;	
			default:
				//
				break;	
		}
	}
	

	private void OwnerProfile() {
		// TODO Auto-generated method stub
		Log.e("Profile", "Owner");
		
		Intent intent = new Intent(context, ProfileActivity.class);
		Bundle b = new Bundle();
		
		b.putString("cname"		, oName);
		b.putString("ccode"		, customerCode);
		b.putString("address"	, customerAddress);
		b.putString("bday"		, oBday);
		b.putString("hobbies"	, oInterest);
		b.putString("num"		, oPhone);
		b.putString("email"		, oEmail);
		b.putString("cstatus"	, oCstatus);
		
		b.putString("average"	, average);
		b.putString("loan"  	, loan);
		b.putString("route"  	, route);
		b.putString("freq"  	, freq);
		
		intent.putExtras(b); 
		startActivity(intent);
	}
	
	
	
	public class ProfileArrayAdapter extends ArrayAdapter<String> {
				
		private final Context context;
		private final String[] values;
		    
		ArrayList<Integer> itemPos = new ArrayList<Integer>();
	 
		public ProfileArrayAdapter(Context context, String[] values) {
			super(context, R.layout.list_row, values);
			this.context = context;
			this.values = values;
		}
	 
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 
			View rowView = inflater.inflate(R.layout.list_row, parent, false);
			TextView textView = (TextView) rowView.findViewById(R.id.label);
			ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
			textView.setText(values[position]);
	 
			// Change icon based on name
			String s = values[position];
	 
			System.out.println(s+position);
			
			switch(position) {
			
				case 0:
					imageView.setImageResource(R.drawable.owner96);
		    		break;					
				default:
					imageView.setImageResource(R.drawable.xplus_track96);
		    		break;		
			}
			
			return rowView;
		}

	}


}
