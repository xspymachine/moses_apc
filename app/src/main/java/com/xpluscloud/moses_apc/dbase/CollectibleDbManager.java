package com.xpluscloud.moses_apc.dbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.xpluscloud.moses_apc.getset.Collectible;

import java.util.ArrayList;
import java.util.List;

public class CollectibleDbManager extends DbManager {
	
	public static final String[] Collectible_PROJECTION = new String[] {
		DesContract.Collectible._ID,
		DesContract.Collectible.DATE,
		DesContract.Collectible.CCODE,
		DesContract.Collectible.INVOICENO,
		DesContract.Collectible.AMOUNT,
		
		DesContract.Collectible.STATUS
	};
	
	public CollectibleDbManager(Context context) {
		super(context);
	}
	
	

	public long AddCollectible(Collectible p) {
		ContentValues cv = new ContentValues();
		cv.put(DesContract.Collectible.DATE 			, p.getDate());
		cv.put(DesContract.Collectible.CCODE			, p.getCcode());
		cv.put(DesContract.Collectible.INVOICENO    	, p.getInvoiceno());
		cv.put(DesContract.Collectible.AMOUNT			, p.getAmount());
		cv.put(DesContract.Collectible.STATUS 			, p.getStatus());
		
		//return db.insert(DesContract.Collectible.TABLE_NAME, null, cv);
		
		return db.insertWithOnConflict(DesContract.Collectible.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
	}
	
	public void AddCollectibles(List<Collectible> list) {
		for(int i = 0; i < list.size(); i++) {
			AddCollectible(list.get(i));
		}
	}
	
	public void updateStatus(String invoiceNo, Integer status) {
		String sql="UPDATE collectibles SET " +
				" status=" + status + 
				" WHERE invoiceno LIKE'" + invoiceNo +"'";
		
		db.execSQL(sql);
	}
	
	
	public Collectible getCollectible(int rowId) {
		Collectible cplan;
		
		Cursor c = retrieveCursor(rowId, DesContract.Collectible.TABLE_NAME, Collectible_PROJECTION);
		
		if(c.moveToFirst()) {
			cplan = createCollectible(c);
		} else {
			cplan = null;
		}
		
		c.close();
		
		return cplan;
	}	
	
	
	
	public List<Collectible> getAllCollectibles() {
		
		List<Collectible> list = new ArrayList<Collectible>();
		Cursor c = retrieveCursor(DesContract.Collectible.TABLE_NAME, Collectible_PROJECTION);
		
		for(int i = 0; i < c.getCount(); i++) {
			if(c.moveToPosition(i)) {
				Collectible cplan = createCollectible(c);
				list.add(cplan);
			}
		}
		
		c.close();
		
		return list;
	}
	
	public List<Collectible> getAllCollectibles(String ccode) {
		
		List<Collectible> list = new ArrayList<Collectible>();
		String sql="SELECT _id, SUBSTR(date,6,2) || " +
				" '/' || SUBSTR(date,9,2) || '/' || " +
				" SUBSTR(date,1,4) AS date," +
				" ccode,invoiceno,amount,status "+
				" FROM Collectibles " +
				" WHERE ccode LIKE '" + ccode +"'";
		Cursor c = db.rawQuery(sql,null);
		
		for(int i = 0; i < c.getCount(); i++) {
			if(c.moveToPosition(i)) {
				Collectible cplan = createCollectible(c);
				list.add(cplan);
			}
		}
		
		c.close();
		
		return list;
	}
	
	
	private Collectible createCollectible(Cursor c) {
		Collectible Collectible = new Collectible();
		
		Collectible.setId(c.getInt(
				c.getColumnIndex(BaseColumns._ID)));
		Collectible.setDate(c.getString(
				c.getColumnIndex(DesContract.Collectible.DATE)));		
		Collectible.setCcode(c.getString(
				c.getColumnIndex(DesContract.Collectible.CCODE)));
		/*Collectible.setCustomerName(c.getString(
				c.getColumnIndex(DesContract.Customer.NAME)));	*/	
		Collectible.setInvoiceno(c.getString(
				c.getColumnIndex(DesContract.Collectible.INVOICENO)));
		Collectible.setAmount(c.getDouble(
				c.getColumnIndex(DesContract.Collectible.AMOUNT)));
		Collectible.setStatus(c.getInt(
				c.getColumnIndex(DesContract.Collectible.STATUS)));
		return Collectible;
	}
	
	
	
}
