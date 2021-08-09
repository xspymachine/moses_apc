package com.xpluscloud.moses_apc;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xpluscloud.moses_apc.dbase.CustomerDbManager;
import com.xpluscloud.moses_apc.getset.TimeInOut;

import java.util.ArrayList;

public class DashboardArrayAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final String[] values;
	private TimeInOut dbo;
	
	ArrayList<Integer> itemPos = new ArrayList<Integer>();
 
	public DashboardArrayAdapter(Context context, String[] values, TimeInOut io) {
		super(context, R.layout.list_row, values);
		this.context = context;
		this.values = values;
		this.dbo = io;
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View rowView = inflater.inflate(R.layout.list_row, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.label);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
		
		String itemText = values[position];
	
		if(itemText.equalsIgnoreCase("Time In")) {
			if(dbo!=null && dbo.getInout()==1) {
				String customerName= this.getCustomerName(dbo.getCustomerCode());
				itemText += " - " + customerName;
				textView.setTextColor(Color.RED);
			}
			else {
				textView.setTextColor(Color.BLACK);
			}
		}
		
		textView.setText(itemText);
				
		switch(position) {

			case 0:
				imageView.setImageResource(R.drawable.ic_summary);
				break;
			case 1:
				imageView.setImageResource(R.drawable.ic_timein);
				break;
			case 2:
				imageView.setImageResource(R.drawable.ic_ccall);
				break;
			case 3:
			case 4:
				imageView.setImageResource(R.drawable.ic_promo);
				break;
			case 5:
				imageView.setImageResource(R.drawable.ic_sales);
				break;
//			case 6:
//				imageView.setImageResource(R.drawable.ic_inventory);
//				break;
			case 6:
				imageView.setImageResource(R.drawable.ic_competitor);
				break;
			case 7:
				imageView.setImageResource(R.drawable.ic_camera);
				break;
			case 8:
				imageView.setImageResource(R.drawable.ic_issues);
				break;
			case 9:
				imageView.setImageResource(R.drawable.ic_out);
				break;
			case 10:
				imageView.setImageResource(R.drawable.ic_mark);
				break;
			case 11:
				imageView.setImageResource(R.drawable.ic_upsig);
				break;
			case 12:
				imageView.setImageResource(R.drawable.ic_upcamera);
				break;
			case 13:
				imageView.setImageResource(R.drawable.ic_down);
				break;		
			default:
				imageView.setImageResource(R.drawable.ic_info);
	    		break;	
		}
		return rowView;
	}
	
	private String getCustomerName(String ccode) {
		String custName;
		
		CustomerDbManager db = new CustomerDbManager(context);
		db.open();
		custName  = db.getCustomerName(ccode);		
		db.close();
		
		return custName;
	}

}
