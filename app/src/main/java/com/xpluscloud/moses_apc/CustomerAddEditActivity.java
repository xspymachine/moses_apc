package com.xpluscloud.moses_apc;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.xpluscloud.moses_apc.dbase.AccountTypeDbManager;
import com.xpluscloud.moses_apc.dbase.CoveragePlanDbManager;
import com.xpluscloud.moses_apc.dbase.CustomerDbManager;
import com.xpluscloud.moses_apc.getset.Customer;
import com.xpluscloud.moses_apc.util.ArrayDef;
import com.xpluscloud.moses_apc.util.DbUtil;
import com.xpluscloud.moses_apc.util.DialogManager;
import com.xpluscloud.moses_apc.util.Master;
import com.xpluscloud.moses_apc.util.StringUtil;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;

//import android.widget.Toast;

public class CustomerAddEditActivity extends AppCompatActivity {
	
	final String TAG="CustomerEditActivity";
	
	final int SALES_BOOKING=0;
	final int CASH_SALES=1;
	
	final int MODE_ADD = 0;
	final int MODE_EDIT = 1;
	int MODE;
	
	Context context;

	String devId;
	long custId;
	TextView tvHeader;
	TextView tvCcode;
	EditText etName;
	Spinner etAddress;
	Spinner etBrgy;
	Spinner etCity;
    Spinner etState;
	Spinner spStatus;
//	Spinner spAcctType;
	Spinner spTerms;
	Spinner spType;
	Spinner spGs;
	EditText etAr;
	LinearLayout cbLayout;
	
	AutoCompleteTextView atCplan;
	
//	EditText etDiscount;
//	EditText etBuffer;
//	RadioButton rbCashsales;
//	RadioButton rbBooking;
	
	RadioGroup rgPckg;
	
	Button btSave, btCancel;
	Button btNewData;
	
	
	String strCmd;
	String custCode;
	
	int flag = 0;
	
	ArrayAdapter<String> aTArrayAdapter;

	ArrayAdapter<String> brgyAdapter;
    ArrayAdapter<String> cityAdapter;
    ArrayAdapter<String> stateAdapter;
		
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customer_edit_tab);
		
		context = CustomerAddEditActivity.this;
		
		Bundle b = getIntent().getExtras();
	    devId 	= b.getString("devId");
	    custId 	= b.getInt("custId");
	    
	    initView();

	    Log.e("custId",""+custId);
	    
	    //Get Customer Info 
	    strCmd = Master.CMD_ACUS;
	    if (custId>0)  {
	    	getCustomerInfo(custId);
	    	strCmd = Master.CMD_UCUS;
	    	MODE = MODE_EDIT;
	    }
	    else {
	    	custCode = get_custCode();
	    	MODE = MODE_ADD;
	    	tvCcode.setText(custCode);
	    	tvHeader.setText("Add Customer");
	    	setTitle("Add Customer");
//	    	TextView tvCplan = (TextView)findViewById(R.id.labelCplan);
//	    	AutoCompleteTextView etCplan = (AutoCompleteTextView)findViewById(R.id.etCplan);
//	    	tvCplan.setVisibility(View.GONE);
//	    	etCplan.setVisibility(View.GONE);
	    }
	    
	    btSave.setOnClickListener(new OnClickListener() {
	        @Override
			public void onClick(View v) {
//	        	if(flag == 0) Toast.makeText(getApplicationContext(), "Please check customer data" , Toast.LENGTH_SHORT).show();
//	        	else 
	        		
	        	if (updateCustomerInfo()) finish();
	        } 
	    });
	  
	    btCancel.setOnClickListener(new OnClickListener() {
	        @Override
			public void onClick(View v) {
				new AlertDialog.Builder(context)
						.setTitle("Add/Edit Customer")
						.setMessage("Are you sure you want to cancel?")
						.setPositiveButton("Yes", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which) {
								finish();
							}

						})
						.setNegativeButton("No", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						})
						.show();
	        } 
	    });

	    btNewData.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (updateCustomerInfo()) customerNewData();

			}
		});
	  
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.customer_edit, menu);
		return true;
	}
	
	private void initView() {
		tvHeader	= (TextView) findViewById(R.id.tvheader);
		tvCcode 	= (TextView) findViewById(R.id.tvCode);
		etName 		= (EditText) findViewById(R.id.etName);
		etAddress 	=  findViewById(R.id.etAddress);
		etBrgy		= (Spinner) findViewById(R.id.etBrgy);
		etCity 		= (Spinner) findViewById(R.id.etCity);
		etState 	= (Spinner) findViewById(R.id.etState);
		spStatus	= (Spinner) findViewById(R.id.etContact);
//		spAcctType	= (Spinner)  findViewById(R.id.spType);
		spTerms		= (Spinner)  findViewById(R.id.spTerms);
		spType		= (Spinner)  findViewById(R.id.spType);
		spGs		= (Spinner)  findViewById(R.id.spGs);
		etAr		= findViewById(R.id.et_ar);
		cbLayout 	= findViewById(R.id.appcb);
		etAr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					if(etAr.getText().toString().equals("0.0")) etAr.setText("");
				}else{
					if(etAr.getText().toString().isEmpty()) etAr.setText("0.0");
					else {
						NumberFormat formatter = new DecimalFormat("#,###");
						String fmAcctR = formatter.format(etAr.getText().toString());
						etAr.setText(fmAcctR);
					}
				}
			}
		});
		
