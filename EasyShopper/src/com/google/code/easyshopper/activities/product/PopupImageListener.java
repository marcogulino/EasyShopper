package com.google.code.easyshopper.activities.product;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.google.code.easyshopper.domain.CartProduct;
import com.google.code.easyshopper.utility.StringUtils;

public class PopupImageListener implements OnClickListener {

	private final CartProduct cartProduct;
	private final GrabImageLauncher grabImageLauncher;
	private final Activity activity;
	private final int productnameEdit;

	public PopupImageListener(int productnameEdit, CartProduct cartProduct, Activity activity, GrabImageLauncher grabImageLauncher) {
		this.productnameEdit = productnameEdit;
		this.cartProduct = cartProduct;
		this.activity = activity;
		this.grabImageLauncher = grabImageLauncher;
	}

	public void onClick(View v) {
		if (!cartProduct.getProduct().getImage().hasImage()) {
			grabImageLauncher.launchFor(cartProduct, activity);
			return;
		}
		EditText editProductName = (EditText) activity.findViewById(productnameEdit);
		cartProduct.getProduct().setName(StringUtils.editTextToString(editProductName));
		new FullImageDialog(cartProduct, activity).show();
	}

}
