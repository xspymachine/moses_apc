package com.xpluscloud.mosesshell_davao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.xpluscloud.mosesshell_davao.dbase.SettingDbManager;
import com.xpluscloud.mosesshell_davao.util.ArrayDef;
import com.xpluscloud.mosesshell_davao.util.DateUtil;
import com.xpluscloud.mosesshell_davao.util.DbUtil;
import com.xpluscloud.mosesshell_davao.util.Master;
import com.xpluscloud.mosesshell_davao.util.PasswordUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends Activity {
	public static final String PREFS_NAME = "RegSettings";
	public SharedPreferences settings;

	private EditText etCompany;
	private EditText etFirstname;
	private EditText etLastname;
	private EditText etMobileno;
	private EditText etEmail;
	private EditText etPass;
	private EditText etCPass;
	private EditText etHint;
//	private CheckBox passCheck;
	private Spinner etType;

	public String devId;
	public String androidId;

	public String regType;

	public boolean registered = false;

	Context context;
	String gateway = Master.INIT_GATEWAY;

	public final String REGISTERED = "Registered";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = RegisterActivity.this;

		Bundle extras = getIntent().getExtras();
		devId = extras.getString("devId");
//        androidId = extras.getString("androidid");
		androidId = Settings.Secure.getString(context.getContentResolver(),
				Settings.Secure.ANDROID_ID);
		register();
	}


	private void register() {
		setContentView(R.layout.register_form);

		etCompany = (EditText) findViewById(R.id.etCompany);
		etFirstname = (EditText) findViewById(R.id.etFirstname);
		etLastname = (EditText) findViewById(R.id.etLastname);
		etMobileno = (EditText) findViewById(R.id.etMobileno);
		etEmail = (EditText) findViewById(R.id.etEmail);
		etPass = (EditText) findViewById(R.id.etPass);
		etCPass = (EditText) findViewById(R.id.etCPass);
		etHint = (EditText) findViewById(R.id.etHint);
//		passCheck = (CheckBox) findViewById(R.id.passCheck);
//		passCheck.setText("Show Password");
		etType = (Spinner) findViewById(R.id.etType);

		ArrayAdapter<?> tmArrayAdapter = new ArrayAdapter<Object>(this,
				R.layout.csi_spinner, ArrayDef.TYPE);
		etType.setAdapter(tmArrayAdapter);
		etType.setSelection(2);
		etType.setEnabled(false);

		Button btClear = (Button) findViewById(R.id.bt_clear_reg);
		Button btRegister = (Button) findViewById(R.id.btRegister);
		DbUtil.changeDrawableColor("#F90606", btClear, 2);
		DbUtil.changeDrawableColor("#7FB200", btRegister, 2);

		initEditText();

		btClear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.v("Button Clicked!", "btClear");
				finish();
			}
		});

		btRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.v("Button Clicked!", "btRegister");
				preRegister();
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				//imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
				imm.hideSoftInputFromWindow(etFirstname.getWindowToken(), 0);
			}
		});

