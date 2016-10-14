package com.lucknow.quaere.shinecity_mlmpanel;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class NavDrawerListAdapter extends BaseExpandableListAdapter {
	private ArrayList<NavDrawerItem> groupItem, tempChild;
	public ArrayList<Object> Childtem = new ArrayList<Object>();
	public LayoutInflater minflater;
	public Activity activity;
	private final Context context;

	public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> grList) {
		this.context = context;
		groupItem = grList;

	}

	public void setInflater(LayoutInflater mInflater, Activity act) {
		this.minflater = mInflater;
		activity = act;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		tempChild = (ArrayList<NavDrawerItem>) Childtem.get(groupPosition);
		//TextView text = null;
		final String childText = (String) getChild(groupPosition, childPosition);
		if (convertView == null) {
			convertView = new TextView(context);
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.child_list_item, null);
		}
		//text = (TextView) convertView;
		TextView txtListChild = (TextView) convertView
				.findViewById(R.id.lblListItem);
		txtListChild.setText(">"+tempChild.get(childPosition));
//		convertView.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Toast.makeText(activity, tempChild.get(childPosition),
//						Toast.LENGTH_SHORT).show();
//			}
//		});
		convertView.setTag(tempChild.get(childPosition));
		return convertView;



	}

	

	@Override
	public int getChildrenCount(int groupPosition) {
		return ((ArrayList<String>) Childtem.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public int getGroupCount() {
		return groupItem.size();
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
			LayoutInflater mInflater = (LayoutInflater)
					context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.drawer_list_item, null);
		}

		ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
		TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
		//TextView txtCount = (TextView) convertView.findViewById(R.id.counter);

		imgIcon.setImageResource(groupItem.get(groupPosition).getIcon());
		txtTitle.setText(groupItem.get(groupPosition).getTitle());

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
