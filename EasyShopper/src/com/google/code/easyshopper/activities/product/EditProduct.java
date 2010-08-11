package com.google.code.easyshopper.activities.product;

import java.util.Currency;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.code.easyshopper.R;
import com.google.code.easyshopper.activities.ESTab;
import com.google.code.easyshopper.db.PriceDBAdapter;
import com.google.code.easyshopper.db.ProductDBAdapter;
import com.google.code.easyshopper.db.ProductShoppingDBAdapter;
import com.google.code.easyshopper.db.helpers.EasyShopperSqliteOpenHelper;
import com.google.code.easyshopper.domain.CartProduct;
import com.google.code.easyshopper.domain.Market;
import com.google.code.easyshopper.domain.Price;
import com.google.code.easyshopper.domain.Shopping;

public class EditProduct implements ESTab {

	private final Activity activity;
	private final SQLiteOpenHelper sqLiteOpenHelper;
	private ImageView productPictureView;
	private final CartProduct cartProduct;
	private final String barcode;
	private View view;
	private UpdateValues updateOnExit;
	private Spinner currencySpinner;
	private ArrayAdapter<CurrencyItem> currencySpinnerAdapter;
	private EditText editPrice;
	private ProductImageCleaner imageCleaner;
	static final String TAG = "edit_product";
	private static final String[] currencies = new String[] { "EUR", "USD" };

	public EditProduct(String barcode, CartProduct cartProduct,
			Activity activity, SQLiteOpenHelper sqLiteOpenHelper) {
		this.barcode = barcode;
		this.cartProduct = cartProduct;
		this.activity = activity;
		this.sqLiteOpenHelper = sqLiteOpenHelper;

	}

	public View getView() {
		if (view == null)
			view = activity.getLayoutInflater().inflate(R.layout.edit_product,
					null);
		return view;
	}

	public void setup() {

		TextView barcodeTextview = (TextView) activity
				.findViewById(R.id.ProductBarcode);
		barcodeTextview.setText(barcode);

		productPictureView = (ImageView) activity
				.findViewById(R.id.ProductSmallPicture);
		final EditText productName = (EditText) activity
				.findViewById(R.id.ProductName);
		Button saveButton = (Button) activity
				.findViewById(R.id.ProductCRUD_DoneButton);
		Button addToCart = (Button) activity.findViewById(R.id.AddToCartButton);
		saveButton.setOnClickListener(new SaveProductListener(productName,
				barcode));
		addToCart.setOnClickListener(new AddToCartListener(productName,
				barcode, cartProduct.getShopping()));

		((TextView) activity.findViewById(R.id.EditPriceDialog_MarketName))
				.setText(cartProduct.getShopping().getMarket().getName());

		currencySpinner = (Spinner) activity.findViewById(R.id.EditCurrency);
		currencySpinnerAdapter = new ArrayAdapter<CurrencyItem>(activity,
				android.R.layout.simple_dropdown_item_1line);
		currencySpinner.setAdapter(currencySpinnerAdapter);
		Price currentPrice = cartProduct.getPrice();
		populateCurrencyCombo(currentPrice);

		editPrice = (EditText) activity.findViewById(R.id.EditPrice);
		editPrice.setText(currentPrice != null ? currentPrice.getAmount()
				.getReadableAmount(1) : "");

		// TODO check sul prezzo anzichè sul market
		addToCart.setEnabled(cartProduct.getShopping().getMarket() != null);
		productName.setText(cartProduct.getProduct().getName());
		productPictureView.setImageDrawable(cartProduct.getProduct().getImage()
				.getDrawableForProductDetails(activity));
		this.imageCleaner = new ProductImageCleaner();
		productPictureView.setOnClickListener(new GrabImageLauncher(cartProduct
				.getProduct().getBarcode(), imageCleaner, activity));
	}

	private void populateCurrencyCombo(Price currentPrice) {
		CurrencyItem currentCurrency = null;
		currencySpinnerAdapter.clear();
		for (String currency : currencies) {
			CurrencyItem adapter = new CurrencyItem(
					Currency.getInstance(currency));
			if (currentPrice != null
					&& currency.equals(currentPrice.getCurrency()
							.getCurrencyCode())) {
				currentCurrency = adapter;
			}
			currencySpinnerAdapter.add(adapter);
		}
		currencySpinner.setSelection(currencySpinnerAdapter
				.getPosition(currentCurrency));
	}

