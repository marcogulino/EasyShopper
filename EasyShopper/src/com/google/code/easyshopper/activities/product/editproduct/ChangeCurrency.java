package com.google.code.easyshopper.activities.product.editproduct;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;

import com.google.code.easyshopper.activities.product.editproduct.EditProduct.CurrencyItem;
import com.google.code.easyshopper.domain.CartProduct;

public class ChangeCurrency implements OnItemSelectedListener {

	private final CartProduct cartProduct;
	private final ArrayAdapter<CurrencyItem> currencySpinnerAdapter;

	public ChangeCurrency(CartProduct cartProduct, ArrayAdapter<CurrencyItem> currencySpinnerAdapter) {
		this.cartProduct = cartProduct;
		this.currencySpinnerAdapter = currencySpinnerAdapter;
	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
		cartProduct.getPrice().getAmount().setCurrency(currencySpinnerAdapter.getItem(position).currency);
	}

	public void onNothingSelected(AdapterView<?> arg0) {
	}

}
