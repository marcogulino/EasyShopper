package com.gmail.gulino.marco.easyshopper;

import android.app.Activity;
import android.content.Intent;

import com.gmail.gulino.marco.easyshopper.domain.Shopping;

public class LaunchShoppingActivity {

	private final Activity context;

	public LaunchShoppingActivity(Activity context) {
		this.context = context;
	}

	public void open(Shopping shopping) {
		Intent newShoppingActivity = new Intent(context, ShoppingActivity.class);
		newShoppingActivity.putExtra(ShoppingActivity.PARAM_SHOPPING_ID, shopping.getId());
		context.startActivityForResult(newShoppingActivity, 0);		
	}

}
