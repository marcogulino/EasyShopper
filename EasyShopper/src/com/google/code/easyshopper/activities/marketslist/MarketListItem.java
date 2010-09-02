package com.google.code.easyshopper.activities.marketslist;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.google.code.easyshopper.R;
import com.google.code.easyshopper.activities.ModelListAdapterItemWithView;
import com.google.code.easyshopper.domain.Market;

public class MarketListItem extends ModelListAdapterItemWithView {


	private final Market market;

	public MarketListItem(Market market) {
		this.market = market;
	}

	@Override
	public void executeOnClick() {
		// TODO Auto-generated method stub

	}

	@Override
	protected String label() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getView(Activity activity) {
		View mainView = activity.getLayoutInflater().inflate(R.layout.market_adapter_item, null);
		((TextView) mainView.findViewById(R.id.MarketName)).setText(market.getName());
		((TextView) mainView.findViewById(R.id.MarketAddress)).setText(market.getAddress());
		return mainView;
	}

}
