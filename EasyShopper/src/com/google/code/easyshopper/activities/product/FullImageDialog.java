package com.google.code.easyshopper.activities.product;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.code.easyshopper.R;
import com.google.code.easyshopper.domain.CartProduct;

public class FullImageDialog extends Dialog {

	private final CartProduct cartProduct;
	private ImageView imageView;

	public FullImageDialog(CartProduct cartProduct, Activity activity) {
		super(activity);
		this.cartProduct = cartProduct;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_image_dialog);
	}
	@Override
	protected void onStart() {
		super.onStart();
		Bitmap productBitmap = cartProduct.getProduct().getImage().getFullBitmap();
		
		imageView = (ImageView) findViewById(R.id.FullImageView);
		imageView.setImageBitmap(productBitmap);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		((BitmapDrawable)imageView.getDrawable()).getBitmap().recycle();
		imageView.setImageBitmap(null);
	}
}
