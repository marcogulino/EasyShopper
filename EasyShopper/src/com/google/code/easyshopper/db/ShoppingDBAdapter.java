package com.google.code.easyshopper.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.code.easyshopper.db.domaincreators.ShoppingCreator;
import com.google.code.easyshopper.db.helpers.Column;
import com.google.code.easyshopper.db.helpers.EasyShopperSqliteOpenHelper;
import com.google.code.easyshopper.db.helpers.LookUpObject;
import com.google.code.easyshopper.db.helpers.Orderby;
import com.google.code.easyshopper.db.helpers.Query;
import com.google.code.easyshopper.db.helpers.Column.ColumnType;
import com.google.code.easyshopper.db.helpers.EasyShopperSqliteOpenHelper.Tables;
import com.google.code.easyshopper.domain.Market;
import com.google.code.easyshopper.domain.Shopping;
import com.google.code.easyshopper.utility.CollectionUtils;
import com.google.code.easyshopper.utility.CollectionUtils.ValueExtractor;

public class ShoppingDBAdapter extends AbstractDBAdapter {
	public enum Columns{
		id(new Column("id", ColumnType.INTEGER, Tables.Shoppings, true, true)),
		market(new Column(Tables.Shoppings, MarketDBAdapter.Columns.id.column())),
		shoppingDate(new Column("shopping_date", ColumnType.INTEGER, Tables.Shoppings));
		
		private final Column column;
		private Columns(Column column) {
			this.column = column;
		}
		
		
		public Column column() {
			return column;
		}
		
	}
	
	public ShoppingDBAdapter(SQLiteOpenHelper sqLiteOpenHelper) {
		super(sqLiteOpenHelper);
	}
	
	public ShoppingDBAdapter(SQLiteDatabase database) {
		super(database);
	}
	
	public List<Shopping> allShoppings() {
		List<Shopping> shoppings = new ArrayList<Shopping>();
		SQLiteDatabase database = readableDatabase();
		Cursor query = new Query(database).query(table(), columns(), null, null, Arrays.asList(Orderby.desc(Columns.shoppingDate.column())));
		ShoppingCreator shoppingCreator = new ShoppingCreator(database);
		while(query.moveToNext()) {
			shoppings.add( shoppingCreator.create(query));
		}
		query.close();
		closeDatabaseIfSafe(database);
		return shoppings;
	}
	
	public Shopping createNew() {
		SQLiteDatabase writableDatabase = writableDatabase();
		ContentValues values = new ContentValues();
		Calendar date = Calendar.getInstance();
		values.put(Columns.shoppingDate.column().name(), date.getTimeInMillis());
		long id=writableDatabase.insertOrThrow(Tables.Shoppings.toString(), null, values ); 
		closeDatabaseIfSafe(writableDatabase);
		return new Shopping(id, date);
	}


	public Shopping lookUp(long shoppingId) {
		SQLiteDatabase database = readableDatabase();
		Shopping shopping = new LookUpObject<Shopping>(database, new ShoppingCreator(database), columns(), Columns.id.column(), table()).lookUp(shoppingId);
		closeDatabaseIfSafe(database);
		return shopping;
	}
	
	public void delete(Shopping shopping) {
		SQLiteDatabase writableDatabase = writableDatabase();
		writableDatabase.execSQL("DELETE FROM " + EasyShopperSqliteOpenHelper.Tables.ProductsShoppings + " WHERE " + ProductShoppingDBAdapter.Columns.shopping.column().name() + " =?", new Object[] {shopping.getId()} );
		writableDatabase.execSQL("DELETE FROM " + Tables.Shoppings + " WHERE " + Columns.id.column().name() + " =?", new Object[] {shopping.getId()} );
		closeDatabaseIfSafe(writableDatabase);
	}


	public void associateTo(Market selectedMarket, Shopping shopping) {
		String query="UPDATE " + Tables.Shoppings + " SET " + Columns.market.column().name() + "= ? WHERE " + Columns.id.column().name() + "=?";
		SQLiteDatabase writableDatabase = writableDatabase();
		writableDatabase.execSQL(query, new Object[] {selectedMarket.getId(), shopping.getId()});
		closeDatabaseIfSafe(writableDatabase);
	}

	@Override
	protected List<Column> columns() {
		return CollectionUtils.sublist(Columns.values(), new ValueExtractor<Column, Columns> () {

			public Column extract(Columns object) {
				return object.column();
			}
			
		} );
	}

	@Override
	protected Tables table() {
		return Tables.Shoppings;
	}
}
