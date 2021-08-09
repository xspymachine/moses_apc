package com.xpluscloud.moses_apc;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DatePickerFragment extends DialogFragment implements
DatePickerDialog.OnDateSetListener {

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    // Use the current date as the default date in the picker
	    final Calendar c = Calendar.getInstance();
	    int year = c.get(Calendar.YEAR);
	    int month = c.get(Calendar.MONTH);
	    int day = c.get(Calendar.DAY_OF_MONTH);
	
	    // Create a new instance of DatePickerDialog and return it
	    return new DatePickerDialog(getActivity(), this, year, month, day);
	}
	
	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
	    // do some stuff for example write on log and update TextField on activity
	    Log.w("DatePicker","Date = " + year);
	    ((TextView) getActivity().findViewById(R.id.tvBucket)).setText(month+1 + "/" + day + "/" + year);
	}
}