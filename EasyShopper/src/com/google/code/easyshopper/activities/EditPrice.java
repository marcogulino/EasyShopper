package com.google.code.easyshopper.activities;

import java.util.Currency;

import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.code.easyshopper.Logger;
import com.google.code.easyshopper.R;
import com.google.code.easyshopper.db.PriceDBAdapter;
import com.google.code.easyshopper.db.helpers.EasyShopperSqliteOpenHelper;
import com.google.code.easyshopper.domain.CartProduct;
import com.google.code.easyshopper.domain.Market;
import com.google.code.easyshopper.domain.Price;

public class EditPrice implements ESTab {
	private final CartProduct cartProduct;
	private final Activity activity;
	private ArrayAdapter<CurrencyItem> currencySpinnerAdapter;
	private Spinner currencySpinner;
	private EditText editPrice;
	private final Runnable runOnOK;
	private View view;
	public static final String TAG = "edit_price";
	private static String[] currencies=new String[]{"EUR", "USD"};

	public EditPrice(Activity activity, CartProduct cartProduct, Runnable runOnOk) {
		this.activity = activity;
		this.cartProduct = cartProduct;
		this.runOnOK = runOnOk;
	}
	
	public View getView() {
		if(view == null)
			view = activity.getLayoutInflater().inflate(R.layout.edit_price_dialog, null);
		return view;
	}
	
	public void setup() {
//		this.setTitle(context.getResources().getString(R.string.Shopping_SetProductPrice));
//		setContentView(R.layout.edit_price_dialog);
		Logger.d(this, "setup", "cartProduct: " + cartProduct);
		((TextView) activity.findViewById(R.id.EditPriceDialog_ProductName)).setText(cartProduct.getProduct().getName());
		Market market = cartProduct.getShopping().getMarket();
		((TextView) activity.findViewById(R.id.EditPriceDialog_MarketName)).setText(market.getName());
		
		currencySpinner = (Spinner) activity.findViewById(R.id.EditCurrency);
		currencySpinnerAdapter = new ArrayAdapter<CurrencyItem>(activity, android.R.layout.simple_dropdown_item_1line);
		currencySpinner.setAdapter(currencySpinnerAdapter);
		Price currentPrice=cartProduct.getPrice();
		populateCurrencyCombo(currentPrice);
		
		((Button) activity.findViewById(R.id.EditPriceDialog_Cancel)).setOnClickListener(new Cancel());
		((Button) activity.findViewById(R.id.EditPriceDialog_Ok)).setOnClickListener(new SavePrice());
		editPrice = (EditText) activity.findViewById(R.id.EditPrice);
		editPrice.setText(currentPrice!=null?currentPrice.getAmount().getReadableAmount(1):"");
	}
	

	private void populateCurrencyCombo(Price currentPrice) {
		CurrencyItem currentCurrency =null;
		currencySpinnerAdapter.clear();
		for (String currency : currencies) {
			CurrencyItem adapter = new CurrencyItem(Currency.getInstance(currency));
			if(currentPrice!=null && currency.equals(currentPrice.getCurrency().getCurrencyCode())) {
				currentCurrency=adapter;
			}
			currencySpinnerAdapter.add(adapter);
		}
		currencySpinner.setSelection(currencySpinnerAdapter.getPosition(currentCurrency));
	}


	public class SavePrice implements android.view.View.OnClickListener {

		public void onClick(View v) {
			Price price =cartProduct.getPrice();
			if(price==null) {
				price=new Price(-1);
			}
			price.setProduct(cartProduct.getProduct());
			price.setMarket(cartProduct.getShopping().getMarket());
			price.getAmount().setCurrency(currencySpinnerAdapter.getItem(currencySpinner.getSelectedItemPosition()).currency);
			price.getAmount().setFromReadableAmount(editPrice.getText().toString());
			new PriceDBAdapter(new EasyShopperSqliteOpenHelper(activity)).saveAndAssociate(price, cartProduct);
			runOnOK.run();
			// TODO what to do here?
//			SetPriceDialog.this.dismiss();
		}

	}
	public class Cancel implements android.view.View.OnClickListener {
		public void onClick(View v) {
			// TODO what to do here?
//			SetPriceDialog.this.cancel();
		}

	}
	public class CurrencyItem {
		public final Currency currency;
		public CurrencyItem(Currency instance) {
			this.currency = instance;
		}
		@Override
		public String toString() {
			return currency.getSymbol();
		}
	}
	public void updateValuesOnExit() {
		// TODO Auto-generated method stub
	}
}
