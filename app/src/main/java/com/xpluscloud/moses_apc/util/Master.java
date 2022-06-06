package com.xpluscloud.moses_apc.util;


import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.Arrays;

public final class Master {

    /**************************
     * Client Company Exclusive
     **************************/

    public final static String COMPANY_NAME = "apc";

    public final static String[] pckg_option = {
            "CTN",
            "BOTTLE",
    };

    public final static String[] sc_option = {
            "Face-to-Face Booking",
            "Off-Route Booking"
    };

    public final static String DEVID = "devid";

    public final static int PCKG_PACK = 0;
    public final static int PCKG_UNIT = 1;

    public final static String PACK = "Case";
    public final static String UNIT = "Pc";
    public final static String PACK2 = "CTN";
    public final static String UNIT2 = "BOTTLE";

    public final static String PACKS = "CTN";
    public final static String UNITS = "CTN";

    public final static String QTY = "Qty";
    public final static String PRICE = "Price";
    public final static String INVENTORY = "Inventory";
    public final static String SUGGESTED = "Suggested";

    public final static String PRICE_PER_PACK = "PPCase";
    public final static String PRICE_PER_UNIT = "PPPiece";

    public final static String INVENTORY_PACK = "Invt Cases";
    public final static String INVENTORY_UNIT = "Invt Pieces";

    public final static String SUGGEST_PACK = "Sug. Order";
    public final static String SUGGEST_UNIT = "Sug. Order";

    public final static String QTY_PACK = "Qty";
    public final static String QTY_UNIT = "Qty";

    public final static String AMOUNT_PACK = "Amount";
    public final static String AMOUNT_UNIT = "Amount";
	
	/*
	 * public final static String PRICE_PER_PACK = "Price/Case";
	public final static String PRICE_PER_UNIT = "Price/Pc";
	
	public final static String INVENTORY_PACK = "Inventory (Cases)";
	public final static String INVENTORY_UNIT = "Inventory (Pcs)";
	
	public final static String SUGGEST_PACK = "Suggested (Cases)";
	public final static String SUGGEST_UNIT = "Suggested (Pcs)";
	
	public final static String QTY_PACK = "Qty (Cases)";
	public final static String QTY_UNIT = "Qty (Pcs)";
	
	public final static String AMOUNT_PACK = "Amount (Cases)";
	public final static String AMOUNT_UNIT = "Amount (Pcs)";
	 */

    public final static Integer TOP_SELLERS_LIST_SIZE = 30;


    /*******************************
     * International Prefix
     *******************************/

    public final static String COUNTRY_CODE = "+63";


    /*******************************
     *Incoming Senders ***********
     *******************************/

    public final static ArrayList<String> commanders =
            new ArrayList<String>(Arrays.asList(
                    "9310600181",
                    "9171084984", //support
                    "9692422959", //receiver
                    "9777171658", //receiver
                    "9178884134"  //sender
            ));
//            new ArrayList<String>(Arrays.asList(
//                    "9176252408",//instinct
//                    "9178125267",
//                    "9088753479",
//                    "9228583243",
//                    "9228583242",
//                    "9237498247",
//                    "9328786950",
//                    "9328786956",
//                    "9778258885", //sir globe
//                    "9328786951",
//                    "9778037710",
//                    "9239544406",
//                    "9253056025"));

    public final static String expiry = "2050-08-30";

    public final static Long GMT = 8l;

    /*******************************
     * Constants ***********
     *******************************/

    public final static String SMS_GATEWAY = "smsGateway";
    public final static String SEND_INTERVAL = "sendInterval";

    /********************************
     * Outgoing gateway
     *******************************///smart - +639989720280
//    public final static String INIT_GATEWAY_GLOBE = "+639771049747"; //globe
//    public final static String INIT_GATEWAY_GLOBE = "+639274010081"; //globe test my number
//    public final static String INIT_GATEWAY_SMART 	= "+639989720280"; //smart
////	public final static String INIT_GATEWAY_SUN 	= "+639255208007"; //sun
//    public final static String INIT_GATEWAY_SUN 	= "+639310600190"; //sun test my number

    public final static String INIT_GATEWAY_GLOBE = "+639777171658"; //globe
    public final static String INIT_GATEWAY_SMART 	= "+639692422959"; //smart
    public final static String INIT_GATEWAY_SUN 	= "+639692422959"; //sun
    public final static String STR_GATEWAY_GLOBE    = "GLOBE"; //globe
    public final static String STR_GATEWAY_SMART 	= "SMART"; //smart
    public final static String STR_GATEWAY_SUN 	    = "SUN"; //sun
    public final static Long INIT_SEND_INTERVAL = 1000 * 60 * 10l;
//    public final static String INIT_GATEWAY = Prefs.getString("gateway",INIT_GATEWAY_SMART);
//    public final static String STR_GATEWAY = Prefs.getString("strgateway",STR_GATEWAY_SMART);


    /*******************************
     * Outgoing  Commands ***********
     *******************************/
    public final static int FOR_APPROVAL_SETTING = 0; //if this app is for approval status 1
    public final static String CMD_REGISTER = "CMDREG1";
    public final static String CMD_INOUT = "CMDTIO"; //Time In Out

