package com.xpluscloud.mosesshell_davao;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.xpluscloud.mosesshell_davao.dbase.AccountTypeDbManager;
import com.xpluscloud.mosesshell_davao.dbase.CallSheetDbManager;
import com.xpluscloud.mosesshell_davao.dbase.CallSheetItemDbManager;
import com.xpluscloud.mosesshell_davao.dbase.CustomerDbManager;
import com.xpluscloud.mosesshell_davao.dbase.InOutDbManager;
import com.xpluscloud.mosesshell_davao.dbase.ItemDbManager;
import com.xpluscloud.mosesshell_davao.getset.CallSheet;
import com.xpluscloud.mosesshell_davao.getset.CallSheetItem;
import com.xpluscloud.mosesshell_davao.getset.Customer;
import com.xpluscloud.mosesshell_davao.getset.TimeInOut;
import com.xpluscloud.mosesshell_davao.util.ArrayDef;
import com.xpluscloud.mosesshell_davao.util.DateUtil;
import com.xpluscloud.mosesshell_davao.util.DbUtil;
import com.xpluscloud.mosesshell_davao.util.DialogManager;
import com.xpluscloud.mosesshell_davao.util.LayoutUtil;
import com.xpluscloud.mosesshell_davao.util.Master;
import com.xpluscloud.mosesshell_davao.util.NumUtil;
import com.xpluscloud.mosesshell_davao.util.StringUtil;

import java.text.DecimalFormat;
import java.util.List;


public class CallSheetActivity extends AppCompatActivity implements ItemOptionDialogFragment.OptionDialogListener {

	String TAG ="CallSheetActivity ";

	String devId;

	final int DELETE_CALLSHEET_ITEM 	= 0;
	final int DELETE_CALLSHEET			= 1;

	final int SINGLE_ITEM	= 0;
	final int TOP_SELLING 	= 1;
	final int CANVAS_DRIVE	= 2;
	final int SELECT_ALL	= 3;

	final int BOOKING = 1;
	final int SAMPLING = 2;

	final int TV_CSI_ID		= 100001;
	final int TV_ITEM_CODE	= 100002;
	final int TV_DESCR 		= 100003;
	final int SP_PCKG		= 100004;
	final int TV_DELIVERY	= 100005;
	final int TV_PASTINVT	= 100006;
	final int ET_PRESENTINVT= 100006;
	final int TV_OFFTAKE	= 100007;
	final int TV_ICO		= 100008;
	final int TV_SUGGEST	= 100009;
	final int ET_ORDER		= 100010;
	final int TV_PRICE		= 100011;
	final int TV_AMOUNT		= 100012;
	final int TV_STATUS		= 100013;

	final int MODE_ADD = 0;
	final int MODE_EDIT = 1;

	int mode;
	Context context;

	public String 	csCode;

	public String 	cCode;
	public String 	cName;
	public String 	cAddress;
	public String	cAcctType;
	public String 	cTerm;
	public Double 	cDiscount;

	public Integer 	soNo;
	public Long 	dateTime;
	public Integer	cBuffer;
	public Integer	cCashSales;


	public Double paymentReceived;


	TextView tvCname;
	TextView tvCaddress;
	TextView tvCacctType;
	TextView etRemarks;
	Spinner tvCterm;
	AutoCompleteTextView atSupplier;

	TextView tvDate;
	EditText etSONo;
	Spinner  spCashSales;


	RelativeLayout rlPayment;
	EditText etPayment;

	public Time timeNow;

	String strDate;

	Button btAddSku;
	Button btSignature;
	Button btSubmit;

	static TextView tvTotalAmount;

	TableLayout table;
	TableRow selectedRow;

	Customer cust;
	Boolean SUBMITTED=false;

	Boolean Init=true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.callsheet_table_header_phone);
		context = CallSheetActivity.this;

		Bundle extras = getIntent().getExtras();
		devId 			= extras.getString("devId");
		csCode			= extras.getString("csCode");
        cCashSales      = extras.getInt("cCashSales");
		cName			= extras.getString("customerName");
		cAddress		= extras.getString("customerAddress");

		initView();

		if (csCode==null || csCode=="") mode = MODE_ADD;
		else mode = MODE_EDIT;

		//Log.d("CallSheetActivity:onCreate",csCode+":"+mode);
		rlPayment = (RelativeLayout) findViewById(R.id.rl_payment);
		rlPayment.setVisibility(View.GONE);

		cust = new Customer();

		if (mode==MODE_ADD) {
			Log.e("cstype","add");
			cCode = extras.getString("customerCode");
			setModeAdd();
		}
		else {
			Log.e("cstype","edit");
			setModeEdit();
		}

		Log.d("SC Type:",""+cCashSales);
		

		String strDate = DateUtil.phShortDate(dateTime);

		try{
			etSONo.setText(String.valueOf(soNo));
			tvDate.setText(strDate);
			tvCname.setText(cName);
			tvCaddress.setText(cAddress);
//	        tvCterm.setText(cTerm);
//			tvCacctType.setText(cAcctType);
		}catch (Exception e){e.printStackTrace();}

		try {

			if(paymentReceived==0.0d) {
				etPayment.setText("");
			}
			else {
				etPayment.setText(""+paymentReceived);
			}

		}catch (Exception e){}


        timeNow = new Time();
        timeNow.setToNow();
        strDate = timeNow.format("%Y-%m-%d");
        setTitle("Sales Order");
        clickListeners();

//        try {
//            CallSheetItemDbManager dbcsi = new CallSheetItemDbManager(context);
//            dbcsi.open();
//            List<CallSheetItem> csItems = dbcsi.getList(dbcsi.getInfo2(dbcsi.getLastId()).getCscode());
//            dbcsi.close();
//            if (csItems.size() > 0) {
//                createCallSheet(1);
//                for (int i = 0; i < csItems.size(); i++) {
//                    addCsItem(csItems.get(i).getItemcode(), csItems.get(i).getPrice());
//                }
//                refreshList();
//            }
//        }catch (Exception e) {e.printStackTrace();}

	}

