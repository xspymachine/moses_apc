package com.xpluscloud.moses_apc;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xpluscloud.moses_apc.dbase.CompetitorPriceDbManager;
import com.xpluscloud.moses_apc.getset.CompetitorPrice;
import com.xpluscloud.moses_apc.util.DbUtil;
import com.xpluscloud.moses_apc.util.Master;

import java.text.DecimalFormat;
import java.util.List;
//import android.widget.Filterable;

public class CompetitorPricingActivity extends ListActivity {
	
	String ccode;
	String cusName;
	String cusAddress;
	String devId;
		
	TextView separator;
	LinearLayout layout;
	
	ItemListAdapter adapter;
	
	Context context;
	
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
//            R.layout.csi_spinner_phone, Master.pckg_option);
	
//	public static HashMap<Integer,Double> myList=new HashMap<Integer,Double>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cutomer_pricing);
		setTitle("Competitors Price");
		context = CompetitorPricingActivity.this;
		
		
		
		Bundle b = getIntent().getExtras();
		ccode = b.getString("customerCode");
		cusName = b.getString("customerName");
		cusAddress = b.getString("customerAddress");
		devId = b.getString("devId");
		
		layout = (LinearLayout) findViewById(R.id.priceHeader);
		separator = (TextView) findViewById(R.id.separator2);
		
		TextView tvCusName = (TextView) findViewById(R.id.tvCusName);
		TextView tvCusAddress = (TextView) findViewById(R.id.tvCusAddress);
		tvCusName.setText(cusName);
		tvCusAddress.setText(cusAddress);
		
		Button btAdd = (Button) findViewById(R.id.btAddComItem);
		btAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				layout.setVisibility(View.VISIBLE);
				separator.setVisibility(View.VISIBLE);
				comItemList();
			}
		});
		
		Button btSubmit = (Button) findViewById(R.id.btCPRSubmit);
		btSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("btSubmit", "onClick");
				submitToServer();
			}
		});
		
		CompetitorPriceDbManager db = new CompetitorPriceDbManager(this);
		db.open();		
		db.addAllItems(ccode, devId);				
		db.close();
		
		initListView(ccode);
		
		ListView listView = getListView();
		registerForContextMenu(listView);
		
	}	
	
	private void comItemList(){
		Intent intent = new Intent(context, CompetitorsItemList.class);
		Bundle b = new Bundle();
		
		b.putString("devId", devId);		
		b.putString("customerCode", ccode);
		intent.putExtras(b); 
		startActivity(intent);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();		
		
		initListView(ccode);
		
	}
		
	private void initListView(String ccode) {
		
		CompetitorPriceDbManager db = new CompetitorPriceDbManager(this);
		db.open();	
		
		List<CompetitorPrice> list = db.getList(ccode);
		adapter = new ItemListAdapter(this, list); 
		setListAdapter(adapter);
		if(!list.isEmpty()){
			layout.setVisibility(View.VISIBLE);
			separator.setVisibility(View.VISIBLE);
		}else{
			layout.setVisibility(View.INVISIBLE);
			separator.setVisibility(View.INVISIBLE);
		}
//		for(int i=0;i<list.size();i++)
//	    {
//	       myList.put(i,list.get(i).getPrice());
//	    }
		db.close();	
	}
	
	private class ItemListAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private List<CompetitorPrice> originalList;
		private List<CompetitorPrice> filteredList;
		
		
		public ItemListAdapter(Context context, List<CompetitorPrice> list) {
			inflater = LayoutInflater.from(context);
			originalList =  list;
			filteredList = list;			
		}
		                 
		public void setValue(List<CompetitorPrice> list) {
		     this.filteredList = list;
		     this.notifyDataSetChanged();
		}

			
		public int getCount() {
			return filteredList.size();
		}
		
		public Object getItem(int position) {
			return filteredList.get(position);
		}
		
		public long getItemId(int position) {
			return filteredList.get(position).getId();
		}
		
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			
			if(convertView == null) {
				convertView = inflater.inflate(R.layout.list_customer_pricing, null);
								
				holder = new ViewHolder();
				
				holder.itemDescription = (TextView) convertView.findViewById(R.id.tviDescription);
				holder.itemQty = (EditText) convertView.findViewById(R.id.etiQty);
				holder.itemPrice = (EditText) convertView.findViewById(R.id.etiPrice);
				holder.btDelete = (ImageButton) convertView.findViewById(R.id.imageButtonDel);
				holder.cmpLayout = (LinearLayout) convertView.findViewById(R.id.cmpLayout);
//				holder.itemComCode = (TextView) convertView.findViewById(R.id.tvComCode);
//				holder.compName = (Spinner) convertView.findViewById(R.id.spinner1);
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			if(position % 2 ==1) holder.cmpLayout.setBackgroundColor(Color.rgb(231, 249, 255));
			else holder.cmpLayout.setBackgroundColor(Color.rgb(195, 240, 255));
			
//			holder.itemComCode.setText(filteredList.get(position).getComcode());
			holder.itemDescription.setText(filteredList.get(position).getDescription());		
//			holder.itemPrice.setText(filteredList.get(position).getPrice());				
			holder.itemPrice.setOnFocusChangeListener(new OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                	String priceString = holder.itemPrice.getText().toString();
                	final double price = priceString.equals("") ? 0.0 : Double.valueOf(priceString);
//                	final int status = filteredList.get(position).getStatus();
                	
                    if (hasFocus){
                    	if(price == 0.0) holder.itemPrice.setText("");
                    	final String code = filteredList.get(position).getComcode();
                    	holder.itemPrice.setBackgroundColor(0xFFFFF3CE);
                    	holder.itemPrice.addTextChangedListener(new TextWatcher() {
							
							@Override
							public void onTextChanged(CharSequence s, int start, int before, int count) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void beforeTextChanged(CharSequence s, int start, int count,
                                                          int after) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void afterTextChanged(Editable s) {
								// TODO Auto-generated method stub	
								Log.e("code",s.toString());
								CompetitorPriceDbManager db = new CompetitorPriceDbManager(context);
								db.open();
								db.updatePrice(code, s.toString());
//								if(status == 1) db.updateStatus(code, status);
//								Double pr = s.toString().equals("") ? 0.0 : Double.parseDouble(s.toString());
//								myList.put(position,pr);
								db.close();
							}
						});
//                    	Log.e("code",filteredList.get(position).getComcode());
                    }else {
                    	if(price == 0.0) holder.itemPrice.setText("0.00");
                    	
                    	DecimalFormat df = new DecimalFormat("#.00");
                		holder.itemPrice.setText(df.format(Double.parseDouble(holder.itemPrice.getText().toString())));
//                    	if(!holder.itemPrice.getText().toString().contains(".")) holder.itemPrice.setText(holder.itemPrice.getText().toString()+".00");
                    	holder.itemPrice.setBackgroundColor(0xFFA9FF53);
                    }
                }
            });
			
