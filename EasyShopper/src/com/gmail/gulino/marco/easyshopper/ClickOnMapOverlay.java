package com.gmail.gulino.marco.easyshopper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class ClickOnMapOverlay extends ItemizedOverlay<OverlayItem> implements LocationRetriever{

	private GeoPoint customPosition = null;
	private final Context context;
	private boolean hintWasShown = false;

	public ClickOnMapOverlay(Drawable drawable, Context context) {
		super(boundCenterBottom(drawable));
		this.context = context;
		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return new OverlayItem(customPosition, "", "");
	}

	@Override
	public int size() {
		return customPosition == null ? 0 : 1;
	}

	@Override
	public boolean onTap(GeoPoint p, MapView mapView) {
		if(! hintWasShown ) {
			Toast.makeText(context, R.string.Market_HintToAddCustomPosition, Toast.LENGTH_SHORT).show();
			hintWasShown=true;
		}
		customPosition = p;
		populate();
		mapView.postInvalidate();
		return true;
	}


	public GeoPoint getLocation() {
		return customPosition;
	}
}
