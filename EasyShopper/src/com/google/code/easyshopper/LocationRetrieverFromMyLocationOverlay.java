package com.google.code.easyshopper;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MyLocationOverlay;

public class LocationRetrieverFromMyLocationOverlay implements LocationRetriever {

	private final MyLocationOverlay myLocationOverlay;

	public LocationRetrieverFromMyLocationOverlay(MyLocationOverlay myLocationOverlay) {
		this.myLocationOverlay = myLocationOverlay;
	}

	public GeoPoint getLocation() {
		return myLocationOverlay.getMyLocation();
	}

}
