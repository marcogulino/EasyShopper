package com.google.code.easyshopper.activities.shopping;

import android.app.Activity;
import android.content.Intent;

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
		    activity.startActivityForResult(intent, 0);
		}

		@Override
		protected String label() {
			return activity.getResources().getString(R.string.ScanBarcode);
		}
		
	}