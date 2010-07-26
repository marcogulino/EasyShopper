package com.google.code.easyshopper;

import com.google.android.maps.OverlayItem;
import com.google.code.easyshopper.domain.Market;

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
