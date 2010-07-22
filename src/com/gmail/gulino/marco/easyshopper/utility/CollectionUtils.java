package com.gmail.gulino.marco.easyshopper.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CollectionUtils {
	public interface ValueExtractor<Vtype, Vobject> {
		public Vtype extract(Vobject object);
	}
	
	public interface Extractor<T> {
		public boolean extract(T object);
	}

	public static <Vtype, Vobject> Vtype firstValueFrom(ValueExtractor<Vtype, Vobject> valueExtractor, Vobject ... objects) {
		for (Vobject vobject : objects) {
			Vtype value=valueExtractor.extract(vobject);
			if(value!=null) return value;
		}
		return null;
	}

	public static <A> List<A> extract(List<A> values, Extractor<A> extractor) {
		ArrayList<A> arrayList = new ArrayList<A>();
		for (A a : values) {
			if(extractor.extract(a)) {
				arrayList.add(a);
			}
		}
		return arrayList;
	}
	
	public static <A, B> List<B> sublist(List<A> parentList, ValueExtractor<B, A> extractor) {
		ArrayList<B> arrayList = new ArrayList<B>();
		for (A a : parentList) {
			arrayList.add(extractor.extract(a));
		}
		return arrayList;
	}

	public static <A, B> List<B> sublist(A[] parentArray, ValueExtractor<B, A> extractor) {
		return sublist(Arrays.asList(parentArray), extractor);
	}

}
