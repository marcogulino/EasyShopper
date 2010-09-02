package com.google.code.easyshopper.activities;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.code.easyshopper.Logger;

public class ViewInItemListAdapter extends ArrayAdapter<ModelListAdapterItemWithView> {

	private final Activity activity;

	public ViewInItemListAdapter(Activity activity) {
		super(activity, 0);
		this.activity = activity;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Logger.d(this, "getView", "got arguments: " + position + ", " + convertView + ", " + parent);
		ModelListAdapterItemWithView item = getItem(position);
		return item.getView(activity);
	}
}
