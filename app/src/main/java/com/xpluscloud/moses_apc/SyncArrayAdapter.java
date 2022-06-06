package com.xpluscloud.moses_apc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xpluscloud.moses_apc.util.LayoutUtil;

import java.util.ArrayList;

public class SyncArrayAdapter extends ArrayAdapter<String> {
		
	public final int CUSTOMER_LIST	= 0;
	public final int CPLAN			= 1;
//	public final int CHECK			= 2;
	public final int BROCHURE		= 2;
	public final int CLBAL			= 3;
	
	private final Context context;
	private final String[] values;
	    
	ArrayList<Integer> itemPos = new ArrayList<Integer>();
 
	public SyncArrayAdapter(Context context, String[] values) {
		super(context, R.layout.list_row, values);
		this.context = context;
		this.values = values;
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View rowView = inflater.inflate(R.layout.list_row, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.label);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
		textView.setText(values[position]);
 
		// Change icon based on name
		String s = values[position];
		if(values[position].contains("Resend")) position = 1000;
 
		System.out.println(""+position);
		
		switch(position) {
			
			case CUSTOMER_LIST:
//				imageView.setImageResource(R.drawable.assets_client);
				imageView.setImageBitmap(LayoutUtil.decodeSampledBitmapFromResource(context.getResources(),R.drawable.assets_client, 90,90));
				break;
			case CPLAN:  
//				imageView.setImageResource(R.drawable.assets_calendarclientlist);
				imageView.setImageBitmap(LayoutUtil.decodeSampledBitmapFromResource(context.getResources(),R.drawable.assets_calendarclientlist, 90,90));
				break;
//			case CHECK:
////				imageView.setImageResource(R.drawable.assets_calendarclientlist);
//				imageView.setImageBitmap(LayoutUtil.decodeSampledBitmapFromResource(context.getResources(),R.drawable.assets_checklist, 90,90));
//				break;
			case BROCHURE:
//				imageView.setImageResource(R.drawable.assets_calendarclientlist);
				imageView.setImageBitmap(LayoutUtil.decodeSampledBitmapFromResource(context.getResources(),R.mipmap.ic_brochure, 90,90));
				break;
			case CLBAL:
//				imageView.setImageResource(R.drawable.assets_calendarclientlist);
				imageView.setImageBitmap(LayoutUtil.decodeSampledBitmapFromResource(context.getResources(),R.mipmap.ic_credit, 90,90));
				break;
			//for resend activity
			case 1000:
//				imageView.setImageResource(R.drawable.assets_restext);
				imageView.setImageBitmap(LayoutUtil.decodeSampledBitmapFromResource(context.getResources(),R.drawable.assets_restext, 90,90));
				break;
			case 1001:
//				imageView.setImageResource(R.drawable.assets_resweb);
				imageView.setImageBitmap(LayoutUtil.decodeSampledBitmapFromResource(context.getResources(),R.drawable.assets_resweb, 90,90));
				break;
			default:
//				imageView.setImageResource(R.drawable.assets_about);
				imageView.setImageBitmap(LayoutUtil.decodeSampledBitmapFromResource(context.getResources(),R.drawable.assets_about, 90,90));
	    		break;		
		}
		
		return rowView;
	}

}
