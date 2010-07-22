package com.gmail.gulino.marco.easyshopper.db.helpers;

import com.gmail.gulino.marco.easyshopper.utility.CollectionUtils.ValueExtractor;

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
