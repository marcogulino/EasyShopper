package com.google.code.easyshopper.db.helpers;


public class Where implements WhereConditionBuilder {

	private final QueryColumn column;
	private final Object value;

	public Where(QueryColumn column, Object value) {
		this.column = column;
		this.value = value;
	}

	public String selection() {
		return column.fullName() + " = ?";
	}

	public String[] values() {
		return new String[] { String.valueOf(value) };
	}

}
