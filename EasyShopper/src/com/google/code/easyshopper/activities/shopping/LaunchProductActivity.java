package com.google.code.easyshopper.activities.shopping;

import android.app.Activity;
import android.content.Intent;

import com.google.code.easyshopper.activities.product.EditProductActivity;
import com.google.code.easyshopper.domain.Shopping;

public class LaunchProductActivity {

	private final Activity activity;
	private final Shopping shopping;

	public LaunchProductActivity(Activity activity, Shopping shopping) {
		this.activity = activity;
		this.shopping = shopping;
	}

	public void startProductActivity(String barcode) {
		Intent addNewProduct = new Intent(activity, EditProductActivity.class);
		addNewProduct.putExtra(EditProductActivity.PARAM_BARCODE, barcode);
		addNewProduct.putExtra(EditProductActivity.PARAM_SHOPPING, shopping.getId());
		activity.startActivityForResult(addNewProduct, 0);
	}

}
