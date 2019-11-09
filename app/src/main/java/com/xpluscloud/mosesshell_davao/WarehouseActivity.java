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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xpluscloud.mosesshell_davao.dbase.WarehouseDbManager;
import com.xpluscloud.mosesshell_davao.getset.CompetitorPrice;
import com.xpluscloud.mosesshell_davao.util.DbUtil;
import com.xpluscloud.mosesshell_davao.util.Master;

import java.util.List;

public class WarehouseActivity extends Activity {
	
	Context context;
	
	LinearLayout[] llx ;
	TextView[] tx  ;
	EditText[] ex2 ;
	
	String ccode="";
	String cusName="";
	String cusAddress="";
	String devId="";
	
	List<CompetitorPrice> listem;
	LinearLayout ll;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_form5);
	    
	    setTitle("Warehouse Capacity Details");
	    
	    context = WarehouseActivity.this;
	    
	    ll = (LinearLayout) findViewById(R.id.mylinear);
	    
	    Bundle b = getIntent().getExtras();
		ccode = b.getString("customerCode");
		devId = b.getString("devId");
	    
	    contentSetup();    

	}
	
	private void contentSetup(){	
		WarehouseDbManager db = new WarehouseDbManager(this);
	    db.open();
	    db.addAllItems(ccode, devId);
	    listem = db.getList(ccode);
	    db.close();
	    
		ll.removeAllViews();
		
	    llx = new LinearLayout[listem.size()];
	    tx = new TextView[listem.size()];
	    ex2 = new EditText[listem.size()];

	    for (int i = 0; i < listem.size(); i++) {
	        llx[i] = new LinearLayout(this);
	        tx[i]  = new TextView(this);
	        ex2[i] =new EditText(this);
	        
	        if(i % 2 ==1) llx[i].setBackgroundColor(Color.rgb(231, 249, 255));
			else llx[i].setBackgroundColor(Color.rgb(195, 240, 255));
	        
	        tx[i].setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT,0.5f));
	        LinearLayout.LayoutParams etLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT,0.5f);
	        etLayoutParams.setMargins(5, 0, 0, 0);
	        ex2[i].setLayoutParams(etLayoutParams);        
	        	        	        
	        tx[i].setText(listem.get(i).getDescription());	        
	        
	        ex2[i].setText(""+listem.get(i).getQty());
	        ex2[i].setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
	        ex2[i].setBackgroundColor(0xFFA9FF53);
	        etSettings(ex2[i],listem.get(i).getComcode());
	        
	        llx[i].setId(i);
	        llx[i].setClickable(true);

	        llx[i].addView(tx[i]);
	        llx[i].addView(ex2[i]);

	        ll.addView(llx[i]);
	    }
		
	}
	
	private void etSettings(final EditText etQty, final String itemcode){
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
							WarehouseDbManager db = new WarehouseDbManager(context);
							db.open();
							db.updateQty(ccode,itemcode, s.toString());
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
		
		WarehouseDbManager db = new WarehouseDbManager(context);
		db.open();
		List<CompetitorPrice> list = db.getPending(ccode);
		
		String message="";
		
		for(int i=0; i<list.size(); i++){			
			message = Master.CMD_WHCAP		 	 + " " +
					devId 						 + ";" +
					ccode					 	 + ";" +
					list.get(i).getComcode()	 + ";" +
					list.get(i).getQty() 	 	 ;
			
			Log.e("message", message);

			DbUtil.saveMsg(context,DbUtil.getGateway(context), message);
		}
		db.updateStatus(ccode);
		finish();
		db.close();
	}

}