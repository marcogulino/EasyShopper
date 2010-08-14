package com.google.code.easyshopper.db;

import java.util.ArrayList;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.code.easyshopper.Logger;
import com.google.code.easyshopper.db.helpers.Column;
import com.google.code.easyshopper.db.helpers.Constraint;
import com.google.code.easyshopper.db.helpers.ConstraintExtractor;
import com.google.code.easyshopper.db.helpers.EasyShopperSqliteOpenHelper.Tables;
import com.google.code.easyshopper.utility.CollectionUtils;
import com.google.code.easyshopper.utility.StringUtils;


public abstract class AbstractDBAdapter {

	private final SQLiteOpenHelper sqLiteOpenHelper;
	private final SQLiteDatabase database;

	public AbstractDBAdapter(SQLiteOpenHelper sqLiteOpenHelper) {
		this.sqLiteOpenHelper = sqLiteOpenHelper;
		this.database=null;
	}
	
	public AbstractDBAdapter(SQLiteDatabase database) {
		this.database = database;
		this.sqLiteOpenHelper=null;
	}
	
	public void create() {
		String query="CREATE TABLE " + table() + " (";
		List<String> declarations = new ArrayList<String>();
		List<String> foreignKeys = new ArrayList<String>();
		for (Column column : columns()) {
			declarations.add(column.createSQL());
			foreignKeys.add(column.foreignKeyReferenceString());
		}
		declarations.addAll(CollectionUtils.sublist(sqlConstraints(), new ConstraintExtractor()));
		declarations.addAll(foreignKeys);
		query+= StringUtils.join(declarations, ", ");
		query+=");";
		Logger.d(this, "create", "*** creating table:" + query);
		database.execSQL(query);
	}

	protected abstract List<Constraint> sqlConstraints();

	protected SQLiteDatabase readableDatabase() {
		if(database!=null) {
			return database;
		}
		return sqLiteOpenHelper.getReadableDatabase();
	}
	
	protected SQLiteDatabase writableDatabase() {
		if(database!=null) {
			return database;
		}
		return sqLiteOpenHelper.getWritableDatabase();
	}
	
	
	protected String[] columnNames() {
		List<String> columns = new ArrayList<String>();
		for (Column column : columns() ) {
			columns.add(column.name());
		}
		return columns.toArray(new String[] {});
	}

	protected abstract List<Column> columns();
	protected abstract Tables table();

	protected void closeDatabaseIfSafe(SQLiteDatabase database) {
		if(sqLiteOpenHelper!= null) {
			database.close();
		}
	}


}