//	@Override
//	protected void onResume() {
//	    super.onResume();
//	   // refreshList(Ccode, strDate, strDate);
//
//	}

	@Override
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);

	}

	private void initView() {
		try {
			tvCname 		= (TextView) findViewById(R.id.tvCName);
			tvCaddress 		= (TextView) findViewById(R.id.tvAddress);
//        tvCacctType 	= (TextView) findViewById(R.id.tvAcctType);
			tvCterm			= (Spinner) findViewById(R.id.spTerms);

			tvDate			= (TextView) findViewById(R.id.tvDate);
			etSONo			= (EditText) findViewById(R.id.etSONo);
			spCashSales		= (Spinner)  findViewById(R.id.spnScType);
			etPayment		= (EditText) findViewById(R.id.etPayment);
			etRemarks		= (EditText) findViewById(R.id.etRemarks);

            ArrayAdapter<?> scArrayAdapter = new ArrayAdapter<Object>(this,
                    R.layout.csi_spinner, Master.sc_option);
            spCashSales.setAdapter(scArrayAdapter);
            spCashSales.setEnabled(false);

			ArrayAdapter<?> tArrayAdapter = new ArrayAdapter<Object>(this,
					R.layout.csi_spinner, ArrayDef.TERMS2);
			tvCterm.setAdapter(tArrayAdapter);
			tvCterm.setEnabled(false);

			tvTotalAmount 	= (TextView) findViewById(R.id.tvTotal);

			btAddSku 		= (Button) findViewById(R.id.btAddSku);
        btSignature		= (Button) findViewById(R.id.btSignature);

        btSignature.setEnabled(false);

			btSubmit 		= (Button) findViewById(R.id.btSubmit);
			btSubmit.setEnabled(false);
		}catch (Exception e){
			e.printStackTrace();
			Toast.makeText( getApplicationContext(), TAG + "encountered an error code 262!"   ,Toast.LENGTH_LONG ).show();
		};
	}

	private void setModeAdd() {
		try {
			soNo			= generateSONo();
			dateTime 		= System.currentTimeMillis();
			cust 			= DbUtil.getCustomerInfo(context, cCode);
			//Log.d(TAG+"SetModeAdd","Acct Type ID:"+cust.getAcctypid() +" - " + cust.getTermid());

			int acctTypeId = cust.getAcctypid();
			if (acctTypeId>ArrayDef.ACCT_TYPES.length-1) {
				acctTypeId = ArrayDef.ACCT_TYPES.length-1;
			}
			cAcctType		= ArrayDef.ACCT_TYPES[acctTypeId];

			int termId = cust.getTermid();
			Log.e("",""+termId);
			if(termId>ArrayDef.TERMS2.length-1) {
				termId = ArrayDef.TERMS2.length-1;
			}
			cTerm			= ArrayDef.TERMS2[termId];
			Log.e("asdasdasda","asdasd"+cTerm);
			cDiscount		= cust.getDiscount();
//			cCashSales		= 0;//cust.getCashSales();
			cBuffer			= cust.getBuffer();
			paymentReceived	= 0.0d;

            spCashSales.setSelection(cCashSales);
            tvCterm.setSelection(termId);

			btAddSku.setEnabled(true);
			btAddSku.setVisibility(View.VISIBLE);
			SUBMITTED=false;
			//discount			= cust.getDiscount();
		}catch (Exception e){
			e.printStackTrace();
			Toast.makeText( getApplicationContext(), TAG + "encountered an error code 279!"   ,Toast.LENGTH_LONG ).show();
		}

	}

	private void setModeEdit() {
		try {
			CallSheet cs = getCSInfo(csCode);
			soNo 			= cs.getSono();
			dateTime		= Long.valueOf(cs.getDate()) * 1000;
			cCode			= cs.getCcode();
			cust 			= DbUtil.getCustomerInfo(context, cCode);
			cAcctType		= ArrayDef.ACCT_TYPES[cust.getAcctypid()];
			cTerm			= ArrayDef.TERMS2[cust.getTermid()];
			cDiscount		= cust.getDiscount();
			cCashSales		= cs.getCash_sales();
			cBuffer			= cs.getBuffer();
			paymentReceived	= cs.getPayment();

			if(cs.getStatus()!=0) {
				btAddSku.setEnabled(false);
				btAddSku.setClickable(false);

				disableSubmitButton();

				etSONo.setEnabled(false);
				etSONo.setBackgroundColor(Color.LTGRAY);
				etSONo.setTextColor(Color.DKGRAY);


				spCashSales.setEnabled(false);
				spCashSales.setBackgroundColor(Color.LTGRAY);

				rlPayment.setBackgroundColor(Color.LTGRAY);

				etPayment.setEnabled(false);
				etPayment.setBackgroundColor(Color.LTGRAY);
				etPayment.setTextColor(Color.DKGRAY);


				SUBMITTED=true;
			}

			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

			updateList();
		}catch (Exception e){
			Toast.makeText( getApplicationContext(), TAG + "encountered an error code 330!"   ,Toast.LENGTH_LONG ).show();
		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.action_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.history:
				Intent intent = new Intent(context, CallSheetListActivity.class);
				Bundle b = new Bundle();

				b.putString("devId", devId);
				b.putString("ccode", null);
				intent.putExtras(b);
				startActivity(intent);
				break;

			case R.id.delete:
				//Log.w("Outbox","Aabout to empty outbox!");
				confirmDialog("Delete Sales Order!", "Are you sure you want to delete this sales order and all of the items?", DELETE_CALLSHEET,0);
				break;

		}
		return true;
	}

	private void clickListeners() {
		btAddSku.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				TimeInOut dbo = getLastInOut();

//	        	if(!spCashSales.getSelectedItem().toString().contains("BOOKING AWAY") && !isTimeIn(cCode,dbo)){
//	        		DialogManager.showAlertDialog(context,
//	    	                "Sales Order Verification",
//	    	                "You have not timed-in. Please verify.", false);
//	        		return;
//	        	}

//	        	if(spCashSales.getSelectedItem().toString().contains("BOOKING AWAY") && isTimeIn(cCode,dbo)){
//	        		DialogManager.showAlertDialog(context,
//	    	                "Sales Order Verification",
//	    	                "You are currently timed-in. Please choose 'BOOKING'.", false);
//	        		return;
//	        	}

				btAddSku.setEnabled(false);
				soNo = Integer.valueOf(etSONo.getText().toString());
				if(soNo<=0) {
					DialogManager.showAlertDialog(context, "Valid SO Number Required!", "Enter SO number.", null);
					btAddSku.setEnabled(true);
					etSONo.requestFocus();
				}
				else {
					createCallSheet(0);
				}
				btAddSku.setEnabled(true);
			}
		});

		btSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.v("Button Clicked!", "btSubmitIO");

                if(btSignature.isEnabled()) {
                    DialogManager.showAlertDialog(context, "Signature required!", "Please provide client signature before submitting.", null);
                    return;
                }

				disableSubmitButton();
                CallSheetItemDbManager db = new CallSheetItemDbManager(context);
                db.open();
                int qty = db.getTotalQuantity(csCode);
                db.close();
