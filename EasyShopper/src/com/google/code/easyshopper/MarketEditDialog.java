/**
 * 
 */
package com.google.code.easyshopper;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.code.easyshopper.MarketActivity.ActivateItem;
import com.google.code.easyshopper.db.MarketDBAdapter;
import com.google.code.easyshopper.db.helpers.EasyShopperSqliteOpenHelper;
import com.google.code.easyshopper.domain.Market;

public final class MarketEditDialog extends EditableTextDialog {
	private final Context context;
	private final LocationRetriever locationRetriever;
	private final ActivateItem activateMarketItem;
	private Market market;
	public MarketEditDialog(Context context, LocationRetriever locationRetriever, ActivateItem activateMarketItem) {
		super(context, context.getResources().getString(R.string.Market_Dialog_getName));
		this.context = context;
		this.locationRetriever = locationRetriever;
		this.activateMarketItem = activateMarketItem;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, new OkButtonOnClickListener(), new CancelButtonOnClickListener());
	}

	public Market execute() {
		show();
		Logger.d(this, "execute", "add from my location clicked");
		if(locationRetriever.getLocation() == null) {
			Toast.makeText(context, context.getResources().getString(R.string.WaitForPosition), Toast.LENGTH_LONG).show();
			return null;
		}
		return market;
	}
	
	private final class CancelButtonOnClickListener implements View.OnClickListener {
		public void onClick(View v) {
			cancel();
		}
	}

	private final class OkButtonOnClickListener implements View.OnClickListener {
		public void onClick(View v) {
			String marketName = getEditText().trim();
			Logger.d(this, "onClick", "got result: "+ marketName);
			market = new Market(MarketDBAdapter.SAVE_NEW);
			market.setName(marketName);
			GeoPoint location = locationRetriever.getLocation();
			
			market.setGeoLocation(location);
			new MarketDBAdapter(new EasyShopperSqliteOpenHelper(context)).saveMarket(market );
			activateMarketItem.populateAndActivate(market);
			dismiss();
		}

	}
}