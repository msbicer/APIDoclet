package com.apidoclet.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.apidoclet.model.RequestMapping;
import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.AnnotationDesc.ElementValuePair;

public class BuilderUtils {
	public static RequestMapping resolveRequestMapping(AnnotationDesc annotation){
		RequestMapping mapping = new RequestMapping();
		
		ElementValuePair[] values = annotation.elementValues();
		for (ElementValuePair pair:values){
			String name = pair.element().name();
			String value = String.valueOf(pair.value());
			if ("value".equals(name)){
				mapping.setValue(parseAnnotationValueList(value));
			} else if ("method".equals(name)){
				List<String> methods = parseAnnotationValueList(value);
				for (int i=0;i<methods.size();i++){
					String method = methods.get(i);
					method = method.substring(method.lastIndexOf(".")+1);
					methods.set(i, method);
				}
				
				mapping.setMethod(methods);
			}
		}
		return mapping;
	}
	
	public static List<String> parseAnnotationValueList(String value){
		if (value == null || value.length()<2){
			return Arrays.asList(value);
		}
		if (value.charAt(0) != '{' || value.charAt(value.length()-1) != '}'){
			if (value.charAt(0) == '"' && value.charAt(value.length()-1) == '"'){
				return Arrays.asList(value.substring(1, value.length()-1));
			} else {
				return Arrays.asList(value);
			}
			
		}
		
		String trimmed = value.substring(1, value.length()-1);
		String[] parts = trimmed.split(",");
		
		List<String> values = new ArrayList<String>();
		for (String part:parts){
			part = part.trim();
			if (part.charAt(0) == '"' && part.charAt(part.length()-1) == '"'){
				values.add(part.substring(1, part.length()-1));
			} else {
				values.add(part);
			}
		}
		
		return values;
	}
}
