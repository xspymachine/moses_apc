package com.xpluscloud.mosesshell_davao;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xpluscloud.mosesshell_davao.dbase.CompetitorPriceDbManager;
import com.xpluscloud.mosesshell_davao.getset.CompetitorPrice;
import com.xpluscloud.mosesshell_davao.util.DateUtil;
import com.xpluscloud.mosesshell_davao.util.DbUtil;
import com.xpluscloud.mosesshell_davao.util.Master;

import java.util.List;

public class NewCompPrice extends Activity {
	
	Context context;
	
	LinearLayout[] llx ;
	TextView[] tx  ;
	EditText[] ex1 ;
	EditText[] ex2 ;
	EditText[] ex3 ;
	EditText[] ex4 ;
	ImageButton[] ib;
	
	String ccode;
	String cusName;
	String cusAddress;
	String devId;
	
	List<CompetitorPrice> listem;
	LinearLayout ll;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_form3);
	    
	    context = NewCompPrice.this;
	    
	    ll = (LinearLayout) findViewById(R.id.mylinear);
	    TextView tvCusName = (TextView) findViewById(R.id.tvName);
	    TextView tvAddress = (TextView) findViewById(R.id.tvAddress);
	    
	    Bundle b = getIntent().getExtras();
		ccode = b.getString("customerCode");
		cusName = b.getString("customerName");
		cusAddress = b.getString("customerAddress");
		devId = b.getString("devId");
		
		tvCusName.setText("Name:		"+cusName);
		tvAddress.setText("Address:	"+cusAddress);
			    
	    contentSetup();    

	}
	
	private void contentSetup(){	
		CompetitorPriceDbManager db = new CompetitorPriceDbManager(this);
	    db.open();
	    db.addAllItems(ccode, devId);
	    listem = db.getList(ccode);
	    db.close();
	    
		ll.removeAllViews();
		
	    llx = new LinearLayout[listem.size()];
	    tx = new TextView[listem.size()];
	    ex1 = new EditText[listem.size()];
	    ex2 = new EditText[listem.size()];
	    ex3 = new EditText[listem.size()];
	    ex4 = new EditText[listem.size()];
	    ib = new ImageButton[listem.size()];

	    for (int i = 0; i < listem.size(); i++) {
	        llx[i] = new LinearLayout(this);
	        tx[i] = new TextView(this);
	        ex1[i] =new EditText(this);
	        ex2[i] =new EditText(this);
	        ex3[i] =new EditText(this);
	        ex4[i] =new EditText(this);
	        ib[i] =new ImageButton(this);
	        ib[i].setImageResource(R.drawable.delete32);
	        
	        if(i % 2 ==1) llx[i].setBackgroundColor(Color.rgb(231, 249, 255));
			else llx[i].setBackgroundColor(Color.rgb(195, 240, 255));
	        
	        tx[i].setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT,0.3f));
	        ex1[i].setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT,0.17f));
	        LinearLayout.LayoutParams etLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT,0.17f);
	        etLayoutParams.setMargins(5, 0, 0, 0);
	        ex2[i].setLayoutParams(etLayoutParams);
	        ex3[i].setLayoutParams(etLayoutParams);
	        ex4[i].setLayoutParams(etLayoutParams);
	        ib[i].setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT,0f));
	        	        
	        ibSettings(ib[i],listem.get(i).getComcode());
	        
	        tx[i].setText(listem.get(i).getDescription());
	        tx[i].setTextSize(15);
	        
	        ex1[i].setText(listem.get(i).getDtrPrice());
	        ex1[i].setInputType(InputType.TYPE_CLASS_NUMBER);
	        etSettings(ex1[i],listem.get(i).getComcode());
	        
	        ex2[i].setText(""+listem.get(i).getRetailPrice());
	        ex2[i].setInputType(InputType.TYPE_CLASS_NUMBER);
	        etSettings2(ex2[i],listem.get(i).getComcode());
	        
	        ex3[i].setText(""+listem.get(i).getVol());
	        ex3[i].setInputType(InputType.TYPE_CLASS_NUMBER);
	        etSettings3(ex3[i],listem.get(i).getComcode());
	        
	        ex4[i].setText(""+listem.get(i).getIoh());
	        ex4[i].setInputType(InputType.TYPE_CLASS_NUMBER);
	        etSettings4(ex4[i],listem.get(i).getComcode());
	        
	        llx[i].setId(i);
	        llx[i].setClickable(true);
	        final int j = i;
