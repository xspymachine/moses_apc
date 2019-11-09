package com.xpluscloud.mosesshell_davao;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xpluscloud.mosesshell_davao.dbase.CallSheetItemDbManager;
import com.xpluscloud.mosesshell_davao.util.Master;

public class ItemOrderActivity extends Activity {
	
	final Integer MODE_ADD = 0;
	final Integer MODE_EDIT = 1;
	
	final Integer SUGGEST_PACK = 0;
	final Integer SUGGEST_UNIT = 1;
		
	public String customerCode;
	public String customerName;
	public String customerAddress;
	
	public String itemCode;
	public String itemDescription;
	public double pricePerPack;
	public double pricePerUnit;
	public double qtyPack;
	public double qtyUnit;
	public double invtPack;
	public double invtUnit;
	public double suggestedPack;
	public double suggestedUnit;
	public Integer status;
	
	public Integer soId=0;
	
	TextView tvCustomerName;
	TextView tvCustomerAddress;
	
	TextView tvItem;
	
	//Packs
	//labels
	 TextView lPack;
	 TextView lPricePerPack;
	 TextView lInventoryPack;
	 TextView lSuggestPack;
	 TextView lAmountPack;
	
	//TextViews
	 TextView tvPricePerPack;
	 TextView tvSuggestPack;
	 TextView tvAmountPack;
	
	//EditTexts
	 EditText etQtyPack;
	 EditText etInventoryPack;
	
	
	
	//Units
	//labels
	 TextView lUnit;
	 TextView lPricePerUnit;
	 TextView lInventoryUnit;
	 TextView lSuggestUnit;
	 TextView lAmountUnit;
	
	//TextViews
	 TextView tvPricePerUnit;
	 TextView tvSuggestUnit;
	 TextView tvAmountUnit;
	
	//EditTexts
	 EditText etQtyUnit;
	 EditText etInventoryUnit;
		
	//TOTAL
	 TextView lTotal;
	
	 Button btSave;
	
	public String devId;
	public String androidId;
	
		
	Context context;
	String gateway = Master.INIT_GATEWAY;
	
