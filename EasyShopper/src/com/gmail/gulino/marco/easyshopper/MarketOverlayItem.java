package com.gmail.gulino.marco.easyshopper;

import com.gmail.gulino.marco.easyshopper.domain.Market;
import com.google.android.maps.OverlayItem;

public class MarketOverlayItem extends OverlayItem{

	private final Market market;

	public MarketOverlayItem(Market market) {
		super(market.getGeoLocation(), market.getName(), market.getName());
		this.market = market;
	}
	
	public Market getMarket() {
		return market;
	}
	
}
