package com.xpluscloud.moses_apc;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xpluscloud.moses_apc.dbase.CollectibleDbManager;
import com.xpluscloud.moses_apc.dbase.CollectionDbManager;
import com.xpluscloud.moses_apc.getset.Collection;
import com.xpluscloud.moses_apc.util.DateUtil;
import com.xpluscloud.moses_apc.util.DbUtil;
import com.xpluscloud.moses_apc.util.DialogManager;
import com.xpluscloud.moses_apc.util.Master;
import com.xpluscloud.moses_apc.util.NumUtil;
import com.xpluscloud.moses_apc.util.StringUtil;


public class CollectionActivity extends Activity {
	
	//private final String TAG = "CollectionActivity";
	private String customerCode;
	private String customerName;
	private String customerAddress;
	private String invoiceNo;
	
	TextView tvCustomer;
	TextView tvDate;
	EditText etInvoiceNo;
	
	
	EditText etOrno;
	EditText etCash;
	EditText etCheck;
	EditText etCheckno;
	EditText etCm;
	EditText etCmno;
	EditText etDeposit;
	EditText etBankname;
	EditText etBankBranch;
	EditText etDepTrNo;
	
	TextView tvTotal;
	
	public Button btSubmit;
	
	Context context;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState); 
	    setContentView(R.layout.activity_collection);
	    setTitle(getResources().getString(R.string.app_name) + " " 	+	
				 getResources().getString(R.string.version )
				 + " - " + "Collection") ;
	    
	    context = CollectionActivity.this;
	   	
	   	Bundle b = getIntent().getExtras();
		customerCode		=b.getString("customerCode");
		customerName		=b.getString("customerName");
		customerAddress		=b.getString("customerAddress");
		
		invoiceNo			=b.getString("invoiceNo");
		
		initView();
		lostFocusListeners();
		
		tvCustomer.setText(customerName +"\n" + customerAddress);
		
		Long sysTime = System.currentTimeMillis();
		
		tvDate.setText(DateUtil.phShortDate(sysTime));
		
		if (invoiceNo!="") {
			etInvoiceNo.setText(invoiceNo);
			
		}
	}
	
	
	
	private void initView() {		
		tvCustomer 	= (TextView) findViewById(R.id.tvCustomer);
		tvDate		= (TextView) findViewById(R.id.tvDate);
		
		etInvoiceNo	= (EditText) findViewById(R.id.etInvoiceNo);
    	etOrno 		= (EditText) findViewById(R.id.etOrNo);
    	etCash 		= (EditText) findViewById(R.id.etCash);
    	etCheck 	= (EditText) findViewById(R.id.etCheck);
    	etCheckno 	= (EditText) findViewById(R.id.etCheckno);
    	etCm 		= (EditText) findViewById(R.id.etCm);
    	etCmno 		= (EditText) findViewById(R.id.etCmno);
    	etDeposit 	= (EditText) findViewById(R.id.etDeposit);
    	etBankname 	= (EditText) findViewById(R.id.etBankName);
    	etBankBranch= (EditText) findViewById(R.id.etBankBranch);
    	etDepTrNo	= (EditText) findViewById(R.id.etDepTrNo);
    	
    	
    	tvTotal		= (TextView) findViewById(R.id.tvTotal);
    	
		btSubmit = (Button) findViewById(R.id.btSubmit);
		btSubmit.setOnClickListener(new OnClickListener() {
	        @Override
			public void onClick(View v) {
	            Log.v("log_tag", "Panel Saved");
	            if(submit())  finish();
	            else return;
	        } 
	    }); 
		
	}
	
	private void lostFocusListeners() {
		etCash.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){	              
                    calculateTotal();
                }
            }
        });
		
		etCheck.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){	              
                    calculateTotal();
                }
            }
        });
		
		etCm.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){	              
                    calculateTotal();
                }
            }
        });
		
		etDeposit.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){	              
                    calculateTotal();
                }
            }
        });
		
		
		
	}
	
	private void calculateTotal() {
		
		double cash = NumUtil.strToDouble(etCash.getText().toString());
		double check = NumUtil.strToDouble(etCheck.getText().toString());
		double cm = NumUtil.strToDouble(etCm.getText().toString());
		double deposit = NumUtil.strToDouble(etDeposit.getText().toString());
		
		double total=cash+check+cm+deposit;
		
		tvTotal.setText(""+total);
		
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
    protected void onStop(){
       super.onStop();
       finish();
    }
	
	private void history(){
		//Intent i = new Intent(this, SalescallListActivity.class);		
		//startActivity(i);		
	}
	
	
	public boolean submit() {		
    	Collection col = new Collection();
    	
    	double cash = NumUtil.strToDouble(etCash.getText().toString());
    	double check = NumUtil.strToDouble(etCheck.getText().toString());
		double cm = NumUtil.strToDouble(etCm.getText().toString());
		double deposit = NumUtil.strToDouble(etDeposit.getText().toString());
		
		double total = cash+check+cm+deposit;
		
		String sysDate 		= DateUtil.strDate(System.currentTimeMillis());
		String orno			= etOrno.getText().toString();
		String checkno 		= etCheckno.getText().toString();
		String cmno 		= etCmno.getText().toString();
		String bankname 	= StringUtil.strCleanUp(etBankname.getText().toString());
		String bankbranch 	= StringUtil.strCleanUp(etBankBranch.getText().toString());
		String deptrno 		= etDepTrNo.getText().toString();
		
		
		if(total<=0.0 ) {
			
			DialogManager.showAlertDialog(context,
	                "Blank Transaction Not Allowed!", 
	                "You must enter collection amount or Reason for No Production or Remarks to submit.", false); 
			
			return false;
			
			
		}
		else {
		
		col.setCcode(customerCode);
		col.setInvoiceno(invoiceNo);
		col.setDate(sysDate);
		col.setOrno(orno);
		col.setCashamount(cash);
		col.setCheckamount(check);
		col.setCmamount(cm);
		col.setAmount_dep(deposit);
		col.setCheckno(checkno);
		col.setCmno(cmno);
		col.setBankname(bankname);
		col.setBankbranch(bankbranch);
		col.setDeptrno(deptrno);
		
		CollectionDbManager db = new CollectionDbManager(context);
		db.open();
		Long id = db.AddCollection(col);
		db.close();
		
		CollectibleDbManager db1 = new CollectibleDbManager(context);
		db1.open();
		db1.updateStatus(invoiceNo, 1);
		db1.close();
		
		String devId = Master.getDevId(context);
		
		String sysTime = String.valueOf(System.currentTimeMillis()/1000);
		
		devId = DbUtil.getSetting(context, Master.DEVID);
		String message =  Master.CMD_COLLECTION + " "
    			+ devId  		+ ";"
    			+ customerCode  + ";"	    			
    			+ invoiceNo 	+ ";" 
    			+ sysDate 		+ ";" 
    			+ orno 			+ ";"
    			+ cash 			+ ";"
    			+ check 		+ ";"
    			+ cm 			+ ";" 
    			+ deposit 		+ ";" 
    			+ checkno 		+ ";" 
    			+ cmno			+ ";" 	
    			+ bankname 		+ ";"
    			+ bankbranch 	+ ";"
    			+ deptrno 		+ ";"
    			+ sysTime 		+ ";"
    			+ id;	
    	DbUtil.saveMsg(context,DbUtil.getGateway(context), message);
    	return true;
		}   	
    }
	
}
