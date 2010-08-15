package com.google.code.easyshopper.activities.shopping;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;

public class SearchOnMarket implements OnClickListener {

	private final Activity activity;
	private final Uri marketUri;

	public SearchOnMarket(Activity activity, Uri marketUri) {
		this.activity = activity;
		this.marketUri = marketUri;
	}


	public void onClick(DialogInterface dialog, int which) {
		Intent intent = new Intent(Intent.ACTION_VIEW, marketUri);
		activity.startActivity(intent);
		dialog.dismiss();
	}

}