//	            double Amount = NumUtil.strToDouble(tvTotalAmount.getText().toString());
	            if(qty>0)  {
					submitCallSheet();
	            }
	            else {
	            	DialogManager.showAlertDialog(context, "No Actual Order!", "Enter at least 1 item with actual order.", null);
	            }

				enableSubmitButton();
			}
		});


        btSignature.setOnClickListener(new OnClickListener() {
	        @Override
			public void onClick(View v) {

	        	disableSignatureButton();

	            getSignature();


//	        	enableSignatureButton();
	        }
	    });



		spCashSales.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long arg3) {
				//if(!Init) {
				CallSheetDbManager db = new CallSheetDbManager(context);
				db.open();
				db.updateSCtype(csCode, position);
				db.close();
        			/*
        			if(position==0) {
        				rlPayment.setVisibility(View.GONE);
        			}
        			else {
        				rlPayment.setVisibility(View.GONE);
        			}*/

				cCashSales = position;
				//}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) { }
		});

		if(!SUBMITTED) {
			etSONo.addTextChangedListener(new SoBufTextWatcher(0));
		}

	}

	private int getTermid(){
		String term = tvCterm.getSelectedItem().toString();
		int id;
		AccountTypeDbManager db = new AccountTypeDbManager(this);
		db.open();
		id = db.getOtherDataId(term,6);
		db.close();

		return id;
	}

	private void createCallSheet(int past){
		if(mode==MODE_ADD) {
			csCode = createCsCode();

			CallSheet cs = new CallSheet();
			cs.setSono(soNo);
			cs.setCscode(csCode);
			cs.setCcode(cCode);
			cs.setDate(String.valueOf(dateTime/1000));
			cs.setCash_sales(cCashSales);
			cs.setBuffer(cBuffer);
			cs.setTermid(getTermid());
//			cs.setSupplier(atSupplier.getText().toString());
			Log.e("SC_TYPE",""+getTermid());

			CallSheetDbManager db = new CallSheetDbManager(context);
			db.open();
			db.AddCallSheet(cs);
			db.close();

			mode=MODE_EDIT;

			updateList();
		}
		//Create call sheet items
		//createCallSheetItem();
        if(past!=1) addItems(1);
//		if(past!=1) selectSingleItem(BOOKING);

	}

	private String createCsCode(){
		CallSheetDbManager db = new CallSheetDbManager(context);
		db.open();
		Integer lastId=db.getLastId()+1;
		db.close();

		String strCode = ("00000" + lastId).substring(String.valueOf(lastId).length());
		String strRandom = StringUtil.randomText(10);
		strCode = devId.substring(devId.length() - 5) + strRandom + strCode ;

		return strCode;
	}

	private CallSheet getCSInfo(String csCode) {
		CallSheetDbManager db = new CallSheetDbManager(context);
		db.open();
		CallSheet cs=db.getInfo(csCode);
		db.close();

		return cs;
	}

	private int generateSONo(){
		int sono=0;
		CallSheetDbManager db = new CallSheetDbManager(context);
		db.open();
		sono=db.getLastSOno();
		db.close();

		sono +=1;

		return sono;
	}

	private void addItems(int opt) {
//		selectSingleItem();
		DialogFragment newFragment = new ItemOptionDialogFragment();//("title",R.array.callsheet_options);
		Bundle args = new Bundle();
		args.putInt("opt", opt);
		args.putInt("catid", catid);
		newFragment.setArguments(args);
		newFragment.show(getFragmentManager(), "AddItems");
	}


	private void setupView(TableRow tr) {
		for(int i=2;i<tr.getChildCount();i++){
			View child=tr.getChildAt(i);
			child.setPadding(3, 3, 3, 3);

			child.setLayoutParams(new TableRow.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
			if (child instanceof TextView){
				((TextView) child).setTextSize(14);
			}else if (child instanceof EditText){
				((EditText) child).setGravity(Gravity.CENTER | Gravity.BOTTOM);
				((EditText) child).setTextSize(14);
				((EditText) child).setInputType(InputType.TYPE_CLASS_NUMBER);
			}else if (child instanceof Spinner){
				LayoutUtil.setMargins((Spinner) child, 0, 0, 6, 0);
			}
		}
	}

	private void setRowColor(TableRow tr, Integer color, Integer typeface) {
		for(int i=2;i<tr.getChildCount();i++){
			View child=tr.getChildAt(i);

			try {
				((TextView) child).setTextColor(color);
				((TextView) child).setTypeface(null,typeface);
			}catch (Exception e){};
		}
	}

	private void refreshList() {
		try {
			table.removeAllViews();
		} catch (Exception e) {
			e.printStackTrace();
		}
		updateList();
		if(!SUBMITTED) btAddSku.setEnabled(true);

	}

	private void updateList() {

		CallSheetItemDbManager db = new CallSheetItemDbManager(this);

		db.open();
		List<CallSheetItem> csItems = db.getList(csCode);
		Double totalAmount;
		totalAmount = db.getTotal(csCode);
		tvTotalAmount.setText(""+ NumUtil.getPhpCurrency(totalAmount,false));
		db.close();


		table = (TableLayout) findViewById(R.id.callsheetitem_table);
		table.setStretchAllColumns(true);

		TableRow header = new TableRow(this);
		header.setLayoutParams(new TableRow.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		header.setBackgroundColor(Color.BLACK);

		//Item ID
		TextView HItemId = new TextView(this);
		HItemId.setText("");
		HItemId.setLayoutParams(new TableRow.LayoutParams(1,1));
		HItemId.setVisibility(View.GONE);
		header.addView(HItemId);

		//Status
		TextView hStatus = new TextView(this);
		hStatus.setText("");
		hStatus.setLayoutParams(new TableRow.LayoutParams(1,1));
		hStatus.setVisibility(View.GONE);
		header.addView(hStatus);


		//Item Description Column
		TextView HitemDescription = new TextView(this);
		HitemDescription.setText("Description");
		HitemDescription.setGravity(Gravity.LEFT | Gravity.BOTTOM);
		HitemDescription.setBackgroundColor(Color.BLACK);
		//HitemDescription.setMinWidth(200);

		header.addView(HitemDescription);

		//PCKG Column
		TextView hPckg = new TextView(this);
		hPckg.setText("UNIT");
		hPckg.setBackgroundColor(Color.BLACK);
		hPckg.setGravity(Gravity.CENTER | Gravity.BOTTOM);
		hPckg.setVisibility(View.VISIBLE);
		header.addView(hPckg);

		//Past Inventory Column
		TextView hPastInvt = new TextView(this);
		hPastInvt.setText("PstI");
		//hPastInvt.setGravity(Gravity.LEFT | Gravity.BOTTOM);
		hPastInvt.setBackgroundColor(Color.BLACK);
//		hPastInvt.setVisibility(View.GONE);
		header.addView(hPastInvt);

		//Delivery Column
		TextView hDelivery = new TextView(this);
		hDelivery.setText("Dlvry");
		//hDelivery.setGravity(Gravity.LEFT | Gravity.BOTTOM);
		hDelivery.setBackgroundColor(Color.BLACK);
//		hDelivery.setVisibility(View.GONE);
		header.addView(hDelivery);

		//Present Inventory Column
		TextView hPresentInvt = new TextView(this);
//		hPresentInvt.setText("PrsI");
		hPresentInvt.setText("PrsI");
		//hPresentInvt.setGravity(Gravity.LEFT | Gravity.BOTTOM);
		hPresentInvt.setBackgroundColor(Color.BLACK);
//		hPresentInvt.setVisibility(View.GONE);
		header.addView(hPresentInvt);

		//OFFTAKE Column
		TextView hOfftake = new TextView(this);
		hOfftake.setText("OT");
		//hOfftake.setGravity(Gravity.LEFT | Gravity.BOTTOM);
		hOfftake.setBackgroundColor(Color.BLACK);
//		hOfftake.setVisibility(View.GONE);
		header.addView(hOfftake);

		//ICO Column
		TextView hIco = new TextView(this);
		hIco.setText("ICO");
		//hIco.setGravity(Gravity.LEFT | Gravity.BOTTOM);
		hIco.setBackgroundColor(Color.BLACK);
		hIco.setVisibility(View.GONE);
		header.addView(hIco);


		//SUGGESTED Column
		TextView hSuggested = new TextView(this);
		hSuggested.setText("Sug");
		//hSuggested.setGravity(Gravity.LEFT | Gravity.BOTTOM);
		hSuggested.setBackgroundColor(Color.BLACK);
//		hSuggested.setVisibility(View.GONE);
		header.addView(hSuggested);


		//Actual Order Column
		TextView hOrderQty = new TextView(this);
		hOrderQty.setText("Order");
		hOrderQty.setGravity(Gravity.LEFT | Gravity.BOTTOM);
		hOrderQty.setBackgroundColor(Color.BLACK);
		//hOrderQty.setMinimumWidth(120);
		hOrderQty.setVisibility(View.GONE);
		header.addView(hOrderQty);

		//Price  Column
		TextView hPrice = new TextView(this);
		hPrice.setText("Price");
		hOrderQty.setGravity(Gravity.CENTER | Gravity.BOTTOM);
		hOrderQty.setBackgroundColor(Color.BLACK);
		header.addView(hPrice);

		//Amount Column
		TextView hAmount = new TextView(this);
		hAmount.setText("Amount");
		hOrderQty.setGravity(Gravity.CENTER | Gravity.BOTTOM);
		hOrderQty.setBackgroundColor(Color.BLACK);
		header.addView(hAmount);

		//Item Code Column
		TextView hitemCode = new TextView(this);
		hitemCode.setText("ItemCode");
		hitemCode.setLayoutParams(new TableRow.LayoutParams(1,1));
		//hitemCode.setGravity(Gravity.LEFT | Gravity.BOTTOM);
		hitemCode.setVisibility(View.GONE);
		header.addView(hitemCode);


		table.addView(header);
		setupView(header);
		setRowColor(header,Color.WHITE,Typeface.BOLD);


		int row=0;
		Init=true;


		for (CallSheetItem item : csItems) 	{
			row++;
			final TableRow tr = new TableRow(this);
			tr.setLayoutParams(new TableRow.LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

			if(row % 2 == 0){
				if(SUBMITTED) tr.setBackgroundColor(0xFFDBDBEA);
				else tr.setBackgroundColor(0xFFE4FFCA);
			}
			else {
				tr.setBackgroundResource(R.drawable.cell_border);
			}

			//Item ID
			final TextView csiId = new TextView(this);
			csiId.setText(String.valueOf(item.getId()));
			csiId.setLayoutParams(new TableRow.LayoutParams(1,1));
			csiId.setVisibility(View.GONE);
			csiId.setId(TV_CSI_ID);
			tr.addView(csiId);

			//Status
			TextView status = new TextView(this);
			status.setText(String.valueOf(item.getStatus()));
			status.setLayoutParams(new TableRow.LayoutParams(1,1));
			status.setVisibility(View.GONE);
			tr.addView(status);

			//Item Description Column
			TextView itemDescription = new TextView(this);
			itemDescription.setText(item.getDescription()+"\n");
			itemDescription.setLayoutParams(new TableRow.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
			itemDescription.setGravity(Gravity.LEFT | Gravity.BOTTOM);
			itemDescription.setSingleLine(false);
			itemDescription.setMaxLines(4);
			//itemDescription.setMinWidth(140);
			itemDescription.setMaxWidth(120);
			tr.addView(itemDescription);


			//PCKG Spinner Column
			final Spinner etPckg = new Spinner(this);
			String pckg =  item.getPckg();
			etPckg.setVisibility(View.VISIBLE);
			etPckg.setLayoutParams(new TableRow.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
			//etPckg.setGravity(Gravity.CENTER | Gravity.BOTTOM);
			etPckg.setPadding(1, 1, 1, 1);
			if(SUBMITTED) {
				etPckg.setEnabled(false);
				etPckg.setClickable(false);
			}

//			AccountTypeDbManager datadb = new AccountTypeDbManager(this);
//			datadb.open();
//			String[] pckg_opt= datadb.getOtherData(10);
//			datadb.close();

			String[] pckg_opt = Master.pckg_option;
			etPckg.setEnabled(true);
			String[] drm = {"DRUM"};
			String[] pl = {"PAIL"};
			ItemDbManager dbItem = new ItemDbManager(context);
			dbItem.open();
			int pack = dbItem.getPacking(item.getItemcode());
			dbItem.close();
			if(pack==1000) {
				etPckg.setEnabled(false);
				pckg_opt=pl;
			}
			else if(pack==2000) {
				etPckg.setEnabled(false);
				pckg_opt = drm;
			}

			@SuppressWarnings({ "unchecked", "rawtypes" })
			ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
					R.layout.csi_spinner_phone, pckg_opt);
			etPckg.setAdapter(spinnerArrayAdapter);
			etPckg.setSelection(0);
//			if(pckg.equalsIgnoreCase(Master.PACKS)) {
//				etPckg.setSelection(Master.PCKG_PACK);
//			}
//			else etPckg.setSelection(Master.PCKG_UNIT);
			etPckg.setId(SP_PCKG);
			tr.addView(etPckg);

			//Past Inventory Column
			final TextView pastInvt = new TextView(this);
			pastInvt.setText(String.valueOf(item.getPastinvt()));
			pastInvt.setGravity(Gravity.CENTER | Gravity.BOTTOM);
//			pastInvt.setVisibility(View.GONE);
			tr.addView(pastInvt);

			//Deliveries
			final TextView delivery = new TextView(this);
			delivery.setText(String.valueOf(item.getDelivery()) );
			delivery.setGravity(Gravity.CENTER | Gravity.BOTTOM);
//			delivery.setVisibility(View.GONE);
			tr.addView(delivery);

			//Present Inventory Column
			final EditText presentInvt = new EditText(this);
			presentInvt.setText(String.valueOf(item.getPresentinvt()));
			presentInvt.setGravity(Gravity.CENTER | Gravity.BOTTOM);
//			presentInvt.setVisibility(View.GONE);

			if(SUBMITTED) {
				presentInvt.setBackgroundColor(0xFFEAEAEE);
				presentInvt.setClickable(false);
				presentInvt.setFocusable(false);
			}
			else presentInvt.setBackgroundColor(0xFFA9FF53);
			presentInvt.setInputType(InputType.TYPE_CLASS_NUMBER);
			presentInvt.addTextChangedListener(new invtTextWatcher(tr));
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(5);
            presentInvt.setFilters(fArray);
			tr.addView(presentInvt);

			presentInvt.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					String qtyString = presentInvt.getText().toString().trim();
					int qty = qtyString.equals("") ? 0:Integer.valueOf(qtyString);

					if(hasFocus){
						//Toast.makeText(getApplicationContext(), "got the focus", Toast.LENGTH_LONG).show();
						presentInvt.setBackgroundColor(0xFFFFF3CE);
						if(qty==0) presentInvt.setText("");
					}else {
						if(qty==0) presentInvt.setText("0");
						presentInvt.setBackgroundColor(0xFFA9FF53);
						//Toast.makeText(getApplicationContext(), "lost the focus", Toast.LENGTH_LONG).show();
					}
				}

			});


			//Offtake  Column
			final TextView offtake = new TextView(this);
			offtake.setText(String.valueOf(item.getOfftake()));
			offtake.setGravity(Gravity.CENTER | Gravity.BOTTOM);
			offtake.setId(TV_OFFTAKE);
//			offtake.setVisibility(View.GONE);
			tr.addView(offtake);

			//ICO  Column
			final TextView ico = new TextView(this);
			ico.setText(String.valueOf(item.getIco()));
			ico.setGravity(Gravity.CENTER | Gravity.BOTTOM);
			ico.setId(TV_ICO);
			ico.setVisibility(View.GONE);
			tr.addView(ico);

			//Suggested  Column
			final EditText suggested = new EditText(this);
			suggested.setText(String.valueOf(item.getSuggested()));
			suggested.setId(TV_SUGGEST);
            suggested.setMinimumWidth(30);
            if(SUBMITTED) {
                suggested.setBackgroundColor(0xFFEAEAEE);
                suggested.setClickable(false);
                suggested.setFocusable(false);
            }
            else suggested.setBackgroundColor(0xFFA9FF53);
            suggested.addTextChangedListener(new qtyTextWatcher(tr));
            suggested.setInputType(InputType.TYPE_CLASS_NUMBER);
            suggested.setFilters(fArray);
            tr.addView(suggested);

            suggested.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    String qtyString = suggested.getText().toString().trim();
                    int qty = qtyString.equals("") ? 0:Integer.valueOf(qtyString);

                    if(hasFocus){
                        suggested.setBackgroundColor(0xFFFFF3CE);
                        if(qty==0) suggested.setText("");
                    }else {
                        suggested.setBackgroundColor(0xFFA9FF53);
                        if(qty==0) suggested.setText("0");

                    }
                }

            });

			//Actual Order  Column
			final EditText orderqty = new EditText(this);
			orderqty.setText(String.valueOf(item.getOrder()));
			orderqty.setGravity(Gravity.CENTER | Gravity.BOTTOM);
			orderqty.setVisibility(View.GONE);
			orderqty.setMinimumWidth(30);
			if(SUBMITTED) {
				orderqty.setBackgroundColor(0xFFEAEAEE);
				orderqty.setClickable(false);
				orderqty.setFocusable(false);
			}
			else orderqty.setBackgroundColor(0xFFA9FF53);
			orderqty.setId(ET_ORDER);
			orderqty.addTextChangedListener(new qtyTextWatcher(tr));
			orderqty.setInputType(InputType.TYPE_CLASS_NUMBER);
			tr.addView(orderqty);

			orderqty.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					String qtyString = orderqty.getText().toString().trim();
					int qty = qtyString.equals("") ? 0:Integer.valueOf(qtyString);

					if(hasFocus){
						orderqty.setBackgroundColor(0xFFFFF3CE);
						if(qty==0) orderqty.setText("");
					}else {
						orderqty.setBackgroundColor(0xFFA9FF53);
						if(qty==0) orderqty.setText("0");

					}
				}

			});



			//Price per unit column
