package com.google.code.easyshopper.activities.product;

import java.util.Currency;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.code.easyshopper.R;
import com.google.code.easyshopper.activities.ESTab;
import com.google.code.easyshopper.domain.CartProduct;
import com.google.code.easyshopper.domain.Price;

public class EditProduct implements ESTab {

	private final Activity activity;
	private ImageView productPictureView;
	private final CartProduct cartProduct;
	private final String barcode;
	private View view;
	private UpdateValues updateOnExit;
	private Spinner currencySpinner;
	private ArrayAdapter<CurrencyItem> currencySpinnerAdapter;
	private EditText editPrice;
	private ProductImageCleaner imageCleaner;
	private ProductSaver productSaver;
	static final String TAG = "edit_product";
	private static final String[] currencies = new String[] { "EUR", "USD" };
	private EditText productName;

	public EditProduct(String barcode, CartProduct cartProduct, Activity activity) {
		this.barcode = barcode;
		this.cartProduct = cartProduct;
		this.activity = activity;
		this.productSaver=new ProductSaver(cartProduct, activity);
	}

	public void refreshProductImage() {
		getImageCleaner().clean();
		productPictureView.setImageDrawable(cartProduct.getProduct().getImage().getDrawableForProductDetails(activity));
	}

	public void updateValuesOnExit() {
		if (updateOnExit != null)
			updateOnExit.update();
	}

	public View getView() {
		if (view == null)
			view = activity.getLayoutInflater().inflate(R.layout.edit_product, null);
		return view;
	}

	public void setup() {

		TextView barcodeTextview = (TextView) activity.findViewById(R.id.ProductBarcode);
		barcodeTextview.setText(barcode);

		Price currentPrice = cartProduct.getPrice();
		editPrice = (EditText) activity.findViewById(R.id.EditPrice);
		editPrice.setText(currentPrice != null ? currentPrice.getAmount().getReadableAmount(1) : "");
		Button saveButton = (Button) activity.findViewById(R.id.ProductCRUD_DoneButton);
		Button addToCart = (Button) activity.findViewById(R.id.AddToCartButton);
		productPictureView = (ImageView) activity.findViewById(R.id.ProductSmallPicture);
		productName = (EditText) activity.findViewById(R.id.ProductName);

		productName.addTextChangedListener(new ButtonsEnablerWatcher(saveButton, addToCart, editPrice));
		editPrice.addTextChangedListener(new ButtonsEnablerWatcher(saveButton, addToCart, productName) );
		CurrencyRetriever currencyRetriever = new CurrencyRetriever() {
			
			public Currency currency() {
				return currencySpinnerAdapter.getItem(currencySpinner.getSelectedItemPosition()).currency;
			}
		};
		saveButton.setOnClickListener(new SaveProductListener(productName, editPrice, barcode, currencyRetriever, productSaver, activity));
		addToCart.setOnClickListener(new AddToCartListener(cartProduct, productName, editPrice, currencyRetriever, barcode, cartProduct.getShopping(), productSaver, activity));

		((TextView) activity.findViewById(R.id.EditPriceDialog_MarketName)).setText(cartProduct.getShopping().getMarket().getName());

		currencySpinner = (Spinner) activity.findViewById(R.id.EditCurrency);
		currencySpinnerAdapter = new ArrayAdapter<CurrencyItem>(activity, android.R.layout.simple_dropdown_item_1line);
		currencySpinner.setAdapter(currencySpinnerAdapter);
		populateCurrencyCombo(currentPrice);


		// TODO check sul prezzo anzichè sul market
		addToCart.setEnabled(cartProduct.getShopping().getMarket() != null);
		productName.setText(cartProduct.getProduct().getName());
		productPictureView.setImageDrawable(cartProduct.getProduct().getImage().getDrawableForProductDetails(activity));
		this.imageCleaner = new ProductImageCleaner();
		productPictureView.setOnClickListener(new GrabImageLauncher(cartProduct.getProduct().getBarcode(), imageCleaner, activity));
	}

	public ImageCleaner getImageCleaner() {
		return imageCleaner;
	}

	private final class ProductImageCleaner implements ImageCleaner {
		public void clean() {
			if (!cartProduct.getProduct().getImage().hasImage(activity))
				return;
			try {
				BitmapDrawable drawable = (BitmapDrawable) productPictureView.getDrawable();
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


	private void populateCurrencyCombo(Price currentPrice) {
		CurrencyItem currentCurrency = null;
		currencySpinnerAdapter.clear();
		for (String currency : currencies) {
			CurrencyItem adapter = new CurrencyItem(Currency.getInstance(currency));
			if (currentPrice != null && currency.equals(currentPrice.getCurrency().getCurrencyCode())) {
				currentCurrency = adapter;
			}
			currencySpinnerAdapter.add(adapter);
		}
		currencySpinner.setSelection(currencySpinnerAdapter.getPosition(currentCurrency));
	}
}
