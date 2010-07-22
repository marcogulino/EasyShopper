package com.google.code.easyshopper.utility;

import com.google.code.easyshopper.utility.CollectionUtils.ValueExtractor;

public class ToStringExtractor<T> implements ValueExtractor<String, T> {

	public String extract(T object) {
		return object.toString();
	}

}
