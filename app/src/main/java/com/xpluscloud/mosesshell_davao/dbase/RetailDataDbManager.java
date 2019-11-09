package com.xpluscloud.mosesshell_davao.dbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xpluscloud.mosesshell_davao.getset.Retail;
import com.xpluscloud.mosesshell_davao.util.DateUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.xpluscloud.mosesshell_davao.util.StringUtil.randomText;

/**
 * Created by Shirwen on 8/14/2017.
 */

public class RetailDataDbManager extends DbManager {
    public RetailDataDbManager(Context context) {
        super(context);
    }

    public List<Retail> getList(String ccode) {
        List<Retail> list = new ArrayList<Retail>();

        String where = " where "+DesContract.RetailData.CCODE+" = '"+ccode+"' AND "+
                DesContract.RetailData.DATETIME+" LIKE '%"+ DateUtil.strDate(System.currentTimeMillis())+"%'" +
                " ORDER BY "+DesContract.RetailData.DESCRIPTION+" ASC";
        String sql = "SELECT *FROM "+DesContract.RetailData.TABLE_NAME+where;

        Cursor c = db.rawQuery(sql, null);
        Log.e("sql", sql);
        for(int i=0; i<c.getCount(); i++){
            if(c.moveToPosition(i)) {
                Retail item = setList(c);
                list.add(item);
            }
        }

        c.close();
        return list;
    }

    private Retail setList(Cursor c){

        Retail cp = new Retail();

        cp.setId(c.getInt(c.getColumnIndex(DesContract.RetailData._ID)));
        cp.setComcode(c.getString(c.getColumnIndex(DesContract.RetailData.COMCODE)));
        cp.setCcode(c.getString(c.getColumnIndex(DesContract.RetailData.CCODE)));
        cp.setItemcode(c.getString(c.getColumnIndex(DesContract.RetailData.ICODE)));
        cp.setDescription(c.getString(c.getColumnIndex(DesContract.RetailData.DESCRIPTION)));
        cp.setPrice(c.getDouble(c.getColumnIndex(DesContract.RetailData.PRICE)));
        cp.setIoh(c.getInt(c.getColumnIndex(DesContract.RetailData.IOH)));
        cp.setStatus(c.getInt(c.getColumnIndex(DesContract.RetailData.STATUS)));
        cp.setPromo(c.getString(c.getColumnIndex(DesContract.RetailData.PROMO)));
        cp.setRemarks(c.getString(c.getColumnIndex(DesContract.RetailData.REMARKS)));
        cp.setDatetime(c.getString(c.getColumnIndex(DesContract.RetailData.DATETIME)));
        cp.setSOS(c.getDouble(c.getColumnIndex(DesContract.RetailData.SOS)));
        cp.setPlanogram(c.getDouble(c.getColumnIndex(DesContract.RetailData.PLANOGRAM)));

        return cp;
    }

    public void updateTypes(String code, String strValue, int type){

        if(!strValue.contains("+")){
            ContentValues cv = new ContentValues();
            cv.put(DesContract.RetailData.STATUS, 0);
            Log.e("value",strValue);

            DecimalFormat df = new DecimalFormat("#.00");
            switch(type){
                case 1:
                    strValue = strValue.equals("") ? "0" : strValue.trim();
                    cv.put(DesContract.RetailData.IOH, strValue);
                    break;
                case 2:
                    strValue = strValue.equals("") ? "0.00" : strValue.trim();
                    df.format(Double.parseDouble(strValue));
                    cv.put(DesContract.RetailData.PRICE, strValue);
                    break;
                case 3: cv.put(DesContract.RetailData.PROMO, strValue);
                    break;
                case 4:
                    strValue = strValue.equals("") ? "0.00" : strValue.trim();
                    df.format(Double.parseDouble(strValue));
                    cv.put(DesContract.RetailData.SOS, strValue);
                    break;
                case 5:
                    strValue = strValue.equals("") ? "0.00" : strValue.trim();
                    df.format(Double.parseDouble(strValue));
                    cv.put(DesContract.RetailData.PLANOGRAM, strValue);
                    break;
            }

            db.update(DesContract.RetailData.TABLE_NAME, cv, DesContract.RetailData.COMCODE+"='"+code+"'", null);

        }
    }

    public String getDataValue(String code, String colName){
        String value = "";
        String sql = "SELECT "+colName+" FROM retail WHERE comcode='"+code+"'";
        Cursor c = db.rawQuery(sql,null);
        if(c.moveToFirst()) value = c.getString(0);
        return value;
    }

