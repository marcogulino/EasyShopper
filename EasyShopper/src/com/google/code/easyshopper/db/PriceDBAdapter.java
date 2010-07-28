package com.google.code.easyshopper.db;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.code.easyshopper.Logger;
import com.google.code.easyshopper.db.domaincreators.PriceCreator;
import com.google.code.easyshopper.db.helpers.Column;
import com.google.code.easyshopper.db.helpers.EasyShopperSqliteOpenHelper;
import com.google.code.easyshopper.db.helpers.LookUpObject;
import com.google.code.easyshopper.db.helpers.Query;
import com.google.code.easyshopper.db.helpers.QueryColumn;
import com.google.code.easyshopper.db.helpers.Table;
import com.google.code.easyshopper.db.helpers.VirtualColumn;
import com.google.code.easyshopper.db.helpers.Where;
import com.google.code.easyshopper.db.helpers.WhereConditionBuilder;
import com.google.code.easyshopper.db.helpers.WhereGroup;
import com.google.code.easyshopper.db.helpers.Column.ColumnType;
import com.google.code.easyshopper.db.helpers.EasyShopperSqliteOpenHelper.Tables;
import com.google.code.easyshopper.domain.Amount;
import com.google.code.easyshopper.domain.CartProduct;
import com.google.code.easyshopper.domain.Market;
import com.google.code.easyshopper.domain.Price;
import com.google.code.easyshopper.domain.Shopping;
import com.google.code.easyshopper.utility.CollectionUtils;
import com.google.code.easyshopper.utility.IncrementalList;

public class PriceDBAdapter extends AbstractDBAdapter {

	public enum Columns {
		id(new Column("id", ColumnType.INTEGER, EasyShopperSqliteOpenHelper.Tables.Prices, true, true)),
		amount(new Column("amount", ColumnType.INTEGER, Tables.Prices)),
		currency(new Column("currency", ColumnType.TEXT, Tables.Prices)),
		product( new Column(Tables.Prices, ProductDBAdapter.Columns.id.column())),
		market(new Column(Tables.Prices, MarketDBAdapter.Columns.id.column()));

		private final Column column;

		private Columns(Column column) {
			this.column = column;
		}

		public Column column() {
			return column;
		}

	}

	public PriceDBAdapter(SQLiteOpenHelper sqliteOpenHelper) {
		super(sqliteOpenHelper);
	}

	public PriceDBAdapter(SQLiteDatabase db) {
		super(db);
	}

	@Override
	protected List<Column> columns() {
		return CollectionUtils.sublist(Columns.values(), new CollectionUtils.ValueExtractor<Column, Columns>() {
			public Column extract(Columns object) {
				return object.column();
			}
		});
	}

	// TODO Rewrite
	public void saveAndAssociate(Price price, CartProduct cartProduct) {
		Logger.d(this, "saveAndAssociate", "price: " + price + ", cartProduct: " + cartProduct);
		SQLiteDatabase writableDatabase = writableDatabase();
		Price existingPrice = new PriceDBAdapter(writableDatabase).lookUp(price.getId());
		if (existingPrice != null) {
			Logger.d(this, "save", "updating existing price: " + existingPrice);
			String query = "UPDATE " + table() + " set " + Columns.amount.column().name() + "=?, "
					+ Columns.currency.column().name() + "=?, " + Columns.market.column().name() + "=?, " + Columns.product.column().name()
					+ "=? WHERE " + Columns.id.column().name() + "=?";
			Object[] objects = new Object[] { price.getLongAmount(), price.getCurrency().getCurrencyCode(),
					price.getMarket().getId(), price.getProduct().getBarcode(),
					price.getId() };
			writableDatabase.execSQL(query, objects);
		} else {
			Logger.d(this, "save", "saving new price: " + price);
			ContentValues values = new ContentValues();
			values.put(Columns.amount.column().name(), price.getLongAmount());
			values.put(Columns.currency.column().name(), price.getCurrency().getCurrencyCode());
			values.put(Columns.market.column().name(), price.getMarket().getId());
			values.put(Columns.product.column().name(), price.getProduct().getBarcode());
			long priceId = writableDatabase.insert(table().toString(), null, values);
			Logger.d(this, "save", "got insert id: " + priceId);
			price.setId(priceId);
		}
		closeDatabaseIfSafe(writableDatabase);
	}

	public Price lookUp(long id) {
		SQLiteDatabase database = readableDatabase();
		Price price = new LookUpObject<Price>(database, new PriceCreator(database), columns(), Columns.id.column(), table())
				.lookUp(id);
		closeDatabaseIfSafe(database);
		return price;
	}

	public List<Amount> calculateTotal(Shopping shopping) {
		SQLiteDatabase database = readableDatabase();
		ArrayList<Amount> prices = new ArrayList<Amount>();

		List<Table> tables = new IncrementalList<Table>().put(Tables.Prices).put(Tables.ProductsShoppings);

		Column currency = Columns.currency.column();

		List<QueryColumn> queryColumns = new IncrementalList<QueryColumn>().put(
				new VirtualColumn("SUM(amount) as total_amount")).put(currency);

		String marketId = String.valueOf(shopping.getMarket() != null ? shopping.getMarket().getId() : -1);
		WhereGroup where = new WhereGroup(new Where(ProductShoppingDBAdapter.Columns.shopping.column(), shopping
				.getId())).and(new Where(PriceDBAdapter.Columns.market.column(), marketId));

		List<QueryColumn> groupBy = new IncrementalList<QueryColumn>().put(currency);

		Cursor query = new Query(database).query(tables, queryColumns, where, groupBy, null);
		while (query.moveToNext()) {
			prices.add(new Amount(query.getLong(0), Currency.getInstance(query.getString(1))));
		}
		query.close();
		closeDatabaseIfSafe(database);
		return prices;
	}

	public Price priceFor(String barcode, Market market) {
		SQLiteDatabase database = readableDatabase();
		WhereConditionBuilder where = new WhereGroup(new Where(Columns.market.column(), market.getId())).and(new Where(
				Columns.product.column(), barcode));
		Cursor query = new Query(database).query(table(),columns(), where, null, null);
		Price price;
		if (query.moveToFirst()) {
			price = new PriceCreator(database).create(query);
		} else {
			price = null;
		}
		query.close();
		closeDatabaseIfSafe(database);
		return price;
	}

	protected Tables table() {
		return Tables.Prices;
	}

}