    public final static String CMD_ACUS = "CMDCUA2"; //add customer
    public final static String CMD_UCUS = "CMDCUU2"; //update customer
    public final static String CMD_DCUS = "CMDCUD"; //delete customer
    public final static String CMD_CUSM = "CMDCUSM";//customer meta

    public final static String CMD_LOCATION = "CMDLOC";    //Location Update

    public final static String CMD_UPDATE = "CMDUDT"; //Dev Status Update

    public final static String CMD_MTL = "CMDMRK"; //Mark Location

    //	public final static String CMD_CUSCALL		= "CMDCCL2"; //Customer Call
    public final static String CMD_SALESCALL = "CMDSCL"; //Sales Call

    public final static String CMD_SALESORDER = "CMDSOR"; //Sales Order

    public final static String CMD_SIGNATURE = "CMDSIG"; //Signature Info

    public final static String CMD_PICTURE = "CMDPIC"; //PICTURE Info
    public final static String CMD_PICTURE2 = "CMDPIC2"; //PICTURE Info remarks
    public final static String CMD_MERCH_PICTURE	= "CMDMPC"; //Merch Customer image Info
    public final static String CMD_DEL_PICTURE		= "CMDDPC"; //Deleted Customer image Info
    public final static String CMD_PO_PICTURE		= "CMDPOPC"; //PO Doc Customer image Info

    public final static String CMD_CALLSHEET = "CMDCST"; //Callsheet

    public final static String CMD_CALLSHEET_ITEM = "CMDCSI"; //Callsheet Item
    public final static String CMD_CALLSHEET_INVEN = "CMDINV"; //Callsheet Item

    public final static String CMD_SET = "CMDSET"; //Settings command

    public final static String CMD_BOOT = "CMDBOT"; //Boot up command

    public final static String GPS_OFF = "GPSOFF";

    public final static String GPS_ON = "GPSON";

    public final static String CMD_GPS = "CMDGPS";

    public final static String CMD_COLLECTION = "CMDCOL1";

    public final static String PRODUCT_RETURN = "CMDRET";

    public final static String COMPETITORS_PRICE = "CMDCMP5"; //competitors item add
    public final static String SURVEY = "CMDSUR"; //competitors item add

    public final static String OWNER = "CMDOWN3"; //new data for customer owner

//	public final static String PURCHASER = "CMDPUR"; //new data for customer purchaser

    public final static String ODATA = "CMDODATA"; //other data

    public final static String CMD_LOGOUT = "CMDLOG"; //In application start command

    public final static String CMD_MERCH = "CMDMER"; //Customer Call
    public final static String CMD_MSTAT = "CMDMSTAT"; //merchandising
    public final static String CMD_TRUCK = "CMDTRUCK"; //truckdetails
    public final static String CMD_WHCAP = "CMDWHCAP"; //warehouse capacity

    public final static String CMD_UCST = "CMDUCST"; //update served callsheets

    public final static String CMD_GPDATA = "CMDGPDATA"; //General Profile
    public final static String CMD_TOK = "CMDTOK"; //FCM tokener

    //REMARKS SEPARATE CMD
    public final static String CMDCMPREM = "CMPREM"; //CMP remarks
    public final static String CMDSURREM = "SURREM"; //SURVEY remarks
    public final static String CMDPMB = "CMDPMB"; //PROMO BUTTON
    public final static String CMD_INV = "CMDINV2"; //INVENTORY ACTIVITY
    public final static String CMD_INVI = "CMDINVI2"; //INVENTORY ACTIVITY ITEMS
    public final static String CMD_CMP6 = "CMDCMP6"; //INVENTORY ACTIVITY
    public final static String CMD_CMPI7 = "CMDCMPI7"; //INVENTORY ACTIVITY ITEMS

    /*CLOUD MESSAGING COMMANDS*/

    public static final int CMD_APPROVED = 2001;
    public static final int CMD_DISAPPROVED = 2002;
    public static final int CMD_SYNC = 2003;
    public static final int CMD_RESEND = 2004;
    public static final int CMD_NEWUPDATE = 2005;
    public static final int CMD_SENDSIGNATURE = 2006;
    public static final int CMD_LOSTPASSWORD = 2007;
    public static final int CMD_MESSAGE = 2008;
    public static final int CMD_WIPE = 2009;

    public static Integer getBatteryInfo(Context context) {
        Intent i = new ContextWrapper(context).registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        // now you can get the level and scale from this intent variable
        int level = i.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = i.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batLevel = (level / (float) scale) * 100;

        int battPercentage = (int) batLevel;
        return battPercentage;
    }

    public static Integer getStatGPS(Context context) {
        Integer statGPS = 0;
        try {
            LocationManager mlocGPS = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (mlocGPS.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                statGPS = 1;
            }

        } catch (Exception ex) {
        }
        return statGPS;
    }

    public static String getDevId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
        }
        return Master.getDevId2(context);
//        return telephonyManager.getDeviceId();
	}

    public static String getDevId2(Context ctx) {
        if (Build.VERSION.SDK_INT >= 29) {
            return Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager.getDeviceId();
        }
    }
	
}