//		etDiscount 	= (EditText) findViewById(etDiscount);
		atCplan		= (AutoCompleteTextView) findViewById(R.id.etCplan);
//		etBuffer 	= (EditText) findViewById(R.id.etBuffer);
//		rbCashsales	= (RadioButton) findViewById(R.id.rbCashSales);
//		rbBooking	= (RadioButton) findViewById(R.id.rbSalesBooking);
		
		btSave		= (Button) findViewById(R.id.bt_save);
	    btCancel	= (Button) findViewById(R.id.bt_cancel);
//	    DbUtil.changeDrawableColor("#F90606", btCancel, 2);
//		DbUtil.changeDrawableColor("#7FB200", btSave, 2);
	    btNewData	= (Button) findViewById(R.id.btNewData);
	    
//	    etDiscount.setText("2");
//	    etBuffer.setText("7");

	    /**************** Coverage Plan Select Option **********************/
	    
	    ArrayList<String> cpcodes = new ArrayList<String>();
	    
	    CoveragePlanDbManager db = new CoveragePlanDbManager(this);
	    db.open();
	    cpcodes = db.getArrayList();
	    db.close();	
	    
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, cpcodes);
	    atCplan.setThreshold(1);
	    atCplan.setDropDownHeight(LayoutParams.WRAP_CONTENT);
	    atCplan.setAdapter(adapter);
	    atCplan.setText("W13 D1");
	    
	    /**************** State/Province Select Option **********************/

		stateAdapter = new ArrayAdapter<>(this,
				R.layout.csi_spinner, getStrAddress(1,""));
		etState.setAdapter(stateAdapter);
		etState.setSelection(0);

		etState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				cityAdapter = new ArrayAdapter<>(context,
					R.layout.csi_spinner, getStrAddress(0,etState.getSelectedItem().toString()));
				etCity.setAdapter(cityAdapter);
				etCity.setSelection(0);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		etCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				brgyAdapter = new ArrayAdapter<>(context,
					R.layout.csi_spinner, getStrAddress(2,etCity.getSelectedItem().toString()));
				etBrgy.setAdapter(brgyAdapter);
				etBrgy.setSelection(0);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	    
	    /**************************************/
	       

	    ArrayList<String> acct_types = new ArrayList<String>();
	    
	    AccountTypeDbManager db3 = new AccountTypeDbManager(this);
	    db3.open();
	    acct_types = db3.getAcctTypes();
	    db3.close();	

    	aTArrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.csi_spinner, acct_types);

    	ArrayAdapter<?> tmArrayAdapter = new ArrayAdapter<Object>(this,
                R.layout.csi_spinner, ArrayDef.TERMS2);
    	spTerms.setAdapter(tmArrayAdapter);
		spTerms.setSelection(3);

		String[] types = ArrayDef.ACCT_TYPES4;
