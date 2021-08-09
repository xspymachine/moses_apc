package com.xpluscloud.moses_apc;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by Shirwen on 4/18/2018.
 */

public class Brochure01 extends Activity {

    ArrayList<Integer> mResources = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);

        Bundle b = getIntent().getExtras();
        String selected = b.getString("selected");

        switch (selected){
            case "SFA detailer": mResources.add(R.drawable.assets_sfa01);
                mResources.add(R.drawable.assets_sfa02);break;
            case "SFA mechanics": mResources.add(R.drawable.assets_sfa0001);break;
            case "SFA full throttle":mResources.add(R.drawable.assets_sfa001);
                mResources.add(R.drawable.assets_sfa002);
                mResources.add(R.drawable.assets_sfa003);break;
        }

        CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(this,mResources);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCustomPagerAdapter);
    }

    private class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;
        ArrayList<Integer> mResources;

        public CustomPagerAdapter(Context context,ArrayList<Integer> _mResources) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mResources = _mResources;
        }

        @Override
        public int getCount() {
            return mResources.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

            TouchImageView imageView = (TouchImageView) itemView.findViewById(R.id.imageView);
            imageView.setImageResource(mResources.get(position));

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
}
