package com.gmail.gulino.marco.easyshopper;

import android.app.Activity;

import com.gmail.gulino.marco.easyshopper.db.ShoppingDBAdapter;
import com.gmail.gulino.marco.easyshopper.db.helpers.EasyShopperSqliteOpenHelper;
import com.gmail.gulino.marco.easyshopper.domain.Shopping;

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
