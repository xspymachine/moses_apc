package com.xpluscloud.mosesshell_davao.dbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xpluscloud.mosesshell_davao.getset.Survey;
import com.xpluscloud.mosesshell_davao.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

import static com.xpluscloud.mosesshell_davao.util.StringUtil.randomText;

/**
 * Created by Shirwen on 8/18/2017.
 */

public class SurveyDbManager extends DbManager {
    public SurveyDbManager(Context context) {
        super(context);
    }

    public void addAllItems(String ccode, String devId){
        String selectWhere = "(SELECT "+DesContract.SurveyData.ICODE+" FROM "+DesContract.SurveyData.TABLE_NAME+" where "+
                DesContract.SurveyData.CCODE+"='"+ccode+"' AND "+
                DesContract.SurveyData.DATETIME+" LIKE '%"+ DateUtil.strDate(System.currentTimeMillis())+"%')";
        String where = " where "+DesContract.CustItemList.ITEMCODE+" NOT IN "+selectWhere;
        String sql = "SELECT *FROM "+DesContract.CustItemList.TABLE_NAME+where;
        String strDatetime = DateUtil.strDateTime(System.currentTimeMillis());
        Cursor c = db.rawQuery(sql, null);
        Log.e("sql", sql+"-"+c.getCount());
        for(int i = 0; i < c.getCount(); i++) {
            if(c.moveToPosition(i)) {
                Survey cp = new Survey();

                cp.setCcode(ccode);
                cp.setItemcode(c.getString(c.getColumnIndex(DesContract.CustItem.ICODE)));
                cp.setDescription(c.getString(c.getColumnIndex(DesContract.CustItem.DESCRIPTION)));
                cp.setScode(get_Code(devId));
                cp.setDatetime(strDatetime);

                AddItem(cp);
            }
        }
        c.close();
    }

    public long AddItem(Survey p) {
        ContentValues cv = new ContentValues();

        cv.put(DesContract.SurveyData.SCODE		    , p.getScode());
        cv.put(DesContract.SurveyData.CCODE		    , p.getCcode());
        cv.put(DesContract.SurveyData.ICODE		    , p.getItemcode());
        cv.put(DesContract.SurveyData.DESCRIPTION	, p.getDescription());
        cv.put(DesContract.SurveyData.FREQ		    , p.getFrequency());
        cv.put(DesContract.SurveyData.SOURCE		, p.getSource());
        cv.put(DesContract.SurveyData.PROMO 		, p.getPromo());
        cv.put(DesContract.SurveyData.STATUS		, p.getStatus());
        cv.put(DesContract.SurveyData.DATETIME	    , p.getDatetime());


        //return db.insert(DesContract.Collection.TABLE_NAME, null, cv);

        return db.insertWithOnConflict(DesContract.SurveyData.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public String get_Code(String devId){
        int lastId = 0;

        String sql = "SELECT _id FROM "+DesContract.SurveyData.TABLE_NAME+" ORDER BY _id DESC LIMIT 1";
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
        String sql = "SELECT remarks FROM survey WHERE ccode = '"+ccode+"' AND datetime LIKE '%"+
                DateUtil.strDate(System.currentTimeMillis())+"%'  LIMIT 1";
        Cursor c = db.rawQuery(sql, null);

        if(c.moveToFirst()) remarks = c.getString(0);

        return remarks;
    }

    public List<Survey> getList(String ccode) {
        List<Survey> list = new ArrayList<>();

        String where = " where "+DesContract.SurveyData.CCODE+" = '"+ccode+"' AND "+
                DesContract.SurveyData.DATETIME+" LIKE '%"+ DateUtil.strDate(System.currentTimeMillis())+"%'" +
                " ORDER BY "+DesContract.SurveyData.DESCRIPTION+" ASC";
        String sql = "SELECT *FROM "+DesContract.SurveyData.TABLE_NAME+where;

        Cursor c = db.rawQuery(sql, null);
        Log.e("sql", sql);
        for(int i=0; i<c.getCount(); i++){
            if(c.moveToPosition(i)) {
                Survey item = setList(c);
                list.add(item);
            }
        }

        c.close();
        return list;
    }

    private Survey setList(Cursor c){

        Survey cp = new Survey();

        cp.setId(c.getInt(c.getColumnIndex(DesContract.SurveyData._ID)));
        cp.setScode(c.getString(c.getColumnIndex(DesContract.SurveyData.SCODE)));
        cp.setCcode(c.getString(c.getColumnIndex(DesContract.SurveyData.CCODE)));
        cp.setItemcode(c.getString(c.getColumnIndex(DesContract.SurveyData.ICODE)));
        cp.setDescription(c.getString(c.getColumnIndex(DesContract.SurveyData.DESCRIPTION)));
        cp.setFrequency(c.getString(c.getColumnIndex(DesContract.SurveyData.FREQ)));
        cp.setSource(c.getString(c.getColumnIndex(DesContract.SurveyData.SOURCE)));
        cp.setPromo(c.getString(c.getColumnIndex(DesContract.SurveyData.PROMO)));
        cp.setStatus(c.getInt(c.getColumnIndex(DesContract.SurveyData.STATUS)));
        cp.setRemarks(c.getString(c.getColumnIndex(DesContract.SurveyData.REMARKS)));
        cp.setDatetime(c.getString(c.getColumnIndex(DesContract.SurveyData.DATETIME)));

        return cp;
    }

    public void updateTypes(String code, String strValue, int type){

        if(!strValue.contains("+")){
            ContentValues cv = new ContentValues();
            cv.put(DesContract.RetailData.STATUS, 0);
            Log.e("value",strValue);
            switch(type){
                case 1:
                    strValue = strValue.equals("") ? "0" : strValue.trim();
                    cv.put(DesContract.SurveyData.FREQ, strValue);
                    break;
                case 2: cv.put(DesContract.SurveyData.SOURCE, strValue);
                    break;
                case 3: cv.put(DesContract.SurveyData.PROMO, strValue);
                    break;
            }

            db.update(DesContract.SurveyData.TABLE_NAME, cv, DesContract.SurveyData.SCODE+"='"+code+"'", null);

        }
    }

    public List<Survey> getPending(String ccode) {
        List<Survey> list = new ArrayList<>();

        String where = " where "+DesContract.SurveyData.CCODE+" = '"+ccode+"' AND (status = 0) ORDER BY _id DESC";//stats=2 for new added
        String sql = "SELECT *FROM "+DesContract.SurveyData.TABLE_NAME+where;

        Cursor c = db.rawQuery(sql, null);

        for(int i=0; i<c.getCount(); i++){
            if(c.moveToPosition(i)) {
                Survey item = setList(c);
                list.add(item);
            }
        }

        c.close();
        return list;
    }

    public void updateRemarks(String code, String remarks){
        ContentValues cv = new ContentValues();
        cv.put(DesContract.SurveyData.REMARKS, remarks);
        cv.put(DesContract.SurveyData.STATUS, 1);

        db.update(DesContract.SurveyData.TABLE_NAME, cv, DesContract.SurveyData.SCODE+"='"+code+"'", null);

    }

}
