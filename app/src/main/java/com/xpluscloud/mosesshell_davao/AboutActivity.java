package com.xpluscloud.mosesshell_davao;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xpluscloud.mosesshell_davao.dbase.DesContract;
import com.xpluscloud.mosesshell_davao.dbase.SyncDbManager;
import com.xpluscloud.mosesshell_davao.dbase.UtilDbManager;
import com.xpluscloud.mosesshell_davao.util.DbUtil;
import com.xpluscloud.mosesshell_davao.util.LayoutUtil;
import com.xpluscloud.mosesshell_davao.util.Master;

import org.json.JSONArray;

import static com.xpluscloud.mosesshell_davao.MainActivity.devId;

public class AboutActivity extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_about);
        context = AboutActivity.this;

        saveIMEI();

//		sync_datas("customers",1000,0,context);
//		uploadCustomers();
        String strTxt;
        if(Master.FOR_APPROVAL_SETTING == 0){
            strTxt = "SFA ARMY APP features <font color=\"red\">CUSTOMERS HAVE NO APPROVAL</font><br>Updated August 11, 2020<br><br>Contact Us<br>Email: support@xplus.ph";
        }else {
            strTxt = "SFA ARMY APP Updated August 11, 2020<br><br>Contact Us<br>Email: support@xplus.ph";
        }

        TextView tv1 = findViewById(R.id.textView1);
        tv1.setText(Html.fromHtml(strTxt), TextView.BufferType.SPANNABLE);
        TextView imei = findViewById(R.id.imei);
        imei.setText(getIMEI());

        ImageView iv1 = findViewById(R.id.iv1);
        ImageView iv2 = findViewById(R.id.iv2);
        ImageView iv3 = findViewById(R.id.iv3);
        ImageView iv4 = findViewById(R.id.iv4);
        ImageView iv12 = findViewById(R.id.imageView12);
        iv1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                callSupport("09253056025");
            }
        });
        iv2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                sendMessage("09253056025", "SUN");