//			final TextView price = new TextView(this);
//			price.setText(String.format("%.2f",item.getPrice()));
//			price.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
//			price.setId(TV_PRICE);
//			tr.addView(price);
			final EditText price = new EditText(this);
			price.setText(String.format("%.2f",item.getPrice()));
			price.setGravity(Gravity.CENTER | Gravity.BOTTOM);
			price.setMinimumWidth(5);
            fArray[0] = new InputFilter.LengthFilter(8);
            price.setFilters(fArray);
//			TableLayout.LayoutParams tparams = new TableLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
//			tparams.setMargins(10, 10, 10, 10);
//			price.setLayoutParams(tparams);
			if(SUBMITTED) {
				price.setBackgroundColor(0xFFEAEAEE);
				price.setClickable(false);
				price.setFocusable(false);
			}
			else price.setBackgroundColor(0xFFA9FF53);
			price.setId(TV_PRICE);
			price.addTextChangedListener(new priceTextWatcher(tr));
			price.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
			tr.addView(price);

			price.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					String prString = price.getText().toString().trim();
					Double pr = prString.equals("") ? 0.0:Double.valueOf(prString);

					if(hasFocus){
						price.setBackgroundColor(0xFFFFF3CE);
						if(pr==0.0) price.setText("");
					}else {
						price.setBackgroundColor(0xE9EAED);
						DecimalFormat df = new DecimalFormat("#.00");
						if(pr==0.0) price.setText("0.00");
						else price.setText(df.format(pr));

					}
				}

			});


			//Amount Column
			final TextView amount = new TextView(this);
			DecimalFormat df = new DecimalFormat("#.00");
			Double amt = item.getOrder() *  item.getPrice();
