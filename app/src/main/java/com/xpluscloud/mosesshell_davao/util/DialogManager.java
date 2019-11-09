package com.xpluscloud.mosesshell_davao.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public final class DialogManager { 
	public static final String TAG = "Notifications";
	
	public static final String DISPLAY_MESSAGE_ACTION =
	           "com.xpluscloud.mpasta.util.DISPLAY_MESSAGE";
		 
	public static final String EXTRA_MESSAGE = "message";

	  /** 
	   * Notifies UI to display a message. 
	   * <p> 
	   * This method is defined in the common helper because it's used both by 
	   * the UI and the background service. 
	   * 
	   * @param context application's context. 
	   * @param message message to be displayed. 
	   */
	public static void displayMessage(Context context, String message) {
	      Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
	      intent.putExtra(EXTRA_MESSAGE, message); 
	      context.sendBroadcast(intent); 
	  }
	
	
    /** 
     * Function to display simple Alert Dialog 
     * @param context - application context 
     * @param title - alert dialog title 
     * @param message - alert message 
     * @param status - success/failure (used to set icon) 
     *               - pass null if you don't want icon 
     * */
   
	public static void showAlertDialog(Context context, String title, String message, Boolean status) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
        .setMessage(message)
        .setCancelable(false)
        .setNegativeButton("Close",new DialogInterface.OnClickListener() {
            @Override
			public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
   
}

