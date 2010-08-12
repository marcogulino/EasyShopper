package com.google.code.easyshopper.activities.product;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.code.easyshopper.Logger;
import com.google.code.easyshopper.domain.CartProduct;
import com.google.code.easyshopper.utility.CameraUtils;

public class GrabImageLauncher implements OnClickListener {
	private final ImageCleaner cleaner;
	private final Activity activity;
	private final CartProduct cartProduct;

	public GrabImageLauncher(CartProduct cartProduct, ImageCleaner cleaner,
			Activity activity) {
		this.cartProduct = cartProduct;
		this.cleaner = cleaner;
		this.activity = activity;
	}

	public void onClick(View v) {
		cleaner.clean();
		File file = new File(CameraUtils.SAVED_PATH + "/.nomedia");
		boolean mkdirs = file.mkdir() || file.mkdirs();
		Logger.d(this, "onClick", "create directories for " + file + ": "
				+ mkdirs);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		Uri savePath = CameraUtils.getImagePath(cartProduct.getBarcodeForProduct());
		Logger.d(this, "onClick", "trying to save to file: " + savePath);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, savePath);
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
		activity.startActivityForResult(intent, 1);
	}
}
