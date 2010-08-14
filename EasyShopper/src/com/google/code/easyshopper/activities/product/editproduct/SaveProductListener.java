package com.google.code.easyshopper.activities.product.editproduct;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.code.easyshopper.activities.product.EditProductActivity;

public class SaveProductListener implements OnClickListener {
	private final ProductSaver productSaver;
	private final Activity activity;

	public SaveProductListener(ProductSaver productSaver, Activity activity) {
		this.productSaver = productSaver;
		this.activity = activity;
	}

	public void onClick(View v) {
		productSaver.save();
		Intent intent = activity.getIntent();
		intent.setAction(EditProductActivity.PRODUCT_SAVED_ACTION);
		activity.setResult(EditProductActivity.PRODUCT_SAVED, intent);
		activity.finish();

	}
}
