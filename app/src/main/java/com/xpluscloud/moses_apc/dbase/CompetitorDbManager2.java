package com.xpluscloud.moses_apc.dbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.xpluscloud.moses_apc.getset.CallSheet;
import com.xpluscloud.moses_apc.getset.CmpItem;
import com.xpluscloud.moses_apc.getset.Inventory;
import com.xpluscloud.moses_apc.getset.Item;
import com.xpluscloud.moses_apc.getset.SalesCall;

import java.util.ArrayList;
import java.util.List;

public class CompetitorDbManager2 extends DbManager {
    public CompetitorDbManager2(Context context) {
        super(context);
    }

    public ArrayList<String> getCategoryList(int opt, int catid) {
        ArrayList<String> list = new ArrayList<>();
        String sql = "";

        switch (opt){
            case 1: sql = "SELECT category  FROM cmpicategory ";
                break;
            case 2: sql = "SELECT subcategory  FROM cmpisubcategory WHERE category_id = "+catid;
                break;
            case 3: sql = "SELECT company  FROM cmpicompany WHERE category_id LIKE '%"+catid+"%'";
                break;
            case 4: sql = "SELECT description  FROM citem WHERE category_id = "+catid;
                break;
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
            case 1: sql ="SELECT category_id FROM cmpicategory WHERE category='"+category+"'";
                break;
            case 2: sql = "SELECT subcategory_id FROM cmpisubcategory WHERE subcategory='"+category+"'";
                break;
        }
        Cursor c = db.rawQuery(sql,null);
        if(c.moveToFirst()) catid = c.getInt(0);

        return catid;
    }
    public String getCatStr(int categoryid,int opt){
        String cat ="";
        String sql = "";
        switch (opt){
            case 1: sql ="SELECT category FROM cmpicategory WHERE category_id="+categoryid;
                break;
            case 2: sql = "SELECT subcategory FROM cmpisubcategory WHERE subcategory_id="+categoryid;
                break;
        }
        Cursor c = db.rawQuery(sql,null);
        if(c.moveToFirst()) cat = c.getString(0);

        return cat;
    }

    public List<Item> getList1234(int catid, int icatid) {
        List<Item> list = new ArrayList<Item>();
        Cursor c;
//        c = getWhere(DesContract.Item.TABLE_NAME, ITEM_PROJECTION,
//                DesContract.Item.CATID+ "="+catid+" AND "+DesContract.Item.SUBCATID+ "="+icatid,
//                " _id ASC LIMIT 500");
        String sql = "SELECT _id, itemcode,description FROM citem" +
                    " WHERE subcategory_id="+icatid +" AND category_id="+catid+
                    " ORDER BY _id ASC";
        c= db.rawQuery(sql,null);

        for(int i = 0; i < c.getCount(); i++) {
            if(c.moveToPosition(i)) {
                Item itm = new Item();
                itm.setId(c.getInt(c.getColumnIndexOrThrow("_id")));
                itm.setItemCode(c.getString(c.getColumnIndexOrThrow("itemcode")));
                itm.setDescription(c.getString(c.getColumnIndexOrThrow("description")));
                list.add(itm);
            }
        }

        c.close();

        return list;
    }

    public ArrayList<String> getCmpFilterList(String cat, String subcat, String brand) {
        ArrayList<String> list = new ArrayList<>();
        Cursor c;
        int catid = getCatId(cat,1);
        int icatid = getCatId(subcat,2);
        String sql = "SELECT _id, itemcode,description FROM citem" +
                " WHERE subcategory_id="+icatid +" AND category_id="+catid+
                " AND bp_name LIKE '%"+brand+"%'" +
                " ORDER BY _id ASC";
        c= db.rawQuery(sql,null);

        Log.e("sql",sql);

        for(int i = 0; i < c.getCount(); i++) {
            if(c.moveToPosition(i)) {
//                Item itm = new Item();
//                itm.setId(c.getInt(c.getColumnIndex("_id")));
//                itm.setItemCode(c.getString(c.getColumnIndex("itemcode")));
//                itm.setDescription(c.getString(c.getColumnIndex("description")));
                list.add(c.getString(c.getColumnIndexOrThrow("description")));
            }
        }

        c.close();

        return list;
    }

    public String getcmpItemcode(String description){
        String code = "";
        String sql = "SELECT itemcode FROM citem WHERE description='"+description+"'";
        Cursor c = db.rawQuery(sql,null);

        if(c.moveToFirst()) code = c.getString(0);

        return code;
    }

