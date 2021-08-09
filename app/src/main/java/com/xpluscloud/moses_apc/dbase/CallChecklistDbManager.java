package com.xpluscloud.moses_apc.dbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xpluscloud.moses_apc.getset.CustomerCallChecklist;
import com.xpluscloud.moses_apc.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shirwen on 10/28/2016.
 */

public class CallChecklistDbManager extends DbManager{
    public CallChecklistDbManager(Context context) {
        super(context);
    }

    private void AddItem(CustomerCallChecklist cc){
        ContentValues cv = new ContentValues();
        cv.put(DesContract.CustomerCallChecklist.CCODE, cc.getCcode());
        cv.put(DesContract.CustomerCallChecklist.CHKID, cc.getChkid());
        cv.put(DesContract.CustomerCallChecklist.YES,   cc.getChkYes());
        cv.put(DesContract.CustomerCallChecklist.NO,    cc.getChkNo());
        cv.put(DesContract.CustomerCallChecklist.DATE,  cc.getDate());
        cv.put(DesContract.CustomerCallChecklist.TIME,  cc.getTime());

        db.insertWithOnConflict(DesContract.CustomerCallChecklist.TABLE_NAME, null,cv, SQLiteDatabase.CONFLICT_IGNORE);
    }


    public ArrayList<String> getChecklistName(String column, String where){
        ArrayList<String> categories = new ArrayList<String>();
        String sql = "SELECT DISTINCT "+column+" FROM call_checklist "+where;
        Cursor c = db.rawQuery(sql,null);
        Log.e("sql1", sql+"-"+c.getCount());
        if(c != null && c.moveToFirst()){
            for(int i = 0; i<c.getCount();i++){
                if(c.moveToPosition(i)){
                    categories.add(c.getString(0));
                }
            }
        }

        c.close();
        return categories;
    }

    public List<CustomerCallChecklist> getCustomerCallCheckList(String ccode, String category) {
        List<CustomerCallChecklist> list = new ArrayList<CustomerCallChecklist>();

        String sql = "SELECT " +
                     "cuschk._id, " +
                     "cuschk.chkl_id, " +
                     "cuschk.chk_yes," +
                     "cuschk.chk_no," +
                     "cuschk.ccode," +
                     "cuschk.gremarks," +
                     "cuschk.date,"+
                     "cuschk.status,"+
                     "chk.description " +
                     " FROM ccall_checklist cuschk" +
                     " LEFT JOIN call_checklist chk ON(cuschk.chkl_id = chk.chkl_id)" +
                     " WHERE category = '"+category+"' AND ccode='"+ccode+"'" +
                     " AND date='"+ DateUtil.strDate(System.currentTimeMillis())+"'";

        Cursor c = db.rawQuery(sql, null);

        for(int i=0; i<c.getCount(); i++){
            if(c.moveToPosition(i)) {
                CustomerCallChecklist item = setList(c);
                list.add(item);
            }
        }
        Log.e("sql", sql+"--"+c.getCount());
        c.close();
        return list;
    }

    public List<CustomerCallChecklist> getCustomerCallCheckListToSubmit(String ccode) {
        List<CustomerCallChecklist> list = new ArrayList<CustomerCallChecklist>();

        String sql = "SELECT " +
                "cuschk._id, " +
                "cuschk.chkl_id, " +
                "cuschk.chk_yes," +
                "cuschk.chk_no," +
                "cuschk.ccode," +
                "cuschk.gremarks," +
                "cuschk.date,"+
                "cuschk.status,"+
                "chk.description " +
                " FROM ccall_checklist cuschk" +
                " LEFT JOIN call_checklist chk ON(cuschk.chkl_id = chk.chkl_id)" +
                " WHERE ccode='"+ccode+"'" +
                " AND date='"+ DateUtil.strDate(System.currentTimeMillis())+"'" +
                " ORDER BY cuschk._id ASC";

        Cursor c = db.rawQuery(sql, null);
        Log.e("sql", sql);
        for(int i=0; i<c.getCount(); i++){
            if(c.moveToPosition(i)) {
                CustomerCallChecklist item = setList(c);
                list.add(item);
            }
        }

        c.close();
        return list;
    }

