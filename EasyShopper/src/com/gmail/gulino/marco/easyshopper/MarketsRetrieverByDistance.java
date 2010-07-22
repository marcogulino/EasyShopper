package com.gmail.gulino.marco.easyshopper;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.gmail.gulino.marco.easyshopper.db.MarketDBAdapter;
import com.gmail.gulino.marco.easyshopper.db.helpers.EasyShopperSqliteOpenHelper;
import com.gmail.gulino.marco.easyshopper.domain.Market;
import com.google.android.maps.GeoPoint;

import android.content.Context;

public class MarketsRetrieverByDistance {

	private final Context context;
	private Market closerMarket;

	public MarketsRetrieverByDistance(Context context) {
		this.context = context;
		closerMarket=null;
	}

	public List<Market> retrieve(final GeoPoint myLocation) {
		List<Market> markets = new MarketDBAdapter(new EasyShopperSqliteOpenHelper(context)).selectAll();
		if(markets.size()==0) return markets;
		if(myLocation != null) {
			Collections.sort(markets, new Comparator<Market>() {
	
				public int compare(Market market1, Market market2) {
					float distance1 = market1.getDistance(myLocation);
					float distance2 = market2.getDistance(myLocation);
					
					return (int) ( ((int)(distance1*1e6)) - ((int)distance2*1e6));
				}
			});
		}
		closerMarket=markets.get(0);
		return markets;
	}
	
	public Market getCloserMarket() {
		return closerMarket;
	}

}