    public ArrayList<String> getBrands(){
        ArrayList<String> bp_list = new ArrayList<>();
        String sql = "SELECT DISTINCT UPPER(bp_name) FROM citem";
        Cursor c = db.rawQuery(sql,null);

        for(int i = 0; i < c.getCount(); i++) {
            if(c.moveToPosition(i)) {
                bp_list.add(c.getString(0));
            }
        }
        return bp_list;
    }

    public long AddInventorySheetItem(Inventory ro) {// ro => record object
        ContentValues cv = new ContentValues();

        if(this.isDuplicate(ro.getINCode(), ro.getItemCode())) return 0;

        cv.put(DesContract.Competitors.INCODE			,ro.getINCode());
        cv.put(DesContract.Competitors.ITEM_CODE		,ro.getItemCode());
        cv.put(DesContract.Competitors.PCKG			    ,ro.getPckg());
        cv.put(DesContract.Competitors.QTY    		    ,ro.getQty());
        cv.put(DesContract.Competitors.PRICE    		,ro.getPrice());
        cv.put(DesContract.Competitors.STATUS			,ro.getStatus());
        cv.put(DesContract.Competitors.PRICEPERPACK		,ro.getPriceperpack());
        cv.put(DesContract.Competitors.SRP			    ,ro.getSrp());
        cv.put(DesContract.Competitors.UOM			    ,ro.getUom());
        cv.put(DesContract.Competitors.CATEGORY			,ro.getCategory());
        cv.put(DesContract.Competitors.SUBCATEGORY		,ro.getSubcategory());
        cv.put(DesContract.Competitors.COMPANY			,ro.getCompany());

        if (ro.getId() > 0) cv.put(BaseColumns._ID, ro.getId());

        return db.insert(
                DesContract.Competitors.TABLE_NAME2,
                null,
                cv
        );
    }

    public long AddOtherItem(CmpItem ro) {// ro => record object
        ContentValues cv = new ContentValues();

        cv.put(DesContract.CustItemList.ITEMCODE		,ro.getItemCode());
        cv.put(DesContract.CustItemList.DESCRIPTION		,ro.getDescription());
        cv.put(DesContract.CustItemList.CATID			,ro.getCategory_id());
        cv.put(DesContract.CustItemList.SUBCATID    	,ro.getSubcategory_id());
        cv.put(DesContract.CustItemList.GROUPID    	    ,ro.getGroupid());
        cv.put(DesContract.CustItemList.BPNAME    	    ,ro.getBpname());
        cv.put(DesContract.CustItemList.CASE_BARCODE    ,ro.getCase_barcode());
        cv.put(DesContract.CustItemList.BARCODE    	    ,ro.getBarcode());
        cv.put(DesContract.CustItemList.STATUS			,ro.getStatus());

        return db.insert(
                "citem",
                null,
                cv
        );
    }

