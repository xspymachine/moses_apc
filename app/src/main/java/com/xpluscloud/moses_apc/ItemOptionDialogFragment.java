package com.xpluscloud.moses_apc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.xpluscloud.moses_apc.dbase.ItemDbManager;

import java.util.ArrayList;

public class ItemOptionDialogFragment extends DialogFragment {

	private OptionDialogListener callback;
	
	String Title="Brand Family";
//	ArrayList<String> Options = new ArrayList<>();
	String[] Options;
	
	
	public ItemOptionDialogFragment(){
		//Empty constructor required for DialogFragment
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try {
			callback = (OptionDialogListener) getActivity();

			Bundle args = getArguments();
			int opt = args.getInt("opt", 0);
			int catid = args.getInt("catid", 0);

			Log.e("sql",opt+"--"+catid);
            ItemDbManager db = new ItemDbManager(getActivity().getApplicationContext());
            db.open();
            ArrayList<String> category = db.getCategoryList(opt,catid);
            db.close();
			if(opt>1) Title = "Brand Name";
            Options = category.toArray(new String[category.size()]);
			//this.dismiss();
		}catch (ClassCastException e){
			throw new ClassCastException("Calling Activity must implement DialogClickListener interface");
		}
		
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle(Title)
	           .setItems(Options, new DialogInterface.OnClickListener() {
	               @Override
				public void onClick(DialogInterface dialog, int position) {
	            	   callback.onSelectOption(position,Options[position]);
	               }
	    });
	    return builder.create();
	}
	
	
	public interface OptionDialogListener {
		void onSelectOption(Integer position, String strValue);
	}
	
}

