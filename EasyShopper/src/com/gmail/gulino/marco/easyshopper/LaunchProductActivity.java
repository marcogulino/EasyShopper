package com.gmail.gulino.marco.easyshopper;

import com.gmail.gulino.marco.easyshopper.domain.Shopping;
import com.gmail.gulino.marco.easyshopper.productactivity.ProductActivity;

import android.app.Activity;
import android.content.Intent;

public class LaunchProductActivity {

	private final Activity activity;
	private final Shopping shopping;

	public LaunchProductActivity(Activity activity, Shopping shopping) {
		this.activity = activity;
		this.shopping = shopping;
	}

	public void startProductActivity(String barcode) {
		Intent addNewProduct = new Intent(activity, ProductActivity.class);
		addNewProduct.putExtra(ProductActivity.PARAM_BARCODE, barcode);
		addNewProduct.putExtra(ProductActivity.PARAM_SHOPPING, shopping.getId());
		activity.startActivityForResult(addNewProduct, 0);
	}

}
