package com.google.code.easyshopper.activities.market;

import java.text.DecimalFormat;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.code.easyshopper.R;
import com.google.code.easyshopper.domain.Market;

public class MarketModelAdapter extends MarketSpinnerItem {

	private final MapController controller;
	private final Market market;
	private final Context context;
	private final View viewToEnable;
	private final GeoPoint myLocation;

	public MarketModelAdapter(Market market, MapController controller, Context context, View viewToEnable,
			GeoPoint myLocation) {
		this.market = market;
		this.controller = controller;
		this.context = context;
		this.viewToEnable = viewToEnable;
		this.myLocation = myLocation;
	}

	@Override
	public void executeOnClick() {
		Toast.makeText(context, market.getName(), Toast.LENGTH_SHORT).show();
		controller.animateTo(market.getGeoLocation());
		viewToEnable.setEnabled(true);
	}

	public Market getMarket() {
		return market;
	}

	@Override
	protected String label() {
		return market.getName();
	}

	@Override
	public String distance() {
		if (myLocation == null)
			return "";
		float distance = market.getDistance(myLocation);
		String unit = context.getResources().getString(R.string.Meters);
		if (distance > 1000) {
			distance = distance / 1000;
			unit = context.getResources().getString(R.string.Kilometers);
		}
		DecimalFormat df = new DecimalFormat("0.##");
		String distanceLabel = df.format(distance) + " " + unit;
		return distanceLabel;
	}

	@Override
	public boolean hasMarket(Market market) {
		return this.market.equals(market);
	}

}
