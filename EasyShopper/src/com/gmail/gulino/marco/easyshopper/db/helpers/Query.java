package com.gmail.gulino.marco.easyshopper.db.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gmail.gulino.marco.easyshopper.Logger;
import com.gmail.gulino.marco.easyshopper.db.helpers.Orderby.OrderByExtractor;
import com.gmail.gulino.marco.easyshopper.utility.CollectionUtils;
import com.gmail.gulino.marco.easyshopper.utility.StringUtils;
import com.gmail.gulino.marco.easyshopper.utility.CollectionUtils.ValueExtractor;

public class Query {

	private final SQLiteDatabase database;

	public Query(SQLiteDatabase database) {
		this.database = database;
	}

	public Cursor query(Table table, List<? extends QueryColumn> queryColumns, WhereConditionBuilder where,
			List<QueryColumn> groupBy, List<Orderby> orderBy) {
		return query(Arrays.asList(table), queryColumns, where, groupBy, null);
	}

	public Cursor query(List<Table> tables, List<? extends QueryColumn> queryColumns, WhereConditionBuilder where,
			List<QueryColumn> groupBy, List<Orderby> orderBy) {
		String qTables = StringUtils.join(CollectionUtils.sublist(tables, new TableNameExtractor()), ", ");
		String[] columns = CollectionUtils.sublist(new ArrayList<QueryColumn>(queryColumns), ColumnsExtractor.fullNames()).toArray(new String[] {});
		String selection = where == null ? null : where.selection();
		String[] selectionArgs = where == null ? null : where.values();
		String groupByColumns = groupBy == null ? null : StringUtils.join(CollectionUtils.sublist(groupBy,
				ColumnsExtractor.fullNames()), ", ");
		String orderByColumns = orderBy == null ? null : StringUtils.join(CollectionUtils.sublist(orderBy, new OrderByExtractor()), ", ");
		Logger.d(this, "query", "tables: " + qTables);
		if(columns != null) Logger.d(this, "query", "columns: " + StringUtils.join(columns, ", "));
		Logger.d(this, "query", "selection: " + selection);
		if(selectionArgs != null) Logger.d(this, "query", "selectionArgs: " + StringUtils.join(selectionArgs, ", ") );
		
		return database.query(qTables, columns, selection, selectionArgs, groupByColumns, null, orderByColumns);
	}

	public class TableNameExtractor implements ValueExtractor<String, Table> {
		public String extract(Table object) {
			return object.table();
		}
	}

}
