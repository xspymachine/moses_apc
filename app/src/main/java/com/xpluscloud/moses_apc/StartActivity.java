package com.xpluscloud.moses_apc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.xpluscloud.moses_apc.dbase.ChecklistDbManager;
import com.xpluscloud.moses_apc.dbase.MerchandisingDbManager;
import com.xpluscloud.moses_apc.util.DateUtil;
import com.xpluscloud.moses_apc.util.DbUtil;
import com.xpluscloud.moses_apc.util.DialogManager;
import com.xpluscloud.moses_apc.util.Master;

import java.util.ArrayList;

public class StartActivity extends Activity {
	public static final String SETTINGS = "MosesSettings";
	public SharedPreferences settings;

	Context context;
	
	private String sysDate;
	private String setDate;
	
	private TextView tvDate;
	/*
	private CheckBox cbRem1;
	private CheckBox cbRem2;
	private CheckBox cbRem3;
	private CheckBox cbRem4;
	private CheckBox cbRem5;
	private CheckBox cbRem6;
	*/
	
	public Button btSubmit;
	public Button btSubmit2;
	
	private RelativeLayout cbGroup;
	private LinearLayout cbGroup2;
	
	String actName="";
	String devId="";
	String customerCode="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reminders);		
		context = StartActivity.this;
		
		long iDate = System.currentTimeMillis();
		sysDate = DateUtil.strDate(iDate);
		tvDate = (TextView) findViewById(R.id.tvDate);
		tvDate.setText(DateUtil.phLongDateTime(iDate));
		
		btSubmit = (Button) findViewById(R.id.btSubmit);
		btSubmit2 = (Button) findViewById(R.id.btSubmit2);
		cbGroup = (RelativeLayout) findViewById(R.id.cbGroup);
		cbGroup2 = (LinearLayout) findViewById(R.id.cbGroup2);

//		Bundle b = getIntent().getExtras(); 		
//		actName		=b.getString("merchandising");
		
		settings = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
		setDate = settings.getString("setDate", "");

