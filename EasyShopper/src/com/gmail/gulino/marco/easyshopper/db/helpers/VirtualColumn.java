package com.gmail.gulino.marco.easyshopper.db.helpers;


public class VirtualColumn implements QueryColumn {
	private final String queryColumnText;

	public VirtualColumn(String queryColumnText) {
		this.queryColumnText = queryColumnText;
	}

	public String fullName() {
		return queryColumnText;
	}

	public String name() {
		return queryColumnText;
	}
	
	@Override
	public String toString() {
		return this.getClass().getName() + ": {" + queryColumnText + "}";
	}

}
