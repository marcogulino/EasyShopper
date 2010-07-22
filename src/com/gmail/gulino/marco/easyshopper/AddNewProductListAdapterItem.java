package com.gmail.gulino.marco.easyshopper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.gulino.marco.easyshopper.domain.Shopping;

public class AddNewProductListAdapterItem extends ProductModelListAdapterItem {
	public static final String SCAN_PRODUCT_ACTION = "com.google.zxing.client.android.SCAN";
	private final Activity activity;
	private final Shopping shopping;

	public AddNewProductListAdapterItem(Activity activity, Shopping shopping) {
		this.activity = activity;
		this.shopping = shopping;
	}

	public void executeOnClick() {
		AlertDialog.Builder builder=new Builder(activity);
		builder.setCancelable(true).setTitle(R.string.ScanNewProductLabel);
		final ArrayAdapter<ModelListAdapterItem> adapter = new ArrayAdapter<ModelListAdapterItem>(activity, android.R.layout.simple_dropdown_item_1line);
		builder.setAdapter(adapter, new OnClickListener() {
			
			public void onClick(DialogInterface paramDialogInterface, int paramInt) {
				ModelListAdapterItem item = adapter.getItem(paramInt);
				item.executeOnClick();
			}
		});
		adapter.add(new StartBarcodeScanner(activity));
		adapter.add(new ManuallyAddBarcode());
		builder.create().show();
		if(true) return;

	}
	

	@Override
	public View getView(Activity activity) {
		View view = activity.getLayoutInflater().inflate(R.layout.products_item_layout, null);
		((TextView) view.findViewById(R.id.ProductPriceQuantity)).setText("");
		((TextView) view.findViewById(R.id.ProductPrice)).setText("");
		((TextView) view.findViewById(R.id.ProductName)).setText(label());
		((ImageView) view.findViewById(R.id.ListViewProductImage)).setImageResource(android.R.drawable.ic_menu_add);
		return view;
	}
	
	@Override
	public String label() {
		return activity.getResources().getString(R.string.ScanNewProductLabel);
	}
	
	public static class StartBarcodeScanner extends ModelListAdapterItem {
		private final Activity activity;

		public StartBarcodeScanner(Activity activity) {
			this.activity = activity;
		}

		@Override
		public void executeOnClick() {
		    Intent intent = new Intent(SCAN_PRODUCT_ACTION);
	        intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
//		    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
		    activity.startActivityForResult(intent, 0);
		}

		@Override
		protected String label() {
			return activity.getResources().getString(R.string.ScanBarcode);
		}
		
	}
	
	private class ManuallyAddBarcode extends ModelListAdapterItem {

		@Override
		public void executeOnClick() {
			final EditableTextDialog editableTextDialog = new ManuallyAddBarcodeDialog(activity, shopping, label());
			editableTextDialog.show();
		}

		@Override
		protected String label() {
			return activity.getResources().getString(R.string.ManuallyAddBarcode);
		}
		
	}
	
	private static final class ManuallyAddBarcodeDialog extends EditableTextDialog {
		private final Activity activity;
		private final Shopping shopping;

		private ManuallyAddBarcodeDialog(Activity activity, Shopping shopping, String title) {
			super(activity, title);
			this.activity = activity;
			this.shopping = shopping;
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			android.view.View.OnClickListener okButtonListener = new View.OnClickListener() {
				
				public void onClick(View paramView) {
					new LaunchProductActivity(activity, shopping).startProductActivity(getEditText());
					dismiss();
				}
			};
			onCreate(savedInstanceState, okButtonListener);
		}
	}

}
