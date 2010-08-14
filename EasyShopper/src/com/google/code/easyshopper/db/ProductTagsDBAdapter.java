package com.google.code.easyshopper.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.code.easyshopper.db.helpers.Column;
import com.google.code.easyshopper.db.helpers.Column.ColumnType;
import com.google.code.easyshopper.db.helpers.Constraint;
import com.google.code.easyshopper.db.helpers.EasyShopperSqliteOpenHelper.Tables;
import com.google.code.easyshopper.db.helpers.MyCursor;
import com.google.code.easyshopper.db.helpers.PrimaryKeyConstraint;
import com.google.code.easyshopper.db.helpers.Query;
import com.google.code.easyshopper.db.helpers.Where;
import com.google.code.easyshopper.domain.CartProduct;
import com.google.code.easyshopper.utility.CollectionUtils;

public class ProductTagsDBAdapter extends AbstractDBAdapter {
	
	public enum Columns {
		tag(new Column("tag", ColumnType.TEXT, Tables.ProductTags)),
		product( new Column(Tables.ProductTags, ProductDBAdapter.Columns.barcode.column()));
		
		private final Column column;
		
		private Columns(Column column) {
			this.column = column;
		}
		
		public Column column() {
			return column;
		}
		
	}
	

	public ProductTagsDBAdapter(SQLiteDatabase database) {
		super(database);
	}
	
	public ProductTagsDBAdapter(SQLiteOpenHelper sqLiteOpenHelper) {
		super(sqLiteOpenHelper);
	}
	
	public List<String> tagsFor(CartProduct product) {
		List<String> tags = new ArrayList<String>();
		SQLiteDatabase readableDatabase = readableDatabase();
		Query query = new Query(readableDatabase);
		MyCursor cursor = new MyCursor(query.query(table(), Arrays.asList(Columns.tag.column()), new Where(Columns.product.column(), product.getBarcodeForProduct()), null, null));
		while(cursor.moveToNext()) {
			tags.add(cursor.getString(Columns.tag.column()));
		}
		cursor.close();
		closeDatabaseIfSafe(readableDatabase);
		return tags;
	}
	
	public long addTag(String tag, CartProduct product) {
		SQLiteDatabase writableDatabase = writableDatabase();
		ContentValues values = new ContentValues();
		values.put(Columns.product.column().name(), product.getBarcodeForProduct());
		values.put(Columns.tag.column().name(),tag);
		long insert = writableDatabase.insert(table().table(), null, values);
		closeDatabaseIfSafe(writableDatabase);
		return insert;
	}

	@Override
	protected List<Column> columns() {
		return CollectionUtils.sublist(Columns.values(), new CollectionUtils.ValueExtractor<Column, Columns>() {
			public Column extract(Columns object) {
				return object.column();
			}
		});
	}

	@Override
	protected Tables table() {
		return Tables.ProductTags;
	}
	


	@Override
	protected List<Constraint> sqlConstraints() {
		List<Constraint> constraints = new ArrayList<Constraint>();
		constraints.add(new PrimaryKeyConstraint(Columns.tag.column(), Columns.product.column()));
		return constraints;
	}
}
