package com.google.code.easyshopper.activities.shopping;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.google.code.easyshopper.EditableTextDialog;
import com.google.code.easyshopper.domain.Shopping;
import com.google.code.easyshopper.utility.StringUtils;

final class ManuallyAddBarcodeDialog extends EditableTextDialog {
	private final Activity activity;
	private final Shopping shopping;

	ManuallyAddBarcodeDialog(Activity activity, Shopping shopping, String title) {
		super(activity, title);
		this.activity = activity;
		this.shopping = shopping;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		android.view.View.OnClickListener okButtonListener = new View.OnClickListener() {
			
			public void onClick(View paramView) {
				new LaunchProductActivity(activity, shopping).startProductActivity(StringUtils.editTextToString(getEditText()));
				dismiss();
			}
		};
		onCreate(savedInstanceState, okButtonListener);
	}
}