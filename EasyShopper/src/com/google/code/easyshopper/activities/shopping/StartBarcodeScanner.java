package com.google.code.easyshopper.activities.shopping;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;

import com.google.code.easyshopper.ModelListAdapterItem;
import com.google.code.easyshopper.R;

public class StartBarcodeScanner extends ModelListAdapterItem {
		private final Activity activity;

		public StartBarcodeScanner(Activity activity) {
			this.activity = activity;
		}

		@Override
		public void executeOnClick() {
		    Intent intent = new Intent(AddNewProductListAdapterItem.SCAN_PRODUCT_ACTION);
	        intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
//		    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
		    try {
				activity.startActivityForResult(intent, 0);
			} catch (ActivityNotFoundException e) {
				Builder builder = new AlertDialog.Builder(activity);
				builder.setCancelable(true);
				builder.setMessage(activity.getResources().getString(R.string.BarcodeScannerNotFound));
				builder.setNegativeButton(android.R.string.no, new OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				Uri uri = Uri.parse("market://search?q=pname%3Acom.google.zxing.client.android");
				builder.setPositiveButton(android.R.string.yes, new SearchOnMarket(activity,uri ));
				builder.create().show();
			}
		}

		@Override
		protected String label() {
			return activity.getResources().getString(R.string.ScanBarcode);
		}
		
	}