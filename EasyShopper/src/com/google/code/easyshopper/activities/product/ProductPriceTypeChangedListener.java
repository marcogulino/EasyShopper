package com.google.code.easyshopper.activities.product;

import com.google.code.easyshopper.R;
import com.google.code.easyshopper.domain.Amount;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TableLayout;
import android.widget.TextView;

public class ProductPriceTypeChangedListener implements OnCheckedChangeListener {

	private final PriceTypeRetriever priceTypeRetriever;
	private final CurrencyRetriever currencyRetriever;
	private final String barcode;
	private final Activity activity;

	public ProductPriceTypeChangedListener(PriceTypeRetriever priceTypeRetriever, Activity activity, String barcode, CurrencyRetriever currencyRetriever) {
		this.priceTypeRetriever = priceTypeRetriever;
		this.activity = activity;
		this.barcode = barcode;
		this.currencyRetriever = currencyRetriever;
	}

	public void onCheckedChanged(RadioGroup group, int checkedId) {
		EditText editPrice=(EditText) activity.findViewById(R.id.EditPrice);
		TableLayout priceDetailsTable=(TableLayout) activity.findViewById(R.id.PriceDetailsTable);
		TextView productPriceLabel=(TextView) activity.findViewById(R.id.ProductPriceLabel);
		
		boolean priceIsInBarcode = priceTypeRetriever.priceIsInBarcode();
		
		priceDetailsTable.setVisibility(priceIsInBarcode ? View.VISIBLE : View.INVISIBLE);
		editPrice.setHint(priceIsInBarcode ? R.string.PriceForKilos : R.string.PriceForUnit);
		
		if(priceIsInBarcode) {
			editPrice.setText("");
			Amount price = priceTypeRetriever.getPrice(barcode, currencyRetriever.currency());
			String priceAsLabel = price.getReadableAmount(1);
			productPriceLabel.setText(priceAsLabel);
		}
	}

}
