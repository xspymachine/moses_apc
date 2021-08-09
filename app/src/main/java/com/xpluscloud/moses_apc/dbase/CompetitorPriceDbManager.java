package com.xpluscloud.moses_apc.dbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xpluscloud.moses_apc.getset.CompetitorPrice;
import com.xpluscloud.moses_apc.util.DateUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CompetitorPriceDbManager extends DbManager{

	public CompetitorPriceDbManager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public long AddItem(CompetitorPrice p) {
		ContentValues cv = new ContentValues();
		
		cv.put(DesContract.CustItem.COMCODE		, p.getComcode());
		cv.put(DesContract.CustItem.CCODE		, p.getCcode());
		cv.put(DesContract.CustItem.ICODE		, p.getIcode());
		cv.put(DesContract.CustItem.CATEGORYCODE, p.getCatcode());
		cv.put(DesContract.CustItem.DESCRIPTION	, p.getDescription());
		cv.put(DesContract.CustItem.DPRICE		, p.getDtrPrice());
		cv.put(DesContract.CustItem.RPRICE		, p.getRetailPrice());
		cv.put(DesContract.CustItem.QTY			, p.getQty());
		cv.put(DesContract.CustItem.IOH			, p.getIoh());		
		cv.put(DesContract.CustItem.TYPE50IOH	, p.getType50ioh());
		cv.put(DesContract.CustItem.TYPE50SU	, p.getType50());
		cv.put(DesContract.CustItem.STATUS		, p.getStatus());
		cv.put(DesContract.CustItem.DATETIME	, p.getDatetime());
		
		
		//return db.insert(DesContract.Collection.TABLE_NAME, null, cv);
		
		return db.insertWithOnConflict(DesContract.CustItem.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
	}
	
	public void AddItems(List<CompetitorPrice> list) {
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
	
	public List<CompetitorPrice> getList(String ccode) {
		List<CompetitorPrice> list = new ArrayList<CompetitorPrice>();
				
		String where = " where "+DesContract.CustItem.CCODE+" = '"+ccode+"' AND "+
					   DesContract.CustItem.DATETIME+" LIKE '%"+DateUtil.strDate(System.currentTimeMillis())+"%'" +
					   " ORDER BY "+DesContract.CustItem.DESCRIPTION+" ASC";
		String sql = "SELECT *FROM "+DesContract.CustItem.TABLE_NAME+where;
		
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
				
		String where = " where "+DesContract.CustItem.CCODE+" = '"+ccode+"' AND (status = 0) ORDER BY _id DESC";//stats=2 for new added
		String sql = "SELECT *FROM "+DesContract.CustItem.TABLE_NAME+where;
		
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
	
	public void updateStatus(String code, int status){
		String where = DesContract.CustItem.COMCODE+" = '"+code+"'";
		
		int add = 1;
//		int edit = 2;
//		int delete = 3;
		
		ContentValues cv = new ContentValues();
		cv.put(DesContract.CustItem.STATUS, add);
//		if(status == 0) cv.put(DesContract.CustItem.STATUS, add);
//		else if(status == 1 || status == 2) cv.put(DesContract.CustItem.STATUS, edit);
//		else if(status == 3) cv.put(DesContract.CustItem.STATUS, delete);
		db.update(DesContract.CustItem.TABLE_NAME, cv, where, null);
	}
	
	public void updatePrice(String code, String strPrice){
		strPrice = strPrice.equals("") ? "0.00" : strPrice.trim();
		
		DecimalFormat df = new DecimalFormat("#.00");
		df.format(Double.parseDouble(strPrice));
		
//		Log.e("db",df.format(Double.parseDouble(strPrice)));
		if(!strPrice.contains("+")){
//			Double price = Double.parseDouble(strPrice);
			Log.e("db2",strPrice);
			ContentValues cv = new ContentValues();
			cv.put(DesContract.CustItem.DPRICE, strPrice);
			cv.put(DesContract.CustItem.STATUS, 0);
			
			db.update(DesContract.CustItem.TABLE_NAME, cv, DesContract.CustItem.COMCODE+"='"+code+"'", null);
		}
	}
	
	public void updateRPrice(String code, String strPrice){
		strPrice = strPrice.equals("") ? "0.00" : strPrice.trim();
		
		DecimalFormat df = new DecimalFormat("#.00");
		df.format(Double.parseDouble(strPrice));
		
//		Log.e("db",df.format(Double.parseDouble(strPrice)));
		if(!strPrice.contains("+")){
//			Double price = Double.parseDouble(strPrice);
			Log.e("db2",strPrice);
			ContentValues cv = new ContentValues();
			cv.put(DesContract.CustItem.RPRICE, strPrice);
			cv.put(DesContract.CustItem.STATUS, 0);
			
			db.update(DesContract.CustItem.TABLE_NAME, cv, DesContract.CustItem.COMCODE+"='"+code+"'", null);
		}
	}
	
//	public void updateQty(String code, String strQty){
//		strQty = strQty.equals("") ? "0" : strQty.trim();
//		
//		DecimalFormat df = new DecimalFormat("#.00");
//		df.format(Double.parseDouble(strQty));
//		
////		Log.e("db",df.format(Double.parseDouble(strPrice)));
//		if(!strQty.contains("+")){
////			Double price = Double.parseDouble(strPrice);
//			Log.e("db2",strQty);
//			ContentValues cv = new ContentValues();
//			cv.put(DesContract.CustItem.QTY, strQty);
//			cv.put(DesContract.CustItem.STATUS, 0);
//			
//			db.update(DesContract.CustItem.TABLE_NAME, cv, DesContract.CustItem.COMCODE+"='"+code+"'", null);
//		}
//	}
	
	public void updateVol(String code, String strVol){
		strVol = strVol.equals("") ? "0" : strVol.trim();
		
		DecimalFormat df = new DecimalFormat("#.00");
		df.format(Double.parseDouble(strVol));
		
//		Log.e("db",df.format(Double.parseDouble(strPrice)));
		if(!strVol.contains("+")){
//			Double price = Double.parseDouble(strPrice);
			Log.e("db2",strVol);
			ContentValues cv = new ContentValues();
			cv.put(DesContract.CustItem.VOL, strVol);
			cv.put(DesContract.CustItem.STATUS, 0);
			
			db.update(DesContract.CustItem.TABLE_NAME, cv, DesContract.CustItem.COMCODE+"='"+code+"'", null);
		}
	}
	
	public void updateIoh(String code, String strIoh){
		strIoh = strIoh.equals("") ? "0" : strIoh.trim();
		
		DecimalFormat df = new DecimalFormat("#.00");
		df.format(Double.parseDouble(strIoh));
		
//		Log.e("db",df.format(Double.parseDouble(strPrice)));
		if(!strIoh.contains("+")){
//			Double price = Double.parseDouble(strPrice);
			Log.e("db2",strIoh);
			ContentValues cv = new ContentValues();
			cv.put(DesContract.CustItem.IOH, strIoh);
			cv.put(DesContract.CustItem.STATUS, 0);
			
			db.update(DesContract.CustItem.TABLE_NAME, cv, DesContract.CustItem.COMCODE+"='"+code+"'", null);
		}
	}
	
	public void updateRemarks(String code, String remarks){
			ContentValues cv = new ContentValues();
			cv.put(DesContract.CustItem.REMARKS, remarks);
			cv.put(DesContract.CustItem.STATUS, 0);
			
			db.update(DesContract.CustItem.TABLE_NAME, cv, DesContract.CustItem.COMCODE+"='"+code+"'", null);
		
	}
	
	public Boolean searchItem(String cCode, String itemCode){
		Boolean search = true;
		String where = " where "+DesContract.CustItem.CCODE+" = '"+cCode+"' AND "+DesContract.CustItem.ICODE+" = '"+itemCode+"'";
		String sql = "SELECT *FROM "+ DesContract.CustItem.TABLE_NAME+where;
		Cursor c = db.rawQuery(sql, null);
		
		if(c.moveToFirst()) search = false;
		
		return search;		
	}
	
	public void deleteItem(String comCode){
		String where = DesContract.CustItem.COMCODE+"='"+comCode+"'";
		db.delete(DesContract.CustItem.TABLE_NAME, where, null);
	}
	
	private CompetitorPrice setList(Cursor c){
		
		CompetitorPrice cp = new CompetitorPrice();
	 	
		cp.setId(c.getInt(c.getColumnIndex(DesContract.CustItem._ID)));
		cp.setComcode(c.getString(c.getColumnIndex(DesContract.CustItem.COMCODE)));
		cp.setCcode(c.getString(c.getColumnIndex(DesContract.CustItem.CCODE)));
		cp.setIcode(c.getString(c.getColumnIndex(DesContract.CustItem.ICODE)));
		cp.setCatcode(c.getString(c.getColumnIndex(DesContract.CustItem.CATEGORYCODE)));
		cp.setDescription(c.getString(c.getColumnIndex(DesContract.CustItem.DESCRIPTION)));
		cp.setDtrPrice(c.getString(c.getColumnIndex(DesContract.CustItem.DPRICE)));
		cp.setRetailPrice(c.getString(c.getColumnIndex(DesContract.CustItem.RPRICE)));
		cp.setQty(c.getInt(c.getColumnIndex(DesContract.CustItem.QTY)));
		cp.setVol(c.getInt(c.getColumnIndex(DesContract.CustItem.VOL)));
		cp.setIoh(c.getInt(c.getColumnIndex(DesContract.CustItem.IOH)));
		cp.setStatus(c.getInt(c.getColumnIndex(DesContract.CustItem.STATUS)));
		
		cp.setType22(c.getInt(c.getColumnIndex(DesContract.CustItem.TYPE22SU)));
		cp.setType22ioh(c.getInt(c.getColumnIndex(DesContract.CustItem.TYPE22IOH)));
		cp.setType50(c.getInt(c.getColumnIndex(DesContract.CustItem.TYPE50SU)));
		cp.setType50ioh(c.getString(c.getColumnIndex(DesContract.CustItem.TYPE50IOH)));
		cp.setType5(c.getInt(c.getColumnIndex(DesContract.CustItem.TYPE5SU)));
		cp.setType5ioh(c.getInt(c.getColumnIndex(DesContract.CustItem.TYPE5IOH)));
		cp.setType7(c.getInt(c.getColumnIndex(DesContract.CustItem.TYPE7SU)));
		cp.setType7ioh(c.getInt(c.getColumnIndex(DesContract.CustItem.TYPE7IOH)));		

		cp.setRemarks(c.getString(c.getColumnIndex(DesContract.CustItem.REMARKS)));
		cp.setDatetime(c.getString(c.getColumnIndex(DesContract.CustItem.DATETIME)));
		
		return cp;		
	}
	
	public void addAllItems(String ccode, String devId){
		String selectWhere = "(SELECT "+DesContract.CustItem.ICODE+" FROM "+DesContract.CustItem.TABLE_NAME+" where "+
								DesContract.CustItem.CCODE+"='"+ccode+"' AND "+
								DesContract.CustItem.DATETIME+" LIKE '%"+DateUtil.strDate(System.currentTimeMillis())+"%')";
		String where = " where "+DesContract.CustItemList.ITEMCODE+" NOT IN "+selectWhere;
		String sql = "SELECT *FROM "+DesContract.CustItemList.TABLE_NAME+where;
		
		Cursor c = db.rawQuery(sql, null);
		Log.e("sql", sql+"-"+c.getCount());
			for(int i = 0; i < c.getCount(); i++) {
				if(c.moveToPosition(i)) {
					CompetitorPrice cp = new CompetitorPrice();
					
					cp.setCcode(ccode);
					cp.setIcode(c.getString(c.getColumnIndex(DesContract.CustItem.ICODE)));
					cp.setDescription(c.getString(c.getColumnIndex(DesContract.CustItem.DESCRIPTION)));	
					cp.setComcode(get_Code(devId));
					cp.setDatetime(DateUtil.strDateTime(System.currentTimeMillis()));
					
					AddItem(cp);
				}
		}
		c.close();
	}
	
	public void updateTypes(String code, String strValue, int type){
		strValue = strValue.equals("") ? "0.00" : strValue.trim();
		
		DecimalFormat df = new DecimalFormat("#.00");
		df.format(Double.parseDouble(strValue));
		
		if(!strValue.contains("+")){
			ContentValues cv = new ContentValues();
			cv.put(DesContract.CustItem.STATUS, 0);		
			    Log.e("value",strValue);
			switch(type){
			case 1: cv.put(DesContract.CustItem.TYPE22IOH, strValue);			
					break;
			case 2: cv.put(DesContract.CustItem.TYPE50IOH, strValue);		
					break;
			case 3: cv.put(DesContract.CustItem.TYPE22SU, strValue);		
					break;
			case 4: cv.put(DesContract.CustItem.TYPE50SU, strValue);		
					break;
			case 5: cv.put(DesContract.CustItem.TYPE5IOH, strValue);		
					break;
			case 6: cv.put(DesContract.CustItem.TYPE7IOH, strValue);		
					break;
			case 7: cv.put(DesContract.CustItem.TYPE5SU, strValue);		
					break;
			case 8: cv.put(DesContract.CustItem.TYPE7SU, strValue);		
					break;
			case 9: cv.put(DesContract.CustItem.DPRICE, strValue);	/////	
					break;
			case 10: cv.put(DesContract.CustItem.RPRICE, strValue);		
					break;
			case 11: cv.put(DesContract.CustItem.VOL, strValue);		
					break;
			case 12: cv.put(DesContract.CustItem.IOH, strValue);		
					break;
			}
			
			db.update(DesContract.CustItem.TABLE_NAME, cv, DesContract.CustItem.COMCODE+"='"+code+"'", null);
			
		}
	}
	
	public String getRemarks(String ccode){
		String remarks = "";
		String sql = "SELECT remarks FROM citem_price WHERE ccode = '"+ccode+"' AND datetime LIKE '%"+
					  DateUtil.strDate(System.currentTimeMillis())+"%'  LIMIT 1";
		Cursor c = db.rawQuery(sql, null);
		
		if(c.moveToFirst()) remarks = c.getString(0);
		
		return remarks;
	}
	
	public int getTotal(String ccode, String colName){
		String where = " WHERE ccode='"+ccode+"' AND datetime LIKE '%"+(DateUtil.strDateMY(System.currentTimeMillis()))+"%' ";
		String sql = "SELECT SUM("+colName+") FROM citem_price "+where;
		int total = 0;
		
		Cursor c = db.rawQuery(sql, null);
		
		if(c.moveToFirst()) total = c.getInt(0);
		Log.e("sql",sql);
		c.close();
		return total;
	}
	
	public Boolean getStatus2(String ccode){
		Boolean status = false;
		String sql = "SELECT *FROM citem_price WHERE ccode='"+ccode+"' "
					+ " AND status = 1"
					+ " AND "+DesContract.CustItem.DATETIME+" LIKE '%"+(DateUtil.strDateMY(System.currentTimeMillis()))+"%'";
		Cursor c = db.rawQuery(sql, null);
		
		Log.e("count",""+c.getCount());
		if(c.getCount() > 0) status = true;

		c.close();
		return status;
	}

}
