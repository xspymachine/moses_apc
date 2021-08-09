package com.xpluscloud.moses_apc;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.xpluscloud.moses_apc.dbase.ReportDbManager;


public class TableViewActivity extends Activity {
	String TAG ="TableViewActivity";
	Context context;
	
	TableLayout table;
	
	public String cCode="";
	public String cName="";
	public String cAddress="";
	
	public String tableName="";
	public String date1="";
	public String date2="";
	public String param="";
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.table_view);		
		context = TableViewActivity.this;
		
		Bundle b = getIntent().getExtras();
		
		tableName	= b.getString("tableName");
		if(tableName==null || tableName=="") finish(); //die immediately
		
		cCode 		= b.getString("customerCode");
		
		RelativeLayout rlCustomer = (RelativeLayout) findViewById(R.id.rlCustomer);
		
		if(cCode==null || cCode=="") {
			rlCustomer.setVisibility(View.GONE);
		}
		else {
			try{
				cName 	= b.getString("customerName");
				cAddress = b.getString("customerAddress");
				
				TextView tvCustomer = (TextView) findViewById(R.id.tvCustomer);
				tvCustomer.setText(cName +"\n" + cAddress);
			
			}catch(Exception e){};
		}
		date1 	= b.getString("date1");
		date2 	= b.getString("date2");
		
		updateTable();
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    
	}
		
	private void refreshTable() {
		table.removeAllViews();
	    updateTable();
	}
	
	private void updateTable() {
		
		ReportDbManager db = new ReportDbManager(this);
		
		db.open();
		Cursor c = db.getCursor(tableName.toUpperCase(),cCode,null,null);
		
		table = (TableLayout) findViewById(R.id.table101);
        table.setStretchAllColumns(true);
        int x = 0;
        
        while (c.moveToNext()) {
        	TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT)
            );
            
            if (x==0) {
            	for (int i=0; i<c.getColumnCount(); i++) {
	        		
	        		String colName = c.getColumnName(i);
	        		TextView column = new TextView(this);
	                column.setLayoutParams(new TableRow.LayoutParams(
	                        LayoutParams.MATCH_PARENT,
	                        LayoutParams.WRAP_CONTENT)
	                );
	                column.setText(colName.toUpperCase());
	                column.setTextSize(16);
	                column.setTextColor(0xFFFFFFFF);
	                column.setPadding(4, 8, 4, 8);
	                column.setGravity(Gravity.CENTER);
	                if(i==0) {
	                	column.setVisibility(View.INVISIBLE);
	                	column.setWidth(1);
	                }
	                row.addView(column);
	                row.setBackgroundColor(0xFF2693FF);
	        	}
            }
            
            else {
	        	for (int i=0; i<c.getColumnCount(); i++) {
	        		
	        		String field = c.getString(i);
	        		TextView column = new TextView(this);
	                column.setLayoutParams(new TableRow.LayoutParams(
	                        LayoutParams.MATCH_PARENT,
	                        LayoutParams.WRAP_CONTENT)
	                );
	                column.setText(field);
	                column.setTextSize(14);
	                column.setTextColor(Color.BLACK);
	                column.setPadding(4, 6, 4, 6);
	                column.setGravity(Gravity.CENTER);
	                if(i==0) {
	                	column.setVisibility(View.INVISIBLE);
	                	column.setWidth(1);
	                }
	                row.addView(column);
	                
	                if(x % 2 == 0){
	                	row.setBackgroundColor(0xFFD2D2DF);
		   			}
		   			else {
		   				row.setBackgroundResource(0x00);
		   			}
	                
	        	}
            }
            x++;
        	table.addView(row);
        }
        
        
       // setContentView(table);
        
        c.close();
        db.close();
	}
	
}