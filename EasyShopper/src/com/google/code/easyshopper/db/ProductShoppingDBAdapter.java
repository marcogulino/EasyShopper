package com.google.code.easyshopper.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.code.easyshopper.ES;
import com.google.code.easyshopper.db.domaincreators.ProductCreator;
import com.google.code.easyshopper.db.helpers.Column;
import com.google.code.easyshopper.db.helpers.Join;
import com.google.code.easyshopper.db.helpers.MyCursor;
import com.google.code.easyshopper.db.helpers.Query;
import com.google.code.easyshopper.db.helpers.QueryColumn;
import com.google.code.easyshopper.db.helpers.VirtualColumn;
import com.google.code.easyshopper.db.helpers.Where;
import com.google.code.easyshopper.db.helpers.WhereConditionBuilder;
import com.google.code.easyshopper.db.helpers.WhereGroup;
import com.google.code.easyshopper.db.helpers.Column.ColumnType;
import com.google.code.easyshopper.db.helpers.EasyShopperSqliteOpenHelper.Tables;
import com.google.code.easyshopper.domain.CartProduct;
import com.google.code.easyshopper.domain.Price;
import com.google.code.easyshopper.domain.Product;
import com.google.code.easyshopper.domain.Shopping;
import com.google.code.easyshopper.utility.CollectionUtils;
import com.google.code.easyshopper.utility.IncrementalList;
import com.google.code.easyshopper.utility.CollectionUtils.ValueExtractor;

public class ProductShoppingDBAdapter extends AbstractDBAdapter {
	public enum Columns{
		id(new Column("id", ColumnType.INTEGER, Tables.ProductsShoppings, true, true)),
		shopping(new Column(Tables.ProductsShoppings, ShoppingDBAdapter.Columns.id.column())),
		product(new Column(Tables.ProductsShoppings, ProductDBAdapter.Columns.id.column())),
		actualBarcode(new Column("actual_barcode", ColumnType.TEXT, Tables.ProductsShoppings) );
		
		
		private final Column column;
		private Columns(Column column) {
			this.column = column;
		}
		
		@Override
		public String toString() {
			return column.name();
		}
		
		public Column column() {
			return column;
		}
		
	}

	public ProductShoppingDBAdapter(SQLiteOpenHelper sqLiteOpenHelper) {
		super(sqLiteOpenHelper); 
	}
	
	public ProductShoppingDBAdapter(SQLiteDatabase database) {
		super(database);
	}
	

	public void insertNewAssociation(String barcode, Shopping shopping) {
		SQLiteDatabase writableDatabase = writableDatabase();
		ContentValues values = new ContentValues();
		Product product = new ProductDBAdapter(writableDatabase).lookup(barcode);
		values.put(Columns.product.column().name(), product.getId());
		values.put(Columns.actualBarcode.column().name(), barcode);
		values.put(Columns.shopping.column().name(), shopping.getId());
		writableDatabase.insert(Tables.ProductsShoppings.toString(), null, values);
		closeDatabaseIfSafe(writableDatabase);
	}

	public List<CartProduct> allProductsFor(Context context, Shopping shopping) {
		List<CartProduct> products = new ArrayList<CartProduct>();
		SQLiteDatabase database = readableDatabase();
		Join join = Join.inner(Tables.Products, Tables.ProductsShoppings, ProductShoppingDBAdapter.Columns.product.column() );
		List<QueryColumn> queryColumns=Arrays.asList(Columns.actualBarcode.column(), ProductDBAdapter.Columns.id.column(), ProductDBAdapter.Columns.barcode.column(), ProductDBAdapter.Columns.name.column(), new VirtualColumn("count(*) as cnt"));
		WhereConditionBuilder where = new Where(ProductShoppingDBAdapter.Columns.shopping.column(), shopping.getId());
		List<QueryColumn> groupBy = new IncrementalList<QueryColumn>().put(ProductDBAdapter.Columns.id.column());
		MyCursor query = new MyCursor(new Query(database).query(join, queryColumns, where, groupBy, null ));
		
		while (query.moveToNext()) {
			Product product = new ProductCreator().create(query);
			Price price =null;
			if (shopping.getMarket() != null) {
				price = new PriceDBAdapter(database).priceFor(product.getBarcode(), shopping.getMarket());
			}

			products.add(new CartProduct(product, shopping, query.getLong(new VirtualColumn("cnt")), price));
		}
		query.close();
		closeDatabaseIfSafe(database);
		return products;
	}

	public void deleteAll(CartProduct cartProduct) {
		String query = "DELETE FROM " + Tables.ProductsShoppings + " WHERE " + Columns.product.column().name() + "=? AND " + Columns.shopping.column().name() + "=?";
		SQLiteDatabase writableDatabase = writableDatabase();
		writableDatabase.execSQL(query, new Object[] { cartProduct.getProduct().getBarcode(),
				cartProduct.getShopping().getId() });
		closeDatabaseIfSafe(writableDatabase);
	}

	public void decreaseQuantity(CartProduct cartProduct) {
		String query = "DELETE FROM " + Tables.ProductsShoppings + " WHERE " + Columns.product.column().name() + "=? AND " + Columns.shopping.column().name() + "=? AND "
				+ Columns.id.column().name() + "= (" + "SELECT " + Columns.id.column().name() + " FROM " + Tables.ProductsShoppings + " WHERE " + Columns.product.column().name() + "='"
				+ cartProduct.getProduct().getBarcode() + "' AND " + Columns.shopping.column().name() + "="
				+ cartProduct.getShopping().getId() + " LIMIT 1" + ")";
		Log.d(ES.APP_NAME, getClass().getName() + ": decreaseQuantity: exec query=" + query);
		SQLiteDatabase writableDatabase = writableDatabase();
		writableDatabase.execSQL(query, new Object[] { cartProduct.getProduct().getBarcode(),
				cartProduct.getShopping().getId() });
		closeDatabaseIfSafe(writableDatabase);
	}

	public int countProductForShopping(Shopping shopping, Product product) {
		SQLiteDatabase database = readableDatabase();
		VirtualColumn countColumn = new VirtualColumn("COUNT (*) as cnt");
		WhereGroup whereGroup = new WhereGroup(new Where(Columns.shopping.column(), shopping.getId()))
		.and(new Where(Columns.product.column(), product.getId()));
		
		Cursor query = new Query(database).query(table(), Arrays.asList(countColumn), whereGroup, null, null);
		query.moveToFirst();
		int count = query.getInt(0);
		query.close();
		closeDatabaseIfSafe(database);
		return count;
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
		return Tables.ProductsShoppings;
	}
}
