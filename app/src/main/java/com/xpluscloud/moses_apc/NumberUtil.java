package com.xpluscloud.moses_apc;


import android.content.Context;

import com.xpluscloud.moses_apc.dbase.OutboxDbManager;

public final class NumberUtil {  

  /** 
   * Tag used on log messages. 
   */
    static final String TAG = "Number Util";
	static void updateStatus(Context context, Integer id, Integer status) {
		  	OutboxDbManager db = new OutboxDbManager(context);
			db.open();
			db.updateStatus(id, status);		
			db.close();
	}
  

}
       