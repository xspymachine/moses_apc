package com.xpluscloud.moses_apc;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.ExpandableListView;

import com.xpluscloud.moses_apc.dbase.DeliveryDbManager;
import com.xpluscloud.moses_apc.getset.Delivery;
import com.xpluscloud.moses_apc.getset.Invoice;

import java.util.List;

public class DeliveryListActivity extends Activity {
	
	Context context;
	String cCode;
	
	SparseArray<GroupDelivery> groups = new SparseArray<GroupDelivery>();

	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_delivery_list);
	    
	    context = DeliveryListActivity.this;
		Bundle b = getIntent().getExtras();
		cCode		=b.getString("customerCode");
	    
	    createData();
	    ExpandableListView listView = (ExpandableListView) findViewById(R.id.listView);
	    ExpandableListAdapter adapter = new ExpandableListAdapter(this,groups);
	    listView.setAdapter(adapter);
	  }

	  public void createData() {
		DeliveryDbManager db =  new DeliveryDbManager(context);
		db.open();
		List<Invoice> data = db.getInvoices(cCode);
		int j=0;
		List<Delivery> items;
		for (Invoice row : data) 	{
			String subHeader = "Date: "+ row.getDate() + " - " +
					"InvoiceNo: "+ row.getInvoiceno() ;
			
			GroupDelivery group = new GroupDelivery(subHeader);
			items =db.getInvoiceItems(cCode, row.getInvoiceno());
			for (Delivery item : items) {
		        group.children.add(item);
		     }
		     groups.append(j, group);
		     j++;
		}
		db.close();
	  }

	} 


