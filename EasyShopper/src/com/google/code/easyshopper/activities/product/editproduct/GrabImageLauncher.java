package com.google.code.easyshopper.activities.product.editproduct;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.google.code.easyshopper.Logger;
import com.google.code.easyshopper.domain.CartProduct;
import com.google.code.easyshopper.utility.CameraUtils;

public class GrabImageLauncher {
	private final ImageManager cleaner;

	public GrabImageLauncher(ImageManager cleaner) {
		this.cleaner = cleaner;
	}

	public void launchFor(CartProduct cartProduct, Activity activity) {
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
		activity.startActivityForResult(intent, 0);
	}
}