    public List<Retail> getPending(String ccode) {
        List<Retail> list = new ArrayList<Retail>();

        String where = " where "+DesContract.RetailData.CCODE+" = '"+ccode+"' AND (status = 0) ORDER BY _id DESC";//stats=2 for new added
        String sql = "SELECT *FROM "+DesContract.RetailData.TABLE_NAME+where;

        Cursor c = db.rawQuery(sql, null);

        for(int i=0; i<c.getCount(); i++){
            if(c.moveToPosition(i)) {
                Retail item = setList(c);
                list.add(item);
            }
        }

        c.close();
        return list;
    }

    public void updateRemarks(String code, String remarks){
        ContentValues cv = new ContentValues();
        cv.put(DesContract.RetailData.REMARKS, remarks);
        cv.put(DesContract.RetailData.STATUS, 1);

        db.update(DesContract.RetailData.TABLE_NAME, cv, DesContract.RetailData.COMCODE+"='"+code+"'", null);

    }

    public void addAllItems(String ccode, String devId, String selected){
        String selectWhere = "(SELECT "+DesContract.RetailData.ICODE+" FROM "+DesContract.RetailData.TABLE_NAME+" where "+
                DesContract.RetailData.CCODE+"='"+ccode+"' AND "+
                DesContract.RetailData.DATETIME+" LIKE '%"+DateUtil.strDate(System.currentTimeMillis())+"%')";
//        String where = " where groupid = '"+selected.toLowerCase()+"' AND "+DesContract.CustItemList.ITEMCODE+" NOT IN "+selectWhere;
        String where = " where "+DesContract.CustItemList.ITEMCODE+" NOT IN "+selectWhere;
        String sql = "SELECT *FROM "+DesContract.CustItemList.TABLE_NAME+where;

        Cursor c = db.rawQuery(sql, null);
        Log.e("sql", sql+"-"+c.getCount());
        String strdatetime = DateUtil.strDateTime(System.currentTimeMillis());
        for(int i = 0; i < c.getCount(); i++) {
            if(c.moveToPosition(i)) {
                Retail cp = new Retail();

                cp.setCcode(ccode);
                cp.setItemcode(c.getString(c.getColumnIndex(DesContract.CustItem.ICODE)));
                cp.setDescription(c.getString(c.getColumnIndex(DesContract.CustItem.DESCRIPTION)));
                cp.setComcode(get_Code(devId));
                cp.setDatetime(strdatetime);

                AddItem(cp);
            }
        }
        c.close();
    }

    public long AddItem(Retail p) {
        ContentValues cv = new ContentValues();

        cv.put(DesContract.RetailData.COMCODE		, p.getComcode());
        cv.put(DesContract.RetailData.CCODE		    , p.getCcode());
        cv.put(DesContract.RetailData.ICODE		    , p.getItemcode());
        cv.put(DesContract.RetailData.DESCRIPTION	, p.getDescription());
        cv.put(DesContract.RetailData.PRICE		    , p.getPrice());
        cv.put(DesContract.RetailData.PROMO		    , p.getPromo());
        cv.put(DesContract.RetailData.IOH			, p.getIoh());
        cv.put(DesContract.RetailData.STATUS		, p.getStatus());
        cv.put(DesContract.RetailData.DATETIME	    , p.getDatetime());


        //return db.insert(DesContract.Collection.TABLE_NAME, null, cv);

        return db.insertWithOnConflict(DesContract.RetailData.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public String get_Code(String devId){
        int lastId = 0;

        String sql = "SELECT _id FROM "+DesContract.RetailData.TABLE_NAME+" ORDER BY _id DESC LIMIT 1";
        Cursor c = db.rawQuery(sql,null);
        if (c != null && c.moveToFirst()) {
            lastId = c.getInt(0); //The 0 is the column index, we only have 1 column, so the index is 0
        }

        String strCode = ("00000" + (lastId+1)).substring(String.valueOf(lastId).length());
        String strRandom = randomText(10);
        strCode = devId.substring(devId.length() - 5) + strRandom + strCode ;

        return strCode;
    }

    public String getRemarks(String ccode){
        String remarks = "";
        String sql = "SELECT remarks FROM retail WHERE ccode = '"+ccode+"' AND datetime LIKE '%"+
                DateUtil.strDate(System.currentTimeMillis())+"%'  LIMIT 1";
        Cursor c = db.rawQuery(sql, null);

        if(c.moveToFirst()) remarks = c.getString(0);

        return remarks;
    }
}
