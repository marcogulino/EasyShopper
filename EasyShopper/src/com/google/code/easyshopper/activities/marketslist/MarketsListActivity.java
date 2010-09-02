package com.google.code.easyshopper.activities.marketslist;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.code.easyshopper.ModelListAdapterItem;
import com.google.code.easyshopper.R;
import com.google.code.easyshopper.activities.ViewInItemListAdapter;
import com.google.code.easyshopper.domain.Market;

public class MarketsListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.markets_list);
		ViewInItemListAdapter marketListAdapter = new ViewInItemListAdapter(this);
		ArrayAdapter<ModelListAdapterItem> spinnerAdapter = new ArrayAdapter<ModelListAdapterItem>(this, android.R.layout.simple_spinner_dropdown_item);
		((Spinner) findViewById(R.id.WhatMarketToShowSpinner)).setAdapter(spinnerAdapter);
		((ListView) findViewById(R.id.MarketsListView)).setAdapter(marketListAdapter);
		
		spinnerAdapter.add(new MarketListPopulator(this, R.string.MarketsList_Favourites));
		spinnerAdapter.add(new MarketListPopulator(this, R.string.MarketsList_Nearby));
		
		Market market = new Market(-1);
		market.setName("market name");
		market.setAddress("market address");
		marketListAdapter.add(new MarketListItem(market));
	}
}
