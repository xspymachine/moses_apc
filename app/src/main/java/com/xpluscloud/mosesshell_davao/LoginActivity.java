package com.xpluscloud.mosesshell_davao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xpluscloud.mosesshell_davao.dbase.SettingDbManager;
import com.xpluscloud.mosesshell_davao.util.DateUtil;
import com.xpluscloud.mosesshell_davao.util.DbUtil;
import com.xpluscloud.mosesshell_davao.util.LayoutUtil;
import com.xpluscloud.mosesshell_davao.util.PasswordUtil;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends Activity {
	public static final String SETTINGS = "MosesSettings";
	public SharedPreferences settings;
	
	Context context;
	
	EditText password;
	Button login;
	TextView hint;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login2);


		
		context = LoginActivity.this;
		   
		settings = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
//		int isLogin = settings.getInt("isLogin", 0);
//		logDate = settings.getString("logDate", "");		
//		
//		if (isLogin == 1 && logDate.equalsIgnoreCase(sysDate)) runMain();
		
		password = (EditText) findViewById(R.id.etPassword);
		login = (Button) findViewById(R.id.btLogin);
		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("LoginActivity", "login Button Click");
				if(checkLogin()){
				setLogin();
				runMain();
//				finish();
				}
				else if(password.getText().toString().equals("9758787225464")){ //xplustracking (keypad number equivalent)
					lostPass();															 	
					finish();
				}
				else {
//					Toast.makeText( getApplicationContext(),   ,Toast.LENGTH_LONG ).show();
					DbUtil.makeToast(LayoutInflater.from(context), "Password Error", context,
							(ViewGroup) findViewById(R.id.custom_toast_layout),1);
					password.requestFocus();
					password.setText("");
				}
			}
		});
		password.setTransformationMethod(new AsteriskPasswordTransformationMethod());
		
		hint = (TextView) findViewById(R.id.btHint);
		hint.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SettingDbManager db = new SettingDbManager(context);
	    		db.open();
	    		String hint = db.getSetting("hint");
	    		db.close();
//	    		Toast.makeText( getApplicationContext(), "Password Hint: "+hint  ,Toast.LENGTH_LONG ).show();	    		
	    		DbUtil.makeToast(LayoutInflater.from(context), "Password Hint: "+hint, context,
	    				(ViewGroup) findViewById(R.id.custom_toast_layout),1);
			}
		});
		ImageView iv = (ImageView) findViewById(R.id.imagetesting101);
        LayoutUtil.setListenerToRootView(LoginActivity.this,context,iv);
	}

    private void lostPass(){
		Intent i = new Intent(this, LostPasswordActivity.class);
		startActivity(i);
		finish();
	}
	
	private Boolean checkLogin(){
		Boolean check = false;
		
		SettingDbManager db = new SettingDbManager(context);
		db.open();
		String userPass = ""+db.getSetting("password");
		db.close();
		
		String hashPass = "";
		try {
			hashPass = PasswordUtil.SHA1(password.getText().toString());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.e("hashPass", hashPass);
		Log.e("userPass", userPass);
		
		if(userPass != null && hashPass.equals(userPass)) check = true;
		
		return check;
	}
	
	private void setLogin(){		
		String sysDate = DateUtil.strDate(System.currentTimeMillis());
		
		SharedPreferences.Editor prefEditor = settings.edit();
		prefEditor.putInt("isLogin", 1);
		prefEditor.putString("logDate", sysDate);
		prefEditor.commit();	
	}
	
	private void runMain() {
		
		Intent intent= new Intent(context, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);

		finish();
	}
	
	@Override
    public void onBackPressed() { 
    	
    		Intent backtoHome = new Intent(Intent.ACTION_MAIN);
    		backtoHome.addCategory(Intent.CATEGORY_HOME);
    		backtoHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		startActivity(backtoHome);

    		finish();
    } 
	
	public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
		public CharSequence getTransformation(CharSequence source, View view) {
		    return new PasswordCharSequence(source);
		}

		private class PasswordCharSequence implements CharSequence {
		    private CharSequence mSource;
		    public PasswordCharSequence(CharSequence source) {
		        mSource = source; // Store char sequence
		    }
		    public char charAt(int index) {
		        return '*'; // This is the important part
		    }
		    public int length() {
		        return mSource.length(); // Return default
		    }
		    public CharSequence subSequence(int start, int end) {
		        return mSource.subSequence(start, end); // Return default
		    }
		}
	}


}
