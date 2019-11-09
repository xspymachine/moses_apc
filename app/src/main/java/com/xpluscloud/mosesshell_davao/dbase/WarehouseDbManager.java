package com.xpluscloud.mosesshell_davao.dbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xpluscloud.mosesshell_davao.getset.CompetitorPrice;

import java.util.ArrayList;
import java.util.List;

public class WarehouseDbManager extends DbManager{

	public WarehouseDbManager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public long AddItem(CompetitorPrice cmp) {
		ContentValues cv = new ContentValues();
		
		cv.put(DesContract.CusWarehouseItems.CCODE		, cmp.getCcode());
		cv.put(DesContract.CusWarehouseItems.ITEMCODE	, cmp.getComcode());
		cv.put(DesContract.CusWarehouseItems.QUANTITY	, cmp.getQty());
		cv.put(DesContract.CusWarehouseItems.STATUS		, cmp.getStatus());
				
		return db.insertWithOnConflict(DesContract.CusWarehouseItems.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
	}
	
	public void AddItems(List<CompetitorPrice> list) {
		for(int i = 0; i < list.size(); i++) {
			AddItem(list.get(i));
		}
	}
	
	public List<CompetitorPrice> getList(String ccode) {
		List<CompetitorPrice> list = new ArrayList<CompetitorPrice>();
				
		String sql = "SELECT cw.itemcode,ci.description,cw.quantity,cw.status FROM customer_whitems cw " +
					 "LEFT JOIN citem ci ON(cw.itemcode = ci.itemcode) " +
					 "WHERE cw.ccode = '"+ccode+"' ORDER BY cw._id ASC";
		
		Cursor c = db.rawQuery(sql, null);
		Log.e("sql", sql);
		for(int i=0; i<c.getCount(); i++){
			if(c.moveToPosition(i)) {
				CompetitorPrice item = setList(c);
				list.add(item);
			}
		}
		
		c.close();
		return list;
	}
	
	public List<CompetitorPrice> getPending(String ccode) {
		List<CompetitorPrice> list = new ArrayList<CompetitorPrice>();
		
		String sql = "SELECT cw.itemcode,ci.description,cw.quantity,cw.status FROM customer_whitems cw " +
				 "LEFT JOIN citem ci ON(cw.itemcode = ci.itemcode) " +
				 "WHERE cw.status = 1 AND cw.ccode = '"+ccode+"' ORDER BY cw._id ASC";
		
		Cursor c = db.rawQuery(sql, null);
		
		for(int i=0; i<c.getCount(); i++){
			if(c.moveToPosition(i)) {
				CompetitorPrice item = setList(c);
				list.add(item);
			}
		}
		
		c.close();
		return list;
	}		
		
	public void updateQty(String ccode, String itemcode, String qty){
		int quantity=0;
		if(qty.matches("[0-9]+")) quantity = Integer.parseInt(qty);
		
		ContentValues cv = new ContentValues();
		cv.put(DesContract.CusWarehouseItems.QUANTITY  , quantity);
		cv.put(DesContract.CusWarehouseItems.STATUS	   , 1);
		
		db.update(DesContract.CusWarehouseItems.TABLE_NAME, cv, "ccode='"+ccode+"' AND itemcode='"+itemcode+"'", null);
		
	}
	
	public void updateStatus(String ccode){
		
		ContentValues cv = new ContentValues();
		cv.put(DesContract.CusWarehouseItems.STATUS	, 0);
		
		db.update(DesContract.CusWarehouseItems.TABLE_NAME, cv, "ccode='"+ccode+"'", null);
		
	}
	
	private CompetitorPrice setList(Cursor c){
		
		CompetitorPrice cmp = new CompetitorPrice();
	 	
		cmp.setComcode(c.getString(c.getColumnIndex(DesContract.CusWarehouseItems.ITEMCODE)));
		cmp.setDescription(c.getString(c.getColumnIndex(DesContract.CustItemList.DESCRIPTION)));
		cmp.setQty(c.getInt(c.getColumnIndex(DesContract.CusWarehouseItems.QUANTITY)));
		cmp.setStatus(c.getInt(c.getColumnIndex(DesContract.CusWarehouseItems.STATUS)));
		
		return cmp;		
	}
	
	public void addAllItems(String ccode, String devId){
		
		String sql = "SELECT *  FROM citem " +
					 "WHERE itemcode NOT IN(SELECT itemcode FROM customer_whitems WHERE ccode = '"+ccode+"') " +
					 "AND (description LIKE '%lafarge%' " +
					 "OR description LIKE '%cemex%' OR description LIKE '%holcim%' " +
					 "OR description LIKE '%eagle%' OR description LIKE '%northern%' " +
					 "OR description LIKE '%grand%' OR description LIKE '%mayon%' " +
					 "OR description LIKE '%mabuhay%')";
		
		
		Log.e("sql", sql);
		Cursor c = db.rawQuery(sql, null);
		
			for(int i = 0; i < c.getCount(); i++) {
				if(c.moveToPosition(i)) {
					CompetitorPrice cmp = new CompetitorPrice();
										
					cmp.setCcode(ccode);
					cmp.setComcode(c.getString(c.getColumnIndex(DesContract.CustItemList.ITEMCODE)));
					cmp.setStatus(0);
					
					AddItem(cmp);
				}
		}
		c.close();
	}

}
