package com.google.code.easyshopper.activities.market;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.google.android.maps.MapView;
import com.google.code.easyshopper.Logger;

public class EasyShopperMapView extends MapView {

	public EasyShopperMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	 int oldZoomLevel=-1;

	 public void dispatchDraw(Canvas canvas) {
	  super.dispatchDraw(canvas);
	  Logger.d(this, "dispatchDraw", "!!!!!!!!! onEveryDispatchDraw");
	  if (getZoomLevel() != oldZoomLevel) {
		  Logger.d(this, "dispatchDraw", "!!!!!!!!! onZoom");
	   oldZoomLevel = getZoomLevel();
	  }
	 }

}
