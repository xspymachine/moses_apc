package com.xpluscloud.moses_apc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.xpluscloud.moses_apc.getset.MyList;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


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
		
		holder.customer.setText(filteredList.get(position).getId() + ".) " + 
				filteredList.get(position).getCustomerName());
		
		holder.address.setText(filteredList.get(position).getAddress());
		
		String strTime = filteredList.get(position).getDateTime();
				
		holder.datetime.setText(strTime);
		
		String txtMsg = filteredList.get(position).getTransaction();
		if(!strTime.contains("Open")){
			if(txtMsg.length()>30) txtMsg=txtMsg.substring(0, 30) + "...";
		}
		
		holder.transaction.setText(txtMsg);
		
		return convertView;
	}
	
	private static class ViewHolder {			
		public TextView customer;
		public TextView address;
		public TextView datetime;
		public TextView transaction;
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