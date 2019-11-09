package com.xpluscloud.mosesshell_davao.dbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.xpluscloud.mosesshell_davao.getset.Inventory;

import java.util.ArrayList;
import java.util.List;

public class InventoryDbManager extends DbManager {

    public final String TAG = "InventoryDbManager";

    public InventoryDbManager(Context context) {
        super(context);
    }

    public long AddInventorySheet(Inventory ro) {// ro => record object
        ContentValues cv = new ContentValues();

        cv.put(DesContract.Inventory.DATE 		, ro.getDate());
        cv.put(DesContract.Inventory.INCODE		, ro.getINCode());
        cv.put(DesContract.Inventory.INNO 		, ro.getINno());
        cv.put(DesContract.Inventory.CCODE    	, ro.getCcode());
        cv.put(DesContract.Inventory.STATUS 	, ro.getStatus());

        if (ro.getId() > 0) cv.put(BaseColumns._ID, ro.getId());

        return db.insertWithOnConflict(
                DesContract.Inventory.TABLE_NAME,
                null,
                cv,
                SQLiteDatabase.CONFLICT_REPLACE
        );

    }

    public long AddInventorySheetItem(Inventory ro) {// ro => record object
        ContentValues cv = new ContentValues();

        if(this.isDuplicate(ro.getINCode(), ro.getItemCode())) return 0;

        cv.put(DesContract.Inventory.INCODE			,ro.getINCode());
        cv.put(DesContract.Inventory.ITEM_CODE		,ro.getItemCode());
        cv.put(DesContract.Inventory.PCKG			,ro.getPckg());
        cv.put(DesContract.Inventory.QTY    		,ro.getQty());
        cv.put(DesContract.Inventory.STATUS			,ro.getStatus());

        if (ro.getId() > 0) cv.put(BaseColumns._ID, ro.getId());

        return db.insert(
                DesContract.Inventory.TABLE_NAME2,
                null,
                cv
        );
    }
    public List<Inventory> getList(String INCODE) {
        List<Inventory> list = new ArrayList<>();

        String sql = "SELECT ii._id, " +
                "ii.incode," +
                "ii.itemcode," +
                "itm.description," +
                "ii.pckg," +
                "ii.qty, " +
                "ii.status " +
                "FROM inventory_items ii " +
                "LEFT JOIN items itm " +
                "ON ii.itemcode=itm.itemcode " +
                "WHERE incode = '" + INCODE + "'";

        Cursor c = db.rawQuery(sql,null);

//        Log.d("CallSheetItemDbManager","Num_Rows"+c.getCount());

        for(int i = 0; i < c.getCount(); i++) {
            if(c.moveToPosition(i)) {
                Inventory item = createObjectItems(c);
                list.add(item);
            }
        }

        c.close();

        return list;
    }
    private Inventory createObjectItems(Cursor c) {
        Inventory ro = new Inventory();

        ro.setId(c.getInt(
                c.getColumnIndex(BaseColumns._ID)));
        ro.setINCode(c.getString(
                c.getColumnIndex(DesContract.Inventory.INCODE)));
        ro.setItemCode(c.getString(
                c.getColumnIndex(DesContract.Inventory.ITEM_CODE)));
        ro.setPckg(c.getString(
                c.getColumnIndex(DesContract.Inventory.PCKG)));
        ro.setDescription(c.getString(
                c.getColumnIndex(DesContract.Item.DESCRIPTION)));
        ro.setQty(c.getInt(
                c.getColumnIndex(DesContract.Inventory.QTY)));
        ro.setStatus(c.getInt(
                c.getColumnIndex(DesContract.Inventory.STATUS)));
        return ro;
    }
    private Inventory createObjectItems2(Cursor c) {
        Inventory ro = new Inventory();

        ro.setId(c.getInt(
                c.getColumnIndex(BaseColumns._ID)));
        ro.setCcode(c.getString(
                c.getColumnIndex(DesContract.Inventory.CCODE)));
        ro.setINno(c.getInt(
                c.getColumnIndex(DesContract.Inventory.INNO)));
        ro.setINCode(c.getString(
                c.getColumnIndex(DesContract.Inventory.INCODE)));
        ro.setDate(c.getString(
                c.getColumnIndex(DesContract.Inventory.DATE)));
        ro.setStatus(c.getInt(
                c.getColumnIndex(DesContract.Inventory.STATUS)));
        return ro;
    }
    private Boolean isDuplicate(String csCode, String itemCode) {
        Boolean exist=false;

        String sql = "SELECT COUNT(_id) " +
                "FROM " + DesContract.Inventory.TABLE_NAME2 +
                " WHERE " + DesContract.Inventory.INCODE +" LIKE '" +csCode +"' " +
                " AND " + DesContract.Inventory.ITEM_CODE +" LIKE '" +itemCode +"' " +
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

    public int getLastSOno() {
        int lastSOno=0;

        int lastid= this.getLastId();
        if(lastid>0) {
            String sql = "SELECT inno FROM inventory WHERE _id = "+lastid+" LIMIT 1";
            Cursor c = db.rawQuery(sql,null);
            if (c != null && c.moveToFirst()) {
                lastSOno = c.getInt(0); //The 0 is the column index, we only have 1 column, so the index is 0
            }
        }
        return lastSOno;
    }
    public int getLastId() {
        int lastId = 0;

        String sql = "SELECT _id FROM inventory ORDER BY _id DESC LIMIT 1";
        Cursor c = db.rawQuery(sql,null);
        if (c != null && c.moveToFirst()) {
            lastId = c.getInt(0); //The 0 is the column index, we only have 1 column, so the index is 0
        }

        return lastId;
    }
    public void UpdateOrderQty(Inventory ro) {// ro => record object
        ContentValues cv = new ContentValues();

        cv.put(DesContract.Inventory.QTY		,ro.getQty());

        db.update(DesContract.Inventory.TABLE_NAME2,
                cv,
                "_id=?",
                new String[]{ Long.toString(ro.getId())}
        );
    }
    public Integer getTotalQuantity(String csCode) {
        int total=0;

        String sql="SELECT SUM(qty) " +
                " FROM " +  DesContract.Inventory.TABLE_NAME2 +
                " WHERE " +  DesContract.Inventory.INCODE 	+
                " = '" + csCode	+"' "+
                " GROUP BY " +  DesContract.Inventory.INCODE
                ;

        //Log.w("Query",sql);
        Cursor c = db.rawQuery(sql, null);
        if(c.moveToFirst()) {
            total = c.getInt(0);
        }
        return total;
    }

    public Inventory getInfo(String incode) {
        Inventory cs=null;
        String sql = "SELECT * FROM inventory WHERE incode = '" + incode + "' LIMIT 1";
        Cursor c = db.rawQuery(sql,null);

        if(c.getCount()>0) {
            if(c.moveToFirst()) {
                cs = createObjectItems2(c);
            }
        }

        c.close();

        return cs;
    }
    public void updateStatus(Integer Id, Integer status) {

        String sql = "UPDATE " + DesContract.Inventory.TABLE_NAME
                + " SET " + DesContract.Inventory.STATUS + "=" +status
                + " WHERE " + BaseColumns._ID + "=" + Id ;

        Log.w("Update Status query",sql);
        db.execSQL(sql);
    }

    public void delete(Integer Id) {

        String where = "_id=" + Id;
        db.delete(DesContract.Inventory.TABLE_NAME2, where, null);

    }

    public void deleteCallSheet(String csCode) {
        String where = "incode LIKE '" + csCode +"'";
        db.delete(DesContract.Inventory.TABLE_NAME, where, null);

    }
    public void deleteCallSheetItems(String csCode) {
        String where = "incode LIKE '" + csCode +"'";
        db.delete(DesContract.Inventory.TABLE_NAME2, where, null);
    }

    public Integer getLastInventoryTransaction(String ccode){
//        SalesCall ccall;
        int count = 0;
        String OrderBy = "_id DESC";
        String Limit = "1";
        String where = "ccode = '"+ccode+"' AND strftime('%Y-%m-%d',s."+DesContract.Inventory.DATE+",'unixepoch') = strftime('%Y-%m-%d', 'now')";
        String sql =  " SELECT *from "+DesContract.Inventory.TABLE_NAME +" s "
                +" LEFT JOIN "+DesContract.Customer.TABLE_NAME+" c "+" USING ("+DesContract.Customer.CCODE+")"
                +" WHERE "+where;

        Cursor c = db.rawQuery(sql, null);
//        if(c.moveToFirst()) {
//            ccall = createSalesCall(c);
//        } else {
//            ccall = null;
//        }
        if(c.getCount() > 0) count=c.getCount();
        c.close();

        return count;
    }
}
