package com.google.code.easyshopper.activities.shopping;

import android.app.Activity;
import android.view.View;

import com.google.code.easyshopper.ModelListAdapterItem;

public abstract class ProductModelListAdapterItem extends ModelListAdapterItem {
	public abstract View getView(Activity activity);
}
