package com.google.code.easyshopper.domain;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.google.code.easyshopper.utility.CameraUtils;

public class ProductImage {

	private final String barcode;

	public ProductImage(String barcode) {
		this.barcode = barcode;
	}

	public Bitmap getSmallBitmap() {
		BitmapFactory.Options bmpFactory = new BitmapFactory.Options();
		bmpFactory.inSampleSize=4;
		return getBitmap(bmpFactory);
	}

	private Bitmap getBitmap(BitmapFactory.Options bmpFactory) {
		Uri imagePath = imagePath();
		boolean hasImage = hasImage(imagePath);
		if(! hasImage) return null;
		
		
		Bitmap bitmap = BitmapFactory.decodeFile(imagePath.getPath(), bmpFactory);
		return bitmap;
	}

	public boolean hasImage() {
		Uri imagePath = imagePath();
		return hasImage(imagePath);
	}
	

	private boolean hasImage(Uri imagePath) {
		return new File(imagePath.getPath()).exists();
	}
	
	private Uri imagePath() {
		return CameraUtils.getImagePath(barcode);
	}
}
