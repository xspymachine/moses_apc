package com.xpluscloud.mosesshell_davao.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public final class NumUtil {  

    public static final String TAG = "Num Util";
    
	public static Double strToDouble(String strNum) {
		double value=0.0d;
		
		if(strNum.length()<=0) return value;
		
		try {		
			strNum = strNum.trim();
			strNum = strNum.replaceAll(",", "");
			value = strNum.equals("") ? 0.0d : Double.valueOf(strNum).doubleValue();
		}catch (Exception e) {
			value=0.0d;
		}
		return value;
	}
	
	public static Integer strToInt(String strNum) {
		int value=0;
		
		if(strNum.length()<=0) return value;
		
		try {		
			strNum = strNum.trim();
			strNum = strNum.replaceAll(",", "");
			value = strNum.equals("") ? 0 : Integer.valueOf(strNum).intValue();
		}catch (Exception e) {
			value=0;
		}
		return value;
	}
	


	public static String getPhpCurrency(Double value, Boolean cSymbol) {
		
		String currencySymbol ="";
		if (cSymbol) currencySymbol="P";
		
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.getDefault());
		DecimalFormatSymbols symbol =
                new DecimalFormatSymbols(Locale.getDefault());
        		symbol.setCurrencySymbol(currencySymbol);
        		formatter.setDecimalFormatSymbols(symbol);		
		
		return formatter.format(value);
	}


}
       