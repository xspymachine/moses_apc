package com.xpluscloud.moses_apc;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.xpluscloud.moses_apc.dbase.CoveragePlanDbManager;
import com.xpluscloud.moses_apc.dbase.CustomerDbManager;
import com.xpluscloud.moses_apc.getset.CustomerData;
import com.xpluscloud.moses_apc.util.CustomerDatePicker;
import com.xpluscloud.moses_apc.util.DateUtil;
import com.xpluscloud.moses_apc.util.DbUtil;
import com.xpluscloud.moses_apc.util.Master;

import java.util.ArrayList;
import java.util.Calendar;

public class CustomerDataActivity2 extends AppCompatActivity {
	
	EditText oName;
	AutoCompleteTextView oCStatus;
	EditText oBday;
	EditText oHonInt;
	EditText oPhone;
	EditText oEmail;
	EditText oPosition;

	EditText pName;
	AutoCompleteTextView pCStatus;
	EditText pBday;
	EditText pHonInt;
	EditText pPhone;
	EditText pEmail;
	EditText pPosition;

	Button btMore;
	Button btSave;
	Button btCancel;
	
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
	
	CustomerDatePicker frag;
	Calendar sDate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customer_data2);
		
		context = CustomerDataActivity2.this;
		
		Bundle b = getIntent().getExtras();
	    devId 	= b.getString("devId");
	    ccode 	= b.getString("ccode");
	    mode	= b.getInt("mode");
	    
	    sDate = Calendar.getInstance();
		getSetViews();