		Log.w("StartActivity",setDate);
//		if(actName.equals("merchandising") && actName!=null){
		try{			
			Bundle b 	 = getIntent().getExtras();
			actName	 	 = b.getString("merchandising");
			devId	 	 = b.getString("devid");
			customerCode = b.getString("ccode");	
			
			if(actName.equals("merchandising") && actName!=null){
				cbGroup.setVisibility(View.GONE);
				btSubmit.setEnabled(false);
				btSubmit.setVisibility(View.GONE);
				
				cbGroup2.setVisibility(View.VISIBLE);
				btSubmit2.setVisibility(View.VISIBLE);
//				DbUtil.changeDrawableColor("#E21717", btSubmit2);
				btSubmit2.setOnClickListener(new OnClickListener() {
			        @Override
					public void onClick(View v) {
			            submitMerchandise(v);
			        } 
			    });			
				
				updateChecklist();
				
			}else{
				if (setDate.equalsIgnoreCase(sysDate)) runMain();			
				Log.w("StartActivity","onCreate");
				btSubmit.setOnClickListener(new OnClickListener() {
			        @Override
					public void onClick(View v) {
			            submitReminders(v);
			        } 
			    });
			}
		}catch(Exception e){
			
			if (setDate.equalsIgnoreCase(sysDate)) runMain();			
			Log.w("StartActivity","onCreate");
			btSubmit.setOnClickListener(new OnClickListener() {
		        @Override
				public void onClick(View v) {
		            submitReminders(v);
		        } 
		    });
		}
				
	}
	
	private void submitReminders(View v) {
		
		RelativeLayout cbGroup = (RelativeLayout) findViewById(R.id.cbGroup);
		int cbCheckCount=0;
		for(int i=0;i<cbGroup.getChildCount();i++){						
	       View child=cbGroup.getChildAt(i);
	       
	       if (child instanceof CheckBox){
	    	   if (((CheckBox) child).isChecked()) {
	    		   cbCheckCount++;
	    	   }
	    	   else {
	    		   DialogManager.showAlertDialog(context,
	    	                "All Items Must Be Checked!", 
	    	                "Please do the steps and check when done.", false); 
	    		   cbCheckCount=0;
	    		   break;
	    	   }
	       }
	       
	       if (cbCheckCount>=6) {
		       SharedPreferences.Editor prefEditor = settings.edit();
			   prefEditor.putString("setDate", sysDate);
			   prefEditor.commit();	
			   runMain();
	       }
		}
		
	}
	
	private void submitMerchandise(View v) {
		
		int cbCheckCount=0;
		String Remarks="";
		String sysTime = String.valueOf(System.currentTimeMillis()/1000);
		String checklist="";
		
		Boolean isCheck = false;
		int status = 0;
		devId = DbUtil.getSetting(context, Master.DEVID);
		
		for(int i=0;i<cbGroup2.getChildCount();i++){						
			View child=cbGroup2.getChildAt(i);
		       
		       if(child instanceof LinearLayout){
		    	   
		    	   for(int j = 0; j<((LinearLayout) child).getChildCount(); j++){
		    		   View child2=((LinearLayout) child).getChildAt(j);
		    		   
		    	       if (child2 instanceof CheckBox){
		    	    	   if (((CheckBox) child2).isChecked()) isCheck = true;
				    	   else isCheck = false;
			       		}
		    	       else if(child2 instanceof TextView){
		    	    	   	if(isCheck){ status = 1;		    	    	    
			    	    	    ChecklistDbManager db = new ChecklistDbManager(context);
			    	    	    db.open();
			    				int mcid = db.getId(((TextView) child2).getText().toString());
			    				db.close();
			    				
			    				String message =  Master.CMD_MSTAT + " "
			    		       			+ devId  				  + ";"
			    		       			+ sysTime		  		  + ";"
			    		       			+ customerCode			  + ";"
			    		       			+ mcid					  + ";"
			    		       			+ status; 		
		   	    		   
			    				DbUtil.saveMsg(context,DbUtil.getGateway(context), message);
		    	    	   	}
		    				else status = 0;
		    	       }
		    	   }	    		   
		       }
		       if(child instanceof EditText){
		    	   Remarks = ((EditText) child).getText().toString();
		       }	      
	       
		}
		
		String message =  Master.CMD_MERCH + " "
       			+ devId  				  + ";"
    	       	+ customerCode			  + ";"
    	       	+ Remarks				  + ";"
       			+ sysTime; 				  
//       		+ id;	
       			
   	    DbUtil.saveMsg(context,DbUtil.getGateway(context), message);
   	    
   	    MerchandisingDbManager db = new MerchandisingDbManager(context);
   	    db.open();
   	    db.addData(customerCode, Remarks);
   	    db.close();
		
		Intent returnIntent = new Intent();
		setResult(RESULT_OK,returnIntent); 		
		finish();
		
	}
	
	private void runMain() {
		
		Intent intent= new Intent(context, StartActivity2.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		context.startActivity(intent); 
		
		finish();
	}
	
	ArrayList<String> checklists = new ArrayList<String>();
	LinearLayout[] llx ;
	TextView[] tx  ;
	CheckBox[] cbx;
	
	private void updateChecklist(){
//		Log.e("","check");
		
		ChecklistDbManager db = new ChecklistDbManager(context);
		db.open();
		checklists = db.getChecklists();
		db.close();
		
		llx = new LinearLayout[checklists.size()];
	    tx = new TextView[checklists.size()];
	    cbx = new CheckBox[checklists.size()];
	    
	    LinearLayout.LayoutParams param1=
				  new LinearLayout.LayoutParams
				  (LinearLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
		param1.setMargins(0, 30, 0, 0);
		
		LinearLayout.LayoutParams param2=
				  new LinearLayout.LayoutParams
				  (LinearLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
		param2.setMargins(10, 30, 0, 0);
		
		LinearLayout.LayoutParams param3=
				  new LinearLayout.LayoutParams
				  (LinearLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
		param3.setMargins(0, 10, 0, 10);
		
		for(int i=0; i<checklists.size(); i++){
//			llx[i] = new LinearLayout(this);
//	        tx[i] = new TextView(this);
//	        cbx[i] =new CheckBox(this);
//
//	        tx[i].setLayoutParams(param2);
//	        tx[i].setText(checklists.get(i).toString());
//	        tx[i].setTextSize(20);
//
//	        cbx[i].setLayoutParams(param1);
//	        cbx[i].setBackgroundResource(R.drawable.custom_checkbox);
//	        cbx[i].setTextSize(20);
//
//	        llx[i].addView(cbx[i]);
//	        llx[i].addView(tx[i]);
//			cbGroup2.addView(llx[i]);

			LayoutInflater inflater = LayoutInflater.from(context);
			View v = inflater.inflate(R.layout.checklist_item, null);
			TextView tvMerch = v.findViewById(R.id.tv_checklist);
			tvMerch.setText(checklists.get(i));

			cbGroup2.addView(v);
		}
		
		TextView tvRemark = new TextView(this);
		EditText etRemark = new EditText(this);
		
		tvRemark.setLayoutParams(param1);
		tvRemark.setTextSize(14);
		tvRemark.setText("Remarks");
	
		etRemark.setLayoutParams(param3);
		etRemark.setLines(3);
		etRemark.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
		etRemark.setFilters(new InputFilter[] {new InputFilter.LengthFilter(80)});
		etRemark.setPadding(8, 15, 8, 15);
		etRemark.setGravity(Gravity.CENTER);
		etRemark.setBackgroundResource(R.drawable.rounded_border);
		
		cbGroup2.addView(tvRemark);
		cbGroup2.addView(etRemark);		
		
	}
	

}
