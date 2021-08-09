package com.xpluscloud.moses_apc;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.xpluscloud.moses_apc.dbase.CustomerDbManager;
import com.xpluscloud.moses_apc.dbase.OutboxDbManager;
import com.xpluscloud.moses_apc.dbase.SyncDbManager;
import com.xpluscloud.moses_apc.dbase.WipeDataDbManager;
import com.xpluscloud.moses_apc.util.DbUtil;
import com.xpluscloud.moses_apc.util.Master;

import org.json.JSONArray;

import java.util.Random;

/**
 * Created by Shirwen on 3/7/2017.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

//        String title      = data.getString("title");
//        String msg        = data.getString("message");
//        String url        = data.getString("url");
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            try {
                String cmdStr = remoteMessage.getData().get("cmd");
                String message = remoteMessage.getData().get("msg");
                int cmd = 0;
                if (!cmdStr.isEmpty() || cmdStr != null) cmd = Integer.parseInt(cmdStr);

                ProcessMessage(cmd, message, remoteMessage, getApplicationContext());
            }catch (Exception e){e.printStackTrace();}
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

//        ProcessMessage(0, "", remoteMessage, getApplicationContext());
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    private void ProcessMessage(int cmd, String message, RemoteMessage intent, Context context){
        String msg_notif = "";
        String ccode = "";
        String customerName = "";
        int opt = 0;
        String link_url = "";
        CustomerDbManager db;
        OutboxDbManager db2;

        switch(cmd){
            case Master.CMD_APPROVED:
                ccode = intent.getData().get("ccode");
                db = new CustomerDbManager(context);
                db.open();
                customerName = db.getCustomerName(ccode);
                db.setStatus(ccode, 21);
                db.close();
                msg_notif = customerName+" has been approved.";
                break;

            case Master.CMD_DISAPPROVED:
                ccode = intent.getData().get("ccode");
                db = new CustomerDbManager(context);
                db.open();
                customerName = db.getCustomerName(ccode);
                db.setStatus(ccode, 10);
                db.close();
                msg_notif = customerName+" has been disapproved.";
                break;

            case Master.CMD_SYNC:
                String table_name = intent.getData().get("tbl_name");
                int limit = Integer.parseInt(intent.getData().get("limit"));
                int status = Integer.parseInt(intent.getData().get("status"));
                sync_datas(table_name,limit,status,context);
                msg_notif = "Data is being sync to server.";
                break;

            case Master.CMD_RESEND:
                long dateFrom = DbUtil.dateTomilli(intent.getData().get("dateFrom"));
                long dateTo = 86400000l+ DbUtil.dateTomilli(intent.getData().get("dateTo"));

                db2 = new OutboxDbManager(context);
                db2.open();
                db2.resendDateRange(dateFrom,dateTo);
                db2.close();
                msg_notif = "Outbox messages are being resend.";
                break;

            case Master.CMD_NEWUPDATE:
                link_url = intent.getData().get("link_url");
                opt = 2;
                msg_notif = "Please update your app by tapping this notification.";
                break;

            case Master.CMD_SENDSIGNATURE:
                break;
            case Master.CMD_LOSTPASSWORD:
                opt = 1;
                msg_notif = "Request to create a new password, just tap this notification.";
                break;
            case Master.CMD_MESSAGE:
                msg_notif = message;
                break;
            case Master.CMD_WIPE:
                wipeData(context);
                msg_notif = "All Data being deleted.";
                break;

            default: msg_notif = "Device is registered to the server.";
                break;
        }

        generateNotification(context, msg_notif, opt,link_url);

    }

    public void generateNotification(Context context, String message, int opt, String link) {
        String title = context.getString(R.string.app_name);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent();
        switch(opt){
            case 1: notificationIntent = new Intent(context, LostPasswordActivity.class);
                break;
            case 2: notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                break;
        }
        PendingIntent pi = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        android.app.Notification.Builder builder = new Notification.Builder(
                this);
        builder.setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.assets_icon)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setStyle(new Notification.BigTextStyle().bigText(message))
                .setContentIntent(pi)
                .setAutoCancel(true);
        Notification notification = new Notification.BigTextStyle(builder)
                .bigText(message).build();

        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        Log.e(TAG, "Message Notification Body: notify");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }

        notificationManager.notify(m, notification);
    }

    private void sync_datas(String table_name, int limit, int status, Context context){

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

    private void wipeData(Context context){
        WipeDataDbManager db = new WipeDataDbManager(context);
        db.open();
        db.dropTables();
        db.close();
    }

}
