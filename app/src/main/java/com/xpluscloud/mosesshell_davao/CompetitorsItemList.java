package com.xpluscloud.mosesshell_davao;

import android.app.ListActivity;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.TextView;

import com.xpluscloud.mosesshell_davao.dbase.ComItemDbManager;
import com.xpluscloud.mosesshell_davao.dbase.CompetitorPriceDbManager;
import com.xpluscloud.mosesshell_davao.getset.CmpItem;
import com.xpluscloud.mosesshell_davao.getset.CompetitorPrice;
import com.xpluscloud.mosesshell_davao.util.DialogManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CompetitorsItemList extends ListActivity {
	
	final int MODE_ADD = 0;
	
	//public String csCode;
	public String customerCode;
	public String devId;
	
    // Search EditText
    EditText inputSearch;
    ItemListAdapter adapter;
    
    Context context;
    
    
    CmpItem selectedRow;
       
	private void updateList() {
		ComItemDbManager db = new ComItemDbManager(this);
		db.open();		
		List<CmpItem> ItemList = db.getList();
		adapter = new ItemListAdapter(this, ItemList); 
		setListAdapter(adapter);
		
		db.close();
	}
	
	private void refreshList() {
		ComItemDbManager db = new ComItemDbManager(this);
		db.open();		
		List<CmpItem> list = db.getList();
		adapter.setValue(list);
		db.close();	
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_list);
		
		context = CompetitorsItemList.this;
		
		Bundle b = getIntent().getExtras();
	    //csCode			=b.getString("csCode");
	    customerCode	=b.getString("customerCode");
	    devId = b.getString("devId");
		
        inputSearch = (EditText) findViewById(R.id.inputSearch);
    
        updateList();
        
        inputSearch.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
            	
                CompetitorsItemList.this.adapter.getFilter().filter(inputSearch.getText().toString());   
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
		
		selectedRow = (CmpItem) getListAdapter().getItem(position);
		
		CompetitorPrice cp = new CompetitorPrice();
		
		cp.setCcode(customerCode);
		cp.setIcode(selectedRow.getItemCode());
		cp.setCatcode(selectedRow.getCategoryCode());
		cp.setDescription(selectedRow.getDescription());
//		cp.setPrice("0.00");
		cp.setStatus(0);
		
		CompetitorPriceDbManager db = new CompetitorPriceDbManager(context);
		db.open();
		cp.setComcode(db.get_Code(devId));
		if(db.searchItem(customerCode, selectedRow.getItemCode())) {
			db.AddItem(cp);
			db.close();
			finish();
		}
		else {
			DialogManager.showAlertDialog(CompetitorsItemList.this,
	                "Duplicate", 
	                "The item is already on list.", true); 
		}
		
	}	
			
	private static class ItemListAdapter extends BaseAdapter implements Filterable {
		private LayoutInflater inflater;
		private List<CmpItem> originalList;
		private List<CmpItem> filteredList;
		
		public ItemListAdapter(Context context, List<CmpItem> list) {
			inflater = LayoutInflater.from(context);
			originalList = list;
			filteredList = list;			
		}
		
		public void setValue(List<CmpItem> list) {
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
				convertView = inflater.inflate(R.layout.list_item, null);
				
				holder = new ViewHolder();
				holder.description = (TextView) convertView.findViewById(R.id.description);
			    holder.itemcode = (TextView) convertView.findViewById(R.id.itemcode);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.description.setText(filteredList.get(position).getDescription());
			holder.itemcode.setText("("+filteredList.get(position).getItemCode() + ")");
			
			return convertView;
		}
		
		private static class ViewHolder {
			public TextView description;
			public TextView itemcode;
		}
				
		
		@Override
	    public Filter getFilter() {
	        return new Filter() {
	            @SuppressWarnings("unchecked")
	            @Override
	            protected void publishResults(CharSequence constraint, FilterResults results) {
	            	filteredList = (List<CmpItem>) results.values;
	            	ItemListAdapter.this.notifyDataSetChanged();
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
		            	List<CmpItem> nItemList = new ArrayList<CmpItem>();
		            	for (CmpItem c : originalList) { 
		            		if (c.getDescription().toUpperCase(Locale.US).contains(constraint.toString().toUpperCase())
		            				|| c.getItemCode().toUpperCase(Locale.US).contains(constraint.toString().toUpperCase()) )
		            			nItemList.add(c); 
		            	} 
		            	results.values = nItemList; 
		            	results.count = nItemList.size(); 
	            	} 
	            	return results; 
            	}
            
	        };
	    }
	}
	
}



