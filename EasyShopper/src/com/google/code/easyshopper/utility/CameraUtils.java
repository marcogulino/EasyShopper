package com.google.code.easyshopper.utility;

import java.io.File;
import java.util.ArrayList;

import android.net.Uri;

import com.google.code.easyshopper.ES;
import com.google.code.easyshopper.Logger;

public class CameraUtils {
	public static final String SAVED_PATH = "/sdcard/Android/data/" + ES.class.getPackage().getName();

	public static boolean hasImageCaptureBug() {

	    // list of known devices that have the bug
	    ArrayList<String> devices = new ArrayList<String>();
	    devices.add("android-devphone1/dream_devphone/dream");
	    devices.add("generic/sdk/generic");
	    devices.add("vodafone/vfpioneer/sapphire");
	    devices.add("tmobile/kila/dream");
	    devices.add("verizon/voles/sholes");
	    devices.add("tmobile/opal/sapphire");
	    devices.add("google_ion/google_ion/sapphire");
	    devices.add("htc_wwe/htc_bravo/bravo");

	    String myDevice = android.os.Build.BRAND + "/" + android.os.Build.PRODUCT + "/"
	            + android.os.Build.DEVICE;
	    Logger.dbg(CameraUtils.class, "hasImageCaptureBug", "*** checking camera bug for device: " + myDevice);
		return devices.contains(myDevice);
	}
	
	public static Uri getImagePath(String path) {
		Uri uri;
		if (hasImageCaptureBug()) {
			uri = Uri.fromFile(new File(SAVED_PATH));
		} else {
		 uri=android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		}
		return uri.buildUpon().appendPath( path).build();
	}
	
	public static String getSavedImagePathFor(String barcode) {
		return SAVED_PATH + "/" + barcode;
	}
}
