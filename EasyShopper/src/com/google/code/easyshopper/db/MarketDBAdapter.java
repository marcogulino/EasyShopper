package com.google.code.easyshopper.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.code.easyshopper.db.domaincreators.MarketCreator;
import com.google.code.easyshopper.db.helpers.Column;
import com.google.code.easyshopper.db.helpers.EasyShopperSqliteOpenHelper;
import com.google.code.easyshopper.db.helpers.LookUpObject;
import com.google.code.easyshopper.db.helpers.Query;
import com.google.code.easyshopper.db.helpers.Column.ColumnType;
import com.google.code.easyshopper.db.helpers.EasyShopperSqliteOpenHelper.Tables;
import com.google.code.easyshopper.domain.Market;
import com.google.code.easyshopper.utility.CollectionUtils;
import com.google.code.easyshopper.utility.CollectionUtils.ValueExtractor;

public class MarketDBAdapter extends AbstractDBAdapter {

	public static final long SAVE_NEW = -1;

	public enum Columns {
		id(new Column("id", ColumnType.INTEGER, EasyShopperSqliteOpenHelper.Tables.Markets, true, true)),
		name(new Column("name", ColumnType.TEXT, EasyShopperSqliteOpenHelper.Tables.Markets)),
		geo_long(new Column("longitude", ColumnType.INTEGER, EasyShopperSqliteOpenHelper.Tables.Markets)),
		geo_lat(new Column("latitude", ColumnType.INTEGER, EasyShopperSqliteOpenHelper.Tables.Markets));

		private final Column column;

		private Columns(Column column) {
			this.column = column;
		}

		public Column column() {
			return column;
		}
	}

	public MarketDBAdapter(SQLiteOpenHelper sqLiteOpenHelper) {
		super(sqLiteOpenHelper);
	}

	public MarketDBAdapter(SQLiteDatabase database) {
		super(database);
	}

	public void saveMarket(Market market) {
		SQLiteDatabase writableDatabase = writableDatabase();
		boolean isExiting = new MarketDBAdapter(writableDatabase).lookup(market.getId()) != null;
		if (isExiting) {
			Object[] bindArgs = new Object[] { market.getName(), market.getLatitude(), market.getLongitude(),
					market.getId() };
			String updateQuery = "UPDATE " + Tables.Markets + " SET " + Columns.name.column().name() + "=?, "
					+ Columns.geo_lat.column().name() + "=?" + Columns.geo_long.column().name() + "=? WHERE ID=?";
			writableDatabase.execSQL(updateQuery, bindArgs);
		} else {
			ContentValues values = new ContentValues();
			values.put(Columns.name.column().name(), market.getName());
			values.put(Columns.geo_lat.column().name(), market.getLatitude());
			values.put(Columns.geo_long.column().name(), market.getLongitude());
			long insertId = writableDatabase.insert(Tables.Markets.toString(), null, values);
			market.setId(insertId);
		}
		closeDatabaseIfSafe(writableDatabase);
	}

	public List<Market> selectAll() {
		List<Market> markets = new ArrayList<Market>();
		SQLiteDatabase readableDatabase = readableDatabase();
		Cursor query=new Query(readableDatabase).query(table(), columns(), null, null, null);
		
		while (query.moveToNext()) {
			markets.add(new MarketCreator().create(query));
		}
		query.close();
		closeDatabaseIfSafe(readableDatabase);
		return markets;

	}

	public Market lookup(long id) {
		SQLiteDatabase database = readableDatabase();
		Market market = new LookUpObject<Market>(database, new MarketCreator(), columns(), Columns.id.column(), table()).lookUp(id);
		closeDatabaseIfSafe(database);
		return market;
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
		return Tables.Markets;
	}

}
