package com.xpluscloud.moses_apc;

import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xpluscloud.moses_apc.dbase.SalescallDbManager;
import com.xpluscloud.moses_apc.getset.MyList;
import com.xpluscloud.moses_apc.util.CallDatePicker;
import com.xpluscloud.moses_apc.util.DateUtil;
import com.xpluscloud.moses_apc.util.DialogManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomerCallSummary extends ListActivity {
	
	EditText date1;
//	EditText date2;
	Button btCustomer;
	Button btGo;
	
	CallDatePicker frag;
	Calendar sDate;
	ListAdapter adapter;
    MyList selectedRow;
	
	String devId="";
	String customerName="";
	String customerCode="";
	String customerAddress="";
	String contactNumber="";
	
	Context context;
	
	int count = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calls_summary);
		
		context = CustomerCallSummary.this;
		
		Bundle b = getIntent().getExtras();
		devId = b.getString("devId");
		sDate = Calendar.getInstance();
		
		setViews();		
		initListView();
		
		ListView listView = getListView();
        registerForContextMenu(listView);
	}


	private void setViews() {
		// TODO Auto-generated method stub
		
		date1 = (EditText) findViewById(R.id.date1);
//		date2 = (EditText) findViewById(R.id.date2);
		btCustomer = (Button) findViewById(R.id.btCustomer);
		btGo = (Button) findViewById(R.id.btGo);
		
		date1.setText(DateUtil.phLongDate(sDate.getTimeInMillis()));
//		date2.setText(DateUtil.phLongDate(sDate.getTimeInMillis()));
		 
		date1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(date1);
			}
		});
		
//		date2.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				showDialog(date2);
//			}
//		});
		
		btCustomer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				searchCustomer(btCustomer);
			}
		});
		
		btGo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				searchCustomer(btCustomer);
