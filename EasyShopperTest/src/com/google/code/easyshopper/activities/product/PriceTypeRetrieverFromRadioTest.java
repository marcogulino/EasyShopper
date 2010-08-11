package com.google.code.easyshopper.activities.product;

import android.test.AndroidTestCase;

import com.google.code.easyshopper.R;
import com.google.code.easyshopper.activities.IRadioGroup;

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

}
