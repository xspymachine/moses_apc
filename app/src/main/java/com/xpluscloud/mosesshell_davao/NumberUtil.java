package com.xpluscloud.mosesshell_davao;


import android.content.Context;

import com.xpluscloud.mosesshell_davao.dbase.OutboxDbManager;

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
       