package com.xpluscloud.mosesshell_davao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.xpluscloud.mosesshell_davao.getset.Outbox;
import com.xpluscloud.mosesshell_davao.util.DbUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OutboxListAdapter extends BaseAdapter implements Filterable {
	private LayoutInflater inflater;
	private List<Outbox> originalList;
	private List<Outbox> filteredList;
	
	String strStatus;
	
	public OutboxListAdapter(Context context, List<Outbox> list) {
		inflater = LayoutInflater.from(context);
		originalList = list;
		filteredList = list;			
	}
	

	
	public void setValue(List<Outbox> list) {
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
			convertView = inflater.inflate(R.layout.list_message, null);
			
			holder = new ViewHolder();
			holder.recipient = (TextView) convertView.findViewById(R.id.recipient);
		    holder.datetime = (TextView) convertView.findViewById(R.id.datetime);
		    holder.message = (TextView) convertView.findViewById(R.id.message);
		    holder.status = (TextView) convertView.findViewById(R.id.status);
		    
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.recipient.setText(filteredList.get(position).getId() + ".) ");
		
		String strTime = filteredList.get(position).getDateTime();
		
		strTime = DbUtil.phTime(Long.parseLong(strTime) * 1000);
				
		holder.datetime.setText(strTime);
		
		String txtMsg = filteredList.get(position).getMessage();
		
		if(txtMsg.length()>150) txtMsg=txtMsg.substring(0, 150) + "...";
		
		holder.message.setText(txtMsg);

		strStatus = getStrStatus(filteredList.get(position).getStatus());
		holder.status.setText(strStatus);
		return convertView;
	}
	
	private static class ViewHolder {			
		public TextView recipient;
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
            	filteredList = (List<Outbox>) results.values;
            	OutboxListAdapter.this.notifyDataSetChanged();
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
	            	List<Outbox> nOutboxList = new ArrayList<Outbox>();
	            	for (Outbox c : filteredList) { 
	            		if (c.getRecipient().toUpperCase(Locale.ENGLISH).contains(constraint.toString().toUpperCase())
	            				|| 	c.getDateTime().toUpperCase(Locale.ENGLISH).contains(constraint.toString().toUpperCase())
	            				|| 	c.getMessage().toUpperCase(Locale.ENGLISH).contains(constraint.toString().toUpperCase())
	            				|| 	getStrStatus(c.getStatus()).toUpperCase(Locale.ENGLISH).contains(constraint.toString().toUpperCase()))
	            			nOutboxList.add(c); 
	            	} 
	            	results.values = nOutboxList; 
	            	results.count = nOutboxList.size(); 
            	} 
            	return results; 
        	}        
        };
    }

    private String getStrStatus(int status){
		String strStatus = "";
		switch(status) {
			case 0:
				strStatus = "Queue";
				break;
			case 1:
				strStatus = "Sent";
				break;
			case 2:
				strStatus = "Failed";
				break;
			case 3:
				strStatus = "Sending";
				break;
			case 4:
				strStatus = "Voided";
				break;

		}

		return strStatus;
	}
	
}
