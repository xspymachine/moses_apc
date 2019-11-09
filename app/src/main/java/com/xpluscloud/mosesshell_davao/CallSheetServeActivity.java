package com.xpluscloud.mosesshell_davao;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xpluscloud.mosesshell_davao.dbase.CallSheetDbManager;
import com.xpluscloud.mosesshell_davao.util.DialogManager;


public class CallSheetServeActivity extends Activity {
	
	int soid 	  = 0;
	int qty		  = 0;
	String ccode  = "";
	String csCode = "";
	String devId  = "";
	
	Button btSave;
	Button btCancel;
	EditText etQty;
	
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
		setContentView(R.layout.activity_callsheetserve);
		context = CallSheetServeActivity.this;
		
		Bundle b = getIntent().getExtras();
		soid 	 = b.getInt("soid");
		ccode 	 = b.getString("ccode");
		csCode 	 = b.getString("csCode");
		devId 	 = b.getString("devId");
		qty 	 = Integer.parseInt(b.getString("qty"));
				
		setupView();
	}
	
	private void setupView(){
		
		etQty 	 = (EditText) findViewById(R.id.editText1);
		btSave   = (Button) findViewById(R.id.button1);
		btCancel = (Button) findViewById(R.id.button2);
		
		btSave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int inputQty = Integer.parseInt(etQty.getText().toString().isEmpty() ? "0" : etQty.getText().toString());
				if(inputQty == 0){
					DialogManager.showAlertDialog(context,
	    	                "Quantity Mismatched", 	    	                
	    	                "Please verify.", false); 
					return;
				}
				
				CallSheetDbManager db = new CallSheetDbManager(context);
				db.open();
				db.updateDeliverStatus(soid,devId,csCode,ccode,inputQty);
				db.close();     
				
				finish();
			}
		});
		
		btCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
	}

}
