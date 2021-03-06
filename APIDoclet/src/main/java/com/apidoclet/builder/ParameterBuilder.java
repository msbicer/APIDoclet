package com.apidoclet.builder;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.AnnotationDesc.ElementValuePair;
import com.sun.javadoc.AnnotationTypeDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.ParamTag;
import com.sun.javadoc.Parameter;

public class ParameterBuilder {
	
	public static com.apidoclet.model.Parameter build(FieldDoc field){
		com.apidoclet.model.Parameter param = new com.apidoclet.model.Parameter();
		param.setName(field.name());
		param.setType(field.type().simpleTypeName());
		param.setQualifiedType(field.type().qualifiedTypeName());
		param.setDescription(field.commentText());
		return param;
	}
	
	public static com.apidoclet.model.Parameter build(Parameter param, ParamTag tag){
		com.apidoclet.model.Parameter parameter = new com.apidoclet.model.Parameter();
		
		parameter.setType(param.typeName());
		parameter.setQualifiedType(param.type().qualifiedTypeName());
		if (tag!=null){
			parameter.setDescription(tag.parameterComment());
		}
		//set default values
		parameter.setName(param.name());
		parameter.setDefaultValue(null);
		parameter.setRequired(true);
		
		boolean isRequestParameter = false;
		AnnotationDesc[] annotations = param.annotations();
		for (AnnotationDesc ann:annotations){
			AnnotationTypeDoc type = ann.annotationType();
			String annotationName = type.qualifiedName();
			if (annotationName.equals("org.springframework.web.bind.annotation.PathVariable")
					|| annotationName.equals("org.springframework.web.bind.annotation.RequestParam")
					|| annotationName.equals("org.springframework.web.bind.annotation.RequestHeader")){
				isRequestParameter = true;
				
				if (annotationName.equals("org.springframework.web.bind.annotation.RequestHeader")){
					parameter.setHeader(true);
				}
				
				ElementValuePair[] values = ann.elementValues();
				for (ElementValuePair pair:values){
					String name = pair.element().name();
					String value = String.valueOf(pair.value().value());
					if ("value".equals(name)){
						parameter.setName(value);
					} else if ("required".equals(name)){
						parameter.setRequired(Boolean.valueOf(value));
					} else if ("defaultValue".equals(name)){
						parameter.setDefaultValue(value);
					}
				}
			}
		}
		
		if (parameter.getDefaultValue()!=null){
			parameter.setRequired(false);
		}
		
		if (isRequestParameter){
			return parameter;
		} else {
			return null;
		}
	}
}
