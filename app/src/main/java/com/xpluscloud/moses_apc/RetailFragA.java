package com.xpluscloud.moses_apc;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
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

import com.xpluscloud.moses_apc.dbase.MerchandisingDbManager;
import com.xpluscloud.moses_apc.dbase.RetailDataDbManager;

import java.util.HashMap;
import java.util.List;

public class RetailFragA extends Fragment {

	LinearLayout ll;
	
	String ccode;
	String devId;
	
	Context context;
	
	List<HashMap<String,String>> listem;

    public interface onSomeEventListener {
        void someEvent(String s, boolean b);
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

        View rootView = inflater.inflate(R.layout.first_frag1, container, false);

        context = getActivity();

//		TextView tv3 = (TextView) rootView.findViewById(R.id.textView2);
////		tv3.setText("IOH");

        ccode = getArguments().getString("ccode");
        devId = getArguments().getString("devId");
        
        ll = (LinearLayout) rootView.findViewById(R.id.mylinear);
        
        contentSetup();

        return rootView;
    }

    public static RetailFragA newInstance(String ccode, String devId) {

        RetailFragA f = new RetailFragA();
        Bundle b = new Bundle();
        b.putString("ccode", ccode);
        b.putString("devId", devId);

        f.setArguments(b);

        return f;
    }
    
    
    private void contentSetup(){	
		MerchandisingDbManager db = new MerchandisingDbManager(context);
	    db.open();
	    listem = db.getList(1);
	    db.close();
	    
		ll.removeAllViews();
        int i=0;
        Log.e("frag","A");
        for(HashMap data : listem){
            final View inflateView = View.inflate(context,R.layout.checklist_item,null);

//            if(i % 2 ==1) inflateView.setBackgroundColor(Color.rgb(231, 249, 255));
//            else inflateView.setBackgroundColor(Color.rgb(195, 240, 255));

//            final TextView description = (TextView) inflateView.findViewById(R.id.retDescription);
//            final EditText etValue =  (EditText) inflateView.findViewById(R.id.retValue);
//            etValue.setInputType(InputType.TYPE_CLASS_NUMBER);

//            description.setText(data.get("description").toString());
//            etValue.setText(""+data.getIoh());
//            Log.e("frag","A"+data.getIoh());
//            etValue.setOnFocusChangeListener(new etFocus(etValue));
//            etValue.addTextChangedListener(new etTextWatcher(data.getComcode()));

            final TextView tvMCID = inflateView.findViewById(R.id.tv_mcid);
            TextView tvMerch = inflateView.findViewById(R.id.tv_checklist);
            tvMerch.setText(data.get("description").toString());
            tvMCID.setText(data.get("mcid").toString());

            CheckBox cbMerch = inflateView.findViewById(R.id.cb_checklist);
            cbMerch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                    someEventListener.someEvent("Fragment A check-"+b+"-"+tvMCID.getText().toString());
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
            String strioh = myEditText.getText().toString();
            final int ioh = strioh.equals("") ? 0 : Integer.valueOf(strioh);
            if (b) {
                if (ioh == 0) myEditText.setText("");
                myEditText.setBackgroundColor(0xFFFFF3CE);
            }else{
                if(myEditText.getText().length()<1) myEditText.setText("0");
                myEditText.setBackgroundColor(0xFFA9FF53);
            }
        }
    }

    private class etTextWatcher implements TextWatcher{
        String comCode;

        public etTextWatcher(String comcode) {
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
            try{
                db.updateTypes(comCode, s.toString(), 1);
            }catch (Exception e) {e.printStackTrace();}
            db.close();
        }
    }

}
