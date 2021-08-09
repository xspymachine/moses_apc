package com.xpluscloud.moses_apc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.Time;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.xpluscloud.moses_apc.dbase.ItemDbManager;
import com.xpluscloud.moses_apc.dbase.ReturnDbManager;
import com.xpluscloud.moses_apc.getset.Customer;
import com.xpluscloud.moses_apc.getset.Pr;
import com.xpluscloud.moses_apc.getset.Return;
import com.xpluscloud.moses_apc.util.DbUtil;
import com.xpluscloud.moses_apc.util.DialogManager;
import com.xpluscloud.moses_apc.util.Master;
import com.xpluscloud.moses_apc.util.NumUtil;
import com.xpluscloud.moses_apc.util.StringUtil;

import java.util.List;


public class ReturnActivity extends Activity {
	
	String TAG ="ReturnActivity";
	
	String devId;
	
	final int DELETE_RETURN_ITEM	= 0;
	final int DELETE_ALL	= 0;
	
	
	final int TV_PR_ID		= 100001;
	final int TV_ITEM_CODE	= 100002;
	final int TV_DESCR 		= 100003; 
	final int SP_PCKG		= 100004;
	final int ET_QTY		= 100010;
	final int TV_PRICE		= 100011;
	final int TV_AMOUNT		= 100012;
	final int TV_STATUS		= 100013;
	
	final int MODE_ADD = 0;
	final int MODE_EDIT = 1;
	
	int mode;
	Context context;

	public String prCode;
	
	public String cCode;
	public String cName;
	public String cAddress;
	
	public Integer prNo;
	public Long dateTime;
	
	
	TextView tvCname;
	TextView tvCaddress;
	
	TextView tvDate;
	EditText etPRNo;
	
	public Time timeNow;
	
	String strDate;
	
	Button btAddPr;
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
		setContentView(R.layout.return_table_header);		
		context = ReturnActivity.this;
		
		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//		devId = telephonyManager.getDeviceId();
		devId = Master.getDevId2(context);
		Bundle extras = getIntent().getExtras();
		
		//devId 			= extras.getString("devId");
		prCode			= extras.getString("prCode");
		
		timeNow = new Time();
        timeNow.setToNow();                
        strDate = timeNow.format("%Y-%m-%d");        
        setTitle("Product Returns");        
		
		initView();
        
		
		if (prCode==null || prCode=="") mode = MODE_ADD;
		else mode = MODE_EDIT;
		
		
		cust = new Customer();
		
		if (mode==MODE_ADD) {
			cCode = extras.getString("customerCode");
			setModeAdd();
		}
		else {
			setModeEdit();
		}
		
		cName 			= cust.getName();
	    cAddress		= cust.getAddress();
        
        etPRNo.setText(String.valueOf(prNo));
        tvDate.setText(strDate);
        tvCname.setText(cName);
        tvCaddress.setText(cAddress);
        
