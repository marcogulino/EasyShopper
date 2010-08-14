package com.google.code.easyshopper.activities.product.editproduct;

import com.google.code.easyshopper.domain.CartProduct;

import android.text.Editable;
import android.text.TextWatcher;

public class UpdateProductNameWatcher implements TextWatcher {

	private final CartProduct cartProduct;

	public UpdateProductNameWatcher(CartProduct cartProduct) {
		this.cartProduct = cartProduct;
	}

	public void afterTextChanged(Editable s) {

	}

	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		cartProduct.getProduct().setName(s.toString());
	}

}
