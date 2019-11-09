package com.xpluscloud.mosesshell_davao.dbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xpluscloud.mosesshell_davao.getset.Truck;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TruckDbManager extends DbManager{

	public TruckDbManager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public long AddItem(Truck t) {
		ContentValues cv = new ContentValues();
		
		cv.put(DesContract.CusTrucksData.CCODE		, t.getCcode());
		cv.put(DesContract.CusTrucksData.TRUCKID	, t.getTruckid());
		cv.put(DesContract.CusTrucksData.TYPE		, t.getType());
		cv.put(DesContract.CusTrucksData.QUANTITY	, t.getQuantity());
		cv.put(DesContract.CusTrucksData.STATUS		, t.getStatus());
				
		return db.insertWithOnConflict(DesContract.CusTrucksData.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
	}
	
	public void AddItems(List<Truck> list) {
		for(int i = 0; i < list.size(); i++) {
			AddItem(list.get(i));
		}
	}
	
	public String get_Code(String devId){
		int lastId = 0;
		
		String sql = "SELECT _id FROM "+DesContract.CustItem.TABLE_NAME+" ORDER BY _id DESC LIMIT 1";
		Cursor c = db.rawQuery(sql,null);
		if (c != null && c.moveToFirst()) {
		    lastId = c.getInt(0); //The 0 is the column index, we only have 1 column, so the index is 0
		}	
		
		String strCode = ("00000" + (lastId+1)).substring(String.valueOf(lastId).length());
		String strRandom = randomText(10);
		strCode = devId.substring(devId.length() - 5) + strRandom + strCode ;
		
		return strCode;
	}
	

	String randomText(int len ) 	{
		 String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		 Random rnd = new Random();
		StringBuilder sb = new StringBuilder( len );
		for( int i = 0; i < len; i++ ) 
			sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
		return sb.toString();
	}
	
	public List<Truck> getList(String ccode) {
		List<Truck> list = new ArrayList<Truck>();
						
		String sql = "SELECT tr.truckid,tl.name,tl.capacity,tr.type,tr.quantity,tl.status FROM trucks tr " +
					 "LEFT JOIN trucks_list tl ON(tr.truckid = tl.truckid) " +
					 "WHERE tr.ccode = '"+ccode+"' ORDER BY tr.truckid ASC";
		
		Cursor c = db.rawQuery(sql, null);
		Log.e("sql", sql);
		for(int i=0; i<c.getCount(); i++){
			if(c.moveToPosition(i)) {
				Truck item = setList(c);
				list.add(item);
			}
		}
		
		c.close();
		return list;
	}
	
	public List<Truck> getPending(String ccode) {
		List<Truck> list = new ArrayList<Truck>();
		
		String sql = "SELECT tr.truckid,tl.name,tl.capacity,tr.type,tr.quantity,tl.status FROM trucks tr " +
				 "LEFT JOIN trucks_list tl ON(tr.truckid = tl.truckid) " +
				 "WHERE tr.status = 1 AND tr.ccode = '"+ccode+"' ORDER BY tr.truckid ASC";
		
		Cursor c = db.rawQuery(sql, null);
		
		for(int i=0; i<c.getCount(); i++){
			if(c.moveToPosition(i)) {
				Truck item = setList(c);
				list.add(item);
			}
		}
		
		c.close();
		return list;
	}		
	
	public void updateType(String ccode, int truckid, String type){
		
		ContentValues cv = new ContentValues();
		cv.put(DesContract.CusTrucksData.TYPE	, type);
		cv.put(DesContract.CusTrucksData.STATUS	, 1);
		
		db.update(DesContract.CusTrucksData.TABLE_NAME, cv, "ccode='"+ccode+"' AND truckid="+truckid, null);
		
	}
	
	public void updateQty(String ccode, int truckid, String qty){
		int quantity=0;
		if(qty.matches("[0-9]+")) quantity = Integer.parseInt(qty);
		
		ContentValues cv = new ContentValues();
		cv.put(DesContract.CusTrucksData.QUANTITY  , quantity);
		cv.put(DesContract.CusTrucksData.STATUS	   , 1);
		
		db.update(DesContract.CusTrucksData.TABLE_NAME, cv, "ccode='"+ccode+"' AND truckid="+truckid, null);
		
	}
	
	public void updateStatus(String ccode){
		
		ContentValues cv = new ContentValues();
		cv.put(DesContract.CusTrucksData.STATUS	, 0);
		
		db.update(DesContract.CusTrucksData.TABLE_NAME, cv, "ccode='"+ccode+"'", null);
		
	}
	
	private Truck setList(Cursor c){
		
		Truck tr = new Truck();
	 	
		tr.setTruckid(c.getInt(c.getColumnIndex(DesContract.CusTrucksData.TRUCKID)));
		tr.setName(c.getString(c.getColumnIndex(DesContract.CusTrucksList.TRUCK_NAME)));
		tr.setCapacity(c.getString(c.getColumnIndex(DesContract.CusTrucksList.CAPACITY)));
		tr.setType(c.getString(c.getColumnIndex(DesContract.CusTrucksData.TYPE)));
		tr.setQuantity(c.getInt(c.getColumnIndex(DesContract.CusTrucksData.QUANTITY)));
		tr.setStatus(c.getInt(c.getColumnIndex(DesContract.CusTrucksList.STATUS)));
		
		return tr;		
	}
	
	public void addAllItems(String ccode, String devId){
		String selectWhere = "(SELECT "+DesContract.CusTrucksData.TRUCKID+" FROM "+DesContract.CusTrucksData.TABLE_NAME+" where "+DesContract.CusTrucksData.CCODE+"='"+ccode+"')";
		String where = " where "+DesContract.CusTrucksList.TRUCKID+" NOT IN "+selectWhere;
		String sql = "SELECT *FROM "+DesContract.CusTrucksList.TABLE_NAME+where;
		Log.e("sql", sql);
		Cursor c = db.rawQuery(sql, null);
		
			for(int i = 0; i < c.getCount(); i++) {
				if(c.moveToPosition(i)) {
					Truck tr = new Truck();
										
					tr.setCcode(ccode);
					tr.setTruckid(c.getInt(c.getColumnIndex(DesContract.CusTrucksList.TRUCKID)));
					tr.setStatus(0);
					
					AddItem(tr);
				}
		}
		c.close();
	}

}
