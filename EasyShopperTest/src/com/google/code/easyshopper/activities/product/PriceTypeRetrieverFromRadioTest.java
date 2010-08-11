package com.google.code.easyshopper.activities.product;

import java.util.Currency;

import android.test.AndroidTestCase;

import com.google.code.easyshopper.R;
import com.google.code.easyshopper.activities.IRadioGroup;
import com.google.code.easyshopper.domain.Amount;

public class PriceTypeRetrieverFromRadioTest extends AndroidTestCase {


	public class FakeRadioGroup implements IRadioGroup {
		private int selectedId;
		public int getCheckedRadioButtonId() {
			return selectedId;
		}
		public FakeRadioGroup setSelectedId(int id) {
			this.selectedId=id;
			return this;
		}
	}

	public void testPriceIsInBarcode() {
		FakeRadioGroup productPriceType = new FakeRadioGroup().setSelectedId(R.id.PriceTypeUnit);
		PriceTypeRetrieverFromRadio priceTypeRetrieverFromRadio = new PriceTypeRetrieverFromRadio(productPriceType);
		assertFalse(priceTypeRetrieverFromRadio.priceIsInBarcode());
		assertEquals(0, priceTypeRetrieverFromRadio.priceBarcodeChars());
		productPriceType.setSelectedId(R.id.PriceTypeWeight);
		assertTrue(priceTypeRetrieverFromRadio.priceIsInBarcode());
		assertEquals(6, priceTypeRetrieverFromRadio.priceBarcodeChars());
	}
	
	public void testShouldRetrievePriceFromBarcode() {
		FakeRadioGroup productPriceType = new FakeRadioGroup().setSelectedId(R.id.PriceTypeWeight);
		PriceTypeRetrieverFromRadio priceTypeRetrieverFromRadio = new PriceTypeRetrieverFromRadio(productPriceType);
		Currency currency = Currency.getInstance("EUR");
		assertEquals(new Amount(394, currency), priceTypeRetrieverFromRadio.getPrice("2096680003941", currency));
		assertEquals(new Amount(10394, currency), priceTypeRetrieverFromRadio.getPrice("2096680103941", currency));
		assertEquals(new Amount(394, currency), priceTypeRetrieverFromRadio.getPrice("20966801003941", currency));
	}
}
