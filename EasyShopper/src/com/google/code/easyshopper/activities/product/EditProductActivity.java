package com.google.code.easyshopper.activities.product;

import java.util.HashMap;
import java.util.Map;

import android.app.TabActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;

import com.google.code.easyshopper.Logger;
import com.google.code.easyshopper.R;
import com.google.code.easyshopper.activities.ESTab;
import com.google.code.easyshopper.db.PriceDBAdapter;
import com.google.code.easyshopper.db.ProductDBAdapter;
import com.google.code.easyshopper.db.ShoppingDBAdapter;
import com.google.code.easyshopper.db.helpers.EasyShopperSqliteOpenHelper;
import com.google.code.easyshopper.domain.CartProduct;
import com.google.code.easyshopper.domain.Price;
import com.google.code.easyshopper.domain.Product;
import com.google.code.easyshopper.domain.Shopping;

public class EditProductActivity extends TabActivity {

	public static final String PARAM_BARCODE = "_BARCODE_";
	public static final int PRODUCT_SAVED = 100;
	public static final String PRODUCT_SAVED_ACTION = "PRODUCT_SAVED";
	public static final String PARAM_SHOPPING = "_shopping_id_";
	private Product product;
	private EditProduct editProduct;
	private Map<String, ESTab> tabs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tabs = new HashMap<String, ESTab>();
		setContentView(R.layout.product_tabbed_layout);
		TabHost tabhost = (TabHost) findViewById(android.R.id.tabhost);
		tabhost.setup();
		OnTabChangeListener listener = new OnTabChangeListener() {
			
			public void onTabChanged(String tabId) {
				Logger.d(this, "onTabChanged", "for tabId: " + tabId );
				editProduct.updateValuesOnExit();
				tabs.get(tabId).setup();
			}
		};
		tabhost.setOnTabChangedListener(listener );
		TabContentFactory tabContentFactory = new TabHost.TabContentFactory() {

			public View createTabContent(String tag) {
				ESTab currentTab = tabs.get(tag);
				Logger.d(this, "createTabContent", "tag: " + tag + ", content: " + currentTab);
				View view = currentTab.getView();
				return view;
			}
		};

		final String barcode = getIntent().getExtras().get(PARAM_BARCODE).toString();
		SQLiteOpenHelper sqLiteOpenHelper = new EasyShopperSqliteOpenHelper(this);
		Shopping shopping = new ShoppingDBAdapter(sqLiteOpenHelper).lookUp(getIntent().getExtras().getLong(
				PARAM_SHOPPING));
		product = new ProductDBAdapter(sqLiteOpenHelper).lookup(barcode);
		if(product==null) product= new Product(barcode);
		Logger.d(this, "onCreate", "product: " + product);

		createEditProductTab(tabhost, tabContentFactory, barcode, shopping);

		tabhost.setCurrentTab(0);
	}

	private void createEditProductTab(TabHost tabhost, TabContentFactory tabContentFactory, final String barcode,
			Shopping shopping) {
		SQLiteOpenHelper sqLiteOpenHelper = new EasyShopperSqliteOpenHelper(this);
		Price currentPrice = new PriceDBAdapter(sqLiteOpenHelper ).priceFor(product.getBarcode(), shopping.getMarket());

		editProduct = new EditProduct(new CartProduct(barcode, product, shopping, 0, currentPrice), this);
		tabs.put(EditProduct.TAG, editProduct);
		TabHost.TabSpec product_spec = tabhost.newTabSpec(EditProduct.TAG);
		product_spec.setContent(tabContentFactory);
		product_spec.setIndicator("Product");
		tabhost.addTab(product_spec);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		editProduct.getImageCleaner().clean();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Logger.d(this, "onActivityResult", ": {" + "requestCode=" + requestCode + ", resultCode=" + resultCode
				+ ", intent: " + data + "}");
		editProduct.getImageCleaner().refresh();
	}

}
