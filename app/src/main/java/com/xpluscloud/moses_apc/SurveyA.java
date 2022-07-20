package com.xpluscloud.moses_apc;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xpluscloud.moses_apc.dbase.SurveyDbManager;
import com.xpluscloud.moses_apc.getset.Survey;

import java.util.List;

/**
 * Created by Shirwen on 8/18/2017.
 */

public class SurveyA extends Fragment {
    LinearLayout ll;

    String ccode;
    String devId;

    Context context;

    List<Survey> listem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.first_frag2, container, false);

        context = getActivity();

        TextView tv3 = (TextView) v.findViewById(R.id.textView2);
        tv3.setText("Order Freq");

        ccode = getArguments().getString("ccode");
        devId = getArguments().getString("devId");

        ll = (LinearLayout) v.findViewById(R.id.mylinear);

        contentSetup();

        return v;
    }

    public static SurveyA newInstance(String ccode, String devId) {

        SurveyA f = new SurveyA();
        Bundle b = new Bundle();
        b.putString("ccode", ccode);
        b.putString("devId", devId);

        f.setArguments(b);

        return f;
    }


    private void contentSetup(){
        SurveyDbManager db = new SurveyDbManager(context);
        db.open();
        listem = db.getList(ccode);
        db.close();
        Log.e("getlist","asd="+listem.size());
        ll.removeAllViews();
        int i=0;

        for(Survey data : listem){
            final View inflateView = View.inflate(context,R.layout.retail_listview,null);

            if(i % 2 ==1) inflateView.setBackgroundColor(Color.rgb(231, 249, 255));
            else inflateView.setBackgroundColor(Color.rgb(195, 240, 255));

            final TextView description = (TextView) inflateView.findViewById(R.id.retDescription);
            final EditText etValue =  (EditText) inflateView.findViewById(R.id.retValue);
            etValue.setInputType(InputType.TYPE_CLASS_NUMBER);

            description.setText(data.getDescription());
            etValue.setText(""+data.getFrequency());
            Log.e("frag","A"+data.getFrequency());

            etSettings(etValue,data.getScode());

            ll.addView(inflateView);
            i++;
        }

    }

    private void etSettings(final EditText edittext, final String comCode){
        edittext.setBackgroundColor(0xFFA9FF53);
        edittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                String strioh = edittext.getText().toString();
                final int ioh = strioh.equals("") ? 0 : Integer.valueOf(strioh);

                if (hasFocus){
                    if(ioh == 0) edittext.setText("");
                    edittext.setBackgroundColor(0xFFFFF3CE);
                    edittext.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count,
                                                      int after) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            // TODO Auto-generated method stub
                            Log.e("code",s.toString());
                            SurveyDbManager db = new SurveyDbManager(context);
                            db.open();
                            try{
                                db.updateTypes(comCode, s.toString(), 1);
                            }catch (Exception e) {e.printStackTrace();}
                            db.close();
                        }
                    });
                }else {
                    if(ioh == 0) edittext.setText("0");
                    edittext.setBackgroundColor(0xFFA9FF53);
                }
            }
        });
    }
}
