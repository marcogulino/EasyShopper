package com.google.code.easyshopper.activities.product;

import java.util.Currency;

import com.google.code.easyshopper.domain.Amount;

public interface PriceTypeRetriever {
	public int priceBarcodeChars();
	public boolean priceIsInBarcode();
	public Amount getPrice(String barcode, Currency currency);
}
