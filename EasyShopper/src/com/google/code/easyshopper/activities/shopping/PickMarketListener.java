package com.google.code.easyshopper.activities.shopping;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.code.easyshopper.activities.market.MarketActivity;
import com.google.code.easyshopper.activities.marketslist.MarketsListActivity;
import com.google.code.easyshopper.domain.Shopping;

final class PickMarketListener implements OnClickListener {
	private final Activity activity;
	private final Shopping shopping;

	public PickMarketListener(Activity activity, Shopping shopping) {
		this.activity = activity;
		this.shopping = shopping;
	}

	public void onClick(View v) {
		startPickMarket();
	}

	private void startPickMarket() {
		Intent marketActivity = new Intent(activity, MarketsListActivity.class);
		marketActivity.putExtra(MarketActivity.SHOPPING_ID, shopping.getId());
		activity.startActivityForResult(marketActivity, MarketActivity.PICK_MARKET);
	}
}