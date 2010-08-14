package com.google.code.easyshopper.db.helpers;

import com.google.code.easyshopper.utility.CollectionUtils.ValueExtractor;

public class ConstraintExtractor implements ValueExtractor<String, Constraint> {

	public String extract(Constraint constraint) {
		return constraint.sqlConstraint();
	}

}
