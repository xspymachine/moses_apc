package com.xpluscloud.moses_apc;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.util.Log;

import com.xpluscloud.moses_apc.dbase.DesContract;
import com.xpluscloud.moses_apc.dbase.InOutDbManager;
import com.xpluscloud.moses_apc.dbase.OutboxDbManager;
import com.xpluscloud.moses_apc.dbase.UtilDbManager;
import com.xpluscloud.moses_apc.getset.Outbox;
import com.xpluscloud.moses_apc.util.DateUtil;
import com.xpluscloud.moses_apc.util.DbUtil;
import com.xpluscloud.moses_apc.util.Master;

import org.json.JSONArray;

import java.util.Timer;
import java.util.TimerTask;

public class DispatcherService extends Service {

    private final static Integer NEW_MSG = 0;
    private final static Integer SENDING = 2;

    private final Long timerInterval = (long) 1000 * 10;
    private final static String TAG = "Dispatcher";
    private long lastFetch = 0;

    private Long longInterval = (long) 1000 * 15;
    private Time longTimer = new Time();

    private Long longIntervalVersion = (long) 1000 * 60 * 30;
    private Time longTimerVersion = new Time();

    Context context;
    String devId;

    TelephonyManager Tm;
    MyPhoneStateListener PhoneListener;

    public int gsmSignal;
    public Time lastSignal = new Time();
    public final Long validSignalDuration = 1000 * 60 * 3l;

    public static AsyncHttpPost asyncHttpPending;
    ConnectionDetector cd;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("Wakelock")
    @Override
    public void onCreate() {
        context = DispatcherService.this;

//		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//		devId = telephonyManager.getDeviceId();
//		devId = DbUtil.getSetting(context, Master.DEVID);
        devId = getDeviceImei();

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "DISPATCH_SERVICE:");
        wl.acquire();

        Settings.System.putInt(getContentResolver(),
                Settings.System.WIFI_SLEEP_POLICY,
                Settings.System.WIFI_SLEEP_POLICY_NEVER);

        gsmSignal = -1;
        lastSignal.setToNow();
        longTimer.setToNow();

        PhoneListener = new MyPhoneStateListener();
        Tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        Tm.listen(PhoneListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

        Log.i(TAG, "About to run timer...");

        cd = new ConnectionDetector(getApplicationContext());

        new fetch();

    }

    private class MyPhoneStateListener extends PhoneStateListener {
        /* Get the Signal strength from the provider, each tiome there is an update */
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);

