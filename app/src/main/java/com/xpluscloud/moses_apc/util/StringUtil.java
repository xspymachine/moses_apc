package com.xpluscloud.moses_apc.util;

import android.content.Context;

import java.util.Random;

public final class StringUtil {  

    static final String TAG = "StringUtil";
    
    public final static String strCleanUp(String source){
    	String result="";
    
    	result = source.replaceAll("'", "`");    	
    	result = result.replaceAll(";", ",");
    
    	return result;
	}
    
    
    
    public static String randomText(int len ) 	{
		 String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		 Random rnd = new Random();
		StringBuilder sb = new StringBuilder( len );
		for( int i = 0; i < len; i++ ) 
			sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
		return sb.toString();
	}

	public static String get_Code(String ccode, int lastId, Context ctx){
		String devId = DbUtil.getSetting(ctx, Master.DEVID);

//        String strCode = ("00000" + (lastId+1)).substring(String.valueOf(lastId).length());
        String strRandom = randomText(5);
//        strCode = devId.substring(devId.length() - 5) + strRandom + strCode ;
		String strCode = "Mos"+ccode+lastId+strRandom;

		return strCode;
	}

}
       