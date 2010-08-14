package com.google.code.easyshopper.db.helpers;

import java.util.Arrays;
import java.util.List;

import com.google.code.easyshopper.utility.CollectionUtils;
import com.google.code.easyshopper.utility.StringUtils;

public class PrimaryKeyConstraint implements Constraint {

	private List<QueryColumn> columns;

	public PrimaryKeyConstraint(QueryColumn ... columns) {
		this.columns=Arrays.asList(columns);
	}

	public String sqlConstraint() {
		StringBuilder sqlConstraint=new StringBuilder();
		sqlConstraint.append("PRIMARY KEY (");
		List<String> columnNames = CollectionUtils.sublist(columns, ColumnsExtractor.names());
		sqlConstraint.append(StringUtils.join(columnNames , ", "));
		sqlConstraint.append(") ");
		return sqlConstraint.toString();
	}

}
