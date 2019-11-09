package com.xpluscloud.mosesshell_davao.dbase;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.xpluscloud.mosesshell_davao.getset.CmpItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ComItemDbManager extends DbManager{
	
	public static final String[] ITEM_PROJECTION = new String[] {
		DesContract.CustItemList._ID,
		DesContract.CustItemList.ITEMCODE,
		DesContract.CustItemList.DESCRIPTION,
		DesContract.CustItemList.BARCODE,
		DesContract.CustItemList.CASE_BARCODE,
		DesContract.CustItemList.STATUS
		
	};

	public ComItemDbManager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public JSONArray getItems(){
		String sql = "SELECT *FROM "+DesContract.Item.TABLE_NAME;
//		String sql = "SELECT *FROM guides ORDER BY RANDOM() LIMIT 1000";
		//String sql1 = "SELECT * FROM " + table + " WHERE " + where +" ORDER BY _id ASC";
		Cursor c = db.rawQuery(sql,null);
		JSONArray jaResult = new JSONArray();

		c.moveToFirst();                   
		
		while (c.isAfterLast() == false) {
//			String result = c.getString(c.getColumnIndex("sortGuideNo"));
//			String sql2 = "SELECT * FROM results where gameid = 4 AND sresult = '"+result+"'";
//			Cursor nCurs = db.rawQuery(sql2, null);
//			
//			if(nCurs.moveToFirst() == false){
				int totalColumn = c.getColumnCount();
				JSONObject rowObject = new JSONObject();
				for( int i=0 ;  i< totalColumn ; i++ ){
					if( c.getColumnName(i) != null ) {
					      try {
						      if( c.getString(i) != null ) {	
//							      Log.e("parser", c.getString(i) );	
						    	  if(c.getString(i).contains("&")){
						    		  rowObject.put(c.getColumnName(i) ,  c.getString(i).replace("&", "and") );	
						    	  }
						    	  else{
						    		  rowObject.put(c.getColumnName(i) ,  c.getString(i) );
						    	  }
						      }		
						      else{	
						    	  rowObject.put( c.getColumnName(i) ,  "" ); 	
						      }
					      }
			
							catch( Exception e ){
								//Log.d("TAG_NAME", e.getMessage() );		
							}	
					} 
				}
					  
				      jaResult.put(rowObject);
//			}
				      c.moveToNext();									
		}
		c.close();
		return jaResult;
	}
	
	public List<CmpItem> getList() {
		List<CmpItem> list = new ArrayList<CmpItem>();
		Cursor c = retrieveCursor(DesContract.CustItemList.TABLE_NAME, ITEM_PROJECTION ,
				DesContract.CustItemList.DESCRIPTION);
		
		for(int i = 0; i < c.getCount(); i++) {
			if(c.moveToPosition(i)) {
				CmpItem item = createItem(c);;
				list.add(item);
			}
		}
		
		c.close();
		
		return list;
	}
	
	private CmpItem createItem(Cursor c) {
		CmpItem item = new CmpItem();
		
		item.setId(c.getInt(
				c.getColumnIndex(BaseColumns._ID)));
		item.setItemCode(c.getString(
				c.getColumnIndex(DesContract.CustItemList.ITEMCODE)));
		item.setDescription(c.getString(
				c.getColumnIndex(DesContract.CustItemList.DESCRIPTION)));
		item.setPackBarcode(c.getString(
				c.getColumnIndex(DesContract.CustItemList.CASE_BARCODE)));
		item.setBarcode(c.getString(
				c.getColumnIndex(DesContract.CustItemList.BARCODE)));
		
		return item;
	}

}