//	        llx[i].setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					msg(tx[j].getText().toString());
//				}
//	        });

	        llx[i].addView(tx[i]);
	        llx[i].addView(ex1[i]);
	        llx[i].addView(ex2[i]);
	        llx[i].addView(ex3[i]);
	        llx[i].addView(ex4[i]);
	        llx[i].addView(ib[i]);

	        ll.addView(llx[i]);
	    }
		
	}
	
	private void ibSettings(final ImageButton ibDelete, final String comCode){
		ibDelete.setVisibility(View.INVISIBLE);
		ibDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				String comcode = filteredList.get(position).getComcode();
				Log.e("onClick",comCode);
				CompetitorPriceDbManager db = new CompetitorPriceDbManager(context);
				db.open();
				db.deleteItem(comCode);
				db.close();
				
				contentSetup();
				
			}
		});
	}
	
	private void etSettings(final EditText etPrice, final String comCode){
		etPrice.setBackgroundColor(0xFFA9FF53);
		etPrice.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
            	String priceString = etPrice.getText().toString();
            	final double price = priceString.equals("") ? 0 : Double.valueOf(priceString);
//            	final int status = filteredList.get(position).getStatus();
            	
                if (hasFocus){
                	if(price == 0) etPrice.setText("");
//                	final String code = filteredList.get(position).getComcode();
                	etPrice.setBackgroundColor(0xFFFFF3CE);
                	etPrice.addTextChangedListener(new TextWatcher() {
						
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
							db.updatePrice(comCode, s.toString());
//							if(status == 1) db.updateStatus(code, status);
//							Double pr = s.toString().equals("") ? 0.0 : Double.parseDouble(s.toString());
//							myList.put(position,pr);
							db.close();
						}
					});
//                	Log.e("code",filteredList.get(position).getComcode());
                }else {
                	if(price == 0) etPrice.setText("0");
                	else{
//	                	DecimalFormat df = new DecimalFormat("#.00");
//	                	etPrice.setText(df.format(Double.parseDouble(etPrice.getText().toString())));
                	}
//                	if(!holder.itemPrice.getText().toString().contains(".")) holder.itemPrice.setText(holder.itemPrice.getText().toString()+".00");
                	etPrice.setBackgroundColor(0xFFA9FF53);
                }
            }
        });
	}
	
	private void etSettings2(final EditText etRPrice, final String comCode){
//		etQty.setVisibility(View.GONE);
		etRPrice.setBackgroundColor(0xFFA9FF53);
		etRPrice.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
            	String priceString = etRPrice.getText().toString();
            	final double qty = priceString.equals("") ? 0 : Double.valueOf(priceString);
//            	final int status = filteredList.get(position).getStatus();
            	
                if (hasFocus){
                	if(qty == 0) etRPrice.setText("");
//                	final String code = filteredList.get(position).getComcode();
                	etRPrice.setBackgroundColor(0xFFFFF3CE);
                	etRPrice.addTextChangedListener(new TextWatcher() {
						
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
							db.updateRPrice(comCode, s.toString());
//							if(status == 1) db.updateStatus(code, status);
//							Double pr = s.toString().equals("") ? 0.0 : Double.parseDouble(s.toString());
//							myList.put(position,pr);
							db.close();
						}
					});
//                	Log.e("code",filteredList.get(position).getComcode());
                }else {
                	if(qty == 0) etRPrice.setText("0");
                	else{
//	                	DecimalFormat df = new DecimalFormat("#.00");
//	                	etRPrice.setText(df.format(Double.parseDouble(etRPrice.getText().toString())));
                	}
//                	if(!holder.itemPrice.getText().toString().contains(".")) holder.itemPrice.setText(holder.itemPrice.getText().toString()+".00");
                	etRPrice.setBackgroundColor(0xFFA9FF53);
                }
            }
        });
	}
	
	private void etSettings3(final EditText etVol, final String comCode){
		etVol.setBackgroundColor(0xFFA9FF53);
		etVol.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
            	String volString = etVol.getText().toString();
            	final int vol = volString.equals("") ? 0 : Integer.valueOf(volString);
//            	final int status = filteredList.get(position).getStatus();
            	
                if (hasFocus){
                	if(vol == 0) etVol.setText("");
//                	final String code = filteredList.get(position).getComcode();
                	etVol.setBackgroundColor(0xFFFFF3CE);
                	etVol.addTextChangedListener(new TextWatcher() {
						
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
							db.updateVol(comCode, s.toString());
//							if(status == 1) db.updateStatus(code, status);
//							Double pr = s.toString().equals("") ? 0.0 : Double.parseDouble(s.toString());
//							myList.put(position,pr);
							db.close();
						}
					});
//                	Log.e("code",filteredList.get(position).getComcode());
                }else {
                	if(vol == 0) etVol.setText("0");
                	else{
//	                	DecimalFormat df = new DecimalFormat("#.00");
//	                	etQty.setText(etQty.getText().toString());
                	}
//                	if(!holder.itemPrice.getText().toString().contains(".")) holder.itemPrice.setText(holder.itemPrice.getText().toString()+".00");
                	etVol.setBackgroundColor(0xFFA9FF53);
                }
            }
        });
	}
	
	private void etSettings4(final EditText etIoh, final String comCode){
		etIoh.setBackgroundColor(0xFFA9FF53);
		etIoh.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
            	String iohString = etIoh.getText().toString();
            	final int ioh = iohString.equals("") ? 0 : Integer.valueOf(iohString);
//            	final int status = filteredList.get(position).getStatus();
            	
                if (hasFocus){
                	if(ioh == 0) etIoh.setText("");
//                	final String code = filteredList.get(position).getComcode();
                	etIoh.setBackgroundColor(0xFFFFF3CE);
                	etIoh.addTextChangedListener(new TextWatcher() {
						
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
							db.updateIoh(comCode, s.toString());
//							if(status == 1) db.updateStatus(code, status);
//							Double pr = s.toString().equals("") ? 0.0 : Double.parseDouble(s.toString());
//							myList.put(position,pr);
							db.close();
						}
					});
//                	Log.e("code",filteredList.get(position).getComcode());
                }else {
                	if(ioh == 0) etIoh.setText("0");
                	else{
//	                	DecimalFormat df = new DecimalFormat("#.00");
//	                	etQty.setText(etQty.getText().toString());
                	}
//                	if(!holder.itemPrice.getText().toString().contains(".")) holder.itemPrice.setText(holder.itemPrice.getText().toString()+".00");
                	etIoh.setBackgroundColor(0xFFA9FF53);
                }
            }
        });
	}


