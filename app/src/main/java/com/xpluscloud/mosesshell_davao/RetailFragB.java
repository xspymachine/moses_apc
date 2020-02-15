package com.xpluscloud.mosesshell_davao;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xpluscloud.mosesshell_davao.dbase.DesContract;
import com.xpluscloud.mosesshell_davao.dbase.MerchandisingDbManager;
import com.xpluscloud.mosesshell_davao.dbase.RetailDataDbManager;
import com.xpluscloud.mosesshell_davao.getset.Retail;
import com.xpluscloud.mosesshell_davao.util.DbUtil;

import java.util.HashMap;
import java.util.List;

public class RetailFragB extends Fragment {
        LinearLayout ll;

        String ccode;
        String devId;

        Context context;

        List<HashMap<String,String>> listem;

    public interface onSomeEventListener{
        void someEvent(String s,boolean b);
    }

    onSomeEventListener someEventListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            someEventListener = (onSomeEventListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onSomeEventListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.first_frag2, container, false);

        context = getActivity();

//        TextView tv3 = (TextView) v.findViewById(R.id.textView2);
//        tv3.setText("Selling Price");

        ccode = getArguments().getString("ccode");
        devId = getArguments().getString("devId");

        ll = (LinearLayout) v.findViewById(R.id.mylinear);

        contentSetup();

        return v;
    }

    public static RetailFragB newInstance(String ccode, String devId) {

        RetailFragB f = new RetailFragB();
        Bundle b = new Bundle();
        b.putString("ccode", ccode);
        b.putString("devId", devId);

        f.setArguments(b);

        return f;
    }

    private void contentSetup(){
        MerchandisingDbManager db = new MerchandisingDbManager(context);
        db.open();
        listem = db.getList(2);
        db.close();

        ll.removeAllViews();
        Log.e("frag","B");

        int i=0;
        for(HashMap<String,String> data : listem){
            final View inflateView = View.inflate(context,R.layout.checklist_item,null);

//            if(i % 2 ==1) inflateView.setBackgroundColor(Color.rgb(231, 249, 255));
//            else inflateView.setBackgroundColor(Color.rgb(195, 240, 255));
//
//            final TextView description = (TextView) inflateView.findViewById(R.id.retDescription);
//            final EditText etValue =  (EditText) inflateView.findViewById(R.id.retValue);
//            etValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
//
//            description.setText(data.getDescription());
//            etValue.setText(""+data.getPrice());
//            Log.e("frag","B"+data.getPrice());
//
//            etValue.setOnFocusChangeListener(new etFocus(etValue));
//            etValue.addTextChangedListener(new etTextWatcher(etValue,data.getComcode()));

            final TextView tvMCID = inflateView.findViewById(R.id.tv_mcid);
            TextView tvMerch = inflateView.findViewById(R.id.tv_checklist);
            tvMerch.setText(data.get("description"));
            tvMCID.setText(data.get("mcid"));

            CheckBox cbMerch = inflateView.findViewById(R.id.cb_checklist);
            cbMerch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                    someEventListener.someEvent("Fragment B check-"+b+"-"+tvMCID.getText().toString());
                    someEventListener.someEvent(tvMCID.getText().toString(),b);
                }
            });

            ll.addView(inflateView);
            i++;
        }
    }

    private class etFocus implements OnFocusChangeListener{
        EditText myEditText;
        public etFocus(EditText etValue) {
            this.myEditText = etValue;
        }

        @Override
        public void onFocusChange(View view, boolean b) {
            String strPrice = myEditText.getText().toString();
            final double value = strPrice.equals("") ? 0 : Double.valueOf(strPrice);

            if(b){
                if(value == 0) myEditText.setText("");
                myEditText.setBackgroundColor(0xFFFFF3CE);
            }else {
                if(myEditText.getText().length()<1) myEditText.setText("0.00");
                myEditText.setBackgroundColor(0xFFA9FF53);
            }
        }
    }

    private class etTextWatcher implements TextWatcher{

        EditText myEditText;
        String comCode;

        public etTextWatcher(EditText etValue, String comcode) {
            this.myEditText = etValue;
            this.comCode = comcode;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.e("code",s.toString());
            RetailDataDbManager db = new RetailDataDbManager(context);
            db.open();
            if(Integer.parseInt(db.getDataValue(comCode, DesContract.RetailData.IOH)) < 1){
                DbUtil.makeToast(LayoutInflater.from(context), "Please input IOH first." , context,null,0);
                if(myEditText.getText().toString().length()>0) myEditText.setText("");
                return;
            }
            try{
                db.updateTypes(comCode, s.toString(), 2);
            }catch (Exception e) {e.printStackTrace();}
            db.close();
        }
    }

}