    private CustomerCallChecklist setList(Cursor c){
        CustomerCallChecklist cCheck = new CustomerCallChecklist();

        cCheck.set_id(c.getInt(c.getColumnIndex(DesContract.CustomerCallChecklist._ID)));
        cCheck.setChkid(c.getInt(c.getColumnIndex(DesContract.CustomerCallChecklist.CHKID)));
        cCheck.setChkYes(c.getInt(c.getColumnIndex(DesContract.CustomerCallChecklist.YES)));
        cCheck.setChkNo(c.getInt(c.getColumnIndex(DesContract.CustomerCallChecklist.NO)));
        cCheck.setCcode(c.getString(c.getColumnIndex(DesContract.CustomerCallChecklist.CCODE)));
        cCheck.setGremarks(c.getString(c.getColumnIndex(DesContract.CustomerCallChecklist.GREMARKS)));
        cCheck.setDescription(c.getString(c.getColumnIndex(DesContract.CallChecklist.DESCRIPTION)));
        cCheck.setDate(c.getString(c.getColumnIndex(DesContract.CustomerCallChecklist.DATE)));
        cCheck.setStatus(c.getInt(c.getColumnIndex(DesContract.CustomerCallChecklist.STATUS)));

        return cCheck;
    }

    public void addCallChecklist(String ccode){
        checkListItems(ccode);
        String selectWhere = "(SELECT "+DesContract.CustomerCallChecklist.CHKID+" FROM "+DesContract.CustomerCallChecklist.TABLE_NAME+" where "+
                DesContract.CustomerCallChecklist.CCODE+"='"+ccode+"' AND "+
                DesContract.CustomerCallChecklist.DATE+" LIKE '%"+ DateUtil.strDate(System.currentTimeMillis())+"%')";
        String where = " where "+DesContract.CallChecklist.CHKL_ID+" NOT IN "+selectWhere;
        String sql = "SELECT *FROM "+DesContract.CallChecklist.TABLE_NAME+where;

        Cursor c = db.rawQuery(sql, null);
        Log.e("sql", sql+"-"+c.getCount());
        for(int i = 0; i < c.getCount(); i++) {
            if(c.moveToPosition(i)) {
                CustomerCallChecklist cc = new CustomerCallChecklist();

                cc.setCcode(ccode);
                cc.setChkid(c.getInt(c.getColumnIndex(DesContract.CustomerCallChecklist.CHKID)));
                cc.setDate(DateUtil.strDate(System.currentTimeMillis()));
                cc.setTime(DateUtil.hour12mill(System.currentTimeMillis()));
                AddItem(cc);
            }
        }
        c.close();
    }

    public void setCheck(String ccode, int _id, int check, int opt, String remark){
        String date = DateUtil.strDate(System.currentTimeMillis());
        String where = " WHERE ccode='"+ccode+"' AND date='"+date+"'";

        switch(opt){
            case 1: db.execSQL("UPDATE ccall_checklist SET chk_yes = "+check +" WHERE _id ="+_id);
                    break;
            case 2: db.execSQL("UPDATE ccall_checklist SET chk_no = "+check +" WHERE _id ="+_id);
                    break;
            case 3: db.execSQL("UPDATE ccall_checklist SET gremarks = '"+remark+"' "+where);
                    break;
            case 4: db.execSQL("UPDATE ccall_checklist SET status = 1 WHERE ccode='"+ccode+"' AND date='"+date+"'");
                    break;
        }

    }

    private void checkListItems(String ccode){
//        String sql = "SELECT *FROM ccall_checklist cc" +
//                     " LEFT JOIN call_checklist c ON(cc.chkl_id = c.chkl_id)" +
//                     " WHERE ccode = '"+ccode+"' AND date = '"+DateUtil.strDate(System.currentTimeMillis())+"'";
//
//        Cursor c = db.rawQuery(sql,null);
//        if(c.moveToFirst()){
//            db.execSQL("DELETE FROM ccall_checklist WHERE ccode = '"+ccode+"' AND date = '"+DateUtil.strDate(System.currentTimeMillis())+"'");
//        }

        String sql1 = "SELECT *FROM ccall_checklist WHERE ccode = '"+ccode+"' AND date = '"+DateUtil.strDate(System.currentTimeMillis())+"'";
        String sql2 = "SELECT *FROM call_checklist";

        Cursor c1 = db.rawQuery(sql1,null);
        Cursor c2 = db.rawQuery(sql2,null);

        if(c1.getCount()!= c2.getCount()){
            db.execSQL("DELETE FROM ccall_checklist WHERE ccode = '"+ccode+"' AND date = '"+DateUtil.strDate(System.currentTimeMillis())+"'");
        }
        c1.close();
        c2.close();
    }

}