//	private void msg(String x){
//	    Toast.makeText(this, x, Toast.LENGTH_LONG).show();
//	}

//	public void cagir(View view){
//	    switch (view.getId()) {
//	    case R.id.btSubmit:
////	        for (int i = 0; i < ex.length; i++) {
////	            if(!ex[i].getText().toString().equals("")){
////	                Log.e(tx[i].getText().toString(),ex[i].getText().toString());
////	            }
////	        }
//	    	Log.e("btSubmit", "onClick");
//			submitToServer();
//	        break;
//
//	    default:
//	        break;
//	    }
//	}
	
	public void submitToServer(View view){
		Log.e("btSubmit", "onClick");
		String cmpAdd = Master.COMPETITORS_PRICE;
//		String cmpEdit = Master.COMPETITORS_EPRICE;
		
		CompetitorPriceDbManager db = new CompetitorPriceDbManager(context);
		db.open();
		List<CompetitorPrice> list = db.getPending(ccode);
		
		
		String message;
		devId = DbUtil.getSetting(context, Master.DEVID);
		
		for(int i=0; i<list.size(); i++){			
//			if(list.get(i).getStatus() == 2) message = cmpEdit;
//			else 
			message = cmpAdd;
			Log.e("list", message);
			message = cmpAdd   				 	 + " " +
					devId 						 + ";" +
					list.get(i).getCcode()  	 + ";" +
					DateUtil.strDateTime(System.currentTimeMillis()) + ";" +
					list.get(i).getIcode() 		 + ";" +
					list.get(i).getComcode() 	 + ";" +
					list.get(i).getDescription() + ";" +
					list.get(i).getCatcode() 	 + ";" +
					list.get(i).getDtrPrice()  	 + ";" +
					list.get(i).getRetailPrice() + ";" +
					list.get(i).getQty()		 + ";" +
					list.get(i).getVol()		 + ";" +
					list.get(i).getIoh()		 + ";" +
					list.get(i).getStatus()		 ;//+ ";" +
//					list.get(i).getId();
			
			DbUtil.saveMsg(context,DbUtil.getGateway(context), message);	
			db.updateStatus(list.get(i).getComcode(), list.get(i).getStatus());
				
		}
		finish();
		db.close();
	}

}