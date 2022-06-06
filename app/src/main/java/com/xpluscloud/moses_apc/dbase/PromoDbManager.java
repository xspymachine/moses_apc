package com.xpluscloud.moses_apc.dbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class PromoDbManager extends DbManager {
    public PromoDbManager(Context context) {
        super(context);
    }

    public void AddCustomerPromo(String ccode, String datetime){

        ContentValues cv = new ContentValues();

        cv.put(DesContract.CustomerPromo.CCODE		,ccode);
        cv.put(DesContract.CustomerPromo.DATETIME	,datetime);
        cv.put(DesContract.CustomerPromo.STATUS		,1);

        db.insert(
                "cus_promo",
                null,
                cv
        );

    }
    public Integer getLastPromoTransaction(String ccode){
//        SalesCall ccall;
        int count = 0;
        String OrderBy = "_id DESC";
        String Limit = "1";
        String where = "ccode = '"+ccode+"' AND strftime('%Y-%m-%d',s."+DesContract.CustomerPromo.DATETIME+",'unixepoch','localtime') = strftime('%Y-%m-%d', 'now','localtime')";
        String sql =  " SELECT *from "+DesContract.CustomerPromo.TABLE_NAME +" s "
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
