package com.gmail.gulino.marco.easyshopper.domain;

import android.location.Location;

import com.gmail.gulino.marco.easyshopper.utility.LocationUtils;
import com.google.android.maps.GeoPoint;

public class Market {

	private final long id;
	private String name;
	private int latitude;
	private int longitude;
	
	public Market(long id) {
		this.id=id;
	}

	public long getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}
	public int getLatitude() {
		return latitude;
	}

	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}

	public int getLongitude() {
		return longitude;
	}

	public void setGeoLocation(GeoPoint location) {
		setLongitude(location.getLongitudeE6());
		setLatitude(location.getLatitudeE6());
	}
	public GeoPoint getGeoLocation() {
		return new GeoPoint(getLatitude(), getLongitude());
	}
	
	
	public float getDistance(GeoPoint geoPoint) {
		return toLocation().distanceTo(LocationUtils.toLocation(geoPoint));
	}

	public Location toLocation() {
		return LocationUtils.toLocation(getGeoLocation());
	}
	
	@Override
	public String toString() {
		return getClass().getName() + " { id=" + id + ", name=" + name + ", lat=" + latitude + ", long=" + longitude + " }";
	}
	
}
