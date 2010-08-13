package com.google.code.easyshopper.activities.product;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.code.easyshopper.R;
import com.google.code.easyshopper.domain.Amount;
import com.google.code.easyshopper.domain.CartProduct;

public class ProductPriceTypeChangedListener implements OnCheckedChangeListener {

	private final PriceTypeRetriever priceTypeRetriever;
	private final CurrencyRetriever currencyRetriever;
	private final Activity activity;
	private final CartProduct cartProduct;
	private final ImageManager cleaner;

	public ProductPriceTypeChangedListener(PriceTypeRetriever priceTypeRetriever, Activity activity, CartProduct cartProduct, CurrencyRetriever currencyRetriever, ImageManager cleaner) {
		this.priceTypeRetriever = priceTypeRetriever;
		this.activity = activity;
		this.cartProduct = cartProduct;
		this.currencyRetriever = currencyRetriever;
		this.cleaner = cleaner;
	}

	public void onCheckedChanged(RadioGroup group, int checkedId) {
		EditText editPrice=(EditText) activity.findViewById(R.id.EditPrice);
		TableLayout priceDetailsTable=(TableLayout) activity.findViewById(R.id.PriceDetailsTable);
		TextView productPriceLabel=(TextView) activity.findViewById(R.id.ProductPriceLabel);
		cartProduct.getProduct().setNumberOfPriceCharacters(priceTypeRetriever.priceBarcodeChars());
		
		boolean priceIsInBarcode = priceTypeRetriever.priceIsInBarcode();
		
		priceDetailsTable.setVisibility(priceIsInBarcode ? View.VISIBLE : View.INVISIBLE);
		editPrice.setHint(priceIsInBarcode ? R.string.PriceForKilos : R.string.PriceForUnit);
		
		if(priceIsInBarcode) {
			editPrice.setText("");
			Amount price = cartProduct.calculatePriceAmount(currencyRetriever.currency());
			String priceAsLabel = price.getReadableAmount(1);
			productPriceLabel.setText(priceAsLabel);
		}
		cleaner.refresh();
	}

}
