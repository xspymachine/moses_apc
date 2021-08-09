package com.xpluscloud.moses_apc;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xpluscloud.moses_apc.dbase.CollectibleDbManager;
import com.xpluscloud.moses_apc.getset.Collectible;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class CollectibleListActivity extends ListActivity {
	
	final int MODE_ADD = 0;
	
	
	public String cCode;
	public String cName;
	public String cAddress;
	public String cBrgy;
	public String cCity;
	
    // Search EditText
	
	TextView tvCustomer;
	
    EditText inputSearch;
    ActivityListAdapter adapter;
    
    Context context;
    
    
    Collectible selectedRow;
       
	private void updateList() {
		CollectibleDbManager db = new CollectibleDbManager(this);
		db.open();		
		List<Collectible> list = db.getAllCollectibles(cCode);
		adapter = new ActivityListAdapter(this, list); 
		setListAdapter(adapter);
		
		db.close();
	}
	
	private void refreshList() {
		CollectibleDbManager db = new CollectibleDbManager(this);
		db.open();		
		List<Collectible> list = db.getAllCollectibles(cCode);
		adapter.setValue(list);
		db.close();	
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collectible_list);
		
		context = CollectibleListActivity.this;
		
		Bundle b = getIntent().getExtras();
	    
	    cCode		=b.getString("customerCode");
	    cName		=b.getString("customerName");
	    cAddress	=b.getString("customerAddress");
	    String customerInfo = cName + "\n" + cAddress;
	    
	    
	    tvCustomer = (TextView) findViewById(R.id.tvCustomer);
	    tvCustomer.setText(customerInfo);
		
        inputSearch = (EditText) findViewById(R.id.inputSearch);
    
        updateList();
        
        inputSearch.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
            	
                CollectibleListActivity.this.adapter.getFilter().filter(inputSearch.getText().toString());   
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
	
	@Override
	protected void onResume() {
	    super.onResume();
	    refreshList();
	}
	
	@Override
    protected void onStop(){
       super.onStop();
       finish();
    }
	
		
	
	
	@Override
	public void onListItemClick(ListView parent, View v, int position, long id) {
		super.onListItemClick(parent, v, position, id);
		
		selectedRow = (Collectible) getListAdapter().getItem(position);
		
		if(selectedRow.getStatus()!=0) return;
		
		Intent intent = new Intent(context,CollectionActivity.class);
		Bundle b = new Bundle();
		b.putString("customerCode", cCode);
		b.putString("customerName", cName);
		b.putString("customerAddress", cAddress);		
		b.putString("invoiceNo", selectedRow.getInvoiceno());
		b.putDouble("invoiceAmount", selectedRow.getAmount());
		
		intent.putExtras(b); 
		startActivity(intent);
		
	}	
			
	@SuppressLint("DefaultLocale")
	private static class ActivityListAdapter extends BaseAdapter implements Filterable {
		private LayoutInflater inflater;
		private List<Collectible> originalList;
		private List<Collectible> filteredList;
		
		public ActivityListAdapter(Context context, List<Collectible> list) {
			inflater = LayoutInflater.from(context);
			originalList = list;
			filteredList = list;			
		}
		
		public void setValue(List<Collectible> list) {
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
				convertView = inflater.inflate(R.layout.list_collectible, null);
				
				holder = new ViewHolder();
				holder.listRow = (LinearLayout) convertView.findViewById(R.id.listRow);
				holder.date = (TextView) convertView.findViewById(R.id.tvDate);
			    holder.invoiceno = (TextView) convertView.findViewById(R.id.tvInvoiceno);
			    holder.amount = (TextView) convertView.findViewById(R.id.tvAmount);
			    holder.paid = (TextView) convertView.findViewById(R.id.tvPaid);
			    
			    
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.date.setText(filteredList.get(position).getDate());
			holder.invoiceno.setText(filteredList.get(position).getInvoiceno());
			holder.amount.setText(""+filteredList.get(position).getAmount());
			
			int status = filteredList.get(position).getStatus();
			
			if (status == 0) {
				holder.listRow.setBackgroundResource(R.drawable.list_selector); 
				holder.paid.setText("N");
				holder.date.setTextColor(0xff333333);
				holder.invoiceno.setTextColor(0xff333333);
				holder.amount.setTextColor(0xff333333);
				holder.paid.setTextColor(0xff333333);
			}
			else{
				holder.listRow.setBackgroundColor(0x99F0F0E1);
				holder.paid.setText("Y");				
				holder.date.setTextColor(0xff939393);
				holder.invoiceno.setTextColor(0xff939393);
				holder.amount.setTextColor(0xff939393);
				holder.paid.setTextColor(0xff939393);
			}
			
			return convertView;
		}
		
		private static class ViewHolder {
			public LinearLayout listRow;
			public TextView date;
			public TextView invoiceno;
			public TextView amount;
			public TextView paid;
		}
				
		
		@Override
	    public Filter getFilter() {
	        return new Filter() {
	            @SuppressWarnings("unchecked")
	            @Override
	            protected void publishResults(CharSequence constraint, FilterResults results) {
	            	filteredList = (List<Collectible>) results.values;
	            	ActivityListAdapter.this.notifyDataSetChanged();
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
		            	List<Collectible> cList = new ArrayList<Collectible>();
		            	for (Collectible c : originalList) { 
		            		if (c.getDate().toUpperCase(Locale.US).contains(constraint.toString().toUpperCase())
		            				|| c.getInvoiceno().toUpperCase(Locale.US).contains(constraint.toString().toUpperCase()) )
		            			cList.add(c); 
		            	} 
		            	results.values = cList; 
		            	results.count = cList.size(); 
	            	} 
	            	return results; 
            	}
            
	        };
	    }
	}
	
}



