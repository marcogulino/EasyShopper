package com.gmail.gulino.marco.easyshopper.domain;

import android.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore.Images.Media;

import com.gmail.gulino.marco.easyshopper.utility.CameraUtils;

public class ProductImage {

	private final String barcode;
	private boolean hasImage=false;
	private BitmapDrawable bitmapDrawable;

	public ProductImage(String barcode) {
		this.barcode = barcode;
	}

	public Drawable getDrawableForProductDetails(Context context) {
		if(bitmapDrawable!= null) return bitmapDrawable;
		try {
			Bitmap bitmap = Media.getBitmap(context.getContentResolver(), CameraUtils.getImagePath(barcode));
			bitmapDrawable = new BitmapDrawable(bitmap);
			hasImage=true;
			return bitmapDrawable;
			
		} catch (Exception e) {
			hasImage=false;
			return context.getResources().getDrawable(R.drawable.ic_menu_gallery);
		}
	}
	
	public boolean hasImage(Context context) {
		if(bitmapDrawable==null) getDrawableForProductDetails(context);
		return hasImage;
	}

}
