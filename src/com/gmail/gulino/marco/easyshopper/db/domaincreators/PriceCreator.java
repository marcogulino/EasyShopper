package com.gmail.gulino.marco.easyshopper.db.domaincreators;

import java.util.Currency;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gmail.gulino.marco.easyshopper.db.MarketDBAdapter;
import com.gmail.gulino.marco.easyshopper.db.ProductDBAdapter;
import com.gmail.gulino.marco.easyshopper.db.PriceDBAdapter.Columns;
import com.gmail.gulino.marco.easyshopper.db.helpers.MyCursor;
import com.gmail.gulino.marco.easyshopper.domain.Price;
import com.gmail.gulino.marco.easyshopper.domain.PriceType;

public class PriceCreator implements DomainObjectCreator<Price> {
	
	private final SQLiteDatabase database;

	public PriceCreator(SQLiteDatabase database) {
		this.database = database;
	}

	public Price create(Cursor cursor) {
		MyCursor myCursor = new MyCursor(cursor);
		Price price=new Price(myCursor.getLong(Columns.id.column()));
		price.getAmount().setAmount(myCursor.getLong(Columns.amount.column()));
		price.getAmount().setCurrency(Currency.getInstance(myCursor.getString(Columns.currency.column())));
		price.setPriceType(PriceType.from(myCursor.getInt(Columns.pricetype.column())));
		price.setProduct(new ProductDBAdapter(database).lookup(myCursor.getString(Columns.product.column())));
		price.setMarket(new MarketDBAdapter(database).lookup(myCursor.getInt(Columns.market.column())));
		return price;
	}

}
