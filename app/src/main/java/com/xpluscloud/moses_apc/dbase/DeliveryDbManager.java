package com.xpluscloud.moses_apc.dbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.xpluscloud.moses_apc.getset.Delivery;
import com.xpluscloud.moses_apc.getset.Invoice;

import java.util.ArrayList;
import java.util.List;

public class DeliveryDbManager extends DbManager {
	
	public static final String[] Delivery_PROJECTION = new String[] {
		DesContract.Delivery._ID,
		DesContract.Delivery.DATE,
		DesContract.Delivery.CCODE,
		DesContract.Delivery.INVOICENO,
		DesContract.Delivery.ITEM_CODE,
		DesContract.Delivery.PCKG,	
		DesContract.Delivery.PRICE,	
		DesContract.Delivery.QTY,
		DesContract.Delivery.SONO,
		DesContract.Delivery.STATUS
	};
	
	public DeliveryDbManager(Context context) {
		super(context);
	}
	
	

	public long AddDelivery(Delivery p) {
		ContentValues cv = new ContentValues();
		cv.put(DesContract.Delivery.DATE 			, p.getDate());
		cv.put(DesContract.Delivery.CCODE			, p.getCcode());
		cv.put(DesContract.Delivery.INVOICENO    	, p.getInvoiceno());
		cv.put(DesContract.Delivery.ITEM_CODE 		, p.getItemcode());
		cv.put(DesContract.Delivery.PCKG			, p.getPckg());
		cv.put(DesContract.Delivery.PRICE			, p.getPrice());	
		cv.put(DesContract.Delivery.QTY				, p.getQty());
		cv.put(DesContract.Delivery.SONO			, p.getSono());
		cv.put(DesContract.Delivery.STATUS 			, p.getStatus());
		
		//return db.insert(DesContract.Delivery.TABLE_NAME, null, cv);
		
		return db.insertWithOnConflict(DesContract.Delivery.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
	}
	
	public void AddDeliveries(List<Delivery> list) {
		for(int i = 0; i < list.size(); i++) {
			AddDelivery(list.get(i));
		}
	}
	
	public Delivery getDelivery(int rowId) {
		Delivery cplan;
		Cursor c = retrieveCursor(rowId, DesContract.Delivery.TABLE_NAME, Delivery_PROJECTION);
		
		if(c.moveToFirst()) {
			cplan = createDelivery(c);
		} else {
			cplan = null;
		}
		
		c.close();
		
		return cplan;
	}	
	
	
	
	public List<Delivery> getAllDeliveries() {
		
		List<Delivery> list = new ArrayList<Delivery>();
		Cursor c = retrieveCursor(DesContract.Delivery.TABLE_NAME, Delivery_PROJECTION);
		
		for(int i = 0; i < c.getCount(); i++) {
			if(c.moveToPosition(i)) {
				Delivery cplan = createDelivery(c);
				list.add(cplan);
			}
		}
		
		c.close();
		
		return list;
	}
	
	
	
	
	
	
	public List<Invoice> getInvoices(String ccode) {
		List<Invoice> list = new ArrayList<Invoice>();
		String sql="SELECT SUBSTR(date,6,2) || " +
				" '/' || SUBSTR(date,9,2) || '/' || " +
				" SUBSTR(date,1,4) AS date," +
				" invoiceno,sono, SUM(price*qty) amount " +
				" FROM deliveries " +
				" WHERE ccode LIKE '" + ccode +"' " +
				" GROUP BY ccode, invoiceno " +
				" ORDER BY date ASC"
				;
		
		Cursor c = db.rawQuery(sql,null);
		
		for(int i = 0; i < c.getCount(); i++) {
			if(c.moveToPosition(i)) {
				Invoice inv = createInvoice(c);
				list.add(inv);
			}
		}
		
		c.close();
		return list;
	}
	
	public List<Delivery> getInvoiceItems(String ccode, String invoiceno) {
		List<Delivery> list = new ArrayList<Delivery>();
		String sql="SELECT A._id,A.itemcode,B.description,A.pckg,A.qty,A.price " +
				" FROM deliveries A " +
				" LEFT JOIN items B " +
				" ON A.itemcode=B.itemcode" +
				" WHERE ccode LIKE '" + ccode +"' " +
						" AND invoiceno LIKE '" + invoiceno +"' " +
				" ORDER BY A.itemcode ASC";
		
		Cursor c = db.rawQuery(sql,null);
		
		for(int i = 0; i < c.getCount(); i++) {
			if(c.moveToPosition(i)) {
				Delivery invItem = createInvoiceItem(c);
				list.add(invItem);
			}
		}
		
		c.close();
		return list;
	}
	
	private Delivery createDelivery(Cursor c) {
		Delivery delivery = new Delivery();
		
		delivery.setId(c.getInt(
				c.getColumnIndex(BaseColumns._ID)));
		delivery.setDate(c.getString(
				c.getColumnIndex(DesContract.Delivery.DATE)));		
		delivery.setCcode(c.getString(
				c.getColumnIndex(DesContract.Delivery.CCODE)));
		delivery.setCustomerName(c.getString(
				c.getColumnIndex(DesContract.Customer.NAME)));		
		delivery.setInvoiceno(c.getString(
				c.getColumnIndex(DesContract.Delivery.INVOICENO)));
		delivery.setItemcode(c.getString(
				c.getColumnIndex(DesContract.Delivery.ITEM_CODE)));
		delivery.setItemDescription(c.getString(
				c.getColumnIndex(DesContract.Item.DESCRIPTION)));		
		delivery.setPckg(c.getString(
				c.getColumnIndex(DesContract.Delivery.PCKG)));			
		delivery.setPrice(c.getDouble(
				c.getColumnIndex(DesContract.Delivery.PRICE)));	
		delivery.setQty(c.getInt(
				c.getColumnIndex(DesContract.Delivery.QTY)));
		delivery.setSono(c.getString(
				c.getColumnIndex(DesContract.Delivery.SONO)));
		delivery.setStatus(c.getInt(
				c.getColumnIndex(DesContract.Delivery.STATUS)));
		return delivery;
	}
	
	
	
	private Invoice createInvoice(Cursor c) {
		Invoice invoice = new Invoice();
		
		invoice.setDate(c.getString(
				c.getColumnIndex(DesContract.Delivery.DATE)));		
		invoice.setInvoiceno(c.getString(
				c.getColumnIndex(DesContract.Delivery.INVOICENO)));
		invoice.setSono(c.getString(
				c.getColumnIndex(DesContract.Delivery.SONO)));
		invoice.setAmount(c.getDouble(
				c.getColumnIndex("amount")));
	
		return invoice;
	}
	
	private Delivery createInvoiceItem(Cursor c) {
		Delivery delivery = new Delivery();
		
		delivery.setId(c.getInt(
				c.getColumnIndex(BaseColumns._ID)));
		delivery.setItemcode(c.getString(
				c.getColumnIndex(DesContract.Delivery.ITEM_CODE)));
		delivery.setItemDescription(c.getString(
				c.getColumnIndex(DesContract.Item.DESCRIPTION)));		
		delivery.setPckg(c.getString(
				c.getColumnIndex(DesContract.Delivery.PCKG)));			
		delivery.setPrice(c.getDouble(
				c.getColumnIndex(DesContract.Delivery.PRICE)));	
		delivery.setQty(c.getInt(
				c.getColumnIndex(DesContract.Delivery.QTY)));
		return delivery;
	}
	
}
