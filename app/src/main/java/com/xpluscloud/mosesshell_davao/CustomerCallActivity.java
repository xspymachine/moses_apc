package com.xpluscloud.mosesshell_davao;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.xpluscloud.mosesshell_davao.dbase.SalescallDbManager;
import com.xpluscloud.mosesshell_davao.getset.SalesCall;
import com.xpluscloud.mosesshell_davao.util.ArrayDef;
import com.xpluscloud.mosesshell_davao.util.DbUtil;
import com.xpluscloud.mosesshell_davao.util.DialogManager;
import com.xpluscloud.mosesshell_davao.util.Master;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CustomerCallActivity extends AppCompatActivity {

	private String customerCode;
	private String customerName;
	private String customerAddress;
	private String devId;

	public CheckBox inpVisited=null;
	public EditText inpRemarks=null;
	public EditText inpSales=null;
	public EditText inpCollection=null;

	public RadioButton rbCash=null;
	public RadioButton rbCheck=null;
	public EditText inpChkNo=null;

	public EditText inpInvNo=null;
	public EditText inpORNo=null;

	public Spinner spNpReason;
	public Spinner spValidation;

	public Button btSubmit;

	public boolean submitted;

	Context context;

	public Integer salesCallSerial;

	String cbSuccess = "Salescall successfully sent!";
	String cbFailed = "Salescall sending failed!";


//	private final String[] REASON_NO_PRODUCTION = {
//			"Tap to Select Reason",
//			"DR/CLIENT IS OUT",
//			"DR/CLIENT OUT-OF-TOWN",
//			"DR/CLIENT OUT-OF-COUNTRY",
//			"CLINIC/OFFICE CLOSED",
//			"OTHER REASON"
//	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customer_call);

		context = CustomerCallActivity.this;

		submitted=false;

		Bundle b = getIntent().getExtras();
		devId				=b.getString("devId");
		customerCode		=b.getString("customerCode");
		customerName		=b.getString("customerName");
		customerAddress		=b.getString("customerAddress");


		setTitle("Moses I - Salescall" );

		this.initView();

		TextView tvCName = (TextView) findViewById(R.id.tvCName);
		TextView tvAddress = (TextView) findViewById(R.id.tvAddress);

		try {
			tvCName.setText(customerName);
			tvAddress.setText(customerAddress);
		}
		catch (Exception e) {
			Log.e("ERROR"," Error in Customer...");
		}

		btSubmit = (Button) findViewById(R.id.bt_submit);

		btSubmit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.v("log_tag", "Panel Saved");

				if(preSubmit()) {
					submitSalesCall();
					finish();
				}
			}
		});


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


