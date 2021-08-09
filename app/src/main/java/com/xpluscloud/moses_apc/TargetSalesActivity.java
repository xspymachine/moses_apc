package com.xpluscloud.moses_apc;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.xpluscloud.moses_apc.dbase.TargetDbManager;
import com.xpluscloud.moses_apc.util.DateUtil;

import java.util.Calendar;

public class TargetSalesActivity extends Activity{
	
	Context context;
	TableLayout tblLayout;
	Button btNext;
	Button btPrev;
	TextView tvMY;
	
	Calendar c = Calendar.getInstance();
	
	final String[] description = {
			"Coverage",
			"Penetration",
			"Throughput"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_target_sales);
		context = TargetSalesActivity.this;
		
		tblLayout = (TableLayout) findViewById(R.id.tableLayout);		
		tblLayout.removeAllViews();								
		
		btNext = (Button) findViewById(R.id.btNext);
		btPrev = (Button) findViewById(R.id.btPrev);
		tvMY = (TextView) findViewById(R.id.tvMY);
		tvMY.setText(DateUtil.DateToStr((c.get(Calendar.MONTH) + 1)+"-"+c.get(Calendar.YEAR)));
		
		String month = ""+(c.get(Calendar.MONTH) + 1);
		if(month.length() < 2) month = 0+month;
		
		createTableRow(c.get(Calendar.YEAR)+"-"+month);
		
//		Log.e("date1",c.get(Calendar.YEAR)+"-"+month);
		
		btNext.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				c.add(Calendar.MONTH, 1);
				tvMY.setText(DateUtil.DateToStr((c.get(Calendar.MONTH) + 1)+"-"+c.get(Calendar.YEAR)));
				tblLayout.removeAllViews();
				String month = ""+(c.get(Calendar.MONTH) + 1);
				if(month.length() < 2) month = 0+month;
				createTableRow(c.get(Calendar.YEAR)+"-"+month);
				
//				Log.e("date2",c.get(Calendar.YEAR)+"-"+month);
			}
		});
		
		btPrev.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				c.add(Calendar.MONTH, -1);
				tvMY.setText(DateUtil.DateToStr((c.get(Calendar.MONTH) + 1)+"-"+c.get(Calendar.YEAR)));
				tblLayout.removeAllViews();	
				String month = ""+(c.get(Calendar.MONTH) + 1);
				if(month.length() < 2) month = 0+month;
				createTableRow(c.get(Calendar.YEAR)+"-"+month);
				
				Log.e("date2",c.get(Calendar.YEAR)+"-"+month);
			}
		});
		
	}
	
	private void createTableRow(String date){
		
		TableRow mtr = new TableRow(context);
		mtr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
		mtr.setBackgroundColor(getResources().getColor(R.color.shellyellow));
		mtr.setPadding(0, 5, 0, 5);
		
		TextView mtv1 = new TextView(context);
		mtv1.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 5f));
		TextView mtv2 = new TextView(context);
		mtv2.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));		
		TextView mtv3 = new TextView(context);
		mtv3.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
		
		mtv1.setText("DESCRIPTION");
//		mtv2.setText("TARGET");
//		mtv3.setText("ACTUAL");
		
		mtv1.setTextColor(Color.BLACK);
		mtv2.setTextColor(Color.WHITE);
		mtv3.setTextColor(Color.WHITE);
		
//		tv1.setGravity(Gravity.CENTER | Gravity.BOTTOM);
		mtv2.setGravity(Gravity.CENTER | Gravity.BOTTOM);
		mtv3.setGravity(Gravity.CENTER | Gravity.BOTTOM);
		
		mtv1.setTextSize(25);
		mtv2.setTextSize(25);
		mtv3.setTextSize(25);
		mtv1.setTypeface(null, Typeface.BOLD);
		
//		mtv1.setBackgroundColor(R.drawable.table_border);
//		mtv2.setBackgroundColor(R.drawable.table_border);
//		mtv3.setBackgroundColor(R.drawable.table_border);
		mtv2.setBackgroundResource(R.drawable.target02);
		mtv3.setBackgroundResource(R.drawable.target01);
		
		mtr.addView(mtv1);
		mtr.addView(mtv2);
		mtr.addView(mtv3);  
		
		tblLayout.addView(mtr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
				
		Log.e("target date", date);
		
		for(int i=0;i<description.length;i++){
			String target = getTargetData(date,i);
			String actual = getActualData(date,i);
			TableRow tr = new TableRow(context);
			tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
			if (i % 2==0) {
				tr.setBackgroundColor(getResources().getColor(R.color.shellred));
			  } else {
				  tr.setBackgroundColor(getResources().getColor(R.color.shellyellow));
			  }
			
			TextView tv1 = new TextView(context);
			tv1.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 5f));		
			TextView tv2 = new TextView(context);
			tv2.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));		
			TextView tv3 = new TextView(context);
			tv3.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
			
			tv1.setText(description[i]);
			if(i==2) tv2.setText(""+target);
			else tv2.setText("5295");
			tv3.setText(""+actual);
			
			tv1.setTextColor(Color.BLACK);
			tv2.setTextColor(Color.BLACK);
			tv3.setTextColor(Color.BLACK);
//			
//			tv1.setBackgroundColor(Color.GRAY);
//			tv2.setBackgroundColor(Color.BLUE);
//			tv3.setBackgroundColor(Color.CYAN);
			
//			tv1.setGravity(Gravity.CENTER | Gravity.BOTTOM);
			tv2.setGravity(Gravity.CENTER | Gravity.BOTTOM);
			tv3.setGravity(Gravity.CENTER | Gravity.BOTTOM);
			
			tv1.setTextSize(25);
			tv2.setTextSize(25);
			tv3.setTextSize(25);
			
//			tv1.setBackgroundColor(R.drawable.table_border);
//			tv2.setBackgroundColor(R.drawable.table_border);
//			tv3.setBackgroundColor(R.drawable.table_border);
			
			tr.addView(tv1);
			tr.addView(tv2);
			tr.addView(tv3);  
			
			tblLayout.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

		}
	}
	
	private String getTargetData(String date, int opt){
		String count = "0";
		TargetDbManager db = new TargetDbManager(context);
		db.open();
		date = date.replace("-", "");
		count = db.getTargetInfo(date, opt);
		db.close();
		
		return count;			
		
	}
	
	private String getActualData(String date, int opt){
		Log.e("date",date);
		String count;
		TargetDbManager db = new TargetDbManager(context);
		db.open();		
		count = db.getActualInfo("", date, opt);
		db.close();
		
		return count;	
	}

}
