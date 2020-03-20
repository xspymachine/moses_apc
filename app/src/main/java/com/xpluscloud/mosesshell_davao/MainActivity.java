package com.xpluscloud.mosesshell_davao;


import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.xpluscloud.mosesshell_davao.dbase.CompetitorDbManager2;
import com.xpluscloud.mosesshell_davao.dbase.CustomerDbManager;
import com.xpluscloud.mosesshell_davao.dbase.InOutDbManager;
import com.xpluscloud.mosesshell_davao.dbase.InventoryDbManager;
import com.xpluscloud.mosesshell_davao.dbase.IssueDbManager;
import com.xpluscloud.mosesshell_davao.dbase.OutboxDbManager;
import com.xpluscloud.mosesshell_davao.dbase.PictureDbManager;
import com.xpluscloud.mosesshell_davao.dbase.PromoDbManager;
import com.xpluscloud.mosesshell_davao.dbase.SalescallDbManager;
import com.xpluscloud.mosesshell_davao.dbase.SettingDbManager;
import com.xpluscloud.mosesshell_davao.dbase.SignatureDbManager;
import com.xpluscloud.mosesshell_davao.getset.Customer;
import com.xpluscloud.mosesshell_davao.getset.CustomerData;
import com.xpluscloud.mosesshell_davao.getset.Issue;
import com.xpluscloud.mosesshell_davao.getset.Picture;
import com.xpluscloud.mosesshell_davao.getset.TimeInOut;
import com.xpluscloud.mosesshell_davao.util.ArrayDef;
import com.xpluscloud.mosesshell_davao.util.DateUtil;
import com.xpluscloud.mosesshell_davao.util.DbUtil;
import com.xpluscloud.mosesshell_davao.util.DialogManager;
import com.xpluscloud.mosesshell_davao.util.Master;

import org.json.JSONArray;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.xpluscloud.mosesshell_davao.util.DbUtil.getSetting;

//implements OptionDialogListener
public class MainActivity extends AppCompatActivity {
    public final int EMPTY_OUTBOX = 0;
    final String TAG = "MainActivity";

    public final int PERFORMANCE        = 0;
    public final int TIME_IN            = 1;
    public final int CUSTOMER_CALL      = 2;
    public final int PROMOTIONS         = 3;
    public final int PROMO              = 4;
    public final int CALL_SHEET         = 5;
//    public final int SURVEY             = 6;
    public final int COMPETITOR         = 6;
    public final int PICTURE            = 7;
    public final int ISSUES				= 8;
    public final int TIME_OUT           = 9;
    public final int MARK_LOCATION      = 10;
    public final int UPLOAD_SIGNATURE   = 11;
    public final int UPLOAD_PICTURE     = 12;
    public final int SYNC               = 13;
    public final int ABOUT              = 14;




    //OptionDialog Fragment origin
    public final int CALLSHEET = 0;
    public final int RETURN = 1;


    Context context;

    public static String devId;
    public Button btSearchCustomer;
    public CircleImageView btProfile;
    public String customerCode = "";
    public String customerName = "";
    public String customerAddress = "";
    public String contactNumber = "";
    public Double discount = 0.0;

    DialogManager alert = new DialogManager();

    public static final String SETTINGS = "MosesSettings";
    public SharedPreferences settings;
    private String sysDate;
    private String logDate;

    private String devPath;
    private String imageRemarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;

        setTitle(getResources().getString(R.string.app_name) + " " +
//				 getResources().getString(R.string.version )
                " - " + "Dashboard");

        devId = getDeviceImei();

//        Log.e("devid", devId);

        SettingDbManager db = new SettingDbManager(context);
        db.open();
        Boolean registered = db.isRegistered2(devId);
        db.close();

        settings = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        int isLogin = settings.getInt("isLogin", 0);
        logDate = settings.getString("logDate", "");
        sysDate = DateUtil.strDate(System.currentTimeMillis());

        int properLogout = settings.getInt("pLogout", 0);
//        Log.e("" + isLogin + "-" + properLogout, logDate + " - " + sysDate);

        if (properLogout != 2) {
            improperLogout();
            Log.e("IMPROPER LOGOUT", "FORCE STOP");
        } else if (isLogin == 1) {
            SharedPreferences.Editor prefEditor = settings.edit();
            prefEditor.putInt("pLogout", 0);
            prefEditor.commit();
        }

        if (!registered) {
            Intent i = new Intent(this, RegisterActivity.class);
            Bundle b = new Bundle();
            b.putString("devId", devId);
            i.putExtras(b);
            startActivity(i);
            finish();
            return;
        } else if (isLogin != 1 || !logDate.equalsIgnoreCase(sysDate)) {
            Intent i = new Intent(this, LoginActivity.class);
            Bundle b = new Bundle();
            b.putString("devId", devId);
            i.putExtras(b);
            startActivity(i);
            finish();
            return;
        }


