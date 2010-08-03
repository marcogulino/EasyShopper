package com.google.code.easyshopper.db.domaincreators;

import android.database.Cursor;

import com.google.code.easyshopper.db.ProductDBAdapter.Columns;
import com.google.code.easyshopper.db.helpers.MyCursor;
import com.google.code.easyshopper.domain.Product;

public class ProductCreator implements DomainObjectCreator<Product> {

	public Product create(Cursor cursor) {
		MyCursor query = new MyCursor(cursor);
		String barcode = query.getString(Columns.barcode.column());
		String productName = query.getString(Columns.name.column());
		return new Product(barcode, productName);
	}

}
