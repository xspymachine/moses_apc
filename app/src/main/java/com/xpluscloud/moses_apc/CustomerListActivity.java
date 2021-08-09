package com.xpluscloud.moses_apc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.xpluscloud.moses_apc.dbase.CoveragePlanDbManager;
import com.xpluscloud.moses_apc.dbase.CustomerDbManager;
import com.xpluscloud.moses_apc.dbase.PeriodDbManager;
import com.xpluscloud.moses_apc.getset.Customer;
import com.xpluscloud.moses_apc.getset.CustomerData;
import com.xpluscloud.moses_apc.util.DateUtil;
import com.xpluscloud.moses_apc.util.DbUtil;
import com.xpluscloud.moses_apc.util.DialogManager;
import com.xpluscloud.moses_apc.util.Master;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.xpluscloud.moses_apc.MyLocationService.currentLocation;
//import android.widget.Toast;

public class CustomerListActivity extends AppCompatActivity {

	public final int DELETE_CUSTOMER = 0;
	public final int DELETE_ALL_CUSTOMERS = 1;
	public final String ACCESS_CODE = "12345";

	public final int ALL_CUSTOMERS = 0;
	public final int ON_SCHEDULE = 1;
	public final int NON_SCHEDULE = 2;

	public int Day;
	public int Week;
	public int Source; //List Source

	public String wdSched;

	// Search EditText
	EditText inputSearch;
	CustomerListAdapter adapter;

	static Context context;

	String devId;

	Customer selectedRow;

	ListView listView;

	private void updateList() {
		CoveragePlanDbManager db = new CoveragePlanDbManager(this);
		db.open();

		List<Customer> customerList = db.getItinerary(Week, Day);
		adapter = new CustomerListAdapter(this, customerList);
		listView.setAdapter(adapter);
		db.close();
	}


	private void refreshList(Integer Option) {
		List<Customer> customerList;
		CoveragePlanDbManager cpdb;

		switch (Option) {

			case ALL_CUSTOMERS:
//				setTitle(wdSched+" - All Customers");
				setTitle("All Customers");
				CustomerDbManager db = new CustomerDbManager(this);
				db.open();

				customerList = db.getAllCustomers();
				adapter = new CustomerListAdapter(this, customerList);
				listView.setAdapter(adapter);

				db.close();
				Source=ALL_CUSTOMERS;
				break;
			case ON_SCHEDULE:
				setTitle(wdSched+" - On Schedule");
				cpdb = new CoveragePlanDbManager(this);
				cpdb.open();

				customerList = cpdb.getItinerary(Week,Day);
				adapter.setValue(customerList);
				listView.setAdapter(adapter);

				cpdb.close();
				Source=ON_SCHEDULE;
				break;
		}

	}

	private int get_nWeek(String date) {
		PeriodDbManager db = new PeriodDbManager(this);
		db.open();
		Integer nWeek = db.getNweek(date);
		db.close();
		return nWeek;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customer_list);

		context = CustomerListActivity.this;
		listView = (ListView) findViewById(R.id.customerlistview);
		listView.setOnItemClickListener(new onListItemClick());
		Bundle b = getIntent().getExtras();
		devId = b.getString("devId");


		Calendar cal = Calendar.getInstance();
		Day = cal.get(Calendar.DAY_OF_WEEK) - 1;

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US); // For dbase query
		String qDate = df.format(cal.getTime());

		Week = get_nWeek(qDate);

		wdSched = "W" + Week + " D" + Day;

		Source = ON_SCHEDULE;

		inputSearch = (EditText) findViewById(R.id.inputSearch);

		updateList();

		inputSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
				// When user changed the Text
				CustomerListActivity.this.adapter.getFilter().filter(inputSearch.getText().toString());
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
			}
		});


