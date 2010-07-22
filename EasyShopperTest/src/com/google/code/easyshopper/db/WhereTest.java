package com.google.code.easyshopper.db;


import android.test.AndroidTestCase;

import com.google.code.easyshopper.db.helpers.Column;
import com.google.code.easyshopper.db.helpers.Where;
import com.google.code.easyshopper.db.helpers.Column.ColumnType;
import com.google.code.easyshopper.db.helpers.EasyShopperSqliteOpenHelper.Tables;

public class WhereTest extends AndroidTestCase {

	public void testWhereConditionBuilding() throws Exception {
		Where where = new Where(new Column("aColumnName", ColumnType.TEXT, Tables.Markets), "a value");
		assertEquals("markets.aColumnName = ?", where.selection());
		assertEquals("a value", where.values()[0]);
	}
}