//                OutboxDbManager db2 = new OutboxDbManager(context);
//                db2.open();
//                db2.resendDateRange(1515945600000l,1516032000000l);
//                db2.close();
            }
        });
        iv3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                callSupport("09778037710");
            }
        });
        iv4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                sendMessage("09778037710", "GLOBE");
            }
        });
        iv12.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                webHome();
            }
        });
    }

    private void sendMessage(final String mobileno, String network) {

//		Drawable dr = context.getResources().getDrawable(R.drawable.assets_msg);
//		Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        // Scale it to 50 x 50
//		Drawable d = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, 60, 40, true));
        Drawable d = new BitmapDrawable(context.getResources(), LayoutUtil.decodeSampledBitmapFromResource(context.getResources(), R.drawable.assets_msg, 60, 40));

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setIcon(d)
                .setTitle("SMS Support " + network)
                .setMessage("Send a message to support team.");
        alertDialog.setCancelable(false);

        final EditText input = new EditText(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(50, 0, 50, 0);
        input.setLayoutParams(lp);
        input.setBackgroundResource(android.R.drawable.edit_text);
        input.setHint("Enter message");

        alertDialog.setView(input);

        alertDialog.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SmsManager sms = SmsManager.getDefault();
                Intent intent = new Intent("customer_sms");
                PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0,
                        intent, 0);

                sms.sendTextMessage(mobileno, null,
                        input.getText().toString(), sentIntent, null);

                final ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Sending message please wait...");
                progressDialog.setCancelable(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();

                context.registerReceiver(new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        switch (getResultCode()) {
                            case Activity.RESULT_OK:
                                Toast.makeText(context.getApplicationContext(), "Message is sent ^_^", Toast.LENGTH_SHORT).show();
                                unregisterReceiver(this);
                                progressDialog.dismiss();
                                break;
                            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                                Toast.makeText(context.getApplicationContext(), "Sending failed! Try again...", Toast.LENGTH_SHORT).show();
                                unregisterReceiver(this);
                                progressDialog.dismiss();
                                break;
                            case SmsManager.RESULT_ERROR_NO_SERVICE:
                                Toast.makeText(context.getApplicationContext(), "No signal from service provider!", Toast.LENGTH_SHORT).show();
                                unregisterReceiver(this);
                                progressDialog.dismiss();
                                break;
                            case SmsManager.RESULT_ERROR_NULL_PDU:
                                Toast.makeText(context.getApplicationContext(), "Message Error!", Toast.LENGTH_SHORT).show();
                                unregisterReceiver(this);
                                progressDialog.dismiss();
                                break;
                            case SmsManager.RESULT_ERROR_RADIO_OFF:
                                Toast.makeText(context.getApplicationContext(), "Device is Offline!", Toast.LENGTH_SHORT).show();
                                unregisterReceiver(this);
                                progressDialog.dismiss();
                                break;
                        }

                    }
                }, new IntentFilter("customer_sms"));

            }

        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void callSupport(String mobileno) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobileno));
        context.startActivity(callIntent);
    }

    private void saveIMEI() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        String devId = telephonyManager.getDeviceId();
        String devId = Master.getDevId2(context);
        SharedPreferences prefs = this.getSharedPreferences(
			      getPackageName(), Context.MODE_PRIVATE);
		
		String imei = prefs.getString("IMEI01","");
		if(imei.isEmpty()) prefs.edit().putString("IMEI01",devId).apply();
		else{
			if(!imei.contains(devId)){
				String imei2 = prefs.getString("IMEI2", "");
				if(imei2.isEmpty()) prefs.edit().putString("IMEI02",devId).apply();
				else{
					if(!imei2.contains(devId)){
						String imei3 = prefs.getString("IMEI3", "");
						if(imei3.isEmpty()) prefs.edit().putString("IMEI03",devId).apply();
					}
				}
				
			}
		}		
	}
	
	private String getIMEI(){
		String devIds = "";
		SharedPreferences prefs = this.getSharedPreferences(
			      getPackageName(), Context.MODE_PRIVATE);
		String devId1 = prefs.getString("IMEI01","");
		String devId2 = prefs.getString("IMEI02","");
		String devId3 = prefs.getString("IMEI03","");
		
		if(!devId1.isEmpty()) devIds = devIds+"IMEI1: "+devId1;
		if(!devId2.isEmpty()) devIds = "\n"+devIds+"IMEI2: "+devId1;
		if(!devId3.isEmpty()) devIds = "\n"+devIds+"IMEI3: "+devId1;
		
		return devIds;
	}
	
	private void webHome(){
		String link = context.getResources().getString(R.string.download_url);
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
		startActivity(browserIntent);
    				
	}

	private void sync_datas(String table_name, int limit, int status, Context context){

		Log.e(table_name,context.getResources().getString(R.string.upload_datas));

		SyncDbManager db = new SyncDbManager(context);
		db.open();
		JSONArray sync_server = new JSONArray();
		sync_server = db.sync_server(table_name,limit,status);
		db.close();

		if(sync_server.length()<=0) return;

		String devId = DbUtil.getSetting(context, Master.DEVID);

		String sourceTable = table_name;

		AsyncHttpPost asyncHttp = new AsyncHttpPost(context,sync_server,devId,sourceTable,"");
		String url=context.getResources().getString(R.string.upload_datas);
		asyncHttp.execute(url);

	}

	private void uploadCustomers(){

		String sourceTable = DesContract.Customer.TABLE_NAME;

		UtilDbManager db = new UtilDbManager(context);
		db.open();
		JSONArray newCustomers 	= new JSONArray();
		newCustomers = db.getJsonArray(sourceTable, "1");
		db.close();

		if(newCustomers.length()<=0) return;

		AsyncHttpPost asyncHttp = new AsyncHttpPost(context,newCustomers,devId,sourceTable,"");
		String url=context.getResources().getString(R.string.upload_customers);
		asyncHttp.execute(url);

	}
}  