//		ListView listView = getListView();
		registerForContextMenu(listView);

	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshList(Source);
	}


	private class onListItemClick implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            selectedRow = (Customer) listView.getAdapter().getItem(position);

            if(Master.FOR_APPROVAL_SETTING == 1) {
				if (selectedRow.getStatus() != 1 && selectedRow.getStatus() != 21) {
					DialogManager.showAlertDialog(CustomerListActivity.this,
							"Needs Approval",
							"Customer needs approval, contact head office for more inquiry.", false);
					return;
				}
			}


            Intent returnIntent = new Intent();
            returnIntent.putExtra("cCode", selectedRow.getCustomerCode());
            returnIntent.putExtra("cName", selectedRow.getName());
            returnIntent.putExtra("cAddress", selectedRow.getAddress());
            returnIntent.putExtra("cNum", selectedRow.getContactNumber());
            returnIntent.putExtra("cDisc", selectedRow.getDiscount());

            setResult(RESULT_OK, returnIntent);
            finish();
        }
    }

    Menu myMenu;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.customer_menu, menu);
        myMenu = menu;
        myMenu.findItem(R.id.allCustomers).setVisible(true);
        myMenu.findItem(R.id.onSched).setVisible(false);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        List<Customer> customerList;
        CoveragePlanDbManager cpdb;
		String title;
		String message;
		switch (item.getItemId()) {
            case R.id.onSched:
                myMenu.findItem(R.id.allCustomers).setVisible(true);
                myMenu.findItem(R.id.onSched).setVisible(false);
                setTitle(wdSched+" - On Schedule");
                cpdb = new CoveragePlanDbManager(this);
                cpdb.open();

                customerList = cpdb.getItinerary(Week,Day);
                adapter.setValue(customerList);
                listView.setAdapter(adapter);

                cpdb.close();
                Source=ON_SCHEDULE;
                break;
            case R.id.allCustomers:
                myMenu.findItem(R.id.allCustomers).setVisible(false);
                myMenu.findItem(R.id.onSched).setVisible(true);
                setTitle("All Customers");
                CustomerDbManager db = new CustomerDbManager(this);
                db.open();

                customerList = db.getAllCustomers();
                adapter = new CustomerListAdapter(this, customerList);
                listView.setAdapter(adapter);

                db.close();
				Source=ALL_CUSTOMERS;
                break;
			case R.id.add_customer:
				Intent i = new Intent(this, CustomerAddEditActivity.class);
				Bundle b = new Bundle();
				b.putString("devId", devId);
				b.putInt("custId", 0);
				i.putExtras(b);
				startActivity(i);
				break;

			case R.id.delete_all:
				title = "Delete All Customers";
				message = "Are you sure you want delete all customers?";
				confirmDialog(title, message, DELETE_ALL_CUSTOMERS, 0);
				break;
			default:
				refreshList(ALL_CUSTOMERS);
				break;
		}
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

		selectedRow = (Customer) listView.getAdapter().getItem(info.position);

		Drawable dr = context.getResources().getDrawable(R.drawable.assets_edit);
		Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
		// Scale it to 50 x 50
		Drawable d = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, 50, 50, true));

