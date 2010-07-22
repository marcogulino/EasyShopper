package com.gmail.gulino.marco.easyshopper.utility;

import android.test.AndroidTestCase;

import com.gmail.gulino.marco.easyshopper.utility.CollectionUtils.ValueExtractor;

public class CollectionUtilsTest extends AndroidTestCase {
	private static class TestClass {
		private final String value;

		public TestClass(String value) {
			this.value = value;
		}
		public String getValue() {
			return value;
		}
	}
	
	private class TestClassValueExtractor implements ValueExtractor<String, TestClass> {

		public String extract(TestClass object) {
			return object.getValue();
		}
		
	}

	public void testFirstValueFrom() {
		assertEquals("a", CollectionUtils.firstValueFrom(new TestClassValueExtractor(), new TestClass(null), new TestClass("a"), new TestClass("b")));
		assertEquals("b", CollectionUtils.firstValueFrom(new TestClassValueExtractor(), new TestClass(null), new TestClass(null), new TestClass("b")));
		assertEquals("1", CollectionUtils.firstValueFrom(new TestClassValueExtractor(), new TestClass("1"), new TestClass(null), new TestClass("b")));
	}
}