            gsmSignal = signalStrength.getGsmSignalStrength();
            lastSignal.setToNow();
            //Log.i("Signal Strength",String.valueOf(signalStrength.getGsmSignalStrength()));
        }

    }

    ;/* End of private Class */


    ///**********************************
    public class fetch {
        final Handler handler = new Handler();
        public Timer t;

        public fetch() {
            t = new Timer();
            t.schedule(new fetchMessageTask(), 0, timerInterval);
        }

        public class fetchMessageTask extends TimerTask {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Time currentTime = new Time();
                        currentTime.setToNow();
                        long elapseTime = currentTime.toMillis(true) - lastFetch;
                        long timeInterval = timerInterval;
                        //long signalDuration = currentTime.toMillis(true) - lastSignal.toMillis(true);
                        long longEllapse = currentTime.toMillis(true) - longTimer.toMillis(true);
                        long longEllapseVersion = currentTime.toMillis(true) - longTimerVersion.toMillis(true);

//		    				devId = DbUtil.getSetting(context, Master.DEVID);

		    				if (!cd.isOnline()) {
//		    					Not connected to internet
								String distimer = DbUtil.getSetting(context,"distim");
								distimer = distimer == null ? "" : distimer;
								if(!distimer.isEmpty()) longInterval = Long.parseLong(distimer);
			    				if(longEllapse >= longInterval) {
			    					Log.i(TAG," Timer Tick... About to fetch pending messages...."+longInterval);
			    					ReadPending(context);
			    					longTimer = currentTime;
			    				}

			    				else if (elapseTime >= timeInterval ) {
			    					Log.i(TAG," Timer Tick... About to readOutbox....");
			    					ReadOutbox(context);
			    				}

		    				}
			    			else {
//			    				updateCustomers(devId);
//			    				lostPassword(devId);
//			    				checkWipeData(devId);
			    				UploadAllPending(context,devId);

			    				//UploadCustomers( context);
//			    				ReadOutbox(context);

//			    				if(longEllapseVersion >= longIntervalVersion) {
//			    					getNewVersion(devId);
//			    					longTimer = currentTime;
//			    				}
		    				}

                        checkLogin();
                        checkIO(devId);
                        lastFetch = SystemClock.elapsedRealtime();
                    }
                });
            }
        }
    }


    public static void ReadOutbox(Context context) {
        //Get messages from queue

        OutboxDbManager db = new OutboxDbManager(context);
        Outbox row = new Outbox();
        db.open();
        row = db.getSingleQueMsg(NEW_MSG);
        db.close();

        if (row != null) {
            Log.i(TAG, " Outbox ID# " + row.getId() + " fetched...");
            SendSmsAsyncTask asyncSend = new SendSmsAsyncTask(context, row.getId(), row.getRecipient(), row.getMessage());
            asyncSend.execute();
        }
    }

    public static void ReadPending(Context context) {
        //Get messages from queue

        OutboxDbManager db = new OutboxDbManager(context);
        Outbox row = new Outbox();
        db.open();

        row = db.getPendingMsg(SENDING);

        db.close();


        if (row != null) {
            Log.i(TAG, " Outbox ID# " + row.getId() + " fetched...");
            SendSmsAsyncTask asyncSend = new SendSmsAsyncTask(context, row.getId(), row.getRecipient(), row.getMessage());
            asyncSend.execute();
        }
    }

    public void UploadAllPending(Context context, String devId) {
        OutboxDbManager odb = new OutboxDbManager(context);
        odb.open();
        JSONArray pendingMsgs;
        pendingMsgs = odb.getAllUnsent();
        odb.close();

        if (pendingMsgs.length() <= 0) return;

        String sourceTable = DesContract.Outbox.TABLE_NAME;

//		AsyncHttpPost asyncHttp = new AsyncHttpPost(context,pendingMsgs,devId,sourceTable,"");
        String url = context.getResources().getString(R.string.upload_msgs);
//		asyncHttp.execute(url);

        if (asyncHttpPending == null || asyncHttpPending.getStatus() != AsyncTask.Status.RUNNING) {
            Log.i("AsyncStatus-AP", "!Running");
            asyncHttpPending = new AsyncHttpPost(context, pendingMsgs, devId, sourceTable, "");
            asyncHttpPending.execute(url);
        }

    }

    public void UploadCustomers(Context context) {
        String sourceTable = DesContract.Customer.TABLE_NAME;

        UtilDbManager db = new UtilDbManager(context);
        db.open();
        JSONArray newCustomers = new JSONArray();
        newCustomers = db.getJsonArray(sourceTable, "status=20");
        db.close();

        if (newCustomers.length() <= 0) return;

        AsyncHttpPost asyncHttp = new AsyncHttpPost(context, newCustomers, devId, sourceTable, "");
        String url = context.getResources().getString(R.string.upload_customers);
        asyncHttp.execute(url);

    }

    private void checkLogin() {

        final String SETTINGS = "MosesSettings";
        SharedPreferences settings;
        String sysDate;
        String logDate;

        settings = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        logDate = settings.getString("logDate", "");
        int isLogin = settings.getInt("isLogin", 0);
        long iDate = System.currentTimeMillis();
        sysDate = DateUtil.strDate(iDate);

        if (isLogin != 0 && !sysDate.equalsIgnoreCase(logDate)) {
            SharedPreferences.Editor prefEditor = settings.edit();
            prefEditor.putInt("isLogin", 0);
            prefEditor.apply();
        }
    }

    private void checkIO(String devId) {

        InOutDbManager db = new InOutDbManager(context);
        db.open();
        db.checkIO(devId);
        db.close();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        Intent intent = new Intent("restartService");
        String serviceClass = "com.xplus.xpdes.DispatcherService";
        intent.putExtra("serviceClass", serviceClass);
        sendBroadcast(intent);
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        // TODO Auto-generated method stub
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            this.onDestroy();
            super.onTaskRemoved(rootIntent);
        }
    }


    private String getDeviceImei() {
        String devId = DbUtil.getSetting(context, Master.DEVID);

        while (devId == null || devId.isEmpty()) {

            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(android.content.Context.TELEPHONY_SERVICE);
            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
//            devId = telephonyManager.getDeviceId();
            devId = Master.getDevId2(context);
			DbUtil.saveSetting(context, Master.DEVID, devId);
		}

		return devId;
	}
		
}