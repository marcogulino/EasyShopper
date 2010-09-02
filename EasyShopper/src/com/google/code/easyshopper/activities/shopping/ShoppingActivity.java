package com.google.code.easyshopper.activities.shopping;

import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.code.easyshopper.Logger;
import com.google.code.easyshopper.OnItemLongClickListenerForListAdapter;
import com.google.code.easyshopper.R;
import com.google.code.easyshopper.activities.ViewInItemListAdapter;
import com.google.code.easyshopper.activities.market.MarketActivity;
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
	private ViewInItemListAdapter mainMenuListAdapter;
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
		mainMenuListAdapter = new ViewInItemListAdapter(this);
		listView.setAdapter(mainMenuListAdapter);
		populateMainList();
		listView.setOnItemClickListener(new OnItemClickListenerForListAdapter());
		listView.setOnItemLongClickListener(new OnItemLongClickListenerForListAdapter());
		dateTextView.setText(shopping.formattedDate(this));

		pickMarketButton.setOnClickListener(new PickMarketListener(this, shopping));
	}

	private void populateMainList() {
		mainMenuListAdapter.clear();
		EasyShopperSqliteOpenHelper sqLiteOpenHelper = new EasyShopperSqliteOpenHelper(this);
		this.shopping = new ShoppingDBAdapter(sqLiteOpenHelper).lookUp(shopping.getId());
		if(shopping.getMarket() == null ) {
			mainMenuListAdapter.add(new PickMarketListAdapter(this, shopping));
		} else {
			mainMenuListAdapter.add(new AddNewProductListAdapterItem(this, shopping));
		}
		List<CartProduct> allProducts = new ProductShoppingDBAdapter(sqLiteOpenHelper).allProductsFor(this,
				shopping);
		int totalItems=0;
		Map<Currency, Amount> totals=new HashMap<Currency, Amount>();
		for (CartProduct product : allProducts) {
			totalItems+= product.getQuantity();
			Logger.d(this, "populateMainList", "adding cart item: " + product);
			mainMenuListAdapter.add(new ProductListAdapterItem(product, this, new PopulateList()));
			addPriceToTotal(product, totals);
		}
		String separator = "";
		String totalLabel = "";
		for (Currency currency : totals.keySet()) {
			totalLabel += separator + totals.get(currency).getReadableAmountLabel(1);
			separator = "\n";
		}
		((TextView) findViewById(R.id.PriceTotal)).setText(totalLabel);
		((TextView) findViewById(R.id.TotalItems)).setText(String.valueOf(totalItems));
	}

	private void addPriceToTotal(CartProduct product, Map<Currency, Amount> totals) {
		Currency currency = product.getPrice().getCurrency();
		Amount currentAmount = totals.get(currency);
		if(currentAmount==null) {
			currentAmount=new Amount(0l, currency);
			totals.put(currency, currentAmount);
		}
		long partial = product.calculatePriceAmount().getAmount() * product.getQuantity();
		currentAmount.setAmount(partial + currentAmount.getAmount());
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
