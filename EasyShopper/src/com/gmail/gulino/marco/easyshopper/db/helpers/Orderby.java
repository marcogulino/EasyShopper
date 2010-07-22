package com.gmail.gulino.marco.easyshopper.db.helpers;

import com.gmail.gulino.marco.easyshopper.utility.CollectionUtils.ValueExtractor;

public class Orderby {
	
	public static class OrderByExtractor implements ValueExtractor<String, Orderby> {

		public String extract(Orderby object) {
			return object.toQuery();
		}

	}
	private final OrderByType orderType;
	private final Column column;
	private enum OrderByType {
		DESC("DESC"), ASC("ASC");
		private String type;
		private OrderByType(String type) {
			this.type=type;
		}
	}

	public Orderby(OrderByType orderType, Column column) {
		this.orderType = orderType;
		this.column = column;
	}
	
	public String toQuery() {
		return column.fullName() + " " + orderType.type;
	}
	public static Orderby desc(Column column) {
		return new Orderby(OrderByType.DESC, column);
	}
	public static Orderby asc(Column column) {
		return new Orderby(OrderByType.ASC, column);
	}

}
