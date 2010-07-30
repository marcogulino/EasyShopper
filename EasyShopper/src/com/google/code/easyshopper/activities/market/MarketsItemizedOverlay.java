/**
 * 
 */
package com.google.code.easyshopper.activities.market;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.google.android.maps.ItemizedOverlay;
import com.google.code.easyshopper.Logger;
import com.google.code.easyshopper.activities.market.MarketActivity.ActivateItem;
import com.google.code.easyshopper.domain.Market;

final class MarketsItemizedOverlay extends ItemizedOverlay<MarketOverlayItem> {
	private List<MarketOverlayItem> items;
	private final Context context;
	private final ActivateItem activateItem;

	MarketsItemizedOverlay(Drawable defaultMarker, Context context, ActivateItem activateItem) {
		super(boundCenterBottom(defaultMarker));
		this.context = context;
		this.activateItem = activateItem;
		Logger.d(this, "MarketsItemizedOverlay", "using marker: " + defaultMarker);
		items=new ArrayList<MarketOverlayItem>();
	}

	@Override
	protected MarketOverlayItem createItem(int i) {
		MarketOverlayItem marketOverlayItem = items.get(i);
		Logger.d(this, "MarketsItemizedOverlay", "createItem" + i + ", " + marketOverlayItem);
		return marketOverlayItem;
	}

	@Override
	public int size() {
		return items.size();
	}
	
	@Override
	protected boolean onTap(int paramInt) {
		Logger.d(this, "onTap", "" +paramInt);
		MarketOverlayItem marketOverlayItem = items.get(paramInt);
		Logger.d(this, "onTap", "showing item text: " + marketOverlayItem.getSnippet());
		Toast.makeText(context, marketOverlayItem.getSnippet(), Toast.LENGTH_SHORT).show();
		activateItem.activate(marketOverlayItem.getMarket());
		return true;
	}
	

	public void add(Market market) {
		items.add(new MarketOverlayItem(market));
		populate();
	}
	
	public void add(List<Market> markets) {
		for (Market market : markets) {
			MarketOverlayItem marketOverlayItem = new MarketOverlayItem(market);
			items.add(marketOverlayItem);
		}
		populate();
	}
}