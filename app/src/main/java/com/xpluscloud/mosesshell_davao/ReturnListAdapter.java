package com.xpluscloud.mosesshell_davao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xpluscloud.mosesshell_davao.getset.Pr;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReturnListAdapter extends BaseAdapter implements Filterable {
	private LayoutInflater inflater;
	private List<Pr> originalList;
	private List<Pr> filteredList;
	
	String strStatus;
	
	public ReturnListAdapter(Context context, List<Pr> oList) {
		inflater = LayoutInflater.from(context);
		originalList = oList;
		filteredList = oList;			
	}
	

	
	public void setValue(List<Pr> list) {
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
		
		int x=0;
		
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.list_message, null);
			
			holder = new ViewHolder();
			holder.list_row = (RelativeLayout) convertView.findViewById(R.id.list_row);
			holder.SNumber = (TextView) convertView.findViewById(R.id.recipient);
		    holder.datetime = (TextView) convertView.findViewById(R.id.datetime);
		    holder.message = (TextView) convertView.findViewById(R.id.message);
		    holder.status = (TextView) convertView.findViewById(R.id.status);
		    
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		x++;
		holder.SNumber.setText(x + ".) ");
		
		String strTime = filteredList.get(position).getDate();
		
		holder.datetime.setText(strTime);
		
		String txtMsg = "PRNo#: " + filteredList.get(position).getPrno();
		
		holder.message.setText(txtMsg);
				
		strStatus = "";
		switch(filteredList.get(position).getStatus()) {
			case 0:
				strStatus = "Open";
				holder.list_row.setBackgroundColor(0xFF555555);
				break;
			case 1:
				strStatus = "Submitted";
				holder.list_row.setBackgroundColor(0xFF333333);
				break;			
				
		}
		holder.status.setText(strStatus);
		return convertView;
	}
	
	private static class ViewHolder {
		public RelativeLayout list_row;
		public TextView SNumber;
		public TextView datetime;
		public TextView message;
		public TextView status;
	}
			
	
	@Override
	public Filter getFilter() {
        return new Filter() {
           
            @SuppressWarnings("unchecked")
			@Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
            	filteredList = (List<Pr>) results.values;
            	ReturnListAdapter.this.notifyDataSetChanged();
            }

            
			@Override
            protected FilterResults performFiltering(CharSequence constraint) {
				
				int Stat=0;
				if (constraint.toString().toUpperCase().equals("OPEN")) {
					Stat = 0;
				}
				else if ((constraint.toString().toUpperCase().equals("CLOSED"))
						|| (constraint.toString().toUpperCase().equals("SUBMITTED"))){
					Stat = 1;
				}
				
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
	            	List<Pr> nCsList = new ArrayList<Pr>();
	            	for (Pr c : filteredList) { 
	            		String sono = String.valueOf(c.getPrno());
	            		if (c.getCcode().toUpperCase(Locale.ENGLISH).contains(constraint.toString().toUpperCase())
	            				|| 	c.getDate().toUpperCase(Locale.ENGLISH).contains(constraint.toString().toUpperCase())
	            				|| 	sono.contains(constraint.toString())
	            				|| 	c.getStatus()==Stat) 
	            			nCsList.add(c); 
	            	} 
	            	results.values = nCsList; 
	            	results.count = nCsList.size(); 
            	} 
            	return results; 
        	}        
        };
    }
	
}
