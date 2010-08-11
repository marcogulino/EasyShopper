package com.google.code.easyshopper.db;

import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.code.easyshopper.Logger;
import com.google.code.easyshopper.db.domaincreators.ProductCreator;
import com.google.code.easyshopper.db.helpers.Column;
import com.google.code.easyshopper.db.helpers.Column.ColumnType;
import com.google.code.easyshopper.db.helpers.EasyShopperSqliteOpenHelper.Tables;
import com.google.code.easyshopper.db.helpers.LookUpObject;
import com.google.code.easyshopper.domain.Product;
import com.google.code.easyshopper.utility.CollectionUtils;
import com.google.code.easyshopper.utility.CollectionUtils.ValueExtractor;

public class ProductDBAdapter extends AbstractDBAdapter {

	public ProductDBAdapter(SQLiteOpenHelper sqLiteOpenHelper) {
		super(sqLiteOpenHelper);
	}

	public ProductDBAdapter(SQLiteDatabase database) {
		super(database);
	}

	public enum Columns {
		barcode(new Column("barcode", ColumnType.TEXT, Tables.Products)),
		barcodePriceChars(new Column("barcode_price_chars", ColumnType.INTEGER, Tables.Products)),
		name(new Column("prod_name", ColumnType.TEXT, Tables.Products));

		private final Column column;

		private Columns(Column column) {
			this.column = column;
		}

		public Column column() {
			return column;
		}
	}

	public Product lookup(String barcode) {
		SQLiteDatabase readableDatabase = readableDatabase();
		Product product = new LookUpObject<Product>(readableDatabase, new ProductCreator(), columns(), Columns.barcode.column(), table()).lookUp(barcode);
		closeDatabaseIfSafe(readableDatabase);
		return product;
	}

	public void save(String barcode, String productName, int numberOfPriceChars) {
		SQLiteDatabase writableDatabase = writableDatabase();
		Product existingProduct = new ProductDBAdapter(writableDatabase).lookup(barcode);
		String query;
		String numberOfPriceCharsAsString = String.valueOf(numberOfPriceChars);
		if (existingProduct != null) {
			query = "UPDATE " + Tables.Products + " set " + Columns.name.column().name() + " = ?, " + Columns.barcodePriceChars.column().name() + "= ? where " + Columns.barcode.column().name() + " = ? ";
			Logger.d(this, "save", "product " + barcode + " already present, updating with query: " + query);
			writableDatabase.execSQL(query, new String[] { productName, numberOfPriceCharsAsString, String.valueOf(existingProduct.getBarcode()) });
		} else {
			query = "INSERT INTO " + Tables.Products + " (" + Columns.barcode.column().name() + ", " + Columns.name.column().name() + ", " + Columns.barcodePriceChars.column().name()
					+ ") values ( ?, ?, ? )";
			Logger.d(this, "save", "product " + barcode + " not yet present, saving with query: " + query);
			writableDatabase.execSQL(query, new String[] { barcode, productName, numberOfPriceCharsAsString });
		}
		closeDatabaseIfSafe(writableDatabase);
	}

	@Override
	protected List<Column> columns() {
		return CollectionUtils.sublist(Columns.values(), new ValueExtractor<Column, Columns>() {
			public Column extract(Columns object) {
				return object.column();
			}
		});
	}

	@Override
	protected Tables table() {
		return Tables.Products;
	}
}
