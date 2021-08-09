package com.xpluscloud.moses_apc.dbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.xpluscloud.moses_apc.getset.Collection;

import java.util.ArrayList;
import java.util.List;

public class CollectionDbManager extends DbManager {
	
	public static final String[] Collection_PROJECTION = new String[] {
		DesContract.Collection._ID,
		DesContract.Collection.DATE,
		DesContract.Collection.CCODE,
		DesContract.Collection.INVOICENO,
		DesContract.Collection.CASH_AMOUNT,
		DesContract.Collection.CHECK_AMOUNT,
		DesContract.Collection.CHECKNO,
		DesContract.Collection.CM_AMOUNT,
		DesContract.Collection.CMNO,
		DesContract.Collection.DEPOSIT_AMOUNT,
		DesContract.Collection.BANKNAME,
		DesContract.Collection.BANKBRANCH,
		DesContract.Collection.DEPOSIT_TRNO,
		DesContract.Collection.ORNO,
		DesContract.Collection.NC_REASON,
		DesContract.Collection.REMARKS,
		DesContract.Collection.STATUS
	};
	
	public CollectionDbManager(Context context) {
		super(context);
	}
	
	

	public long AddCollection(Collection p) {
		ContentValues cv = new ContentValues();
		cv.put(DesContract.Collection.DATE 			, p.getDate());
		cv.put(DesContract.Collection.CCODE			, p.getCcode());
		cv.put(DesContract.Collection.INVOICENO    	, p.getInvoiceno());
		cv.put(DesContract.Collection.CASH_AMOUNT	, p.getCashamount());		
		cv.put(DesContract.Collection.CHECK_AMOUNT	, p.getCheckamount());
		cv.put(DesContract.Collection.CHECKNO		, p.getCheckno());
		cv.put(DesContract.Collection.CM_AMOUNT		, p.getCmamount());
		cv.put(DesContract.Collection.CMNO			, p.getCmno());
		cv.put(DesContract.Collection.DEPOSIT_AMOUNT, p.getAmount_dep());
		cv.put(DesContract.Collection.BANKNAME		, p.getBankname());
		cv.put(DesContract.Collection.BANKBRANCH	, p.getBankbranch());
		cv.put(DesContract.Collection.DEPOSIT_TRNO	, p.getDeptrno());
		cv.put(DesContract.Collection.ORNO			, p.getOrno());
		cv.put(DesContract.Collection.NC_REASON		, p.getNcreason());
		cv.put(DesContract.Collection.REMARKS		, p.getRemarks());
		cv.put(DesContract.Collection.STATUS 			, p.getStatus());
		
		//return db.insert(DesContract.Collection.TABLE_NAME, null, cv);
		
		return db.insertWithOnConflict(DesContract.Collection.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
	}
	
	public void AddCollections(List<Collection> list) {
		for(int i = 0; i < list.size(); i++) {
			AddCollection(list.get(i));
		}
	}
	
	public Collection getCollection(int rowId) {
		Collection cplan;
		
		Cursor c = retrieveCursor(rowId, DesContract.Collection.TABLE_NAME, Collection_PROJECTION);
		
		if(c.moveToFirst()) {
			cplan = createCollection(c);
		} else {
			cplan = null;
		}
		
		c.close();
		
		return cplan;
	}	
	
	
	
	public List<Collection> getAllCollections() {
		
		List<Collection> list = new ArrayList<Collection>();
		Cursor c = retrieveCursor(DesContract.Collection.TABLE_NAME, Collection_PROJECTION);
		
		for(int i = 0; i < c.getCount(); i++) {
			if(c.moveToPosition(i)) {
				Collection cplan = createCollection(c);
				list.add(cplan);
			}
		}
		
		c.close();
		
		return list;
	}
	
	public List<Collection> getAllCollections(String ccode) {
		
		List<Collection> list = new ArrayList<Collection>();
		String sql="SELECT _id, SUBSTR(date,6,2) || " +
				" '/' || SUBSTR(date,9,2) || '/' || " +
				" SUBSTR(date,1,4) AS date," +
				" ccode,invoiceno,total,status "+
				" FROM collections " +
				" WHERE ccode LIKE '" + ccode +"'";
		Cursor c = db.rawQuery(sql,null);
		
		for(int i = 0; i < c.getCount(); i++) {
			if(c.moveToPosition(i)) {
				Collection cplan = createCollection(c);
				list.add(cplan);
			}
		}
		
		c.close();
		
		return list;
	}
	
	
	private Collection createCollection(Cursor c) {
		Collection Collection = new Collection();
		
		Collection.setId(c.getInt(
				c.getColumnIndex(BaseColumns._ID)));
		Collection.setDate(c.getString(
				c.getColumnIndex(DesContract.Collection.DATE)));		
		Collection.setCcode(c.getString(
				c.getColumnIndex(DesContract.Collection.CCODE)));
		Collection.setInvoiceno(c.getString(
				c.getColumnIndex(DesContract.Collection.INVOICENO)));
		
		Collection.setCashamount(c.getDouble(
				c.getColumnIndex(DesContract.Collection.CASH_AMOUNT)));
		
		Collection.setCheckamount(c.getDouble(
				c.getColumnIndex(DesContract.Collection.CHECK_AMOUNT)));
		Collection.setCheckno(c.getString(
				c.getColumnIndex(DesContract.Collection.CHECKNO)));
		
		Collection.setCmamount(c.getDouble(
				c.getColumnIndex(DesContract.Collection.CM_AMOUNT)));
		Collection.setCmno(c.getString(
				c.getColumnIndex(DesContract.Collection.CMNO)));
		
		Collection.setAmount_dep(c.getDouble(
				c.getColumnIndex(DesContract.Collection.DEPOSIT_AMOUNT)));
		Collection.setBankname(c.getString(
				c.getColumnIndex(DesContract.Collection.BANKNAME)));
		Collection.setBankbranch(c.getString(
				c.getColumnIndex(DesContract.Collection.BANKBRANCH)));
		Collection.setDeptrno(c.getString(
				c.getColumnIndex(DesContract.Collection.DEPOSIT_TRNO)));
		Collection.setOrno(c.getString(
				c.getColumnIndex(DesContract.Collection.ORNO)));
		Collection.setNcreason(c.getInt(
				c.getColumnIndex(DesContract.Collection.NC_REASON)));
		Collection.setRemarks(c.getString(
				c.getColumnIndex(DesContract.Collection.REMARKS)));		
		Collection.setStatus(c.getInt(
				c.getColumnIndex(DesContract.Collection.STATUS)));
		return Collection;
	}
	
	
	
	
}
