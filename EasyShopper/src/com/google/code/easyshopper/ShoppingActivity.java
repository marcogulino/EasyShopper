package com.google.code.easyshopper;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.code.easyshopper.db.PriceDBAdapter;
import com.google.code.easyshopper.db.ProductShoppingDBAdapter;
import com.google.code.easyshopper.db.ShoppingDBAdapter;
import com.google.code.easyshopper.db.helpers.EasyShopperSqliteOpenHelper;
import com.google.code.easyshopper.domain.Amount;
import com.google.code.easyshopper.domain.CartProduct;
import com.google.code.easyshopper.domain.Market;
import com.google.code.easyshopper.domain.Shopping;
import com.google.code.easyshopper.listeners.OnItemClickListenerForListAdapter;

public class ShoppingActivity extends Activity {
	public static final String PARAM_SHOPPING_ID = "_PARAM_SHOPPING_ID_";
	private ProductsListAdapter mainMenuListAdapter;
	private Shopping shopping;
	private ShoppingDBAdapter shoppingDBAdapter;

	public class PopulateList implements Runnable {
		public void run() {
			Logger.d(this, "populate", "refreshing shopping products list");
			ShoppingActivity.this.populateMainList();
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		long shoppingId = getIntent().getLongExtra(PARAM_SHOPPING_ID, -1);

		setContentView(R.layout.shopping_list_layout);

		ListView listView = (ListView) findViewById(R.id.shopping_list);
		TextView dateTextView = (TextView) findViewById(R.id.ShoppingDate);

		Button pickMarketButton = (Button) findViewById(R.id.PickMarket);
		Button suggestBestMarketButton = (Button) findViewById(R.id.SuggestBestMarket);

		suggestBestMarketButton.setEnabled(false);
		shoppingDBAdapter = new ShoppingDBAdapter(new EasyShopperSqliteOpenHelper(this));
		shopping = shoppingDBAdapter.lookUp(shoppingId);

		setMarketViewText();
		mainMenuListAdapter = new ProductsListAdapter(this);
		listView.setAdapter(mainMenuListAdapter);
		populateMainList();
		listView.setOnItemClickListener(new OnItemClickListenerForListAdapter());
		listView.setOnItemLongClickListener(new OnItemLongClickListenerForListAdapter());
		dateTextView.setText(shopping.formattedDate(this));

		pickMarketButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent marketActivity = new Intent(ShoppingActivity.this, MarketActivity.class);
				marketActivity.putExtra(MarketActivity.SHOPPING_ID, shopping.getId());
				startActivityForResult(marketActivity, MarketActivity.PICK_MARKET);
			}
		});
	}

	private void populateMainList() {
		mainMenuListAdapter.clear();
		mainMenuListAdapter.add(new AddNewProductListAdapterItem(this, shopping));
		EasyShopperSqliteOpenHelper sqLiteOpenHelper = new EasyShopperSqliteOpenHelper(this);
		Shopping reloadedShopping = new ShoppingDBAdapter(sqLiteOpenHelper).lookUp(shopping.getId());
		Logger.d(this, "populateMainList", "reloading shopping: old=" + shopping + ", reloaded=" + reloadedShopping);
		this.shopping = reloadedShopping;
		List<CartProduct> allProducts = new ProductShoppingDBAdapter(sqLiteOpenHelper).allProductsFor(this,
				reloadedShopping);
		int totalItems=0;
		for (CartProduct product : allProducts) {
			totalItems+= product.getQuantity();
			Logger.d(this, "populateMainList", "adding cart item: " + product);
			mainMenuListAdapter.add(new ProductListAdapterItem(product, this, new PopulateList()));
		}
		List<Amount> total = new PriceDBAdapter(sqLiteOpenHelper).calculateTotal(reloadedShopping);
		String separator = "";
		String totalLabel = "";
		for (Amount amount : total) {
			totalLabel += separator + amount.getReadableAmountLabel(1);
			separator = "\n";
		}
		((TextView) findViewById(R.id.PriceTotal)).setText(totalLabel);
		((TextView) findViewById(R.id.TotalItems)).setText(String.valueOf(totalItems));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Logger.d(this, "onActivityResult", "got " + requestCode + ", " + resultCode + ", " + data);
		if (data == null) {
			Logger.d(this, "onActivityResult", "no data found, exiting");
			return;
		}
		String activity = data.getAction();
		if (AddNewProductListAdapterItem.SCAN_PRODUCT_ACTION.equals(activity)) {
			String barcode = data.getStringExtra("SCAN_RESULT");
			Logger.d(this, "onActivityResult", "contents: " + barcode);
			new LaunchProductActivity(this, shopping).startProductActivity(barcode);
			return;
		}
		Logger.d(this, "onActivityResult", "got component: " + activity);
		if (MarketActivity.SELECTED_MARKET.equals(activity)) {
			this.shopping = shoppingDBAdapter.lookUp(shopping.getId());
			setMarketViewText();
		}
		populateMainList();
	}

	private void setMarketViewText() {
		TextView marketTextView = (TextView) findViewById(R.id.Market);
		Market market = shopping.getMarket();
		marketTextView.setText(market != null ? market.getName() : "");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		populateMainList();
	}
}
