package com.google.code.easyshopper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.google.code.easyshopper.db.ShoppingDBAdapter;
import com.google.code.easyshopper.db.helpers.EasyShopperSqliteOpenHelper;
import com.google.code.easyshopper.domain.Shopping;

public class ShoppingListAdapter extends ModelListAdapterItem {

	private final String label;
	private final Shopping shopping;
	private final Activity activity;
	private final Runnable listPopulator;

	public ShoppingListAdapter(String label, Shopping shopping, Activity activity, Runnable listPopulator) {
		this.label = label;
		this.shopping = shopping;
		this.activity = activity;
		this.listPopulator = listPopulator;
	}

	@Override
	public void executeOnClick() {
		new LaunchShoppingActivity(activity).open(shopping);
	}

	@Override
	public boolean executeOnLongClick() {
		AlertDialog.Builder builder = new Builder(activity);
		String[] longClickActions = new String[] { activity.getResources().getString(R.string.delete) };
		longClickDialog(builder, longClickActions);
		return true;
	}
	
	private void longClickDialog(AlertDialog.Builder builder, final String[] longClickActions) {
		builder.setItems(longClickActions , new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				new ShoppingDBAdapter(new EasyShopperSqliteOpenHelper(activity)).delete(shopping);
				listPopulator.run();
			}
		});
		builder.create().show();
	}

	@Override
	protected String label() {
		return label;
	}

}
