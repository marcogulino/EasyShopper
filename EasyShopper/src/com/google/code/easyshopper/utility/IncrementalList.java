package com.google.code.easyshopper.utility;

import java.util.ArrayList;
import java.util.List;

public class IncrementalList<T> extends ArrayList<T> {
	private static final long serialVersionUID = 622993901152702713L;

	public IncrementalList<T> put(T element) {
		add(element);
		return this;
	}

	public List<T> putAll(T[] elements) {
		for (T t : elements) {
			add(t);
		}
		return this;
	}
}
