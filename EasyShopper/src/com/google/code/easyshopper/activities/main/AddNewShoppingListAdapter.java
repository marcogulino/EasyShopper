package com.google.code.easyshopper.activities.main;

import android.app.Activity;

import com.google.code.easyshopper.ModelListAdapterItem;
import com.google.code.easyshopper.R;
import com.google.code.easyshopper.db.ShoppingDBAdapter;
import com.google.code.easyshopper.db.helpers.EasyShopperSqliteOpenHelper;
import com.google.code.easyshopper.domain.Shopping;

public class AddNewShoppingListAdapter extends ModelListAdapterItem {

	private final Activity activity;

	public AddNewShoppingListAdapter(Activity activity) {
		this.activity = activity;
	}

	@Override
	public void executeOnClick() {
		Shopping shopping = new ShoppingDBAdapter(new EasyShopperSqliteOpenHelper(activity)).createNew();
		new LaunchShoppingActivity(activity).open(shopping);

	}

	@Override
	protected String label() {
		return activity.getResources().getString(R.string.NewShoppingMenuItem);
	}

}