	public ImageCleaner getImageCleaner() {
		return imageCleaner;
	}

	private void saveProduct(String barcode, EditText productName) {
		ProductDBAdapter productDBAdapter = new ProductDBAdapter(
				sqLiteOpenHelper);
		productDBAdapter.save(barcode, productName.getText().toString().trim());
		cartProduct.setProduct(productDBAdapter.lookup(barcode));
		Price price = cartProduct.getPrice();
		if (price == null) {
			price = new Price(-1);
		}
		price.setProduct(cartProduct.getProduct());
		price.setMarket(cartProduct.getShopping().getMarket());
		price.getAmount().setCurrency(
				currencySpinnerAdapter.getItem(currencySpinner
						.getSelectedItemPosition()).currency);
		price.getAmount().setFromReadableAmount(editPrice.getText().toString());
		new PriceDBAdapter(new EasyShopperSqliteOpenHelper(activity))
				.saveAndAssociate(price, cartProduct);
	}

	private final class ProductImageCleaner implements ImageCleaner {
		public void clean() {
			if (!cartProduct.getProduct().getImage().hasImage(activity))
				return;
			try {
				BitmapDrawable drawable = (BitmapDrawable) productPictureView
						.getDrawable();
				Bitmap bitmap = drawable.getBitmap();
				productPictureView.setImageBitmap(null);
				bitmap.recycle();
			} catch (Exception e) {

			}
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

	private final class SaveProductListener implements OnClickListener {
		private final EditText productName;
		private final String barcode;

		private SaveProductListener(EditText productName, String barcode) {
			this.productName = productName;
			this.barcode = barcode;
		}

		public void onClick(View v) {
			saveProduct(barcode, productName);
			Intent intent = activity.getIntent();
			intent.setAction(EditProductActivity.PRODUCT_SAVED_ACTION);
			activity.setResult(EditProductActivity.PRODUCT_SAVED, intent);
			activity.finish();

		}
	}

	public class AddToCartListener implements OnClickListener {

		private final class AddProductToCart implements Runnable {
			public void run() {
				addProductToCart();
			}
		}

		private final EditText productName;
		private final String barcode;
		private final Shopping shopping;

		public AddToCartListener(EditText productName, String barcode,
				Shopping shopping) {
			this.productName = productName;
			this.barcode = barcode;
			this.shopping = shopping;
		}

		public void onClick(View v) {
			saveProduct(barcode, productName);
			Market market = shopping.getMarket();
			if (market != null) {
				if (new PriceDBAdapter(sqLiteOpenHelper).priceFor(barcode,
						market) == null) {
					Runnable onOk = new AddProductToCart();
					onOk.run();
					// CartProduct cartProduct = new
					// CartProduct(EditProduct.this.cartProduct.getProduct(),
					// shopping, 1, null);
					// TODO show setPriceDialog tab instead
					// new SetPriceDialog(activity, cartProduct, onOk).show();
				} else {
					addProductToCart();
				}
			}
		}

		private void addProductToCart() {
			ProductShoppingDBAdapter productShoppingDBAdapter = new ProductShoppingDBAdapter(
					sqLiteOpenHelper);
			productShoppingDBAdapter.insertNewAssociation(barcode, shopping);
			int howMany = productShoppingDBAdapter.countProductForShopping(
					shopping, cartProduct.getProduct());
			String text = activity
					.getResources()
					.getString(R.string.ProductActivity_HowMany)
					.replaceAll("%\\{howmany\\}", String.valueOf(howMany))
					.replace("%{shoppingList}",
							shopping.formattedDate(activity));
			Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
		}
	}

	public void refreshProductImage() {
		getImageCleaner().clean();
		productPictureView.setImageDrawable(cartProduct.getProduct().getImage()
				.getDrawableForProductDetails(activity));
	}

	public void updateValuesOnExit() {
		if (updateOnExit != null)
			updateOnExit.update();
	}

}
