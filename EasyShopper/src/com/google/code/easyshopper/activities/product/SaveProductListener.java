package com.google.code.easyshopper.activities.product;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.google.code.easyshopper.utility.StringUtils;

public class SaveProductListener implements OnClickListener {
	private final EditText productName;
	private final String barcode;
	private final EditText editPrice;
	private final ProductSaver productSaver;
	private final Activity activity;
	private final CurrencyRetriever currencyRetriever;
	private final PriceTypeRetriever priceTypeRetriever;

	public SaveProductListener(EditText productName, EditText editPrice, String barcode, PriceTypeRetriever priceTypeRetriever, CurrencyRetriever currencyRetriever, ProductSaver productSaver, Activity activity) {
		this.productName = productName;
		this.editPrice = editPrice;
		this.barcode = barcode;
		this.priceTypeRetriever = priceTypeRetriever;
		this.currencyRetriever = currencyRetriever;
		this.productSaver = productSaver;
		this.activity = activity;
	}

	public void onClick(View v) {
		productSaver.save(this.barcode, StringUtils.editTextToString(this.productName), priceTypeRetriever.priceBarcodeChars(), currencyRetriever.currency(), StringUtils.editTextToString(editPrice));
		Intent intent = activity.getIntent();
		intent.setAction(EditProductActivity.PRODUCT_SAVED_ACTION);
		activity.setResult(EditProductActivity.PRODUCT_SAVED, intent);
		activity.finish();

	}
}
