package com.google.code.easyshopper.db.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.code.easyshopper.db.MarketDBAdapter;
import com.google.code.easyshopper.db.PriceDBAdapter;
import com.google.code.easyshopper.db.ProductDBAdapter;
import com.google.code.easyshopper.db.ProductShoppingDBAdapter;
import com.google.code.easyshopper.db.ShoppingDBAdapter;

public class EasyShopperSqliteOpenHelper extends SQLiteOpenHelper {

	public static final int PRODUCT_DB_VERSION = 1;
	public static final String DATABASE_NAME = "easyshopper";
	
	public enum Tables implements Table {
		Products("products"),
		Prices("prices"),
		Markets("markets"),
		Shoppings("shoppings"),
		ProductsShoppings("products_shoppings");
		
		private String tablename;
		private Tables(String tablename) {
			this.tablename=tablename;
		}
		
		public String table() {
			return tablename;
		}
		@Override
		public String toString() {
			return table();
		}
	}

	public EasyShopperSqliteOpenHelper(Context context) {
		super(context, EasyShopperSqliteOpenHelper.DATABASE_NAME, null,
				EasyShopperSqliteOpenHelper.PRODUCT_DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		new ProductDBAdapter(db).create();
		new MarketDBAdapter(db).create();
		new ShoppingDBAdapter(db).create();
		new PriceDBAdapter(db).create();
		new ProductShoppingDBAdapter(db).create();
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
