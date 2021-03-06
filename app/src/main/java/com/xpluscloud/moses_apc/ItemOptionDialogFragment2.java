package com.xpluscloud.moses_apc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.xpluscloud.moses_apc.dbase.CompetitorDbManager2;

import java.util.ArrayList;

public class ItemOptionDialogFragment2 extends DialogFragment {

    private OptionDialogListener callback;

    String Title="Product ID";
    //	ArrayList<String> Options = new ArrayList<>();
    String[] Options;
    int opt;


    public ItemOptionDialogFragment2(){
        //Empty constructor required for DialogFragment
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            callback = (OptionDialogListener) getActivity();

            Bundle args = getArguments();
            opt = args.getInt("opt", 0);
            int catid = args.getInt("catid", 0);

            CompetitorDbManager2 db = new CompetitorDbManager2(getActivity().getApplicationContext());
            db.open();
            ArrayList<String> category = db.getCategoryList(opt,catid);
            db.close();
            if(opt>1) Title = "Product Type";
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
                        callback.onSelectOption(position,Options[position], opt);
                    }
                });
        return builder.create();
    }


    public interface OptionDialogListener {
        void onSelectOption(Integer position, String strValue, int opt);
    }

}

