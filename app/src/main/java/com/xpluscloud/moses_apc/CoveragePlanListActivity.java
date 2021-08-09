package com.xpluscloud.moses_apc;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.xpluscloud.moses_apc.dbase.CoveragePlanDbManager;
import com.xpluscloud.moses_apc.dbase.PeriodDbManager;
import com.xpluscloud.moses_apc.getset.CoveragePlan;
import com.xpluscloud.moses_apc.getset.Customer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class CoveragePlanListActivity extends ListActivity {
	static final int DATE_DIALOG_ID = 0;
	   
    // Search EditText
    EditText inputSearch;
    CoveragePlanListAdapter adapter;
    
    TextView tvBucket;
    
    Button dateButton;
    
    Context context;
    
    String devId;
    
    CoveragePlan selectedRow;
       
	private void updateList(Integer week, Integer day ) {
		CoveragePlanDbManager db = new CoveragePlanDbManager(this);
		db.open();
		
		List<Customer> CoveragePlanList = db.getItinerary(week,day);
		adapter = new CoveragePlanListAdapter(this, CoveragePlanList); 
		setListAdapter(adapter);
		
		db.close();
	}
	
	

	@Override
	protected void onCreate(Bundle i) {
		super.onCreate(i);
		setContentView(R.layout.activity_cplan_list);
		
		context = CoveragePlanListActivity.this; 
			
		Bundle b = getIntent().getExtras();
	    Long iDate = b.getLong("iDate");
						
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(iDate);
		int day = cal.get(Calendar.DAY_OF_WEEK)-1;
		
		
		SimpleDateFormat phd = new SimpleDateFormat("MM/dd/yyyy", Locale.US); //For title display
		String phDate = phd.format(cal.getTime());
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US); // For dbase query
	    String qDate = df.format(cal.getTime());
		
		
		setTitle("ITINERARY");	
		
		
		dateButton = (Button) findViewById(R.id.dateButton);
		
		Integer nWeek=get_nWeek(qDate);
		dateButton.setText( "Itinerary: " + phDate + " (Week: " + nWeek + " Day: " + day +")");
		
		
		dateButton.setOnClickListener(new OnClickListener() {
	        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
			@Override
	        public void onClick(View v) {
	            // TODO Auto-generated method stub
	        	try {
		            DialogFragment newFragment = new DatePickerFragment();
		            newFragment.show(getFragmentManager(), "DatePicker");
	        	} catch(Exception e) {}
	        }
	    });
		
		
		tvBucket = (TextView) findViewById(R.id.tvBucket);
	    
		tvBucket.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
            }
             
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
                 
            }
             
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            	updateTextButton();            	
            }
        });
		
        inputSearch = (EditText) findViewById(R.id.inputSearch);
    
        updateList(nWeek,day);
        
        inputSearch.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                CoveragePlanListActivity.this.adapter.getFilter().filter(cs);   
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
        ListView listView = getListView();
        registerForContextMenu(listView);

	}	
	
	
	private void updateTextButton() {
		String dateString = tvBucket.getText().toString();
		
		String aDate[] = dateString.split("/");
		
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.valueOf(aDate[2]), Integer.valueOf(aDate[0]), Integer.valueOf(aDate[1]));
		int day = cal.get(Calendar.DAY_OF_WEEK)-1;
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
	    String sDate = df.format(cal.getTime());
	    
	    Integer nWeek=get_nWeek(sDate);
		dateButton.setText( "Itinerary: " + dateString + " (Week: " + nWeek + " Day: " + day +")");
		
		CoveragePlanDbManager db = new CoveragePlanDbManager(this);
		db.open();
		
		List<Customer> CoveragePlanList = db.getItinerary(nWeek,day);
		adapter.setValue(CoveragePlanList);
		
		db.close();

	}
	
	
	private int get_nWeek(String date) {
		PeriodDbManager db = new PeriodDbManager(this);
		db.open();		
		Integer nWeek = db.getNweek(date);
		db.close();
		return nWeek;
	}
	
	@Override
	public void onListItemClick(ListView parent, View v, int position, long id) {
		super.onListItemClick(parent, v, position, id);		
	}
	
	
	
	private static class CoveragePlanListAdapter extends BaseAdapter implements Filterable {
		private LayoutInflater inflater;
		private List<Customer> originalList;
		private List<Customer> filteredList;
		
		public CoveragePlanListAdapter(Context context, List<Customer> list) {
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
			
			if(convertView == null) {
				convertView = inflater.inflate(R.layout.list_customer, null);
				
				holder = new ViewHolder();
				holder.text = (TextView) convertView.findViewById(R.id.customer_name);
			    holder.textAddress = (TextView) convertView.findViewById(R.id.customer_address);
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.text.setText(filteredList.get(position).getName() +" (" + filteredList.get(position).getCplanCode() + ")");
			holder.textAddress.setText(filteredList.get(position).getAddress());
			
			return convertView;
		}
		
		private static class ViewHolder {			
			public TextView text;
			public TextView textAddress;
		}
				
		
		@Override
	    public Filter getFilter() {
	        return new Filter() {
	            @SuppressWarnings("unchecked")
	            @Override
	            protected void publishResults(CharSequence constraint, FilterResults results) {
	            	filteredList = (List<Customer>) results.values;
	            	CoveragePlanListAdapter.this.notifyDataSetChanged();
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
		            	List<Customer> nCoveragePlanList = new ArrayList<Customer>();
		            	for (Customer c : originalList) { 
		            		if (c.getName().toUpperCase().contains(constraint.toString().toUpperCase()) ) 
		            			nCoveragePlanList.add(c); 
		            	} 
		            	results.values = nCoveragePlanList; 
		            	results.count = nCoveragePlanList.size(); 
	            	} 
	            	return results; 
            	}
            
	        };
	    }
	}
	
}




