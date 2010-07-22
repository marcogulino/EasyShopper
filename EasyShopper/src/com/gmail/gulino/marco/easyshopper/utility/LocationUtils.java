package com.gmail.gulino.marco.easyshopper.utility;

import android.location.Location;
import android.location.LocationManager;

import com.google.android.maps.GeoPoint;

public class LocationUtils {

	public static Location toLocation(GeoPoint geoPoint) {
		Location otherLocation = new Location(LocationManager.GPS_PROVIDER);
		otherLocation.setLongitude(((double)geoPoint.getLongitudeE6()) / 1e6);
		otherLocation.setLatitude(((double)geoPoint.getLatitudeE6()) / 1e6);
		return otherLocation;
	}
	
	public static GeoPoint toGeoPoint(Location location) {
		int latitudeE6 = (int) (location.getLatitude() * 1e6);
		int longitudeE6 = (int) (location.getLongitude() * 1e6);
		return new GeoPoint(latitudeE6 , longitudeE6 );
	}

}
