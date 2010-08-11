package com.google.code.easyshopper.activities.product;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.google.code.easyshopper.R;
import com.google.code.easyshopper.db.ProductShoppingDBAdapter;
import com.google.code.easyshopper.db.helpers.EasyShopperSqliteOpenHelper;
import com.google.code.easyshopper.domain.CartProduct;
import com.google.code.easyshopper.domain.Shopping;
import com.google.code.easyshopper.utility.StringUtils;

public class AddToCartListener implements OnClickListener {
	private final EditText productName;
	private final String barcode;
	private final Shopping shopping;
	private final ProductSaver productSaver;
	private final EditText editPrice;
	private final Activity activity;
	private final CartProduct cartProduct;
	private final CurrencyRetriever currencyRetriever;
	private final PriceTypeRetriever priceTypeRetriever;

	public AddToCartListener(CartProduct cartProduct, EditText productName, EditText editPrice, PriceTypeRetriever priceTypeRetriever, CurrencyRetriever currencyRetriever, String barcode, Shopping shopping, ProductSaver productSaver, Activity activity) {
		this.cartProduct = cartProduct;
		this.productName = productName;
		this.editPrice = editPrice;
		this.priceTypeRetriever = priceTypeRetriever;
		this.currencyRetriever = currencyRetriever;
		this.barcode = barcode;
		this.shopping = shopping;
		this.productSaver = productSaver;
		this.activity = activity;
	}

	public void onClick(View v) {
		productSaver.save(this.barcode, StringUtils.editTextToString(this.productName), priceTypeRetriever.priceBarcodeChars(), currencyRetriever.currency(), StringUtils.editTextToString(editPrice));
		addProductToCart();
	}

	private void addProductToCart() {
		ProductShoppingDBAdapter productShoppingDBAdapter = new ProductShoppingDBAdapter(new EasyShopperSqliteOpenHelper(activity));
		productShoppingDBAdapter.insertNewAssociation(barcode, shopping);
		int howMany = productShoppingDBAdapter.countProductForShopping(shopping, cartProduct.getProduct());
		String text = activity.getResources().getString(R.string.ProductActivity_HowMany).replaceAll("%\\{howmany\\}", String.valueOf(howMany))
				.replace("%{shoppingList}", shopping.formattedDate(activity));
		Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
	}

}
