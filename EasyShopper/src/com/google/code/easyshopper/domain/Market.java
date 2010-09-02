package com.google.code.easyshopper.domain;

import android.location.Location;

import com.google.android.maps.GeoPoint;
import com.google.code.easyshopper.utility.LocationUtils;

public class Market {

	private long id;
	private String name;
	private int latitude;
	private int longitude;
	private String address;
	
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

	public void setId(long id) {
		this.id=id;
	}
	

	// TODO Save on database
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getAddress() {
		return address;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + latitude;
		result = prime * result + longitude;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Market other = (Market) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (id != other.id)
			return false;
		if (latitude != other.latitude)
			return false;
		if (longitude != other.longitude)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
}
