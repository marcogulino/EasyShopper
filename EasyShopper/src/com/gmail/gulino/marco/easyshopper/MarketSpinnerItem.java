package com.gmail.gulino.marco.easyshopper;

import com.gmail.gulino.marco.easyshopper.domain.Market;

public abstract class MarketSpinnerItem extends ModelListAdapterItem  {
	public abstract boolean hasMarket(Market market);

	public abstract String distance();
}
