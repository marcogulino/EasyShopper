package com.gmail.gulino.marco.easyshopper.db.domaincreators;

import android.database.Cursor;

import com.gmail.gulino.marco.easyshopper.db.ProductDBAdapter.Columns;
import com.gmail.gulino.marco.easyshopper.db.helpers.MyCursor;
import com.gmail.gulino.marco.easyshopper.domain.Product;

public class ProductCreator implements DomainObjectCreator<Product> {

	public Product create(Cursor cursor) {
		MyCursor query = new MyCursor(cursor);
		int id=query.getInt(Columns.id.column());
		String barcode = query.getString(Columns.barcode.column());
		String productName = query.getString(Columns.name.column());
		return new Product(id, barcode, productName);
	}

}
