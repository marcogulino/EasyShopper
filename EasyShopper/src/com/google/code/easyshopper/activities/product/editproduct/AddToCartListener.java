package com.google.code.easyshopper.activities.product.editproduct;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.code.easyshopper.R;
import com.google.code.easyshopper.db.ProductShoppingDBAdapter;
import com.google.code.easyshopper.db.helpers.EasyShopperSqliteOpenHelper;
import com.google.code.easyshopper.domain.CartProduct;

public class AddToCartListener implements OnClickListener {
	private final ProductSaver productSaver;
	private final Activity activity;
	private final CartProduct cartProduct;

	public AddToCartListener(CartProduct cartProduct, ProductSaver productSaver, Activity activity) {
		this.cartProduct = cartProduct;
		this.productSaver = productSaver;
		this.activity = activity;
	}

	public void onClick(View v) {
		productSaver.save();
		addProductToCart();
	}

	private void addProductToCart() {
		ProductShoppingDBAdapter productShoppingDBAdapter = new ProductShoppingDBAdapter(new EasyShopperSqliteOpenHelper(activity));
		productShoppingDBAdapter.insertNewAssociation(cartProduct);
		int howMany = productShoppingDBAdapter.countProductForShopping(cartProduct.getShopping(), cartProduct.getProduct());
		String text = activity.getResources().getString(R.string.ProductActivity_HowMany).replaceAll("%\\{howmany\\}", String.valueOf(howMany))
				.replace("%{shoppingList}", cartProduct.getShopping().formattedDate(activity));
		Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
	}

}