	Time timeNow;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_order);
        
        context = ItemOrderActivity.this;
        
        timeNow = new Time();
        timeNow.setToNow();        
        String phDate = timeNow.format("%m-%d-%Y");
        
        setTitle("CallSheetItem Item " + phDate);
        setupView();
        
        Bundle extras = getIntent().getExtras();
        
        //Integer mode 	= extras.getInt("mode");
        
        devId 			= extras.getString("devId");
        androidId 		= extras.getString("androidid");
        customerCode	= extras.getString("customerCode");
        
        customerName 	= extras.getString("customerName");
        customerAddress	= extras.getString("customerAddress");
        itemCode		= extras.getString("itemCode");
        itemDescription = extras.getString("description");
        pricePerPack	= extras.getDouble("pricePerPack");
        pricePerUnit	= extras.getDouble("pricePerUnit");
        
        
        tvCustomerName.setText(customerName);
        tvCustomerAddress.setText(customerAddress);        
        tvItem.setText(itemDescription);
        
        lPack.setText(Master.PACKS);
        lPricePerPack.setText(Master.PRICE);
        tvPricePerPack.setText(String.valueOf(pricePerPack));
        lInventoryPack.setText(Master.INVENTORY_PACK);
        lSuggestPack.setText(Master.SUGGEST_PACK);
        lAmountPack.setText(Master.AMOUNT_PACK);
        
        lUnit.setText(Master.UNITS);
        lPricePerUnit.setText(Master.PRICE);
        tvPricePerUnit.setText(String.valueOf(pricePerUnit));
        lInventoryUnit.setText(Master.INVENTORY_UNIT);
        lSuggestUnit.setText(Master.SUGGEST_UNIT);
        lAmountUnit.setText(Master.AMOUNT_UNIT);
        
       
        lTotal.setText("Total: 0");
        
        inpQtyPack();
        inpQtyUnit();
        inpInvtPack();
        inpInvtUnit();
        
        btSave.setOnClickListener(new OnClickListener() {
	        @Override
			public void onClick(View v) {
	            Log.v("Button Clicked!", "btSubmitIO");
	            btSave.setEnabled(false);
	            save();
	        } 
	    });
        
    }
    
    private void setupView() {
    	 tvCustomerName = (TextView) findViewById(R.id.tvCName);
         tvCustomerAddress = (TextView) findViewById(R.id.tvAddress);
         
         tvItem = (TextView) findViewById(R.id.tv_item);
         
         lPack 				= (TextView) findViewById(R.id.l_pack);
         lPricePerPack 		= (TextView) findViewById(R.id.l_price_per_pack);
         tvPricePerPack 	= (TextView) findViewById(R.id.tv_price_per_pack);
         lInventoryPack 	= (TextView) findViewById(R.id.l_inventory_pack);
         etInventoryPack 	= (EditText) findViewById(R.id.et_inventory_pack);
         lSuggestPack 		= (TextView) findViewById(R.id.l_suggest_pack);
         tvSuggestPack 		= (TextView) findViewById(R.id.tv_suggest_pack);
         etQtyPack 			= (EditText) findViewById(R.id.et_qty_pack);
         lAmountPack 		= (TextView) findViewById(R.id.l_amount_pack);
         tvAmountPack 		= (TextView) findViewById(R.id.tv_amount_pack);
         
         lUnit 				= (TextView) findViewById(R.id.l_unit);
         lPricePerUnit 		= (TextView) findViewById(R.id.l_price_per_unit);
         tvPricePerUnit 	= (TextView) findViewById(R.id.tv_price_per_unit);
         lInventoryUnit 	= (TextView) findViewById(R.id.l_inventory_unit);
         etInventoryUnit 	= (EditText) findViewById(R.id.et_inventory_unit);
         lSuggestUnit 		= (TextView) findViewById(R.id.l_suggest_unit);
         tvSuggestUnit 		= (TextView) findViewById(R.id.tv_suggest_unit);
         etQtyUnit 			= (EditText) findViewById(R.id.et_qty_unit);
         lAmountUnit 		= (TextView) findViewById(R.id.l_amount_unit);
         tvAmountUnit 		= (TextView) findViewById(R.id.tv_amount_unit);
         
         lTotal 			= (TextView) findViewById(R.id.l_total);
         
         btSave 			= (Button) findViewById(R.id.bt_save);
    }
    
    private Double suggestOrder(Double inventory, Integer option) {
    	
    	//Get CP frequency
    	
    	CallSheetItemDbManager db = new CallSheetItemDbManager(context);
    	Double suggestedOrder=0.0;
    	db.open();
    	//suggestedOrder = db.getSuggestedOrder(customerCode, itemCode, inventory, option);
		db.close();
    	
		return 	suggestedOrder;
    	
    }
    
    
    private void inpInvtPack() {
    	etInventoryPack.addTextChangedListener(new TextWatcher() {
        
	        @Override
	        public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
	            // When user changed the Text                            
	        }
	         
	        @Override
	        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
	            // TODO Auto-generated method stub
	             
	        }
	         
	        @Override
	        public void afterTextChanged(Editable arg0) {
	            // TODO Auto-generated method stub
	        	
	        	try {
		        	Double suggestedOrder=suggestOrder(Double.valueOf(etInventoryPack.getText().toString()),SUGGEST_PACK);
		        	tvSuggestPack.setText(String.format("%.0f", suggestedOrder));
	        	} catch(Exception e) {}
	        }
	    });  	
	}
    
    private void inpInvtUnit() {
    	etInventoryUnit.addTextChangedListener(new TextWatcher() {
        
	        @Override
	        public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
	            // When user changed the Text                            
	        }
	         
	        @Override
	        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
	            // TODO Auto-generated method stub
	             
	        }
	         
	        @Override
	        public void afterTextChanged(Editable arg0) {
	            // TODO Auto-generated method stub
	        	try {
		        	Double suggestedOrder=suggestOrder(Double.valueOf(etInventoryUnit.getText().toString()),SUGGEST_UNIT);
		        	tvSuggestUnit.setText(String.format("%.0f", suggestedOrder));
	        	} catch(Exception e) {}
	        }
	    });
    }
    
    
 
    private void inpQtyPack() {
    		etQtyPack.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
            	
            	calculatePackAmount();  
            	
            }
             
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
                 
            }
             
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub 
            }
        });
    }
    
    private void inpQtyUnit() {
		etQtyUnit.addTextChangedListener(new TextWatcher() {
        
	        @Override
	        public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
	            // When user changed the Text
	        	
	        	calculateUnitAmount();    
	        	
	        }
	         
	        @Override
	        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
	            // TODO Auto-generated method stub
	             
	        }
	         
	        @Override
	        public void afterTextChanged(Editable arg0) {
	            // TODO Auto-generated method stub 
	        }
	    });
	}
   
    
    private void calculatePackAmount() {
    	double d = 0;
    	String qty = etQtyPack.getText().toString();
    	if(qty != null && qty.length() > 0) {
    		try {
    			d = Double.parseDouble(qty);
    		}catch (Exception e) {}
    	}    	    	
    	tvAmountPack.setText(String.format("%.2f", d*pricePerPack));
    	
    	calculateTotalAmount();
    }
    
    private void calculateUnitAmount() {
    	double d = 0;
    	String qty = etQtyUnit.getText().toString();
    	if(qty != null && qty.length() > 0) {
    		try {
    			d = Double.parseDouble(qty);
    		}catch (Exception e) { }
    	}    
    	
    	tvAmountUnit.setText(String.format("%.2f", d*pricePerUnit));
    	
    	calculateTotalAmount();
    }
    
    private void calculateTotalAmount() {
    	double amtPack=0;
    	double amtUnit=0;
    	
    	String amountPack = tvAmountPack.getText().toString();
    	String amountUnit = tvAmountUnit.getText().toString();
    	
    	if(amountPack != null && amountPack.length() > 0) {
    		try {
    			amtPack = Double.valueOf(amountPack);
    		}catch (Exception e) { }
    	}  
    	
    	if(amountUnit != null && amountUnit.length() > 0) {
    		try {
    			amtUnit = Double.valueOf(amountUnit);
    		}catch (Exception e) { }
    	}  
    	    	
    	lTotal.setText("Total: " + String.format("%.2f",amtPack + amtUnit));
    	
    }
    
    private void save() {
    	
	
	finish();
    }
    
}