        final Intent i = new Intent(this, MyLocationService.class);
        startService(i);
        Log.w(TAG, " About to call Dispatcher... ");

        final Intent dispatcher = new Intent(this, DispatcherService.class);
//		startService(dispatcher);

        btSearchCustomer = findViewById(R.id.bt_search_customer);

        btSearchCustomer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Button Clicked!", "SearchCustomer");
                searchCustomer(v);
            }
        });

        btProfile = findViewById(R.id.profile_image);
        btProfile.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.v("Button Clicked!", "PROFILE");
                viewProfile(v);
            }
        });
        refreshList();
        if(Master.FOR_APPROVAL_SETTING == 0) noapproval();
        getCustomerBdayList(2);

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        devId = telephonyManager.getDeviceId();

//		InOutDbManager db = new InOutDbManager(context);
//		db.open();
//		db.checkIO(devId);
//		int count = db.getCount();
//		db.close();

        String appdate = getResources().getString(R.string.versiondate);

        if (DbUtil.getSetting(context, "senddatatoken") == null || !DbUtil.getSetting(context, "senddatatoken").contains(appdate)) {

            AsyncHttpPost asyncHttp = new AsyncHttpPost(context, new JSONArray(), devId, "",
            getResources().getString(R.string.app_name) + appdate + " ARMY SUN https");
//            getResources().getString(R.string.app_name) + appdate + " ARMY SUN NO APPROVAL https");
//                    getResources().getString(R.string.app_name) + appdate + " ARMY SUN NOTIMEIN https");
//            asyncHttp.execute(context.getResources().getString(R.string.appversion));

            String refreshedToken = DbUtil.getSetting(context, "refreshedToken");
            String message = Master.CMD_TOK + " " +
                    devId + ";" +
                    refreshedToken + ";" +
                    System.currentTimeMillis() / 1000;

            DbUtil.saveMsg(getApplicationContext(), DbUtil.getGateway(getApplicationContext()), message);

            DbUtil.saveSetting(context, "senddatatoken", appdate);
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor prefEditor = settings.edit();
                        prefEditor.putInt("isLogin", 0);
                        prefEditor.putInt("pLogout", 2);
                        prefEditor.commit();
                        finish();
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

    ListView listView;

    private void refreshList() {
        TimeInOut dbo = getLastInOut();

        try {
            if (dbo != null && dbo.getInout() == 1) {

                this.setCurrentCustomer(dbo.getCustomerCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        listView = findViewById(R.id.listview);

        listView.setAdapter(new DashboardArrayAdapter(this, ArrayDef.DASHBOARD_LIST, dbo));

        listView.setOnItemClickListener(new onListItemClick());
    }

    private void setCurrentCustomer(String ccode) {

        Customer cust = this.getCustomerInfo(ccode);
        customerCode = cust.getCustomerCode();
        customerName = cust.getName();
        customerAddress = cust.getAddress();
        contactNumber = cust.getContactNumber();
        discount = cust.getDiscount();
        if (contactNumber != "")
            btSearchCustomer.setText(customerName + " " + contactNumber + " \n" + customerAddress);
        else btSearchCustomer.setText(customerName + " \n" + customerAddress);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Outbox:
                Intent i = new Intent(this, OutboxListActivity.class);
                startActivity(i);
                break;

            case R.id.empty_outbox:
                Log.w("Outbox", "About to empty outbox!");
                confirmDialog("Empty Outbox!", "Are you sure you want to delete all messages in outbox?", EMPTY_OUTBOX, 0);
                break;

        }
        return true;
    }


    private class onListItemClick implements AdapterView.OnItemClickListener {


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //get selected items
            //String selectedValue = (String) getListAdapter().getItem(position-1);
            Log.w("Customer Code", "" + customerCode);

            if (position <= MARK_LOCATION && position >= TIME_IN) {
                if (Master.getStatGPS(context) != 1) {
                    gpsOff();
                    return;

                }

                String gpsDate = getSetting(context, "gpsdate");
//			if (gpsDate==null) {
//				noGpsSignal();
//				return;
//			}

                if (customerCode == null || customerCode.isEmpty()) {
                    noCustomer();
                    return;
                }
                else if(alertMarkLocation() && position < MARK_LOCATION) {
                    noMarkLocation();
                    return;
                }

                int timeIsOn = android.provider.Settings.System.getInt(getContentResolver(), android.provider.Settings.Global.AUTO_TIME, 0);
//			int timeZoneIsOn = android.provider.Settings.System.getInt(getContentResolver(), android.provider.Settings.Global.AUTO_TIME_ZONE, 0);

                if (timeIsOn == 0) {
                    checkNetworkTime();
                    return;
                }
            }

            //Get LastTransaction
            TimeInOut dbo = getLastInOut();
//            Log.e("position", "" + position);
            switch (position) {
                case TIME_IN:
                    if (dbo == null) {
                        timeInOut("IN");
                    } else if (dbo.getInout() == 0 && getInDate(customerCode)) {
                        timeInOut("IN");
                    }
                    else if (dbo.getInout()==0 && !getInDate(customerCode)){
                        duplicateTimeIn(customerCode);
                    }
                    else {
                        NoTimeOut(dbo.getCustomerCode());
                    }
                    break;

                case CUSTOMER_CALL:
                    if (isTimeIn(customerCode, dbo)) customerCall();
                    else NoTimeIn(customerCode);
                    break;

                case CALL_SHEET:
                    if (isTimeIn(customerCode, dbo) && cusSalescall()) {
                        callSheet(0);
                    } else
                    if (isTimeIn(customerCode, dbo)) NoSalescall(customerCode);
                    else callSheet(1);
                    break;

                case TIME_OUT:
                    if (dbo != null) {
                         if ((dbo.getInout() == 1) && !cusSalescall()) {
                            NoSalescall(customerCode);
                        }
                        else if ((dbo.getInout() == 1) && !cusCompetitorCheck()) {
                             NoCompetitorMapping(customerCode);
                         }
//                         else if ((dbo.getInout() == 1) && !cusInventoryCheck()) {
//                             NoInventory(customerCode);
//                         }
                         else if ((dbo.getInout() == 1) && !cusPromoCheck()) {
                             NoCusPromo(customerCode);
                        }
                         else if ((dbo.getInout() == 1) && (dbo.getCustomerCode().equalsIgnoreCase(customerCode))) {
                             timeInOut("OUT");
                         }else {
                            NoTimeIn(customerCode);
                        }
                    } else {
                        NoTimeIn(customerCode);
                    }
                    break;
                case MARK_LOCATION:
                    if(marked(customerCode)){
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Already Marked")
                                .setMessage("Proceed with re-marking  location of this customer?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        markLocation();
                                    }
                                })
                                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    else markLocation();
                    break;

                case UPLOAD_SIGNATURE:
                    uploadSignature();
                    break;

                case SYNC:
                    sync();
                    break;

                case ABOUT:
                    about();
                    break;

			case ISSUES:
                issues();
				break;

//			case SIGNATURE:
//                getSignature();
//				break;

                case UPLOAD_PICTURE:
                    uploadPicture();
                    break;
//
                case PICTURE:
                    if (isTimeIn(customerCode, dbo)) takePicture();
                    else NoTimeIn(customerCode);
                    break;
                case PROMOTIONS:
                    if (isTimeIn(customerCode, dbo)) promotions();
                    else NoTimeIn(customerCode);
                    break;
                case PERFORMANCE:
                    statusSummary();
                    break;
//                case INVENTORY:
//                    if (isTimeIn(customerCode, dbo)) inventory();
//                    else NoTimeIn(customerCode);
//                    break;
//                case SURVEY:
//                    DialogManager.showAlertDialog(MainActivity.this,
//                            "Survey Button",
//                            "This button is temporary only for viewing purposes.", false);
//                    break;
                case PROMO:
                    if (isTimeIn(customerCode, dbo)) promobutton();
                    else NoTimeIn(customerCode);
                    break;
                case COMPETITOR:
                    if (isTimeIn(customerCode, dbo)) competitor();
                    else NoTimeIn(customerCode);
                    break;

                default:
                    //
                    break;
            }
        }
    }

    public void sendAlert() {
        Location loc = null;
        String myCoordinates = "";
        try {
            loc = MyLocationService.currentLocation;
            if (loc != null) {
                myCoordinates = loc.getLatitude()
                        + "," + loc.getLongitude()
                        + "," + loc.getAccuracy()
                        + "," + loc.getTime()
                        + "," + loc.getProvider();
            }

        } catch (Exception e) {
        }

        devId = getSetting(context, Master.DEVID);

        String smsg = "XPT911 " + devId + "," + myCoordinates; //command

        DbUtil.saveMsg(context, DbUtil.getGateway(context), smsg);
        Toast.makeText(getApplicationContext(), "Emergency Alert Sent! ", Toast.LENGTH_LONG).show();
    }

    private Boolean alertMarkLocation(){
        CustomerDbManager db = new CustomerDbManager(context);
        db.open();
        Customer c = db.getCustomerByCode(customerCode);
        db.close();

        return !(c.getLatitude() > 0);
    }

    private Boolean marked(String ccode) {
        CustomerDbManager db = new CustomerDbManager(context);
        db.open();
        Customer c = db.getCustomerByCode(ccode);
        db.close();

        return (c.getLatitude() > 1);
    }


    /***** Search Customer Section ********/

    private void searchCustomer(View Button) {
        Intent i = new Intent(this, CustomerListActivity.class);
        Bundle b = new Bundle();
        b.putString("devId", devId);
        i.putExtras(b);
        startActivityForResult(i, 1);
    }

    /***** View Profile Section ********/

    private void viewProfile(View Button) {

        if (customerCode == null || customerCode.isEmpty()) {
            noCustomer();
            return;
        }

        Intent i = new Intent(this, CustomerProfileActivity.class);
        Customer cust = this.getCustomerInfo(customerCode);
        String cmpltAddress = cust.getAddress() + " " + cust.getBrgy() + " " + cust.getCity() + ", " + cust.getState();
        Bundle b = new Bundle();
        b.putString("devId", devId);
        b.putString("ccode", customerCode);
        b.putString("customerName", customerName);
        b.putString("customerAddress", cmpltAddress);
        i.putExtras(b);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    customerName = data.getStringExtra("cName");
                    customerCode = data.getStringExtra("cCode");
                    customerAddress = data.getStringExtra("cAddress");
                    contactNumber = data.getStringExtra("cNum");
                    discount = data.getDoubleExtra("cDisc", 0.0);
                    if (contactNumber != "")
                        btSearchCustomer.setText(customerName + " " + contactNumber + " \n" + customerAddress);
                    else btSearchCustomer.setText(customerName + " \n" + customerAddress);


                    clientIssue(customerCode);
                }
                if (resultCode == RESULT_CANCELED) {
                    //Write your code if there's no result
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap picture = (Bitmap) extras.get("data");
                    String remarks = extras.getString("remarks");
                    picture = Bitmap.createScaledBitmap(picture, 320, 240, true);
                    savePicture(picture);
                }
                break;

        }


    }//onActivityResult


    /***** Search ListView Section ********/

    private void clientIssue(String ccode) {
        IssueDbManager db = new IssueDbManager(context);
        db.open();
        Issue issueInfo = db.getOpenIssue(ccode, "");
        db.close();

        if (issueInfo != null) {
            DialogManager.showAlertDialog(MainActivity.this,
                    "Client Issue",
                    "Please tap on Customer Issue for more info.", false);
        }

    }
    private void noMarkLocation() {
        DialogManager.showAlertDialog(MainActivity.this,
                "Mark Location",
                "Please mark the location of this customer.", false);
    }

    private void noCustomer() {
        DialogManager.showAlertDialog(MainActivity.this,
                "Client is Required!",
                "Please search and select a client for this activity.", false);
    }

    private void NoTimeOut(String ccode) {
        DialogManager.showAlertDialog(MainActivity.this,
                "No Time Out!",
                "You must Time Out at " + this.getCustomerName(ccode) + "...", false);
    }

    private void duplicateTimeIn(String ccode) {
//		DialogManager.showAlertDialog(MainActivity.this,
//                "Already have Time In!",
//                "You can only Time In at " + this.getCustomerName(ccode)+" once per day...", false);

//        DialogManager.showAlertDialog(MainActivity.this,
//                "Already have Time In!",
//                "You can only Time In at " + this.getCustomerName(ccode) + " at least twice per day...", false);

        DialogManager.showAlertDialog(MainActivity.this,
                "Already have Time In!",
                "You can only Time In at " + this.getCustomerName(ccode) + " at least once a month...", false);
    }

    private void NoTimeIn(String ccode) {
        DialogManager.showAlertDialog(MainActivity.this,
                "Time In Required!",
                "You must time in at " + this.getCustomerName(ccode) + " first!", false);
    }

    private void timeInOut(String inout) {
        Intent intent = new Intent(context, InOutActivity.class);
        Bundle b = new Bundle();

        b.putString("devId", devId);
        b.putString("customerCode", customerCode);
        b.putString("customerName", customerName);
        b.putString("customerAddress", customerAddress);
        b.putString("inout", inout);

        intent.putExtras(b);
        startActivity(intent);
    }

    private void customerCall() {

        Intent intent = new Intent(context, CustomerCallActivity.class);
        Bundle b = new Bundle();
        b.putString("customerCode", customerCode);
        b.putString("customerName", customerName);
        b.putString("customerAddress", customerAddress);
        b.putString("devId", devId);

        intent.putExtras(b);
        startActivity(intent);
    }

    private void callSheet(int sales_type) {
        Intent intent = new Intent(context, CallSheetActivity.class);
        Bundle b = new Bundle();

        b.putString("devId", devId);
        b.putString("customerCode", customerCode);
        b.putString("customerName", customerName);
        b.putString("customerAddress", customerAddress);
        b.putString("csCode", null);
        b.putInt("cCashSales", sales_type);
        intent.putExtras(b);
        startActivity(intent);
    }

    private void markLocation() {
        Intent intent = new Intent(context, MarkLocationActivity.class);
        Bundle b = new Bundle();

        b.putString("devId", devId);
        b.putString("customerCode", customerCode);
        b.putString("customerName", customerName);
        b.putString("customerAddress", customerAddress);
        intent.putExtras(b);
        startActivity(intent);
    }

    private void issues() {
        Intent intent = new Intent(context, IssuesActivity.class);
        Bundle b = new Bundle();

        b.putString("devId", devId);
        b.putString("customerCode", customerCode);
        b.putString("customerName", customerName);
        b.putString("customerAddress", customerAddress);
        intent.putExtras(b);
        startActivity(intent);
    }

    private void uploadSignature() {
        Intent intent = new Intent(context, UploadSignatureActivity.class);
        startActivity(intent);
    }

    private void sync() {
        Intent intent = new Intent(context, SyncActivity.class);
        Bundle b = new Bundle();
        b.putString("devId", devId);
        intent.putExtras(b);
        startActivity(intent);
    }

    private void about() {
        Intent intent = new Intent(context, AboutActivity.class);
        Bundle b = new Bundle();
        b.putString("devId", devId);
        intent.putExtras(b);
        startActivity(intent);
    }

    private void confirmedDeleteAll() {
        OutboxDbManager db = new OutboxDbManager(context);
        db.open();
        db.deleteAll();
        db.close();

    }

    private void inventory() {
        Intent intent = new Intent(context, InventoryActivity.class);
        Bundle b = new Bundle();
        b.putString("devId", devId);
        b.putString("customerCode", customerCode);
        b.putString("customerName", customerName);
        b.putString("customerAddress", customerAddress);
        intent.putExtras(b);
        startActivity(intent);
    }
    private void promobutton() {
        Intent intent = new Intent(context, PromoActivity.class);
        Bundle b = new Bundle();
        b.putString("devId", devId);
        b.putString("ccode", customerCode);
        intent.putExtras(b);
        startActivity(intent);
    }
    private void competitor() {
        Intent intent = new Intent(context, CompetitorActivity2.class);
        Bundle b = new Bundle();
        b.putString("devId", devId);
        b.putString("customerCode", customerCode);
        b.putString("customerName", customerName);
        b.putString("customerAddress", customerAddress);
        intent.putExtras(b);
        startActivity(intent);
    }

    private void uploadPicture() {
        Intent intent = new Intent(context, UploadPictureActivity.class);
        startActivity(intent);
    }

    private void promotions() {
        Intent intent = new Intent(context, PromotionsActivity.class);
        startActivity(intent);
    }

    private TimeInOut getLastInOut() {
        TimeInOut dbo;

        InOutDbManager db = new InOutDbManager(context);
        db.open();
        dbo = db.getLastTransaction();
        db.close();

        return dbo;
    }

    private Boolean isTimeIn(String ccode, TimeInOut dbo) {
        Boolean timeIn = false;

        if (dbo == null) return false;

        if ((dbo.getInout() == 1) && (dbo.getCustomerCode().equalsIgnoreCase(ccode))) {
            Long sysTime = System.currentTimeMillis();
            String today = DateUtil.strDate(sysTime);
            Long longDate = Long.valueOf(dbo.getDateTime()) * 1000;
            String strDate = DateUtil.strDate(longDate);

//            Log.e("today", today);
//            Log.e("strDate", strDate);
            if (strDate.contentEquals(today)) {
                timeIn = true;
            }

        }
        return timeIn;
    }

    private Customer getCustomerInfo(String ccode) {
        Customer cust;
        CustomerDbManager db = new CustomerDbManager(context);
        db.open();
        cust = db.getCustomer(ccode);
        db.close();

        return cust;
    }

    private String getCustomerName(String ccode) {
        String custName;

        CustomerDbManager db = new CustomerDbManager(context);
        db.open();
        custName = db.getCustomerName(ccode);
        db.close();

        return custName;
    }

    private void getSignature() {
        Intent intent = new Intent(context, GetSignatureActivity.class);
        Bundle b = new Bundle();

        b.putString("devId", devId);
//		b.putString("txNo", ""+soNo);
        b.putString("customerCode", customerCode);
        b.putString("customerName", customerName);
        b.putString("customerAddress", customerAddress);
        intent.putExtras(b);
        startActivity(intent);
    }

    private void callsSummary() {
        Intent intent = new Intent(context, CustomerCallSummary.class);
        Bundle b = new Bundle();

        b.putString("devId", devId);
        b.putString("customerCode", customerCode);
        b.putString("customerName", customerName);
        b.putString("customerAddress", customerAddress);
        intent.putExtras(b);
        startActivity(intent);
    }

    private void statusSummary() {
//        Intent intent = new Intent(context, SummaryStatusWeb.class);
        Intent intent = new Intent(context, TargetSalesActivity.class);
        Bundle b = new Bundle();
        b.putString("devId", devId);
        intent.putExtras(b);
        startActivity(intent);
    }

    /*************************************************************
     *** Confirm Dialog
     ************************************************************/

    public void confirmDialog(String title, String message, Integer option, Integer id) {

        final Integer Option = option;
        final String Title = title;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon(R.drawable.delete32);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (Option) {
                    case EMPTY_OUTBOX:
                        confirmedDeleteAll();
                        break;

                }

                Toast.makeText(getApplicationContext(), Title + " confirmed!", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    private Boolean cusSalescall() {
        Boolean ccall = false;

        SalescallDbManager db = new SalescallDbManager(context);
        db.open();
        if (db.getLastSaleTransaction(customerCode) != null) {
            ccall = true;
        }
        db.close();

        return ccall;
    }
    private Boolean cusCompetitorCheck() {
        Boolean ccall = false;

        CompetitorDbManager2 db = new CompetitorDbManager2(context);
        db.open();
        if (db.getLastCompetitorTransaction(customerCode) > 0) {
            ccall = true;
        }
        db.close();

        return ccall;
    }
    private Boolean cusInventoryCheck() {
        Boolean ccall = false;

        InventoryDbManager db = new InventoryDbManager(context);
        db.open();
        if (db.getLastInventoryTransaction(customerCode) > 0) {
            ccall = true;
        }
        db.close();

        return ccall;
    }
    private Boolean cusPromoCheck() {
        Boolean ccall = false;

        PromoDbManager db = new PromoDbManager(context);
        db.open();
        if (db.getLastPromoTransaction(customerCode) > 0) {
            ccall = true;
        }
        db.close();

        return ccall;
    }

    private Boolean cusSigCall() {
        Boolean sig = false;

        SignatureDbManager db = new SignatureDbManager(context);
        db.open();
        if (db.getLastSigTransaction(customerCode) != null) {
            sig = true;
        }
        db.close();

        return sig;
    }

    private void NoSalescall(String ccode) {
        DialogManager.showAlertDialog(MainActivity.this,
                "Customer Call Required!",
                "You must describe the Customer Call in " + this.getCustomerName(ccode) + " first!", false);
    }

    private void NoCompetitorMapping(String ccode) {
        DialogManager.showAlertDialog(MainActivity.this,
                "Competitor Mapping Required!",
                "You must check Competitor Mapping in " + this.getCustomerName(ccode) + " before timeout!", false);
    }
    private void NoInventory(String ccode) {
        DialogManager.showAlertDialog(MainActivity.this,
                "Inventory Required!",
                "You must check inventory in " + this.getCustomerName(ccode) + " before timeout!", false);
    }
    private void NoCusPromo(String ccode) {
        DialogManager.showAlertDialog(MainActivity.this,
                "Promo Required!",
                "You must invite " + this.getCustomerName(ccode) + " for promo offer before timeout!", false);
    }
    private void NoSigcall(String ccode) {
        DialogManager.showAlertDialog(MainActivity.this,
                "Signature Required!",
                "You must create a signature for " + this.getCustomerName(ccode) + " first!", false);
    }

    private void gpsOff() {
        DialogManager.showAlertDialog(MainActivity.this,
                "GPS IS OFF",
                "Turn ON GPS and try again.", false);
    }

    public void GPSon() {
        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    private void noGpsSignal() {
        DialogManager.showAlertDialog(MainActivity.this,
                "No GPS Signal",
                "Go out in an open sky and wait for 5-10 minutes to acquire GPS signal.", false);
    }

    private void noapproval() {
        String txt = "This app features <font color=\"red\">CUSTOMERS HAVE NO APPROVAL</font> please be guided.";
//        String txt =  "Please be reminded to download all data tables.";
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("App Info")
                .setMessage(Html.fromHtml(txt))
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

    private void improperLogout() {
        long sysDateTime = System.currentTimeMillis();
        Integer battery = Master.getBatteryInfo(context);
        String statGPS = Master.getStatGPS(context).equals(1) ? " GPS: ON" : " GPS: OFF";

        devId = getSetting(context, Master.DEVID);
        String message = Master.CMD_LOGOUT + " " +
                devId + "," +
                sysDateTime + "," +
                " Battery: " + battery + "%," +
                statGPS;

        DbUtil.saveMsg(context, DbUtil.getGateway(context), message);
    }

    private Boolean getInDate(String ccode) {
		Long datetime;
        int count = 0;
        InOutDbManager db = new InOutDbManager(context);
        db.open();
//        datetime  = ""+DateUtil.phShortDate((db.getLastInByCode(ccode)*1000));
        datetime = db.getLastInByCode(ccode);

        db.close();
//		Log.e("",date);
//		Log.e("",DateUtil.phShortDate(System.currentTimeMillis()));
//		if(date.equals(DateUtil.phShortDate(System.currentTimeMillis()))){
//			return false;
//		}


        Date lastInDate = new Date(datetime*1000);
        Date currentDate = new Date(System.currentTimeMillis());
        SimpleDateFormat df = new SimpleDateFormat("MMyyyy", Locale.US);
        Log.e("date1",df.format(lastInDate));
        Log.e("date2",df.format(currentDate));
        return !df.format(lastInDate).equals(df.format(currentDate));


//        return count != 2;
    }

    private void checkNetworkTime() {
        DialogManager.showAlertDialog(MainActivity.this,
                "Network-provided time is off",
                "Please go to settings->date & time\nthen check 'Automatic date & time'.", false);
    }

    private String getDeviceImei() {
        String devId = getSetting(context, Master.DEVID);

        while (devId == null || devId.isEmpty()) {

            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(android.content.Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return "";
            }
            devId = telephonyManager.getDeviceId();

			DbUtil.saveSetting(context, Master.DEVID, devId);
        }

		return devId;
	}

	private void takePicture(){
		Drawable dr = context.getResources().getDrawable(R.drawable.assets_about);
		Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
		// Scale it to 50 x 50
		Drawable d = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, 60, 40, true));

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
				.setTitle("Remarks")
				.setMessage("Add remarks before taking picture.");
		alertDialog.setCancelable(false);

		final EditText input = new EditText(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		lp.setMargins(50, 0, 50, 0);
		input.setLayoutParams(lp);
		input.setBackgroundResource(android.R.drawable.edit_text);
		input.setHint("Brand, Type of Activity, Details");
		alertDialog.setView(input);

		alertDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});

		final AlertDialog dialog = alertDialog.create();
		dialog.show();

		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				imageRemarks = input.getText().toString();
				if(imageRemarks.length() > 50){
					DbUtil.makeToast(LayoutInflater.from(context), "Please summarize your remarks, 50 characters only." , context,null,1);
					return;
				}else{
					Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					takePictureIntent.putExtra("remarks", input.getText().toString());
					if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
						startActivityForResult(takePictureIntent, 2);
						dialog.dismiss();
					}
				}
			}
		});

		dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialog.dismiss();
			}
		});

	}


	private void savePicture(Bitmap Picture) {
		Calendar c = Calendar.getInstance();

		SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd", Locale.US);
		String folder = df2.format(c.getTime());

		Boolean mSDcheck = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

		File[] extMounts = getApplicationContext().getExternalFilesDirs(null);
		File sdRoot = null;
		if(extMounts.length > 1) sdRoot = extMounts[1];
		if(sdRoot == null){
//			Log.e("STORAGE","NOT AVAILABLE");
			devPath = Environment.getExternalStorageDirectory() + "/"
					+ getResources().getString(R.string.pictures_dir) + "/"
					+ folder +"/";
			prepareDirectory();
		}else{
			String domainedFolder = sdRoot.getAbsolutePath();
//			Log.e("STORAGE","AVAILABLE="+domainedFolder);
			devPath = sdRoot.getAbsolutePath()+"/"+getResources().getString(R.string.pictures_dir)+"/"+folder+"/";
			prepareDirectory();

		}

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss",Locale.US);
		String dateStamp =  df.format(c.getTime());
		String filename = dateStamp + "_" + customerCode + ".jpg";


		Bitmap picture = embedText(Picture, c.getTimeInMillis());


		String file = devPath + filename;


		File newfile = new File(file);
		try {
			newfile.createNewFile();

		} catch (IOException e) {
			e.printStackTrace();
		}


		try {
			FileOutputStream fo = new FileOutputStream(newfile);
			picture.compress(Bitmap.CompressFormat.JPEG,100, fo);
			fo.flush();
			fo.close();

		} catch (Exception e) {
			Log.v("log_tag", e.toString());
			DbUtil.makeToast(LayoutInflater.from(context),  "Error saving picture!" , context,null,0);
		}
		deleteLastPhotoTaken();
		String sysTime = String.valueOf(System.currentTimeMillis()/1000);

		int ba = 0; //BeforeAFter

		PictureDbManager db = new PictureDbManager(context);
		db.open();

		Picture p = new Picture();
		p.setDatetime(sysTime);
		p.setCustomerCode(customerCode);
		p.setBa(ba);
		p.setFilename(filename);
		p.setStatus(0);

		Long id = db.Add(p);
		db.close();

		devId = getSetting(context, Master.DEVID);

		String message = Master.CMD_PICTURE + " " +
				devId 			+ ";" +
				customerCode 	+ ";" +
				filename 		+ ";" +
				ba				+ ";" +
				dateStamp 		+ ";" +
				sysTime			+ ";" +
				id;

		DbUtil.saveMsg(context,DbUtil.getGateway(context), message);
        String message2 = Master.CMD_PICTURE2 + " " +
                devId 			+ ";" +
                customerCode 	+ ";" +
                filename 		+ ";" +
                imageRemarks    + ";" +
                id;

        DbUtil.saveMsg(context,DbUtil.getGateway(context), message2);
	}


	private Bitmap embedText(Bitmap picture, long timeStamp) {

		String strDate = DateUtil.phLongDateTime(timeStamp);

		Canvas canvas = new Canvas(picture);
		Paint paint = new Paint();

		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.FILL);
		paint.setShadowLayer(1.0f, 1.0f, 1.0f, Color.BLACK);
		paint.setTextSize(10);
