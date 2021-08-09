package com.xpluscloud.moses_apc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.xpluscloud.moses_apc.dbase.CustomerDbManager;
import com.xpluscloud.moses_apc.getset.CustomerData;
//import com.xpluscloud.mosesshell.util.CustomerDatePicker;
import com.xpluscloud.moses_apc.util.DateUtil;
import com.xpluscloud.moses_apc.util.DbUtil;
import com.xpluscloud.moses_apc.util.Master;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class CustomerDataActivity extends Activity {
	
	EditText oName;
	AutoCompleteTextView oCStatus;
	EditText oBday;
	EditText oHonInt;
	EditText oPhone;
	EditText oEmail;
	
//	EditText pName;
//	AutoCompleteTextView pCStatus;
//	EditText pBday;
//	EditText pHonInt;
//	EditText pPhone;
//	EditText pEmail;
	
	EditText etNoTruck;
	Spinner spDelivery;
	AutoCompleteTextView etSupplier;
	EditText etWcap;
	
	Button btTruck;
//	Button btCap;
	
	ImageButton btSave;
	ImageButton btCancel;
	
	String devId;
	String ccode;
	int mode;
	
	Context context;
	
	ArrayAdapter<String> aTArrayAdapter;
	
	public static final String[] status = {
		"Single", 
		"Married",
		"Separated",
		"Widowed",
		"Gay"
	};
	
	public static final String[] delivery_time = {
		"-", 
		"Morning",
		"Afternoon",
		"Night",
		"Past Midnight"
	};
	
//	CustomerDatePicker frag;
	Calendar sDate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customer_data);
		
		context = CustomerDataActivity.this;
		
		Bundle b = getIntent().getExtras();
	    devId 	= b.getString("devId");
	    ccode 	= b.getString("ccode");
	    mode	= b.getInt("mode");
	    
	    sDate = Calendar.getInstance();
		getSetViews();
		if(mode == 1 && !ccode.isEmpty()) getInfos(ccode);
		
	}
	
	private void getSetViews(){
		oName 		= (EditText) findViewById(R.id.etOName);
		oCStatus 	= (AutoCompleteTextView) findViewById(R.id.etOStat);
		oBday 		= (EditText) findViewById(R.id.etOBday);
		oHonInt 	= (EditText) findViewById(R.id.etOHobbies);
		oPhone 		= (EditText) findViewById(R.id.etOPhone);
		oEmail 		= (EditText) findViewById(R.id.etOEmail);
		
		etNoTruck 	= (EditText) findViewById(R.id.etTruck);
		spDelivery 	= (Spinner) findViewById(R.id.spDelivery);
		etSupplier 	= (AutoCompleteTextView) findViewById(R.id.etSupplier);
		etWcap		= (EditText) findViewById(R.id.etWarehouse);
		
		btTruck   	= (Button) findViewById(R.id.btTruck);
//		btCap		= (Button) findViewById(R.id.btCap);
				
		btSave = (ImageButton) findViewById(R.id.bt_save);
		btCancel = (ImageButton) findViewById(R.id.bt_cancel);
		
		btTruck.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(context, TruckActivity.class);
				Bundle b = new Bundle();
				b.putString("devId", devId);	
				b.putString("customerCode", ccode);
				i.putExtras(b); 
				startActivity(i);
			}
		});
		
