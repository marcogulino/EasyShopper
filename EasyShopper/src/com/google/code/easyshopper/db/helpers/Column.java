package com.google.code.easyshopper.db.helpers;


public class Column implements QueryColumn {

	private final String columnName;
	private final Table table;
	private final ColumnType columnType;
	private final Column foreignKeyReference;
	private final boolean isPrimaryKey;
	private final boolean isAutoIncrement;
	
	public enum ColumnType {
		INTEGER, TEXT
		
		
	}

	public Column(String columnName, ColumnType columnType, Table tablename) {
		this(columnName, columnType, tablename, false, false);
	}
	
	public Column(String columnName, ColumnType columnType, Table tablename, boolean isPrimaryKey, boolean isAutoIncrement) {
		this(columnName, columnType, tablename, isPrimaryKey, isAutoIncrement, null);
	}
	
	public Column(Table tableName, Column foreignKeyReference) {
		this(foreignKeyReference.table + "_" + foreignKeyReference.columnName, foreignKeyReference.columnType, tableName, false, false, foreignKeyReference);
	}

	private Column(String columnName, ColumnType columnType, Table tablename, boolean isPrimaryKey, boolean isAutoIncrement, Column foreignKeyReference) {
		this.columnName = columnName;
		this.columnType = columnType;
		this.table = tablename;
		this.isPrimaryKey = isPrimaryKey;
		this.isAutoIncrement = isAutoIncrement;
		this.foreignKeyReference = foreignKeyReference;
	}

	public String name() {
		return columnName;
	}
	
	public String fullName() {
		return table.table() + "." + columnName;
	}
	
	public String createSQL() {
		String columnDescription = columnName + " " + columnType.name();
		if(isPrimaryKey) {
			columnDescription+=" PRIMARY KEY";
		}
		if(isAutoIncrement) {
			columnDescription+=" AUTOINCREMENT";
		}
		return columnDescription;
	}
	
	public Column foreignKeyReference() {
		return foreignKeyReference;
	}
	
	public String foreignKeyReferenceString() {
		if(foreignKeyReference==null) {
			return "";
		}
		return "FOREIGN KEY (" + name() + ") REFERENCES " +
			foreignKeyReference.table.table() + "(" + foreignKeyReference.name() + ")";
	}
	
	@Override
	public String toString() {
		return this.getClass().getName() + ": {table " + table.table() + ", column: " + columnName + ", type: " + columnType.name() + "}";
	}

}