//		menu.setHeaderIcon(d);
		menu.setHeaderTitle("Manage Customer\n" + selectedRow.getName());

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_ed, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		Intent intent;
		Bundle b;
		b = new Bundle();
		b.putString("devId", devId);

		String title;
		String message;

		switch (item.getItemId()) {
			case R.id.action_edit:
				intent = new Intent(context, CustomerAddEditActivity.class);

				b.putString("devId", devId);
				b.putInt("custId", selectedRow.getId());

				intent.putExtras(b);
				startActivity(intent);
				return true;
			case R.id.action_delete:
				title = "Delete Customer";
				message = "Are you sure you want delete " + selectedRow.getName() + "?";
				confirmDialog(title, message, DELETE_CUSTOMER, selectedRow.getId());
				return true;
			case R.id.action_call:
				actionCall(getContactNumber(selectedRow.getCustomerCode()));
				return true;
			case R.id.action_sms:
				actionSms(getContactNumber(selectedRow.getCustomerCode()),selectedRow.getName());
				return true;
		}
		return false;
	}

	private void actionCall(String number){
		String contact = number;
		if (contact.length() < 10) {
			contactError();
			return;
		}

		final String mobileno = "0" + contact.substring(contact.length() - 10);

		Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobileno));
		try {
			context.startActivity(callIntent);
		}catch (SecurityException e){}
	}

	private void actionSms(String number,String name){
		String contact = number;
					if(contact.length() < 10){
						contactError();
						return;
					}
					final String mobileno = "+63"+contact.substring(contact.length()-10);

					AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
			        .setTitle("Send SMS")
			        .setMessage("Send a message to "+name);
					alertDialog.setCancelable(false);

					final EditText input = new EditText(context);
					 LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					     LinearLayout.LayoutParams.MATCH_PARENT,
					     LinearLayout.LayoutParams.MATCH_PARENT);
					 lp.setMargins(50, 0, 50, 0);
					 input.setLayoutParams(lp);
					 input.setBackgroundResource(android.R.drawable.edit_text);
					 input.setText("Will be there at  about (select time)");
					 input.setSelection(input.getText().length());
					 input.setFocusable(false);

					 final Button btDate = new Button(context);
					 btDate.setLayoutParams(lp);
					 btDate.setText("select date");
					 input.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Log.e("buttonclick","date");
