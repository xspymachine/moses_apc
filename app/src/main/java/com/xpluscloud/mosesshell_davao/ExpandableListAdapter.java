package com.xpluscloud.mosesshell_davao;

import android.app.Activity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.xpluscloud.mosesshell_davao.getset.Delivery;
import com.xpluscloud.mosesshell_davao.util.NumUtil;


public class ExpandableListAdapter extends BaseExpandableListAdapter {

  private final SparseArray<GroupDelivery> groups;
  public LayoutInflater inflater;
  public Activity activity;

  public ExpandableListAdapter(Activity act, SparseArray<GroupDelivery> groups) {
    activity = act;
    this.groups = groups;
    inflater = act.getLayoutInflater();
  }

  @Override
  public Object getChild(int groupPosition, int childPosition) {
    return groups.get(groupPosition).children.get(childPosition);
  }

  @Override
  public long getChildId(int groupPosition, int childPosition) {
    return 0;
  }

  @Override
  public View getChildView(int groupPosition,
                           final int childPosition,
                           boolean isLastChild,
                           View convertView,
                           ViewGroup parent) {
    
	  final Delivery children = (Delivery) getChild(groupPosition, childPosition);
	  
	  TextView tvItemCode 		= null;
	  TextView tvDescription 	= null;
	  TextView tvPckg 			= null;
	  TextView tvQty 			= null;
	  TextView tvPrice 			= null;
	  TextView tvAmount 		= null;
	  
	  if (convertView == null) {
		  convertView = inflater.inflate(R.layout.listrow_details, null);
	  }
	  
	  tvItemCode = (TextView) convertView.findViewById(R.id.tvItemCode);
	  tvItemCode.setText(children.getItemcode());
	  
	  tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
	  tvDescription.setText(children.getItemDescription());
	  
	  tvPckg = (TextView) convertView.findViewById(R.id.tvPckg);
	  tvPckg.setText(children.getPckg());
	  
	  tvQty = (TextView) convertView.findViewById(R.id.tvQty);
	  tvQty.setText(""+children.getQty());
	  
	  tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
	  tvPrice.setText(""+ NumUtil.getPhpCurrency(children.getPrice(),false));
	  
	  double amount = children.getQty()*children.getPrice();
	  
	  tvAmount = (TextView) convertView.findViewById(R.id.tvAmount);
	  tvAmount.setText(""+NumUtil.getPhpCurrency(amount,false));
	  
	  convertView.setOnClickListener(new OnClickListener() {
		  @Override
		  public void onClick(View v) {
			  Toast.makeText(activity, children.getItemcode(),
					  Toast.LENGTH_SHORT).show();
		  }
	  });
	  return convertView;
  }

  @Override
  public int getChildrenCount(int groupPosition) {
    return groups.get(groupPosition).children.size();
  }

  @Override
  public Object getGroup(int groupPosition) {
    return groups.get(groupPosition);
  }

  @Override
  public int getGroupCount() {
    return groups.size();
  }

  @Override
  public void onGroupCollapsed(int groupPosition) {
    super.onGroupCollapsed(groupPosition);
  }

  @Override
  public void onGroupExpanded(int groupPosition) {
    super.onGroupExpanded(groupPosition);
  }

  @Override
  public long getGroupId(int groupPosition) {
    return 0;
  }

  @Override
  public View getGroupView(int groupPosition, boolean isExpanded,
                           View convertView, ViewGroup parent) {
    if (convertView == null) {
      convertView = inflater.inflate(R.layout.listrow_group, null);
    }
    GroupDelivery group = (GroupDelivery) getGroup(groupPosition);
    ((CheckedTextView) convertView).setText(group.string);
    ((CheckedTextView) convertView).setChecked(isExpanded);
    return convertView;
  }

  @Override
  public boolean hasStableIds() {
    return false;
  }

  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition) {
    return false;
  }
} 