//				Log.e("date", reverseDate(date1.getText().toString()));
				if(!date1.getText().toString().equals("")) initListView();
				else {
					Toast.makeText(getApplicationContext(), "Please provide a valid dates",
							   Toast.LENGTH_LONG).show();
				}		
			}
		});
		
	}
	
	public interface DateDialogFragmentListener{
		//this interface is a listener between the Date Dialog fragment and the activity to update the buttons date
		public void updateChangedDate(int year, int month, int day);
	}
	
	public void showDialog(final EditText etView) {
		FragmentTransaction ft = getFragmentManager().beginTransaction(); //get the fragment
		frag = CallDatePicker.newInstance(this, new DateDialogFragmentListener(){
			public void updateChangedDate(int year, int month, int day){
				sDate.set(year, month, day);
				etView.setText(DateUtil.phLongDate(sDate.getTimeInMillis()));	    			
			}
		}, sDate);
		
		frag.show(ft, "DateDialogFragment");	
		
		Log.e("date","date");
	}

	/***** Search Customer Section ********/
	
	private void searchCustomer(View Button){
		Intent i = new Intent(this, CustomerListActivity.class);
		Bundle b = new Bundle();
		b.putString("devId", devId);		  		  		
		i.putExtras(b); 
		startActivityForResult(i, 1);
	}

	private void initListView() {
		// TODO Auto-generated method stub
		String range1 = reverseDate(date1.getText().toString());
//		String range2 = date2.getText().toString();
		
		SalescallDbManager db = new SalescallDbManager(this);
		db.open();		
		List<MyList> list;
//		if(customerName.equals("") && date1.getText().toString().equals("")) list = db.getAll();
//		else 
		list = db.getSummaryCalls(range1, customerName);
		adapter = new ListAdapter(this, list); 
		setListAdapter(adapter);
		
//		Log.e("count",""+list.size());
		
//		count = list.size();
		
		db.close();	
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		switch(requestCode) {
			case 1: 
				if(resultCode == RESULT_OK){      
			    	 customerName=data.getStringExtra("cName");
			         customerCode=data.getStringExtra("cCode");
			         customerAddress=data.getStringExtra("cAddress");
			         contactNumber=data.getStringExtra("cNum");
			         if(contactNumber!="")  btCustomer.setText(customerName +" " + contactNumber + " \n" + customerAddress);		         
			         else btCustomer.setText(customerName +" \n" + customerAddress);
			         
			         initListView();
			     }
			     if (resultCode == RESULT_CANCELED) {    
			         //Write your code if there's no result
			     }
			     break;
			     
		}
		
		  
	}//onActivityResult
	
	@Override
	protected void onListItemClick(ListView parent, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(parent, v, position, id);
		
		selectedRow = (MyList) getListAdapter().getItem(position);
		
		String ccode = selectedRow.getCustomerCode();
		String date = reverseDate(date1.getText().toString());
		String[] arr = getInfo(ccode, date, 3).split("Remarks");
//		Log.e(""+arr.length,arr[1]);
		String remark = "";
		try {
			remark = arr[1];
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		DialogManager.showAlertDialog(context,
                selectedRow.getCustomerName()+"\n"+
                selectedRow.getAddress(), 
                "Visited: "+getInfo(ccode, date, 1)+"                " +
			    "NP calls: "+getInfo(ccode, date, 2)+"\n" +
			    "Remarks"+remark+"\n" +
			    "Closing Notes: "+getInfo(ccode, date, 4)+"", false); 
		return;
		
		
	}
	
	private static class ViewHolder {			
		public TextView customer;
		public TextView address;
		public TextView datetime;
		public TextView transaction;
	}
	
	public class ListAdapter extends BaseAdapter implements Filterable {
		private LayoutInflater inflater;
		private List<MyList> originalList;
		private List<MyList> filteredList;
		
		
		public ListAdapter(Context context, List<MyList> list) {
			inflater = LayoutInflater.from(context);
			originalList =  list;
			filteredList = list;			
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
			
			if(convertView == null) {
				convertView = inflater.inflate(R.layout.list_history, null);
				
				holder = new ViewHolder();
				holder.customer = (TextView) convertView.findViewById(R.id.ccode);
				holder.address = (TextView) convertView.findViewById(R.id.address);
			    holder.datetime = (TextView) convertView.findViewById(R.id.datetime);
			    holder.transaction = (TextView) convertView.findViewById(R.id.transaction);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.customer.setText(filteredList.get(position).getCustomerName());
			
			holder.address.setText(filteredList.get(position).getAddress());
								
			holder.datetime.setText("");
			
			String ccode = filteredList.get(position).getCustomerCode();
			String date = reverseDate(date1.getText().toString());
			String[] arr = getInfo(ccode, date, 3).split("Remarks");
//			Log.e(""+arr.length,arr[1]);
			String remark = "";
			try {
				remark = arr[1];
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			holder.transaction.setText("Visited: "+getInfo(ccode, date, 1)+"                " +
									   "NP calls: "+getInfo(ccode, date, 2)+"\n" +
									   "Remarks"+remark+"\n" +
									   "Closing Notes: "+getInfo(ccode, date, 4)+"");
			
			return convertView;
		}
		
		
		@Override
		public Filter getFilter() {
	        return new Filter() {
	           
	            @SuppressWarnings("unchecked")
				@Override
	            protected void publishResults(CharSequence constraint, FilterResults results) {
	            	filteredList = (List<MyList>) results.values;
	            	ListAdapter.this.notifyDataSetChanged();
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
		            	List<MyList> nList = new ArrayList<MyList>();
		            	for (MyList c : originalList) { 
		            		if (c.getCustomerName().toUpperCase(Locale.ENGLISH).contains(constraint.toString().toUpperCase())
		            				|| 	c.getDateTime().toUpperCase(Locale.ENGLISH).contains(constraint.toString().toUpperCase())
		            				|| 	c.getTransaction().toUpperCase(Locale.ENGLISH).contains(constraint.toString().toUpperCase()))
		            			nList.add(c); 
		            	} 
		            	results.values = nList; 
		            	results.count = nList.size(); 
	            	} 
	            	return results; 
	        	}
	        
	        };
	    }		
		
	}	
	
	private String reverseDate(String date){
		SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
		Date newDate = null;
		try {
			newDate = format.parse(date);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		    
//		long contime = newDate.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM", Locale.US);
		return df.format(newDate); 
	}
	
	private String getInfo(String ccode, String date, int option){
		String info = "";
		SalescallDbManager db = new SalescallDbManager(context);
		db.open();
		info = db.getInfo(ccode, date, option);
		db.close();
		return info;
	}	
	
}