//							input.setFocusable(true);
							Calendar mcurrentTime = Calendar.getInstance();
				            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
				            int minute = mcurrentTime.get(Calendar.MINUTE);
				            TimePickerDialog mTimePicker;
				            mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
				                @Override
				                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
				                    input.setText("Will be there at about "+ DateUtil.updateTime(selectedHour,selectedMinute));
				                }
				            }, hour, minute, false);//Yes 24 hour time
				            mTimePicker.setTitle("Select Time");
				            mTimePicker.show();
						}
					});

					 alertDialog.setView(input);

					 alertDialog.setPositiveButton("Send", new DialogInterface.OnClickListener()
				    {
				        @Override
				        public void onClick(DialogInterface dialog, int which) {
//				        	dialog.dismiss();
				        	Log.e("text",input.getText().toString());
				        	SmsManager sms = SmsManager.getDefault();
				        	Intent intent = new Intent("customer_sms");
				        	PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0,
				                    intent, 0);

				        	sms.sendTextMessage(mobileno, null,
				                    input.getText().toString(), sentIntent, null);

				        	final ProgressDialog progressDialog = new ProgressDialog(context);
				        	progressDialog.setMessage("Sending message please wait...");
				    		progressDialog.setCancelable(false);
				    		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				    		progressDialog.show();

				        	context.registerReceiver(new BroadcastReceiver(){
							    @Override
							    public void onReceive(Context context, Intent intent) {
							        switch (getResultCode()) {
							            case Activity.RESULT_OK:
//							            	Toast.makeText(context.getApplicationContext(), "Message is sent ^_^",  Toast.LENGTH_SHORT).show();
											DbUtil.makeToast(LayoutInflater.from(context),  "Message is sent ^_^", context,
													(ViewGroup) findViewById(R.id.custom_toast_layout),0);
							            	progressDialog.dismiss();
							                break;
							            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//							                Toast.makeText(context.getApplicationContext(), "Sending failed! Try again...",  Toast.LENGTH_SHORT).show();
                                            DbUtil.makeToast(LayoutInflater.from(context),  "Sending failed! Try again...", context,
                                                    (ViewGroup) findViewById(R.id.custom_toast_layout),0);
							            	Log.e("SMS Error ", "RESULT_ERROR_GENERIC_FAILURE!");
							            	progressDialog.dismiss();
							                break;
							            case SmsManager.RESULT_ERROR_NO_SERVICE:
//							                Toast.makeText(context.getApplicationContext(), "No signal from service provider!",  Toast.LENGTH_SHORT).show();
                                            DbUtil.makeToast(LayoutInflater.from(context),  "No signal from service provider!", context,
                                                    (ViewGroup) findViewById(R.id.custom_toast_layout),0);
							            	Log.e("SMS Error ", "RESULT_ERROR_NO_SERVICE");
							            	progressDialog.dismiss();
							                break;
							            case SmsManager.RESULT_ERROR_NULL_PDU:
//							                Toast.makeText(context.getApplicationContext(), "Message Error!", Toast.LENGTH_SHORT).show();
                                            DbUtil.makeToast(LayoutInflater.from(context),  "Message Error!", context,
                                                    (ViewGroup) findViewById(R.id.custom_toast_layout),0);
							            	Log.e("SMS Error ", "RESULT_ERROR_NULL_PDU");
							            	progressDialog.dismiss();
							                break;
							            case SmsManager.RESULT_ERROR_RADIO_OFF:
							            	Log.e("SMS Error ", "RESULT_ERROR_RADIO_OFF");
							            	progressDialog.dismiss();
//							                Toast.makeText(context.getApplicationContext(), "Device is Offline!",    Toast.LENGTH_SHORT).show();
                                            DbUtil.makeToast(LayoutInflater.from(context),  "Device is Offline!", context,
                                                    (ViewGroup) findViewById(R.id.custom_toast_layout),0);
							                break;
							        }

							    }
							}, new IntentFilter("customer_sms"));

				        }

				    })
				    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					})
				    .show();
	}

	private void confirmedDelete(int Id) {
		CustomerDbManager db = new CustomerDbManager(this);
		db.open();
		String customerCode = db.getCustomerCode(Id);
		db.delete(Id);
		db.close();

		String sysTime = String.valueOf(System.currentTimeMillis() / 1000);

		String message = Master.CMD_DCUS + " " +
				devId + ";" +
				customerCode + ";" +
				sysTime + ";";

		DbUtil.saveMsg(context, DbUtil.getGateway(context), message);
		refreshList(Source);

	}

	private void confirmedDeleteAll() {
		CustomerDbManager db = new CustomerDbManager(this);
		db.open();
		db.deleteAll();
		db.close();
//		Toast.makeText(getApplicationContext(), "All customers has been deleted!" , Toast.LENGTH_SHORT).show();
		DbUtil.makeToast(LayoutInflater.from(context), "All customers has been deleted!", context,
				(ViewGroup) findViewById(R.id.custom_toast_layout), 0);
		refreshList(Source);
	}


	private static class CustomerListAdapter extends BaseAdapter implements Filterable {
		private LayoutInflater inflater;
		private List<Customer> originalList;
		private List<Customer> filteredList;

		public CustomerListAdapter(Context context, List<Customer> list) {
			inflater = LayoutInflater.from(context);
			originalList = list;
			filteredList = list;
		}

		public void setValue(List<Customer> list) {
			this.filteredList = list;
			this.notifyDataSetChanged();
		}


		@Override
		public int getCount() {
			return filteredList.size();
		}

		@Override
		public Object getItem(int position) {
			return filteredList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return filteredList.get(position).getId();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				convertView = inflater.inflate(R.layout.list_customer, null);

				holder = new ViewHolder();
				holder.customerName = convertView.findViewById(R.id.customer_name);
				holder.customerAddress = convertView.findViewById(R.id.customer_address);
				holder.contactNumber = convertView.findViewById(R.id.contactNumber);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.customerName.setText(filteredList.get(position).getName());

			String completeAddress = filteredList.get(position).getAddress() +
					" " + filteredList.get(position).getBrgy() +
					" " + filteredList.get(position).getCity() +
					" " + filteredList.get(position).getState();

			holder.customerAddress.setText(completeAddress);
			String ccode = filteredList.get(position).getCustomerCode();
			String str_contact = getContactNumber(ccode)+"<br>";
			holder.contactNumber.setText(str_contact);
			if(filteredList.get(position).getLatitude() < 1) {
				str_contact +="<font color=\"#f99d32\">UNMARK LOCATION</font><br>";
			}else{

				Location myLoc = currentLocation;
				if(myLoc != null){
					Location loc1 = new Location("");
					loc1.setLatitude(filteredList.get(position).getLatitude());
					loc1.setLongitude(filteredList.get(position).getLongitude());
					Location loc2 = new Location("");
					loc2.setLatitude(myLoc.getLatitude());
					loc2.setLongitude(myLoc.getLongitude());

					float distanceInMeters = loc2.distanceTo(loc1);
					float km = distanceInMeters/1000;
					DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
					if(km < 1) str_contact +="<font color=\"#3b5998\">"+formatter.format(distanceInMeters)+" meters away</font><br>";
					else str_contact +="<font color=\"#3b5998\">"+formatter.format(km)+" kilometers away</font><br>";
				}

			}
			if(Master.FOR_APPROVAL_SETTING == 1) {
				if(filteredList.get(position).getStatus() != 1 && filteredList.get(position).getStatus() != 21) {
					str_contact += "<font color=\"red\">FOR APPROVAL</font>";
				}
			}
			holder.contactNumber.setText(Html.fromHtml(str_contact));
			return convertView;
		}
		
		private static class ViewHolder {
			public TextView customerName;
			public TextView customerAddress;
			public TextView contactNumber;
		}
				
		
		@Override
	    public Filter getFilter() {
	        return new Filter() {
	            @SuppressWarnings("unchecked")
	            @Override
	            protected void publishResults(CharSequence constraint, FilterResults results) {
	            	filteredList = (List<Customer>) results.values;
	            	CustomerListAdapter.this.notifyDataSetChanged();
	            }

	            
				@Override
	            protected FilterResults performFiltering(CharSequence constraint) {
	            	FilterResults results = new FilterResults();
            		// We implement here the filter logic 
	            	if (constraint == null || constraint.length() == 0) { 
	            		// No filter implemented we return all the list 
		            	results.values = originalList; 
		            	results.count = originalList.size();
		            	return results;

	            	} 
	            	else { 
		            	// We perform filtering operation 
		            	List<Customer> ncustomerList = new ArrayList<Customer>();
		            	for (Customer c : originalList) { 
		            		if (c.getName().toUpperCase().contains(constraint.toString().toUpperCase()) 
		            				|| c.getCustomerCode().toUpperCase().contains(constraint.toString().toUpperCase()) ) 
		            			ncustomerList.add(c); 
		            	} 
		            	results.values = ncustomerList; 
		            	results.count = ncustomerList.size(); 
	            	} 
	            	return results; 
            	}
            
	        };
	    }
	}
	
	public void confirmDialog(String title, String message, Integer option, Integer id) {
		   
		   final Integer Option = option;
		   final String Title = title;
		   final Integer Id = id;
		   
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
	        	   		case DELETE_CUSTOMER: {  
//	        	   			Log.d("ConfirmDialog-DeleteCustomer ID:",""+Id);
	        	   			confirmedDelete(Id);
	        	   		}	        	   		
	        	   		break;
	        	   		case DELETE_ALL_CUSTOMERS: {   
	         	   			confirmedDeleteAll();
	        	   		}	        	   		
	        	   		break;
		        	  }
	 
	            
//	            	Toast.makeText(getApplicationContext(), Title + " confirmed!" , Toast.LENGTH_SHORT).show();
                    DbUtil.makeToast(LayoutInflater.from(context),  Title + " confirmed!", context,
                            (ViewGroup) findViewById(R.id.custom_toast_layout),1);
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
	
	private static void contactError(){
		DialogManager.showAlertDialog(context,
                "Contact number unavailable", 
                " Please provide a contact number for this client.", false); 
	}

	private static String getContactNumber(String ccode){
        CustomerDbManager db = new CustomerDbManager(context);
        db.open();
        CustomerData cdata=db.getCustomerDataInfo2(ccode,10);
        db.close();
        String cnumber = "";
        if(cdata!=null) cnumber = "Contact #: "+cdata.getOphone();

        return cnumber;
    }
	
}