//			holder.itemQty.setText(filteredList.get(position).getPrice());				
			holder.itemQty.setOnFocusChangeListener(new OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                	String qtyString = holder.itemQty.getText().toString();
                	final double qty = qtyString.equals("") ? 0 : Integer.valueOf(qtyString);
//                	final int status = filteredList.get(position).getStatus();
                	
                    if (hasFocus){
                    	if(qty == 0) holder.itemPrice.setText("");
                    	final String code = filteredList.get(position).getComcode();
                    	holder.itemQty.setBackgroundColor(0xFFFFF3CE);
                    	holder.itemQty.addTextChangedListener(new TextWatcher() {
							
							@Override
							public void onTextChanged(CharSequence s, int start, int before, int count) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void beforeTextChanged(CharSequence s, int start, int count,
                                                          int after) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void afterTextChanged(Editable s) {
								// TODO Auto-generated method stub	
//								Log.e("code",s.toString());
								CompetitorPriceDbManager db = new CompetitorPriceDbManager(context);
								db.open();
//								db.updateQty(code, s.toString());
//								if(status == 1) db.updateStatus(code, status);
//								Double pr = s.toString().equals("") ? 0.0 : Double.parseDouble(s.toString());
//								myList.put(position,pr);
								db.close();
							}
						});
//                    	Log.e("code",filteredList.get(position).getComcode());
                    }else {
                    	if(qty == 0) holder.itemQty.setText("0");
                    	
//                    	DecimalFormat df = new DecimalFormat("#.00");
                		holder.itemQty.setText(holder.itemQty.getText().toString());
//                    	if(!holder.itemPrice.getText().toString().contains(".")) holder.itemPrice.setText(holder.itemPrice.getText().toString()+".00");
                    	holder.itemQty.setBackgroundColor(0xFFA9FF53);
                    }
                }
            });
			
			holder.btDelete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String comcode = filteredList.get(position).getComcode();
//					Log.e("onClick",comcode);
					CompetitorPriceDbManager db = new CompetitorPriceDbManager(context);
					db.open();
					db.deleteItem(comcode);
					db.close();
					
					initListView(ccode);
				}
			});
									
//			holder.compName.setAdapter(spinnerArrayAdapter);
//			holder.compName.setSelection(Master.PCKG_UNIT);
			
//			} else {
//				holder = (ViewHolder) convertView.getTag();
//			}
			return convertView;
		}
		
		private class ViewHolder {
			public TextView itemDescription;
//			public TextView itemComCode;
			public EditText itemQty;
			public EditText itemPrice;
			public ImageButton btDelete;
			public LinearLayout cmpLayout;
//			public Spinner compName;
		}
		
	}
	
	private void submitToServer(){
		String cmpAdd = Master.COMPETITORS_PRICE;
//		String cmpEdit = Master.COMPETITORS_EPRICE;
		
		CompetitorPriceDbManager db = new CompetitorPriceDbManager(context);
		db.open();
		List<CompetitorPrice> list = db.getPending(ccode);
		
		
		String message;
		
		for(int i=0; i<list.size(); i++){			
//			if(list.get(i).getStatus() == 2) message = cmpEdit;
//			else 
			message = cmpAdd;
			Log.e("list", message);
			message = cmpAdd   				 	 + " " +
					devId 						 + ";" +
					list.get(i).getCcode()  	 + ";" +
					list.get(i).getIcode() 		 + ";" +
					list.get(i).getComcode() 	 + ";" + 
					list.get(i).getDescription() + ";" + 
					list.get(i).getCatcode() 	 + ";" + 
//					list.get(i).getPrice()  	 + ";" + 
					list.get(i).getQty()		 + ";" +
					list.get(i).getStatus();
			
			DbUtil.saveMsg(context,DbUtil.getGateway(context), message);
			db.updateStatus(list.get(i).getComcode(), list.get(i).getStatus());
				
		}
		finish();
		db.close();
	}
	
}
