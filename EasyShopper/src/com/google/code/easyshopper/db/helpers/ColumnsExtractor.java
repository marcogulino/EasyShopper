package com.google.code.easyshopper.db.helpers;

import com.google.code.easyshopper.utility.CollectionUtils.ValueExtractor;

public class ColumnsExtractor {

	public static ValueExtractor<String, QueryColumn> fullNames() {
		return new ValueExtractor<String, QueryColumn>() {

			public String extract(QueryColumn column) {
				return column.fullName();
			}
		};
	}
	
	public static ValueExtractor<String, QueryColumn> names() {
		return new ValueExtractor<String, QueryColumn>() {
			
			public String extract(QueryColumn column) {
				return column.name();
			}
		};
	}
}
