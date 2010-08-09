package com.google.code.easyshopper.activities;

import android.view.View;

public interface ESTab {
	public View getView();
	public void setup();
	public void updateValuesOnExit();
	
	public interface UpdateValues {
		public void update();
	}
}
