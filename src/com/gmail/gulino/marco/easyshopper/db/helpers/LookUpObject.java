package com.gmail.gulino.marco.easyshopper.db.helpers;

import java.util.List;

import com.gmail.gulino.marco.easyshopper.db.domaincreators.DomainObjectCreator;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class LookUpObject<T> {

	private final SQLiteDatabase database;
	private final DomainObjectCreator<T> objectCreator;
	private final List<Column> columns;
	private final Column primaryKeyColumn;
	private final Table table;

	public LookUpObject(SQLiteDatabase database, DomainObjectCreator<T> objectCreator, List<Column> columns, Column primaryKeyColumn, Table table) {
		this.database = database;
		this.objectCreator = objectCreator;
		this.columns = columns;
		this.primaryKeyColumn = primaryKeyColumn;
		this.table = table;
	}

	public T lookUp(Object id) {
		WhereConditionBuilder where = new Where(primaryKeyColumn, id);
		Cursor query = new Query(database).query(table, columns, where, null, null);
		if (!query.moveToFirst()) {
			query.close();
			return null;
		}
		T create = objectCreator.create(query);
		query.close();
		return create;		
	}
	
	

}
