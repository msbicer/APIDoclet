package com.apidoclet.util;

public class Utils {
	public static String toString(Object[] array) {
		return toString(array, ", ");
	}
	
	public static String toString(Object[] array, String separator) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			sb.append(array[i].toString());
			if (i < array.length - 1) {
				sb.append(separator);
			}
		}
		return sb.toString();
	}
	
	public static <T> T nvl(T val1, T val2){
		return val1 == null ? val2 : val1;
	}
}