//		if(mode == 1 && !ccode.isEmpty()){
//			RelativeLayout RL = (RelativeLayout) findViewById(R.id.logistics);
//			RL.setVisibility(View.VISIBLE);
//			getInfos(ccode);
//		}

		CustomerDbManager db = new CustomerDbManager(context);
		db.open();
		CustomerData data = db.getCustomerDataInfo2(ccode,10);
        CustomerData data2 = db.getCustomerDataInfo2(ccode,20);
		if(data!=null) setViews(data);
        if(data2!=null) setViews2(data2);
		db.close();
		
	}

	private void setViews(CustomerData data){
		oName.setText(data.getOname());
		oBday.setText(data.getObday());
		oHonInt.setText(data.getOhobInt());
        oPhone.setText(data.getOphone());
        oEmail.setText(data.getOemail());
		oPosition.setText(data.getOstat());
	}
    private void setViews2(CustomerData data){
        pName.setText(data.getOname());
        pBday.setText(data.getObday());
        pHonInt.setText(data.getOhobInt());
        pPhone.setText(data.getOphone());
        pEmail.setText(data.getOemail());
		pPosition.setText(data.getOstat());
    }
	
	private void getSetViews(){
		oName 		= (EditText) findViewById(R.id.etOName);
		oCStatus 	= (AutoCompleteTextView) findViewById(R.id.etOStat);
		oBday 		= (EditText) findViewById(R.id.etOBday);
		oHonInt 	= (EditText) findViewById(R.id.etOHobbies);
		oPhone 		= (EditText) findViewById(R.id.etOPhone);
		oEmail 		= (EditText) findViewById(R.id.etOEmail);
		oPosition	= (EditText) findViewById(R.id.etOposition);

		pName 		= (EditText) findViewById(R.id.etPName);
		pCStatus 	= (AutoCompleteTextView) findViewById(R.id.etPStat);
		pBday 		= (EditText) findViewById(R.id.etPBday);
		pHonInt 	= (EditText) findViewById(R.id.etPHobbies);
		pPhone 		= (EditText) findViewById(R.id.etPPhone);
		pEmail 		= (EditText) findViewById(R.id.etPEmail);
		pPosition	= (EditText) findViewById(R.id.etPposition);

		btMore = findViewById(R.id.bt_more_details);
		btSave = (Button) findViewById(R.id.bt_save);
		btCancel = (Button) findViewById(R.id.bt_cancel);
		DbUtil.changeDrawableColor("#F90606", btCancel, 2);
		DbUtil.changeDrawableColor("#7FB200", btSave, 2);
				
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
		btMore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(context,"Web Server still constructing, wait until next update.",Toast.LENGTH_SHORT).show();
			}
		});
		
		ArrayList<String> cpcodes = new ArrayList<String>();
	    
		CoveragePlanDbManager db = new CoveragePlanDbManager(this);
	    db.open();
	    cpcodes = db.getArrayList();
	    db.close();	

		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, status);
		oCStatus.setThreshold(1);
		oCStatus.setDropDownHeight(LayoutParams.WRAP_CONTENT);
		oCStatus.setAdapter(adapter1);		
		
		oBday.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                onClickTextInput();
				etSelectView = oBday;
			}
		});

		pBday.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                onClickTextInput();
				etSelectView = pBday;
			}
		});

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mycalendar.set(Calendar.YEAR,year);
                mycalendar.set(Calendar.MONTH,month);
                mycalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                updateLabel(etSelectView);
            }
        };
		
	}

	private void onClickTextInput(){
	    new DatePickerDialog(CustomerDataActivity2.this,date,mycalendar.get(Calendar.YEAR),
                mycalendar.get(Calendar.MONTH),mycalendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    private void updateLabel(EditText etView){
	    etView.setText(DateUtil.phLongDay(mycalendar.getTimeInMillis()));
    }

	DatePickerDialog.OnDateSetListener date;
	Calendar mycalendar = Calendar.getInstance();
	EditText etSelectView;
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.e("ccode",ccode);
	}

	
	private void saveData(){

//		if(oName.getText().toString().equals("")){
////			Toast.makeText(getApplicationContext(), "Please provide Owner Name" , Toast.LENGTH_LONG).show();
//			DbUtil.makeToast(LayoutInflater.from(context),  "Please provide Owner Name", context,
//					(ViewGroup) findViewById(R.id.custom_toast_layout),1);
//			oName.requestFocus();
//			return;
//		}
//		if(!Arrays.asList(status).contains(oCStatus.getText().toString())){
////			Toast.makeText(getApplicationContext(), "Pls provide valid status: M if married, S if single or separated, W if widowed, G if gay." , Toast.LENGTH_LONG).show();
//			DbUtil.makeToast(LayoutInflater.from(context),  "Pls provide valid status: M if married, S if single or separated, W if widowed, G if gay.", context,
//					(ViewGroup) findViewById(R.id.custom_toast_layout),1);
//			oCStatus.requestFocus();
//			return;
//		}
		
		try{
			CustomerData cust = new CustomerData();
			cust.setCcode(ccode);			
			cust.setOname(oName.getText().toString());
			cust.setOstat(oPosition.getText().toString());
			cust.setObday(oBday.getText().toString().isEmpty() ? "" : DateUtil.reformatDay(oBday.getText().toString()));
			cust.setOhobInt(oHonInt.getText().toString());
			cust.setOphone(oPhone.getText().toString());
			cust.setOemail(oEmail.getText().toString());
			cust.setOstatus(10);

            CustomerData cust2 = new CustomerData();
            cust2.setCcode(ccode);
            cust2.setOname(pName.getText().toString());
            cust2.setOstat(pPosition.getText().toString());
            cust2.setObday(pBday.getText().toString().isEmpty() ? "" : DateUtil.reformatDay(pBday.getText().toString()));
            cust2.setOhobInt(pHonInt.getText().toString());
            cust2.setOphone(pPhone.getText().toString());
            cust2.setOemail(pEmail.getText().toString());
            cust2.setOstatus(20);

//			cust.getPname(pNam)
			
			CustomerDbManager db = new CustomerDbManager(this);
			db.open();	
			db.AddNewData(cust);
            db.AddNewData(cust2);
			db.close();
			
			addDataToOutbox();	
			
//			Toast.makeText(getApplicationContext(), "Customer Data has been updated!" , Toast.LENGTH_SHORT).show();
			DbUtil.makeToast(LayoutInflater.from(context),  "Customer Data has been updated!", context,
					(ViewGroup) findViewById(R.id.custom_toast_layout),0);
		}catch(Exception e) {
			e.printStackTrace();
//			Toast.makeText(getApplicationContext(), "Customer Data ERROR" , Toast.LENGTH_SHORT).show();
			DbUtil.makeToast(LayoutInflater.from(context),  "Customer Data ERROR", context,
					(ViewGroup) findViewById(R.id.custom_toast_layout),0);
			return;
		}
		
		finish();
		
	}
	
	private void addDataToOutbox(){
		
		String ownerName 				= oName.getText().toString();
		String ownerCivilStatus 		= oCStatus.getText().toString();
		String ownerBday				= oBday.getText().toString().isEmpty() ? "" : DateUtil.reformatDay(oBday.getText().toString());
		String ownerHobbiesInterests	= oHonInt.getText().toString();
		String ownerPhone				= oPhone.getText().toString();
		String ownerEmail				= oEmail.getText().toString();
		
		String message = Master.OWNER   + " " +
				devId 					+ ";" +
				ccode  					+ ";" +
				ownerName 				+ ";" +
				ownerCivilStatus 		+ ";" +
				ownerBday 				+ ";" +
				ownerHobbiesInterests 	+ ";" +
				ownerPhone 				+ ";" +
				ownerEmail              + ";" +"primary";
		DbUtil.saveMsg(context,DbUtil.getGateway(context), message);

        String owner2Name 				= pName.getText().toString();
        String owner2CivilStatus 		= pCStatus.getText().toString();
        String owner2Bday				= pBday.getText().toString().isEmpty() ? "" : DateUtil.reformatDay(pBday.getText().toString());
        String owner2HobbiesInterests	= pHonInt.getText().toString();
        String owner2Phone				= pPhone.getText().toString();
        String owner2Email				= pEmail.getText().toString();

        String message2 = Master.OWNER   + " " +
                devId 					+ ";" +
                ccode  					+ ";" +
                owner2Name 				+ ";" +
                owner2CivilStatus 		+ ";" +
                owner2Bday 				+ ";" +
                owner2HobbiesInterests 	+ ";" +
                owner2Phone 				+ ";" +
                owner2Email             + ";" +"secondary";
        DbUtil.saveMsg(context,DbUtil.getGateway(context), message2);
		
//		String averageSales  = etAverage.getText().toString();
//		String onLoan		 = etLoan.getText().toString();
//		int route_cpid 	 	 = chkCplan(etRoute.getText().toString());
//		String freq			 = etFreq.getText().toString();
//
//		String message2 = Master.CMD_GPDATA 	+ " " +
//				devId 					+ ";" +
//				ccode  					+ ";" +
//				averageSales 			+ ";" +
//				onLoan	 				+ ";" +
//				route_cpid				+ ";" +
//				freq;
//		DbUtil.saveMsg(context,DbUtil.getGateway(context), message2);
	}

}
