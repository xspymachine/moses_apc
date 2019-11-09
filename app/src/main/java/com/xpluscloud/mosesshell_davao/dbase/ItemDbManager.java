package com.xpluscloud.mosesshell_davao.dbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.xpluscloud.mosesshell_davao.getset.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemDbManager extends DbManager {
	
	public static final String[] ITEM_PROJECTION = new String[] {
		DesContract.Item._ID,
		DesContract.Item.ITEM_CODE,
		DesContract.Item.DESCRIPTION,
		DesContract.Item.CATEGORY_CODE,
		DesContract.Item.BARCODE,
		DesContract.Item.PACK_BARCODE,
		DesContract.Item.QTY_PER_PACK,
		DesContract.Item.PRICE_PER_PACK,
		DesContract.Item.PRICE_PER_UNIT,
		DesContract.Item.PRIORITY,
		DesContract.Item.ITEM_ID
		
	};
	
	public ItemDbManager(Context context) {
		super(context);
	}
	
	public long AddItem(Item i) {
		ContentValues cv = new ContentValues();
		
		cv.put(DesContract.Item.ITEM_CODE		, i.getItemCode());
		cv.put(DesContract.Item.DESCRIPTION		, i.getDescription());
		cv.put(DesContract.Item.CATEGORY_CODE	, i.getCategoryCode());
		cv.put(DesContract.Item.BARCODE         , i.getBarcode());
		cv.put(DesContract.Item.PACK_BARCODE    , i.getPackBarcode());
		cv.put(DesContract.Item.QTY_PER_PACK	, i.getQtyPerPack());
		cv.put(DesContract.Item.PRICE_PER_PACK  , i.getPricePerPack());
		cv.put(DesContract.Item.PRICE_PER_UNIT  , i.getPricePerUnit());
		cv.put(DesContract.Item.PRIORITY  		, i.getPriority());
		cv.put(DesContract.Item.ITEM_ID  		, i.getItemid());
		
		//Log.e("Price per Pack",""+i.getPricePerPack());
		
		return db.insertWithOnConflict(DesContract.Item.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
	}
	
	public void AddItems(List<Item> list) {
		for(int i = 0; i < list.size(); i++) {
			AddItem(list.get(i));
		}
	}
	
	public Item getItem(int rowId) {
		Item item;
		Cursor c = retrieveCursor(rowId, DesContract.Item.TABLE_NAME, ITEM_PROJECTION);
		
		if(c.moveToFirst()) {
			item = createItem(c);
		} else {
			item = null;
		}
		
		c.close();
		
		return item;
	}
	
	public List<Item> getList1234(int catid,int icatid) {
		List<Item> list = new ArrayList<Item>();
		Cursor c;
//		Cursor c = retrieveCursor(DesContract.Item.TABLE_NAME, ITEM_PROJECTION ,
//				DesContract.Item.PRIORITY + " ASC, " +
//				DesContract.Item.CATEGORY_CODE + " ASC, " +
//				DesContract.Item.ITEM_ID + " ASC LIMIT 500");

//		switch(opt){
//			case 1:
				c = getWhere(DesContract.Item.TABLE_NAME, ITEM_PROJECTION,
						DesContract.Item.CATID+ "="+catid+" AND "+DesContract.Item.SUBCATID+ "="+icatid,
						DesContract.Item.PRIORITY + " ASC, " +
								DesContract.Item.CATEGORY_CODE + " ASC, " +
								DesContract.Item.ITEM_ID + " ASC LIMIT 500");
//				break;
//			case 2:
//				c = getWhere(DesContract.Item.TABLE_NAME, ITEM_PROJECTION,
//						DesContract.Item.PRICE_PER_UNIT+ " < 1 AND "+ DesContract.Item.PRICE_PER_PACK+" < 1",
//						DesContract.Item.PRIORITY + " ASC, " +
//								DesContract.Item.CATEGORY_CODE + " ASC, " +
//								DesContract.Item.ITEM_ID + " ASC LIMIT 500");
//				break;
//		};
		
		for(int i = 0; i < c.getCount(); i++) {
			if(c.moveToPosition(i)) {
				Item item = createItem(c);
				list.add(item);
			}
		}
		
		c.close();
		
		return list;
	}
	
	public List<Item> getPriorityList(int priority) {
		List<Item> list = new ArrayList<Item>();
		
		String sql="SELECT * FROM items " +
				" WHERE priority <= " + priority +
				" ORDER BY priority ASC, categorycode ASC, itemid ASC " +
				" LIMIT 100";
		
		
		Cursor c = db.rawQuery(sql,null);
		
		for(int i = 0; i < c.getCount(); i++) {
			if(c.moveToPosition(i)) {
				Item item = createItem(c);
				list.add(item);
			}
		}
		
		c.close();
		
		return list;
	}
	
	public List<Item> getTop(int limit) {
		List<Item> list = new ArrayList<Item>();
		
		String sql="SELECT * FROM items " +
				" ORDER BY priority ASC, categorycode ASC, itemid ASC " +
				" LIMIT " + limit ;
		
		
		Cursor c = db.rawQuery(sql,null);
		
		for(int i = 0; i < c.getCount(); i++) {
			if(c.moveToPosition(i)) {
				Item item = createItem(c);
				list.add(item);
			}
		}
		
		c.close();
		
		return list;
	}
	
	
	public ArrayList<String> getCategoryList(int opt,int catid) {
		ArrayList<String> list = new ArrayList<>();
		String sql = "";

		switch (opt){
			case 1: sql = "SELECT category  FROM icategory ";
			break;
			case 2: sql = "SELECT subcategory  FROM isubcategory WHERE category_id="+catid;
		}
		
		Cursor c = db.rawQuery(sql,null);
		
		for(int i = 0; i < c.getCount(); i++) {
			if(c.moveToPosition(i)) {
				list.add(c.getString(0));
			}
		}
		c.close();
		return list;
	}

	public int getCatId(String category,int opt){
		int catid =0;
		String sql = "";
		switch (opt){
			case 1: sql ="SELECT category_id FROM icategory WHERE category='"+category+"'";
					break;
			case 2: sql = "SELECT subcategory_id FROM isubcategory WHERE subcategory='"+category+"'";
					break;
		}
		Cursor c = db.rawQuery(sql,null);
		if(c.moveToFirst()) catid = c.getInt(0);

		return catid;
	}
	
	public List<Item> getCanvassItems() {
		List<Item> list = new ArrayList<Item>();
		
		String sql="SELECT * FROM items " +
				" WHERE priority >=100000"  +
				" ORDER BY priority ASC, categorycode ASC, itemid ASC " +
				" LIMIT 100";
		
		
		Cursor c = db.rawQuery(sql,null);
		
		
		for(int i = 0; i < c.getCount(); i++) {
			if(c.moveToPosition(i)) {
				Item item = createItem(c);;
				list.add(item);
			}
		}
		
		c.close();
		
		return list;
	}
	
	
	public Double getPrice(String itemCode, int iPckg) {
		Double pPrice = 0.0;
		
		String PCKG="pp_pack";
		if(iPckg==1) {
			PCKG = "pp_unit";
		}
		
		String sql = "SELECT " + PCKG +
				" FROM items " +
				" WHERE itemcode LIKE '" + itemCode +"'" +
				" LIMIT 1" ;
		
		
		
		Cursor c = db.rawQuery(sql,null);
		
		if(c.getCount()>0) {
			if (c != null && c.moveToFirst()) {
				pPrice = c.getDouble(0); //The 0 is the column index, we only have 1 column, so the index is 0
			}
		}
		return pPrice;
	}

	public int getPacking(String itemcode){
		String sql = "SELECT packing FROM items WHERE itemcode='"+itemcode+"'";
		Cursor c = db.rawQuery(sql,null);
		int pack=0;
		if(c.getCount()>0) {
			if (c != null && c.moveToFirst()) {
				pack = c.getInt(0); //The 0 is the column index, we only have 1 column, so the index is 0
			}
		}

		return pack;
	}
	
	
	public void delete(Integer Id) {
		
		String where = "_id=" + Id;
		db.delete(DesContract.Item.TABLE_NAME, where, null);
		
	}
	
	public void deleteAll() {
			
		db.delete(DesContract.Item.TABLE_NAME, null, null);
		
	}

	private Item createItem(Cursor c) {
		Item item = new Item();
		
		item.setId(c.getInt(
				c.getColumnIndex(BaseColumns._ID)));
		item.setItemCode(c.getString(
				c.getColumnIndex(DesContract.Item.ITEM_CODE)));
		item.setDescription(c.getString(
				c.getColumnIndex(DesContract.Item.DESCRIPTION)));
		item.setCategoryCode(c.getString(
				c.getColumnIndex(DesContract.Item.CATEGORY_CODE)));
		item.setPackBarcode(c.getString(
				c.getColumnIndex(DesContract.Item.PACK_BARCODE)));
		item.setBarcode(c.getString(
				c.getColumnIndex(DesContract.Item.BARCODE)));
		item.setQtyPerPack(c.getInt(
				c.getColumnIndex(DesContract.Item.QTY_PER_PACK)));
		item.setPricePerPack(c.getDouble(
				c.getColumnIndex(DesContract.Item.PRICE_PER_PACK)));
		item.setPricePerUnit(c.getDouble(
				c.getColumnIndex(DesContract.Item.PRICE_PER_UNIT)));		
		item.setPriority(c.getInt(
				c.getColumnIndex(DesContract.Item.PRIORITY)));
		item.setItemid(c.getInt(
				c.getColumnIndex(DesContract.Item.ITEM_ID)));
		return item;
	}
}
