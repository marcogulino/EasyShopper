package com.gmail.gulino.marco.easyshopper.db.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class WhereGroup implements WhereConditionBuilder{

	private class Entry {
		public WhereConditionBuilder where;
		private String operator;
		public Entry(WhereConditionBuilder where, String operator) {
			this.where=where;
			this.operator=operator;
		}
		public String operator() {
			return " " + operator + " ";
		}
	}
	
	private final WhereConditionBuilder firstCondition;
	private final List<Entry> otherConditions;

	public WhereGroup(WhereConditionBuilder firstCondition) {
		this.firstCondition = firstCondition;
		otherConditions=new ArrayList<Entry>();
	}

	public WhereGroup and(WhereConditionBuilder where) {
		otherConditions.add(new Entry(where, "AND") );
		return this;
	}
	
	public WhereGroup or(WhereConditionBuilder where) {
		otherConditions.add(new Entry(where, "OR"));
		return this;
	}

	public String selection() {
		StringBuilder selection = new StringBuilder("( ").append(firstCondition.selection());
		for (Entry entry : otherConditions) {
			selection.append(entry.operator() ).append(entry.where.selection());
		}
		selection.append(" )" );
		return selection.toString();
	}

	public String[] values() {
		List<String> values=new ArrayList<String>();
		values.addAll(Arrays.asList(firstCondition.values()));

		for (Entry entry : otherConditions) {
			values.addAll(Arrays.asList(entry.where.values()));
		}
		
		return values.toArray(new String[] {} );
	}

}
