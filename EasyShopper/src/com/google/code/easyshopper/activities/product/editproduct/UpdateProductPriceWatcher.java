package com.google.code.easyshopper.activities.product.editproduct;

import com.google.code.easyshopper.domain.CartProduct;

import android.text.Editable;
import android.text.TextWatcher;

public class UpdateProductPriceWatcher implements TextWatcher {

	private final CartProduct cartProduct;

	public UpdateProductPriceWatcher(CartProduct cartProduct) {
		this.cartProduct = cartProduct;
	}

	public void afterTextChanged(Editable s) {
	}

	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		cartProduct.getPrice().getAmount().setFromReadableAmount(s.toString());
	}

}
