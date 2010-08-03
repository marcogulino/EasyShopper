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
		id(new Column("id", ColumnType.INTEGER, Tables.Products, true, true)),
		barcode(new Column("barcode", ColumnType.TEXT, Tables.Products)),
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

	public Product lookup(int id) {
		SQLiteDatabase readableDatabase = readableDatabase();
		Product product = new LookUpObject<Product>(readableDatabase, new ProductCreator(), columns(), Columns.id.column(), table()).lookUp(id);
		closeDatabaseIfSafe(readableDatabase);
		return product;
	}

	public void save(String barcode, String productName) {
		SQLiteDatabase writableDatabase = writableDatabase();
		Product existingProduct = new ProductDBAdapter(writableDatabase).lookup(barcode);
		String query;
		if (existingProduct != null ) {
			query = "UPDATE " + Tables.Products + " set " + Columns.name.column().name() + "= ? where "
					+ Columns.id.column().name() + " = ? ";
			Logger.d(this, "save", "product " + barcode + " already present, updating with query: " + query);
			writableDatabase.execSQL(query, new String[] { productName, String.valueOf(existingProduct.getId()) });
		} else {
			query = "INSERT INTO " + Tables.Products + " (" + Columns.barcode.column().name() + ", "
					+ Columns.name.column().name() + ") values ( ?, ? )";
			Logger.d(this, "save", "product " + barcode + " not yet present, saving with query: " + query);
			writableDatabase.execSQL(query, new String[] { barcode, productName });
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
