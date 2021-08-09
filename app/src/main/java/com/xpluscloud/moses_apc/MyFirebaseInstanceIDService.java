package com.xpluscloud.moses_apc;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.xpluscloud.moses_apc.util.DbUtil;
import com.xpluscloud.moses_apc.util.Master;

/**
 * Created by Shirwen on 3/7/2017.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
//        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        String devId = DbUtil.getSetting(getApplicationContext(), com.xpluscloud.mpasta.util.Master.DEVID);
//
//        while (devId == null) {
//            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            devId = telephonyManager.getDeviceId();
//        }
//        DbUtil.saveSetting(getApplicationContext(), "refreshedToken",refreshedToken);
//        String message = Master.CMD_TOK + " " +
//                devId + ";" +
//                refreshedToken + ";" +
//                System.currentTimeMillis()/1000;
//
//        DbUtil.saveMsg(getApplicationContext(), DbUtil.getGateway(getApplicationContext()), message);
        // [END register_for_fcm]

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        String devId = "";
        while (devId == null || devId.isEmpty()) {
            devId = Master.getDevId(getApplicationContext());

            DbUtil.saveSetting(getApplicationContext(), Master.DEVID, devId);
        }
        DbUtil.saveSetting(getApplicationContext(), "refreshedToken",token);
        String message = Master.CMD_TOK + " " +
                devId + ";" +
                token + ";" +
                System.currentTimeMillis()/1000;

        DbUtil.saveMsg(getApplicationContext(), DbUtil.getGateway(getApplicationContext()), message);
    }

}
