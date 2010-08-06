package com.google.code.easyshopper.activities.product;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;

import com.google.code.easyshopper.Logger;
import com.google.code.easyshopper.R;
import com.google.code.easyshopper.db.ProductDBAdapter;
import com.google.code.easyshopper.db.ShoppingDBAdapter;
import com.google.code.easyshopper.db.helpers.EasyShopperSqliteOpenHelper;
import com.google.code.easyshopper.domain.Product;
import com.google.code.easyshopper.domain.Shopping;

public class EditProductActivity extends Activity {

	public static final String PARAM_BARCODE = "_BARCODE_";
	public static final int PRODUCT_SAVED = 100;
	public static final String PRODUCT_SAVED_ACTION = "PRODUCT_SAVED";
	public static final String PARAM_SHOPPING = "_shopping_id_";
	private Product product;
	private SQLiteOpenHelper sqLiteOpenHelper;
	private EditProduct editProduct;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_tabbed_layout);
		TabHost tabs=(TabHost)findViewById(R.id.ProductTabHost); 
		tabs.setup();
		TabContentFactory tabContentFactory = new TabHost.TabContentFactory() {

			public View createTabContent(String tag) {
				int layout = R.layout.edit_product;
				if("edit_price".equals(tag)) layout=R.layout.edit_price_dialog; 
				View inflate = getLayoutInflater().inflate(layout, null);
				return inflate;
			}
			
		};
		TabHost.TabSpec product_spec=tabs.newTabSpec("edit_product");
		product_spec.setContent(tabContentFactory);
		product_spec.setIndicator("Product");
		tabs.addTab(product_spec);
		
		TabHost.TabSpec price_spec=tabs.newTabSpec("edit_price");
		price_spec.setContent(tabContentFactory);
		price_spec.setIndicator("Price");
		tabs.addTab(price_spec);
		
		tabs.setCurrentTab(0);
		final String barcode = getIntent().getExtras().get(PARAM_BARCODE).toString();
		sqLiteOpenHelper = new EasyShopperSqliteOpenHelper(this);
		Shopping shopping = new ShoppingDBAdapter(sqLiteOpenHelper).lookUp(getIntent().getExtras().getLong(
				PARAM_SHOPPING));
		product = new ProductDBAdapter(sqLiteOpenHelper).lookup(barcode);

		editProduct = new EditProduct(barcode, product, shopping, this, sqLiteOpenHelper);
		editProduct.setup();
	}



	@Override
	protected void onDestroy() {
		super.onDestroy();
		editProduct.cleanProductImage();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Logger.d(this, "onActivityResult", ": {" + "requestCode=" + requestCode + ", resultCode=" + resultCode
				+ ", intent: " + data + "}");
		editProduct.refreshProductImage();
	}



}
