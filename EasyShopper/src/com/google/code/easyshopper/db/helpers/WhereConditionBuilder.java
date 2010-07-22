package com.google.code.easyshopper.db.helpers;

public interface WhereConditionBuilder {

	String selection();

	String[] values();

}
