package com.gmail.gulino.marco.easyshopper.utility;

import com.gmail.gulino.marco.easyshopper.utility.CollectionUtils.ValueExtractor;

public class ToStringExtractor<T> implements ValueExtractor<String, T> {

	public String extract(T object) {
		return object.toString();
	}

}
