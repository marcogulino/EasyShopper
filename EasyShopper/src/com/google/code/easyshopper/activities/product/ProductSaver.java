package com.google.code.easyshopper.activities.product;

import java.util.Currency;

import android.app.Activity;

import com.google.code.easyshopper.db.PriceDBAdapter;
import com.google.code.easyshopper.db.ProductDBAdapter;
import com.google.code.easyshopper.db.helpers.EasyShopperSqliteOpenHelper;
import com.google.code.easyshopper.domain.CartProduct;
import com.google.code.easyshopper.domain.Price;

public class ProductSaver {

	private final Activity activity;
	private final CartProduct cartProduct;

	public ProductSaver(CartProduct cartProduct, Activity activity) {
		this.cartProduct = cartProduct;
		this.activity = activity;
		
		
	}
	
	public void save(String barcode, String productName, Currency currency, String priceString) {
		ProductDBAdapter productDBAdapter = new ProductDBAdapter(new EasyShopperSqliteOpenHelper(activity));
		productDBAdapter.save(barcode, productName);
		cartProduct.setProduct(productDBAdapter.lookup(barcode));
		Price price = cartProduct.getPrice();
		if (price == null) {
			price = new Price(-1);
		}
		price.setProduct(cartProduct.getProduct());
		price.setMarket(cartProduct.getShopping().getMarket());
		price.getAmount().setCurrency(currency);
		price.getAmount().setFromReadableAmount(priceString);
		new PriceDBAdapter(new EasyShopperSqliteOpenHelper(activity)).saveAndAssociate(price, cartProduct);		
	}
}
