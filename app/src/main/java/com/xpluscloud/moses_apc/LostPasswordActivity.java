package com.xpluscloud.moses_apc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.xpluscloud.moses_apc.dbase.SettingDbManager;
import com.xpluscloud.moses_apc.util.DateUtil;
import com.xpluscloud.moses_apc.util.Master;
import com.xpluscloud.moses_apc.util.PasswordUtil;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LostPasswordActivity extends Activity {
	
	public SharedPreferences settings;
	Context context;
	
	EditText newPass;
	EditText conPass;
	EditText newHint;
	CheckBox newCheck;
	Button btSave, btCancel;
	
	String devId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lost_password);
		context = LostPasswordActivity.this;
		
		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//		devId = telephonyManager.getDeviceId();
		devId = Master.getDevId2(context);
		newPass = (EditText) findViewById(R.id.etNewPass);
		conPass = (EditText) findViewById(R.id.etConPass);
		newHint = (EditText) findViewById(R.id.etNewHint);
		newCheck = (CheckBox) findViewById(R.id.passCheck);
		newCheck.setText("Show Password");
		
		btSave = (Button) findViewById(R.id.btSave);
		btSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("LostPassword", "save");
				saveNewPass();
			}
		});
		
		btCancel = (Button) findViewById(R.id.btCancel);
		btCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		newCheck.setOnClickListener(new OnClickListener() {

  	      @Override
  	      public void onClick(View v) {
  	                //is chkIos checked?
  	        if (((CheckBox) v).isChecked()) {
  	                         //Case 1   
  	        	newPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
  	        	conPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
  	        }
  	        else{ 
  	        	newPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
  	        	conPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
  	          //case 2
  	        }

  	      }
  	    });
	}
	
	private void saveNewPass(){
		try{
			if(!checkPassword(newPass.getText().toString())){
				Toast.makeText( getApplicationContext(), "Password strength is weak,\n" +
						"Please provide 8 characters with uppercase,lowercase,character and numerics."  , Toast.LENGTH_LONG ).show();
				newPass.requestFocus();
				newPass.setText("");
				return;
			}
			
			if(!conPass.getText().toString().equals(newPass.getText().toString())){
				Toast.makeText( getApplicationContext(), "Password mismatch"  , Toast.LENGTH_LONG ).show();
				conPass.requestFocus();
				conPass.setText("");
				return;
			}		
			
			if(newHint.getText().toString().equals("")){
    			Toast.makeText( getApplicationContext(), "Please provide a Password Hint"  , Toast.LENGTH_LONG ).show();
    			newHint.requestFocus();
    			return;
    		}
			
			SettingDbManager db = new SettingDbManager(context);
			db.open();    		
			
			if(db.getLastFour(newPass.getText().toString().substring(newPass.getText().toString().length()-4))){
				Toast.makeText( getApplicationContext(), "Password strength is weak,\n"+
							"Your last 4 characters is the same as your past password please provide another password.", Toast.LENGTH_LONG ).show();
				newPass.requestFocus();
				newPass.setText("");
				conPass.setText("");
				newHint.setText("");
				db.close();
				return;
			}
			
			db.updatePassword();
			try {
				db.setKeyValue("password", PasswordUtil.SHA1(newPass.getText().toString()), 1);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			db.setKeyValue("lastFour", newPass.getText().toString().substring(newPass.getText().toString().length()-4), 1);
			db.setKeyValue("hint",newHint.getText().toString(),1);
			db.close();
			
			setLogin();
			runMain();
			finish();
			
		}catch(Exception e){
			Log.e("Exception Error: ","LostPassword exception!");
			e.printStackTrace();
		}
	}
	
	private void setLogin(){		
		String sysDate = DateUtil.strDate(System.currentTimeMillis());
		
		settings = getSharedPreferences("MosesSettings", Context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = settings.edit();
		prefEditor.putInt("isLogin", 1);
		prefEditor.putString("logDate", sysDate);
		prefEditor.commit();	
	}
	
	private void runMain() {
		
		Intent intent= new Intent(context, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent); 
		
		finish();
	}
	
private Boolean checkPassword(String password){
    	
    	final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20})";
    	Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
    	Matcher matcher = pattern.matcher(password);
    	 	    	
    	return matcher.matches();
    }   

}