//			amount.setText(NumUtil.getPhpCurrency(amt,false));
			amount.setText(df.format(amt));
			amount.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
			amount.setId(TV_AMOUNT);
			tr.addView(amount);

			//checkbox
//			final CheckBox cb = new CheckBox(this);
//			tr.addView(cb);

			//Item Code Column
			final TextView itemCode = new TextView(this);
			itemCode.setText(item.getItemcode());
			itemCode.setLayoutParams(new TableRow.LayoutParams(1,1));
			//itemCode.setGravity(Gravity.LEFT | Gravity.BOTTOM);
			itemCode.setVisibility(View.GONE);
			itemCode.setId(TV_ITEM_CODE);
			tr.addView(itemCode);

			etPckg.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long arg3) {
					if(!Init) {
						Double pPrice =0.0;
						String strQty = orderqty.getText().toString();
						int qty = strQty.equals("") ? 0:Integer.valueOf(strQty);


						CallSheetItemDbManager db = new CallSheetItemDbManager(context);
						db.open();

						int pstInvt = db.getPastInvt(csCode,cCode,
								itemCode.getText().toString(),
								etPckg.getSelectedItem().toString() );

						int dlvry = db.getDeliveryQty(cCode,
								itemCode.getText().toString(),
								etPckg.getSelectedItem().toString());

						pastInvt.setText(""+pstInvt);

//		        		if(item.getPrice()){
//						if(price.getText().toString().isEmpty()){
//							pPrice = getPrice(itemCode.getText().toString(),position);
//							price.setText(""+pPrice);
//						}
//						else pPrice = Double.parseDouble(price.getText().toString());
						if (price.getText().toString().isEmpty()) {
							pPrice = getPrice(itemCode.getText().toString(), position);
						} else pPrice = getPrice(itemCode.getText().toString(), position);

						price.setText(""+pPrice);

						amount.setText(""+NumUtil.getPhpCurrency(qty*pPrice,false));


						//Save CS_ITEM
						CallSheetItem csi = new CallSheetItem();

						csi.setId(Integer.valueOf(csiId.getText().toString()));
						csi.setPckg(etPckg.getSelectedItem().toString());
						csi.setPastinvt(pstInvt);
						csi.setDelivery(dlvry);
						csi.setPrice(pPrice);
						db.UpdatePckg(csi);

						Double totalAmount;
						totalAmount = db.getTotal(csCode);
						tvTotalAmount.setText(""+NumUtil.getPhpCurrency(totalAmount,false));

						db.close();
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) { }
			});


			registerForContextMenu(tr);

			table.addView(tr);
			setupView(tr);

			if(!SUBMITTED) {
				setRowColor(tr,Color.BLACK,Typeface.NORMAL);
				if(cCashSales==0) enableSubmitButton();
				else {

					rlPayment.setVisibility(View.GONE);
				}
			}
			else {
				setRowColor(tr,Color.DKGRAY,Typeface.NORMAL);
				amount.setTextColor(Color.DKGRAY);
				disableSubmitButton();

				disableSignatureButton();
			}
			amount.setTypeface(null,Typeface.BOLD);
		}

		Init=false;
		if(row>0 && !SUBMITTED) {
			enableSubmitButton();

			enableSignatureButton();
		}
	}

	private void getSignature() {
		Intent intent = new Intent(context, GetSignatureActivity.class);
		Bundle b = new Bundle();

		b.putString("devId", devId);
		b.putString("txNo", ""+soNo);
		b.putString("customerCode", cCode);
		b.putString("customerName", cName);
		b.putString("customerAddress", cAddress);
		intent.putExtras(b);
		startActivity(intent);
	}

	private void submitCallSheet() {

		final ProgressDialog progress = ProgressDialog.show(this, "Submitting",
				"Please wait...", true);

		new Thread(new Runnable() {
			@Override
			public void run(){
				sendCallSheet();
				runOnUiThread(new Runnable() {
					@Override
					public void run(){
						progress.dismiss();
					}
				});
			}
		}).start();

	}



	private void sendCallSheet() {
		//Boolean submitted=false;
		Long sysTime = System.currentTimeMillis();
		String trDate =  String.valueOf(sysTime/1000);
		double totalAmount = NumUtil.strToDouble(tvTotalAmount.getText().toString());

		CallSheetDbManager db = new CallSheetDbManager(this);
		db.open();

		CallSheet cs = db.getInfo(csCode);

		String message="";

		devId = DbUtil.getSetting(context, Master.DEVID);

		message = Master.CMD_CALLSHEET 				+ " " +
				devId 								+ ";" +
				cs.getCcode() 						+ ";" +
				cs.getCscode() 						+ ";" +
				cs.getDate() 						+ ";" +
				cs.getSono().toString()				+ ";" +
				cBuffer 							+ ";" +
				cCashSales							+ ";" +
				totalAmount							+ ";" +
				trDate								+ ";" +
				cs.getId()							+ ";" +
				etRemarks.getText().toString()    	+ ";" +
				getTermid();

		DbUtil.saveMsg(context,DbUtil.getGateway(context), message);

		db.updateStatus(cs.getId(), 1); //1=Transmitted

		db.close();
		//submitted=true;

		CallSheetItemDbManager dbi = new CallSheetItemDbManager(this);
		dbi.open();
		List<CallSheetItem> csi = dbi.getList(csCode);
		dbi.close();

		for (CallSheetItem item : csi) 	{
			if(item.getStatus()==0 && item.getOrder()>0) {
				message = Master.CMD_CALLSHEET_ITEM + " " +
						devId 					+ ";" +
						item.getCscode() 		+ ";" +
						item.getItemcode()		+ ";" +
						item.getPckg() 			+ ";" +
						"" 						+ ";" +//
						""						+ ";" +//
						""						+ ";" +//
						""						+ ";" +//
						""						+ ";" +//
						item.getSuggested()		+ ";" +//
						item.getOrder()			+ ";" +
						item.getPrice()			+ ";" +
						item.getId();

				DbUtil.saveMsg(context,DbUtil.getGateway(context), message);

				String message2 = Master.CMD_CALLSHEET_INVEN + " " +
						devId 					+ ";" +
						item.getCscode() 		+ ";" +
						item.getItemcode()		+ ";" +
						item.getPastinvt() 		+ ";" +//
						item.getDelivery()		+ ";" +//
						item.getPresentinvt()	+ ";" +//
						item.getOfftake()		+ ";" +//
						item.getIco()			+ ";" +//
						item.getId();

				DbUtil.saveMsg(context,DbUtil.getGateway(context), message2);

				dbi.open();
				dbi.updateStatus(item.getId(), 1); //1=Transmitted
				dbi.close();

			}
		}


		//if (submitted) Toast.makeText(getApplicationContext(), "Callsheet has been submitted to the central server!" , Toast.LENGTH_SHORT).show();

		finish();

	}


	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {

		selectedRow = (TableRow) v;

		menu.setHeaderIcon(R.drawable.delete32);
		menu.setHeaderTitle("Delete Callsheet");

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu_delete_callsheet, menu);

	}

	@Override
	public boolean onContextItemSelected(MenuItem mItem) {
		String title;
		String message;

		TextView strSO = (TextView) selectedRow.getChildAt(0);
		TextView status = (TextView) selectedRow.getChildAt(1);
		TextView itemDescription = (TextView) selectedRow.getChildAt(2);


		Integer Id = Integer.valueOf(strSO.getText().toString());

		if (Integer.valueOf(status.getText().toString())!=0) {
			DialogManager.showAlertDialog(context,"Locked Callsheet!",
					"This sales order had already been transmitted to the central server and cannot be modified!", true);
			return false;
		}

		switch (mItem.getItemId()) {
			case R.id.delete_callsheet_item:

				title = "Delete Callsheet Item";
				message = "Are you sure you want delete " + itemDescription.getText().toString() + "?";
				confirmDialog(title,  message, DELETE_CALLSHEET_ITEM,Id,csCode);
				return true;

			case R.id.delete_callsheet:
				title = "Delete Entire Callsheet";
				message = "Are you sure you want delete the entire sales order?";
				confirmDialog(title,  message, DELETE_CALLSHEET,Id,csCode);
				return true;


		}
		return false;
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onStop(){
		super.onStop();
	}


	private void confirmedDeleteCallSheetItem(int Id) {
		CallSheetItemDbManager db = new CallSheetItemDbManager(this);
		db.open();
		db.delete(Id);
		db.close();

		refreshList();
	}


	private void confirmedDeleteCallSheet(String csCode) {
		CallSheetItemDbManager db = new CallSheetItemDbManager(this);
		db.open();
		db.deleteCallSheet(csCode);
		db.close();

		CallSheetDbManager csdb = new CallSheetDbManager(this);
		csdb.open();
		csdb.deleteCallSheet(csCode);
		csdb.close();

		finish();
	}


	public void confirmDialog(String title, String message, Integer option,Integer id,String cscode) {

		final Integer Option = option;
		final Integer Id = id;
		final String csCode=cscode;

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);

		// Setting Icon to Dialog
		alertDialog.setIcon(R.drawable.delete32);

		// Setting Positive "Yes" Button
		alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog,int which) {

				switch(Option) {
					case DELETE_CALLSHEET_ITEM:
						confirmedDeleteCallSheetItem(Id);
						break;
					case DELETE_CALLSHEET:
						confirmedDeleteCallSheet(csCode);
						break;
				}
				//Toast.makeText(getApplicationContext(), Title + " confirmed!" , Toast.LENGTH_SHORT).show();
			}
		});

		// Setting Negative "NO" Button
		alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.cancel();
			}
		});

		// Showing Alert Message
		alertDialog.show();

	}

	@Override
	public void onSelectOption(Integer position, String strValue) {
		// TODO Auto-generated method stub
		ItemDbManager db = new ItemDbManager(context);
		db.open();
		int _id = db.getCatId(strValue,1);
		if(_id>0){
			catid = _id;
			addItems(2);
		}
		else {
			icatid=db.getCatId(strValue,2);
			Log.e("catid",""+catid);
			Log.e("icatid",""+icatid);
			selectSingleItem(catid,icatid);
		}


		db.close();

	}

	int catid=0;
	int icatid=0;


	/***********************************************
	 ****** Add Single Item ************************
	 ***********************************************/

	private void selectSingleItem(int catid, int icatid) {
		Intent intent = new Intent(context, ItemListActivity.class);
		Bundle b = new Bundle();

		b.putString("csCode", csCode);
		b.putInt("catid", catid);
		b.putInt("icatid", icatid);
		intent.putExtras(b);
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if(resultCode == RESULT_OK){
				String itemCode=data.getStringExtra("itemCode");
				//String itemDescription=data.getStringExtra("itemDescription");
				Double ppPack=data.getDoubleExtra("ppPack",0.0);
				//Double ppUnit=data.getDoubleExtra("ppUnit",0.0);
				addCsItem (itemCode, ppPack);
				refreshList();

			}
			if (resultCode == RESULT_CANCELED) {
				//Write your code if there's no result
			}
		}
	}//onActivityResult

	private void addCsItem(String itemCode,Double Price) {
		CallSheetItemDbManager db = new CallSheetItemDbManager(context);
		db.open();

		int deliveredQtyPack = db.getDeliveryQty(cCode, itemCode, Master.PACKS);
		//int deliveredQtyUnit = db.getDeliveryQty(Ccode, itemCode, Master.UNIT);
		int pastInvt = db.getPastInvt(csCode,cCode, itemCode, Master.PACKS );
		int suggested = db.getSuggested(csCode,cCode, itemCode, Master.PACKS );
        Log.e("suggested",""+suggested);
		CallSheetItem csi = new CallSheetItem();

		csi.setCscode(csCode);
		csi.setItemcode(itemCode);
		csi.setPckg(Master.PACKS);
		csi.setPastinvt(pastInvt);
        csi.setSuggested(suggested);
		csi.setDelivery(deliveredQtyPack);
		csi.setPrice(Price);

		db.AddCallSheetItem(csi);

		db.close();

	}



	private Double getPrice(String itemCode, int iPckg) {
		Double pPrice=0.0;
		ItemDbManager db = new ItemDbManager(context);
		db.open();
		pPrice = db.getPrice(itemCode, iPckg);
		db.close();

		return pPrice;
	}

	private void enableSubmitButton() {
		btSubmit.setBackgroundResource(R.drawable.btn_ripple2);
		btSubmit.setTextColor(Color.BLUE);
		btSubmit.setEnabled(true);
		btSubmit.setVisibility(View.VISIBLE);

	}

	private void disableSubmitButton() {
		btSubmit.setBackgroundResource(R.drawable.rounded_gray_button);
		btSubmit.setEnabled(false);
		btSubmit.setTextColor(Color.LTGRAY);


	}

	private void enableSignatureButton() {
		btSignature.setBackgroundResource(R.drawable.btn_ripple2);
		btSignature.setTextColor(Color.BLUE);
		btSignature.setEnabled(true);
		btSignature.setVisibility(View.VISIBLE);
	}

	private void disableSignatureButton() {
		btSignature.setBackgroundResource(R.drawable.rounded_gray_button);
		btSignature.setEnabled(false);
    	btSignature.setTextColor(Color.LTGRAY);
    	btSignature.setVisibility(View.INVISIBLE);
	}

	private class SoBufTextWatcher implements TextWatcher{
		private int Option;
		private SoBufTextWatcher(int option) {
			this.Option = option;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			//do nothing
		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			//do nothing
		}
		@Override
		public void afterTextChanged(Editable s) {
			if(!Init) {
				String qtyString = s.toString().trim();
				int value = qtyString.equals("") ? 0:Integer.valueOf(qtyString);


				CallSheetDbManager db = new CallSheetDbManager(context);
				db.open();
				db.updateSonoBuffer(csCode,Option,value);
				db.close();
			}
			return;
		}
	}


	private class qtyTextWatcher implements TextWatcher{

		private View view;
		private qtyTextWatcher(View view) {
			this.view = view;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			//do nothing
		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			//do nothing
		}
		@Override
		public void afterTextChanged(Editable s) {

			try{
				String qtyString = s.toString().trim();
				int quantity = qtyString.equals("") ? 0:Integer.valueOf(qtyString);

				//Log.d(TAG+" qtyTextWatcher","quantity="+quantity);

				TextView tvPrice = (TextView) view.findViewById(TV_PRICE);
				String strPrice = tvPrice.getText().toString();
				Double price = strPrice.equals("") ? 0.0:Double.valueOf(strPrice);

				TextView tvAmount = (TextView) view.findViewById(TV_AMOUNT);
				tvAmount.setText(NumUtil.getPhpCurrency(quantity*price,false));

				if(!Init){
					//Update CallSheetItem OrderQTy
					TextView csiId = (TextView) view.findViewById(TV_CSI_ID);
					//Save CS_ITEM
					CallSheetItem csi = new CallSheetItem();

					csi.setId(Integer.valueOf(csiId.getText().toString()));
					csi.setOrder(quantity);

					CallSheetItemDbManager db = new CallSheetItemDbManager(context);
					db.open();
					db.UpdateOrderQty(csi);

					Double totalAmount;
					totalAmount = db.getTotal(csCode);
					tvTotalAmount.setText(""+NumUtil.getPhpCurrency(totalAmount,false));
					db.close();
				}
			}catch(Exception e){}
			return;
		}
	}

	private class priceTextWatcher implements TextWatcher{

		private View view;
		private priceTextWatcher(View view) {
			this.view = view;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			//do nothing
		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			//do nothing
		}
		@Override
		public void afterTextChanged(Editable s) {

			try{
				String prString = s.toString().trim();
				Double price = prString.equals("") ? 0:Double.valueOf(prString);

				//Log.d(TAG+" qtyTextWatcher","quantity="+quantity);

				EditText etQty = (EditText) view.findViewById(TV_SUGGEST);
				String strQty = etQty.getText().toString();
				int quantity = strQty.equals("") ? 0:Integer.valueOf(strQty);

				TextView tvAmount = (TextView) view.findViewById(TV_AMOUNT);
				tvAmount.setText(NumUtil.getPhpCurrency(quantity*price,false));

				if(!Init){
					//Update CallSheetItem OrderQTy
					TextView csiId = (TextView) view.findViewById(TV_CSI_ID);
					//Save CS_ITEM
					CallSheetItem csi = new CallSheetItem();
					Log.e("price",""+price);
					csi.setId(Integer.valueOf(csiId.getText().toString()));
					csi.setPrice(price);

					CallSheetItemDbManager db = new CallSheetItemDbManager(context);
					db.open();
					db.UpdateOrderPrice(csi);

					Double totalAmount;
					totalAmount = db.getTotal(csCode);
					tvTotalAmount.setText(""+NumUtil.getPhpCurrency(totalAmount,false));
					db.close();
				}
			}catch(Exception e){};
			return;
		}
	}

	/*************************************************************
	 *** Confirm Dialog
	 ************************************************************/

	public void confirmDialog(String title, String message, Integer option,Integer id) {

		final Integer Option = option;
		final String Title = title;
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setIcon(R.drawable.delete32);
		alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog,int which) {

				switch(Option) {
					case DELETE_CALLSHEET:
						//confirmedDeleteAll();
						break;

				}

				Toast.makeText(getApplicationContext(), Title + " confirmed!" , Toast.LENGTH_SHORT).show();
			}
		});

		alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.cancel();
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}

	private class invtTextWatcher implements TextWatcher{

		private View view;
		private invtTextWatcher(View view) {
			this.view = view;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			//do nothing
		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			//do nothing
		}
		@Override
		public void afterTextChanged(Editable s) {
			if(!Init){
				String invtString = s.toString().trim();
				int invt = invtString.equals("") ? 0:Integer.valueOf(invtString);

				TextView tvItemCode = (TextView) view.findViewById(TV_ITEM_CODE);
				String itemCode = tvItemCode.getText().toString();

				Spinner spPCKG = (Spinner) view.findViewById(SP_PCKG);
				String pckg = spPCKG.getSelectedItem().toString();

				CallSheetItemDbManager db = new CallSheetItemDbManager(context);
				db.open();
				int offtake = db.getOffTake(csCode, cCode, itemCode, pckg, invt);
				db.close();

				TextView tvOfftake = (TextView) view.findViewById(TV_OFFTAKE);
				tvOfftake.setText(""+offtake);


				CustomerDbManager cdb = new CustomerDbManager(context);
				cdb.open();
				int frequency = cdb.getFrequency(cCode);
				cdb.close();
                Log.e("freq","freq"+frequency);

				int ico = offtake * ((frequency * 7));

				TextView tvICO = (TextView) view.findViewById(TV_ICO);
				tvICO.setText(""+ico);

				int suggest =(int) (offtake+(offtake*0.10));
				if(invt>0) suggest = offtake;
//				int suggest = ico - invt;
//				if(suggest<0) suggest=0;
				TextView tvSuggest = (TextView) view.findViewById(TV_SUGGEST);
				tvSuggest.setText(""+suggest);

				//Save CS_ITEM
				TextView csiId = (TextView) view.findViewById(TV_CSI_ID);
				CallSheetItem csi = new CallSheetItem();

				//Log.d("CallSheetActivity:invtTextWatcher",csiId.getText().toString()+":"+invt);

				csi.setId(Integer.valueOf(csiId.getText().toString()));
				csi.setPresentinvt(invt);
				csi.setOfftake(offtake);
				csi.setIco(ico);
				csi.setSuggested(suggest);

				CallSheetItemDbManager csidb = new CallSheetItemDbManager(context);
				csidb.open();
				csidb.UpdateInvt(csi);
				csidb.close();
			}
			return;
		}
	}

