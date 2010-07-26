package com.google.code.easyshopper.db.domaincreators;

import java.util.Calendar;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.code.easyshopper.db.MarketDBAdapter;
import com.google.code.easyshopper.db.ShoppingDBAdapter.Columns;
import com.google.code.easyshopper.db.helpers.MyCursor;
import com.google.code.easyshopper.domain.Market;
import com.google.code.easyshopper.domain.Shopping;

public class ShoppingCreator implements DomainObjectCreator<Shopping> {
	
	private final SQLiteDatabase database;

	public ShoppingCreator(SQLiteDatabase database) {
		this.database = database;
		
	}

	public Shopping create(Cursor cursor) {
		MyCursor query=new MyCursor(cursor);
		Calendar date = Calendar.getInstance();
		
		Shopping shopping = new Shopping(query.getLong(Columns.id.column()));
		date.setTimeInMillis(query.getLong(Columns.shoppingDate.column()));
		shopping.setDate(date);
		long marketId = query.getLong(Columns.market.column());
		Market market = new MarketDBAdapter(database).lookup(marketId);
		shopping.setMarket(market);
		return shopping;
	}

}
