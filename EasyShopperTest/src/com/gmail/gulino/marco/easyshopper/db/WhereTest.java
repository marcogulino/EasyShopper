package com.gmail.gulino.marco.easyshopper.db;


import com.gmail.gulino.marco.easyshopper.db.helpers.Column;
import com.gmail.gulino.marco.easyshopper.db.helpers.Where;
import com.gmail.gulino.marco.easyshopper.db.helpers.Column.ColumnType;
import com.gmail.gulino.marco.easyshopper.db.helpers.EasyShopperSqliteOpenHelper.Tables;

import android.test.AndroidTestCase;

public class WhereTest extends AndroidTestCase {

	public void testWhereConditionBuilding() throws Exception {
		Where where = new Where(new Column("aColumnName", ColumnType.TEXT, Tables.Markets), "a value");
		assertEquals("markets.aColumnName = ?", where.selection());
		assertEquals("a value", where.values()[0]);
	}
}
