package com.xpluscloud.moses_apc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xpluscloud.moses_apc.dbase.CallSheetItemDbManager;
import com.xpluscloud.moses_apc.dbase.CompetitorDbManager2;
import com.xpluscloud.moses_apc.dbase.InventoryDbManager;
import com.xpluscloud.moses_apc.getset.CallSheet;
import com.xpluscloud.moses_apc.getset.CallSheetItem;
import com.xpluscloud.moses_apc.getset.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CallSheetListAdapter extends BaseAdapter implements Filterable {
	private LayoutInflater inflater;
	private List<CallSheet> originalList;
	private List<CallSheet> filteredList;
	
	String strStatus;
	Context context;
	
	public CallSheetListAdapter(Context _context, List<CallSheet> oList) {
		inflater = LayoutInflater.from(_context);
		originalList = oList;
		filteredList = oList;
		context = _context;
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
		
		holder.SNumber.setText(filteredList.get(position).getId() + ".) ");
		
		String strTime = filteredList.get(position).getDate();
		
		holder.datetime.setText(strTime);
        String items = getItems(filteredList.get(position).getCscode());

		String txtMsg = "Customer: " + filteredList.get(position).getcusname() +
                "\n"+items+
				"\nSO#: " + filteredList.get(position).getSono();
		
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
            	filteredList = (List<CallSheet>) results.values;
            	CallSheetListAdapter.this.notifyDataSetChanged();
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
	            	List<CallSheet> nCsList = new ArrayList<CallSheet>();
	            	for (CallSheet c : filteredList) { 
	            		String sono = String.valueOf(c.getSono());
	            		if (c.getCcode().toUpperCase(Locale.ENGLISH).contains(constraint.toString().toUpperCase())
	            				|| 	c.getDate().toUpperCase(Locale.ENGLISH).contains(constraint.toString().toUpperCase())
	            				|| 	c.getcusname().toUpperCase(Locale.ENGLISH).contains(constraint.toString().toUpperCase())
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

    private String getItems(String cScode){
	    StringBuilder iDesc= new StringBuilder();
        CallSheetItemDbManager db = new CallSheetItemDbManager(context);
        db.open();
        List<CallSheetItem> items = db.getList(cScode);
        for(int i=0;i<items.size();i++){
            iDesc.append("  -").append(items.get(i).getDescription()).append("\n");
        }
		db.close();
		if(items.size() < 1) iDesc = getDbInventory(cScode);
	    return iDesc.toString();
    }
    private StringBuilder getDbInventory(String inCode){
		StringBuilder iDesc= new StringBuilder();
		InventoryDbManager db = new InventoryDbManager(context);
		db.open();
		List<Inventory> items = db.getList(inCode);
		for(int i=0;i<items.size();i++){
			iDesc.append("  -").append(items.get(i).getDescription()).append("\n");
		}
		db.close();
		if(items.size() < 1) iDesc = getDbCompetitor(inCode);
		return iDesc;
	}
	private StringBuilder getDbCompetitor(String cmpCode){
		StringBuilder iDesc= new StringBuilder();
		CompetitorDbManager2 db = new CompetitorDbManager2(context);
		db.open();
		List<Inventory> items = db.getList(cmpCode);
		for(int i=0;i<items.size();i++){
			iDesc.append("  -").append(items.get(i).getDescription()).append("\n");
		}
		db.close();
		return iDesc;
	}
}
