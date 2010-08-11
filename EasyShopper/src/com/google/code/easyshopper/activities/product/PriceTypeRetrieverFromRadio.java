package com.google.code.easyshopper.activities.product;

import java.util.Currency;

import com.google.code.easyshopper.R;
import com.google.code.easyshopper.activities.IRadioGroup;
import com.google.code.easyshopper.domain.Amount;

public class PriceTypeRetrieverFromRadio implements PriceTypeRetriever {
	private final IRadioGroup productPriceType;

	PriceTypeRetrieverFromRadio(IRadioGroup productPriceType) {
		this.productPriceType = productPriceType;
	}

	public boolean priceIsInBarcode() {
		return productPriceType.getCheckedRadioButtonId() == R.id.PriceTypeWeight;
	}

	public int priceBarcodeChars() {
		return priceIsInBarcode() ? 6 : 0;
	}

	public Amount getPrice(String barcode, Currency currency) {
		if( ! priceIsInBarcode() )return null;
		StringBuilder price= new StringBuilder(barcode.substring(barcode.length() - priceBarcodeChars()));
		Amount amount = new Amount().setCurrency(currency);
		price.insert(price.length()-3, amount.getSeparator());
		return amount.setFromReadableAmount(price.toString());
	}
}
