package com.google.code.easyshopper.activities.main;

import android.app.Activity;
import android.content.Intent;

import com.google.code.easyshopper.activities.shopping.ShoppingActivity;
import com.google.code.easyshopper.domain.Shopping;

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