//	private int getSupplierId(String supplier){
//		int spid = 0;
//		CustomerDbManager db = new CustomerDbManager(context);
//		db.open();
//		spid = db.getSupplierId(supplier);
//		db.close();
//		return spid;
//	}


//	private class AsyncUpdateList extends AsyncTask<String, String, String> { 		
//				
//		
//		Context context;
//		
//	     public AsyncUpdateList(Context _context) { 
//	       
//	    } 
//		
//	 
//		@Override 
//	    protected String doInBackground(String... params) { 
//	    	String result="";
//	        
//	    	return result;
//	    } 
//	    
//	    @Override 
//	    protected void onPostExecute(String result) { 
//	    }
//	} 

	private TimeInOut getLastInOut() {
		TimeInOut dbo;

		InOutDbManager db = new InOutDbManager(context);
		db.open();
		dbo  = db.getLastTransaction();
		db.close();

		return dbo;
	}

	private Boolean isTimeIn(String ccode, TimeInOut dbo) {
		Boolean timeIn=false;

		if(dbo==null) return false;

		if ((dbo.getInout()==1) && (dbo.getCustomerCode().equalsIgnoreCase(ccode))) {
			Long sysTime = System.currentTimeMillis();
			String today = DateUtil.strDate(sysTime);

			Long longDate = Long.valueOf(dbo.getDateTime()) * 1000;
			String strDate = DateUtil.strDate(longDate);

			if (strDate.contentEquals(today)) {
				timeIn=true;
			}

		}
		return timeIn;
	}


}