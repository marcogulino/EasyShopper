package com.google.code.easyshopper.activities.shopping;

import com.google.code.easyshopper.ModelListAdapterItem;

import android.app.Activity;
import android.view.View;

public abstract class ProductModelListAdapterItem extends ModelListAdapterItem {
	public abstract View getView(Activity activity);
}
