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
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xpluscloud.mosesshell_davao.dbase.TruckDbManager;
import com.xpluscloud.mosesshell_davao.getset.Truck;
import com.xpluscloud.mosesshell_davao.util.DbUtil;
import com.xpluscloud.mosesshell_davao.util.Master;

import java.util.List;

public class TruckActivity extends Activity {
	
	Context context;
	
	LinearLayout[] llx ;
	TextView[] tx  ;
	TextView[] tx1 ;
	EditText[] ex2 ;
	EditText[] ex3 ;
	ImageButton[] ib;
	
	String ccode="";
	String cusName="";
	String cusAddress="";
	String devId="";
	
	List<Truck> listem;
	LinearLayout ll;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_form4);
	    
	    setTitle("Truck Details");
	    
	    context = TruckActivity.this;
	    
	    ll = (LinearLayout) findViewById(R.id.mylinear);
	    
	    Bundle b = getIntent().getExtras();
		ccode = b.getString("customerCode");
		devId = b.getString("devId");
		
		Log.e("ccode",ccode);
	    
	    contentSetup();    

	}
	
	private void contentSetup(){	
		TruckDbManager db = new TruckDbManager(this);
	    db.open();
	    db.addAllItems(ccode, devId);
	    listem = db.getList(ccode);
	    db.close();
	    
		ll.removeAllViews();
		
	    llx = new LinearLayout[listem.size()];
	    tx = new TextView[listem.size()];
	    tx1 = new TextView[listem.size()];
	    ex2 = new EditText[listem.size()];
	    ex3 = new EditText[listem.size()];
//	    ex4 = new EditText[listem.size()];
	    ib = new ImageButton[listem.size()];

	    for (int i = 0; i < listem.size(); i++) {
	        llx[i] = new LinearLayout(this);
	        tx[i]  = new TextView(this);
	        tx1[i] =new TextView(this);
	        ex2[i] =new EditText(this);
	        ex3[i] =new EditText(this);
	        ib[i] =new ImageButton(this);
	        ib[i].setImageResource(R.drawable.delete32);
	        
	        if(i % 2 ==1) llx[i].setBackgroundColor(Color.rgb(231, 249, 255));
			else llx[i].setBackgroundColor(Color.rgb(195, 240, 255));
	        
	        tx[i].setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT,0.2f));
	        tx1[i].setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT,0.3f));
	        LinearLayout.LayoutParams etLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT,0.2f);
	        etLayoutParams.setMargins(5, 0, 0, 0);
	        ex2[i].setLayoutParams(etLayoutParams);
	        ex3[i].setLayoutParams(etLayoutParams);
	        ib[i].setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT,0f));
	        	        	        
	        tx[i].setText(listem.get(i).getName());	        
	        tx1[i].setText(listem.get(i).getCapacity());
	        
	        ex2[i].setText(""+listem.get(i).getType());
	        ex2[i].setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
	        ex2[i].setBackgroundColor(0xFFA9FF53);
	        etSettings2(ex2[i],listem.get(i).getTruckid());
	        
	        ex3[i].setText(""+listem.get(i).getQuantity());
	        ex3[i].setInputType(InputType.TYPE_CLASS_NUMBER);
	        ex3[i].setBackgroundColor(0xFFA9FF53);
	        etSettings3(ex3[i],listem.get(i).getTruckid());
	        
	        llx[i].setId(i);
	        llx[i].setClickable(true);

	        llx[i].addView(tx[i]);
	        llx[i].addView(ex2[i]);
	        llx[i].addView(tx1[i]);
	        llx[i].addView(ex3[i]);
	        llx[i].addView(ib[i]);

	        ll.addView(llx[i]);
	    }
		
	}
			
	private void etSettings2(final EditText etType, final int truckId){
		etType.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){                	
                	
                	etType.setBackgroundColor(0xFFFFF3CE);
                	etType.addTextChangedListener(new TextWatcher() {
						
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
							Log.e("code",""+truckId);
							TruckDbManager db = new TruckDbManager(context);
							db.open();
							db.updateType(ccode,truckId, s.toString());
							db.close();
						}
					});
                }else etType.setBackgroundColor(0xFFA9FF53);
            }
        });
	}
	
	private void etSettings3(final EditText etQty, final int truckId){
		etQty.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
            	String qtyString = etQty.getText().toString();
            	final int qty = qtyString.equals("") ? 0 : Integer.valueOf(qtyString);
            	
                if (hasFocus){
                	if(qty == 0) etQty.setText("");
                	etQty.setBackgroundColor(0xFFFFF3CE);
                	etQty.addTextChangedListener(new TextWatcher() {
						
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
							TruckDbManager db = new TruckDbManager(context);
							db.open();
							db.updateQty(ccode,truckId, s.toString());
							db.close();
						}
					});
                }else {
                	if(qty == 0) etQty.setText("0");
                	etQty.setBackgroundColor(0xFFA9FF53);
                }
            }
        });
	}
	
	public void submitToServer(View view){
		
		TruckDbManager db = new TruckDbManager(context);
		db.open();
		List<Truck> list = db.getPending(ccode);
		
		String message="";
		
		for(int i=0; i<list.size(); i++){			
			message = Master.CMD_TRUCK		 	 + " " +
					devId 						 + ";" +
					ccode					 	 + ";" +
					list.get(i).getTruckid()	 + ";" +
					list.get(i).getType() 	 	 + ";" +
					list.get(i).getQuantity() 	 ;

			DbUtil.saveMsg(context,DbUtil.getGateway(context), message);
		}
		db.updateStatus(ccode);
		finish();
		db.close();
	}

}