        clickListeners();
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	  // refreshList();
	    
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
		
	}
	
	private void initView() {
		tvCname 		= (TextView) findViewById(R.id.tvCName);
        tvCaddress 		= (TextView) findViewById(R.id.tvAddress);

	 	tvDate			= (TextView) findViewById(R.id.tvDate);
        etPRNo			= (EditText) findViewById(R.id.etPRNo);
        
        tvTotalAmount 	= (TextView) findViewById(R.id.tvTotal);
       
        btAddPr 		= (Button) findViewById(R.id.btAddPr);
        btSignature		= (Button) findViewById(R.id.btSignature);
        
        btSignature.setEnabled(false);
        
        btSubmit 		= (Button) findViewById(R.id.btSubmit);
        btSubmit.setEnabled(false);
	}
	
	private void setModeAdd() {
		prNo			= generatePRNo();
		dateTime 		= System.currentTimeMillis();
		cust 			= DbUtil.getCustomerInfo(context, cCode);
        btAddPr.setEnabled(true);
        btAddPr.setVisibility(View.VISIBLE);
        
        SUBMITTED=false;
        //discount			= cust.getDiscount();
	}
	
	private void setModeEdit() {
		Pr pr 			= getPRInfo(prCode);
		prNo 			= pr.getPrno();
		dateTime		= Long.valueOf(pr.getDate()) * 1000;
		cCode			= pr.getCcode();
		cust 			= DbUtil.getCustomerInfo(context, cCode); 
		
        
        if(pr.getStatus()!=0) {
        	btAddPr.setEnabled(false);
        	btAddPr.setClickable(false);
        	
        	disableSubmitButton();
        	
        	etPRNo.setEnabled(false);
        	etPRNo.setBackgroundColor(Color.LTGRAY);
        	etPRNo.setTextColor(Color.DKGRAY);
        	SUBMITTED=true;
        }
        
        getWindow().setSoftInputMode(
        	      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        
        updateList();	
	}
	
	
	private void clickListeners() {
        btAddPr.setOnClickListener(new OnClickListener() {
	        @Override
			public void onClick(View v) {
	        	btAddPr.setEnabled(false);
	        	prNo = Integer.valueOf(etPRNo.getText().toString());
	        	if(prNo<=0) {	    			
	    			DialogManager.showAlertDialog(context, "Valid PR Number Required!", "Enter PR number.", null);
	    			btAddPr.setEnabled(true);
	    			etPRNo.requestFocus();	    			
	    		}
	        	else {	        	
	        		createReturn();
	        	}
	        	btAddPr.setEnabled(true);
	        } 
	    });	
        
        btSubmit.setOnClickListener(new OnClickListener() {
	        @Override
			public void onClick(View v) {
	            //Log.v(TAG, ":btSubmit clicked"); 
	            
	            disableSubmitButton();
	            
	            double Amount = NumUtil.strToDouble(tvTotalAmount.getText().toString());
	            
	            if(Amount>0.0)  {
	            	submitReturn();
	            }
	            else {
	            	DialogManager.showAlertDialog(context, "No Product Return!", "Enter at least 1 item returned.", null);
	            }
	            
	            enableSubmitButton();
	        } 
	    });	
        
        
        btSignature.setOnClickListener(new OnClickListener() {
	        @Override
			public void onClick(View v) {
	        	disableSignatureButton();
	            getSignature();
	        	enableSignatureButton();
	        } 
	    });	
	}
	
	
	
	
	private void createReturn(){
		if(mode==MODE_ADD) {
			prCode = generatePrCode();
			mode=MODE_EDIT;
		}
		selectSingleItem();
	}
		
	private String generatePrCode(){
		 ReturnDbManager db = new ReturnDbManager(context);
		 db.open();		
		 Integer lastId=db.getLastId()+1;
		 db.close();
		
		 String strCode = ("00000" + lastId).substring(String.valueOf(lastId).length());
		 String strRandom = StringUtil.randomText(10);
		 strCode = devId.substring(devId.length() - 5) + strRandom + strCode ;
		
		return strCode;
	 }

	private Pr getPRInfo(String prCode) {
		ReturnDbManager db = new ReturnDbManager(context);
		 db.open();		
		 Pr pr=db.getInfo(prCode);			
		 db.close();
		 
		 return pr;
	}
	
	private int generatePRNo(){
		int prno=0;
		ReturnDbManager db = new ReturnDbManager(context);
		db.open();		 
		prno=db.getLastPRno();			
		db.close();
		
		prno +=1;
		
		return prno;
	}
	
		
	private void setupView(TableRow tr) {
		for(int i=2;i<tr.getChildCount();i++){						
	       View child=tr.getChildAt(i);
	       child.setPadding(4, 10, 4, 10);
	       child.setLayoutParams(new TableRow.LayoutParams(
	    		   android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
	    		   android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
	       if (child instanceof TextView){
		       ((TextView) child).setTextSize(16);
	       }else if (child instanceof EditText){
	    	   ((EditText) child).setGravity(Gravity.CENTER | Gravity.BOTTOM);
		       ((EditText) child).setTextSize(16);
		       ((EditText) child).setInputType(InputType.TYPE_CLASS_NUMBER);
	       }else if (child instanceof Spinner){
	    	   ((Spinner) child).setGravity(Gravity.CENTER | Gravity.BOTTOM);
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
	    if(!SUBMITTED) btAddPr.setEnabled(true);
	    
	}
	
	private void updateList() {
		
		ReturnDbManager db = new ReturnDbManager(this);		
			
		db.open();		
		List<Return> returns = db.getList(prCode);
		Double totalAmount;
		totalAmount = db.getTotal(prCode);
		tvTotalAmount.setText(""+NumUtil.getPhpCurrency(totalAmount,false));
		db.close();
		
		
		table = (TableLayout) findViewById(R.id.pr_table);
		
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
		
		//Item Code Column
		TextView hitemCode = new TextView(this);
		hitemCode.setText("ItemCode");
		hitemCode.setLayoutParams(new TableRow.LayoutParams(1,1));
		//hitemCode.setGravity(Gravity.LEFT | Gravity.BOTTOM);
		hitemCode.setVisibility(View.VISIBLE);
		header.addView(hitemCode);
		
		
		//Item Description Column
		TextView HitemDescription = new TextView(this);
		HitemDescription.setText("Description");		
		HitemDescription.setGravity(Gravity.LEFT | Gravity.BOTTOM);
		HitemDescription.setBackgroundColor(Color.BLACK);
		header.addView(HitemDescription);
		
		//PCKG Column
		TextView hPckg = new TextView(this);
		hPckg.setText("PCKG");		
		hPckg.setGravity(Gravity.LEFT | Gravity.BOTTOM);
		hPckg.setBackgroundColor(Color.BLACK);
		header.addView(hPckg);	
		
		//QTY Column
		TextView hQty = new TextView(this);
		hQty.setText("Qty");		
		hQty.setGravity(Gravity.LEFT | Gravity.BOTTOM);
		hQty.setBackgroundColor(Color.BLACK);
		header.addView(hQty);		
		
		//Price  Column			
		TextView hPrice = new TextView(this);
		hPrice.setText("Price");
		hPrice.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
		hPrice.setBackgroundColor(Color.BLACK);
		header.addView(hPrice);
		
		//Amount Column
		TextView hAmount = new TextView(this);
		hAmount.setText("Amount");
		hPrice.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
		hPrice.setBackgroundColor(Color.BLACK);
		header.addView(hAmount);
		
		table.addView(header);
		setupView(header);
		setRowColor(header, Color.WHITE, Typeface.BOLD);
		
	
		int row=0;
		Init=true;
		
    	
		for (Return item : returns) 	{
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
			final TextView priId = new TextView(this);
			priId.setText(String.valueOf(item.getId()));
			priId.setLayoutParams(new TableRow.LayoutParams(1,1));
			priId.setVisibility(View.GONE);
			priId.setId(TV_PR_ID);
			tr.addView(priId);
			
			//Status
			TextView status = new TextView(this);
			status.setText(String.valueOf(item.getStatus()));
			status.setLayoutParams(new TableRow.LayoutParams(1,1));
			status.setVisibility(View.GONE);
			tr.addView(status);
			
			//Item Code Column
			final TextView itemCode = new TextView(this);
			itemCode.setText(item.getItemcode());
			itemCode.setLayoutParams(new TableRow.LayoutParams(1,1));
			//itemCode.setGravity(Gravity.LEFT | Gravity.BOTTOM);
			itemCode.setVisibility(View.VISIBLE);
			itemCode.setId(TV_ITEM_CODE);
			tr.addView(itemCode);
			
			//Item Description Column
			TextView itemDescription = new TextView(this);
			itemDescription.setText(item.getItemDescription());
			itemDescription.setLayoutParams(new TableRow.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
			itemDescription.setGravity(Gravity.LEFT | Gravity.BOTTOM);
			itemDescription.setSingleLine(false);
			itemDescription.setWidth(200);
			//itemDescription.setWidth(140);
			tr.addView(itemDescription);
			
						
			//PCKG Spinner Column
			final Spinner etPckg = new Spinner(this);
			String pckg =  item.getPckg();
			etPckg.setLayoutParams(new TableRow.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
			etPckg.setGravity(Gravity.CENTER | Gravity.BOTTOM);
			
			if(SUBMITTED) {
				etPckg.setEnabled(false);
				etPckg.setClickable(false);
			}

			@SuppressWarnings({ "unchecked", "rawtypes" })
            ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
                    R.layout.csi_spinner, Master.pckg_option);
			etPckg.setAdapter(spinnerArrayAdapter);
			
			if(pckg.equalsIgnoreCase(Master.PACKS)) {
				etPckg.setSelection(Master.PCKG_PACK);
			}
			else etPckg.setSelection(Master.PCKG_UNIT);			
			etPckg.setId(SP_PCKG);
			tr.addView(etPckg);
			
			
			//Qty			
			final EditText qty = new EditText(this);
			qty.setText(String.valueOf(item.getQty()));
			qty.setGravity(Gravity.CENTER | Gravity.BOTTOM);
			if(SUBMITTED) {
				qty.setBackgroundColor(0xFFEAEAEE);
				qty.setClickable(false);
				qty.setFocusable(false);
			}
			else qty.setBackgroundColor(0xFFA9FF53);
			qty.setId(ET_QTY);
			qty.addTextChangedListener(new qtyTextWatcher(tr));
			qty.setInputType(InputType.TYPE_CLASS_NUMBER);
			tr.addView(qty);
			
			qty.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					String qtyString = qty.getText().toString().trim();
					int qtyR = qtyString.equals("") ? 0: Integer.valueOf(qtyString);
					
				    if(hasFocus){
				    	qty.setBackgroundColor(0xFFFFF3CE);
				        if(qtyR==0) qty.setText("");
				    }else {
				    	qty.setBackgroundColor(0xFFA9FF53);
				    	if(qtyR==0) qty.setText("0");
				    	
				    }
				}
				
			});
			
			
						
			//Price per unit column
			final TextView price = new TextView(this);
			price.setText(String.format("%.2f",item.getPrice()));
			price.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
			price.setId(TV_PRICE);
			tr.addView(price);
			
			//Amount Column
			final TextView amount = new TextView(this);
			Double amt = item.getQty() *  item.getPrice();
			amount.setText(NumUtil.getPhpCurrency(amt,false));
			amount.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
			amount.setId(TV_AMOUNT);
			tr.addView(amount);
						
			
			
			etPckg.setOnItemSelectedListener(new OnItemSelectedListener() {
	            @Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
	            	if(!Init) {
		            	Double pPrice =0.0;
		            	String strQty = qty.getText().toString();
		            	int qty = strQty.equals("") ? 0: Integer.valueOf(strQty);
		            	
		            	
		            	ReturnDbManager db = new ReturnDbManager(context);
		        		db.open();
		        		
		        		
		            	pPrice = getPrice(itemCode.getText().toString(),position);
		            	price.setText(""+pPrice);
		            	
		            	amount.setText(""+NumUtil.getPhpCurrency(qty*pPrice,false));
		            	
		            	
		            	//Save CS_ITEM
		            	Return pri = new Return();
		        		
		            	pri.setId(Integer.valueOf(priId.getText().toString()));
		            	pri.setPrice(pPrice);
		            	pri.setPckg(etPckg.getSelectedItem().toString());
		        		db.UpdatePckg(pri);
		        		
		        		Double totalAmount;
			    		totalAmount = db.getTotal(prCode);
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
				setRowColor(tr, Color.BLACK, Typeface.NORMAL);
				enableSubmitButton();
			}
			else {
				setRowColor(tr, Color.DKGRAY, Typeface.NORMAL);
				amount.setTextColor(Color.DKGRAY);
				disableSubmitButton();
				
				disableSignatureButton();
			}
			amount.setTypeface(null, Typeface.BOLD);
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
		b.putString("txNo", ""+prNo);		
		b.putString("customerCode", cCode);
		b.putString("customerName", cName);
		b.putString("customerAddress", cAddress);
		intent.putExtras(b); 
		startActivity(intent);
	}
	
	private void submitReturn() {
		
		final ProgressDialog progress = ProgressDialog.show(this, "Submitting",
	  			  "Please wait...", true);

	  		new Thread(new Runnable() {
			  @Override
			  public void run(){
				sendReturn();  
			    runOnUiThread(new Runnable() {
			      @Override
			      public void run(){
			        progress.dismiss();
			      }
			    });
			  }
	  		}).start();
		
	}
	
	
	
	private void sendReturn() {
		
		ReturnDbManager dbi = new ReturnDbManager(this);			
		dbi.open();		
		List<Return> pri = dbi.getList(prCode);
		
		devId = DbUtil.getSetting(context, Master.DEVID);
		String message="";
		for (Return item : pri) 	{
			if(item.getStatus()==0 && item.getQty()>0) {
				message = Master.PRODUCT_RETURN + " " +
						devId 					+ ";" +
						item.getCcode() 		+ ";" +						
						item.getItemcode()		+ ";" + 
						item.getDate() 			+ ";" +
						item.getPrno() 			+ ";" +
						item.getPrcode() 		+ ";" +	
						item.getPckg() 			+ ";" + 
						item.getQty()			+ ";" + 
						item.getPrice()			+ ";" +
						item.getId();
				
				DbUtil.saveMsg(context,DbUtil.getGateway(context), message);
			}		
		}
		
		
		dbi.updateStatus(prCode, 1); //1=Transmitted
		dbi.close();
		
		
		finish();
		
	}

	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
	       	    
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
			DialogManager.showAlertDialog(context,"Locked Transaction!", 
					"This transaction had already been transmitted to the central server and cannot be modified!", true);			
			return false;			
		}
		
        switch (mItem.getItemId()) {            
            case R.id.delete_return_item:
            	
            	title = "Delete Return Item";
           	 	message = "Are you sure you want delete " + itemDescription.getText().toString() + "?";
           	 	confirmDialog(title,  message, DELETE_RETURN_ITEM,Id,prCode);
        		return true;
                
            case R.id.delete_all: 
            	 title = "Delete Entire Transaction";
            	 message = "Are you sure you want delete the entire product return transaction?";
            	confirmDialog(title,  message, DELETE_ALL,Id,prCode);
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

	/*
	private void confirmedDeleteReturnItem(int Id) {
		ReturnDbManager db = new ReturnDbManager(this);
		db.open();		
		db.deleteReturnItem(Id);
		db.close();
		
		refreshList();
	}
	*/
	
	private void confirmedDeleteReturn(String prCode) {
		ReturnDbManager db = new ReturnDbManager(this);
		db.open();		
		db.deleteReturn(prCode);
		db.close();
		
		
		finish();
	}
	
	
	public void confirmDialog(String title, String message, Integer option, Integer id, String cscode) {
		   
		   final Integer Option = option;
		   //final Integer Id = id;
		   final String prCode=cscode;
		   
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
				public void onClick(DialogInterface dialog, int which) {
	            	
	            	switch(Option) {            	   		
	        	   		
	        	   		case DELETE_ALL:   
	        	   			confirmedDeleteReturn(prCode);
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

	
	/***********************************************
	****** Add Single Item ************************
	***********************************************/
	
	private void selectSingleItem() {
		Intent intent = new Intent(context, ItemListActivity.class);
		Bundle b = new Bundle();
		
		intent.putExtras(b); 
		startActivityForResult(intent, 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  if (requestCode == 1) {
		     if(resultCode == RESULT_OK){      
		    	 String itemCode=data.getStringExtra("itemCode");
		         Double ppPack=data.getDoubleExtra("ppPack",0.0);
		         addPrItem (itemCode, ppPack);
		         refreshList();
		         
		     }
		     if (resultCode == RESULT_CANCELED) {    
		         //Write your code if there's no result
		     }
		  }
		}//onActivityResult
	
	/***********************************************/
	
	private void addPrItem(String itemCode, Double Price) {
		ReturnDbManager db = new ReturnDbManager(context);
		db.open();
		Return pri = new Return();
		pri.setPrcode(prCode);
		pri.setDate(String.valueOf(dateTime/1000));
		pri.setCcode(cCode);
		pri.setPrno(prNo);
		pri.setItemcode(itemCode);
		pri.setPckg(Master.PACKS);
		pri.setPrice(Price);
		pri.setQty(0);
		pri.setStatus(0);
		db.AddReturn(pri);
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
		btSubmit.setBackgroundResource(R.drawable.rounded_blue_button);
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
		btSignature.setBackgroundResource(R.drawable.rounded_blue_button);
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
	
	private class qtyTextWatcher implements TextWatcher {
		 
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
				   int quantity = qtyString.equals("") ? 0: Integer.valueOf(qtyString);
				   
				   //Log.d(TAG+" qtyTextWatcher","quantity="+quantity);
				   
				   TextView tvPrice = (TextView) view.findViewById(TV_PRICE);
				   String strPrice = tvPrice.getText().toString();
				   Double price = strPrice.equals("") ? 0.0: Double.valueOf(strPrice);
				   
				   TextView tvAmount = (TextView) view.findViewById(TV_AMOUNT);
				   tvAmount.setText(NumUtil.getPhpCurrency(quantity*price,false));
				   
				   if(!Init){
					   //Update ReturnItem OrderQTy
					   TextView prId = (TextView) view.findViewById(TV_PR_ID);
						 //Save CS_ITEM
			           	Return pri = new Return();
			       		
			           	pri.setId(Integer.valueOf(prId.getText().toString()));
			           	pri.setQty(quantity);
			       		
			       		ReturnDbManager db = new ReturnDbManager(context);
			    		db.open();
			       		db.UpdateQty(pri);
			           
			    		Double totalAmount;
			    		totalAmount = db.getTotal(prCode);
			    		tvTotalAmount.setText(""+NumUtil.getPhpCurrency(totalAmount,false));
			    		db.close();
				   }
			  }catch(Exception e){};
			   return;
		  }
	}	
}