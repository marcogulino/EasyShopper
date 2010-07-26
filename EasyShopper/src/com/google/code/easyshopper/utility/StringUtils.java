package com.google.code.easyshopper.utility;

import java.util.List;

public class StringUtils {

	public static String join(List<String> strings, String separator) {
		String result="";
		for (String string : strings) {
			if(result.length() >0 && string.length()>0) {
				result+=separator;
			}
			result+=string;
		}
		return result;
	}

	public static String join(String[] strings, String separator) {
		return join(new IncrementalList<String>().putAll(strings), separator);
	}

}
