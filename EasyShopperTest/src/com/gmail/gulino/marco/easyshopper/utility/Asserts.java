package com.gmail.gulino.marco.easyshopper.utility;

import static junit.framework.Assert.fail;
import android.util.Log;
public class Asserts {

	public static <T> void assertArrayEquals(T[] firstArray, T[] secondArray) {
		if(firstArray.length != secondArray.length) {
			fail(arraysToString(firstArray, secondArray));
		}
		
		for(int i=0; i<firstArray.length; i++) {
			T first = firstArray[i];
			T second = secondArray[i];
			Log.d("*********", "index " + i + ": comparing " + first + " to " + second);
			if(! areEquals(first, second)) {
				fail(arraysToString(firstArray, secondArray, i));
			}
		}
		
	}
	
	private static <T> boolean areEquals(T first, T second) {
		if( (first==null && second!= null) || (first!=null && second==null) ) return false;
		if(first==null && second==null) return true;
		if(first==second) return true;
		if(first.equals(second)) return true;
		return false;
	}
	
	private static <T> String arraysToString(T[] firstArray, T[] secondArray) {
		T[] greaterArray=firstArray.length>secondArray.length?firstArray:secondArray;
		T[] smallerArray=firstArray.length>secondArray.length?secondArray:firstArray;
		return arraysToString(greaterArray, smallerArray, -1);
	}


	private static <T> String arraysToString(T[] firstArray, T[] secondArray, int failingIndex) {
		StringBuilder result=new StringBuilder();
		for(int i=0; i<firstArray.length; i++) {
			if(i==failingIndex) result.append("*");
			result.append("[").append(String.valueOf(firstArray[i])).append("] --- [");
			if(secondArray.length>=i+1) {
				result.append(String.valueOf(secondArray[i]));
			} else {
				result.append("<not present>");
			}
			result.append("]");
		}
		
		return result.toString();
	}
}