//	    Log.e("height",""+picture.getHeight());
		int height = picture.getHeight();
		canvas.drawText(customerName, 10,(height-55),paint);
		canvas.drawText(customerAddress, 10,(height-45),paint);
		canvas.drawText(strDate, 10,(height-35),paint);
		canvas.drawText("Remarks: "+imageRemarks, 10,(height-20),paint);
		return picture;
	}


	private boolean prepareDirectory() {
		try {
            return makedirs();
		} catch (Exception e) {
			e.printStackTrace();
			DbUtil.makeToast(LayoutInflater.from(context), "Could not initiate File System.. Is Sdcard mounted properly?" , context,null,1);
			return false;
		}
	}

	private boolean makedirs() {
		File tempdir = new File(devPath);
		if (!tempdir.exists())
			tempdir.mkdirs();
		return (tempdir.isDirectory());
	}

	private void deleteLastPhotoTaken() {

		String[] projection = new String[] {
				MediaStore.Images.ImageColumns._ID,
				MediaStore.Images.ImageColumns.DATA,
				MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
				MediaStore.Images.ImageColumns.DATE_TAKEN,
				MediaStore.Images.ImageColumns.MIME_TYPE };

		final Cursor cursor = getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
				null,null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

		if (cursor != null) {
			cursor.moveToFirst();

			int column_index_data =
					cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

			String image_path = cursor.getString(column_index_data);
//			Log.e("sdpath",image_path);
			File file = new File(image_path);
			if (file.exists()) {
				file.delete();
				System.out.println("file Deleted :" + image_path);
				deleteFileFromMediaStore(context.getContentResolver(),file);
			}else System.out.println("file not Deleted :" + image_path);
		}
	}

	public static void deleteFileFromMediaStore(final ContentResolver contentResolver, final File file) {
		String canonicalPath;
		try {
			canonicalPath = file.getCanonicalPath();
		} catch (IOException e) {
			canonicalPath = file.getAbsolutePath();
		}
		final Uri uri = MediaStore.Files.getContentUri("external");
		final int result = contentResolver.delete(uri,
				MediaStore.Files.FileColumns.DATA + "=?", new String[] {canonicalPath});
		if (result == 0) {
			final String absolutePath = file.getAbsolutePath();
			if (!absolutePath.equals(canonicalPath)) {
				contentResolver.delete(uri,
						MediaStore.Files.FileColumns.DATA + "=?", new String[]{absolutePath});
			}
		}
	}

    private void getCustomerBdayList(int days){
        CustomerDbManager db = new CustomerDbManager(context);
        db.open();
        List<CustomerData> list = db.getOneTwoBday(days);
        db.close();

        int total = list.size();
        int i = 0;
//        Log.e("total","tot"+total);
        getBdayAlerts(i, total, list,days);
    }

    private void getBdayAlerts(int i,final int total,final List<CustomerData> list,final int days) {

        if(i == total){
            if(days==2) getCustomerBdayList(1);
            else if(days==1) getCustomerBdayList(0);
        }
        else if(i<total){
            String contactname = list.get(i).getOname();

            CustomerDbManager db = new CustomerDbManager(context);
            db.open();
            String customerName = db.getCustomerName(list.get(i).getCcode());
            db.close();

            String[] splitName = customerName.split("\\(");
            customerName = customerName.replace("("+splitName[splitName.length-1],"");
            i++;
            final int counter = i;
            final MediaPlayer mMediaPlayer = new MediaPlayer();
            try {
                Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                mMediaPlayer.setDataSource(this, alert);
                final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                if (audioManager.getStreamVolume(AudioManager.STREAM_RING) != 0) {
                    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
                    mMediaPlayer.setLooping(true);
                    mMediaPlayer.prepareAsync();
                    mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            mediaPlayer.start();
                        }
                    });
                }
            } catch (Exception e) {
            }

            final Vibrator v = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            long[] pattern = {0, 100, 100, 1000, 100, 100};
            v.vibrate(pattern, 2);

//            String strDay = days > 0 ? "after "+days+" day" : "today";
            String msg = "Tomorrow is the birthday of "+contactname.trim()+" of "+customerName.trim()+".";
            String msg2 = "Two days from now is the birthday of "+contactname.trim()+" of "+customerName.trim()+".";
            String today = "Today is the birthday of "+contactname.trim()+" of "+customerName.trim()+".";
            if(days==0) msg=today;
            else if(days==2) msg=msg2;
            new AlertDialog.Builder(this)
                    .setTitle("Birthday Alarm")
                    .setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            v.cancel();
                            mMediaPlayer.stop();
//                            Log.e("count",""+counter);
                            getBdayAlerts(counter,total,list,days);
                        }

                    })
                    .show();
        }
    }
}