//	@Override
//	protected void onStop(){
//		super.onStop();
//		finish();
//
//	}

	private void history(){
		Intent i = new Intent(this, SalescallListActivity.class);
		startActivity(i);
	}

	private void initView() {
		inpVisited = (CheckBox) findViewById(R.id.cb_visited);
		inpRemarks = (EditText) findViewById(R.id.et_remarks);
		inpSales = (EditText) findViewById(R.id.et_sales);
		inpCollection = (EditText) findViewById(R.id.et_collection);
		rbCash = (RadioButton) findViewById(R.id.rb_cash);
		rbCheck = (RadioButton) findViewById(R.id.rb_check);
		inpChkNo = (EditText) findViewById(R.id.et_checkno);
		inpInvNo = (EditText) findViewById(R.id.et_invoiceno);
		inpORNo = (EditText) findViewById(R.id.et_orno);


		spNpReason = (Spinner) findViewById(R.id.sp_npreason);

		try {
			ArrayAdapter<?> scArrayAdapter = new ArrayAdapter<Object>(this,
					R.layout.csi_spinner, ArrayDef.REASON_NO_PRODUCTION2);
			spNpReason.setAdapter(scArrayAdapter);

		}catch (Exception e){}

		spValidation = (Spinner) findViewById(R.id.sp_validation);

		try {
			ArrayAdapter<?> scArrayAdapter = new ArrayAdapter<Object>(this,
					R.layout.csi_spinner, ArrayDef.VALIDATION);
			spValidation.setAdapter(scArrayAdapter);

		}catch (Exception e){}
	}

	private Boolean preSubmit(){
		if(spNpReason.getSelectedItemPosition() > 0 && inpRemarks.getText().toString().isEmpty()){
				Toast.makeText(getApplicationContext(), "Explain reason for not buying."  , Toast.LENGTH_SHORT ).show();
			return false;
		}else if(spValidation.getSelectedItemPosition() == 0 ){
            Toast.makeText(getApplicationContext(), "Please tag this outlet as covered or not covered AND whether not buying."  , Toast.LENGTH_SHORT ).show();
            return false;
        }

		return true;
	}

	public void submitSalesCall() {
		String task="Sending sales call information.";
		Toast.makeText(getApplicationContext(), task +".."  , Toast.LENGTH_SHORT ).show();

		try {
			int position = spNpReason.getSelectedItemPosition();
			int valid = spValidation.getSelectedItemPosition();
			String npreason = (String) (position==0 ? "" : spNpReason.getSelectedItem());
			String Remarks = npreason + "\n" + inpRemarks.getText().toString();
			String Collection = inpCollection.getText().toString();
			String Sales = inpSales.getText().toString();

			String PayMethod = null;
			if (rbCash.isChecked()) {
				PayMethod = "Cash";
			}
			else PayMethod = "Check";

			String CheckNo = inpChkNo.getText().toString();

			String InvNo = inpInvNo.getText().toString();
			String ORNo = inpORNo.getText().toString();
			String Visited = null;

			if (inpVisited.isChecked()) {
				Visited = "Y";
			}
			else Visited = "N";

			Remarks = Remarks.replaceAll(";",",");
			Remarks = Remarks.replaceAll("'","`");

			String sysTime = String.valueOf(System.currentTimeMillis()/1000);


			//Save to Sales call table and get id.
			SalescallDbManager db = new SalescallDbManager(context);
			db.open();

			SalesCall c = new SalesCall();

			String Blob = "\nVisited: " 	+ Visited +"\n" +
					"Sales Amount:" 	+ "" +"\n" +
					"Collected Amount:" + "" +"\n" +
					"PayMEthod:"		+ "" +  "\n" +
					"CheckNo:"		+ "" +  "\n" +
					"InvoiceNo"		+ "" +  "\n" +
					"ORNo" 			+ "" + "\n" +
					"Remarks: " 	+ Remarks;


			c.setDatetime(sysTime);
			c.setCustomerCode(customerCode);
			c.setBlob(Blob);
			c.setStatus(0);

			Long id = db.Add(c);
			db.close();

			String message;


			//Save message to outbox
			message =  Master.CMD_SALESCALL + " "
					+ devId  		+ ";"
					+ customerCode  + ";"
					+ Visited 		+ ";"
					+ valid 		+ ";" //sales // validation 1=covered/2=not covered
					+ "" 			+ ";" //collection
					+ "" 			+ ";" //paymethod
					+ "" 			+ ";" //checkno
					+ "" 			+ ";" //invoiceno
					+ "" 			+ ";" //orno
					+ Remarks		+ ";"
					+ sysTime 		+ ";"
					+ id;
			DbUtil.saveMsg(context,DbUtil.getGateway(context), message);
            Log.e("Success :***: ", message);


			submitted=true;
		}
		catch (Exception e) {
			Log.e("Error :***: ", e.getMessage());
		}

		inpVisited.setText("");
		inpRemarks.setText("");

//		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

//		getVisitedCallsPerDay(context,customerCode);
	}

	public void saveDraft(String msg) {

		String filePath = "sdcard/SalesCalls/draft.txt";
		//Log.e("Filepath: ", filePath);
		File file = new File(filePath);

		try {
			if (file.exists()) file.delete();
		} catch(Exception e) {}


		try  {
			file.createNewFile();
		}
		catch (IOException e)  {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try  {
			//BufferedWriter for performance, true to set append to file flag
			BufferedWriter buf = new BufferedWriter(new FileWriter(file, true));
			buf.write(msg);
			buf.newLine();
			buf.close();
		}
		catch (IOException e)   {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void loadDraft() {

		String filePath = "sdcard/SalesCalls/draft.txt";
		//Log.e("Filepath: ", filePath);
		File file = new File(filePath);
		String data = ""; //new StringBuilder();
		try {
			if (file.exists()) {
				//Read text from file
				try {
					BufferedReader br = new BufferedReader(new FileReader(file));
					data = br.readLine();

				}
				catch (IOException e) {
					//You'll need to add proper error handling here
				}

			}
		} catch(Exception e) {}
		//Toast.makeText( getApplicationContext(), data  ,Toast.LENGTH_LONG ).show();

		if(data.isEmpty()) return;

		String[] items = data.split("~");

		Log.e("Draft",items[0]);

		//Toast.makeText( getApplicationContext(), items[1]  ,Toast.LENGTH_LONG ).show();


		TextView tvCName = (TextView) findViewById(R.id.tvCName);
		TextView tvAddress = (TextView) findViewById(R.id.tvAddress);

		inpVisited = (CheckBox) findViewById(R.id.cb_visited);
		inpRemarks = (EditText) findViewById(R.id.et_remarks);
		inpSales = (EditText) findViewById(R.id.et_sales);
		inpCollection = (EditText) findViewById(R.id.et_collection);
		rbCash = (RadioButton) findViewById(R.id.rb_cash);
		rbCheck = (RadioButton) findViewById(R.id.rb_check);
		inpChkNo = (EditText) findViewById(R.id.et_checkno);
		inpInvNo = (EditText) findViewById(R.id.et_invoiceno);
		inpORNo = (EditText) findViewById(R.id.et_orno);

		tvCName.setText("(" + items[0] + ")" + items[1] );
		tvAddress.setText(items[2]);


		if (items[3].equals('Y')) {
			inpVisited.setChecked(true);
		}
		else {
			inpVisited.setChecked(false);
		}

		inpSales.setText(items[4]);
		inpCollection.setText(items[5]);

		if(items[6].equalsIgnoreCase("Cash")) {
			rbCash.setChecked(true);
			rbCheck.setChecked(false);
			inpChkNo.setText("");
		}
		else {
			if(items[6].equalsIgnoreCase("Check")) {
				rbCash.setChecked(false);
				rbCheck.setChecked(true);
				inpChkNo.setText(items[7]);
			}

		}

		inpInvNo.setText(items[8]);
		inpORNo.setText(items[9]);
		inpRemarks.setText(items[10]);
	}


//	public void getVisitedCallsPerDay(Context context, String ccode){
//		int visited = 0;
//		int schedule = 0;
//
//		Date resultdate = new Date(System.currentTimeMillis());
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM", Locale.US);
//
//		CalendarDbManager db = new CalendarDbManager(context);
//		db.open();
//		visited = db.getVisitedCallsPerMonth(df.format(resultdate), ccode);
//		schedule = db.getSchedulePerCustomer(df.format(resultdate), ccode);
//		db.close();
//
//		if(visited>=schedule){
//			CustomerDbManager db2 = new CustomerDbManager(context);
//			db2.open();
//			db2.setStatus(ccode,123);
//			db2.close();
//		}
//
////		return visited;
//	}

	public void validtype(View v){
//		Intent intent = new Intent(context, StoretypeActivity.class);
//		Bundle b = new Bundle();
//		b.putInt("type", 3);
//		intent.putExtras(b);
//		startActivity(intent);
		LayoutInflater inflater = LayoutInflater.from(context);
		View customV = inflater.inflate(R.layout.activity_validationtype, null);

		DialogPlus dialog = DialogPlus.newDialog(context)
				.setContentHolder(new ViewHolder(customV))
				.setPadding(50, 50, 50, 50)
				.setMargin(20,20,20,20)
				.create();
		dialog.show();
	}

}
