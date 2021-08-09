package com.xpluscloud.moses_apc.dbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.xpluscloud.moses_apc.getset.Issue;
import com.xpluscloud.moses_apc.getset.MyList;
import com.xpluscloud.moses_apc.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shirwen on 11/9/2016.
 */

public class IssueDbManager extends DbManager {
    public IssueDbManager(Context context) {
        super(context);
    }

    public long createIssue(Issue i){
        ContentValues cv = new ContentValues();
        cv.put(DesContract.CustomerIssues.CODEISSUE , i.getCode_issue());
        cv.put(DesContract.CustomerIssues.CCODE     , i.getCcode());
        cv.put(DesContract.CustomerIssues.ISSUE     , i.getIssue());
        cv.put(DesContract.CustomerIssues.DATEOPEN  , i.getDate_open());
        cv.put(DesContract.CustomerIssues.DATECLOSE , "");
        cv.put(DesContract.CustomerIssues.STATUS    , i.getStatus());

        return db.insertWithOnConflict(DesContract.CustomerIssues.TABLE_NAME, null,cv, SQLiteDatabase.CONFLICT_REPLACE);

    }

    public int getLastId(){
        int lastId = 0;

        String sql = "SELECT _id FROM "+ DesContract.CustomerIssues.TABLE_NAME+" ORDER BY _id DESC LIMIT 1";
        Cursor c = db.rawQuery(sql,null);
        if (c != null && c.moveToFirst()) {
            lastId = c.getInt(0); //The 0 is the column index, we only have 1 column, so the index is 0
        }
        c.close();

//        Log.e("issueid",""+lastId);

        return lastId;
    }

    public Issue getOpenIssue(String ccode, String code_issue){
        Issue info;
        String where_code = "";
        if(!code_issue.isEmpty()) where_code = " AND "+DesContract.CustomerIssues.CODEISSUE+"='"+code_issue+"'";

        String sql = "SELECT * FROM "
                     + DesContract.CustomerIssues.TABLE_NAME
                     + " WHERE "
                     + DesContract.CustomerIssues.STATUS + " = " + "0"
                     + " AND "+DesContract.CustomerIssues.CCODE + " = '" + ccode + "'"+where_code;

        Cursor c = db.rawQuery(sql,null);

//        Log.e("count issues", "count "+c.getCount());

        if(c.moveToFirst()) info = getCustomerIssueInfo(c);
        else info = null;

        c.close();
        return info;
    }

    public Issue getCustomerIssue(String code_issue){
        Issue info;
        String sql = "SELECT * FROM "
                + DesContract.CustomerIssues.TABLE_NAME
                + " WHERE "
                + DesContract.CustomerIssues.CODEISSUE + " = '" + code_issue + "'";

        Cursor c = db.rawQuery(sql,null);

        if(c.moveToFirst()) info = getCustomerIssueInfo(c);
        else info = null;

        c.close();
        return info;
    }

    public List<MyList> getAllOpenIssue(String ccode){
        List<MyList> list = new ArrayList<MyList>();
        String sql = "SELECT "
                + "ci."+DesContract.CustomerIssues._ID   + ","
                + DesContract.CustomerIssues.DATEOPEN   + ","
                + DesContract.CustomerIssues.DATECLOSE  + ","
                + "ci."+DesContract.CustomerIssues.CCODE      + ","
                + DesContract.Customer.NAME             + ","
                + DesContract.Customer.ADDRESS          + ","
                + DesContract.CustomerIssues.ISSUE      + ","
                + DesContract.CustomerIssues.CODEISSUE
                + " FROM "
                + DesContract.CustomerIssues.TABLE_NAME
                + " ci LEFT JOIN " + DesContract.Customer.TABLE_NAME + " c ON (c.ccode = ci.ccode) "
                + " WHERE "
                + "ci."+DesContract.CustomerIssues.CCODE + " = '" + ccode + "' ORDER BY ci._id DESC";

        Cursor c = db.rawQuery(sql,null);

        if (c != null && c.moveToFirst()) {
            for(int i = 0; i < c.getCount(); i++) {
                if(c.moveToPosition(i)) {
                    MyList item = createMyList(c,i);
                    list.add(item);
                }
            }
        }

        c.close();
        return list;
    }

    private MyList createMyList(Cursor c, int i) {
        MyList list = new MyList();
        String date1 = c.getString(1).isEmpty() ? "" : ""+ Long.parseLong(c.getString(1));
        String date2 = c.getString(2).isEmpty() ? "" : ""+ Long.parseLong(c.getString(2));

        String open = date1.isEmpty() ? "" :  DateUtil.phLongDate(Long.parseLong(date1));
        String close = date2.isEmpty() ? "" :  DateUtil.phLongDate(Long.parseLong(date2));

        list.setId(i+1);
        list.setDateTime("Opened: "+ open+"\nClosed: "+ close);
        list.setCustomerCode(c.getString(3));
        list.setCustomerName(c.getString(4));
        list.setAddress(c.getString(5));
        list.setTransaction(c.getString(6));
        list.setTransCode(c.getString(7));
        return list;

    }

    private Issue getCustomerIssueInfo(Cursor c) {
        Issue i = new Issue();

        i.setId(c.getInt(
                c.getColumnIndex(BaseColumns._ID)));
        i.setCode_issue(c.getString(
                c.getColumnIndex(DesContract.CustomerIssues.CODEISSUE)));
        i.setCcode(c.getString(
                c.getColumnIndex(DesContract.CustomerIssues.CCODE)));
        i.setIssue(c.getString(
                c.getColumnIndex(DesContract.CustomerIssues.ISSUE)));
        i.setDate_open(c.getString(
                c.getColumnIndex(DesContract.CustomerIssues.DATEOPEN)));
        i.setDate_close(c.getString(
                c.getColumnIndex(DesContract.CustomerIssues.DATECLOSE)));
        i.setStatus(c.getInt(
                c.getColumnIndex(DesContract.CustomerIssues.STATUS)));

        return i;
    }

    public void updateCloseIssue(Issue i){
        ContentValues cv = new ContentValues();
        cv.put(DesContract.CustomerIssues.DATECLOSE, System.currentTimeMillis());
        cv.put(DesContract.CustomerIssues.STATUS   , 1);
        cv.put(DesContract.CustomerIssues.ISSUE    , i.getIssue());

        String where = DesContract.CustomerIssues.CODEISSUE + " = '" + i.getCode_issue() + "'";

        db.update(DesContract.CustomerIssues.TABLE_NAME,cv,where,null);
    }

}
