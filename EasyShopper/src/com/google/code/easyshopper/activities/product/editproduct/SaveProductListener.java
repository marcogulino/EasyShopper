package com.google.code.easyshopper.activities.product.editproduct;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.google.code.easyshopper.activities.product.EditProductActivity;
import com.google.code.easyshopper.utility.StringUtils;

public class SaveProductListener implements OnClickListener {
	private final EditText editPrice;
	private final ProductSaver productSaver;
	private final Activity activity;

	public SaveProductListener(EditText editPrice, ProductSaver productSaver, Activity activity) {
		this.editPrice = editPrice;
		this.productSaver = productSaver;
		this.activity = activity;
	}

	public void onClick(View v) {
		productSaver.save(StringUtils.editTextToString(editPrice));
		Intent intent = activity.getIntent();
		intent.setAction(EditProductActivity.PRODUCT_SAVED_ACTION);
		activity.setResult(EditProductActivity.PRODUCT_SAVED, intent);
		activity.finish();

	}
}
