package com.google.code.easyshopper.domain;

import java.io.File;

import android.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.google.code.easyshopper.utility.CameraUtils;

public class ProductImage {

	private final String barcode;
	private boolean hasImage=false;
	private BitmapDrawable bitmapDrawable;

	public ProductImage(String barcode) {
		this.barcode = barcode;
	}

	public Drawable getDrawableForProductDetails(Context context) {
		if(bitmapDrawable!= null) return bitmapDrawable;
		Uri imagePath = CameraUtils.getImagePath(barcode);
		if(new File(imagePath.getPath()).exists())
		 {
			BitmapFactory.Options bmpFactory = new BitmapFactory.Options();
			bmpFactory.inSampleSize=4;
			Bitmap bitmap = BitmapFactory.decodeFile(imagePath.getPath(), bmpFactory);
			bitmapDrawable = new BitmapDrawable(bitmap);
			hasImage=true;
			return bitmapDrawable;
		 }
			hasImage=false;
			return context.getResources().getDrawable(R.drawable.ic_menu_gallery);
	}
	
	public boolean hasImage(Context context) {
		if(bitmapDrawable==null) getDrawableForProductDetails(context);
		return hasImage;
	}

}
