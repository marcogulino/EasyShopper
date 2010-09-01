package com.google.code.easyshopper.activities.market;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.code.easyshopper.Logger;
import com.google.code.easyshopper.R;
import com.google.code.easyshopper.db.ShoppingDBAdapter;
import com.google.code.easyshopper.db.helpers.EasyShopperSqliteOpenHelper;
import com.google.code.easyshopper.domain.Market;
import com.google.code.easyshopper.domain.Shopping;
import com.google.code.easyshopper.listeners.OnItemSelectedForModelAdapter;
import com.google.code.easyshopper.utility.LocationUtils;

public class MarketActivity extends MapActivity implements LocationListener {

	public class ActivateItem {

		public void populateAndActivate(Market market) {
			populateSpinner();
			activate(market);
		}
		public void activate(Market market) {
			for (int i = 0; i < marketsSpinnerAdapter.getCount(); i++) {
				if (marketsSpinnerAdapter.getItem(i).hasMarket(market)) {
					marketsSpinner.setSelection(i);
				}
			}
		}
	}

	public static final int PICK_MARKET = 0;
	public static final String SELECTED_MARKET = "_SELECTED_MARKET";
	public static final int NO_MARKET_SELECTED = -1;
	protected static final int MARKET_PICKED = 1;
	public static final String SHOPPING_ID = "_SHOPPING_ID_";
	private EasyShopperMapView marketMapView;
	private MyLocationOverlay myLocationOverlay;
	private MarketsItemizedOverlay itemizedOverlay;
	private MarketsSpinnerAdapter marketsSpinnerAdapter;
	private Spinner marketsSpinner;
	private ClickOnMapOverlay clickOnMapOverlay;
	private Button okButton;
	private MarketsRetrieverByDistance marketsRetrieverByDistance;
	private LocationManager locationManager;
	private GeoPoint myLocation;
	private boolean hintWasShown=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    	setContentView(R.layout.market_layout);
		marketsRetrieverByDistance = new MarketsRetrieverByDistance(this);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    	marketMapView = (EasyShopperMapView) findViewById(R.id.MarketMapView);
    	marketMapView.setBuiltInZoomControls(true);
    	marketMapView.setSatellite(false);
    	marketMapView.setTraffic(false);
    	marketMapView.getController().setZoom(20);
    	myLocationOverlay = new MyLocationOverlay(this, marketMapView);
		marketMapView.getOverlays().add(myLocationOverlay);
		enableLocationRetrieving();
		itemizedOverlay = new MarketsItemizedOverlay(getResources().getDrawable(R.drawable.bubble), this, new ActivateItem());
		clickOnMapOverlay = new ClickOnMapOverlay(getResources().getDrawable(R.drawable.bubble_blu), this);
		marketMapView.getOverlays().add(clickOnMapOverlay);
		marketMapView.getOverlays().add(itemizedOverlay);
		marketsSpinner = (Spinner) findViewById(R.id.MarketsSpinner);
		marketsSpinnerAdapter = new MarketsSpinnerAdapter(this);
		marketsSpinner.setAdapter(marketsSpinnerAdapter);
		okButton = (Button) findViewById(R.id.Market_OkButton);
		marketsSpinner.setOnItemSelectedListener(new OnItemSelectedForModelAdapter());
		Button cancelButton=(Button) findViewById(R.id.Market_CancelButton);
		
		okButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = getIntent();
				Market selectedMarket = ((MarketModelAdapter) marketsSpinnerAdapter.getItem(marketsSpinner.getSelectedItemPosition())).getMarket();
				intent.putExtra(SELECTED_MARKET, selectedMarket.getId());
				intent.setAction(SELECTED_MARKET);
				setResult(MARKET_PICKED, intent);
				long shoppingId = intent.getLongExtra(SHOPPING_ID, -1);
				Logger.d(this, "okButton::onClick","ssociating shoppingId " + shoppingId + " with market " + selectedMarket);
				new ShoppingDBAdapter(new EasyShopperSqliteOpenHelper(MarketActivity.this)).associateTo(selectedMarket, new Shopping(shoppingId));
				finish();
			}
		});
		
		cancelButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				setResult(NO_MARKET_SELECTED);
				finish();
			}
		});
		
		populateSpinner();
	}

	private void populateSpinner() {
		marketsSpinnerAdapter.clear();
		marketsSpinnerAdapter.add(new MarketSpinnerItem() {
			@Override
			protected String label() {
				return getResources().getString(R.string.PickOne);
			}

			@Override
			public void executeOnClick() {
				okButton.setEnabled(false);
			}

			@Override
			public boolean hasMarket(Market market) {
				return market == null;
			}

			@Override
			public String distance() {
				return "";
			}
		});
		List<Market> markets = marketsRetrieverByDistance.retrieve(myLocation);
		for (Market market : markets) {
			marketsSpinnerAdapter.add(new MarketModelAdapter(market, marketMapView.getController(), this, okButton, myLocation));
		}
		marketsSpinner.setSelection(-1);
		itemizedOverlay.add(markets);
		marketMapView.postInvalidate();
	}

	@Override
	public boolean onCreatePanelMenu(int featureId, Menu menu) {
		menu.add(getResources().getString(R.string.Market_MyPosition)).setIcon(android.R.drawable.ic_menu_mylocation)
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {

					public boolean onMenuItemClick(MenuItem item) {
						MapController controller = marketMapView.getController();
						GeoPoint myLocation = myLocationOverlay.getMyLocation();
						Logger.d(this, "onCreatePanelMenu::onMenuItemClick", "gotLocation: " + myLocation);
						if (myLocation == null) {
							Toast.makeText(MarketActivity.this, getResources().getString(R.string.WaitForPosition),
									Toast.LENGTH_LONG).show();
							return false;
						}
						controller.animateTo(myLocation);
						return true;
					}
				});

		menu.add(getResources().getString(R.string.Market_AddFromMyLocation)).setIcon(R.drawable.add_from_my_location)
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {

					public boolean onMenuItemClick(MenuItem item) {
						MarketEditDialog marketEditDialog = new MarketEditDialog(MarketActivity.this,
								new LocationRetrieverFromMyLocationOverlay(myLocationOverlay), new ActivateItem());
						Market market = marketEditDialog.execute();
						return market != null;
					}
				});

		menu.add(getResources().getString(R.string.Market_AddFromMap)).setIcon(android.R.drawable.ic_menu_add)
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {

					public boolean onMenuItemClick(MenuItem item) {
						if (clickOnMapOverlay.getLocation() == null) {
							Toast.makeText(MarketActivity.this,
									getResources().getString(R.string.Market_SelectOnMapFirst), Toast.LENGTH_LONG)
									.show();
							return false;
						}
						MarketEditDialog marketEditDialog = new MarketEditDialog(MarketActivity.this,
								clickOnMapOverlay, new ActivateItem());
						Market market = marketEditDialog.execute();
						return market != null;
					}
				});
		return true;
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	protected void onDestroy() {
		Logger.d(this, "onDestroy", "disabling my location service");
		disableLocationRetrieving();
		super.onDestroy();
	}


	@Override
	protected void onPause() {
		Logger.d(this, "onPause", "disabling my location service");
		disableLocationRetrieving();
		super.onPause();
	}

	@Override
	protected void onResume() {
		Logger.d(this, "onResume", "reenabling my location service");
		enableLocationRetrieving();
		super.onResume();
	}

	private void enableLocationRetrieving() {
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
		myLocationOverlay.enableMyLocation();
	}
	
	private void disableLocationRetrieving() {
		locationManager.removeUpdates(this);
		myLocationOverlay.disableMyLocation();
	}

	@Override
	protected boolean isLocationDisplayed() {
		return true;
	}

	public void onLocationChanged(Location paramLocation) {
		myLocation=LocationUtils.toGeoPoint(paramLocation);
		new ActivateItem().populateAndActivate(marketsRetrieverByDistance.getCloserMarket());
		if(! hintWasShown) {
			Toast.makeText(MarketActivity.this, R.string.Market_HintToAddMyPosition, Toast.LENGTH_SHORT).show();
			hintWasShown=true;
		}
		int selectedItemPosition = marketsSpinner.getSelectedItemPosition();
		if(clickOnMapOverlay.size()!= 0 || ! marketsSpinnerAdapter.getItem(selectedItemPosition).hasMarket(null) ) {
			Logger.d(this, "onLocationChanged", "skipping: clickOnMapOverlay size=" + clickOnMapOverlay.size() );
			Logger.d(this, "onLocationChanged", "skipping: selected market position: " + selectedItemPosition + ", has market null: " + marketsSpinnerAdapter.getItem(selectedItemPosition).hasMarket(null) );
			return;
		}
		marketMapView.getController().animateTo(myLocation);
	}

	public void onProviderDisabled(String paramString) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderEnabled(String paramString) {
		// TODO Auto-generated method stub
		
	}

	public void onStatusChanged(String paramString, int paramInt, Bundle paramBundle) {
		// TODO Auto-generated method stub
		
	}
}
