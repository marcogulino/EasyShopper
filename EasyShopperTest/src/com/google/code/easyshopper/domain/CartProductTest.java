package com.google.code.easyshopper.domain;

import java.util.Currency;

import android.test.AndroidTestCase;

public class CartProductTest extends AndroidTestCase  {

	
	public void testShouldRetrievePriceFromBarcode() {
		Currency currency = Currency.getInstance("EUR");
		;
		Product aProduct = new Product("1234");
		aProduct.setNumberOfPriceCharacters(6);
		assertEquals(new Amount(394, currency), new CartProduct("2096680003941", aProduct, null, 1, null).calculatePriceAmount(currency));
		assertEquals(new Amount(394, currency), new CartProduct("2096680003941", aProduct, null, 2, null).calculatePriceAmount(currency));
		assertEquals(new Amount(10394, currency), new CartProduct("2096680103941", aProduct, null, 1, null).calculatePriceAmount(currency));
		assertEquals(new Amount(394, currency), new CartProduct("20966801003941", aProduct, null, 1, null).calculatePriceAmount(currency));
	}
}
