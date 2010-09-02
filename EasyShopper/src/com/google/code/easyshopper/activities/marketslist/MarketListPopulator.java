package com.google.code.easyshopper.activities.marketslist;

import android.content.Context;

import com.google.code.easyshopper.ModelListAdapterItem;

public class MarketListPopulator extends ModelListAdapterItem {


	private final Context context;
	private final int label;

	public MarketListPopulator(Context context, int label) {
		this.context = context;
		this.label = label;
	}

	@Override
	public void executeOnClick() {
		// TODO Auto-generated method stub

	}

	@Override
	protected String label() {
		return context.getResources().getString(label);
	}

}
