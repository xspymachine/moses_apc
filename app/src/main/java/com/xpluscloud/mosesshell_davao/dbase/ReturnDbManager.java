package com.xpluscloud.mosesshell_davao.dbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.xpluscloud.mosesshell_davao.getset.Pr;
import com.xpluscloud.mosesshell_davao.getset.Return;

import java.util.ArrayList;
import java.util.List;

public class ReturnDbManager extends DbManager {
	
	public static final String[] Return_PROJECTION = new String[] {
		DesContract.Return._ID,
		DesContract.Return.PRCODE,
		DesContract.Return.DATE,
		DesContract.Return.CCODE,
		DesContract.Return.PRNO,
		DesContract.Return.ITEM_CODE,
		DesContract.Return.PCKG,	
		DesContract.Return.PRICE,	
		DesContract.Return.QTY,
		DesContract.Return.STATUS
	};
	
	public ReturnDbManager(Context context) {
		super(context);
	}
	
	

	public long AddReturn(Return p) {
		ContentValues cv = new ContentValues();
		cv.put(DesContract.Return.PRCODE 		, p.getPrcode());
		cv.put(DesContract.Return.DATE 			, p.getDate());
		cv.put(DesContract.Return.CCODE			, p.getCcode());
		cv.put(DesContract.Return.PRNO    		, p.getPrno());
		cv.put(DesContract.Return.ITEM_CODE 	, p.getItemcode());
		cv.put(DesContract.Return.PCKG			, p.getPckg());
		cv.put(DesContract.Return.PRICE			, p.getPrice());	
		cv.put(DesContract.Return.QTY			, p.getQty());
		cv.put(DesContract.Return.STATUS 		, p.getStatus());
		
		//return db.insert(DesContract.Return.TABLE_NAME, null, cv);
		
		return db.insertWithOnConflict(DesContract.Return.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
	}
	
	public void AddReturns(List<Return> list) {
		for(int i = 0; i < list.size(); i++) {
			AddReturn(list.get(i));
		}
	}
	
	public Return getReturn(int rowId) {
		Return cplan;
		Cursor c = retrieveCursor(rowId, DesContract.Return.TABLE_NAME, Return_PROJECTION);
		
		if(c.moveToFirst()) {
			cplan = createReturn(c);
		} else {
			cplan = null;
		}
		
		c.close();
		
		return cplan;
	}	
	
	public int getLastPRno() {
		int lastPRno=0;
		
		int lastid= this.getLastId();
		if(lastid>0) {
			String sql = "SELECT prno FROM returns WHERE _id = "+lastid+" LIMIT 1";
			Cursor c = db.rawQuery(sql,null);
			if (c != null && c.moveToFirst()) {
				lastPRno = c.getInt(0); //The 0 is the column index, we only have 1 column, so the index is 0
			}
		}
		return lastPRno;
	}
	
	public int getLastId() {
		int lastId = 0;
		
		String sql = "SELECT _id FROM returns ORDER BY _id DESC LIMIT 1";
		Cursor c = db.rawQuery(sql,null);
		if (c != null && c.moveToFirst()) {
		    lastId = c.getInt(0); //The 0 is the column index, we only have 1 column, so the index is 0
		}	
		
		return lastId;
	}
	
	public Pr getInfo(String prCode) {
		Pr cs=null;
		String sql = "SELECT prcode,date,ccode,prno,SUM(price*qty) amount,status" +
				" FROM returns " +
				" WHERE prcode = '" + prCode + "'" +
				" GROUP BY prcode ORDER BY date ASC" +
				" LIMIT 1";
		Cursor c = db.rawQuery(sql,null);
		
		if(c.getCount()>0) {
			if(c.moveToFirst()) {
				cs = createPr(c);
			} 
		}
		
		c.close();
		
		return cs;
	}
	
	public List<Return> getList(String prCode) {
		List<Return> list = new ArrayList<Return>();
		
		String sql = "SELECT A._id, " +
					"A.date," +
					"A.prcode," +
					"A.ccode," +
					"A.prno," +
					"A.itemcode," +
					"B.categorycode," +
					"B.description," +
					"A.pckg," +
					"A.qty," +
					"A.price," +
					"A.status " +
				"FROM returns A " +
				"LEFT JOIN items B " +
				"ON A.itemcode=B.itemcode " +
				"WHERE A.prcode = '" + prCode + "'";
		
		Cursor c = db.rawQuery(sql,null);
		
		for(int i = 0; i < c.getCount(); i++) {
			if(c.moveToPosition(i)) {
				Return item = createReturn(c);;
				list.add(item);
			}
		}
		
		c.close();
		
		return list;
	}
	
	public void deleteReturn(String prCode) {
		String where = "prcode LIKE '" + prCode +"' AND status=0";
		db.delete(DesContract.Return.TABLE_NAME, where, null);
		
	}
	
	
	
	public void deleteReturnItem(int id) {
		String where = "_id="+id;
		db.delete(DesContract.Return.TABLE_NAME, where, null);
		
	}
	
	
	public void deleteAllReturn(String cCode) {
		String where = "ccode LIKE '" + cCode +"' ";
		db.delete(DesContract.Return.TABLE_NAME, where, null);
		
	}
	/*
	public List<Return> getAll() {
		
		List<Return> list = new ArrayList<Return>();
		Cursor c = retrieveCursor(DesContract.Return.TABLE_NAME, Return_PROJECTION);
		
		for(int i = 0; i < c.getCount(); i++) {
			if(c.moveToPosition(i)) {
				Return cplan = createReturn(c);
				list.add(cplan);
			}
		}
		
		c.close();
		
		return list;
	}
	*/
	
	public List<Pr> getPrs(String ccode) {
		List<Pr> list = new ArrayList<Pr>();
		String sql="SELECT prcode, ccode," +
				" strftime('%m/%d/%Y %H:%M:%S',date,'unixepoch','localtime')  AS date,prno," +
				" SUM(price*qty) amount,status " +
				" FROM returns " +
				" WHERE ccode LIKE '" + ccode +"' " +
				" GROUP BY ccode, prcode " +
				" ORDER BY date DESC"
				;
		
		Cursor c = db.rawQuery(sql,null);
		
		for(int i = 0; i < c.getCount(); i++) {
			if(c.moveToPosition(i)) {
				Pr pr = createPr(c);
				list.add(pr);
			}
		}
		
		c.close();
		return list;
	}
	
	
	public List<Return> getReturns(String ccode, String prcode) {
		List<Return> list = new ArrayList<Return>();
		String sql="SELECT A._id,A.prcode,A.itemcode,B.description," +
						" A.pckg,A.qty,A.price,A.status " +
				" FROM returns A " +
				" LEFT JOIN items B " +
				" ON A.itemcode=B.itemcode" +
				" WHERE ccode LIKE '" + ccode +"' " +
						" AND prcode LIKE '" + prcode +"' " +
				" ORDER BY A.itemcode ASC";
		
		Cursor c = db.rawQuery(sql,null);
		
		for(int i = 0; i < c.getCount(); i++) {
			if(c.moveToPosition(i)) {
				Return invItem = createReturn(c);
				list.add(invItem);
			}
		}
		
		c.close();
		return list;
	}
	
	public Double getTotal(String prCode) {
		Double total=0.0;
		
		String sql="SELECT SUM(qty*price) " +
				" FROM " +  DesContract.Return.TABLE_NAME + 
				" WHERE " +  DesContract.Return.PRCODE 	+
					" = '" + prCode	+"' "+
				" GROUP BY " +  DesContract.Return.PRCODE 
				;
							
		//Log.w("Query",sql);
		Cursor c = db.rawQuery(sql, null);
		if(c.moveToFirst()) {
			total = c.getDouble(0);
		} 		
		return total;
	}
	
	public void UpdatePckg(Return ro) {// ro => record object
		ContentValues cv = new ContentValues();
		
		cv.put(DesContract.Return.PCKG, ro.getPckg());
		cv.put(DesContract.Return.PRICE, ro.getPrice());
		db.update(DesContract.Return.TABLE_NAME, 
			cv, 
			"_id=?",
			new String[]{ Long.toString(ro.getId())}
		);
	}
	
	public void UpdateQty(Return ro) {// ro => record object
		ContentValues cv = new ContentValues();
		
		cv.put(DesContract.Return.QTY,ro.getQty());
		
		db.update(DesContract.Return.TABLE_NAME, 
			cv, 
			"_id=?",
			new String[]{ Long.toString(ro.getId())}
		);
	}
	
	public void updateStatus(String prCode, Integer status) {
		String sql = "UPDATE " + DesContract.Return.TABLE_NAME
				+ " SET " + DesContract.Return.STATUS + "=" +status
				+ " WHERE prcode='" + prCode +"'" ;

		//Log.w("Update Status query",sql);
		db.execSQL(sql);
	}
	
	private Return createReturn(Cursor c) {
		Return pr = new Return();
		
		pr.setId(c.getInt(
				c.getColumnIndex(BaseColumns._ID)));
		pr.setPrcode(c.getString(
				c.getColumnIndex(DesContract.Return.PRCODE)));	
		pr.setDate(c.getString(
				c.getColumnIndex(DesContract.Return.DATE)));		
		pr.setCcode(c.getString(
				c.getColumnIndex(DesContract.Return.CCODE)));	
		pr.setPrno(c.getInt(
				c.getColumnIndex(DesContract.Return.PRNO)));
		pr.setItemcode(c.getString(
				c.getColumnIndex(DesContract.Return.ITEM_CODE)));
		pr.setItemDescription(c.getString(
				c.getColumnIndex(DesContract.Item.DESCRIPTION)));		
		pr.setPckg(c.getString(
				c.getColumnIndex(DesContract.Return.PCKG)));			
		pr.setPrice(c.getDouble(
				c.getColumnIndex(DesContract.Return.PRICE)));	
		pr.setQty(c.getInt(
				c.getColumnIndex(DesContract.Return.QTY)));
		
		double price = c.getDouble(c.getColumnIndex(DesContract.Return.PRICE));
		int qty = c.getInt(c.getColumnIndex(DesContract.Return.QTY));
		
		pr.setAmount(price*qty);
		
		pr.setStatus(c.getInt(
				c.getColumnIndex(DesContract.Return.STATUS)));
		return pr;
	}
	
	private Pr createPr(Cursor c) {
		Pr pr = new Pr();
		

		pr.setPrcode(c.getString(
				c.getColumnIndex(DesContract.Return.PRCODE)));
		pr.setCcode(c.getString(
				c.getColumnIndex(DesContract.Return.CCODE)));
		pr.setDate(c.getString(
				c.getColumnIndex(DesContract.Return.DATE)));		
		pr.setPrno(c.getInt(
				c.getColumnIndex(DesContract.Return.PRNO)));
		pr.setAmount(c.getDouble(
				c.getColumnIndex("amount")));
		pr.setStatus(c.getInt(
				c.getColumnIndex(DesContract.Return.STATUS)));
		return pr;
	}

	
	
}