    private Boolean isDuplicate(String csCode, String itemCode) {
        Boolean exist=false;

        String sql = "SELECT COUNT(_id) " +
                "FROM " + DesContract.Competitors.TABLE_NAME2 +
                " WHERE " + DesContract.Competitors.INCODE +" LIKE '" +csCode +"' " +
                " AND " + DesContract.Competitors.ITEM_CODE +" LIKE '" +itemCode +"' " +
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

    public Inventory getInfo(String incode) {
        Inventory cs=null;
        String sql = "SELECT * FROM cmpsheet WHERE cmpcode = '" + incode + "' LIMIT 1";
        Cursor c = db.rawQuery(sql,null);

        if(c.getCount()>0) {
            if(c.moveToFirst()) {
                cs = createObjectItems2(c);
            }
        }

        c.close();

        return cs;
    }
    private Inventory createObjectItems2(Cursor c) {
        Inventory ro = new Inventory();

        ro.setId(c.getInt(
                c.getColumnIndexOrThrow(BaseColumns._ID)));
        ro.setCcode(c.getString(
                c.getColumnIndexOrThrow(DesContract.Competitors.CCODE)));
        ro.setINno(c.getInt(
                c.getColumnIndexOrThrow(DesContract.Competitors.INNO)));
        ro.setINCode(c.getString(
                c.getColumnIndexOrThrow(DesContract.Competitors.INCODE)));
        ro.setDate(c.getString(
                c.getColumnIndexOrThrow(DesContract.Competitors.DATE)));
        ro.setStatus(c.getInt(
                c.getColumnIndexOrThrow(DesContract.Competitors.STATUS)));
        return ro;
    }

    public long AddInventorySheet(Inventory ro) {// ro => record object
        ContentValues cv = new ContentValues();

        cv.put(DesContract.Competitors.DATE 		, ro.getDate());
        cv.put(DesContract.Competitors.INCODE		, ro.getINCode());
        cv.put(DesContract.Competitors.INNO 		, ro.getINno());
        cv.put(DesContract.Competitors.CCODE    	, ro.getCcode());
        cv.put(DesContract.Competitors.STATUS 	    , ro.getStatus());

        if (ro.getId() > 0) cv.put(BaseColumns._ID, ro.getId());

        return db.insertWithOnConflict(
                DesContract.Competitors.TABLE_NAME,
                null,
                cv,
                SQLiteDatabase.CONFLICT_REPLACE
        );

    }
    public void updateStatus(Integer Id, Integer status) {

        String sql = "UPDATE " + DesContract.Competitors.TABLE_NAME
                + " SET " + DesContract.Competitors.STATUS + "=" +status
                + " WHERE " + BaseColumns._ID + "=" + Id ;

        Log.w("Update Status query",sql);
        db.execSQL(sql);
    }
    public List<Inventory> getList(String INCODE) {
        List<Inventory> list = new ArrayList<>();

        String sql = "SELECT ii._id, " +
                "ii.cmpcode," +
                "ii.itemcode," +
                "itm.description," +
                "itm.subcategory_id," +
                "itm.category_id," +
                "ii.pckg," +
                "ii.qty, " +
                "ii.price, " +
                "ii.category, " +
                "ii.subcategory, " +
                "ii.company, " +
                "ii.status " +
                "FROM cmpsheetitems ii " +
                "LEFT JOIN citem itm " +
                "ON ii.itemcode=itm.itemcode " +
                "WHERE cmpcode = '" + INCODE + "'";

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
                c.getColumnIndexOrThrow(BaseColumns._ID)));
        ro.setINCode(c.getString(
                c.getColumnIndexOrThrow(DesContract.Competitors.INCODE)));
        ro.setItemCode(c.getString(
                c.getColumnIndexOrThrow(DesContract.Competitors.ITEM_CODE)));
        ro.setPckg(c.getString(
                c.getColumnIndexOrThrow(DesContract.Competitors.PCKG)));
        ro.setDescription(c.getString(
                c.getColumnIndexOrThrow(DesContract.Item.DESCRIPTION)));
        ro.setQty(c.getInt(
                c.getColumnIndexOrThrow(DesContract.Competitors.QTY)));
        ro.setPrice(c.getFloat(
                c.getColumnIndexOrThrow(DesContract.Competitors.PRICE)));
        ro.setStatus(c.getInt(
                c.getColumnIndexOrThrow(DesContract.Competitors.STATUS)));
        ro.setSubcategoryid(c.getInt(c.getColumnIndexOrThrow("subcategory_id")));
        ro.setCategoryid(c.getInt(c.getColumnIndexOrThrow("category_id")));
        ro.setCategory(c.getString(c.getColumnIndexOrThrow("category")));
        ro.setSubcategory(c.getString(c.getColumnIndexOrThrow("subcategory")));
        ro.setCompany(c.getString(c.getColumnIndexOrThrow("company")));
        return ro;
    }
    public int getLastSOno() {
        int lastSOno=0;

        int lastid= this.getLastId();
        if(lastid>0) {
            String sql = "SELECT cmpno FROM cmpsheet WHERE _id = "+lastid+" LIMIT 1";
            Cursor c = db.rawQuery(sql,null);
            if (c != null && c.moveToFirst()) {
                lastSOno = c.getInt(0); //The 0 is the column index, we only have 1 column, so the index is 0
            }
        }
        return lastSOno;
    }
    public int getLastId() {
        int lastId = 0;

        String sql = "SELECT _id FROM cmpsheet ORDER BY _id DESC LIMIT 1";
        Cursor c = db.rawQuery(sql,null);
        if (c != null && c.moveToFirst()) {
            lastId = c.getInt(0); //The 0 is the column index, we only have 1 column, so the index is 0
        }

        return lastId;
    }
    public void UpdateOrderPrice(Inventory ro) {// ro => record object
        ContentValues cv = new ContentValues();

        cv.put(DesContract.Inventory.PRICE		,ro.getPrice());

        int i = db.update(DesContract.Competitors.TABLE_NAME2,
                cv,
                "_id=?",
                new String[]{ Long.toString(ro.getId())}
        );
        Log.w("Query",""+i);
    }
    public Integer getTotalPrice(String csCode) {
        int total=0;

        String sql="SELECT SUM(price) " +
                " FROM " +  DesContract.Competitors.TABLE_NAME2 +
                " WHERE " +  DesContract.Competitors.INCODE 	+
                " = '" + csCode	+"' "+
                " GROUP BY " +  DesContract.Competitors.INCODE
                ;

        //Log.w("Query",sql);
        Cursor c = db.rawQuery(sql, null);
        if(c.moveToFirst()) {
            total = c.getInt(0);
        }
        return total;
    }

    public List<CallSheet> getAll(String cCode, String date) {
        List<CallSheet> list = new ArrayList<CallSheet>();
        String where;

        if (cCode==null || cCode.isEmpty()) where ="";
        else {
            where = " WHERE ccode LIKE '" + cCode +"' "+date;
        }
        if(!date.isEmpty()) where = date;

        String sql = "SELECT t1._id," +
                "strftime('%m/%d/%Y %H:%M:%S',t1.date,'unixepoch','localtime')  AS date," +
                "t1.cmpcode," +
                "t1.cmpno," +
                "t2.name," +
                "t1.status " +
                " FROM cmpsheet AS t1" +
                " LEFT JOIN customers AS t2" +
                " ON t1.ccode=t2.ccode " + where +
                " ORDER BY  t1.date DESC, t1._id DESC";

        Cursor c = db.rawQuery(sql,null);

        if (c.getCount()>0) {
            for(int i = 0; i < c.getCount(); i++) {
                if(c.moveToPosition(i)) {
                    CallSheet obj = new CallSheet();
                    obj.setId(c.getInt(
                            c.getColumnIndexOrThrow(BaseColumns._ID)));
                    obj.setDate(c.getString(
                            c.getColumnIndexOrThrow(DesContract.Competitors.DATE)));
                    obj.setCscode(c.getString(
                            c.getColumnIndexOrThrow(DesContract.Competitors.INCODE)));
                    obj.setcusname(c.getString(
                            c.getColumnIndexOrThrow("name")));
                    obj.setSono(c.getInt(
                            c.getColumnIndexOrThrow(DesContract.Competitors.INNO)));
                    obj.setStatus(c.getInt(
                            c.getColumnIndexOrThrow(DesContract.Competitors.STATUS)));
                    list.add(obj);
                }
            }
        }

        c.close();

        return list;
    }

    public void delete(Integer Id) {

        String where = "_id=" + Id;
        db.delete(DesContract.Competitors.TABLE_NAME2, where, null);

    }

    public void deleteCallSheet(String csCode) {
        String where = "cmpcode LIKE '" + csCode +"'";
        db.delete(DesContract.Competitors.TABLE_NAME, where, null);

    }
    public void deleteCallSheetItems(String csCode) {
        String where = "cmpcode LIKE '" + csCode +"'";
        db.delete(DesContract.Competitors.TABLE_NAME2, where, null);
    }
    public Integer getLastCompetitorTransaction(String ccode){
        SalesCall ccall;
        int count = 0;
        String OrderBy = "_id DESC";
        String Limit = "1";
        String where = "ccode = '"+ccode+"' AND strftime('%Y-%m-%d',s."+DesContract.Competitors.DATE+",'unixepoch','localtime') = strftime('%Y-%m-%d', 'now','localtime') AND s.status =1";
        String sql =  " SELECT *from "+DesContract.Competitors.TABLE_NAME +" s "
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

    public long addcatsubcat(String description, int opt) {// ro => record object
        ContentValues cv = new ContentValues();
        int _id = 0;

        if(opt == 1){
            Cursor c = db.rawQuery("SELECT category_id FROM cmpicategory ORDER BY category_id DESC LIMIT 1",null);
            if(c.moveToFirst()) _id = c.getInt(0);
            _id = _id+1;
            cv.put("category_id", _id);
            cv.put("category",description);
            db.insert("cmpicategory",null,cv);
            return _id;
        }else{
            Cursor c = db.rawQuery("SELECT subcategory_id FROM cmpisubcategory ORDER BY subcategory_id DESC LIMIT 1",null);
            if(c.moveToFirst()) _id = c.getInt(0);
            _id = _id+1;
            cv.put("subcategory_id", _id);
            cv.put("subcategory",description);
            db.insert("cmpisubcategory",null,cv);
            return _id;
        }
    }

    public ArrayList<String> getVariants(int subcatid){
        ArrayList<String> variants = new ArrayList<>();
        variants.add("Select Variant");
        Cursor c = db.rawQuery("SELECT t2.category FROM citem t1 LEFT JOIN cmpicategory t2 ON(t1.category_id = t2.category_id) " +
                "WHERE t1.subcategory_id='"+subcatid+"'",null);
        for(int i=0;i<c.getCount();i++){
            if(c.moveToPosition(i)) variants.add(c.getString(0));
        }
        return variants;
    }


}
