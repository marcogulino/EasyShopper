package com.google.code.easyshopper.activities.shopping;

import com.google.code.easyshopper.R;
import com.google.code.easyshopper.domain.Shopping;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PickMarketListAdapter extends ProductModelListAdapterItem {

	private final Activity activity;
	private final Shopping shopping;

	public PickMarketListAdapter(Activity activity, Shopping shopping) {
		this.activity = activity;
		this.shopping = shopping;
	}

	@Override
	public View getView(Activity activity) {
		View view = activity.getLayoutInflater().inflate(R.layout.products_item_layout, null);
		((TextView) view.findViewById(R.id.ProductPriceQuantity)).setText("");
		((TextView) view.findViewById(R.id.ProductPrice)).setText("");
		((TextView) view.findViewById(R.id.ProductName)).setText(label());
		((ImageView) view.findViewById(R.id.ListViewProductImage)).setImageResource(android.R.drawable.ic_menu_myplaces);
		return view;
	}

	@Override
	public void executeOnClick() {
		new PickMarketListener(activity, shopping).onClick(null);

	}

	@Override
	protected String label() {
		return activity.getResources().getString(R.string.PickMarket);
	}

}
