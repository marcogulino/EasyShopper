package com.gmail.gulino.marco.easyshopper.db.helpers;


public class Join implements Table {
	private final Table firstTable;
	private final Table secondTable;
	private final JoinType joinType;
	private final Column firstColumn;
	private final Column secondColumn;


	private enum JoinType {
		INNER("INNER JOIN"),
		LEFT("LEFT JOIN"),
		RIGHT("RIGHT JOIN");
		
		private String type;

		private JoinType(String type) {
			this.type=type;
		}
		
		public String type() {
			return type;
		}
	}

	private Join(Table firstTable, Table secondTable, JoinType joinType, Column firstColumn, Column secondColumn) {
		this.firstTable = firstTable;
		this.secondTable = secondTable;
		this.joinType = joinType;
		this.firstColumn = firstColumn;
		this.secondColumn = secondColumn;
	}


	public String table() {
		return firstTable.table() + " " + joinType.type() + " " + secondTable.table() + " ON ( " + firstColumn.fullName() + " = " + secondColumn.fullName() + " )";
	}
	

	public static Join inner(Table firstTable, Table secondTable, Column column) {
		return new Join(firstTable, secondTable, JoinType.INNER, column, column.foreignKeyReference() );
	}


}
