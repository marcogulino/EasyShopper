package com.google.code.easyshopper;

import android.util.Log;

public class Logger {
	public static void d(Object object, String method, String message) {
		Logger.dbg(object.getClass(), method , message);
	}
	
	public static void d(Object object, String method, String message, Throwable throwable) {
		Logger.dbg(object.getClass(), method , message, throwable);
	}
	
	@SuppressWarnings("rawtypes")
	public static void dbg(Class clazz, String method, String message) {
		Log.d(ES.APP_NAME + "/" + clazz.getName() + "::" + method, message);
	}

	@SuppressWarnings("rawtypes")
	public static void dbg(Class clazz, String method, String message, Throwable throwable) {
		Log.d(ES.APP_NAME + "/" + clazz.getName() + "::" + method, message, throwable);
	}
}
