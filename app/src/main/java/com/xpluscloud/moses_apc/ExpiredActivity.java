package com.xpluscloud.moses_apc;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

public class ExpiredActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.expired);
      
	 }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
	    	finish();
	        return true; 
	    } 
	    return super.onKeyDown(keyCode, event); 
	} 
	
	@Override
    protected void onStop(){
       super.onStop();
    }
	
		
}



