package com.google.code.easyshopper.activities.product;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.code.easyshopper.domain.CartProduct;

public class PopupImageListener implements OnClickListener {

	private final CartProduct cartProduct;
	private final GrabImageLauncher grabImageLauncher;
	private final Activity activity;

	public PopupImageListener(CartProduct cartProduct, Activity activity, GrabImageLauncher grabImageLauncher) {
		this.cartProduct = cartProduct;
		this.activity = activity;
		this.grabImageLauncher = grabImageLauncher;
	}

	public void onClick(View v) {
		if( ! cartProduct.getProduct().getImage().hasImage()) {
			grabImageLauncher.launchFor(cartProduct, activity);
			return;
		}
		new FullImageDialog(cartProduct, activity).show();
	}

}
