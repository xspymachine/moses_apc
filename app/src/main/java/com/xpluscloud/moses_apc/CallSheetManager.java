package com.xpluscloud.moses_apc;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xpluscloud.moses_apc.dbase.CallSheetDbManager;
import com.xpluscloud.moses_apc.getset.CallSheet;
import com.xpluscloud.moses_apc.util.DateUtil;
import com.xpluscloud.moses_apc.util.DbUtil;
import com.xpluscloud.moses_apc.util.Master;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CallSheetManager extends ListActivity {
	final String TAG = "CallSheetManager";
	
	Context context;
	
	// Search EditText
    EditText inputSearch;
    CallSheetListAdapter adapter;
    CallSheet selectedRow;
    
    String devId = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_list);
		context = CallSheetManager.this;
		
		Bundle b = getIntent().getExtras();
	    devId 	= b.getString("devId");
	    devId = DbUtil.getSetting(context, Master.DEVID);
		
		initListView() ;
		
		
		inputSearch = (EditText) findViewById(R.id.inputSearch);
	        
        inputSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1,
                                      int arg2, int arg3) {
				CallSheetManager.this.adapter.getFilter().filter(arg0); 
				
			}         
           
        });
        
        ListView listView = getListView();
        registerForContextMenu(listView);

	}
	public void initListView() {		
		CallSheetDbManager db = new CallSheetDbManager(this);
		db.open();
		
		List<CallSheet> CallSheetList = db.getAll(null);
		adapter = new CallSheetListAdapter(this, CallSheetList); 
		setListAdapter(adapter);
		
		db.close();	
		
	}

//	public void refreshListView(Context context) {
//		OutboxDbManager db = new OutboxDbManager(context);
//		db.open();			
//		List<Outbox> list = db.getDispMessages();	
//		adapter.setValue(list);	
//		db.close();
//		
//	}
	
	
	
//	 @Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
	    	    
	    selectedRow = (CallSheet) getListAdapter().getItem(info.position);	   
	    if(selectedRow.getStatus() == 99){
	    	Toast.makeText(getApplicationContext(), "This order has been served",
	    	Toast.LENGTH_SHORT).show();
	    	return;
	    }
	    
	    menu.setHeaderIcon(R.drawable.modify);
	   	menu.setHeaderTitle("Sales Order Manager");
	    
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.callsheet_menu, menu);
	    	    
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		initListView();
	}
	
	@Override
    public boolean onContextItemSelected(MenuItem item) {
		
        switch (item.getItemId()) {            
            case R.id.action_mark:
            	Integer soid = selectedRow.getId();
            	String ccode = selectedRow.getCcode();
            	String csCode = selectedRow.getCscode();
            	String[] arrName = selectedRow.getcusname().split("--->");
            	String qty = arrName[1];
//            	CallSheetDbManager db = new CallSheetDbManager(context);
//				db.open();
//				db.updateDeliverStatus(soid,devId,csCode,ccode);
//				db.close();            	
            	
				Intent i = new Intent(this, CallSheetServeActivity.class);
				Bundle b = new Bundle();
				b.putInt("soid", soid);
				b.putString("ccode", ccode);
				b.putString("csCode", csCode);
				b.putString("devId", devId);
				b.putString("qty", qty);
				i.putExtras(b);
				startActivity(i);
				initListView();
        		return true;
        }
        return false;
    }
	
	private static class ViewHolder {	
		public TextView date;
		public TextView sono;
		public TextView cashsale;
		public TextView name;
	}	

	public class CallSheetListAdapter extends BaseAdapter implements Filterable {
		private LayoutInflater inflater;
		private List<CallSheet> originalList;
		private List<CallSheet> filteredList;
		
		String strStatus;
		
		public CallSheetListAdapter(Context context, List<CallSheet> list) {
			inflater = LayoutInflater.from(context);
			originalList = list;
			filteredList = list;			
		}
		

		
		public void setValue(List<CallSheet> list) {
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
			
			if(convertView == null) {
				convertView = inflater.inflate(R.layout.list_callsheet, null);
				
				holder = new ViewHolder();
				holder.date = (TextView) convertView.findViewById(R.id.textView1);
			    holder.name = (TextView) convertView.findViewById(R.id.textView2);
			    holder.sono = (TextView) convertView.findViewById(R.id.textView3);
			    holder.cashsale = (TextView) convertView.findViewById(R.id.textView4);
							    
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.date.setText(DateUtil.shortDateToLongDate(filteredList.get(position).getDate()));
			holder.sono.setText("SONO: "+filteredList.get(position).getSono());
			
			String[] arrName = filteredList.get(position).getcusname().split("--->");
			holder.name.setText(""+arrName[0]);
			
			String soType = "";
			
			if(filteredList.get(position).getCash_sales() == 1) soType = "SO Type: Cash";
//			else if(filteredList.get(position).getCash_sales() == 2) holder.cashsale.setText("Actual Volume Type");
			else if(filteredList.get(position).getCash_sales() == 2) soType = "SO Type: Booking away";
			else soType = "SO Type: Booking";
			
			holder.cashsale.setText(soType+"\n\n"+arrName[1] + " bags ordered\n"+ arrName[2] +" bags served/delivered");
			
			if(filteredList.get(position).getStatus()==99){
				holder.cashsale.setText(holder.cashsale.getText()+"\n\nThis order has been served");
				convertView.setBackgroundColor(Color.GRAY);
			}else convertView.setBackgroundColor(Color.WHITE);
			
			return convertView;
		}		
						
		
		@Override
		public Filter getFilter() {
	        return new Filter() {
	           
	            @SuppressWarnings("unchecked")
				@Override
	            protected void publishResults(CharSequence constraint, FilterResults results) {
	            	filteredList = (List<CallSheet>) results.values;
	            	CallSheetListAdapter.this.notifyDataSetChanged();
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
		            	List<CallSheet> callSheetList = new ArrayList<CallSheet>();
		            	for (CallSheet c : filteredList) { 
		            		if (c.getDate().toUpperCase(Locale.ENGLISH).contains(constraint.toString().toUpperCase())
		            				|| c.getcusname().toUpperCase(Locale.ENGLISH).contains(constraint.toString().toUpperCase())
		            				|| c.getSono().toString().toUpperCase(Locale.ENGLISH).contains(constraint.toString().toUpperCase()))
		            			callSheetList.add(c); 
		            	} 
		            	results.values = callSheetList; 
		            	results.count = callSheetList.size(); 
	            	} 
	            	return results; 
	        	}        
	        };
	    }
		
	}
}