//		btCap.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent i = new Intent(context, WarehouseActivity.class);
//				Bundle b = new Bundle(); 
//				b.putString("devId", devId);	
//				b.putString("customerCode", ccode);
//				i.putExtras(b); 
//				startActivity(i);				
//			}
//		});
		
		btSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				saveData();
			}
		});
		
		btCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		ArrayList<String> suppliers = new ArrayList<String>();
	    
	    CustomerDbManager db2 = new CustomerDbManager(this);
	    db2.open();
	    suppliers = db2.getArrayList("suppliers");
	    db2.close();	
		
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, suppliers);
		etSupplier.setThreshold(1);
		etSupplier.setDropDownHeight(LayoutParams.WRAP_CONTENT);
		etSupplier.setAdapter(adapter2);
		
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, status);
		oCStatus.setThreshold(1);
		oCStatus.setDropDownHeight(LayoutParams.WRAP_CONTENT);
		oCStatus.setAdapter(adapter1);
		
		aTArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.csi_spinner, delivery_time);
		spDelivery.setAdapter(aTArrayAdapter);
		
		oBday.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				showDialog(oBday);
			}
		});
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.e("ccode",ccode);
		try{
			CustomerDbManager db = new CustomerDbManager(context);
			db.open();
			etNoTruck.setText(""+db.getNoTrucks(ccode));
			db.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public interface DateDialogFragmentListener{
		//this interface is a listener between the Date Dialog fragment and the activity to update the buttons date
		public void updateChangedDate(int year, int month, int day);
	}
	
//	public void showDialog(final EditText etView) {
//		FragmentTransaction ft = getFragmentManager().beginTransaction(); //get the fragment
//		frag = CustomerDatePicker.newInstance(this, new DateDialogFragmentListener(){
//			public void updateChangedDate(int year, int month, int day){
//				sDate.set(year, month, day);
//				etView.setText(DateUtil.phLongDate(sDate.getTimeInMillis()));	    			
//			}
//		}, sDate);
//		
//		frag.show(ft, "DateDialogFragment");	    	
//	}
	
	private void getInfos(String ccode){
		
//		CustomerDbManager db = new CustomerDbManager(this);
//		db.open();		
//		CustomerData custData = db.getCustomerDataInfo(ccode);
//		db.close();
//		try {
//			oName.setText(custData.getOname());
//			oCStatus.setText(custData.getOstat());
//			oBday.setText(DateUtil.reverseDate(custData.getObday()));
//			oHonInt.setText(custData.getOhobInt());
//			oPhone.setText(custData.getOphone());
//			oEmail.setText(custData.getOemail());
//					
//			etNoTruck.setText(custData.getPtruck());
//			spDelivery.setSelection(aTArrayAdapter.getPosition(custData.getPdelivery()));
//			etSupplier.setText(custData.getPsupplier());
//			etWcap.setText(custData.getPwarehouse());
//			
//			
//		}catch (Exception e) {
//			Log.e("Error: getCustomerDataInfo  ","Exception Error!");
//		}
		
	}
	
	private void saveData(){
		
		if(getSupplierId(etSupplier.getText().toString()) == 0){
			Toast.makeText(getApplicationContext(), "Please provide Supplier" , Toast.LENGTH_LONG).show();
			etSupplier.requestFocus();
			return;
		}
				
		if(oName.getText().toString().equals("")){
			Toast.makeText(getApplicationContext(), "Please provide Owner Name" , Toast.LENGTH_LONG).show();
			oName.requestFocus();
			return;
		}
		if(!Arrays.asList(status).contains(oCStatus.getText().toString())){
			Toast.makeText(getApplicationContext(), "Pls provide valid status: M if married, S if single or separated, W if widowed, G if gay." , Toast.LENGTH_LONG).show();
			oCStatus.requestFocus();
			return;
		}
		
		try{
			CustomerData cust = new CustomerData();
			cust.setCcode(ccode);			
			cust.setOname(oName.getText().toString());
			cust.setOstat(oCStatus.getText().toString());
			cust.setObday(oBday.getText().toString().isEmpty() ? "" : DateUtil.reformatDate(oBday.getText().toString()));
			cust.setOhobInt(oHonInt.getText().toString());
			cust.setOphone(oPhone.getText().toString());
			cust.setOemail(oEmail.getText().toString());				
			
////			cust.setCcode(ccode);			
//			cust.setPtruck(etNoTruck.getText().toString());
//			cust.setPdelivery(spDelivery.getSelectedItem().toString());
//			cust.setPsupplier(etSupplier.getText().toString());
//			cust.setPwarehouse(etWcap.getText().toString());
			
			CustomerDbManager db = new CustomerDbManager(this);
			db.open();	
			db.AddNewData(cust);
			db.close();
			
			addDataToOutbox();	
			
			Toast.makeText(getApplicationContext(), "Customer Data has been updated!" , Toast.LENGTH_SHORT).show();
		}catch(Exception e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "Customer Data ERROR" , Toast.LENGTH_SHORT).show();
			return;
		}
		
		finish();
		
	}
	
	private void addDataToOutbox(){
		
		String ownerName 				= oName.getText().toString();
		String ownerCivilStatus 		= oCStatus.getText().toString();
		String ownerBday				= oBday.getText().toString().isEmpty() ? "" : DateUtil.reformatDate(oBday.getText().toString());
		String ownerHobbiesInterests	= oHonInt.getText().toString();
		String ownerPhone				= oPhone.getText().toString();
		String ownerEmail				= oEmail.getText().toString();
//		devId = DbUtil.getSetting(context, Master.DEVID);
		
		String message = Master.OWNER   + " " +
				devId 					+ ";" +
				ccode  					+ ";" +
				ownerName 				+ ";" +
				ownerCivilStatus 		+ ";" +
				ownerBday 				+ ";" +
				ownerHobbiesInterests 	+ ";" +
				ownerPhone 				+ ";" +
				ownerEmail;
		DbUtil.saveMsg(context,DbUtil.getGateway(context), message);
		
		String no_trucks 	 = etNoTruck.getText().toString();
		String delivery		 = spDelivery.getSelectedItem().toString();
		int supplier_id 	 = getSupplierId(etSupplier.getText().toString());
		String warehouse_cap = etWcap.getText().toString();
		
		String message2 = Master.ODATA 	+ " " +
				devId 					+ ";" +
				ccode  					+ ";" +	
				no_trucks 				+ ";" +
				delivery	 			+ ";" +
				supplier_id				+ ";" +
				warehouse_cap;
		DbUtil.saveMsg(context,DbUtil.getGateway(context), message2);
	}
	
	private int getSupplierId(String supplier){
		int spid = 0;
		CustomerDbManager db = new CustomerDbManager(context);
		db.open();
		spid = db.getSupplierId(supplier);
		db.close();
		return spid;
	}

}