//		passCheck.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				//is chkIos checked?
//				if (((CheckBox) v).isChecked()) {
//					//Case 1
//					etPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//					etCPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//				} else {
//					etPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//					etCPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//					//case 2
//				}
//
//			}
//		});

	}

	private void initEditText() {
		ViewGroup group = (ViewGroup) findViewById(R.id.rl_regmain);
		for (int i = 0, count = group.getChildCount(); i < count; ++i) {
			View view = group.getChildAt(i);
			if (view instanceof EditText) {
				((EditText) view).setText("");
				((EditText) view).setBackgroundColor(0xfffffedf);
			}
		}

	}

	private void preRegister() {
		try {
			String firstname = etFirstname.getText().toString();
			String lastname = etLastname.getText().toString();
			int type = etType.getSelectedItemPosition();

            if(type == 0 ){
                DbUtil.makeToast(LayoutInflater.from(context),  "Please select type of salesman.", context,
                        (ViewGroup) findViewById(R.id.custom_toast_layout),1);
                return;
            }

			if (firstname.length() < 2) {
//    			Toast.makeText( getApplicationContext(), "Enter your firstname and lastname."  ,Toast.LENGTH_LONG ).show();
				DbUtil.makeToast(LayoutInflater.from(context), "Enter your firstname and lastname.", context,
						(ViewGroup) findViewById(R.id.custom_toast_layout), 1);
				etFirstname.requestFocus();
				return;
			}
			String mobileno = etMobileno.getText().toString();
			if (mobileno.length() < 10 || mobileno.length() > 13) {
//    			Toast.makeText( getApplicationContext(), "Enter a valid Mobileno."  ,Toast.LENGTH_LONG ).show();
				DbUtil.makeToast(LayoutInflater.from(context), "Enter a valid Mobile number.", context, null, 1);
				etMobileno.requestFocus();
				return;
			}

			if (!checkPassword(etPass.getText().toString())) {
//    			Toast.makeText( getApplicationContext(), "Password strength is weak,\n" +
//    					"Please provide 8 characters with uppercase,lowercase,character and numerics."  ,Toast.LENGTH_LONG ).show();
				DbUtil.makeToast(LayoutInflater.from(context), "Password strength is weak,\n" +
								"Please provide 8 minimum and 20 maximum characters \nwith uppercase,lowercase,character and numerics.", context,
						(ViewGroup) findViewById(R.id.custom_toast_layout), 1);
				etPass.requestFocus();
				etPass.setText("");
				etCPass.setText("");
				return;
			}

			if (!etPass.getText().toString().equals(etCPass.getText().toString())) {
//    			Toast.makeText( getApplicationContext(), "Password not match"  ,Toast.LENGTH_LONG ).show();
				DbUtil.makeToast(LayoutInflater.from(context), "Password do not match.", context,
						(ViewGroup) findViewById(R.id.custom_toast_layout), 1);
				etCPass.requestFocus();
				etCPass.setText("");
				return;
			}

			if (etHint.getText().toString().equals("")) {
//    			Toast.makeText( getApplicationContext(), "Please provide a Password Hint"  ,Toast.LENGTH_LONG ).show();
				DbUtil.makeToast(LayoutInflater.from(context), "Please provide a Password Hint.", context,
						(ViewGroup) findViewById(R.id.custom_toast_layout), 1);
				etHint.requestFocus();
				return;
			}
			setLogin();
//    		Toast.makeText(getApplicationContext(), "Processing registration... "  ,Toast.LENGTH_LONG ).show();
			DbUtil.makeToast(LayoutInflater.from(context), "Processing registration... ", context,
					(ViewGroup) findViewById(R.id.custom_toast_layout), 1);
			String company = etCompany.getText().toString();
			String email = etEmail.getText().toString();

			while (devId == null) {
				TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
					// TODO: Consider calling
					return;
				}
				devId = telephonyManager.getDeviceId();
			}
    		
    		String message = devId
    				+";" + androidId
    				+";" + company
    				+";" + firstname
    				+";" + lastname
    				+";" + mobileno
    				+";" + email
                    +";" + etType.getSelectedItem().toString();
    		
    		message = Master.CMD_REGISTER + " " + message;    		
    		DbUtil.saveMsg(context,gateway, message);
			DbUtil.saveSetting(context, Master.DEVID, devId);
            Log.e("types",etType.getSelectedItem().toString());
    		SettingDbManager db = new SettingDbManager(context);
    		db.open();    		
//    		db.setKeyValue("devid",devId,1);
    		db.setKeyValue("password", PasswordUtil.SHA1(etPass.getText().toString()), 1);
    		db.setKeyValue("lastFour", etPass.getText().toString().substring(etPass.getText().toString().length()-4), 1);
    		db.setKeyValue("hint",etHint.getText().toString(),1);
            db.setKeyValue("type",etType.getSelectedItem().toString(),1);
    		db.close();
    		
    		//DialogManager.showAlertDialog(context, "Registration", "Registration Application has been sent!", false);
		   
	       Intent i = new Intent(context, MainActivity.class);
	       i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	       context.startActivity(i);
	       finish();
			
    	}catch(Exception e){
    		Log.e("Exception Error: ","preRegister exception!");
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
       
    @Override
    public void onBackPressed() { 
    	
    		Intent backtoHome = new Intent(Intent.ACTION_MAIN);
    		backtoHome.addCategory(Intent.CATEGORY_HOME);
    		backtoHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		startActivity(backtoHome);
    	
    		finish();    	
    } 
    
    private Boolean checkPassword(String password){
    	
    	final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20})";
    	Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
    	Matcher matcher = pattern.matcher(password);
    	 	    	
    	return matcher.matches();
    }    
}