//		if(DbUtil.getSetting(context,"type").contains("B2C")) types = ArrayDef.ACCT_TYPES2;
//		else types = ArrayDef.ACCT_TYPES3;

		spType.setAdapter(new ArrayAdapter<Object>(this,
				R.layout.csi_spinner, types));
		spGs.setAdapter(new ArrayAdapter<Object>(this,
				R.layout.csi_spinner, ArrayDef.GROUPSALES));

		ArrayAdapter<?> stArrayAdapter = new ArrayAdapter<Object>(this,
				R.layout.csi_spinner, ArrayDef.STATUS);
		spStatus.setAdapter(stArrayAdapter);
		spStatus.setSelection(0);
	}

	@Override
	public void onBackPressed() {
	}
	
	
	private void getCustomerInfo(long custId) {	
//		Log.d("CUSTOMERADDEDIT", "start");
		CustomerDbManager db = new CustomerDbManager(this);
//		Customer cust = null;
		db.open();		
//		try{
			Customer cust = db.getCustomer(custId);
//		}catch(Exception e){e.printStackTrace();}
		db.close();
		try {
			custCode = cust.getCustomerCode();
			tvCcode.setText(cust.getCustomerCode());
			etName.setText(cust.getName());

			int spinnerPosition4 = cityAdapter.getPosition(cust.getBrgy());
			etBrgy.setSelection(spinnerPosition4);

			String acctR = (cust.getAr() == null || cust.getAr().isEmpty()) ? "0.0" : cust.getAr();
			etAr.setText(acctR);

            int spinnerPosition1 = cityAdapter.getPosition(cust.getCity());
            etCity.setSelection(spinnerPosition1);

            int spinnerPosition2 = stateAdapter.getPosition(cust.getState());
            etState.setSelection(spinnerPosition2);

			int spinnerPosition3 = Arrays.asList(getResources().getStringArray(R.array.cjourn) ).indexOf(cust.getAddress());
			etAddress.setSelection(spinnerPosition3);

//			etCity.setText(cust.getCity());
//			etState.setText(cust.getState());
//			etContact.setText(cust.getContactNumber());
			
//			int aType = aTArrayAdapter.getPosition(getType(cust.getAcctypid()));
									
//        	spAcctType.setSelection(aType);
        	
        	int term = cust.getTermid();
        	
//        	if(term == 9) term = 2;
//			else if(term == 7) term = 5;

			int type = cust.getAcctypid();
			int termL = ArrayDef.TERMS2.length-1;
//			int typeL = ArrayDef.ACCT_TYPES2.length-1;
        	spTerms.setSelection(term>termL?termL:term);

//			spType.setSelection(type>typeL?typeL:type);
			Log.e("accttypeid",""+type);
			int typeIndex = Arrays.asList(ArrayDef.ACCT_TYPES4).lastIndexOf(getTypeString(type));
			spType.setSelection(typeIndex);

            int status = Arrays.asList(ArrayDef.STATUS).indexOf(cust.getContactNumber());
            spStatus.setSelection(status<0?0:status);

			int gsIndex = Arrays.asList(ArrayDef.GROUPSALES).indexOf(cust.getSalesgroup());
			spGs.setSelection(gsIndex);

			if(cust.getApplication()!=null && cust.getApplication().contains(",")){
				String[] strApp = cust.getApplication().split(",");
				int count =0;
				while (strApp.length > count){
					for(int i=0;i<cbLayout.getChildCount();i++){
						View child=cbLayout.getChildAt(i);
						if (child instanceof CheckBox){
							if (((CheckBox) child).getText().toString().equals(strApp[count])) {
								((CheckBox) child).setChecked(true);
								break;
							}
						}
					}
					count++;
				}
			}

//        	etDiscount.setText(""+cust.getDiscount());
			Log.e("cplan",cust.getCplanCode());
			if(cust.getCplanCode().length()<2) atCplan.setText("F2");
			else atCplan.setText(cust.getCplanCode());
		
//			etBuffer.setText(""+cust.getBuffer());
			
			Integer customerCashSales=cust.getCashSales();
			
//			if (customerCashSales==SALES_BOOKING) {
//				rbBooking.setChecked(true);
//			}
//			else {
//				rbCashsales.setChecked(true);
//			}
			
			tvCcode.setVisibility(View.GONE);
			
			//Log.i("LOG","CPID = " + cust.getCplanCode());
			
			customerBuffer= cust.getBuffer();
			customerCplan = cust.getCplanCode();
			customerDisc = cust.getDiscount();
			customerAcctype = cust.getAcctypid();
			customerTerm = cust.getTermid();
						
			
		}catch (Exception e) {
//			Log.e("Error:getCustomerInfo","Exception Error!");
			e.printStackTrace();
		}
//		Log.d("CUSTOMERADDEDIT", "end");
		
	}
	
	int customerBuffer= 0;
	String customerCplan = "";
	Double customerDisc = 0.0;
	int customerAcctype = 0;
	int customerTerm = 0;
	
	
	
	private Boolean updateCustomerInfo(){
		String cpcode = atCplan.getText().toString();
//		String region = etState.getText().toString();

		if(etState.getSelectedItemPosition() < 1){
			DialogManager
					.showAlertDialog(context,
							"Invalid Province",
							"Please provide a valid Province or download `places` from Dashboard-downloads.",
							true);
			return false;
		}
		if(spGs.getSelectedItemPosition() < 1){
			DialogManager
					.showAlertDialog(context,
							"Invalid Sales Group",
							"Please provide a valid Sales Group.",
							true);
			return false;
		}
		if(spType.getSelectedItemPosition() < 1){
			DialogManager
					.showAlertDialog(context,
							"Invalid Customer Segment",
							"Please provide a valid Customer Segment.",
							true);
			return false;
		}
		
		if(cpcode.isEmpty()){
			DialogManager
			.showAlertDialog(context,
					"Invalid CPlan Code",
					"Example Code: W13 D1,W3 D1 \n" +
							" for Week 1 Monday & Week 3 Monday." +
							" For more inquiry, pls contact office.",
					true);
			atCplan.requestFocus();
			return false;
		}

		if (cpcode.length()>0) {
			if (chkCplan(cpcode)) {
				DialogManager
				.showAlertDialog(context,
						"Invalid CPlan Code",
						"Example Code: W13 D1,W3 D1 \n" +
								" for Week 1 Monday & Week 3 Monday." +
								" For more inquiry, pls contact office.",
						true);
				atCplan.requestFocus();
				return false;
			}
		}

		String customerApplication =  cb_application();
		if(customerApplication.isEmpty()){
			DialogManager
					.showAlertDialog(context,
							"Customer Application is empty.",
							"Please provide at least one.",
							true);
			atCplan.requestFocus();
			return false;
		}
		
		try {
			
			String customerCode 	= tvCcode.getText().toString();
			String customerName 	= etName.getText().toString();
			String customerAddress 	= etAddress.getSelectedItem().toString();
			String customerBrgy 	= etBrgy.getSelectedItem().toString();
			String customerCity 	= etCity.getSelectedItem().toString();
			String customerState 	= etState.getSelectedItem().toString();
			String customerContact 	= spStatus.getSelectedItem().toString();
			int    customerTerm		= spTerms.getSelectedItemPosition();
			int    customerType		= spType.getSelectedItemPosition();
			customerAcctype         = getTypeId(spType.getSelectedItem().toString());
			String customerGroupsales 		= spGs.getSelectedItem().toString();

			int termid 				= getOtherDataId(spTerms.getSelectedItem().toString(),6);

			String customerCplan 	= atCplan.getText().toString();

			int customerCashSales=SALES_BOOKING;
			
			customerName 			= StringUtil.strCleanUp(customerName);
			
//			customerAddress			= StringUtil.strCleanUp(customerAddress);
			customerCity 			= StringUtil.strCleanUp(customerCity); 
			customerBrgy			= StringUtil.strCleanUp(customerBrgy);

//			String fmAcctR="";
//			if(!etAr.getText().toString().isEmpty()) {
//				NumberFormat formatter = new DecimalFormat("#,###");
//				fmAcctR = formatter.format(etAr.getText().toString());
//			}
			
//			Log.d(TAG+":updateCustomerInfo","customerCode: "+customerCode);
			Customer cust = new Customer();
			cust.setCustomerCode(customerCode);			
			cust.setName(customerName);
			cust.setAddress(customerAddress);
			cust.setBrgy(customerBrgy);
			cust.setCity(customerCity);
			cust.setState(customerState);
			cust.setContactNumber(customerContact);				
			cust.setCplanCode(customerCplan);
			cust.setBuffer(customerBuffer); 
			cust.setDiscount(customerDisc);
			cust.setCashSales(customerCashSales);
			cust.setAcctypid(customerAcctype);
			cust.setTermid(customerTerm);
			cust.setTypeid(customerType);
//			cust.setAr(fmAcctR);
			cust.setSalesgroup(customerGroupsales);
			cust.setApplication(customerApplication);
						
			CustomerDbManager db = new CustomerDbManager(this);
			db.open();	
			if(MODE==MODE_EDIT) {
				cust.setId((int) custId);
				db.update(cust);
			}
			else {
				custId = db.AddCustomer(cust);
			}
			db.close();
			
//			devId = DbUtil.getSetting(context, Master.DEVID);
			String sysTime = String.valueOf(System.currentTimeMillis()/1000);
			
			String message = strCmd + " " +
					devId 				+ ";" +
					customerCode  		+ ";" +
					customerName 		+ ";" +
					customerAddress 	+ ";" +
					customerBrgy 		+ ";" +
					customerCity 		+ ";" +
					customerState 		+ ";" +
					customerContact 	+ ";" +
					customerAcctype 	+ ";" +
					termid		 		+ ";" +
					customerDisc	 	+ ";" +
					customerCplan 		+ ";" +
					customerBuffer 		+ ";" +
					customerCashSales 	+ ";" +
					sysTime 			+ ";" +
					custId;	
			DbUtil.saveMsg(context,DbUtil.getGateway(context), message);
			String message2 = Master.CMD_CUSM + " " +
					devId 				+ ";" +
					customerCode  		+ ";" +
					"groupsales" 		+ ";" +
					customerGroupsales;
			DbUtil.saveMsg(context,DbUtil.getGateway(context), message2);
			String message3 = Master.CMD_CUSM + " " +
					devId 				+ ";" +
					customerCode  		+ ";" +
					"application"		+ ";" +
					customerApplication;
			DbUtil.saveMsg(context,DbUtil.getGateway(context), message3);

			DbUtil.makeToast(LayoutInflater.from(context),  "Customer has been updated!", context,
					(ViewGroup) findViewById(R.id.custom_toast_layout),0);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	private String cb_application(){
		String txtCbApp = "";
		int cbCheckCount=0;
		for(int i=0;i<cbLayout.getChildCount();i++){
			View child=cbLayout.getChildAt(i);

			if (child instanceof CheckBox){
				if (((CheckBox) child).isChecked()) {
					cbCheckCount++;
					txtCbApp += ((CheckBox) child).getText().toString()+",";
				}
			}
		}
		if (cbCheckCount>=1) {
			return txtCbApp.substring(0,txtCbApp.length()-1);
		}else return "";
	}
	
	private String get_custCode(){
		CustomerDbManager db = new CustomerDbManager(this);
		db.open();		
		Integer lastId=db.getLastId()+1;
		db.close();
		
		String strCode = ("00000" + lastId).substring(String.valueOf(lastId).length());
		String strRandom = StringUtil.randomText(10);
		strCode = devId.substring(devId.length() - 5) + strRandom + strCode ;
		
		return strCode;
	}

	
	private Boolean chkCplan(String cpcode) {
		CoveragePlanDbManager db = new CoveragePlanDbManager(this);
		
		db.open();
		Integer cpid = db.getCpId(cpcode);
		db.close();
		if (cpid>0) return false;
		else return true;
	}

	private void customerNewData(){
		Intent intent = new Intent(context, CustomerDataActivity2.class);
		Bundle b = new Bundle();
		
		b.putString("devId", devId);		
		b.putString("ccode", custCode);
		b.putInt("mode", MODE);
		
		intent.putExtras(b); 
		startActivity(intent);
		
		flag = 1;
	}
	
	private int getTypeId(String type){
		int id;
		
		AccountTypeDbManager db = new AccountTypeDbManager(this);		
		db.open();
		id = db.getTypeId(type);
		db.close();
		
		return id;
	}

	private String getTypeString(int typeId){
		String type;

		AccountTypeDbManager db = new AccountTypeDbManager(this);
		db.open();
		type = db.getType(typeId);
		db.close();

		return type;
	}

	private int getOtherDataId(String type,int status){
		int id;

		AccountTypeDbManager db = new AccountTypeDbManager(this);
		db.open();
		id = db.getOtherDataId(type,status);
		db.close();

		return id;
	}

	private String[] getStrAddress(int opt,String where){

        CustomerDbManager db = new CustomerDbManager(context);
        db.open();
        ArrayList<String> str = db.getAddress(opt,where);
        db.close();

        String[] stockArr = new String[str.size()];
        stockArr = str.toArray(stockArr);


        return stockArr;
	}

	public void storeType(View v){
        Intent intent = new Intent(context, StoretypeActivity.class);
		Bundle b = new Bundle();
		b.putInt("type", 1);
		intent.putExtras(b);
		startActivity(intent);
    }
}
