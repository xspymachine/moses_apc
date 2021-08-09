package com.xpluscloud.moses_apc.dbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.text.format.Time;
import android.util.Log;

import com.xpluscloud.moses_apc.getset.CallSheetItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CallSheetItemDbManager extends DbManager {
	
	String TAG="CallSheetItemDbManager";
	
	public static final String[] CallSheetItem_PROJECTION = new String[] {
		DesContract.CallSheetItem._ID,
		DesContract.CallSheetItem.CSCODE,
		DesContract.Item.CATEGORY_CODE,
		DesContract.CallSheetItem.ITEM_CODE,
		DesContract.Item.DESCRIPTION,
		DesContract.CallSheetItem.PCKG,
		DesContract.CallSheetItem.PAST_INVT,
		DesContract.CallSheetItem.DELIVERY,
		DesContract.CallSheetItem.PRESENT_INVT,
		DesContract.CallSheetItem.OFFTAKE,
		DesContract.CallSheetItem.ICO,
		DesContract.CallSheetItem.SUGGESTED,
		DesContract.CallSheetItem.ORDER_QTY,
		DesContract.CallSheetItem.PRICE,
		DesContract.CallSheetItem.STATUS
		
	};
	
	public CallSheetItemDbManager(Context context) {
		super(context);
	}
	
	public long AddCallSheetItem(CallSheetItem ro) {// ro => record object
		ContentValues cv = new ContentValues();
		
		if(this.isDuplicate(ro.getCscode(), ro.getItemcode())) return 0;
		
		cv.put(DesContract.CallSheetItem.CSCODE			,ro.getCscode());
		cv.put(DesContract.CallSheetItem.ITEM_CODE		,ro.getItemcode());
		cv.put(DesContract.CallSheetItem.PCKG			,ro.getPckg());
		cv.put(DesContract.CallSheetItem.PAST_INVT		,ro.getPastinvt());
		cv.put(DesContract.CallSheetItem.DELIVERY		,ro.getDelivery());
		cv.put(DesContract.CallSheetItem.PRESENT_INVT	,ro.getPresentinvt());
		cv.put(DesContract.CallSheetItem.OFFTAKE		,ro.getOfftake());
		cv.put(DesContract.CallSheetItem.ICO			,ro.getIco());
		cv.put(DesContract.CallSheetItem.SUGGESTED		,ro.getSuggested());
		cv.put(DesContract.CallSheetItem.ORDER_QTY		,ro.getOrder());
		cv.put(DesContract.CallSheetItem.PRICE			,ro.getPrice());
		cv.put(DesContract.CallSheetItem.STATUS			,ro.getStatus());
		
		if (ro.getId() > 0) cv.put(BaseColumns._ID, ro.getId());
		
		return db.insert(
				DesContract.CallSheetItem.TABLE_NAME, 
				null, 
				cv
			);
	
	}
	
	private Boolean isDuplicate(String csCode, String itemCode) {
		Boolean exist=false;
		
		String sql = "SELECT COUNT(_id) " +
				"FROM " + DesContract.CallSheetItem.TABLE_NAME +
				" WHERE " + DesContract.CallSheetItem.CSCODE +" LIKE '" +csCode +"' " +
						" AND " + DesContract.CallSheetItem.ITEM_CODE +" LIKE '" +itemCode +"' " +
				" LIMIT 1" ;
		Cursor c = db.rawQuery(sql,null);
		
		
		
		if (c.getCount()>0 && c.moveToFirst()) {
			//Log.d(TAG+":isDuplicate","Count="+c.getInt(0));
		    if (c.getInt(0)>0) exist = true; //The 0 is the column index, we only have 1 column, so the index is 0
		}
		
		//if (c.getCount()>0) exist = true;
		c.close();
		
		return exist;
	}
	
	/*
	public void copyPastOrder(String cCode, String csCode) {
		//get Last Callsheet of customer
		String lastCsCode ="";
		
		String sql = "SELECT cscode " +
				" FROM callsheet WHERE ccode = '" + cCode + "' " +
					"AND csCode != '" + csCode +"' " +
				 "ORDER BY date DESC " +
				 "LIMIT 1";
		
		Cursor c = db.rawQuery(sql,null);
		if (c != null && c.moveToFirst()) {
			lastCsCode = c.getString(0); 
		}	
		
		sql ="INSERT INTO callsheetitems(date,cscode,itemcode,pckg,price) " +
				"(SELECT date,'" + csCode + "', itemcode,pckg,price";
	}
	*/
	
	public void UpdatePckg(CallSheetItem ro) {// ro => record object
		ContentValues cv = new ContentValues();
		
		cv.put(DesContract.CallSheetItem.PCKG			,ro.getPckg());
		cv.put(DesContract.CallSheetItem.PAST_INVT		,ro.getPastinvt());
		cv.put(DesContract.CallSheetItem.DELIVERY		,ro.getDelivery());
		cv.put(DesContract.CallSheetItem.PRICE			,ro.getPrice());
		
		db.update(DesContract.CallSheetItem.TABLE_NAME, 
			cv, 
			"_id=?",
			new String[]{ Long.toString(ro.getId())}
		);
	}
	
	public void UpdateInvt(CallSheetItem ro) {// ro => record object
		ContentValues cv = new ContentValues();
		
		cv.put(DesContract.CallSheetItem.PRESENT_INVT	,ro.getPresentinvt());
		cv.put(DesContract.CallSheetItem.OFFTAKE		,ro.getOfftake());
		cv.put(DesContract.CallSheetItem.ICO			,ro.getIco());
		cv.put(DesContract.CallSheetItem.SUGGESTED		,ro.getSuggested());
		
		db.update(DesContract.CallSheetItem.TABLE_NAME, 
			cv, 
			"_id=?",
			new String[]{ Long.toString(ro.getId())}
		);
	}
	
	
	public void UpdateOrderQty(CallSheetItem ro) {// ro => record object
		ContentValues cv = new ContentValues();
		
		cv.put(DesContract.CallSheetItem.ORDER_QTY		,ro.getOrder());
		cv.put(DesContract.CallSheetItem.SUGGESTED		,ro.getOrder());
		cv.put(DesContract.CallSheetItem.SHARETPTS		,ro.getTotalSharePts());
		
		db.update(DesContract.CallSheetItem.TABLE_NAME, 
			cv, 
			"_id=?",
			new String[]{ Long.toString(ro.getId())}
		);
	}
//	public void UpdateShare(CallSheetItem ro) {// ro => record object
//		ContentValues cv = new ContentValues();
//
//		cv.put(DesContract.CallSheetItem.SHARETPTS		,ro.getTotalSharePts());
//
//		db.update(DesContract.CallSheetItem.TABLE_NAME,
//				cv,
//				"_id=?",
//				new String[]{ Long.toString(ro.getId())}
//		);
//	}
	
	public void UpdateOrderPrice(CallSheetItem ro) {// ro => record object
		ContentValues cv = new ContentValues();
		
		cv.put(DesContract.CallSheetItem.PRICE		,ro.getPrice());
		
		db.update(DesContract.CallSheetItem.TABLE_NAME, 
			cv, 
			"_id=?",
			new String[]{ Long.toString(ro.getId())}
		);
	}
	
	
	
	
	
	public int getLastId() {
		int lastId = 0;
		
		String sql = "SELECT _id FROM callsheetitems ORDER BY _id DESC LIMIT 1";
		Cursor c = db.rawQuery(sql,null);
		if (c != null && c.moveToFirst()) {
		    lastId = c.getInt(0); //The 0 is the column index, we only have 1 column, so the index is 0
		}	
		
		return lastId;
	}
	
	public CallSheetItem getInfo(Integer id) {
		CallSheetItem cs;
		String sql = "SELECT * FROM callsheetitems WHERE _id = " + id + " LIMIT 1";
		Cursor c = db.rawQuery(sql,null);
		
		if(c.moveToFirst()) {
			cs = createObject(c);
		} else {
			cs = null;
		}
		
		c.close();
		
		return cs;
	}

    public CallSheetItem getInfo2(Integer id) {
        CallSheetItem cs;
        String sql = "SELECT * FROM callsheetitems WHERE _id = " + id + " LIMIT 1";
        Cursor c = db.rawQuery(sql,null);

        if(c.moveToFirst()) {
            cs = new CallSheetItem();
            cs.setCscode(c.getString(c.getColumnIndex(DesContract.CallSheetItem.CSCODE)));
        } else {
            cs = null;
        }

        c.close();

        return cs;
    }
	
	public void AddColumn(){
		String sqlSelect = "PRAGMA table_info(callsheetitems)";
		Cursor c = db.rawQuery(sqlSelect, null);
		c.moveToFirst();
		String[] colNames = new String[50];
		int i =0;
		while(c.moveToNext()){
			Log.e("names",c.getString(1));
			colNames[i] = c.getString(1);
					i++;
		}				
		if (Arrays.asList(colNames).contains("delivery_date")) {
		    // Do some stuff.
			Log.e("contains","YES");
		}else{
			Log.e("contains","NO");
			String sqlAddCol = "ALTER TABLE callsheetitems ADD COLUMN delivery_date text DEFAULT ''";
			db.execSQL(sqlAddCol);	
		}
		
	}
	
	public List<CallSheetItem> getList(String csCode) {
		List<CallSheetItem> list = new ArrayList<CallSheetItem>();
		AddColumn();
		String sql = "SELECT csi._id, " +
					"csi.cscode," +
					"csi.itemcode," +
					"itm.categorycode," +
					"itm.description," +
					"csi.pckg," +
					"csi.pastinvt," +
					"csi.delivery," +
					"csi.presentinvt," +
					"csi.offtake," +
					"csi.ico," +
					"csi.suggested," +
					"csi.order_qty," +
					"csi.price," +
					"csi.status, " +
					"csi.delivery_date, " +
					"itm.share_pts, " +
					"csi.sharetpts, " +
					"itm.liters " +
				"FROM callsheetitems csi " +
				"LEFT JOIN items itm " +
				"ON csi.itemcode=itm.itemcode " +
				"WHERE cscode = '" + csCode + "'";
		
		Cursor c = db.rawQuery(sql,null);
		
		Log.d("CallSheetItemDbManager","Num_Rows"+c.getCount());
		
		for(int i = 0; i < c.getCount(); i++) {
			if(c.moveToPosition(i)) {
				CallSheetItem item = createObject(c);
				list.add(item);
			}
		}
		
		c.close();
		
		return list;
	}
	
	/*public Cursor  getCursor(String csCode) {
		
		String sql = "SELECT csi._id, " +
					"csi.cscode," +
					"csi.itemcode," +
					"itm.categorycode," +
					"itm.description," +
					"csi.pckg," +
					"csi.pastinvt," +
					"csi.delivery," +
					"csi.presentinvt," +
					"csi.offtake," +
					"csi.ico," +
					"csi.suggested," +
					"csi.order_qty," +
					"csi.price," +
					"csi.status " +
				"FROM callsheetitems csi " +
				"LEFT JOIN items itm " +
				"ON csi.itemcode=itm.itemcode " +
				"WHERE cscode = '" + csCode + "'";
		
		return db.rawQuery(sql,null);
	}*/

	public Integer getTotalQuantity(String csCode) {
		int total=0;

		String sql="SELECT SUM(order_qty) " +
				" FROM " +  DesContract.CallSheetItem.TABLE_NAME +
				" WHERE " +  DesContract.CallSheetItem.CSCODE 	+
				" = '" + csCode	+"' "+
				" GROUP BY " +  DesContract.CallSheetItem.CSCODE
				;

		//Log.w("Query",sql);
		Cursor c = db.rawQuery(sql, null);
		if(c.moveToFirst()) {
			total = c.getInt(0);
		}
		return total;
	}
	
	
	
	public Double getTotal(String csCode) {
		Double total=0.0;
		
		String sql="SELECT SUM(order_qty*price) " +
				" FROM " +  DesContract.CallSheetItem.TABLE_NAME + 
				" WHERE " +  DesContract.CallSheetItem.CSCODE 	+
					" = '" + csCode	+"' "+
				" GROUP BY " +  DesContract.CallSheetItem.CSCODE 
				;
							
		//Log.w("Query",sql);
		Cursor c = db.rawQuery(sql, null);
		if(c.moveToFirst()) {
			total = c.getDouble(0);
		} 		
		return total;
	}

	public int getTotalPoints(String csCode) {
		int total=0;

		String sql="SELECT SUM(sharetpts) FROM callsheetitems"+
				" WHERE cscode = '"+csCode+"'";

		//Log.w("Query",sql);
		Cursor c = db.rawQuery(sql, null);
		if(c.moveToFirst()) {
			total = c.getInt(0);
		}
		return total;
	}
	
	public String getPastOrderDate(String csCode, String cCode, String itemCode, String pckg) {
		String pastDate = "";
		String sql = "SELECT date " +
				"FROM callsheetitems csi " +
				"LEFT JOIN callsheets csh " +
					"ON csi.cscode=csh.cscode " +
				"WHERE csi.cscode NOT LIKE '" + csCode +"' " +
					"AND ccode LIKE '" + cCode +"' " +
					"AND itemcode LIKE '" + itemCode +"'" +
					"AND pckg LIKE '" + pckg +"'" +
					"AND csh.status = 1" +
				" ORDER BY csh.date DESC " +
				" LIMIT 1";
		Cursor c = db.rawQuery(sql,null);
				
		if(c.moveToFirst()) {
			pastDate = c.getString(0);
		}
		c.close();
		
		return pastDate;
	}
	
	
	public Integer getPastInvt(String csCode, String cCode, String itemCode, String pckg) {
		Integer pastInvt=0;
		
		String sql = "SELECT presentinvt " +
				"FROM callsheetitems csi " +
				"LEFT JOIN callsheets csh " +
					"ON csi.cscode=csh.cscode " +
				"WHERE csi.cscode NOT LIKE '" + csCode +"' " +
					"AND ccode LIKE '" + cCode +"' " +
					"AND itemcode LIKE '" + itemCode +"'" +
					"AND pckg LIKE '" + pckg +"'" +
					"AND csh.status = 1" +
				" ORDER BY csh.date DESC " +
				" LIMIT 1";
		Cursor c = db.rawQuery(sql,null);
				
		if(c.moveToFirst()) {
			pastInvt = c.getInt(0);
		}
		c.close();
		return pastInvt;
	}

	public Integer getSuggested(String csCode, String cCode, String itemCode, String pckg) {
		Integer suggested=0;

		String sql = "SELECT suggested " +
				"FROM callsheetitems csi " +
				"LEFT JOIN callsheets csh " +
				"ON csi.cscode=csh.cscode " +
				"WHERE csi.cscode NOT LIKE '" + csCode +"' " +
				"AND ccode LIKE '" + cCode +"' " +
				"AND itemcode LIKE '" + itemCode +"'" +
				"AND pckg LIKE '" + pckg +"'" +
				"AND csh.status = 1" +
				" ORDER BY csh.date DESC " +
				" LIMIT 1";
		Cursor c = db.rawQuery(sql,null);

		Log.e("sql",sql);

		if(c.moveToFirst()) {
			suggested = c.getInt(0);
		}
		c.close();
		return suggested;
	}
	
	public Integer getDeliveryQty(String cCode, String itemCode, String pckg) {
		Integer qty=0;
		
		Time timeNow = new Time();
        timeNow.setToNow();
        String strDate = timeNow.format("%Y-%m-%d");
		
        String sql = "SELECT qty " +
				"FROM deliveries " +				
				"WHERE ccode LIKE '" + cCode +"' " +					
					"AND itemcode LIKE '" + itemCode +"'" +
					"AND pckg LIKE '" + pckg +"' " +
					"AND date <= '" + strDate +"' " +
				" ORDER BY date DESC " +
				" LIMIT 1";
        Cursor c = db.rawQuery(sql,null);
		if (c != null && c.moveToFirst()) {
			qty = c.getInt(0); //The 0 is the column index, we only have 1 column, so the index is 0
		}	
		return qty;
	}
	
	public Integer getOffTake(String csCode, String cCode, String itemCode, String pckg, Integer presentInvt) {
		int offtake=0;
		
		int pastInvt = this.getPastInvt(csCode, cCode, itemCode, pckg);
		int delivery = this.getDeliveryQty(cCode, itemCode, pckg);
		String pastDate = this.getPastOrderDate(csCode, cCode, itemCode, pckg);
		if(pastDate!="") {
			Long pDate = Long.valueOf(pastDate) * 1000;
			
			Long today = System.currentTimeMillis();
			
			int days = (int) (today - pDate) /(1000*60*60*24);
//			if(presentInvt ==0) offtake = (int) ((pastInvt+delivery)+(((pastInvt+delivery))*0.10));
			offtake = (pastInvt+delivery-presentInvt);
//			if (days>0) offtake = (pastInvt+delivery-presentInvt)/days;
			if(offtake < 0 ) offtake=0;
		}
		return offtake;
	}
	
	
	
	public void updateStatus(Integer Id, Integer status) {
		
		String sql = "UPDATE " + DesContract.CallSheetItem.TABLE_NAME
				+ " SET " + DesContract.CallSheetItem.STATUS + "=" +status
				+ " WHERE " + BaseColumns._ID + "=" + Id ;

		//Log.w("Update Status query",sql);
		db.execSQL(sql);
	}
	

	public void delete(Integer Id) {
		
		String where = "_id=" + Id;
		db.delete(DesContract.CallSheetItem.TABLE_NAME, where, null);
		
	}
	
	public void deleteCallSheet(String csCode) {
		String where = "cscode LIKE '" + csCode +"'";
		db.delete(DesContract.CallSheetItem.TABLE_NAME, where, null);
	}
	
	public void deleteAll() {		
		db.delete(DesContract.CallSheetItem.TABLE_NAME, null, null);
	}
	
	
	private CallSheetItem createObject(Cursor c) {
		CallSheetItem ro = new CallSheetItem();
		
		ro.setId(c.getInt(
				c.getColumnIndex(BaseColumns._ID)));
		ro.setCscode(c.getString(
				c.getColumnIndex(DesContract.CallSheetItem.CSCODE)));
		ro.setItemcode(c.getString(
				c.getColumnIndex(DesContract.CallSheetItem.ITEM_CODE)));
		ro.setCategory(c.getString(
				c.getColumnIndex(DesContract.Item.CATEGORY_CODE)));
		ro.setDescription(c.getString(
				c.getColumnIndex(DesContract.Item.DESCRIPTION)));
		ro.setPckg(c.getString(
				c.getColumnIndex(DesContract.CallSheetItem.PCKG)));	
		ro.setPastinvt(c.getInt(
				c.getColumnIndex(DesContract.CallSheetItem.PAST_INVT)));
		ro.setDelivery(c.getInt(
				c.getColumnIndex(DesContract.CallSheetItem.DELIVERY)));
		ro.setPresentinvt(c.getInt(
				c.getColumnIndex(DesContract.CallSheetItem.PRESENT_INVT)));
		ro.setOfftake(c.getInt(
				c.getColumnIndex(DesContract.CallSheetItem.OFFTAKE)));
		ro.setIco(c.getInt(
				c.getColumnIndex(DesContract.CallSheetItem.ICO)));
		ro.setSuggested(c.getInt(
				c.getColumnIndex(DesContract.CallSheetItem.SUGGESTED)));
		ro.setOrder(c.getInt(
				c.getColumnIndex(DesContract.CallSheetItem.ORDER_QTY)));
		ro.setPrice(c.getDouble(
				c.getColumnIndex(DesContract.CallSheetItem.PRICE)));
		ro.setStatus(c.getInt(
				c.getColumnIndex(DesContract.CallSheetItem.STATUS)));
		ro.setSharepts(c.getDouble(
				c.getColumnIndex(DesContract.Item.SHAREPTS)));
		ro.setLiters(c.getDouble(
				c.getColumnIndex(DesContract.Item.LITERS)));
		ro.setTotalSharePts(c.getInt(
				c.getColumnIndex(DesContract.CallSheetItem.SHARETPTS)));
		ro.setDelivery_date(c.getString(
				c.getColumnIndex(DesContract.CallSheetItem.DELIVERY_DATE)));	
		return ro;
	}
		
}
