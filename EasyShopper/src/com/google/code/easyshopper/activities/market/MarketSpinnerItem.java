package com.google.code.easyshopper.activities.market;

import com.google.code.easyshopper.ModelListAdapterItem;
import com.google.code.easyshopper.domain.Market;

public abstract class MarketSpinnerItem extends ModelListAdapterItem  {
	public abstract boolean hasMarket(Market market);

	public abstract String distance();
}
