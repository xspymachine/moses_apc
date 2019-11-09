package com.xpluscloud.mosesshell_davao.dbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.xpluscloud.mosesshell_davao.getset.MyList;
import com.xpluscloud.mosesshell_davao.getset.Signature;

import java.util.ArrayList;
import java.util.List;

public class SignatureDbManager extends DbManager {
	
	public static final String[] Signature_PROJECTION = new String[] {
		DesContract.Signature._ID,
		DesContract.Signature.DATETIME,
		DesContract.Signature.CCODE,
		DesContract.Signature.FILENAME,		
		DesContract.Signature.STATUS
	};
	
	public SignatureDbManager(Context context) {
		super(context);
	}
	
	public long Add(Signature c) {
		ContentValues cv = new ContentValues();
		cv.put(DesContract.Signature.DATETIME	, c.getDatetime());
		cv.put(DesContract.Signature.CCODE     	, c.getCustomerCode());
		cv.put(DesContract.Signature.FILENAME   , c.getFilename());		
		cv.put(DesContract.Signature.STATUS 	, c.getStatus());
		
		Long id = db.insert(DesContract.Signature.TABLE_NAME, null, cv);
		
		//Delete Old data
		delete_old();		
		
		return id;
	}
	
	
	public Signature getRow(int rowId) {
		Signature cust;
		Cursor c = retrieveCursor(rowId, DesContract.Signature.TABLE_NAME, Signature_PROJECTION);
		
		if(c.moveToFirst()) {
			cust = createSignature(c);
		} else {
			cust = null;
		}
		
		c.close();
		
		return cust;
	}
	
	public void delete(String dateBefore) {
		 db.delete(DesContract.Signature.TABLE_NAME, "datetime < ?", new String[] {dateBefore});
	}
	
	public List<MyList> getAll() {
		List<MyList> list = new ArrayList<MyList>();
		
		String sql = "SELECT t1._id AS id," +
				"strftime('%m/%d/%Y %H:%M:%S',t1.datetime,'unixepoch','localtime')  AS dtime," +				
				"t1.ccode AS cuscode," + 
				"t2.name AS cusname," +	
				"t2.address," +	
				"t1.filename" + 
				" FROM signatures AS t1" +
				" LEFT JOIN customers AS t2" +
				" ON t1.ccode=t2.ccode" +
				" ORDER BY  t1._id DESC";
		
		Cursor c = db.rawQuery(sql,null);
		
		if (c != null && c.moveToFirst()) {
			for(int i = 0; i < c.getCount(); i++) {
				if(c.moveToPosition(i)) {
					MyList item = createMyList(c);
					list.add(item);
				}
			}
		}
		c.close();		
		return list;
	}
	
	public void delete_old() {
		long past = System.currentTimeMillis()-1000*60*60*24*7l; //24hours ago.
		String sql = "DELETE FROM signatures WHERE datetime < '"+ past +"' ";
		db.rawQuery(sql,null);
	}
	
	
	public int getLastId() {
		int lastId = 0;
		
		String sql = "SELECT _id FROM signature ORDER BY _id DESC LIMIT 1";
		Cursor c = db.rawQuery(sql,null);
		if (c != null && c.moveToFirst()) {
		    lastId = c.getInt(0); //The 0 is the column index, we only have 1 column, so the index is 0
		}	
		
		return lastId;
	}

	public Signature getLastSigTransaction(String ccode){
		Signature sig;

		String OrderBy = "_id DESC";
		String Limit = "1";
		String where = "ccode = '"+ccode+"' AND strftime('%Y-%m-%d',s."+DesContract.Signature.DATETIME+",'unixepoch') = strftime('%Y-%m-%d', 'now')";
		String sql =  " SELECT *from "+DesContract.Signature.TABLE_NAME +" s "
				+" LEFT JOIN "+DesContract.Customer.TABLE_NAME+" c "+" USING ("+DesContract.Signature.CCODE+")"
				+" WHERE "+where;

		Cursor c = db.rawQuery(sql, null);
		if(c.moveToFirst()) {
			sig = createSignature(c);
		} else {
			sig = null;
		}

		c.close();

		return sig;
	}
	
	
	public void updateStatus(Integer id, Integer status) {
		String sql="UPDATE signature SET " +
				" status=" + status + 
				", priority=priority + 1 "  + 
				" WHERE _id=" + id;
		
		db.execSQL(sql);
	}
	
	
	private Signature createSignature(Cursor c) {
		Signature Signature = new Signature();		
		Signature.setId(c.getInt(
				c.getColumnIndex(BaseColumns._ID)));
		Signature.setDatetime(c.getString(
				c.getColumnIndex(DesContract.Signature.DATETIME)));
		Signature.setCustomerCode(c.getString(
				c.getColumnIndex(DesContract.Signature.CCODE)));
		Signature.setFilename(c.getString(
				c.getColumnIndex(DesContract.Signature.FILENAME)));
		Signature.setStatus(c.getInt(
				c.getColumnIndex(DesContract.Signature.STATUS)));
		return Signature;
	}
	
	private MyList createMyList(Cursor c) {
		MyList list = new MyList();
		
		list.setId(c.getInt(0));
		list.setDateTime(c.getString(1));
		list.setCustomerCode(c.getString(2));
		list.setCustomerName(c.getString(3));
		list.setAddress(c.getString(4));
		list.setTransaction(c.getString(5));		
		return list;
	
	}
		
	
}
