package com.google.code.easyshopper.activities;

import android.app.Activity;
import android.view.View;

import com.google.code.easyshopper.ModelListAdapterItem;

public abstract class ModelListAdapterItemWithView extends ModelListAdapterItem {
	public abstract View getView(Activity activity);
}
