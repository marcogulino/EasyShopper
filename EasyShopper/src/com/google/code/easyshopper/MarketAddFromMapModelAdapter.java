/**
 * 
 */
package com.google.code.easyshopper;

import android.content.Context;

public final class MarketAddFromMapModelAdapter extends ModelListAdapterItem {
	private final Context context;

	public MarketAddFromMapModelAdapter(Context context) {
		this.context = context;
	}

	@Override
	protected String label() {
		return context.getResources().getString(R.string.Market_AddFromMap);
	}

	@Override
	public void executeOnClick() {
		Logger.d(this, "executeOnClick", "add from map clicked");
	}
}