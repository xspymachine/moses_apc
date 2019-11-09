package com.xpluscloud.mosesshell_davao;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.xpluscloud.mosesshell_davao.dbase.CompetitorDbManager2;
import com.xpluscloud.mosesshell_davao.dbase.ItemDbManager;

import java.util.ArrayList;

public class ItemOptionDialogFragment2 extends DialogFragment {

    private OptionDialogListener callback;

    String Title="Brand Family";
    //	ArrayList<String> Options = new ArrayList<>();
    String[] Options;


    public ItemOptionDialogFragment2(){
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

            CompetitorDbManager2 db = new CompetitorDbManager2(getActivity().getApplicationContext());
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

