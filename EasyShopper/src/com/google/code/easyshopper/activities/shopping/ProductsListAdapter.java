package com.google.code.easyshopper.activities.shopping;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.code.easyshopper.Logger;
import com.google.code.easyshopper.R;

public class ProductsListAdapter extends ArrayAdapter<ProductModelListAdapterItem> {

	private final Activity activity;

	public ProductsListAdapter(Activity activity) {
		super(activity, R.layout.products_item_layout);
		this.activity = activity;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Logger.d(this, "getView", "got arguments: " + position + ", " + convertView + ", " + parent);
		ProductModelListAdapterItem item = getItem(position);
		return item.getView(activity);
	}
}
